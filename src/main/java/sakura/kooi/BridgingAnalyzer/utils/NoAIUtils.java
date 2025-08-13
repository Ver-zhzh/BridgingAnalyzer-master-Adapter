package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

/**
 * Cross-version NoAI utility
 * - Uses reflection to avoid linking to classes missing in 1.8 (e.g., org.bukkit.entity.Mob)
 * - Falls back to NMS NBT NoAI flag on legacy versions
 */
public class NoAIUtils {

    // Modern API reflection (to avoid NoClassDefFoundError on 1.8)
    private static Class<?> mobClass; // org.bukkit.entity.Mob (1.14+)
    private static Method mobSetAware; // Mob#setAware(boolean)
    private static Method livingSetAI; // LivingEntity#setAI(boolean) (1.9+)

    // Legacy NMS reflection (1.8.x)
    private static Class<?> craftEntityClass;
    private static Method getHandleMethod;
    private static Class<?> nmsEntityClass;
    private static Method getNbtTagMethod; // Entity#getNBTTag
    private static Class<?> nbtTagCompoundClass;
    private static Method nbtSetIntMethod; // NBTTagCompound#setInt
    private static Method entityReadTagMethod; // Entity#c(NBTTagCompound)
    private static Method entitySetTagMethod; // Entity#f(NBTTagCompound)

    static {
        // Try load modern API reflectively
        try {
            mobClass = Class.forName("org.bukkit.entity.Mob");
            mobSetAware = mobClass.getMethod("setAware", boolean.class);
        } catch (Throwable ignored) { /* not available on 1.8 */ }

        try {
            livingSetAI = LivingEntity.class.getMethod("setAI", boolean.class);
        } catch (Throwable ignored) { /* not available on 1.8 */ }

        // Prepare legacy NMS reflection for 1.8.x
        try {
            String nmsver = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            craftEntityClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftEntity");
            getHandleMethod = craftEntityClass.getDeclaredMethod("getHandle");

            nmsEntityClass = Class.forName("net.minecraft.server." + nmsver + ".Entity");
            getNbtTagMethod = nmsEntityClass.getDeclaredMethod("getNBTTag");

            nbtTagCompoundClass = Class.forName("net.minecraft.server." + nmsver + ".NBTTagCompound");
            nbtSetIntMethod = nbtTagCompoundClass.getDeclaredMethod("setInt", String.class, int.class);

            entityReadTagMethod = nmsEntityClass.getDeclaredMethod("c", nbtTagCompoundClass);
            entitySetTagMethod = nmsEntityClass.getDeclaredMethod("f", nbtTagCompoundClass);
        } catch (Throwable ignored) { /* not 1.8 or methods changed; we'll just skip legacy path */ }
    }

    /**
     * Set AI state for an entity in a cross-version safe way
     */
    public static void setAI(Entity bukkitEntity, boolean hasAI) {
        // 1) Try modern Mob#setAware
        try {
            if (mobClass != null && mobClass.isInstance(bukkitEntity) && mobSetAware != null) {
                mobSetAware.invoke(bukkitEntity, hasAI);
                return;
            }
        } catch (Throwable ignored) { }

        // 2) Try LivingEntity#setAI
        try {
            if (bukkitEntity instanceof LivingEntity && livingSetAI != null) {
                livingSetAI.invoke((LivingEntity) bukkitEntity, hasAI);
                return;
            }
        } catch (Throwable ignored) { }

        // 3) Legacy 1.8: set NBT NoAI
        try {
            if (craftEntityClass != null && getHandleMethod != null && getNbtTagMethod != null &&
                nbtTagCompoundClass != null && nbtSetIntMethod != null &&
                entityReadTagMethod != null && entitySetTagMethod != null) {

                Object objCraftEntity = craftEntityClass.cast(bukkitEntity);
                Object objNMSEntity = getHandleMethod.invoke(objCraftEntity);
                Object objNBTTag = getNbtTagMethod.invoke(objNMSEntity);
                if (objNBTTag == null) objNBTTag = nbtTagCompoundClass.getConstructor().newInstance();

                entityReadTagMethod.invoke(objNMSEntity, objNBTTag);
                nbtSetIntMethod.invoke(objNBTTag, "NoAI", hasAI ? 0 : 1);
                entitySetTagMethod.invoke(objNMSEntity, objNBTTag);
            }
        } catch (Throwable ignored) { }
    }
}
