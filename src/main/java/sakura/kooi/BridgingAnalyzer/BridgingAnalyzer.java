package sakura.kooi.BridgingAnalyzer;

/** Bridging practice plugin (SakuraKooi / Ver_zhzh). Universal JAR for 1.8.8–26.x+. */

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sakura.kooi.BridgingAnalyzer.utils.PotionEffectUtils;
import sakura.kooi.BridgingAnalyzer.commands.*;
import org.bstats.bukkit.Metrics;
import sakura.kooi.BridgingAnalyzer.utils.NoAIUtils;
import sakura.kooi.BridgingAnalyzer.utils.SoundMachine;
import sakura.kooi.BridgingAnalyzer.utils.TitleUtils;
import sakura.kooi.BridgingAnalyzer.utils.Utils;
import sakura.kooi.BridgingAnalyzer.api.BlockSkinProvider;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BridgingAnalyzer extends JavaPlugin implements Listener {
    @Getter
    private static BridgingAnalyzer instance;
    @Getter
    private static HashMap<Player, Counter> counters = new HashMap<>();
    @Getter
    private static HashMap<Block, Material> placedBlocks = new HashMap<>();
    @Setter
    private static BlockSkinProvider blockSkinProvider;

    private static final ConcurrentHashMap<Player, Boolean> noClearPermissionCache = new ConcurrentHashMap<>();
    private static final long TARGET_RESPAWN_DELAY_TICKS = 40L;
    private final Set<String> pendingTargetRespawns = ConcurrentHashMap.newKeySet();
    private int actionBarTick = 0;

    public static boolean hasNoClearPermission(Player player) {
        return noClearPermissionCache.computeIfAbsent(player,
            p -> p.hasPermission("bridginganalyzer.noclear"));
    }

    public static void clearPermissionCache(Player player) {
        noClearPermissionCache.remove(player);
    }

    public static void clearEffect(Player player) {
        for (PotionEffect eff : player.getActivePotionEffects()) {
            PotionEffectType invisibility = PotionEffectUtils.getEffectType("INVISIBILITY", "invisibility");
            if (invisibility != null && eff.getType() == invisibility && player.isOp()) {
                continue;
            }
            player.removePotionEffect(eff.getType());
        }
    }

    public static void clearInventory(Player p) {
        Inventory inv = p.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Key")) {
                    continue;
                }
            inv.setItem(i, null);
        }
    }

    public static Counter getCounter(Player p) {
        Counter c = counters.get(p);
        if (c == null) {
            c = new Counter(p);
            counters.put(p, c);
        }
        return c;
    }

    public static void spawnVillager() {
        BridgingAnalyzer plugin = getInstance();
        if (plugin == null) {
            return;
        }

        for (World world : Bukkit.getWorlds()) {
            for (ArmorStand stand : world.getEntitiesByClass(ArmorStand.class)) {
                if (stand.getCustomName() == null || !stand.getCustomName().contains("VillagerSpawnPoint")) {
                    continue;
                }
                Location spawn = stand.getLocation().clone().add(0, 1, 0);
                String spawnKey = plugin.getSpawnKey(spawn);
                if (plugin.pendingTargetRespawns.contains(spawnKey)) {
                    continue;
                }
                if (hasTargetNear(spawn)) {
                    continue;
                }
                spawnTargetVillager(spawn);
            }
        }
    }

    private static boolean hasTargetNear(Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, 0.8, 1.5, 0.8)) {
            if (isTargetVillager(entity)) {
                return true;
            }
        }
        return false;
    }

    private static void configureTargetVillager(Villager villager) {
        PotionEffectUtils.applyEffect(villager, new String[]{"SLOWNESS", "SLOW"}, 32766, 254);
        villager.setProfession(Profession.LIBRARIAN);
        PotionEffectUtils.setMaxHealth(villager, 1);
        villager.setHealth(villager.getMaxHealth());
        villager.setCustomName("靶子");
        villager.setCustomNameVisible(false);
        NoAIUtils.setAI(villager, false);
    }

    private static Villager spawnTargetVillager(Location location) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        configureTargetVillager(villager);
        return villager;
    }

    private Location resolveTargetAnchor(Villager villager) {
        Location best = null;
        double bestDistance = Double.MAX_VALUE;
        for (ArmorStand stand : villager.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (stand.getCustomName() == null || !stand.getCustomName().contains("VillagerSpawnPoint")) {
                continue;
            }
            Location spawn = stand.getLocation().clone().add(0, 1, 0);
            double distance = spawn.distanceSquared(villager.getLocation());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = spawn;
            }
        }
        return best != null ? best : villager.getLocation().clone();
    }

    private String getSpawnKey(Location location) {
        if (location == null || location.getWorld() == null) {
            return "unknown";
        }
        return location.getWorld().getName() + ':' + location.getBlockX() + ':' + location.getBlockY() + ':'
                + location.getBlockZ();
    }

    private void scheduleTargetRespawn(Location anchor, Villager villager) {
        if (anchor == null || anchor.getWorld() == null) {
            if (villager != null && villager.isValid()) {
                villager.remove();
            }
            return;
        }

        String spawnKey = getSpawnKey(anchor);
        if (pendingTargetRespawns.contains(spawnKey)) {
            if (villager != null && villager.isValid()) {
                villager.remove();
            }
            return;
        }

        pendingTargetRespawns.add(spawnKey);
        if (villager != null && villager.isValid()) {
            Sound hurtSound = SoundMachine.get("VILLAGER_HIT", "ENTITY_VILLAGER_HURT");
            if (hurtSound != null) {
                villager.getWorld().playSound(villager.getLocation(), hurtSound, 1, 1);
            }
            villager.remove();
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            pendingTargetRespawns.remove(spawnKey);
            if (anchor.getWorld() != null && !hasTargetNear(anchor)) {
                spawnTargetVillager(anchor);
            }
        }, TARGET_RESPAWN_DELAY_TICKS);
    }

    public static void teleportCheckPoint(Player p) {
        if (!p.isOnline()) {
            return;
        }
        Counter counter = getCounter(p);
        counter.markTriggerBlockUsed();
        p.setFallDistance(0);
        clearInventory(p);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setNoDamageTicks(40);
        boolean loadedChest = counter.teleportCheckPoint();
        if (!loadedChest) {
            p.getInventory().addItem(blockSkinProvider.provide(p));
        }
        counter.markTriggerBlockUsed();
        p.setGameMode(GameMode.SURVIVAL);
    }

    public static void refreshItem(Player p) {
        clearInventory(p);
        p.getInventory().addItem(blockSkinProvider.provide(p));
    }

    public static boolean isPlacedByPlayer(Block b) {
        if (getPlacedBlocks().containsKey(b)) return getPlacedBlocks().get(b).equals(b.getType());
        return false;
    }

    @EventHandler
    public void antiArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        e.setCancelled(true);
        String standName = e.getRightClicked().getCustomName();
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().isOp()
                && standName != null && standName.contains("VillagerSpawnPoint")) {
            e.getRightClicked().remove();
            TitleUtils.sendTitle(e.getPlayer(), "", "§a村民刷新点已移除", 10, 20, 10);
        }
    }

    @EventHandler
    public void disableVillagerShop(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void disableWeather(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    private void handlePlayerDisconnect(Player player, String consoleSuffix) {
        clearPermissionCache(player);
        Counter counter = counters.remove(player);
        if (counter != null) {
            if (counter.isBridgeTimingActive()) {
                counter.stopBridgeTiming();
            }
            counter.instantBreakBlock();
        }
        if (consoleSuffix != null) {
            Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §a玩家 " + player.getName() + consoleSuffix);
        }
    }

    @EventHandler
    public void noHunger(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) e.getEntity();
        EntityDamageEvent.DamageCause cause = e.getCause();
        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION
                || cause == EntityDamageEvent.DamageCause.CRAMMING) {
            e.setDamage(0.0);
            return;
        }

        Counter c = BridgingAnalyzer.getCounter(player);
        if (e.getFinalDamage() > 20 && c.canUseTriggerBlock()) {
            c.reset();
            teleportCheckPoint(player);
            TitleUtils.sendTitle(player, "",
                    "§4致命伤害 - " + Utils.formatDouble(e.getFinalDamage() / 2) + " ❤", 10, 20, 10);
            e.setDamage(0.0);
        } else if (e.getFinalDamage() > 10) {
            TitleUtils.sendTitle(player, "",
                    "§c严重伤害 - " + Utils.formatDouble(e.getFinalDamage() / 2) + " ❤", 10, 20, 10);
            e.setDamage(0.0);
        } else {
            e.setDamage(0.0);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §c正在清除所有已放置方块....");
        for (Counter c : counters.values()) {
            c.instantBreakBlock();
        }
        counters.clear();
        for (Block b : Counter.scheduledBreakBlocks) {
            b.setType(Material.AIR);
        }
        Counter.scheduledBreakBlocks.clear();
        Bukkit.getConsoleSender().sendMessage("§bBridgingAnalyzer §7>> §a方块清除完毕.");
    }

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("§b§l=== BridgingAnalyzer v2.3.3 ===");
        getLogger().info("§aOriginal Author: SakuraKooi");
        getLogger().info("§aAdaptation: Ver_zhzh");
        getLogger().info("§eUniversal Jar: 1.8.8 - 26.x+");
        getLogger().info("§6Detected: " + sakura.kooi.BridgingAnalyzer.api.ServerVersion.current().getDisplayVersion());
        getLogger().info("§6Features: Bridge Timing / Multi-Version");
        getLogger().info("§b§l==============================");

        try {
            sakura.kooi.BridgingAnalyzer.api.VersionManager.initialize();
            getLogger().info("§aUniversal adapter loaded successfully!");

            sakura.kooi.BridgingAnalyzer.utils.ParticleManager.getInstance()
                .initialize(sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter());
            getLogger().info("§aParticle manager initialized successfully!");

        } catch (Exception e) {
            getLogger().severe("§cFailed to initialize version adapter: " + e.getMessage());
            getLogger().severe("§cPlugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Metrics metrics = new Metrics(this, 3991);
        blockSkinProvider = new DefaultBlockSkinProvider();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new CounterListener(), this);
        pluginManager.registerEvents(new HighlightListener(), this);
        pluginManager.registerEvents(new TriggerBlockListener(), this);
        getCommand("bridge").setExecutor(new BridgeCommand());
        getCommand("clearblock").setExecutor(new ClearCommand());
        getCommand("bsaveworld").setExecutor(new SaveWorldCommand());
        getCommand("imstuck").setExecutor(new StuckCommand());
        getCommand("genvillager").setExecutor(new VillagerSpawnPointCommand());
        spawnVillager();
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            spawnVillager();
        }, 300, 300);

        // ActionBar: 2-tick loop; CPS/distance every 4 ticks, bridge timer every 2 ticks
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            actionBarTick++;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Counter counter = getCounter(player);
                if (!counter.isSpeedCountEnabled()) {
                    continue;
                }
                boolean timingActive = counter.isBridgeTimingEnabled() && counter.isBridgeTimingActive();
                if (!timingActive && actionBarTick % 2 != 0) {
                    continue;
                }

                String message = "§c§l最大CPS - " + counter.getMaxCPS() + " §d§l当前CPS - " + counter.getCPS()
                        + " §a§l| §c§l最远距离 - " + counter.getMaxBridgeLength() + " §d§l当前距离 - "
                        + counter.getBridgeLength();
                if (timingActive) {
                    message += " §6§l| §a§lTime §f- §e§l" + counter.formatBridgeTime();
                }

                sakura.kooi.BridgingAnalyzer.utils.ActionBarUtils.sendActionBar(player, message);
            }
        }, 0, 2);



        Bukkit.getConsoleSender().sendMessage(new String[]{
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------",
                "§bBridgingAnalyzer §7>> §a搭路练习 已加载 §bBy.SakuraKooi",
                "§bBridgingAnalyzer §7>> §chttps://github.com/SakuraKoi/BridgingAnalyzer/",
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------",
                "§bBridgingAnalyzer §7>> §e踩在 §a绿宝石块 §e上可以设置传送点",
                "§bBridgingAnalyzer §7>> §e踩在 §c红石块 §e上可以回到传送点",
                "§bBridgingAnalyzer §7>> §e踩在 §b青金石块 §e上可以回到出生点",
                "§bBridgingAnalyzer §7>> §e使用 §a/genvillager §e可在站立位置创建村民刷新点",
                "§bBridgingAnalyzer §7>> §c掉入虚空会自动回到 §a传送点 §c并重置地图",
                "§bBridgingAnalyzer §7>> §c注意: 创造模式放置的方块不会被重置, 请在生存模式下练习",
                "§bBridgingAnalyzer §7>> §a通用单Jar已启用: 支持 1.8.8 - 26.x+",
                "§bBridgingAnalyzer §7>> §f----------------------------------------------------------------"
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(new String[]{
                "§b§l搭路练习 §7>> §e输入 §6/bridge §e更改练习参数",
                "§b§l搭路练习 §7>> §6Bilibili @SakuraKooi"
        });
        if (hasNoClearPermission(player)) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline()) {
                return;
            }
            Counter counter = getCounter(player);
            counter.setCheckPoint(Counter.createSpawnCheckPoint(player.getWorld()));
            counter.markTriggerBlockUsed();
            teleportCheckPoint(player);
        }, 5L);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (hasNoClearPermission(e.getPlayer())) return;

        Material goldenPickaxe = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.GOLDEN_PICKAXE);
        if (goldenPickaxe == null) {
            try {
                goldenPickaxe = Material.valueOf("GOLD_PICKAXE");
            } catch (IllegalArgumentException ex) {
                goldenPickaxe = Material.valueOf("GOLDEN_PICKAXE");
            }
        }

        if (e.getItemDrop().getItemStack().getType() == goldenPickaxe) {
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        handlePlayerDisconnect(e.getPlayer(), " 离线, 已清除其放置的方块.");
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        handlePlayerDisconnect(e.getPlayer(), " 被踢出, 已清除其放置的方块.");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTargetVillagerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Villager)) {
            return;
        }
        Villager villager = (Villager) e.getEntity();
        if (!isTargetVillager(villager)) {
            return;
        }

        if (villager.getHealth() - e.getFinalDamage() > 0) {
            return;
        }

        e.setCancelled(true);
        scheduleTargetRespawn(resolveTargetAnchor(villager), villager);
    }

    private static boolean isTargetVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return false;
        }
        return "靶子".equals(entity.getCustomName());
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() == null) return;
        if (e.getDamager() == null) return;
        if (e.getEntity().getType() == EntityType.PLAYER) if (e.getDamager().getType() == EntityType.PLAYER) {
            int state = onPvPDamage((Player) e.getEntity(), (Player) e.getDamager());
            if (state == -1) {
                e.setCancelled(true);
            } else if (state == 1) {
                e.setCancelled(true);
                BridgingAnalyzer.getCounter((Player) e.getDamager()).setPvPEnabled(true);
                TitleUtils.sendTitle((Player) e.getDamager(), "", "§c注意: §aPvP已开启", 10, 20, 10);
                ((Player) e.getEntity()).damage(0.00);
                ((Player) e.getEntity()).setNoDamageTicks(60);
                ((Player) e.getDamager()).setNoDamageTicks(60);
            }
        } else if (e.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) e.getDamager();
            if (proj.getShooter() instanceof Player) {
                int state = onPvPDamage((Player) e.getEntity(), (Player) proj.getShooter());
                if (state == -1) {
                    e.setCancelled(true);
                } else if (state == 1) {
                    e.setCancelled(true);
                    BridgingAnalyzer.getCounter((Player) proj.getShooter()).setPvPEnabled(true);
                    TitleUtils.sendTitle((Player) proj.getShooter(), "", "§c注意: §aPvP已开启", 10, 20, 10);
                    ((Player) e.getEntity()).damage(0.00);
                    ((Player) e.getEntity()).setNoDamageTicks(60);
                    ((Player) proj.getShooter()).setNoDamageTicks(60);
                }
            }
        }
    }

    private int onPvPDamage(Player player, Player damager) {
        if (!BridgingAnalyzer.getCounter(player).isPvPEnabled()) return -1; // cancel
        if (!BridgingAnalyzer.getCounter(damager).isPvPEnabled()) return 1; // enable
        return 0; // accept
    }

}
