package sakura.kooi.BridgingAnalyzer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.utils.ActionBarUtils;
import sakura.kooi.BridgingAnalyzer.utils.TitleUtils;

public class CounterListener implements Listener {
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getPlayer() != null) if (!BridgingAnalyzer.isPlacedByPlayer(e.getBlock())) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction().toString().contains("CLICK")) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) if (e.isCancelled()) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.countCPS();
            // ActionBar显示现在由定时任务统一处理，这里不再需要显示
        }
    }

    @EventHandler
    public void onFallDown(PlayerMoveEvent e) {
        if (e.getTo().getY() < 0) {
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());

            // 搭路记时功能：掉落虚空时停止计时（失败，不显示时间）
            if (c.isBridgeTimingActive()) {
                c.stopBridgeTiming();
            }

            if (c.isSpeedCountEnabled()) {
                // Display max bridging speed when falling (6.5s total display time)
                TitleUtils.sendTitle(e.getPlayer(), "", "§cMax - " + c.getMaxBridgeSpeed() + " block/s", 10, 100, 20);
            }
            c.reset();
            BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
        }
    }

    @EventHandler
    public void onLiqudFlow(BlockFromToEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());

            // 搭路记时功能：如果这是第一个方块且启用了计时功能，开始计时
            if (c.isBridgeTimingEnabled() && !c.isBridgeTimingActive() && c.getAllBlocks().isEmpty()) {
                c.startBridgeTiming();
            }

            c.countBridge(e.getBlock());
            if (c.isSpeedCountEnabled()) {
                // Display bridging speed in title (5s total display time for visibility)
                TitleUtils.sendTitle(e.getPlayer(), "", "§b" + c.getBridgeSpeed() + " block/s", 10, 80, 10);
            }
            // Fix: Use cross-version compatible ItemStack API
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                ItemStack handItem;
                try {
                    // Try modern API first (1.9+)
                    handItem = e.getPlayer().getInventory().getItemInMainHand();
                } catch (NoSuchMethodError ex) {
                    // Fallback to legacy API (1.8.8)
                    handItem = e.getPlayer().getInventory().getItemInHand();
                }

                if (handItem != null && handItem.getType() != Material.AIR) {
                    e.getPlayer().getInventory().addItem(new ItemStack(handItem.getType(), 1));
                }
            }, 1);
        }
    }

    @EventHandler
    public void onPlaceLiqud(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.addLogBlock(e.getBlockClicked().getRelative(e.getBlockFace()));
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> e.getPlayer().getInventory().remove(Material.BUCKET), 1);
        }
    }
}
