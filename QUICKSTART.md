# SmartClicker 快速入门指南

## 🎯 5分钟上手

### 第一步: 打开项目

1. **安装Android Studio** (如未安装)
   - 访问: https://developer.android.com/studio
   - 下载并安装最新版本

2. **打开项目**
   ```
   File → Open → 选择 SmartClicker 文件夹
   ```

3. **等待Gradle同步**
   - Android Studio会自动下载依赖
   - 看到底部状态栏显示 "BUILD SUCCESSFUL" 表示完成

### 第二步: 准备测试设备

**选项1: 使用真机(推荐)**
1. 开启开发者模式(设置 → 关于手机 → 连续点击版本号7次)
2. 开启USB调试(设置 → 开发者选项 → USB调试)
3. 用USB连接电脑
4. 在Android Studio选择你的设备

**选项2: 使用模拟器**
1. Android Studio → Tools → Device Manager
2. Create Device → 选择设备(如 Pixel 5)
3. 下载系统镜像并启动
4. 选择Android 12(API 31)或更高版本

### 第三步: 运行应用

1. 点击Android Studio的绿色运行按钮 ▶️
2. 等待编译完成
3. 应用会自动安装并启动

### 第四步: 配置权限

1. **启用无障碍服务**
   - 应用会提示"需要启用无障碍服务"
   - 点击"去设置"
   - 找到"智能点击器"并启用
   - 返回应用

2. **授予悬浮窗权限**
   - 应用会提示"需要悬浮窗权限"
   - 点击"去设置"
   - 开启"智能点击器"的悬浮窗权限
   - 返回应用

### 第五步: 使用基础功能

#### 测试简单点击
1. 打开任意应用(如设置)
2. 在SmartClicker中点击"添加步骤"
3. 选择"简单点击"
4. 在屏幕上点击想要点击的位置
5. 点击"保存步骤"
6. 点击"开始点击"测试

#### 测试智能扫描
1. 在任意界面,点击"智能扫描"
2. 等待扫描完成
3. 查看发现的可点击元素数量

## 💡 使用技巧

### 技巧1: 快速添加点击位置
- 在屏幕上直接点击选择位置
- 坐标会自动记录

### 技巧2: 文字识别
- 输入界面上的文字
- 应用会自动识别并点击

### 技巧3: 保存方案
- 创建多步操作后,点击"方案管理"
- 保存为预设方案
- 下次可以直接加载

### 技巧4: 使用悬浮窗
- 悬浮窗可随时调出应用
- 拖动悬浮窗到方便操作的位置

## ⚠️ 常见问题

### Q: 应用闪退怎么办?
A: 检查是否已启用无障碍服务和悬浮窗权限

### Q: 点击没有反应?
A: 确保无障碍服务处于启用状态

### Q: 扫描不到元素?
A: 等待界面完全加载后再扫描

### Q: 模拟器运行慢?
A: 启用硬件加速,分配更多内存(2GB+)

### Q: 真机安装失败?
A: 
- 开启"未知来源应用安装"
- 在开发者选项中开启"USB调试(安全调试)"

## 📚 深入学习

### 推荐阅读
1. [README.md](README.md) - 完整项目说明
2. [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - 项目总结
3. Android开发者文档: https://developer.android.com

### 核心代码位置
- 无障碍服务: `service/SmartAccessibilityService.kt`
- 元素识别: `utils/ElementRecognizer.kt`
- 主界面: `ui/MainActivity.kt`
- 数据模型: `model/Models.kt`

## 🆘 获取帮助

### 查看日志
```bash
# 查看实时日志
adb logcat | grep SmartClicker

# 保存日志到文件
adb logcat -d > smartclicker_log.txt
```

### 调试技巧
1. 使用Logcat过滤器: `SmartClicker`
2. 在关键位置添加Log.d()调试信息
3. 使用Android Studio调试器设置断点

## 🎉 开始你的智能点击之旅!

现在你已经掌握了基础知识,尽情发挥创意吧!

---

**需要更多帮助?**
- 查看完整文档: README.md
- 了解项目结构: PROJECT_SUMMARY.md
- 报告问题或建议: 创建Issue
