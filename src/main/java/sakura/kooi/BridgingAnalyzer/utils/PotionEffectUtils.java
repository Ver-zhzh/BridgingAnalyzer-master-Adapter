package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public final class PotionEffectUtils {

    private PotionEffectUtils() {
    }

    public static PotionEffectType getEffectType(String... names) {
        for (String name : names) {
            PotionEffectType type = PotionEffectType.getByName(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    public static void applyEffect(LivingEntity entity, String[] typeNames, int duration, int amplifier) {
        PotionEffectType type = getEffectType(typeNames);
        if (type == null || entity == null) {
            return;
        }
        entity.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false), true);
    }

    public static void setMaxHealth(LivingEntity entity, double health) {
        if (entity == null) {
            return;
        }

        try {
            Method setMaxHealth = entity.getClass().getMethod("setMaxHealth", double.class);
            setMaxHealth.invoke(entity, health);
            entity.setHealth(Math.min(entity.getHealth(), health));
            return;
        } catch (Throwable ignored) {
        }

        try {
            Class<?> attributeClass = Class.forName("org.bukkit.attribute.Attribute");
            Object maxHealthAttribute = Enum.valueOf((Class<Enum>) attributeClass, "GENERIC_MAX_HEALTH");
            Object attributeInstance = entity.getClass()
                    .getMethod("getAttribute", attributeClass)
                    .invoke(entity, maxHealthAttribute);
            if (attributeInstance != null) {
                attributeInstance.getClass().getMethod("setBaseValue", double.class).invoke(attributeInstance, health);
                entity.setHealth(Math.min(entity.getHealth(), health));
            }
        } catch (Throwable ignored) {
            entity.setHealth(Math.min(entity.getHealth(), health));
        }
    }
}
