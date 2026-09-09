package sakura.kooi.BridgingAnalyzer.adapters;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.VersionAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Single runtime adapter for all supported server versions (1.8.8 - 26.x+).
 * Uses reflection and feature detection instead of per-version adapter classes.
 */
public class UniversalAdapter implements VersionAdapter {

    private static Method titleMethod;
    private static Method actionBarStringMethod;
    private static Method spigotMethod;
    private static Method spigotSendMessageMethod;
    private static Class<?> chatMessageTypeClass;
    private static Object actionBarType;
    private static Class<?> textComponentClass;
    private static Method fromLegacyTextMethod;
    private static Method spawnParticleMethod;
    private static Method createBlockDataMethod;
    private static Method sendBlockChangeBlockDataMethod;
    private static Method matchMaterialMethod;
    private static Method sendBlockChangeLegacyMethod;
    private static Class<?> blockDataClass;

    private static boolean featuresInitialized;
    private static boolean hasTitleApi;
    private static boolean hasParticleApi;
    private static boolean hasModernMaterials;
    private static boolean hasBlockDataApi;
    private static boolean hasSpigotActionBar;

    private static Class<?> craftPlayerClass;
    private static Class<?> packetPlayOutTitleClass;
    private static Class<?> iChatBaseComponentClass;
    private static Class<?> enumTitleActionClass;
    private static Method getHandleMethod;
    private static Method sendPacketMethod;
    private static Method chatSerializerMethod;
    private static Field playerConnectionField;
    private static Object enumTitleAction;
    private static Object enumSubtitleAction;
    private static Class<?> packetPlayOutChatClass;
    private static boolean nmsTitleReady;

    private static void ensureFeaturesInitialized() {
        if (featuresInitialized) {
            return;
        }
        featuresInitialized = true;

        titleMethod = findMethod(Player.class, "sendTitle",
                String.class, String.class, int.class, int.class, int.class);
        hasTitleApi = titleMethod != null;

        actionBarStringMethod = findMethod(Player.class, "sendActionBar", String.class);

        try {
            spigotMethod = Player.class.getMethod("spigot");
            chatMessageTypeClass = Class.forName("net.md_5.bungee.api.ChatMessageType");
            actionBarType = Enum.valueOf((Class<Enum>) chatMessageTypeClass, "ACTION_BAR");
            textComponentClass = Class.forName("net.md_5.bungee.api.chat.TextComponent");
            fromLegacyTextMethod = textComponentClass.getMethod("fromLegacyText", String.class);
            spigotSendMessageMethod = spigotMethod.getReturnType().getMethod(
                    "sendMessage", chatMessageTypeClass, Class.forName("[Lnet.md_5.bungee.api.chat.BaseComponent;"));
            hasSpigotActionBar = true;
        } catch (Throwable ignored) {
            hasSpigotActionBar = false;
        }

        try {
            Class.forName("org.bukkit.Particle");
            spawnParticleMethod = World.class.getMethod("spawnParticle", Particle.class, Location.class,
                    int.class, double.class, double.class, double.class, double.class);
            hasParticleApi = true;
        } catch (Throwable ignored) {
            hasParticleApi = false;
        }

        createBlockDataMethod = findMethod(Material.class, "createBlockData");
        if (createBlockDataMethod != null) {
            try {
                blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
                sendBlockChangeBlockDataMethod = Player.class.getMethod("sendBlockChange", Location.class, blockDataClass);
                hasBlockDataApi = true;
            } catch (Throwable ignored) {
                hasBlockDataApi = false;
            }
        }

        sendBlockChangeLegacyMethod = findMethod(Player.class, "sendBlockChange", Location.class, Material.class, byte.class);
        hasModernMaterials = hasBlockDataApi;
        matchMaterialMethod = findMethod(Material.class, "matchMaterial", String.class);

        initLegacyNmsTitle();
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void initLegacyNmsTitle() {
        try {
            String nmsVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName();
            nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf('.') + 1);
            if (!nmsVersion.startsWith("v")) {
                return;
            }

            String craftPackage = "org.bukkit.craftbukkit." + nmsVersion;
            String nmsPackage = "net.minecraft.server." + nmsVersion;

            craftPlayerClass = Class.forName(craftPackage + ".entity.CraftPlayer");
            packetPlayOutTitleClass = Class.forName(nmsPackage + ".PacketPlayOutTitle");
            iChatBaseComponentClass = Class.forName(nmsPackage + ".IChatBaseComponent");
            enumTitleActionClass = Class.forName(nmsPackage + ".PacketPlayOutTitle$EnumTitleAction");

            Class<?> chatSerializerClass = iChatBaseComponentClass.getDeclaredClasses()[0];
            chatSerializerMethod = chatSerializerClass.getMethod("a", String.class);
            enumTitleAction = enumTitleActionClass.getField("TITLE").get(null);
            enumSubtitleAction = enumTitleActionClass.getField("SUBTITLE").get(null);

            getHandleMethod = craftPlayerClass.getMethod("getHandle");
            Class<?> entityPlayerClass = Class.forName(nmsPackage + ".EntityPlayer");
            playerConnectionField = entityPlayerClass.getField("playerConnection");
            Class<?> playerConnectionClass = Class.forName(nmsPackage + ".PlayerConnection");
            sendPacketMethod = playerConnectionClass.getMethod("sendPacket", Class.forName(nmsPackage + ".Packet"));
            packetPlayOutChatClass = Class.forName(nmsPackage + ".PacketPlayOutChat");
            nmsTitleReady = true;
        } catch (Throwable ignored) {
            nmsTitleReady = false;
        }
    }

    @Override
    public String getSupportedVersion() {
        return "universal";
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        ensureFeaturesInitialized();
        String safeTitle = title == null ? "" : title;
        String safeSubtitle = subtitle == null ? "" : subtitle;

        if (hasTitleApi && titleMethod != null) {
            try {
                titleMethod.invoke(player, safeTitle, safeSubtitle, fadeIn, stay, fadeOut);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (nmsTitleReady) {
            try {
                Object craftPlayer = craftPlayerClass.cast(player);
                Object entityPlayer = getHandleMethod.invoke(craftPlayer);
                Object playerConnection = playerConnectionField.get(entityPlayer);

                Object titleChat = chatSerializerMethod.invoke(null, "{\"text\":\"" + escapeJson(safeTitle) + "\"}");
                Object titlePacket = packetPlayOutTitleClass.getConstructor(
                        enumTitleActionClass, iChatBaseComponentClass, int.class, int.class, int.class
                ).newInstance(enumTitleAction, titleChat, fadeIn, stay, fadeOut);
                sendPacketMethod.invoke(playerConnection, titlePacket);

                Object subtitleChat = chatSerializerMethod.invoke(null, "{\"text\":\"" + escapeJson(safeSubtitle) + "\"}");
                Object subtitlePacket = packetPlayOutTitleClass.getConstructor(
                        enumTitleActionClass, iChatBaseComponentClass, int.class, int.class, int.class
                ).newInstance(enumSubtitleAction, subtitleChat, fadeIn, stay, fadeOut);
                sendPacketMethod.invoke(playerConnection, subtitlePacket);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (!safeTitle.isEmpty()) {
            player.sendMessage("§6[Title] " + safeTitle);
        }
        if (!safeSubtitle.isEmpty()) {
            player.sendMessage("§7[Subtitle] " + safeSubtitle);
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {
        ensureFeaturesInitialized();

        if (actionBarStringMethod != null) {
            try {
                actionBarStringMethod.invoke(player, message);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (hasSpigotActionBar) {
            try {
                Object spigot = spigotMethod.invoke(player);
                Object components = fromLegacyTextMethod.invoke(null, message);
                spigotSendMessageMethod.invoke(spigot, actionBarType, components);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (nmsTitleReady) {
            try {
                Object craftPlayer = craftPlayerClass.cast(player);
                Object entityPlayer = getHandleMethod.invoke(craftPlayer);
                Object playerConnection = playerConnectionField.get(entityPlayer);
                Object messageComponent = chatSerializerMethod.invoke(null, "{\"text\":\"" + escapeJson(message) + "\"}");
                Object chatPacket = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class)
                        .newInstance(messageComponent, (byte) 2);
                sendPacketMethod.invoke(playerConnection, chatPacket);
                return;
            } catch (Throwable ignored) {
            }
        }

        player.sendMessage("§e[ActionBar] " + message);
    }

    @Override
    public void spawnParticle(Location location, String particleType, int count) {
        ensureFeaturesInitialized();
        if (location == null || location.getWorld() == null) {
            return;
        }

        if (hasParticleApi) {
            Particle particle = resolveParticle(particleType);
            if (particle != null && spawnParticleMethod != null) {
                try {
                    spawnParticleMethod.invoke(location.getWorld(), particle, location, count, 0D, 0D, 0D, 0D);
                    return;
                } catch (Throwable ignored) {
                }
            }
        }

        Effect effect = resolveEffect(particleType);
        if (effect != null) {
            try {
                location.getWorld().playEffect(location, effect, count);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public Material getMaterial(String materialName) {
        if (materialName == null) {
            return null;
        }

        switch (materialName) {
            case Materials.GOLDEN_PICKAXE:
                return firstMaterial("GOLDEN_PICKAXE", "GOLD_PICKAXE");
            case Materials.SMOOTH_SANDSTONE:
                return firstMaterial("SMOOTH_SANDSTONE", "SANDSTONE");
            case Materials.FIREWORK_ROCKET:
                return firstMaterial("FIREWORK_ROCKET", "FIREWORK");
            case Materials.GLASS_PANE:
                return firstMaterial("GLASS_PANE", "THIN_GLASS");
            case "STONE_BRICKS":
                return firstMaterial("STONE_BRICKS", "SMOOTH_BRICK");
            case "LIGHT_WEIGHTED_PRESSURE_PLATE":
                return firstMaterial("LIGHT_WEIGHTED_PRESSURE_PLATE", "GOLD_PLATE");
            case "HEAVY_WEIGHTED_PRESSURE_PLATE":
                return firstMaterial("HEAVY_WEIGHTED_PRESSURE_PLATE", "IRON_PLATE");
            case "OAK_PRESSURE_PLATE":
                return firstMaterial("OAK_PRESSURE_PLATE", "WOOD_PLATE");
            case "STONE_PRESSURE_PLATE":
                return firstMaterial("STONE_PRESSURE_PLATE", "STONE_PLATE");
            default:
                return firstMaterial(materialName);
        }
    }

    @Override
    public Sound getSound(String soundName) {
        if (soundName == null) {
            return null;
        }

        switch (soundName) {
            case Sounds.ENTITY_PLAYER_LEVELUP:
                return firstSound("ENTITY_PLAYER_LEVELUP", "LEVEL_UP");
            case Sounds.ENTITY_EXPERIENCE_ORB_PICKUP:
                return firstSound("ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP");
            case Sounds.ENTITY_ENDERMEN_TELEPORT:
                return firstSound("ENTITY_ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT", "ENDERMAN_TELEPORT");
            case Sounds.ENTITY_ITEM_PICKUP:
                return firstSound("ENTITY_ITEM_PICKUP", "ITEM_PICKUP");
            default:
                return firstSound(soundName);
        }
    }

    @Override
    public ItemStack createItemStack(Material material, int amount, short data) {
        if (material == null) {
            return new ItemStack(Material.STONE, amount);
        }
        ItemStack itemStack = new ItemStack(material, amount);
        if (data != 0) {
            try {
                itemStack.getClass().getMethod("setDurability", short.class).invoke(itemStack, data);
            } catch (Throwable ignored) {
            }
        }
        return itemStack;
    }

    @Override
    public void sendBlockChange(Player player, Location location, Material material, byte data) {
        ensureFeaturesInitialized();
        if (player == null || location == null || material == null) {
            return;
        }

        if (hasBlockDataApi && createBlockDataMethod != null && sendBlockChangeBlockDataMethod != null) {
            try {
                Object blockData = createBlockDataMethod.invoke(material);
                sendBlockChangeBlockDataMethod.invoke(player, location, blockData);
                return;
            } catch (Throwable ignored) {
            }
        }

        if (sendBlockChangeLegacyMethod != null) {
            try {
                sendBlockChangeLegacyMethod.invoke(player, location, material, data);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public boolean supportsFeature(String feature) {
        ensureFeaturesInitialized();
        switch (feature) {
            case Features.TITLE_API:
                return hasTitleApi || nmsTitleReady;
            case Features.PARTICLE_API:
                return hasParticleApi;
            case Features.MODERN_MATERIALS:
                return hasModernMaterials;
            case Features.ACTION_BAR:
                return hasSpigotActionBar || actionBarStringMethod != null || nmsTitleReady;
            case Features.SPIGOT_API:
                return spigotMethod != null;
            default:
                return false;
        }
    }

    private Material firstMaterial(String... names) {
        for (String name : names) {
            if (matchMaterialMethod != null) {
                try {
                    Material material = (Material) matchMaterialMethod.invoke(null, name);
                    if (material != null) {
                        return material;
                    }
                } catch (Throwable ignored) {
                }
            }
            try {
                return Material.valueOf(name);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return null;
    }

    private Sound firstSound(String... names) {
        for (String name : names) {
            try {
                return Sound.valueOf(name);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return null;
    }

    private Particle resolveParticle(String particleType) {
        String[] candidates;
        switch (particleType) {
            case Particles.FIREWORK:
                candidates = new String[]{"FIREWORK", "FIREWORKS_SPARK"};
                break;
            case Particles.WITCH:
                candidates = new String[]{"WITCH", "SPELL_WITCH"};
                break;
            case Particles.PORTAL:
                candidates = new String[]{"PORTAL"};
                break;
            case Particles.FLAME:
                candidates = new String[]{"FLAME"};
                break;
            default:
                candidates = new String[]{particleType};
                break;
        }

        for (String candidate : candidates) {
            try {
                return Particle.valueOf(candidate);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return null;
    }

    private Effect resolveEffect(String particleType) {
        String[] candidates;
        switch (particleType) {
            case Particles.FIREWORK:
                candidates = new String[]{"FIREWORKS_SPARK"};
                break;
            case Particles.WITCH:
                candidates = new String[]{"POTION_SWIRL", "SPELL_WITCH"};
                break;
            case Particles.PORTAL:
                candidates = new String[]{"ENDER_SIGNAL", "PORTAL"};
                break;
            case Particles.FLAME:
                candidates = new String[]{"MOBSPAWNER_FLAMES", "FLAME"};
                break;
            default:
                candidates = new String[]{particleType};
                break;
        }

        for (String candidate : candidates) {
            try {
                return Effect.valueOf(candidate);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return null;
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
