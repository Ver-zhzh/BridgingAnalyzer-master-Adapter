package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.Bukkit;
import sakura.kooi.BridgingAnalyzer.BridgingAnalyzer;
import sakura.kooi.BridgingAnalyzer.adapters.UniversalAdapter;

/**
 * Loads a single universal adapter for all supported server versions.
 */
public class VersionManager {

    private static VersionAdapter adapter;
    private static ServerVersion serverVersion;
    private static String craftPackageVersion;
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) {
            return;
        }

        serverVersion = ServerVersion.current();
        craftPackageVersion = ServerVersion.detectCraftPackageVersion();
        adapter = new UniversalAdapter();
        initialized = true;

        BridgingAnalyzer.getInstance().getLogger().info("Detected server version: " + serverVersion.getDisplayVersion()
                + (serverVersion.isCalendarFormat() ? " (calendar format)" : " (legacy format)"));
        if (craftPackageVersion != null) {
            BridgingAnalyzer.getInstance().getLogger().info("CraftBukkit package: " + craftPackageVersion);
        }
        BridgingAnalyzer.getInstance().getLogger().info("Loaded universal adapter for 1.8.8 - 26.x+");
    }

    public static VersionAdapter getAdapter() {
        if (!initialized || adapter == null) {
            throw new IllegalStateException("VersionManager not initialized!");
        }
        return adapter;
    }

    public static ServerVersion getServerVersion() {
        return serverVersion;
    }

    public static String getServerVersionLabel() {
        return serverVersion == null ? "unknown" : serverVersion.getDisplayVersion();
    }

    public static boolean isModernServer() {
        return serverVersion != null && (serverVersion.isCalendarFormat() || serverVersion.isAtLeast(1, 20, 5));
    }

    public static class UnsupportedVersionException extends RuntimeException {
        public UnsupportedVersionException(String message) {
            super(message);
        }
    }
}
