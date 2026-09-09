package sakura.kooi.BridgingAnalyzer.api;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Minecraft server versions from legacy 1.x to calendar-year 26.x+ formats.
 */
public final class ServerVersion {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    private final int major;
    private final int minor;
    private final int patch;
    private final boolean calendarFormat;
    private final String displayVersion;

    private ServerVersion(int major, int minor, int patch, boolean calendarFormat, String displayVersion) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.calendarFormat = calendarFormat;
        this.displayVersion = displayVersion;
    }

    public static ServerVersion current() {
        return parse(Bukkit.getBukkitVersion());
    }

    public static ServerVersion parse(String bukkitVersion) {
        if (bukkitVersion == null || bukkitVersion.isEmpty()) {
            return new ServerVersion(0, 0, 0, false, "unknown");
        }

        String numeric = bukkitVersion.split("-")[0];
        Matcher matcher = VERSION_PATTERN.matcher(numeric);
        if (!matcher.find()) {
            return new ServerVersion(0, 0, 0, false, numeric);
        }

        int first = Integer.parseInt(matcher.group(1));
        int second = Integer.parseInt(matcher.group(2));
        int third = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));

        if (first >= 26) {
            String display = third > 0 ? first + "." + second + "." + third : first + "." + second;
            return new ServerVersion(first, second, third, true, display);
        }

        if (first == 1) {
            String display = third > 0 ? first + "." + second + "." + third : first + "." + second;
            return new ServerVersion(first, second, third, false, display);
        }

        return new ServerVersion(first, second, third, first > 1, numeric);
    }

    public static String detectCraftPackageVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String suffix = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (suffix.startsWith("v")) {
            return suffix;
        }
        return null;
    }

    public boolean isCalendarFormat() {
        return calendarFormat;
    }

    public boolean isLegacyFormat() {
        return !calendarFormat && major == 1;
    }

    public boolean isAtLeast(int targetMajor, int targetMinor) {
        if (major != targetMajor) {
            return major > targetMajor;
        }
        if (minor != targetMinor) {
            return minor > targetMinor;
        }
        return patch >= 0;
    }

    public boolean isAtLeast(int targetMajor, int targetMinor, int targetPatch) {
        if (major != targetMajor) {
            return major > targetMajor;
        }
        if (minor != targetMinor) {
            return minor > targetMinor;
        }
        return patch >= targetPatch;
    }

    public boolean supportsModernBukkitApi() {
        if (calendarFormat) {
            return true;
        }
        return isAtLeast(1, 13);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    @Override
    public String toString() {
        return displayVersion;
    }
}
