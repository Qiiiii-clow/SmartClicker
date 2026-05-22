# SmartClicker - 智能屏幕点击器

## 📱 项目简介

SmartClicker 是一款基于 Android 无障碍服务的智能自动点击工具,支持屏幕元素识别、智能点击、步骤编辑等功能。

## ✨ 核心功能

### 基础功能
- ✅ **简单点击** - 点击屏幕指定位置
- ✅ **连续点击** - 重复点击同一位置
- ✅ **长按操作** - 模拟长按屏幕
- ✅ **步骤编辑** - 创建、保存、加载点击方案

### 智能功能
- 🤖 **智能扫描** - 自动识别屏幕可点击元素
- 🎯 **文字识别** - 识别屏幕文字并点击
- 🖼️ **识图点击** - 通过图片模板识别元素
- 🎨 **颜色识别** - 通过颜色定位目标
- 🧠 **语义分析** - 智能推荐点击目标
- 📊 **元素分类** - 自动分类按钮、输入框等

### 技术特性
- 免Root - 无需获取root权限
- 无障碍服务 - 通过 AccessibilityService 实现
- 悬浮窗 - 快捷操作入口
- 方案管理 - 保存和加载点击方案

## 🏗️ 项目结构

```
SmartClicker/
├── app/
│   ├── src/main/java/com/smartclicker/app/
│   │   ├── model/              # 数据模型
│   │   │   └── Models.kt
│   │   ├── service/            # 核心服务
│   │   │   ├── SmartAccessibilityService.kt  # 无障碍服务
│   │   │   ├── ClickExecutionService.kt      # 点击执行服务
│   │   │   └── FloatWindowService.kt         # 悬浮窗服务
│   │   ├── ui/                 # 界面
│   │   │   ├── MainActivity.kt
│   │   │   ├── SettingsActivity.kt
│   │   │   └── StepEditorActivity.kt
│   │   ├── viewmodel/          # ViewModel
│   │   │   ├── MainViewModel.kt
│   │   │   ├── SettingsViewModel.kt
│   │   │   └── StepEditorViewModel.kt
│   │   ├── adapter/            # 适配器
│   │   │   └── StepListAdapter.kt
│   │   ├── repository/         # 数据仓库
│   │   │   ├── SettingsRepository.kt
│   │   │   └── ClickSessionRepository.kt
│   │   ├── utils/              # 工具类
│   │   │   └── ElementRecognizer.kt
│   │   ├── di/                 # 依赖注入
│   │   │   └── DIContainer.kt
│   │   └── SmartClickerApp.kt  # 应用入口
│   ├── src/main/res/           # 资源文件
│   └── build.gradle.kts        # 构建配置
├── build.gradle.kts            # 项目构建配置
├── settings.gradle.kts         # 项目设置
└── README.md                   # 项目说明
```

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK 21+ (Android 5.0+)
- 目标 SDK 34 (Android 14)

### 构建步骤

1. **克隆或打开项目**
   ```bash
   cd SmartClicker
   ```

2. **在 Android Studio 中打开项目**
   - 打开 Android Studio
   - File → Open → 选择 SmartClicker 文件夹

3. **同步 Gradle**
   - Android Studio 会自动同步 Gradle
   - 如果未同步,点击 "Sync Now"

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 Run 按钮 (▶️)

5. **配置无障碍服务**
   - 安装并打开应用
   - 按照提示启用无障碍服务
   - 授予悬浮窗权限

## 📖 使用说明

### 1. 基础点击
1. 点击"添加步骤"
2. 选择"简单点击"
3. 在屏幕上选择点击位置
4. 保存步骤
5. 点击"开始点击"

### 2. 智能扫描
1. 点击"智能扫描"
2. 应用会扫描当前屏幕的可点击元素
3. 在结果中查看发现的元素

### 3. 文字识别点击
1. 点击"添加步骤"
2. 选择"文字识别"
3. 输入要识别的文字
4. 保存并执行

### 4. 方案管理
1. 点击"方案管理"
2. 创建新方案或加载已有方案
3. 方案会自动保存到本地

## 🔧 技术栈

- **语言**: Kotlin
- **UI框架**: Material Design 3
- **架构模式**: MVVM
- **异步处理**: Kotlin Coroutines
- **数据存储**: DataStore Preferences + JSON
- **依赖注入**: 手动DI容器
- **无障碍服务**: AccessibilityService
- **手势操作**: GestureDescription

## 📝 核心实现

### 无障碍服务
通过 `AccessibilityService` 实现屏幕元素读取和手势模拟:

```kotlin
// 扫描可点击元素
fun scanClickableElements(): List<ScreenElement>

// 执行点击
fun performClick(element: ScreenElement): Boolean
fun performClickAt(x: Int, y: Int): Boolean

// 长按
fun performLongPress(element: ScreenElement, duration: Long = 1000L): Boolean
```

### 元素识别
智能识别屏幕元素类型:

```kotlin
// 识别按钮、输入框、图片等
fun recognizeElementType(node: AccessibilityNodeInfo): ElementType

// 语义分析推荐点击目标
fun recommendClickTargets(elements: List<ScreenElement>, keyword: String): List<ScreenElement>
```

## ⚠️ 注意事项

1. **权限要求**: 需要无障碍服务和悬浮窗权限
2. **兼容性**: 支持 Android 5.0 (API 21) 及以上版本
3. **电池优化**: 建议在电池优化中排除本应用
4. **后台运行**: 可能需要禁止应用被清理

## 🚧 待完成功能

- [ ] 图像匹配算法实现
- [ ] 颜色识别与匹配
- [ ] 滑动操作参数配置
- [ ] 更多UI优化
- [ ] 单元测试
- [ ] 国际化支持

## 📄 许可证

MIT License

## 👨‍💻 开发

欢迎提交 Issue 和 Pull Request!

---

**注意**: 本应用仅用于学习和合法用途,请勿用于非法目的。
