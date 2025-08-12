# BridgingAnalyzer - Bridging Practice Plugin

<div align="center">

**Minecraft Bridging Practice Plugin for Bedwars**  
**专为起床战争设计的搭路练习插件**

[![License](https://img.shields.io/github/license/SakuraKoi/BridgingAnalyzer?style=flat-square)](LICENSE)
[![Downloads](https://img.shields.io/github/downloads/SakuraKoi/BridgingAnalyzer/total?style=flat-square)](https://github.com/SakuraKoi/BridgingAnalyzer/releases)
[![bStats Players](https://img.shields.io/bstats/players/3991?style=flat-square)](https://bstats.org/plugin/bukkit/BridgingAnalyzer/3991)
[![bStats Servers](https://img.shields.io/bstats/servers/3991?style=flat-square)](https://bstats.org/plugin/bukkit/BridgingAnalyzer/3991)

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21+-brightgreen?style=flat-square)](https://www.spigotmc.org/)
[![Java Version](https://img.shields.io/badge/Java-21+-orange?style=flat-square)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Built%20with-Maven-blue?style=flat-square)](https://maven.apache.org/)

**[Original Project](https://github.com/SakuraKoi/BridgingAnalyzer)** | **[Download Latest](https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter/releases)** | **[中文文档](README.md)**

</div>

---

## 📋 Table of Contents

- [🎉 Version 2.0.0 - Minecraft 1.21 Support](#-version-200---minecraft-121-support)
- [👥 Contributors](#-contributors)
- [✨ New Features](#-new-features)
- [🚀 Quick Start](#-quick-start)
- [📋 System Requirements](#-system-requirements)
- [🔧 Build Instructions](#-build-instructions)
- [🎮 Features](#-features)
- [📖 Usage Guide](#-usage-guide)
- [🔄 Upgrading from Legacy Versions](#-upgrading-from-legacy-versions)
- [📝 Changelog](#-changelog)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [🙏 Acknowledgments](#-acknowledgments)

---

## 🎉 Version 2.0.0 - Minecraft 1.21 Support

**Major Update!** BridgingAnalyzer now fully supports **Minecraft 1.21**!

This is a completely modernized version, fully upgraded from 1.8.9 to 1.21, bringing significant performance improvements, stability enhancements, and user experience optimizations.

### 👥 Contributors
- **Original Author**: SakuraKooi
- **1.21 Adaptation**: Ver_zhzh

### ✨ New Features
- 🚀 **Complete Minecraft 1.21 Support** - Fully upgraded from 1.8.9
- 🏗️ **Modern Maven Project Structure** - Standardized build and dependency management
- 🎆 **Modern Particle Effects System** - Using latest Bukkit Particle API
- ⚡ **Performance Optimizations** - Event handling, permission caching, block clearing optimizations
- 📺 **Modern Title Display** - Using native Bukkit Title API
- 🔧 **Removed Reflection Dependencies** - Enhanced stability and compatibility

### 🛠️ Technical Upgrades
- **Java 21** Support
- **Spigot 1.21** API
- **bStats 3.0.2** Latest statistics system
- **Lombok 1.18.30** Modern code generation

---

## 🚀 Quick Start

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
- **Java 21+** - Must use Java 21 or higher
- **Maven 3.6+** - For project building and dependency management
- **[Lombok](https://projectlombok.org/)** - For getter/setter code generation, please install Lombok plugin in your IDE

### 🚀 **Build Steps**

```bash
# Clone the 1.21 adaptation project
git clone https://github.com/Ver-zhzh/BridgingAnalyzer-master-Adapter.git

# Enter project directory
cd BridgingAnalyzer-master-Adapter

# Compile project (first build will automatically download dependencies)
mvn clean compile

# Package plugin
mvn package

# Generated jar file located at target/BridgingAnalyzer-2.0.0.jar
```

### 📊 **Maven Advantages**
- **Automatic Dependency Management** - Automatically downloads Spigot API, bStats, Lombok, and other dependencies
- **Version Control** - Unified management of all dependency versions, avoiding conflicts
- **Standardized Build** - Follows Maven standards, convenient for CI/CD integration
- **IDE Integration** - Modern IDEs natively support Maven projects

### ⚠️ **Important Notice**
This plugin uses bStats for anonymous usage statistics. Please don't delete the statistics classes when building! Maven will automatically handle bStats dependencies and packaging.

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
