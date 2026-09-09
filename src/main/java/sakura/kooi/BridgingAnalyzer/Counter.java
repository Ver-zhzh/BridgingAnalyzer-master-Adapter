package sakura.kooi.BridgingAnalyzer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import sakura.kooi.BridgingAnalyzer.utils.SoundMachine;
import sakura.kooi.BridgingAnalyzer.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;

public class Counter {
    public static HashSet<Block> scheduledBreakBlocks = new HashSet<>();
    private ArrayList<Long> counterCPS = new ArrayList<>();
    private int maxCPS = 0;
    private ArrayList<Long> counterBridge = new ArrayList<>();
    private double maxBridge = 0;
    private int currentLength = 0;
    private int maxLength = 0;
    private ArrayList<Block> allBlock = new ArrayList<>();
    private Block lastBlock;
    private static final long TRIGGER_COOLDOWN_MS = 3000L;
    private Location checkPoint;
    private Player player;
    private long triggerBlockCooldownUntil = 0L;
    @Getter
    @Setter
    private boolean speedCountEnabled = true;
    @Getter
    @Setter
    private boolean PvPEnabled = false;
    @Getter
    @Setter
    private boolean highlightEnabled = true;
    @Getter
    @Setter
    private boolean standBridgeMarkerEnabled = false;
    @Getter
    @Setter
    private boolean bridgeTimingEnabled = true;
    private long bridgeStartTime = 0;
    private long bridgeEndTime = 0;
    private boolean isBridgeTimingActive = false;
    // Cached formatted bridge time string
    private String cachedFormattedTime = "00:00.000";
    private long lastFormattedTimeMs = 0;
    public Counter(Player p) {
        player = p;
        checkPoint = createSpawnCheckPoint(p.getWorld());
    }

    public static Location createSpawnCheckPoint(World world) {
        Location spawn = world.getSpawnLocation().clone();
        return createCheckpointLocation(spawn);
    }

    /** Checkpoint on the block below feet (same rule as emerald trigger). */
    public static Location createCheckpointLocation(Location playerLocation) {
        Block standOn = getStandOnBlock(playerLocation);
        Location checkpoint = standOn.getLocation().add(0.5D, 1.0D, 0.5D);
        checkpoint.setYaw(playerLocation.getYaw());
        checkpoint.setPitch(playerLocation.getPitch());
        return checkpoint;
    }

    private static Block getStandOnBlock(Location location) {
        Block feetBlock = location.getBlock();
        Block standOn = feetBlock.getRelative(BlockFace.DOWN);
        if (standOn.getType() != Material.AIR) {
            return standOn;
        }
        return location.clone().add(0, -1, 0).getBlock();
    }

    public boolean canUseTriggerBlock() {
        return System.currentTimeMillis() >= triggerBlockCooldownUntil;
    }

    public void markTriggerBlockUsed() {
        triggerBlockCooldownUntil = System.currentTimeMillis() + TRIGGER_COOLDOWN_MS;
    }

    public void addLogBlock(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getType());
    }

    public void breakBlock() {
        scheduledBreakBlocks.addAll(allBlock);
        new BreakRunnable(new ArrayList<>(allBlock));
        allBlock.clear();
    }

    public void countBridge(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getType());
        if ((lastBlock != null) && ((lastBlock.getY() + 1) != block.getY())) {
            counterBridge.add(System.currentTimeMillis());
            currentLength++;
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        lastBlock = block;
        removeBridgeTimeout();
        getBridgeSpeed();
    }

    public void countCPS() {
        counterCPS.add(System.currentTimeMillis());
        removeCPSTimeout();
        if (counterCPS.size() > maxCPS) {
            maxCPS = counterCPS.size();
        }
    }

    public ArrayList<Block> getAllBlocks() {
        return allBlock;
    }

    public int getBridgeLength() {
        return currentLength;
    }

    public double getBridgeSpeed() {
        double result;
        if (counterBridge.isEmpty()) {
            result = 0.00;
        } else {
            long peri = counterBridge.get(counterBridge.size() - 1) - counterBridge.get(0);
            if (peri > 1000L) {
                result = counterBridge.size() / (peri / 1000.0);
                if (result > maxBridge) {
                    maxBridge = Utils.formatDouble(result);
                }
            } else {
                result = counterBridge.size();
            }
        }
        return Utils.formatDouble(result);
    }

    public int getCPS() {
        return counterCPS.size();
    }

    public int getMaxBridgeLength() {
        return maxLength;
    }

    public double getMaxBridgeSpeed() {
        return maxBridge;
    }

    public int getMaxCPS() {
        return maxCPS;
    }

    public void instantBreakBlock() {
        for (Block b : allBlock) {
            Utils.breakBlock(b);
            BridgingAnalyzer.getPlacedBlocks().remove(b);
        }
        allBlock.clear();
    }

    public void removeBlockRecord(Block b) {
        allBlock.remove(b);
        BridgingAnalyzer.getPlacedBlocks().remove(b);
    }

    private void removeBridgeTimeout() {
        while (!counterBridge.isEmpty() && ((System.currentTimeMillis() - counterBridge.get(0)) > 3000)) {
            counterBridge.remove(0);
        }
    }

    private void removeCPSTimeout() {
        while (!counterCPS.isEmpty() && ((System.currentTimeMillis() - counterCPS.get(0)) > 1000)) {
            counterCPS.remove(0);
        }
    }

    public void reset() {
        counterCPS.clear();
        maxCPS = 0;
        counterBridge.clear();
        maxBridge = 0;
        currentLength = 0;
        resetBridgeTiming(); // 重置计时功能
        breakBlock();
    }

    public void resetMax() {
        maxCPS = 0;
        maxLength = 0;
    }

    public void resetMaxLength() {
        maxLength = 0;
    }

    public void setCheckPoint(Location loc) {
        checkPoint = loc.clone();
        resetBridgeTiming();
        loadChestItemsBelowCheckpoint();
    }

    public boolean teleportCheckPoint() {
        if (!player.isOnline()) {
            return false;
        }
        player.teleport(checkPoint.clone());
        return loadChestItemsBelowCheckpoint();
    }

    private boolean loadChestItemsBelowCheckpoint() {
        Block chestBlock = findLinkedChestBlock();
        if (chestBlock == null) {
            return false;
        }

        if (!chestBlock.getChunk().isLoaded()) {
            chestBlock.getChunk().load();
        }

        BlockState state = chestBlock.getState();
        if (!(state instanceof Chest)) {
            return false;
        }

        Chest chest = (Chest) state;
        ItemStack[] contents = chest.getBlockInventory().getContents();
        if (!hasInventoryItems(contents)) {
            contents = chest.getInventory().getContents();
        }
        if (!hasInventoryItems(contents)) {
            return false;
        }

        BridgingAnalyzer.clearInventory(player);
        for (ItemStack stack : contents) {
            if (stack != null && stack.getType() != Material.AIR) {
                Utils.addItem(player.getInventory(), stack.clone());
            }
        }
        player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1, 1);
        return true;
    }

    private static boolean hasInventoryItems(ItemStack[] contents) {
        if (contents == null) {
            return false;
        }
        for (ItemStack stack : contents) {
            if (stack != null && stack.getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    /** Chest is two blocks below the emerald stand-on block. */
    private Block findLinkedChestBlock() {
        if (checkPoint == null) {
            return null;
        }
        Block standOn = getStandOnBlock(checkPoint);
        if (standOn.getType() == Material.AIR) {
            return null;
        }
        Block target = standOn.getRelative(BlockFace.DOWN, 2);
        return isChestMaterial(target.getType()) ? target : null;
    }

    private static boolean isChestMaterial(Material material) {
        if (material == null || material == Material.AIR) {
            return false;
        }
        String name = material.name();
        return "CHEST".equals(name) || "TRAPPED_CHEST".equals(name);
    }

    public void vectoryBreakBlock() {
        counterCPS.clear();
        counterBridge.clear();
        currentLength = 0;
        for (Block b : allBlock)
            if (b.getType() != Material.AIR) {
                b.setType(Material.SEA_LANTERN);
            }
        BridgingAnalyzer.teleportCheckPoint(player);
        breakBlock();
    }

    public void startBridgeTiming() {
        if (bridgeTimingEnabled && !isBridgeTimingActive) {
            bridgeStartTime = System.currentTimeMillis();
            bridgeEndTime = 0;
            isBridgeTimingActive = true;
        }
    }

    public void stopBridgeTiming() {
        if (isBridgeTimingActive) {
            bridgeEndTime = System.currentTimeMillis();
            isBridgeTimingActive = false;
        }
    }

    public void resetBridgeTiming() {
        bridgeStartTime = 0;
        bridgeEndTime = 0;
        isBridgeTimingActive = false;
        cachedFormattedTime = "00:00.000";
        lastFormattedTimeMs = 0;
    }

    public long getBridgeTime() {
        if (!bridgeTimingEnabled) return 0;
        if (isBridgeTimingActive && bridgeStartTime > 0) {
            return System.currentTimeMillis() - bridgeStartTime;
        } else if (bridgeEndTime > 0 && bridgeStartTime > 0) {
            return bridgeEndTime - bridgeStartTime;
        }
        return 0;
    }

    public String formatBridgeTime() {
        long timeMs = getBridgeTime();
        if (timeMs <= 0) return "00:00.000";

        if (timeMs == lastFormattedTimeMs && cachedFormattedTime != null) {
            return cachedFormattedTime;
        }

        long totalSeconds = timeMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long milliseconds = timeMs % 1000;

        minutes = Math.max(0, Math.min(99, minutes));
        seconds = Math.max(0, Math.min(59, seconds));
        milliseconds = Math.max(0, Math.min(999, milliseconds));

        cachedFormattedTime = String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
        lastFormattedTimeMs = timeMs;
        return cachedFormattedTime;
    }

    public boolean isBridgeTimingActive() {
        return isBridgeTimingActive;
    }

    public class BreakRunnable implements Runnable {
        BukkitTask task;
        ArrayList<Block> blocks = new ArrayList<>();

        public BreakRunnable(ArrayList<Block> allBlocks) {
            blocks.addAll(allBlocks);
            scheduledBreakBlocks.addAll(blocks);
            if (blocks.isEmpty()) return;
            int tick = 1 + (60 / blocks.size());
            if (tick > 3) {
                tick = 3;
            }
            task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.getInstance(), this, 10, tick);
        }

        @Override
        public void run() {
            if (!blocks.isEmpty()) {
                int blocksPerTick = Math.min(3, blocks.size());

                for (int i = 0; i < blocksPerTick && !blocks.isEmpty(); i++) {
                    Block b = blocks.remove(0);
                    scheduledBreakBlocks.remove(b);
                    BridgingAnalyzer.getPlacedBlocks().remove(b);

                    if (b.getType() != Material.AIR) {
                        Utils.breakBlock(b);
                    }
                }
            } else {
                task.cancel();
            }
        }
    }

}
