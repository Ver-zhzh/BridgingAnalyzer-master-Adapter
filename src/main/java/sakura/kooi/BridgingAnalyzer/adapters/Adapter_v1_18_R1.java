package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

/**
 * Version adapter for Minecraft 1.18.1 (v1_18_R1)
 * Supports Caves & Cliffs Part II features and modern material system
 * Uses modern Bukkit API with BlockData support
 * 
 * @author Ver_zhzh
 */
public class Adapter_v1_18_R1 implements VersionAdapter {
    
    @Override
    public String getSupportedVersion() {
        return "v1_18_R1";
    }
    
    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // 1.18 has native Title API
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
        // 1.18 uses modern flattened material names with deepslate additions
        try {
            return Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    @Override
    public Sound getSound(String soundName) {
        // Handle sound name changes in 1.18
        switch (soundName) {
            case Sounds.ENTITY_ENDERMEN_TELEPORT: return Sound.valueOf("ENTITY_ENDERMAN_TELEPORT");
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
        // 1.18+ ignores data parameter (use modern BlockData instead)
        return new ItemStack(material, amount);
    }
    
    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        // 1.18+ uses modern BlockData API
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
     * Map abstract particle names to 1.18 particle enum names.
     * 1.18 maintains compatibility with previous modern versions.
     */
    private Particle getParticleFromName(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK:
                    // In 1.18, FIREWORKS_SPARK is still the correct name
                    return Particle.valueOf("FIREWORKS_SPARK");
                case Particles.WITCH:
                    // 1.18 uses SPELL_WITCH
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
