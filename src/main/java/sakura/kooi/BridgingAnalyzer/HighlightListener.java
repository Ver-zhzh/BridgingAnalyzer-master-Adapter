package sakura.kooi.BridgingAnalyzer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.Particle;

import java.util.HashMap;

public class HighlightListener implements Listener {
    private HashMap<Player, Block> highlightHistory = new HashMap<>();

    private Block getRelativeBrick(Block b) {
        Material stoneBricks = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial("STONE_BRICKS");
        if (stoneBricks == null) {
            try {
                stoneBricks = Material.valueOf("SMOOTH_BRICK");
            } catch (IllegalArgumentException e) {
                stoneBricks = Material.STONE;
            }
        }

        Block relative = b.getRelative(BlockFace.EAST);
        if (relative.getType() == stoneBricks) return relative;
        relative = b.getRelative(BlockFace.WEST);
        if (relative.getType() == stoneBricks) return relative;
        relative = b.getRelative(BlockFace.SOUTH);
        if (relative.getType() == stoneBricks) return relative;
        relative = b.getRelative(BlockFace.NORTH);
        if (relative.getType() == stoneBricks) return relative;
        return null;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onFallDown(PlayerMoveEvent e) {
        if (e.getTo().getY() < 0) {
            Block historyBlock = highlightHistory.get(e.getPlayer());
            if (historyBlock != null) {
                sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().sendBlockChange(
                    e.getPlayer(), historyBlock.getLocation(), historyBlock.getType(), (byte) 0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!BridgingAnalyzer.getCounter(e.getPlayer()).isHighlightEnabled()) return;
        if (e.getFrom().getBlock() != e.getTo().getBlock()) {
            Block target = getRelativeBrick(roundLocation(e.getTo().clone().add(0, -1, 0)).getBlock());
            if (target != null) {
                Block historyBlock = highlightHistory.get(e.getPlayer());
                if (historyBlock != null) {
                    sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().sendBlockChange(
                        e.getPlayer(), historyBlock.getLocation(), historyBlock.getType(), (byte) 0);
                }
                Material snowBlock = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.SNOW_BLOCK);
                if (snowBlock == null) {
                    snowBlock = Material.valueOf("SNOW_BLOCK");
                }
                sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().sendBlockChange(
                    e.getPlayer(), target.getLocation(), snowBlock, (byte) 0);
                highlightHistory.put(e.getPlayer(), target);
            }
        }
    }

    @EventHandler
    public void onStandBridgeMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            return;
        }
        if (!BridgingAnalyzer.getCounter(e.getPlayer()).isStandBridgeMarkerEnabled()) return;
        try {
            sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().spawnParticle(
                e.getTo().clone().add(0.08, 0.0, 0.08), "VILLAGER_HAPPY", 5);
        } catch (Exception ignored) {
        }
    }

    private Location roundLocation(Location location) {
        Location loc = location.clone();
        loc.setX(Math.round(loc.getX()));
        loc.setZ(Math.round(loc.getZ()));
        return loc;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        highlightHistory.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        highlightHistory.remove(e.getPlayer());
    }

}
