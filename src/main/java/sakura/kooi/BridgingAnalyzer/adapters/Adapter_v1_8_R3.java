package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Version adapter for Minecraft 1.8.8 (v1_8_R3)
 * Uses reflection for NMS access and legacy API handling
 * 
 * @author Ver_zhzh
 */
public class Adapter_v1_8_R3 implements VersionAdapter {
    
    // Cached reflection objects
    private static Class<?> craftPlayerClass;
    private static Class<?> packetPlayOutTitleClass;
    private static Class<?> iChatBaseComponentClass;
    private static Class<?> enumTitleActionClass;
    private static Method getHandleMethod;
    private static Method sendPacketMethod;
    private static Method chatSerializerMethod;
    private static Field playerConnectionField;
    private static Object enumTitleAction;
    private static Object enumSubtitleAction;
    
    static {
        try {
            // Initialize reflection objects for 1.8.8 (following original project approach)
            String nmsVersion = "v1_8_R3";
            String nmsPackage = "net.minecraft.server." + nmsVersion;
            String craftPackage = "org.bukkit.craftbukkit." + nmsVersion;

            System.out.println("[BridgingAnalyzer] Initializing NMS reflection for " + nmsVersion);

            craftPlayerClass = Class.forName(craftPackage + ".entity.CraftPlayer");
            System.out.println("[BridgingAnalyzer] Found CraftPlayer class");

            packetPlayOutTitleClass = Class.forName(nmsPackage + ".PacketPlayOutTitle");
            System.out.println("[BridgingAnalyzer] Found PacketPlayOutTitle class");

            iChatBaseComponentClass = Class.forName(nmsPackage + ".IChatBaseComponent");
            System.out.println("[BridgingAnalyzer] Found IChatBaseComponent class");

            enumTitleActionClass = Class.forName(nmsPackage + ".PacketPlayOutTitle$EnumTitleAction");
            System.out.println("[BridgingAnalyzer] Found EnumTitleAction class");

            // Get ChatSerializer method (original project approach)
            Class<?> chatSerializerClass = iChatBaseComponentClass.getDeclaredClasses()[0];
            chatSerializerMethod = chatSerializerClass.getMethod("a", String.class);
            System.out.println("[BridgingAnalyzer] Found ChatSerializer method");

            // Get enum values
            enumTitleAction = enumTitleActionClass.getField("TITLE").get(null);
            enumSubtitleAction = enumTitleActionClass.getField("SUBTITLE").get(null);
            System.out.println("[BridgingAnalyzer] Found enum values");

            getHandleMethod = craftPlayerClass.getMethod("getHandle");
            Class<?> entityPlayerClass = Class.forName(nmsPackage + ".EntityPlayer");
            playerConnectionField = entityPlayerClass.getField("playerConnection");
            Class<?> playerConnectionClass = Class.forName(nmsPackage + ".PlayerConnection");
            sendPacketMethod = playerConnectionClass.getMethod("sendPacket", Class.forName(nmsPackage + ".Packet"));

            System.out.println("[BridgingAnalyzer] NMS reflection initialized successfully!");

        } catch (Exception e) {
            System.err.println("[BridgingAnalyzer] Failed to initialize NMS reflection: " + e.getMessage());
            e.printStackTrace();

            // Set all to null to indicate failure
            craftPlayerClass = null;
            packetPlayOutTitleClass = null;
            iChatBaseComponentClass = null;
            enumTitleActionClass = null;
            getHandleMethod = null;
            sendPacketMethod = null;
            chatSerializerMethod = null;
            playerConnectionField = null;
            enumTitleAction = null;
            enumSubtitleAction = null;
        }
    }
    
    @Override
    public String getSupportedVersion() {
        return "v1_8_R3";
    }
    
    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        // Check if reflection objects are properly initialized
        if (craftPlayerClass == null || packetPlayOutTitleClass == null ||
            iChatBaseComponentClass == null || enumTitleActionClass == null ||
            getHandleMethod == null || sendPacketMethod == null || playerConnectionField == null ||
            chatSerializerMethod == null || enumTitleAction == null || enumSubtitleAction == null) {
            // Fallback: send as chat message if reflection failed
            if (title != null && !title.isEmpty()) {
                player.sendMessage("§6[Title] " + title);
            }
            if (subtitle != null && !subtitle.isEmpty()) {
                player.sendMessage("§7[Subtitle] " + subtitle);
            }
            return;
        }

        try {
            // 1.8.8 doesn't have native title API, use NMS (original project approach)
            Object craftPlayer = craftPlayerClass.cast(player);
            Object entityPlayer = getHandleMethod.invoke(craftPlayer);
            Object playerConnection = playerConnectionField.get(entityPlayer);

            // Always send both title and subtitle to clear previous state
            String safeTitle = (title == null) ? "" : title;
            String safeSubtitle = (subtitle == null) ? "" : subtitle;

            Object titleChat = chatSerializerMethod.invoke(null, "{\"text\":\"" + safeTitle + "\"}");
            Object titlePacket = packetPlayOutTitleClass.getConstructor(
                enumTitleActionClass,
                iChatBaseComponentClass,
                int.class, int.class, int.class
            ).newInstance(
                enumTitleAction,
                titleChat,
                fadeIn, stay, fadeOut
            );
            sendPacketMethod.invoke(playerConnection, titlePacket);

            Object subtitleChat = chatSerializerMethod.invoke(null, "{\"text\":\"" + safeSubtitle + "\"}");
            Object subtitlePacket = packetPlayOutTitleClass.getConstructor(
                enumTitleActionClass,
                iChatBaseComponentClass,
                int.class, int.class, int.class
            ).newInstance(
                enumSubtitleAction,
                subtitleChat,
                fadeIn, stay, fadeOut
            );
            sendPacketMethod.invoke(playerConnection, subtitlePacket);

        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("[BridgingAnalyzer] Title NMS error: " + e.getMessage());
            e.printStackTrace();

            // Fallback: send as chat message
            if (title != null && !title.isEmpty()) {
                player.sendMessage("§6[Title] " + title);
            }
            if (subtitle != null && !subtitle.isEmpty()) {
                player.sendMessage("§7[Subtitle] " + subtitle);
            }
        }
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // 1.8.8 ActionBar via NMS
            Object craftPlayer = craftPlayerClass.cast(player);
            Object entityPlayer = getHandleMethod.invoke(craftPlayer);
            Object playerConnection = playerConnectionField.get(entityPlayer);
            
            // Create message component using JSON format for consistency
            Object messageComponent = chatSerializerMethod.invoke(null, "{\"text\":\"" + message + "\"}");
            
            // Use PacketPlayOutChat with action bar type (byte 2)
            String nmsPackage = "net.minecraft.server.v1_8_R3";
            Class<?> packetPlayOutChatClass = Class.forName(nmsPackage + ".PacketPlayOutChat");
            Object chatPacket = packetPlayOutChatClass.getConstructor(
                Class.forName(nmsPackage + ".IChatBaseComponent"), 
                byte.class
            ).newInstance(messageComponent, (byte) 2);
            
            sendPacketMethod.invoke(playerConnection, chatPacket);
            
        } catch (Exception e) {
            // Fallback: send as chat message
            player.sendMessage("§e[ActionBar] " + message);
        }
    }
    
    @Override
    public void spawnParticle(Location location, String particleType, int count) {
        // 1.8.8 doesn't have Particle API, use legacy effect
        try {
            org.bukkit.Effect effect = getEffectFromParticle(particleType);
            if (effect != null) {
                location.getWorld().playEffect(location, effect, count);
            }
        } catch (Exception e) {
            // Ignore particle errors in 1.8.8
        }
    }
    
    @Override
    public Material getMaterial(String materialName) {
        // Handle legacy material names for 1.8.8
        switch (materialName) {
            case Materials.GOLDEN_PICKAXE: return Material.valueOf("GOLD_PICKAXE");
            case Materials.SMOOTH_SANDSTONE: return Material.valueOf("SANDSTONE");
            case Materials.FIREWORK_ROCKET: return Material.valueOf("FIREWORK");
            case "STONE_BRICKS": return Material.valueOf("SMOOTH_BRICK");
            case "LIGHT_WEIGHTED_PRESSURE_PLATE": return Material.valueOf("GOLD_PLATE");
            case "HEAVY_WEIGHTED_PRESSURE_PLATE": return Material.valueOf("IRON_PLATE");
            case "OAK_PRESSURE_PLATE": return Material.valueOf("WOOD_PLATE");
            case "STONE_PRESSURE_PLATE": return Material.valueOf("STONE_PLATE");
            default:
                try {
                    return Material.valueOf(materialName);
                } catch (IllegalArgumentException e) {
                    return null;
                }
        }
    }
    
    @Override
    public Sound getSound(String soundName) {
        // Handle legacy sound names
        switch (soundName) {
            case Sounds.ENTITY_PLAYER_LEVELUP: return Sound.valueOf("LEVEL_UP");
            case Sounds.ENTITY_EXPERIENCE_ORB_PICKUP: return Sound.valueOf("ORB_PICKUP");
            case Sounds.ENTITY_ENDERMEN_TELEPORT: return Sound.valueOf("ENDERMAN_TELEPORT");
            case Sounds.ENTITY_ITEM_PICKUP: return Sound.valueOf("ITEM_PICKUP");
            default:
                try {
                    return Sound.valueOf(soundName);
                } catch (IllegalArgumentException e) {
                    return null;
                }
        }
    }
    
    @Override
    public ItemStack createItemStack(Material material, int amount, short data) {
        return new ItemStack(material, amount, data);
    }
    
    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        player.sendBlockChange(location, material, data);
    }
    
    @Override
    public boolean supportsFeature(String feature) {
        switch (feature) {
            case Features.TITLE_API: return false; // No native API
            case Features.PARTICLE_API: return false; // No Particle API
            case Features.MODERN_MATERIALS: return false; // Legacy materials
            case Features.ACTION_BAR: return true; // Via NMS
            case Features.SPIGOT_API: return true; // Basic Spigot
            default: return false;
        }
    }
    
    /**
     * Convert particle name to legacy Effect
     */
    private org.bukkit.Effect getEffectFromParticle(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK: return org.bukkit.Effect.valueOf("FIREWORKS_SPARK");
                case Particles.WITCH: return org.bukkit.Effect.valueOf("POTION_SWIRL");
                case Particles.PORTAL: return org.bukkit.Effect.valueOf("ENDER_SIGNAL");
                case Particles.FLAME: return org.bukkit.Effect.valueOf("MOBSPAWNER_FLAMES");
                default: return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
