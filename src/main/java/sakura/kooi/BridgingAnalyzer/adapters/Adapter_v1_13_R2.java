package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

/**
 * Version adapter for Minecraft 1.13.2 (v1_13_R2)
 * Uses modern (flattened) Bukkit API introduced in 1.13
 * and provides safe mappings that exist on 1.13 runtimes.
 *
 * This adapter is intentionally reused by 1.14–1.20 via VersionManager fallback,
 * because the Bukkit API surface used here is stable across these versions.
 *
 * @author Ver_zhzh
 */
public class Adapter_v1_13_R2 implements VersionAdapter {

    @Override
    public String getSupportedVersion() {
        return "v1_13_R2";
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // 1.11+ native Title API
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception ignored) {
            // should not happen on 1.13+
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {
        try {
            // Spigot ActionBar API (present on 1.13)
            player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message)
            );
        } catch (Exception e) {
            // Final fallback: send as chat message
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
        } catch (Exception ignored) {
        }
    }

    @Override
    public Material getMaterial(String materialName) {
        // 1.13+ uses modern flattened material names
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
        // Data deprecated in 1.13+, ignore
        return new ItemStack(material, amount);
    }

    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        // 1.13+ BlockData API
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
     * Map abstract particle names to 1.13 particle enum names.
     * We avoid referencing enum constants directly to prevent NoSuchFieldError on cross-compiles.
     */
    private Particle getParticleFromName(String particleType) {
        try {
            switch (particleType) {
                case Particles.FIREWORK:
                    // In 1.13, FIREWORKS_SPARK is the firework particle name
                    return Particle.valueOf("FIREWORKS_SPARK");
                case Particles.WITCH:
                    // 1.13 keeps SPELL_WITCH
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

