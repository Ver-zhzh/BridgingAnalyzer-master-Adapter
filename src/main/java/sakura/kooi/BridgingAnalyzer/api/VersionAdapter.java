package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Version adapter interface for cross-version compatibility
 * Handles API differences between Minecraft versions 1.8.8 - 1.21
 * 
 * @author Ver_zhzh
 */
public interface VersionAdapter {
    
    /**
     * Get the server version this adapter supports
     * @return Version string (e.g., "v1_8_R3", "v1_21_R1")
     */
    String getSupportedVersion();
    
    /**
     * Send title to player with cross-version compatibility
     * @param player Target player
     * @param title Main title text
     * @param subtitle Subtitle text
     * @param fadeIn Fade in time in ticks
     * @param stay Stay time in ticks
     * @param fadeOut Fade out time in ticks
     */
    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
    
    /**
     * Send action bar message to player
     * @param player Target player
     * @param message Message to display
     */
    void sendActionBar(Player player, String message);
    
    /**
     * Spawn particle effect at location
     * @param location Target location
     * @param particleType Particle type name
     * @param count Number of particles
     */
    void spawnParticle(Location location, String particleType, int count);
    
    /**
     * Get material by name with version compatibility
     * @param materialName Material name (modern naming)
     * @return Material instance or null if not found
     */
    Material getMaterial(String materialName);
    
    /**
     * Get sound by name with version compatibility
     * @param soundName Sound name (modern naming)
     * @return Sound instance or null if not found
     */
    Sound getSound(String soundName);
    
    /**
     * Create ItemStack with version-specific handling
     * @param material Material type
     * @param amount Item amount
     * @param data Item data (for legacy versions)
     * @return ItemStack instance
     */
    ItemStack createItemStack(Material material, int amount, short data);
    
    /**
     * Send block change to player (for highlighting)
     * @param player Target player
     * @param location Block location
     * @param material Block material
     * @param data Block data (for legacy versions)
     */
    void sendBlockChange(Player player, Location location, Material material, byte data);
    
    /**
     * Check if this version supports a specific feature
     * @param feature Feature name
     * @return true if supported
     */
    boolean supportsFeature(String feature);
    
    /**
     * Get version-specific constants
     */
    interface Features {
        String TITLE_API = "title_api";           // 1.11+
        String PARTICLE_API = "particle_api";     // 1.9+
        String MODERN_MATERIALS = "modern_materials"; // 1.13+
        String ACTION_BAR = "action_bar";         // All versions (different implementations)
        String SPIGOT_API = "spigot_api";         // Spigot-specific features
    }
    
    /**
     * Common material mappings for cross-version compatibility
     */
    interface Materials {
        String GOLDEN_PICKAXE = "GOLDEN_PICKAXE";
        String SMOOTH_SANDSTONE = "SMOOTH_SANDSTONE";
        String FIREWORK_ROCKET = "FIREWORK_ROCKET";
        String EMERALD_BLOCK = "EMERALD_BLOCK";
        String REDSTONE_BLOCK = "REDSTONE_BLOCK";
        String LAPIS_BLOCK = "LAPIS_BLOCK";
        String MELON_BLOCK = "MELON_BLOCK";
        String BEACON = "BEACON";
        String SNOW_BLOCK = "SNOW_BLOCK";
    }
    
    /**
     * Common sound mappings for cross-version compatibility
     */
    interface Sounds {
        String ENTITY_PLAYER_LEVELUP = "ENTITY_PLAYER_LEVELUP";
        String ENTITY_EXPERIENCE_ORB_PICKUP = "ENTITY_EXPERIENCE_ORB_PICKUP";
        String ENTITY_ENDERMEN_TELEPORT = "ENTITY_ENDERMEN_TELEPORT";
        String ENTITY_ITEM_PICKUP = "ENTITY_ITEM_PICKUP";
    }
    
    /**
     * Common particle mappings for cross-version compatibility
     */
    interface Particles {
        String FIREWORK = "FIREWORK";
        String WITCH = "WITCH";
        String PORTAL = "PORTAL";
        String FLAME = "FLAME";
    }
}
