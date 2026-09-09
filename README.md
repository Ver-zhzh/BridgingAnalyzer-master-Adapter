# BridgingAnalyzer 搭路练习

Minecraft 起床战争搭路练习插件 · 通用单 Jar，支持 **1.8.8 – 26.x+**

[![License](https://img.shields.io/github/license/SakuraKoi/BridgingAnalyzer?style=flat-square)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8+-orange?style=flat-square)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.8.8--26.x+-green?style=flat-square)](https://www.spigotmc.org/)

[原项目 SakuraKoi/BridgingAnalyzer](https://github.com/SakuraKoi/BridgingAnalyzer) · [English README](README_EN.md) · [Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues)

## 亮点

- **一个 Jar 全版本**：无需按版本选包，Spigot/Paper 1.8.8 至 26.x 均可
- **反射适配**：`UniversalAdapter` 运行时探测 API，无 per-version 适配器类
- **搭路记时**：毫秒级 ActionBar 计时，适合排名练习
- **触发方块**：绿宝石设点、红石回点、青金石回出生点等

## 快速开始

1. 下载 `BridgingAnalyzer-2.3.3.jar`（`mvn package` 或 Releases）
2. 放入服务器 `plugins/` 并重启
3. 玩家输入 `/bridge` 查看个人设置

### 练习区结构（箱子给物）

```
[绿宝石]  ← 踩上设传送点
[任意方块]
[箱子]    ← 放入练习用方块
```

## 构建

```bash
git clone https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter.git
cd BridgingAnalyzer-master-Adapter
mvn clean package -DskipTests
# 输出: target/BridgingAnalyzer-2.3.3.jar
```

Windows 也可运行 `build_all_versions.bat`（同样只打一个 Jar）。

**要求**：Java 8+、Maven 3.6+、IDE 需安装 Lombok 插件。

## 命令

| 命令 | 权限 | 说明 |
|------|------|------|
| `/bridge` | 默认 | 个人练习设置（highlight/pvp/speed/stand/time） |
| `/clearblock [玩家]` | `bridginganalyzer.clear` | 清除已放置方块 |
| `/imstuck` | 默认 | 卡住时自救 |
| `/genvillager` | OP | 创建村民靶子刷新点 |
| `/bsaveworld` | OP | 清方块并保存世界 |

## 触发方块

| 方块 | 功能 |
|------|------|
| 绿宝石 | 设置传送点 |
| 红石 | 回到传送点（胜利） |
| 青金石 | 回到出生点 |
| 西瓜 | 击退练习 |
| 信标 | 上下传送 |
| 金压力板 | 速度效果 |

## 版本 2.3.x 更新摘要

- **2.3.3** 计时 ActionBar 恢复 2 tick 精度；CPS/距离仍 4 tick 节流
- **2.3.0** 靶子一击死亡、延迟复活、无死亡刷屏
- **2.2.x** 修复 Paper 1.21/26.x 移动拉回、箱子读物、方块补回、挖方块不掉落
- **2.2.0** 合并为通用单 Jar，支持 26.x 日历版本号

## 贡献者

- **原作者**：[SakuraKooi](https://github.com/SakuraKooi)
- **适配与维护**：[Ver_zhzh](https://github.com/Ver-zhzh)

## 许可证

本项目基于 [GPL-2.0](LICENSE) 发布。修改再分发时请保留原作者信息并公开源码。
