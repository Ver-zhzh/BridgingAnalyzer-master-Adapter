package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Modern title utility using Bukkit native API for Minecraft 1.21
 * Replaces the old NMS-based implementation
 */
public class TitleUtils {

    /**
     * Send title and subtitle to a player using Bukkit native API
     * @param player The player to send the title to
     * @param title The main title text
     * @param subtitle The subtitle text
     * @param fadeIn Fade in time in ticks
     * @param stay Stay time in ticks
     * @param fadeOut Fade out time in ticks
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            // Use version adapter for cross-version compatibility
            sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception e) {
            // Ultimate fallback: send as chat message
            if (title != null && !title.isEmpty()) {
                player.sendMessage("§6[Title] " + title);
            }
            if (subtitle != null && !subtitle.isEmpty()) {
                player.sendMessage("§7[Subtitle] " + subtitle);
            }
        }
    }

    /**
     * Broadcast title and subtitle to all online players
     * @param title The main title text
     * @param subtitle The subtitle text
     * @param fadeIn Fade in time in ticks
     * @param stay Stay time in ticks
     * @param fadeOut Fade out time in ticks
     */
    public static void boardcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendTitle(p, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    /**
     * Send a simple title with default timing
     * @param player The player to send the title to
     * @param title The main title text
     * @param subtitle The subtitle text
     */
    public static void sendSimpleTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    /**
     * Clear the title for a player
     * @param player The player to clear the title for
     */
    public static void clearTitle(Player player) {
        try {
            player.resetTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
