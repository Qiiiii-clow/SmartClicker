# SmartClicker 测试指南

## 📋 测试前准备

### 1. 环境要求

#### 必需软件
- ✅ **Android Studio** (Hedgehog 2023.1.1 或更高版本)
  - 下载地址: https://developer.android.com/studio
  - 支持 macOS/Windows/Linux
  - 推荐配置: 8GB+ RAM, 10GB+ 磁盘空间

- ✅ **JDK 17** (Android Studio自带)
- ✅ **Android SDK 34** (Android 14)
- ✅ **Android SDK 21** (Android 5.0, 最低兼容版本)

### 2. 测试设备选择

#### 方案A: 真机测试(推荐,功能最完整)

**优点**: 
- 无障碍服务功能完整
- 手势操作真实有效
- 性能表现准确

**准备步骤**:

**Android 真机设置**:
1. **开启开发者模式**
   - 设置 → 关于手机 → 连续点击"版本号"7次
   - 输入锁屏密码确认

2. **开启USB调试**
   - 设置 → 开发者选项 → USB调试 → 开启
   - 可选: USB调试(安全设置) → 开启

3. **连接电脑**
   - 用USB线连接手机和电脑
   - 手机上弹出"允许USB调试?" → 点击"允许"
   - 勾选"始终允许"

4. **验证连接**
   ```bash
   adb devices
   ```
   应该看到设备列表

**macOS特殊设置**:
- 系统设置 → 隐私与安全性 → 允许来自以下位置的应用 → 任何来源

#### 方案B: 安卓模拟器

**优点**: 
- 无需额外设备
- 可快速切换不同版本
- 方便截图和录屏

**准备步骤**:
1. 打开Android Studio
2. Tools → Device Manager
3. Create Device
4. 选择设备(推荐: Pixel 5, Pixel 6)
5. 选择系统镜像(推荐: Android 14 API 34)
6. 下载并完成配置
7. 点击 ▶️ 启动模拟器

**模拟器优化**:
- 分配至少 2GB RAM
- 启用硬件加速(HAXM/VirtIO)
- 设置高分辨率(1080x2340)

---

## 🔧 测试步骤

### 第一步: 打开项目

```bash
# 方法1: Android Studio
File → Open → 选择 SmartClicker 文件夹 → OK

# 方法2: 命令行
cd /Users/qi/CodeBuddy/20260522084010/SmartClicker
open -a "Android Studio" .
```

### 第二步: 同步Gradle

1. 首次打开会自动同步Gradle
2. 如果未自动同步,点击底部 "Sync Now"
3. 等待下载完成(首次需要下载依赖,约2-5分钟)
4. 看到 "BUILD SUCCESSFUL" 表示完成

**同步失败处理**:
- 检查网络连接
- 使用镜像源(国内推荐阿里云)
- File → Invalidate Caches → Invalidate and Restart

### 第三步: 编译运行

#### 使用真机
1. 连接真机并确认 `adb devices` 能看到
2. 在Android Studio顶部选择你的设备
3. 点击绿色运行按钮 ▶️ 或 Shift+F10
4. 等待编译和安装
5. 应用自动启动

#### 使用模拟器
1. 启动模拟器
2. 在Android Studio顶部选择模拟器
3. 点击 ▶️ 运行
4. 等待编译和安装
5. 应用自动启动

**编译输出位置**:
```
SmartClicker/app/build/outputs/apk/debug/app-debug.apk
```

---

## 🧪 功能测试清单

### 测试1: 基础安装和启动

**步骤**:
1. 安装应用后首次打开
2. 检查界面是否正常显示
3. 检查是否有闪退或卡顿

**预期结果**:
- ✅ 应用正常启动
- ✅ 显示主界面
- ✅ 标题显示"智能点击器"
- ✅ 无闪退或崩溃

**命令行测试**:
```bash
# 安装APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 查看启动日志
adb logcat | grep SmartClicker
```

---

### 测试2: 无障碍服务配置

**步骤**:
1. 打开应用
2. 点击"设置"按钮
3. 点击"启用无障碍服务"
4. 在系统设置中找到"智能点击器"
5. 开启开关

**预期结果**:
- ✅ 能跳转到无障碍设置页面
- ✅ 能找到"智能点击器"服务
- ✅ 开启后状态显示"已启用"

**验证代码**:
```bash
# 检查无障碍服务是否启用
adb shell settings get secure enabled_accessibility_services | grep SmartAccessibilityService
```

---

### 测试3: 悬浮窗权限配置

**步骤**:
1. 在主界面查看提示
2. 点击"去设置"
3. 开启"智能点击器"的悬浮窗权限

**预期结果**:
- ✅ 能跳转到悬浮窗权限设置
- ✅ 开启后悬浮窗正常显示

**验证**:
- 返回主界面,应该能看到圆形悬浮按钮
- 拖动悬浮按钮能跟随手指移动

---

### 测试4: 简单点击功能

**步骤**:
1. 打开任意应用(如"设置")
2. 回到SmartClicker
3. 点击"添加步骤"按钮
4. 选择"简单点击"
5. 在弹出界面点击屏幕任意位置
6. 点击"保存步骤"
7. 返回主界面
8. 点击"开始点击"

**预期结果**:
- ✅ 能成功添加点击步骤
- ✅ 步骤列表显示新添加的步骤
- ✅ 点击"开始点击"后,屏幕会执行点击操作

**验证点击**:
```bash
# 查看点击日志
adb logcat -s SmartAccessibilityService:D
```

---

### 测试5: 智能扫描功能

**步骤**:
1. 打开任意应用(如"设置")
2. 等待界面完全加载
3. 回到SmartClicker
4. 点击"智能扫描"按钮
5. 等待3秒

**预期结果**:
- ✅ 按钮显示"正在扫描..."
- ✅ 扫描完成后显示发现的元素数量
- ✅ 日志显示扫描详情

**查看日志**:
```bash
adb logcat | grep "扫描到"
```

**预期日志输出**:
```
扫描到 XX 个元素, 可点击: XX
```

---

### 测试6: 文字识别点击

**步骤**:
1. 打开设置应用
2. 找到任意文字(如"关于手机")
3. 在SmartClicker点击"添加步骤"
4. 选择"文字识别"
5. 输入要识别的文字(如"关于")
6. 保存步骤
7. 切换到设置应用
8. 点击"开始点击"

**预期结果**:
- ✅ 应用能识别包含该文字的界面元素
- ✅ 自动点击到目标位置

---

### 测试7: 连续点击功能

**步骤**:
1. 添加连续点击步骤
2. 设置重复次数(如5次)
3. 设置延迟时间(如200ms)
4. 执行点击

**预期结果**:
- ✅ 按设定次数执行点击
- ✅ 每次点击之间有间隔

---

### 测试8: 方案保存和加载

**步骤**:
1. 添加3-5个点击步骤
2. 点击"方案管理"
3. 点击"保存方案"
4. 输入方案名称(如"测试方案")
5. 关闭应用
6. 重新打开应用
7. 点击"方案管理"
8. 查看是否有之前保存的方案
9. 点击加载方案

**预期结果**:
- ✅ 方案能成功保存
- ✅ 重新打开应用能加载方案
- ✅ 步骤信息完整

**验证文件**:
```bash
# 查看保存的方案文件
adb shell ls /data/data/com.smartclicker.app/files/click_sessions/
```

---

### 测试9: 长按功能

**步骤**:
1. 添加长按步骤
2. 选择长按位置
3. 设置长按时间(如1000ms)
4. 执行

**预期结果**:
- ✅ 在指定位置执行长按操作

---

### 测试10: 停止点击功能

**步骤**:
1. 添加多个步骤
2. 设置较长延迟
3. 点击"开始点击"
4. 立即点击"停止"

**预期结果**:
- ✅ 点击操作立即停止
- ✅ 按钮恢复为"开始点击"
- ✅ 日志显示"停止所有操作"

---

## 🐛 调试方法

### 查看实时日志

**方法1: Android Studio Logcat**
1. View → Tool Windows → Logcat
2. 选择设备
3. 过滤: `SmartClicker`
4. 级别: Debug

**方法2: 命令行**
```bash
# 实时查看
adb logcat | grep SmartClicker

# 保存日志到文件
adb logcat -d > test_log.txt

# 查看特定标签
adb logcat -s SmartAccessibilityService
adb logcat -s ClickExecutionService
adb logcat -s SmartClickerRepo
```

### 常见调试场景

**场景1: 应用闪退**
```bash
# 查看崩溃日志
adb logcat | grep FATAL

# 查看最近崩溃
adb bugreport > crash_report.txt
```

**场景2: 点击无响应**
```bash
# 检查无障碍服务
adb shell settings get secure enabled_accessibility_services

# 检查服务状态
adb shell service list | grep accessibility
```

**场景3: 权限问题**
```bash
# 检查悬浮窗权限
adb shell dumpsys window | grep "draw-over-ops"

# 手动授予权限
adb shell pm grant com.smartclicker.app android.permission.SYSTEM_ALERT_WINDOW
```

---

## 📊 性能测试

### 内存使用
```bash
# 查看应用内存
adb shell dumpsys meminfo com.smartclicker.app

# 监控内存变化
adb shell dumpsys meminfo com.smartclicker.app > memory.txt
```

### CPU使用
```bash
# 查看进程CPU
adb shell top -m 10

# 查看特定进程
adb shell top -m 10 | grep com.smartclicker.app
```

### 电池消耗
```bash
# 查看电池统计
adb shell dumpsys batterystats
```

---

## ✅ 测试检查清单

### 安装测试
- [ ] APK安装成功
- [ ] 首次启动无闪退
- [ ] 图标正确显示

### 权限测试
- [ ] 无障碍服务能正常启用
- [ ] 悬浮窗权限能正常授予
- [ ] 权限拒绝时有友好提示

### 功能测试
- [ ] 简单点击正常
- [ ] 连续点击正常
- [ ] 长按操作正常
- [ ] 智能扫描正常
- [ ] 文字识别正常
- [ ] 步骤编辑正常
- [ ] 方案保存/加载正常
- [ ] 停止点击正常
- [ ] 悬浮窗正常显示

### UI测试
- [ ] 所有界面正常显示
- [ ] 按钮点击有反馈
- [ ] 文字显示正确
- [ ] 布局在不同屏幕正常

### 兼容性测试
- [ ] Android 5.0 正常运行
- [ ] Android 7.0 正常运行
- [ ] Android 10 正常运行
- [ ] Android 14 正常运行

### 稳定性测试
- [ ] 连续运行1小时无崩溃
- [ ] 切换应用无异常
- [ ] 后台运行无异常

---

## 🚀 快速测试脚本

创建 `test.sh` 脚本快速测试:

```bash
#!/bin/bash

echo "=== SmartClicker 快速测试 ==="

# 1. 检查设备连接
echo "1. 检查设备连接..."
adb devices
if [ $? -ne 0 ]; then
    echo "错误: ADB未连接,请检查设备"
    exit 1
fi

# 2. 编译APK
echo "2. 编译APK..."
./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "错误: 编译失败"
    exit 1
fi

# 3. 卸载旧版本
echo "3. 卸载旧版本..."
adb uninstall com.smartclicker.app

# 4. 安装APK
echo "4. 安装APK..."
adb install app/build/outputs/apk/debug/app-debug.apk
if [ $? -ne 0 ]; then
    echo "错误: 安装失败"
    exit 1
fi

# 5. 启动应用
echo "5. 启动应用..."
adb shell am start -n com.smartclicker.app/.ui.MainActivity

# 6. 查看日志
echo "6. 查看日志(按Ctrl+C结束)..."
adb logcat | grep SmartClicker

echo "=== 测试完成 ==="
```

使用方法:
```bash
chmod +x test.sh
./test.sh
```

---

## 📝 测试报告模板

```
测试日期: ___________
测试人员: ___________
设备信息: ___________
Android版本: ___________
应用版本: 1.0.0

功能测试:
[ ] 安装测试 - 通过/失败
[ ] 权限测试 - 通过/失败
[ ] 简单点击 - 通过/失败
[ ] 智能扫描 - 通过/失败
[ ] 文字识别 - 通过/失败
[ ] 方案管理 - 通过/失败

发现的问题:
1. _______________
2. _______________
3. _______________

备注:
_____________________
```

---

## 🆘 常见问题

### Q1: Gradle同步失败
**A**: 
```bash
# 使用国内镜像
在 build.gradle.kts 中添加:
repositories {
    maven { url 'https://maven.aliyun.com/repository/google' }
    maven { url 'https://maven.aliyun.com/repository/public' }
    google()
    mavenCentral()
}
```

### Q2: 真机无法识别
**A**: 
- 更换USB线(使用数据线而非充电线)
- 更换USB端口
- 检查是否开启了USB调试
- macOS需要信任电脑

### Q3: 点击不生效
**A**: 
- 确认无障碍服务已启用
- 确认应用在前台运行
- 检查是否选择了正确的点击位置

### Q4: 模拟器运行慢
**A**: 
- 启用硬件加速
- 分配更多内存(2GB+)
- 使用x86_64架构的镜像
- 关闭其他占用资源的应用

### Q5: 编译报错
**A**:
```bash
# 清理项目
./gradlew clean

# 重建项目
./gradlew build --refresh-dependencies

# 清除缓存
rm -rf ~/.gradle/caches
```

---

## 📞 获取帮助

- 查看完整文档: README.md
- 查看项目结构: PROJECT_SUMMARY.md
- 快速入门: QUICKSTART.md
- Android官方文档: https://developer.android.com

---

**祝测试顺利! 🎉**
