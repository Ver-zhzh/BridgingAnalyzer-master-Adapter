package sakura.kooi.BridgingAnalyzer;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import sakura.kooi.BridgingAnalyzer.utils.*;

public class TriggerBlockListener implements Listener {
    @EventHandler
    public void antiTriggerBlockCover(BlockPlaceEvent e) {
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            if (isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN)) || isTriggerBlock(e.getBlock().getRelative(BlockFace.DOWN,
                    2))) {
                Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                    Utils.breakBlock(e.getBlock());
                    BridgingAnalyzer.getCounter(e.getPlayer()).removeBlockRecord(e.getBlock());
                }, 100);
            }
        }
    }

    private Vector getAttackVector(Location location) {
        double ran = 90 + Math.random() * 30 - 15;
        float newZ = (float) (location.getZ() + 3.0D * Math.sin(Math.toRadians(location.getYaw() + ran)));
        float newX = (float) (location.getX() + 3.0D * Math.cos(Math.toRadians(location.getYaw() + ran)));
        return new Vector(newX - location.getX(), 0.0D, newZ - location.getZ());
    }

    private boolean isTriggerBlock(Block b) {
        if (b.getType() == Material.EMERALD_BLOCK) return true;
        if (b.getType() == Material.REDSTONE_BLOCK) return true;
        if (b.getType() == Material.LAPIS_BLOCK) return true;
        return b.getType() == Material.BEACON;
    }

    @EventHandler
    public void triggerCheckPointBlock(PlayerMoveEvent e) {
        if (!shouldProcessTriggerMove(e, Material.EMERALD_BLOCK)) return;

        Player player = e.getPlayer();
        Location spawnLoc = Counter.createCheckpointLocation(e.getTo());
        Counter c = BridgingAnalyzer.getCounter(player);
        c.markTriggerBlockUsed();
        c.instantBreakBlock();
        c.setCheckPoint(spawnLoc);

            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5, 0.5),
                sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.CLOUD, 1) {

                @Override
                public void onFinish() {
                }

            };
        TitleUtils.sendTitle(player, "", "§a传送点已设置", 5, 10, 5);
        Sound orbPickupSound = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getSound(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Sounds.ENTITY_EXPERIENCE_ORB_PICKUP);
        if (orbPickupSound != null) {
            player.getWorld().playSound(e.getTo(), orbPickupSound, 1, 1);
        }
    }

    @EventHandler
    public void triggerEndPointBlock(PlayerMoveEvent e) {
        if (!shouldProcessTriggerMove(e, Material.REDSTONE_BLOCK)) return;

        Player player = e.getPlayer();
        Counter counter = BridgingAnalyzer.getCounter(player);
        counter.markTriggerBlockUsed();

            if (counter.isBridgeTimingActive()) {
                counter.stopBridgeTiming();
                if (counter.isBridgeTimingEnabled()) {
                    TitleUtils.sendTitle(player, "§6§lVICTORY", "§a§lTime: §e§l" + counter.formatBridgeTime(), 10, 60, 20);
                } else {
                    TitleUtils.sendTitle(player, "§6§lVICTORY", "", 5, 20, 5);
                }
            } else {
                TitleUtils.sendTitle(player, "§6§lVICTORY", "", 5, 20, 5);
            }

            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 0.1, 0.5),
                sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.WITCH, 20) {
                @Override
                public void onFinish() {
                    FireworkUtils.shootFirework(player);
                }

            };
            counter.vectoryBreakBlock();
            player.getWorld().playSound(e.getTo(),
                SoundMachine.get("LEVEL_UP", "ENTITY_PLAYER_LEVELUP"), 1, 1);
    }

    @EventHandler
    public void triggerKnockbackBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        Material melonBlock = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.MELON_BLOCK);
        if (melonBlock == null) {
            try {
                melonBlock = Material.valueOf("MELON_BLOCK");
            } catch (IllegalArgumentException ex) {
                try {
                    melonBlock = Material.valueOf("MELON");
                } catch (IllegalArgumentException ex2) {
                    melonBlock = Material.STONE;
                }
            }
        }

        if (melonBlock != null && enteredBlockBelow(e, melonBlock)) {
            e.getPlayer().setNoDamageTicks(20);

            Player player = e.getPlayer();
            BridgingAnalyzer.getCounter(player).markTriggerBlockUsed();
            Vector finalVector = getAttackVector(player.getLocation());
            Location finalAttackFrom = player.getLocation().add(finalVector);
            finalAttackFrom.setY(player.getLocation().getY() + 1.2);
            Vector normalize = finalAttackFrom.toVector().subtract(player.getLocation().toVector()).normalize();
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> {
                player.setNoDamageTicks(0);
                player.damage(0.0);
                player.setVelocity(normalize.multiply(-1.25).setY(0.45));
            }, 7);
        }
    }

    @EventHandler
    public void triggerSpawnPointBlock(PlayerMoveEvent e) {
        if (!shouldProcessTriggerMove(e, Material.LAPIS_BLOCK)) return;

        Player player = e.getPlayer();
        Counter c = BridgingAnalyzer.getCounter(player);
        c.markTriggerBlockUsed();
        c.instantBreakBlock();
        c.setCheckPoint(Counter.createSpawnCheckPoint(player.getWorld()));
        c.resetMax();

        new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5,
                0.5), sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.FIREWORK, 35) {

            @Override
            public void onFinish() {
                if (!player.isOnline()) {
                    return;
                }
                BridgingAnalyzer.teleportCheckPoint(player);
                BridgingAnalyzer.clearEffect(player);
                if (!player.isOp()) {
                    player.getInventory().setHelmet(null);
                    player.getInventory().setChestplate(null);
                    player.getInventory().setLeggings(null);
                    player.getInventory().setBoots(null);
                }
            }

        };
        TitleUtils.sendTitle(player, "", "§b正在返回出生点...", 5, 25, 5);
        player.getWorld().playSound(e.getTo(),
                SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
    }

    @EventHandler
    public void triggerSpeedPlate(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        Material lightWeightedPlate = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial("LIGHT_WEIGHTED_PRESSURE_PLATE");
        if (lightWeightedPlate == null) {
            try {
                lightWeightedPlate = Material.valueOf("GOLD_PLATE");
            } catch (IllegalArgumentException ex) {
                try {
                    lightWeightedPlate = Material.valueOf("STONE_PRESSURE_PLATE");
                } catch (IllegalArgumentException ex2) {
                    lightWeightedPlate = Material.STONE;
                }
            }
        }

        if (lightWeightedPlate != null && enteredBlock(e, lightWeightedPlate)) {
            Player player = e.getPlayer();
            player.setNoDamageTicks(20);
            BridgingAnalyzer.getCounter(player).markTriggerBlockUsed();
            PotionEffectUtils.applyEffect(player, new String[]{"SPEED"}, 100, 2);
        }
    }

    @EventHandler
    public void triggerTeleportBlock(PlayerMoveEvent e) {
        if (!shouldProcessTriggerMove(e, Material.BEACON)) return;

        Player player = e.getPlayer();
        player.setNoDamageTicks(20);
        BridgingAnalyzer.getCounter(player).markTriggerBlockUsed();
            Block to = e.getTo().getBlock();

            Material glassPane = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.GLASS_PANE);
            if (glassPane == null) {
                try {
                    glassPane = Material.valueOf("THIN_GLASS");
                } catch (IllegalArgumentException ex) {
                    glassPane = Material.valueOf("GLASS_PANE");
                }
            }

            while ((to.getType() == Material.AIR || to.getType() == glassPane || isSignMaterial(to.getType())) && to.getY() < 255) {
                to = to.getRelative(BlockFace.UP);
            }
        if (to.getType() == Material.BEACON) {
            player.setNoDamageTicks(50);
            Block teleportTarget = to;
            new TeleportRingEffect(e.getTo().getBlock().getLocation().add(0.5, 0,
                    0.5), teleportTarget.getLocation().add(0.5,
                    1.0,
                    0.5), 1, 0, 40) {

                @Override
                public void onFinish() {
                    if (!player.isOnline()) {
                        return;
                    }
                    Location loc = teleportTarget.getLocation().add(0.5, 1.5, 0.5);
                    loc.setYaw(player.getLocation().getYaw());
                    loc.setPitch(player.getLocation().getPitch());
                    player.teleport(loc);
                    BridgingAnalyzer.getCounter(player).markTriggerBlockUsed();
                }

            };
            Sound teleportSound = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getSound(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Sounds.ENTITY_ENDERMEN_TELEPORT);
            if (teleportSound != null) {
                player.getWorld().playSound(e.getTo(), teleportSound, 1, 1);
            }
        }
    }

    @EventHandler
    public void triggerTeleportBlock(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
            e.getPlayer().setNoDamageTicks(20);
            Block to = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN, 2);

            Material glassPane = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.GLASS_PANE);
            if (glassPane == null) {
                try {
                    glassPane = Material.valueOf("THIN_GLASS");
                } catch (IllegalArgumentException ex) {
                    glassPane = Material.valueOf("GLASS_PANE");
                }
            }

            while ((to.getType() == Material.AIR || to.getType() == glassPane || isSignMaterial(to.getType())) && to.getY() > 0) {
                to = to.getRelative(BlockFace.DOWN);
            }
            if (to.getType() == Material.BEACON) {
                e.getPlayer().setNoDamageTicks(50);
                Block teleportTarget = to;
                new TeleportRingEffect(e.getPlayer().getLocation().getBlock().getLocation().add(0.5, 0,
                        0.5), teleportTarget.getLocation().add(0.5,
                        1.0,
                        0.5), 1, 10, 40) {
                    @Override
                    public void onFinish() {
                        Location loc = teleportTarget.getLocation().add(0.5, 1.5, 0.5);
                        loc.setYaw(e.getPlayer().getLocation().getYaw());
                        loc.setPitch(e.getPlayer().getLocation().getPitch());
                        e.getPlayer().teleport(loc);
                    }

                };
                Sound teleportSound = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getSound(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Sounds.ENTITY_ENDERMEN_TELEPORT);
                if (teleportSound != null) {
                    e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), teleportSound, 1, 1);
                }
            }

        }
    }

    private boolean shouldProcessTriggerMove(PlayerMoveEvent e, Material triggerBelow) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            return false;
        }
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return false;
        }
        Counter counter = BridgingAnalyzer.getCounter(e.getPlayer());
        if (!counter.canUseTriggerBlock()) {
            return false;
        }
        return enteredBlockBelow(e, triggerBelow);
    }

    private boolean enteredBlockBelow(PlayerMoveEvent e, Material material) {
        Material fromBelow = e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType();
        Material toBelow = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
        return toBelow == material && fromBelow != material;
    }

    private boolean enteredBlock(PlayerMoveEvent e, Material material) {
        Material fromBlock = e.getFrom().getBlock().getType();
        Material toBlock = e.getTo().getBlock().getType();
        return toBlock == material && fromBlock != material;
    }

    /** Returns true for sign materials across legacy and modern names. */
    private boolean isSignMaterial(Material material) {
        String materialName = material.name();
        if (materialName.contains("SIGN")) {
            return true;
        }
        try {
            return material == Material.valueOf("SIGN_POST") ||
                   material == Material.valueOf("WALL_SIGN") ||
                   material == Material.valueOf("SIGN");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
