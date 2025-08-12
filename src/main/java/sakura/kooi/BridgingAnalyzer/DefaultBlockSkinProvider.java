package sakura.kooi.BridgingAnalyzer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sakura.kooi.BridgingAnalyzer.api.BlockSkinProvider;

public class DefaultBlockSkinProvider implements BlockSkinProvider {
    @Override
    public ItemStack provide(Player player) {
        // Use version adapter for cross-version material compatibility
        Material material = sakura.kooi.BridgingAnalyzer.api.VersionManager.getAdapter().getMaterial("SMOOTH_SANDSTONE");
        if (material == null) {
            material = Material.SANDSTONE; // Fallback for 1.8.8
        }
        return new ItemStack(material, 64);
    }
}
