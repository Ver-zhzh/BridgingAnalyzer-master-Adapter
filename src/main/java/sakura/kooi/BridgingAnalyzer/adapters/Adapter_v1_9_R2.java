package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

import java.lang.reflect.Method;

/**
 * Version adapter for Minecraft 1.9.4 (v1_9_R2)
 * Has Particle API but no native Title API
 * 
 * @author Ver_zhzh
 */
public class Adapter_v1_9_R2 implements VersionAdapter {
    
    @Override
    public String getSupportedVersion() {
        return "v1_9_R2";
    }
    
    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // 1.9.4 doesn't have native title API, try Spigot method
            Method sendTitleMethod = player.getClass().getMethod("sendTitle", String.class, String.class);
            sendTitleMethod.invoke(player, title, subtitle);
        } catch (Exception e) {
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
            // Try Spigot ActionBar API
            player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message)
            );
        } catch (Exception e) {
            // Fallback: send as chat message
            player.sendMessage("§e[ActionBar] " + message);
        }
    }
    
    @Override
    public void spawnParticle(Location location, String particleType, int count) {
        try {
            // 1.9.4 has Particle API
            Particle particle = getParticleFromName(particleType);
            if (particle != null) {
                location.getWorld().spawnParticle(particle, location, count, 0, 0, 0, 0);
            }
        } catch (Exception e) {
            // Fallback to legacy effects
            try {
                org.bukkit.Effect effect = getEffectFromParticle(particleType);
                if (effect != null) {
                    location.getWorld().playEffect(location, effect, count);
                }
            } catch (Exception e2) {
                // Ignore particle errors
            }
        }
    }
    
    @Override
    public Material getMaterial(String materialName) {
        // Handle legacy material names for 1.9.4
        switch (materialName) {
            case Materials.GOLDEN_PICKAXE: return Material.valueOf("GOLD_PICKAXE");
            case Materials.SMOOTH_SANDSTONE: return Material.valueOf("SANDSTONE");
            case Materials.FIREWORK_ROCKET: return Material.valueOf("FIREWORK");
            case Materials.GLASS_PANE: return Material.valueOf("THIN_GLASS");
            case Materials.MELON_BLOCK: return Material.valueOf("MELON_BLOCK");
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
        // Handle legacy sound names for 1.9.4
        switch (soundName) {
            case Sounds.ENTITY_PLAYER_LEVELUP: return Sound.valueOf("ENTITY_PLAYER_LEVELUP");
            case Sounds.ENTITY_EXPERIENCE_ORB_PICKUP: return Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP");
            case Sounds.ENTITY_ENDERMEN_TELEPORT: return Sound.valueOf("ENTITY_ENDERMEN_TELEPORT");
            case Sounds.ENTITY_ITEM_PICKUP: return Sound.valueOf("ENTITY_ITEM_PICKUP");
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
            case Features.PARTICLE_API: return true; // Has Particle API
            case Features.MODERN_MATERIALS: return false; // Legacy materials
            case Features.ACTION_BAR: return true; // Via Spigot
            case Features.SPIGOT_API: return true;
            default: return false;
        }
    }
    
    /**
     * Convert particle name to 1.9.4 Particle enum
     */
    private Particle getParticleFromName(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK: return Particle.valueOf("FIREWORKS_SPARK");
                case Particles.WITCH: return Particle.valueOf("SPELL_WITCH");
                case Particles.PORTAL: return Particle.valueOf("PORTAL");
                case Particles.FLAME: return Particle.valueOf("FLAME");
                default: return Particle.valueOf(particleType);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Convert particle name to legacy Effect (fallback)
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
