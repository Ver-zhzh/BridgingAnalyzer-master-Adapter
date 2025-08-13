package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

/**
 * Version adapter for Minecraft 1.14.4 (v1_14_R1)
 * First version with modern material system (flattening) after 1.13
 * Uses modern Bukkit API with BlockData support
 * 
 * @author Ver_zhzh
 */
public class Adapter_v1_14_R1 implements VersionAdapter {
    
    @Override
    public String getSupportedVersion() {
        return "v1_14_R1";
    }
    
    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // 1.14 has native Title API
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
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
        // 1.14 uses modern flattened material names
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
        // 1.14+ ignores data parameter (use modern BlockData instead)
        return new ItemStack(material, amount);
    }
    
    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        // 1.14+ uses modern BlockData API
        player.sendBlockChange(location, material.createBlockData());
    }
    
    @Override
    public boolean supportsFeature(String feature) {
        switch (feature) {
            case Features.TITLE_API:
            case Features.PARTICLE_API:
            case Features.MODERN_MATERIALS:
            case Features.ACTION_BAR:
            case Features.SPIGOT_API:
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Map abstract particle names to 1.14 particle enum names.
     * 1.14 introduced some particle name changes compared to earlier versions.
     */
    private Particle getParticleFromName(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK:
                    // In 1.14, FIREWORKS_SPARK is the correct name
                    return Particle.valueOf("FIREWORKS_SPARK");
                case Particles.WITCH:
                    // 1.14 uses SPELL_WITCH
                    return Particle.valueOf("SPELL_WITCH");
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
