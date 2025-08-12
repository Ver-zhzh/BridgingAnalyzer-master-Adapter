package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.entity.Player;

/**
 * Modern ActionBar utility using Spigot native API for Minecraft 1.21
 * Implements real ActionBar functionality
 */
public class ActionBarUtils {

    /**
     * Send action bar message to a player using Spigot's native method
     * @param player The player to send the action bar to
     * @param message The message to display (supports color codes with §)
     */
    public static void sendActionBar(Player player, String message) {
        try {
            // Use version adapter for cross-version compatibility
            sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().sendActionBar(player, message);
        } catch (Exception e) {
            // Final fallback: send as chat message
            player.sendMessage("§e[ActionBar] " + message);
        }
    }

    /**
     * Clear the action bar for a player
     * @param player The player to clear the action bar for
     */
    public static void clearActionBar(Player player) {
        sendActionBar(player, "");
    }
}