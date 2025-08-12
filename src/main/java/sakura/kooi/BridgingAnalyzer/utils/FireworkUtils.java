package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class FireworkUtils {
    public static Color getColor(int c) {
        switch (c) {
            case 1:
            default:
                return Color.AQUA;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.FUCHSIA;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIME;
            case 8:
                return Color.MAROON;
            case 9:
                return Color.NAVY;
            case 10:
                return Color.OLIVE;
            case 11:
                return Color.ORANGE;
            case 12:
                return Color.PURPLE;
            case 13:
                return Color.RED;
            case 14:
                return Color.SILVER;
            case 15:
                return Color.TEAL;
            case 16:
                return Color.WHITE;
        }
    }

    public static void shootFirework(Player player) {
        // Use cross-version compatible EntityType
        EntityType fireworkType;
        try {
            // Try modern name first (1.13+)
            fireworkType = EntityType.valueOf("FIREWORK_ROCKET");
        } catch (IllegalArgumentException e) {
            // Fallback to legacy name (1.8.8-1.12.2)
            fireworkType = EntityType.valueOf("FIREWORK");
        }

        Firework firework = (Firework) player.getWorld().spawnEntity(
                player.getLocation(), fireworkType);
        FireworkMeta fm = firework.getFireworkMeta();
        Random r = new Random();
        FireworkEffect.Type type = FireworkEffect.Type.STAR;
        int c1i = r.nextInt(16) + 1;
        int c2i = r.nextInt(16) + 1;
        Color c1 = getColor(c1i);
        Color c2 = getColor(c2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(true)
                .withColor(c1).withFade(c2).with(type).trail(true).build();
        fm.addEffect(effect);
        // int power = r.nextInt(1) + 1;
        fm.setPower(0);
        firework.setFireworkMeta(fm);
    }
}
