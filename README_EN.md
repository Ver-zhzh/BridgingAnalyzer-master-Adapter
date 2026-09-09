# BridgingAnalyzer - Bridging Practice

Minecraft Bedwars bridging practice plugin · **One universal JAR for 1.8.8 – 26.x+**

[![License](https://img.shields.io/github/license/SakuraKoi/BridgingAnalyzer?style=flat-square)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8+-orange?style=flat-square)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.8.8--26.x+-green?style=flat-square)](https://www.spigotmc.org/)

[Original by SakuraKooi](https://github.com/SakuraKoi/BridgingAnalyzer) · [中文文档](README.md) · [Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues)

## Highlights

- **Single JAR** for Spigot/Paper 1.8.8 through 26.x
- **Runtime adapter** via reflection (`UniversalAdapter`), no per-version adapter classes
- **Bridge timing** with millisecond ActionBar display for rankings
- **Trigger blocks** for checkpoints, spawn return, victory, etc.

## Quick Start

1. Download `BridgingAnalyzer-2.3.3.jar` (build or Releases)
2. Place in `plugins/` and restart
3. Run `/bridge` in-game for personal settings

### Practice area (chest supplies blocks)

```
[Emerald]   ← stand here to set checkpoint
[Any block]
[Chest]     ← put practice blocks inside
```

## Build

```bash
git clone https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter.git
cd BridgingAnalyzer-master-Adapter
mvn clean package -DskipTests
# output: target/BridgingAnalyzer-2.3.3.jar
```

Requires Java 8+, Maven 3.6+, and Lombok in your IDE.

## Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/bridge` | default | Personal settings (highlight/pvp/speed/stand/time) |
| `/clearblock [player]` | `bridginganalyzer.clear` | Clear placed blocks |
| `/imstuck` | default | Unstuck helper |
| `/genvillager` | OP | Create villager target spawn point |
| `/bsaveworld` | OP | Clear blocks and save world |

## Trigger Blocks

| Block | Effect |
|-------|--------|
| Emerald | Set checkpoint |
| Redstone | Return to checkpoint (victory) |
| Lapis | Return to world spawn |
| Melon | Knockback practice |
| Beacon | Vertical teleport |
| Gold pressure plate | Speed boost |

## v2.3.x Changelog (summary)

- **2.3.3** Bridge timer at 2-tick ActionBar refresh; CPS/distance throttled to 4 ticks
- **2.3.0** One-hit villager target, delayed respawn, no death log spam
- **2.2.x** Paper 1.21/26.x movement fix, chest loading, block replenish, no break drops
- **2.2.0** Universal single JAR, calendar version 26.x support

## Credits

- **Original author**: [SakuraKooi](https://github.com/SakuraKooi)
- **Adapter & maintenance**: [Ver_zhzh](https://github.com/Ver-zhzh)

## License

GPL-2.0 — see [LICENSE](LICENSE). Preserve author credit and publish source when redistributing modified builds.
