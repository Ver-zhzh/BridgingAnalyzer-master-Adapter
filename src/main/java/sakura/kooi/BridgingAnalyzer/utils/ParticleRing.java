package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

public abstract class ParticleRing {
    // Pre-calculated circle coordinates for performance optimization
    private static final int CIRCLE_ELEMENTS = 20;
    private static final double RADIUS = 1.0;
    private static final double[] CIRCLE_X = new double[CIRCLE_ELEMENTS];
    private static final double[] CIRCLE_Z = new double[CIRCLE_ELEMENTS];

    static {
        // Pre-calculate circle coordinates to avoid repeated trigonometric calculations
        for (int i = 0; i < CIRCLE_ELEMENTS; i++) {
            double alpha = 360.0 / CIRCLE_ELEMENTS * i;
            CIRCLE_X[i] = RADIUS * Math.sin(Math.toRadians(alpha));
            CIRCLE_Z[i] = RADIUS * Math.cos(Math.toRadians(alpha));
        }
    }

    // Cross-version constructor using ParticleManager for compatibility
    public ParticleRing(Location centerLoc, String particleType, long delay) {
        // Use pre-calculated coordinates for better performance
        ParticleManager particleManager = ParticleManager.getInstance();
        if (particleManager.isInitialized()) {
            for (int i = 0; i < CIRCLE_ELEMENTS; i++) {
                Location particleLocation = new Location(centerLoc.getWorld(),
                    centerLoc.getX() + CIRCLE_X[i],
                    centerLoc.getY(),
                    centerLoc.getZ() + CIRCLE_Z[i]);
                particleManager.spawnParticle(particleLocation, particleType, 1);
            }
        }
        Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), this::onFinish, delay);
    }

    public abstract void onFinish();
}
