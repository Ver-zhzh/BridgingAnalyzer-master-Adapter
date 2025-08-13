package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Location;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

/**
 * Cross-version particle manager
 * Handles particle effects through version adapters to ensure compatibility
 * across Minecraft versions 1.8.8 - 1.21 (excluding 1.13)
 * Supported versions: 1.8.8, 1.9.4, 1.11.2, 1.12.2, 1.14.4+, 1.21+
 *
 * @author Ver_zhzh
 */
public class ParticleManager {
    
    private static ParticleManager instance;
    private VersionAdapter versionAdapter;
    
    private ParticleManager() {
        // Private constructor for singleton
    }
    
    /**
     * Get the singleton instance
     */
    public static ParticleManager getInstance() {
        if (instance == null) {
            instance = new ParticleManager();
        }
        return instance;
    }
    
    /**
     * Initialize the particle manager with version adapter
     * @param adapter The version adapter to use
     */
    public void initialize(VersionAdapter adapter) {
        this.versionAdapter = adapter;
    }
    
    /**
     * Spawn particle effect at location
     * @param location Target location
     * @param particleType Particle type (using constants)
     * @param count Number of particles
     */
    public void spawnParticle(Location location, String particleType, int count) {
        if (versionAdapter != null) {
            versionAdapter.spawnParticle(location, particleType, count);
        }
    }
    
    /**
     * Spawn particle ring effect
     * @param centerLoc Center location
     * @param particleType Particle type
     * @param radius Ring radius
     * @param points Number of points in the ring
     */
    public void spawnRing(Location centerLoc, String particleType, double radius, int points) {
        if (versionAdapter == null) return;
        
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = centerLoc.getX() + radius * Math.cos(angle);
            double z = centerLoc.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(centerLoc.getWorld(), x, centerLoc.getY(), z);
            
            versionAdapter.spawnParticle(particleLocation, particleType, 1);
        }
    }
    
    /**
     * Check if particle manager is initialized
     */
    public boolean isInitialized() {
        return versionAdapter != null;
    }
    
    /**
     * Particle type constants for cross-version compatibility
     */
    public static class ParticleTypes {
        public static final String CLOUD = "CLOUD";
        public static final String WITCH = "WITCH";
        public static final String FIREWORK = "FIREWORK";
        public static final String PORTAL = "PORTAL";
        public static final String FLAME = "FLAME";
    }
}
