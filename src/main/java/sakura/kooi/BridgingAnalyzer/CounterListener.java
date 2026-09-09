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
import sakura.kooi.BridgingAnalyzer.utils.TitleUtils;
import sakura.kooi.BridgingAnalyzer.utils.Utils;

public class CounterListener implements Listener {
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!BridgingAnalyzer.isPlacedByPlayer(e.getBlock())) {
            e.setCancelled(true);
            return;
        }
        e.setDropItems(false);
        e.setExpToDrop(0);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction().toString().contains("CLICK")) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) if (e.isCancelled()) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.countCPS();
        }
    }

    @EventHandler
    public void onFallDown(PlayerMoveEvent e) {
        if (e.getTo().getY() < 0) {
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());

            if (c.isBridgeTimingActive()) {
                c.stopBridgeTiming();
            }

            if (c.isSpeedCountEnabled()) {
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

            if (c.isBridgeTimingEnabled() && !c.isBridgeTimingActive() && c.getAllBlocks().isEmpty()) {
                c.startBridgeTiming();
            }

            c.countBridge(e.getBlock());
            if (c.isSpeedCountEnabled()) {
                TitleUtils.sendTitle(e.getPlayer(), "", "§b" + c.getBridgeSpeed() + " block/s", 10, 80, 10);
            }
            final ItemStack replenish = resolvePlacedItem(e);
            if (replenish == null) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                if (!e.getPlayer().isOnline()) {
                    return;
                }
                Utils.addItem(e.getPlayer().getInventory(), replenish);
            }, 1L);
        }
    }

    private ItemStack resolvePlacedItem(BlockPlaceEvent event) {
        ItemStack placedItem = null;
        try {
            placedItem = event.getItemInHand();
        } catch (Throwable ignored) {
        }
        ItemStack replenish = Utils.cloneSingle(placedItem);
        if (replenish != null) {
            return replenish;
        }

        Material blockType = event.getBlockPlaced().getType();
        if (blockType == null || blockType == Material.AIR) {
            return null;
        }
        return new ItemStack(blockType, 1);
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
