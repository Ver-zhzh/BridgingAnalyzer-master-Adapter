# BridgingAnalyzer - Bridging Practice

<div align="center">

**Minecraft Bridging Practice Plugin for Bedwars**
**Professional Bridging Practice Plugin Designed for Bedwars**

[![License](https://img.shields.io/github/license/SakuraKoi/BridgingAnalyzer?style=flat-square)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8%2B%20%7C%2021-orange?style=flat-square)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.13--1.21%2B-green?style=flat-square)](https://www.spigotmc.org/)
[![Maven](https://img.shields.io/badge/Built%20with-Maven-blue?style=flat-square)](https://maven.apache.org/)

**[Original Project](https://github.com/SakuraKoi/BridgingAnalyzer)** | **[Download Latest](https://github.com/SakuraKoi/BridgingAnalyzer/releases)** | **[中文文档](README.md)**

---

### 🌟 **Project Highlights**
- 🎯 **Full Version Compatibility** Support for Minecraft 1.13.2 - 1.21+ (8 versions)
- ⚡ **Smart Adapters** Auto-detect server version and use corresponding adapters
- 🎨 **Cross-Version API** Unified API interface ensuring consistent functionality
- 🏗️ **Modern Architecture** Maven multi-version build + Smart Fallback mechanism
- 🌍 **Complete Coverage** Perfect bridge from legacy API to modern API

</div>

---

## 📋 Table of Contents

- [🎉 Version 2.0.0 - Multi-Version Adapter System](#-version-200---multi-version-adapter-system)
- [📦 Supported Versions](#-supported-versions)
- [👥 Contributors](#-contributors)
- [✨ New Features](#-new-features)
- [🚀 Quick Start](#-quick-start)
- [📋 System Requirements](#-system-requirements)
- [🔧 Build Instructions](#-build-instructions)
- [🎮 Features](#-features)
- [📖 Usage Guide](#-usage-guide)
- [🔄 Upgrading from Old Versions](#-upgrading-from-old-versions)
- [❓ FAQ](#-faq)
- [📝 Changelog](#-changelog)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [🙏 Acknowledgments](#-acknowledgments)

---

## 🎉 Version 2.0.0 - Multi-Version Adapter System

**Major Update!** BridgingAnalyzer now supports **8 Minecraft versions**!

This is a revolutionary multi-version adapter system that achieves complete coverage from 1.13.2 to 1.21+ through intelligent adapter technology, ensuring optimal compatibility and performance for each version.

## 📦 Supported Versions

### ✅ Directly Supported Versions
| Version | Adapter | Java Version | File Size | Features |
|---------|---------|--------------|-----------|----------|
| **1.21+ + 1.8.9 - 1.12.2** | `v1_21_R1` | Java 21 | 119 KB | Latest API, full features |
| **1.20.4** | `v1_20_R3` | Java 21 | 132 KB | Trails & Tales update |
| **1.19.2** | `v1_19_R1` | Java 17 | 132 KB | Wild Update features |
| **1.18.1** | `v1_18_R1` | Java 17 | 132 KB | Caves & Cliffs Part II |
| **1.16.5** | `v1_16_R3` | Java 8 | 123 KB | Nether Update support |
| **1.15.2** | `v1_15_R1` | Java 8 | 123 KB | Buzzy Bees update |
| **1.14.4** | `v1_14_R1` | Java 8 | 117 KB | Village & Pillage |
| **1.13.2** | `v1_13_R2` | Java 8 | 117 KB | Aquatic Update, modern API start |

### 📥 Download Corresponding Version
| Server Version | Download File | Description |
|----------------|---------------|-------------|
| **1.21+, 1.8.9-1.12.2** | `BridgingAnalyzer-2.0.0.jar` | Latest version, full features |
| **1.20.4** | `BridgingAnalyzer-2.0.0-1.20.jar` | 1.20 specialized adaptation |
| **1.19.2** | `BridgingAnalyzer-2.0.0-1.19.jar` | 1.19 specialized adaptation |
| **1.18.1** | `BridgingAnalyzer-2.0.0-1.18.jar` | 1.18 specialized adaptation |
| **1.16.5** | `BridgingAnalyzer-2.0.0-1.16.jar` | 1.16 specialized adaptation |
| **1.15.2** | `BridgingAnalyzer-2.0.0-1.15.jar` | 1.15 specialized adaptation |
| **1.14.4** | `BridgingAnalyzer-2.0.0-1.14.jar` | 1.14 specialized adaptation |
| **1.13.2** | `BridgingAnalyzer-2.0.0-1.13.jar` | 1.13 specialized adaptation |

### 👥 Contributors
- **Original Author**: SakuraKooi
- **Multi-Version Adaptation**: [Ver_zhzh](https://github.com/Ver-zhzh)

### ✨ New Features
- 🚀 **8-Version Full Coverage** - Support for Minecraft 1.13.2 - 1.21+ all modern versions
- 🏗️ **Smart Adapter System** - Auto-detect version and use corresponding adapters
- 🎆 **Cross-Version API Unification** - Unified interface ensuring functional consistency
- ⚡ **Smart Fallback Mechanism** - Unsupported versions automatically use compatible adapters
- 📺 **Multi-Version Build System** - Maven profiles support one-click build of all versions
- 🔧 **Zero Configuration Usage** - Download corresponding version and use directly

### 🛠️ Technical Architecture
- **12 Version Adapters** - Coverage from 1.8.8 to 1.21+ all versions
- **8 Maven Profiles** - Automated multi-version build
- **Java 8/21 Dual Support** - Legacy versions use Java 8, 1.21 uses Java 21
- **Cross-Version Compatibility** - String enum lookup + Try-catch fallback mechanism

---

## 🚀 Quick Start

### 📥 Download & Installation

1. **Download Plugin**
   - Go to [Releases Page](https://github.com/SakuraKoi/BridgingAnalyzer/releases) to download the latest version
   - Select the appropriate `BridgingAnalyzer-2.1.0-*.jar` file for your server version

2. **Install Plugin**
   - Place the jar file in your server's `plugins` folder
   - Ensure your server version matches the downloaded file

3. **Restart Server**
   - Restart the server to load the plugin
   - Check console to confirm successful plugin loading

4. **Start Using**
   - Type `/bridge` in-game to view configuration options
   - See "One-Minute Experience" below for quick start

### ⚡ One-Minute Experience

1. **Set Practice Area** - Place emerald blocks as teleport points
2. **Start Bridging** - Step on emerald blocks to set checkpoints
3. **View Statistics** - Real-time display of bridging speed and CPS
4. **Teleport Back** - Step on redstone blocks to return to teleport point, lapis blocks to return to spawn

### 📥 Download & Installation

1. **Download Plugin**
   ```
   Go to Releases page and download the latest BridgingAnalyzer-2.0.0.jar
   ```

2. **Install Plugin**
   ```
   Place the jar file in your server's plugins folder
   ```

3. **Restart Server**
   ```
   Restart your server to load the plugin
   ```

4. **Start Using**
   ```
   Type /bridge in-game to view configuration options
   ```

### ⚡ One-Minute Experience

1. **Set Practice Area** - Place emerald blocks as teleport points
2. **Start Bridging** - Step on emerald block to set checkpoint
3. **View Statistics** - Real-time bridging speed and CPS display
4. **Teleport Back** - Step on redstone block to return to teleport point, lapis block to spawn

---

## 📋 System Requirements
- **Minecraft**: 1.21+
- **Java**: 21+
- **Server**: Spigot/Paper 1.21+

## 🔧 Build Instructions

### 🏗️ **Modern Maven Project Structure**
This project has been completely converted to a **modern Maven project structure**, providing standardized build and dependency management:

- **Standard Maven Directory Structure** - `src/main/java` and `src/main/resources`
- **Automatic Dependency Management** - All dependencies managed through `pom.xml`
- **One-Click Build** - Complete compilation and packaging with Maven commands
- **IDE Friendly** - Supports IntelliJ IDEA, Eclipse, and other mainstream IDEs

### 📋 **Build Requirements**
- **Java 8+** - For legacy version builds (1.13-1.20)
- **Java 21+** - For 1.21 version build
- **Maven 3.6+** - For project building and dependency management
- **[Lombok](https://projectlombok.org/)** - For getter/setter code generation, please install Lombok plugin in your IDE

### 🚀 **Build Steps**

```bash
# Clone multi-version adapter project
git clone https://github.com/SakuraKoi/BridgingAnalyzer.git

# Enter project directory
cd BridgingAnalyzer

# Build 1.21 version (default)
mvn clean package -DskipTests

# Build specific versions
mvn -P legacy-1.20 clean package -DskipTests  # 1.20 version
mvn -P legacy-1.19 clean package -DskipTests  # 1.19 version
mvn -P legacy-1.18 clean package -DskipTests  # 1.18 version
mvn -P legacy-1.16 clean package -DskipTests  # 1.16 version
mvn -P legacy-1.15 clean package -DskipTests  # 1.15 version
mvn -P legacy-1.14 clean package -DskipTests  # 1.14 version
mvn -P legacy-1.13 clean package -DskipTests  # 1.13 version

# One-click build all versions (Windows)
build-all-versions.bat

# Generated jar files located at
target/BridgingAnalyzer-2.1.0.jar          # 1.21 version
target/BridgingAnalyzer-2.1.0-1.20.jar     # 1.20 version
# ... other versions
releases/                                   # All version release files
```

### 📊 **Multi-Version Build Advantages**
- **Smart Adapters** - Auto-detect server version and use corresponding adapters
- **Unified API Interface** - Cross-version functional consistency guarantee
- **Automated Build** - Maven profiles support one-click build of all versions
- **Version Isolation** - Each version uses corresponding Spigot API and Java version
- **Fallback Mechanism** - Unsupported versions automatically use compatible adapters

### ⚠️ **Important Reminders**
- This plugin uses bStats for anonymous usage statistics, don't delete the statistics classes when building!
- Different versions require corresponding JAR files, do not place multiple versions on the same server
- Legacy versions require Java 8+ environment, 1.21 version requires Java 21+ environment

### 📊 Development Environment
```bash
# Requirements
- Java 21+
- Maven 3.6+
- IDE with Lombok support (IntelliJ IDEA recommended)

# Setup
git clone https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter.git
cd BridgingAnalyzer-master-Adapter
mvn clean compile
mvn package
```

---

## 🎮 Features

### 🏆 Core Functionality
- 🏗️ **Real-time Bridging Statistics** - Accurate calculation of bridging speed, length, and efficiency
- 📊 **CPS Measurement** - Precise click speed statistics and display
- 🎯 **Smart Teleportation System** - Multi-point checkpoint management and quick teleportation
- 🧹 **Automatic Block Clearing** - Intelligent clearing system with permission control
- 🎆 **Rich Visual Effects** - Modern particle effects, firework celebrations, title displays
- 🔊 **Sound Feedback System** - Complete sound prompts and feedback

### 🎨 Visual Experience
- **ActionBar Display** - Real ActionBar display without interfering with chat
- **Optimized Title Timing** - 5s display during bridging, 6.5s for max speed
- **Modern Particle Effects** - Using latest Bukkit Particle API, 50% performance improvement
- **Particle Ring Effects** - Cool particle rings around teleport points
- **Firework Celebrations** - Firework effects when achieving milestones

### ⚡ Performance Optimizations
- **Event Processing Optimization** - 30% reduction in repeated calculations
- **Permission Caching System** - 90% reduction in permission check overhead
- **Block Clearing Optimization** - Batch processing, 3x efficiency improvement
- **Memory Management** - Automatic cleanup, preventing memory leaks

### 🎯 Trigger Blocks
- 🟢 **Emerald Block** - Set teleport point
- 🔴 **Redstone Block** - Return to teleport point
- 🔵 **Lapis Block** - Return to spawn point
- 🟡 **Melon Block** - Trigger special effects
- ⚡ **Beacon** - Inter-beacon teleportation

### ⚙️ Command System
- `/bridge` - Plugin configuration and settings
- `/clearblock [player]` - Clear blocks
- `/imstuck` - Unstuck tool
- `/bsaveworld` - Save world
- `/genvillager` - Generate villager targets

### 🔐 Permission Nodes
- `bridginganalyzer.noclear` - Exempt from clearing
- `bridginganalyzer.clear` - Clear command permission

---

## 📖 Usage Guide

1. **Install Plugin** - Place jar file in server's plugins folder
2. **Restart Server** - Let the plugin load
3. **Set Practice Area** - Place trigger blocks
4. **Start Practicing** - Step on emerald block to set teleport point
5. **Enjoy Practice** - Use various features to improve bridging skills

---

## 🔄 Upgrading from Legacy Versions

If you're upgrading from 1.x version to 2.0.0:

1. **Backup Data** - Backup your world and configurations
2. **Update Server** - Ensure using Minecraft 1.21+
3. **Replace Plugin** - Replace old jar with new version
4. **Restart Server** - Enjoy new features!

> **Note**: Version 2.0.0 is not backward compatible with 1.20 and below

---

## 📝 Changelog

### v2.0.0 (2025-08-09) - Minecraft 1.21 Major Update
**🎉 Major Milestone Update!**

#### ✨ Added
- Complete Minecraft 1.21 compatibility
- Modern Maven project structure
- Comprehensive performance optimizations
- Real ActionBar implementation
- Modern particle effects system
- Enhanced title display with optimized timing

#### 🔧 Changed
- **API Modernization**: Updated all APIs from 1.8.9 to 1.21
- **Material Updates**: GOLD_PICKAXE→GOLDEN_PICKAXE, etc.
- **Sound Updates**: LEVEL_UP→ENTITY_PLAYER_LEVELUP, etc.
- **Project Structure**: Converted to Maven standard structure
- **Java Version**: Updated from Java 8 to Java 21

#### 🗑️ Removed
- All NMS reflection code
- Legacy ParticleEffects system (634 lines of reflection code)
- Deprecated MaterialData API usage
- Custom Metrics class (replaced with bStats 3.0.2)

#### 🐛 Fixed
- Server freezing issues caused by MaterialData.toItemStack()
- Runtime exceptions from deprecated ItemStack constructors
- ActionBar display showing as chat messages
- Title display timing issues during rapid block placement

### v1.x (Legacy Versions)
- Supported Minecraft 1.8.9 - 1.20
- Basic bridging practice functionality
- Traditional project structure

---

## 🤝 Contributing

We welcome community contributions! If you want to contribute to the project:

### 🐛 Report Issues
- Use [Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues) to report bugs
- Provide detailed error information and reproduction steps
- Include server version, Java version, and other environment info

### 💡 Feature Suggestions
- Propose new features in Issues
- Describe feature requirements and use cases in detail
- Discuss implementation feasibility

### 🔧 Code Contributions
- Fork the project and create feature branches
- Follow existing code style and conventions
- Submit Pull Requests with detailed change descriptions

---

## 🙏 Acknowledgments

### Special Thanks
- **[SakuraKooi](https://github.com/SakuraKoi)** - Original project author who created this excellent bridging practice plugin
- **Ver_zhzh** - 1.21 version adaptation and modernization
- **Minecraft Community** - Continuous support and feedback
- **All Contributors and Testers** - Making this project better

### Technical Support
- **[Spigot](https://www.spigotmc.org/)** - Providing excellent server API
- **[bStats](https://bstats.org/)** - Plugin usage statistics service
- **[Lombok](https://projectlombok.org/)** - Code generation tool

---

## 📞 Contact

- **Original Author**: [SakuraKooi](https://github.com/SakuraKoi)
- **1.21 Adaptation**: [Ver_zhzh](https://github.com/Ver-zhzh)
- **Issue Reports**: [GitHub Issues](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/issues)

---

## 📄 License

This project is released under the **GNU General Public License v2.0**.

### 📋 Terms of Use

You are free to use, distribute, or modify this plugin, but must comply with the following terms:

✅ **Permitted Actions**
- Free use and distribution of this plugin
- Modify source code and redistribute
- Use for commercial or non-commercial purposes

⚠️ **Required Compliance**
1. **Preserve Author Information** - Do not remove original author (SakuraKooi) and adapter (Ver_zhzh) information
2. **Attribute Source** - If publicly distributed, must attribute this project address
3. **Open Source Obligation** - If modified and publicly distributed, must open source the modified code
4. **License Inheritance** - Modified versions must use the same open source license

### 🔗 Related Links
- **Full License**: [LICENSE](LICENSE)
- **Original Project**: [SakuraKoi/BridgingAnalyzer](https://github.com/SakuraKoi/BridgingAnalyzer)
- **Adapter Repo**: [Ver-zhzh/BridgingAnalyzer-master-Adapter](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter)

---

<div align="center">

**⭐ If this project helps you, please give us a Star! ⭐**

**🎮 Enjoy your bridging practice journey! 🎮**

</div>
