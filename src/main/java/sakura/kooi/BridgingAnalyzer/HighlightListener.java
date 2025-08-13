package sakura.kooi.BridgingAnalyzer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Particle;

import java.util.HashMap;

public class HighlightListener implements Listener {
    private HashMap<Player, Block> highlightHistory = new HashMap<>();

    private Block getRelativeBrick(Block b) {
        // Use version adapter for cross-version material compatibility
        Material stoneBricks = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial("STONE_BRICKS");
        if (stoneBricks == null) {
            // Fallback for 1.8.8 - use SMOOTH_BRICK
            try {
                stoneBricks = Material.valueOf("SMOOTH_BRICK");
            } catch (IllegalArgumentException e) {
                stoneBricks = Material.STONE; // Ultimate fallback
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
                // Fix: Use modern sendBlockChange API for 1.21 compatibility
                e.getPlayer().sendBlockChange(historyBlock.getLocation(), historyBlock.getBlockData());
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
                    // Fix: Use modern sendBlockChange API for 1.21 compatibility
                    e.getPlayer().sendBlockChange(historyBlock.getLocation(), historyBlock.getBlockData());
                }
                // Fix: Use modern BlockData API instead of deprecated byte data
                e.getPlayer().sendBlockChange(target.getLocation(), Material.SNOW_BLOCK.createBlockData());
                highlightHistory.put(e.getPlayer(), target);
            }
        }
    }

    @EventHandler
    public void onStandBridgeMove(PlayerMoveEvent e) {
        if (!BridgingAnalyzer.getCounter(e.getPlayer()).isStandBridgeMarkerEnabled()) return;
        // Use version adapter for cross-version particle compatibility
        try {
            Particle happyVillager;
            try {
                happyVillager = Particle.valueOf("HAPPY_VILLAGER");
            } catch (IllegalArgumentException ex) {
                // Fallback for older versions
                happyVillager = Particle.valueOf("VILLAGER_HAPPY");
            }
            e.getTo().getWorld().spawnParticle(happyVillager, e.getTo().clone().add(0.08, 0.0, 0.08), 5, 0, 0, 0, 0);
        } catch (Exception ex) {
            // Ignore particle errors on incompatible versions
        }
    }

    private Location roundLocation(Location location) {
        Location loc = location.clone();
        loc.setX(Math.round(loc.getX()));
        loc.setZ(Math.round(loc.getZ()));
        return loc;
    }

}
