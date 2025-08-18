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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        // Performance optimization: early returns to reduce processing
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        // Cache the block type check for better performance
        Material blockBelow = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
        if (blockBelow == Material.EMERALD_BLOCK) {
            e.getPlayer().setNoDamageTicks(40);
            Location spawnLoc = e.getTo().getBlock().getLocation().add(0.5, 1, 0.5);
            spawnLoc.setYaw(e.getPlayer().getLocation().getYaw());
            spawnLoc.setPitch(e.getPlayer().getLocation().getPitch());
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());

            // 修复Bug：设置复活点时清理所有已放置的方块
            c.instantBreakBlock();
            c.setCheckPoint(spawnLoc);

            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5, 0.5),
                sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.CLOUD, 1) {

                @Override
                public void onFinish() {
                }

            };
            TitleUtils.sendTitle(e.getPlayer(), "", "§a传送点已设置", 5, 10, 5);
            Sound orbPickupSound = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getSound(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Sounds.ENTITY_EXPERIENCE_ORB_PICKUP);
            if (orbPickupSound != null) {
                e.getPlayer().getWorld().playSound(e.getTo(), orbPickupSound, 1, 1);
            }
        }
    }

    @EventHandler
    public void triggerEndPointBlock(PlayerMoveEvent e) {
        // Performance optimization: early returns and cached block type
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        Material blockBelow = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
        if (blockBelow == Material.REDSTONE_BLOCK) {
            e.getPlayer().setNoDamageTicks(40);
            Counter counter = BridgingAnalyzer.getCounter(e.getPlayer());

            // 搭路记时功能：到达红石块终点时停止计时
            if (counter.isBridgeTimingActive()) {
                counter.stopBridgeTiming();
                if (counter.isBridgeTimingEnabled()) {
                    // 显示胜利信息和计时结果
                    TitleUtils.sendTitle(e.getPlayer(), "§6§lVICTORY", "§a§lTime: §e§l" + counter.formatBridgeTime(), 10, 60, 20);
                } else {
                    TitleUtils.sendTitle(e.getPlayer(), "§6§lVICTORY", "", 5, 20, 5);
                }
            } else {
                TitleUtils.sendTitle(e.getPlayer(), "§6§lVICTORY", "", 5, 20, 5);
            }

            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 0.1, 0.5),
                sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.WITCH, 20) {
                @Override
                public void onFinish() {
                    // BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
                    FireworkUtils.shootFirework(e.getPlayer());
                }

            };
            counter.vectoryBreakBlock();
            e.getPlayer().getWorld().playSound(e.getTo(),
                SoundMachine.get("LEVEL_UP", "ENTITY_PLAYER_LEVELUP"), 1, 1);
        }
    }

    @EventHandler
    public void triggerKnockbackBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        // Use version adapter for cross-version material compatibility
        Material melonBlock = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.MELON_BLOCK);
        if (melonBlock == null) {
            // Fallback for older versions - try MELON_BLOCK first (1.8.8-1.12.2), then MELON (very old versions)
            try {
                melonBlock = Material.valueOf("MELON_BLOCK");
            } catch (IllegalArgumentException ex) {
                try {
                    melonBlock = Material.valueOf("MELON"); // Ultimate fallback
                } catch (IllegalArgumentException ex2) {
                    melonBlock = Material.STONE; // Safety fallback
                }
            }
        }

        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == melonBlock) {
            e.getPlayer().setNoDamageTicks(20);

            Player player = e.getPlayer();
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
        // Performance optimization: early returns and cached block type
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        Material blockBelow = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
        if (blockBelow == Material.LAPIS_BLOCK) {
            e.getPlayer().setNoDamageTicks(40);
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());

            // 修复Bug：返回出生点时清理所有已放置的方块
            c.instantBreakBlock();
            c.setCheckPoint(Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5));
            c.resetMax();

            new ParticleRing(e.getTo().getBlock().getLocation().add(0.5, 1.5,
                    0.5), sakura.kooi.BridgingAnalyzer.utils.ParticleManager.ParticleTypes.FIREWORK, 35) {

                @Override
                public void onFinish() {
                    BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
                    BridgingAnalyzer.clearEffect(e.getPlayer());
                    if (!e.getPlayer().isOp()) {
                        e.getPlayer().getInventory().setHelmet(null);
                        e.getPlayer().getInventory().setChestplate(null);
                        e.getPlayer().getInventory().setLeggings(null);
                        e.getPlayer().getInventory().setBoots(null);
                    }
                }

            };
            TitleUtils.sendTitle(e.getPlayer(), "", "§b正在返回出生点...", 5, 25, 5);
            e.getPlayer().getWorld().playSound(e.getTo(),
                    SoundMachine.get("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
        }
    }

    @EventHandler
    public void triggerSpeedPlate(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        // Use version adapter for cross-version material compatibility
        Material lightWeightedPlate = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial("LIGHT_WEIGHTED_PRESSURE_PLATE");
        if (lightWeightedPlate == null) {
            // Fallback for 1.8.8 - use GOLD_PLATE
            try {
                lightWeightedPlate = Material.valueOf("GOLD_PLATE");
            } catch (IllegalArgumentException ex) {
                // Ultimate fallback - use STONE_PRESSURE_PLATE for 1.21 or STONE for 1.8.8
                try {
                    lightWeightedPlate = Material.valueOf("STONE_PRESSURE_PLATE");
                } catch (IllegalArgumentException ex2) {
                    lightWeightedPlate = Material.STONE; // Final fallback
                }
            }
        }

        if (e.getTo().getBlock().getType() == lightWeightedPlate) {
            e.getPlayer().setNoDamageTicks(20);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2), true);
        }
    }

    @EventHandler
    public void triggerTeleportBlock(PlayerMoveEvent e) {
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) return;
        if (e.getPlayer().getNoDamageTicks() != 0) return;
        if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
            e.getPlayer().setNoDamageTicks(20);
            Block to = e.getTo().getBlock();

            // Use version adapter for cross-version material compatibility
            Material glassPane = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.GLASS_PANE);
            if (glassPane == null) {
                // Fallback for older versions
                try {
                    glassPane = Material.valueOf("THIN_GLASS");
                } catch (IllegalArgumentException ex) {
                    glassPane = Material.valueOf("GLASS_PANE"); // Modern fallback
                }
            }

            while ((to.getType() == Material.AIR || to.getType() == glassPane || isSignMaterial(to.getType())) && to.getY() < 255) {
                to = to.getRelative(BlockFace.UP);
            }
            if (to.getType() == Material.BEACON) {
                e.getPlayer().setNoDamageTicks(50);
                Block teleportTarget = to;
                new TeleportRingEffect(e.getTo().getBlock().getLocation().add(0.5, 0,
                        0.5), teleportTarget.getLocation().add(0.5,
                        1.0,
                        0.5), 1, 0, 40) {

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
                    e.getPlayer().getWorld().playSound(e.getTo(), teleportSound, 1, 1);
                }
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

            // Use version adapter for cross-version material compatibility
            Material glassPane = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial(sakura.kooi.BridgingAnalyzer.api.VersionAdapter.Materials.GLASS_PANE);
            if (glassPane == null) {
                // Fallback for older versions
                try {
                    glassPane = Material.valueOf("THIN_GLASS");
                } catch (IllegalArgumentException ex) {
                    glassPane = Material.valueOf("GLASS_PANE"); // Modern fallback
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

    /**
     * Check if a material is a sign (cross-version compatible)
     */
    private boolean isSignMaterial(Material material) {
        String materialName = material.name();
        // Check for modern sign names (1.14+)
        if (materialName.contains("SIGN")) {
            return true;
        }
        // Check for legacy sign names (1.13 and below)
        try {
            return material == Material.valueOf("SIGN_POST") ||
                   material == Material.valueOf("WALL_SIGN") ||
                   material == Material.valueOf("SIGN");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
