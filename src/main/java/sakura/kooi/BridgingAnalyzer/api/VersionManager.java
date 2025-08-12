package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.Bukkit;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;

/**
 * Version manager for handling different Minecraft versions
 * Automatically detects server version and loads appropriate adapter
 * 
 * @author Ver_zhzh
 */
public class VersionManager {
    
    private static VersionAdapter adapter;
    private static String serverVersion;
    private static boolean initialized = false;
    
    /**
     * Initialize version manager and load appropriate adapter
     */
    public static void initialize() {
        if (initialized) return;
        
        try {
            serverVersion = detectServerVersion();
            BridgingAnalyzer.getInstance().getLogger().info("Detected server version: " + serverVersion);
            
            adapter = loadAdapter(serverVersion);
            
            if (adapter != null) {
                BridgingAnalyzer.getInstance().getLogger().info("Loaded version adapter: " + adapter.getClass().getSimpleName());
                initialized = true;
            } else {
                throw new UnsupportedVersionException("No adapter found for version: " + serverVersion);
            }
            
        } catch (Exception e) {
            BridgingAnalyzer.getInstance().getLogger().severe("Failed to initialize version manager: " + e.getMessage());
            throw new RuntimeException("Version initialization failed", e);
        }
    }
    
    /**
     * Get the current version adapter
     */
    public static VersionAdapter getAdapter() {
        if (!initialized || adapter == null) {
            throw new IllegalStateException("VersionManager not initialized!");
        }
        return adapter;
    }
    
    /**
     * Get detected server version
     */
    public static String getServerVersion() {
        return serverVersion;
    }
    
    /**
     * Detect server version from package name
     */
    private static String detectServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        
        if (version.startsWith("v")) {
            return version;
        } else {
            // Fallback: map from Bukkit version
            String bukkitVersion = Bukkit.getBukkitVersion();
            return mapBukkitVersionToNMS(bukkitVersion);
        }
    }
    
    /**
     * Map Bukkit version to NMS version
     */
    private static String mapBukkitVersionToNMS(String bukkitVersion) {
        if (bukkitVersion.contains("1.8.8")) return "v1_8_R3";
        if (bukkitVersion.contains("1.9.4")) return "v1_9_R2";
        if (bukkitVersion.contains("1.11.2")) return "v1_11_R1";
        if (bukkitVersion.contains("1.12.2")) return "v1_12_R1";
        // 1.13+ mappings (approximate to most common R-mappings)
        if (bukkitVersion.contains("1.13")) return "v1_13_R2";    // 1.13.2
        if (bukkitVersion.contains("1.14")) return "v1_14_R1";
        if (bukkitVersion.contains("1.15")) return "v1_15_R1";
        if (bukkitVersion.contains("1.16.5")) return "v1_16_R3";
        if (bukkitVersion.contains("1.18.1")) return "v1_18_R1";
        if (bukkitVersion.contains("1.19")) return "v1_19_R1";    // 1.19(.2) commonly R1
        if (bukkitVersion.contains("1.20.4")) return "v1_20_R3";
        if (bukkitVersion.contains("1.21")) return "v1_21_R1";
        return "unknown";
    }
    
    /**
     * Load version adapter for specific version
     */
    private static VersionAdapter loadAdapter(String version) {
        try {
            String adapterClassName = "sakura.kooi.BridgingAnalyzer.adapters.Adapter_" + version;
            Class<?> adapterClass = Class.forName(adapterClassName);
            return (VersionAdapter) adapterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            BridgingAnalyzer.getInstance().getLogger().warning("Failed to load adapter for " + version);
            return loadFallbackAdapter(version);
        }
    }
    
    /**
     * Load fallback adapter for unsupported versions
     */
    private static VersionAdapter loadFallbackAdapter(String version) {
        if (version.startsWith("v1_8")) return loadAdapter("v1_8_R3");
        if (version.startsWith("v1_9")) return loadAdapter("v1_9_R2");
        if (version.startsWith("v1_11")) return loadAdapter("v1_11_R1");
        if (version.startsWith("v1_12")) return loadAdapter("v1_12_R1");
        // Treat 1.13–1.20 as modern API compatible with 1.13 adapter implementation
        if (version.startsWith("v1_13") || version.startsWith("v1_14") || version.startsWith("v1_15")
                || version.startsWith("v1_16") || version.startsWith("v1_17") || version.startsWith("v1_18")
                || version.startsWith("v1_19") || version.startsWith("v1_20")) {
            return loadAdapter("v1_13_R2");
        }
        if (version.startsWith("v1_21")) return loadAdapter("v1_21_R1");
        return null;
    }
    
    /**
     * Exception for unsupported versions
     */
    public static class UnsupportedVersionException extends RuntimeException {
        public UnsupportedVersionException(String message) {
            super(message);
        }
    }
}
