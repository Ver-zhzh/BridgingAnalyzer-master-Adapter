package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

/**
 * Version adapter for Minecraft 1.21 (v1_21_R1)
 * Uses modern Bukkit API with full feature support
 * 
 * @author Ver_zhzh
 */
public class Adapter_v1_21_R1 implements VersionAdapter {
    
    @Override
    public String getSupportedVersion() {
        return "v1_21_R1";
    }
    
    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // Use modern Bukkit Title API
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // Try Spigot ActionBar API first
            player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message)
            );
        } catch (Exception e) {
            try {
                // Fallback: try reflection for sendActionBar
                player.getClass().getMethod("sendActionBar", String.class).invoke(player, message);
            } catch (Exception e2) {
                // Final fallback: send as chat message
                player.sendMessage("§e[ActionBar] " + message);
            }
        }
    }
    
    @Override
    public void spawnParticle(Location location, String particleType, int count) {
        try {
            Particle particle = getParticleFromName(particleType);
            if (particle != null) {
                location.getWorld().spawnParticle(particle, location, count, 0, 0, 0, 0);
            }
        } catch (Exception e) {
            // Ignore particle errors
        }
    }
    
    @Override
    public Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public Sound getSound(String soundName) {
        try {
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public ItemStack createItemStack(Material material, int amount, short data) {
        // In 1.21, data parameter is ignored (use modern BlockData instead)
        return new ItemStack(material, amount);
    }
    
    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        // Use modern BlockData API
        player.sendBlockChange(location, material.createBlockData());
    }
    
    @Override
    public boolean supportsFeature(String feature) {
        switch (feature) {
            case Features.TITLE_API: return true;
            case Features.PARTICLE_API: return true;
            case Features.MODERN_MATERIALS: return true;
            case Features.ACTION_BAR: return true;
            case Features.SPIGOT_API: return true;
            default: return false;
        }
    }
    
    /**
     * Convert particle name to modern Particle enum
     */
    private Particle getParticleFromName(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK:
                    // Try modern name first, fallback to legacy
                    try {
                        return Particle.valueOf("FIREWORK");
                    } catch (IllegalArgumentException e) {
                        return Particle.valueOf("FIREWORKS_SPARK");
                    }
                case Particles.WITCH:
                    // Try modern name first, fallback to legacy
                    try {
                        return Particle.valueOf("WITCH");
                    } catch (IllegalArgumentException e) {
                        return Particle.valueOf("SPELL_WITCH");
                    }
                case Particles.PORTAL:
                    return Particle.valueOf("PORTAL");
                case Particles.FLAME:
                    return Particle.valueOf("FLAME");
                default:
                    return Particle.valueOf(particleType);
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
