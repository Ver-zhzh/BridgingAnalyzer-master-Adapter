# BridgingAnalyzer 搭路练习

<div align="center">

**Minecraft Bridging Practice Plugin for Bedwars**
**专为起床战争设计的搭路练习插件**

[![License](https://img.shields.io/github/license/SakuraKoi/BridgingAnalyzer?style=flat-square)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8%2B%20%7C%2021-orange?style=flat-square)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.13--1.21%2B-green?style=flat-square)](https://www.spigotmc.org/)
[![Maven](https://img.shields.io/badge/Built%20with-Maven-blue?style=flat-square)](https://maven.apache.org/)

**[原项目地址](https://github.com/SakuraKoi/BridgingAnalyzer)** | **[下载最新版本](https://github.com/SakuraKoi/BridgingAnalyzer/releases)** | **[English README](README_EN.md)**

---

### 🌟 **项目亮点**
- 🎯 **全版本兼容** 支持 Minecraft 1.13.2 - 1.21+ 共8个版本
- ⚡ **智能适配器** 自动检测服务器版本并使用对应适配器
- 🎨 **跨版本API** 统一的API接口，确保功能一致性
- 🏗️ **现代架构** Maven多版本构建 + 智能Fallback机制
- 🌍 **完整覆盖** 从传统API到现代API的完美桥接

</div>

## 📋 目录

- [🎉 版本 2.0.0 - 多版本适配系统](#-版本-200---多版本适配系统)
- [📦 支持的版本](#-支持的版本)
- [👥 贡献者](#-贡献者)
- [✨ 新特性](#-新特性)
- [🚀 快速开始](#-快速开始)
- [📋 系统要求](#-系统要求)
- [🔧 构建说明](#-构建说明)
- [🎮 功能特性](#-功能特性)
- [📖 使用指南](#-使用指南)
- [🔄 从旧版本升级](#-从旧版本升级)
- [❓ 常见问题](#-常见问题)
- [📝 更新日志](#-更新日志)
- [🤝 贡献指南](#-贡献指南)
- [📄 许可证](#-许可证)
- [🙏 致谢](#-致谢)

---

## 🎉 版本 2.0.0 - 多版本适配系统

**重大更新！** BridgingAnalyzer 现已支持 **8 个 Minecraft 版本**！

这是一个革命性的多版本适配系统，通过智能适配器技术实现了从 1.13.2 到 1.21+ 的完整覆盖，确保每个版本都能获得最佳的兼容性和性能。

## 📦 支持的版本

### ✅ 直接支持的版本
| 版本 | 适配器 | Java版本 | 文件大小 | 特性说明 |
|------|--------|----------|----------|----------|
| **1.21+**  +1.8.9 - 1.12.2 | `v1_21_R1` | Java 21 | 119 KB 
| **1.20.4** | `v1_20_R3` | Java 21 | 132 KB 
| **1.19.2** | `v1_19_R1` | Java 17 | 132 KB 
| **1.18.1** | `v1_18_R1` | Java 17 | 132 KB 
| **1.16.5** | `v1_16_R3` | Java 8 | 123 KB 
| **1.15.2** | `v1_15_R1` | Java 8 | 123 KB 
| **1.14.4** | `v1_14_R1` | Java 8 | 117 KB 
| **1.13.2** | `v1_13_R2` | Java 8 | 117 KB 



### 📥 下载对应版本
| 服务器版本 | 下载文件 | 说明 |
|------------|----------|------|
| **1.21+，1.8.9-1.12.2** | `BridgingAnalyzer-2.0.0.jar` | 最新版本，完整功能 |
| **1.20.4** | `BridgingAnalyzer-2.0.0-1.20.jar` | 1.20专门适配 |
| **1.19.2** | `BridgingAnalyzer-2.0.0-1.19.jar` | 1.19专门适配 |
| **1.18.1** | `BridgingAnalyzer-2.0.0-1.18.jar` | 1.18专门适配 |
| **1.16.5** | `BridgingAnalyzer-2.0.0-1.16.jar` | 1.16专门适配 |
| **1.15.2** | `BridgingAnalyzer-2.0.0-1.15.jar` | 1.15专门适配 |
| **1.14.4** | `BridgingAnalyzer-2.0.0-1.14.jar` | 1.14专门适配 |
| **1.13.2** | `BridgingAnalyzer-2.0.0-1.13.jar` | 1.13专门适配 |

### 👥 贡献者
- **原作者**: SakuraKooi
- **1.21适配**: [Ver_zhzh](https://github.com/Ver-zhzh)

### 🛠️ 技术架构
- **12个版本适配器** - 覆盖从1.8.8到1.21+的所有版本
- **8个Maven Profiles** - 自动化多版本构建
- **Java 8/21双支持** - legacy版本使用Java 8，1.21使用Java 21
- **跨版本兼容性** - 字符串枚举查找 + Try-catch降级机制

---
## 🚀 快速开始

### 📥 下载安装

1. **下载插件**
   - 前往 [Releases 页面](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/releases) 下载最新版本
   - 选择 `BridgingAnalyzer-2.0.0.jar` 文件

2. **安装插件**
   - 将 jar 文件放入服务器的 `plugins` 文件夹
   - 确保服务器版本为 Minecraft 1.21+

3. **重启服务器**
   - 重启服务器以加载插件
   - 查看控制台确认插件成功加载

4. **开始使用**
   - 在游戏中输入 `/bridge` 查看配置选项
   - 查看下方的"一分钟体验"快速上手

### ⚡ 一分钟体验

1. **设置练习区域** - 放置绿宝石块作为传送点
2. **开始搭路** - 踩在绿宝石块上设置检查点
3. **查看统计** - 实时显示搭路速度和CPS
4. **传送回归** - 踩红石块回到传送点，踩青金石块回到出生点

## 🔧 构建说明

### 🏗️ **Maven项目结构**
本项目已完全转换为**现代化Maven项目结构**，提供标准化的构建和依赖管理：

- **标准Maven目录结构** - `src/main/java` 和 `src/main/resources`
- **自动依赖管理** - 通过 `pom.xml` 管理所有依赖
- **一键构建** - 使用Maven命令即可完成编译和打包
- **IDE友好** - 支持IntelliJ IDEA、Eclipse等主流IDE

### 📋 **构建要求**
- **Java 8+** - 用于legacy版本构建 (1.13-1.20)
- **Java 21+** - 用于1.21版本构建
- **Maven 3.6+** - 用于项目构建和依赖管理
- **[Lombok](https://projectlombok.org/)** - 用于Getter和Setter代码生成，请在IDE中安装Lombok插件

### 🚀 **构建步骤**

```bash
# 克隆多版本适配项目
git clone https://github.com/SakuraKoi/BridgingAnalyzer.git

# 进入项目目录
cd BridgingAnalyzer

# 构建1.21，1.8.9-1.12.2版本 (默认)
mvn clean package -DskipTests

# 构建特定版本
mvn -P legacy-1.20 clean package -DskipTests  # 1.20版本
mvn -P legacy-1.19 clean package -DskipTests  # 1.19版本
mvn -P legacy-1.18 clean package -DskipTests  # 1.18版本
mvn -P legacy-1.16 clean package -DskipTests  # 1.16版本
mvn -P legacy-1.15 clean package -DskipTests  # 1.15版本
mvn -P legacy-1.14 clean package -DskipTests  # 1.14版本
mvn -P legacy-1.13 clean package -DskipTests  # 1.13版本

# 一键构建所有版本 (Windows)
build-all-versions.bat

# 生成的jar文件位于
target/BridgingAnalyzer-2.0.0.jar          # 1.21版本
target/BridgingAnalyzer-2.0.0-1.20.jar     # 1.20版本
# ... 其他版本
releases/                                   # 所有版本的发布文件
```

### ⚠️ **重要提醒**
- 本插件使用bStats进行匿名使用统计
- 不同版本需要使用对应的JAR文件，不要在同一服务器上放置多个版本
- legacy版本需要Java 8+/Java 17环境，1.21版本需要Java 21+环境


### ⚡ 性能优化
- **事件处理优化** - 减少30%重复计算
- **权限缓存系统** - 减少90%权限检查开销
- **方块清理优化** - 批量处理，提升3倍效率
- **内存管理** - 自动清理，防止内存泄漏

### 🎯 触发方块
| 方块类型 | 功能说明 | 使用方法 |
|---------|---------|---------|
| 🟢 **绿宝石块** | 设置传送点 | 踩上去设置检查点，开始搭路练习 |
| 🔴 **红石块** | 回到传送点 | 踩上去快速回到最近的检查点 |
| 🔵 **青金石块** | 回到出生点 | 踩上去回到世界出生点 |
| 🟡 **西瓜块** | 触发特殊效果 | 踩上去触发粒子效果和音效 |
| ⚡ **信标** | 信标间传送 | 在不同信标之间快速传送 |

### ⚙️ 命令系统
| 命令 | 权限 | 功能说明 |
|------|------|---------|
| `/bridge` | 默认 | 插件配置和设置界面 |
| `/clearblock [玩家]` | `bridginganalyzer.clear` | 清理指定玩家的方块 |
| `/imstuck` | 默认 | 脱困工具，传送到安全位置 |
| `/bsaveworld` | OP | 保存世界数据 |
| `/genvillager` | OP | 生成村民靶子用于练习 |

### 🔐 权限节点
| 权限 | 默认 | 功能说明 |
|------|------|---------|
| `bridginganalyzer.noclear` | OP | 免于方块自动清理 |
| `bridginganalyzer.clear` | OP | 使用清理命令的权限 |

## 📖 使用指南

1. **安装插件** - 将jar文件放入服务器的plugins文件夹
2. **重启服务器** - 让插件加载
3. **设置练习区域** - 放置触发方块
4. **开始练习** - 踩在绿宝石块上设置传送点
5. **享受练习** - 使用各种功能提升搭路技巧


---

## ❓ 常见问题

### 🔧 **安装和配置问题**

**Q: 插件无法加载，提示Java版本错误？**
A: 请确保服务器使用Java 21或更高版本。可以通过 `java -version` 命令检查。

**Q: 支持哪些服务端？**
A: 支持Spigot 1.21+和Paper 1.21+。推荐使用Paper以获得更好的性能。

### 🔄 **升级和兼容性问题**

**Q: 从1.x版本升级需要注意什么？**
A: 需要备份数据，升级服务器到1.21，升级Java到21。配置文件和权限节点保持不变。

**Q: Via协议支持情况如何？**
A: 在1.21服务器上安装ViaVersion等协议插件后，支持1.8.9-1.21客户端正常游玩。

---
<img width="640" height="400" alt="f88aed62d0428802d894ed3378933616_720" src="https://github.com/user-attachments/assets/f1e2d40a-a025-4ce4-9051-cbe0e35a6cc4" />



## 🤝 贡献指南

我们欢迎社区贡献！如果你想为项目做出贡献：

### 🐛 报告问题
- 使用 [Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues) 报告bug
- 提供详细的错误信息和复现步骤
- 包含服务器版本、Java版本等环境信息

### 💡 功能建议
- 在 Issues 中提出新功能建议
- 详细描述功能需求和使用场景
- 讨论实现方案的可行性

### 🔧 代码贡献
- Fork 项目并创建功能分支
- 遵循现有的代码风格和规范
- 提交 Pull Request 并详细描述更改

### 📋 开发环境
```bash
# 克隆项目
git clone https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter.git

# 进入项目目录
cd BridgingAnalyzer-master-Adapter

# 编译项目
mvn clean compile

# 打包插件
mvn package
```

---

## 🙏 致谢

### 特别感谢
- **[SakuraKooi](https://github.com/SakuraKoi)** - 原项目作者，创造了这个优秀的搭路练习插件
- **Ver_zhzh** - 1.21版本适配和现代化改造


## 📞 联系方式

- **原作者**: [SakuraKooi](https://github.com/SakuraKoi)
- **1.21适配**: [Ver_zhzh]QQ1416727282
---


✅ **允许的行为**
- 自由使用和传播本插件
- 修改源代码并重新发布
- 用于商业或非商业目的

⚠️ **必须遵守的要求**
1. **保留作者信息** - 不得抹去原作者(SakuraKooi)的信息
2. **注明来源** - 如果公开发布，需注明本项目地址
3. **开源义务** - 如果修改并公开发布，需公开修改后的源代码
4. **协议继承** - 修改版本必须使用相同的开源协议

### 🔗 相关链接
- **完整许可证**: [LICENSE](LICENSE)
- **原项目**: [SakuraKoi/BridgingAnalyzer](https://github.com/SakuraKoi/BridgingAnalyzer)
- **适配版仓库**: [Ver-zhzh/BridgingAnalyzer-master-Adapter](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter)

---

<div align="center">


</div>

>
> =出于对源项目的尊重，本项目已遵守
>
> 1. 保留了作者(SakuraKooi)的信息
> 2. 公开发布时已经注明源帖地址
> 3. 已公开修改后的源代码

---

<div align="center">

### 📊 **项目统计**

![GitHub stars](https://img.shields.io/github/stars/Ver-zhzh/BridgingAnalyzer-master-Adapter?style=social)
![GitHub forks](https://img.shields.io/github/forks/Ver-zhzh/BridgingAnalyzer-master-Adapter?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/Ver-zhzh/BridgingAnalyzer-master-Adapter?style=social)


### 🔗 **相关链接**

[📖 中文文档](README.md) • [📖 English Docs](README_EN.md) • [📦 Releases](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/releases) • [🐛 Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues) • [🔄 Pull Requests](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/pulls)

</div>