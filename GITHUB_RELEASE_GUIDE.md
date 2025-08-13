# GitHub 发布指导

## 📋 发布前检查清单

### ✅ 文件完整性检查
- [x] **README.md** - 已更新为多版本适配系统
- [x] **CHANGELOG.md** - 包含v2.1.0的详细更新日志
- [x] **RELEASE_NOTES.md** - 发布说明文档
- [x] **LICENSE** - MIT许可证文件
- [x] **.gitignore** - 已配置，保留releases目录中的JAR文件
- [x] **pom.xml** - 包含8个Maven profiles，版本号2.1.0
- [x] **build-all-versions.bat** - 一键构建脚本

### ✅ 发布文件检查
releases目录中应包含以下8个JAR文件：

| 文件名 | 大小 | 目标版本 |
|--------|------|----------|
| `BridgingAnalyzer-2.1.0.jar` | ~119 KB | 1.21+ |
| `BridgingAnalyzer-2.1.0-1.20.jar` | ~132 KB | 1.20.4 |
| `BridgingAnalyzer-2.1.0-1.19.jar` | ~132 KB | 1.19.2 |
| `BridgingAnalyzer-2.1.0-1.18.jar` | ~132 KB | 1.18.1 |
| `BridgingAnalyzer-2.1.0-1.16.jar` | ~123 KB | 1.16.5 |
| `BridgingAnalyzer-2.1.0-1.15.jar` | ~123 KB | 1.15.2 |
| `BridgingAnalyzer-2.1.0-1.14.jar` | ~117 KB | 1.14.4 |
| `BridgingAnalyzer-2.1.0-1.13.jar` | ~117 KB | 1.13.2 |

## 🚀 GitHub发布步骤

### 1. 推送代码到GitHub
```bash
# 初始化Git仓库（如果还没有）
git init

# 添加所有文件
git add .

# 提交更改
git commit -m "feat: 多版本适配系统 v2.1.0

- 支持Minecraft 1.13.2-1.21+共8个版本
- 智能适配器系统自动检测版本
- Maven多版本构建支持
- 跨版本API统一接口
- 完整的Fallback机制"

# 添加远程仓库（替换为您的仓库URL）
git remote add origin https://github.com/SakuraKoi/BridgingAnalyzer.git

# 推送到主分支
git push -u origin main
```

### 2. 创建GitHub Release

1. **访问GitHub仓库页面**
2. **点击 "Releases"**
3. **点击 "Create a new release"**
4. **填写发布信息**：

#### 标签版本 (Tag version)
```
v2.1.0
```

#### 发布标题 (Release title)
```
BridgingAnalyzer v2.1.0 - 多版本适配系统
```

#### 发布描述 (Release description)
```markdown
# 🎉 重大更新：多版本适配系统

这是一个革命性的更新，引入了完整的多版本适配系统，支持从 Minecraft 1.13.2 到 1.21+ 的所有现代版本。

## 🚀 主要特性

### 📦 8版本全覆盖
- ✅ 1.21+ (最新版本)
- ✅ 1.20.4 (足迹与故事)
- ✅ 1.19.2 (荒野更新)
- ✅ 1.18.1 (洞穴与山崖 Part II)
- ✅ 1.16.5 (下界更新)
- ✅ 1.15.2 (蜜蜂更新)
- ✅ 1.14.4 (村庄与掠夺)
- ✅ 1.13.2 (水域更新)

### 🏗️ 智能适配器系统
- **自动版本检测**: 插件会自动检测服务器版本
- **智能适配器加载**: 使用对应版本的适配器
- **Fallback机制**: 未支持版本自动使用兼容适配器
- **统一API接口**: 确保所有版本功能一致

## 📥 下载指南

**重要**: 请根据您的服务器版本选择对应的JAR文件！

| 服务器版本 | 下载文件 | Java版本 |
|------------|----------|----------|
| **1.21+** | BridgingAnalyzer-2.1.0.jar | Java 21 |
| **1.20.4** | BridgingAnalyzer-2.1.0-1.20.jar | Java 8 |
| **1.19.2** | BridgingAnalyzer-2.1.0-1.19.jar | Java 8 |
| **1.18.1** | BridgingAnalyzer-2.1.0-1.18.jar | Java 8 |
| **1.16.5** | BridgingAnalyzer-2.1.0-1.16.jar | Java 8 |
| **1.15.2** | BridgingAnalyzer-2.1.0-1.15.jar | Java 8 |
| **1.14.4** | BridgingAnalyzer-2.1.0-1.14.jar | Java 8 |
| **1.13.2** | BridgingAnalyzer-2.1.0-1.13.jar | Java 8 |

## ⚠️ 重要注意事项

- **版本匹配**: 必须使用与服务器版本对应的JAR文件
- **单一版本**: 一个服务器只能使用一个版本的JAR文件
- **Java环境**: 确保服务器Java版本符合要求

完整的更新日志请查看 [CHANGELOG.md](CHANGELOG.md)
```

### 3. 上传发布文件

在 "Attach binaries" 部分，上传以下文件：
- `BridgingAnalyzer-2.1.0.jar`
- `BridgingAnalyzer-2.1.0-1.20.jar`
- `BridgingAnalyzer-2.1.0-1.19.jar`
- `BridgingAnalyzer-2.1.0-1.18.jar`
- `BridgingAnalyzer-2.1.0-1.16.jar`
- `BridgingAnalyzer-2.1.0-1.15.jar`
- `BridgingAnalyzer-2.1.0-1.14.jar`
- `BridgingAnalyzer-2.1.0-1.13.jar`

### 4. 发布设置

- [x] **Set as the latest release** - 设为最新版本
- [x] **Create a discussion for this release** - 为此版本创建讨论（可选）

### 5. 点击 "Publish release"

## 📊 发布后任务

### 1. 更新项目描述
在GitHub仓库主页更新项目描述：
```
Minecraft Bridging Practice Plugin - 支持1.13.2-1.21+的多版本适配系统
```

### 2. 添加标签 (Topics)
```
minecraft, bukkit, spigot, plugin, bridging, bedwars, multi-version, java
```

### 3. 更新README徽章
确保README.md中的徽章链接正确指向新的仓库。

### 4. 社区推广
- 在相关Minecraft社区分享
- 更新插件发布平台（如SpigotMC）
- 通知现有用户升级

## 🎯 成功指标

发布成功后，您应该看到：
- [x] GitHub Release页面显示v2.1.0
- [x] 8个JAR文件可供下载
- [x] README.md正确显示多版本信息
- [x] 项目标签和描述已更新

---

**恭喜！您的多版本适配系统已成功发布到GitHub！** 🎉
