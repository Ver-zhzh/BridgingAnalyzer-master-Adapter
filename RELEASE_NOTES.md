# BridgingAnalyzer v2.1.0 发布说明

## 🎉 重大更新：多版本适配系统

我们很高兴地宣布 BridgingAnalyzer v2.1.0 的发布！这是一个革命性的更新，引入了完整的多版本适配系统。

### 🚀 主要特性

#### 📦 8版本全覆盖
现在支持从 Minecraft 1.13.2 到 1.21+ 的所有现代版本：
- ✅ 1.21+ (最新版本)
- ✅ 1.20.4 (足迹与故事)
- ✅ 1.19.2 (荒野更新)
- ✅ 1.18.1 (洞穴与山崖 Part II)
- ✅ 1.16.5 (下界更新)
- ✅ 1.15.2 (蜜蜂更新)
- ✅ 1.14.4 (村庄与掠夺)
- ✅ 1.13.2 (水域更新)

#### 🏗️ 智能适配器系统
- **自动版本检测**: 插件会自动检测服务器版本
- **智能适配器加载**: 使用对应版本的适配器
- **Fallback机制**: 未支持版本自动使用兼容适配器
- **统一API接口**: 确保所有版本功能一致

#### 🔧 技术架构
- **12个版本适配器**: 覆盖从1.8.8到1.21+的所有版本
- **跨版本兼容性**: 字符串枚举查找 + Try-catch降级
- **Java版本适配**: legacy版本使用Java 8，1.21使用Java 21
- **零配置使用**: 下载对应版本即可直接使用

### 📥 下载指南

**重要**: 请根据您的服务器版本选择对应的JAR文件！

| 服务器版本 | 下载文件 | 文件大小 | Java版本 |
|------------|----------|----------|----------|
| **1.21+** | [BridgingAnalyzer-2.1.0.jar](releases/BridgingAnalyzer-2.1.0.jar) | ~119 KB | Java 21 |
| **1.20.4** | [BridgingAnalyzer-2.1.0-1.20.jar](releases/BridgingAnalyzer-2.1.0-1.20.jar) | ~132 KB | Java 8 |
| **1.19.2** | [BridgingAnalyzer-2.1.0-1.19.jar](releases/BridgingAnalyzer-2.1.0-1.19.jar) | ~132 KB | Java 8 |
| **1.18.1** | [BridgingAnalyzer-2.1.0-1.18.jar](releases/BridgingAnalyzer-2.1.0-1.18.jar) | ~132 KB | Java 8 |
| **1.16.5** | [BridgingAnalyzer-2.1.0-1.16.jar](releases/BridgingAnalyzer-2.1.0-1.16.jar) | ~123 KB | Java 8 |
| **1.15.2** | [BridgingAnalyzer-2.1.0-1.15.jar](releases/BridgingAnalyzer-2.1.0-1.15.jar) | ~123 KB | Java 8 |
| **1.14.4** | [BridgingAnalyzer-2.1.0-1.14.jar](releases/BridgingAnalyzer-2.1.0-1.14.jar) | ~117 KB | Java 8 |
| **1.13.2** | [BridgingAnalyzer-2.1.0-1.13.jar](releases/BridgingAnalyzer-2.1.0-1.13.jar) | ~117 KB | Java 8 |

### 🔄 升级指南

1. **备份配置**: 备份现有的插件配置文件
2. **选择版本**: 根据服务器版本选择对应的JAR文件
3. **替换文件**: 删除旧版本插件，放入新版本
4. **重启服务器**: 重启服务器完成升级

### ⚠️ 重要注意事项

- **版本匹配**: 必须使用与服务器版本对应的JAR文件
- **单一版本**: 一个服务器只能使用一个版本的JAR文件
- **Java环境**: 确保服务器Java版本符合要求
- **配置兼容**: 配置文件与旧版本完全兼容

### 🐛 已修复问题

- ✅ 修复了跨版本API兼容性问题
- ✅ 修复了粒子名称在不同版本间的差异
- ✅ 修复了材质名称的版本兼容性
- ✅ 修复了Java版本兼容性问题
- ✅ 修复了ActionBar在某些版本上的显示问题

### 🔮 未来计划

- 持续优化跨版本兼容性
- 添加更多自定义功能
- 改进用户界面和体验
- 支持更多服务器类型

### 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户：
- **SakuraKooi** - 原插件作者
- **Ver_zhzh** - 多版本适配系统开发
- **Minecraft社区** - 持续的支持和反馈

### 📞 支持

如果您遇到任何问题或有建议，请：
- 提交 [GitHub Issues](https://github.com/SakuraKoi/BridgingAnalyzer/issues)
- 参与 [GitHub Discussions](https://github.com/SakuraKoi/BridgingAnalyzer/discussions)
- 查看 [项目Wiki](https://github.com/SakuraKoi/BridgingAnalyzer/wiki)

---

**享受您的搭路练习！** 🌉
