# SmartClicker 测试 - 快速开始

## 🚀 5分钟快速测试

### 前提条件

**必须安装**:
- ✅ Android Studio (https://developer.android.com/studio)

### 步骤1: 打开项目

```bash
# 方法1: 命令行打开
cd /Users/qi/CodeBuddy/20260522084010/SmartClicker
open -a "Android Studio" .

# 方法2: Android Studio手动打开
# File → Open → 选择 SmartClicker 文件夹
```

### 步骤2: 同步Gradle

- Android Studio会自动同步
- 如果未自动同步,点击底部 **"Sync Now"**
- 等待显示 **"BUILD SUCCESSFUL"**

### 步骤3: 运行应用

#### 选项A: 使用模拟器(最简单)

1. **创建模拟器** (首次使用)
   - Tools → Device Manager → Create Device
   - 选择 "Pixel 5"
   - 选择 "Android 14 (API 34)"
   - 下载并完成
   - 点击 ▶️ 启动

2. **运行应用**
   - 顶部选择你的模拟器
   - 点击绿色 ▶️ 运行按钮
   - 等待编译和安装
   - 应用自动启动! 🎉

#### 选项B: 使用真机(推荐)

1. **开启开发者模式**
   - 设置 → 关于手机 → 连续点击"版本号"7次
   - 设置 → 开发者选项 → USB调试 → 开启

2. **连接电脑**
   - USB连接手机和电脑
   - 手机上点击"允许"

3. **运行应用**
   - 顶部选择你的设备
   - 点击 ▶️ 运行

### 步骤4: 配置权限

1. **启用无障碍服务**
   - 点击"设置"
   - 点击"启用无障碍服务"
   - 找到"智能点击器"并开启

2. **授予悬浮窗权限**
   - 点击"启用悬浮窗权限"
   - 开启"智能点击器"

### 步骤5: 测试功能

**快速测试清单**:
1. ✅ 打开应用,界面正常显示
2. ✅ 点击"智能扫描",显示元素数量
3. ✅ 点击"添加步骤",选择"简单点击"
4. ✅ 在屏幕上点击选择位置
5. ✅ 保存步骤,返回列表看到新步骤
6. ✅ 点击"开始点击",验证点击生效

---

## 📜 使用测试脚本(推荐)

```bash
# 进入项目目录
cd /Users/qi/CodeBuddy/20260522084010/SmartClicker

# 查看所有命令
./test.sh help

# 完整测试流程(编译+安装+启动)
./test.sh all

# 只编译和安装
./test.sh install

# 查看实时日志
./test.sh log
```

---

## 🐛 常见问题

### Q: Gradle同步失败?
```bash
# 使用国内镜像
在 app/build.gradle.kts 的 repositories 中添加:
maven { url 'https://maven.aliyun.com/repository/google' }
maven { url 'https://maven.aliyun.com/repository/central' }
```

### Q: 设备未被识别?
- 检查USB线(使用数据线)
- 检查USB调试是否开启
- macOS: 系统设置 → 隐私 → 允许

### Q: 点击不生效?
- 确认无障碍服务已启用
- 确认已选择正确的点击位置
- 查看日志: `adb logcat | grep SmartClicker`

### Q: 编译报错?
```bash
# 清理并重新构建
./gradlew clean
./gradlew build --refresh-dependencies
```

---

## 📊 测试检查清单

安装后请按顺序测试:

- [ ] 应用正常启动
- [ ] 界面显示正常
- [ ] 无障碍服务能启用
- [ ] 悬浮窗能显示
- [ ] 智能扫描能工作
- [ ] 能添加点击步骤
- [ ] 能保存方案
- [ ] 点击操作能执行

---

## 📚 更多文档

- **完整测试指南**: [TESTING.md](TESTING.md)
- **项目说明**: [README.md](README.md)
- **快速入门**: [QUICKSTART.md](QUICKSTART.md)
- **项目总结**: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)

---

**祝测试顺利! 🎉**

有问题?查看完整文档或运行 `./test.sh help`
