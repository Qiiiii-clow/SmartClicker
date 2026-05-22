# 🚀 GitHub Actions 获取APK - 详细教程

## 📋 前置准备

**你需要一个GitHub账号**:
- 访问 https://github.com/signup
- 注册免费账号
- 验证邮箱

---

## 🎯 完整操作步骤 (约15分钟)

### 第1步: 安装Git (如果未安装)

**macOS**:
```bash
# 检查是否已安装
git --version

# 如果未安装,安装Xcode命令行工具
xcode-select --install
```

**Windows**:
- 下载 https://git-scm.com/download/win
- 双击安装,保持默认设置

---

### 第2步: 配置Git

打开终端(macOS)或命令行(Windows),执行:

```bash
# 设置你的GitHub用户名和邮箱
git config --global user.name "你的GitHub用户名"
git config --global user.email "你的邮箱@example.com"

# 验证配置
git config --global --list
```

---

### 第3步: 运行自动设置脚本

```bash
# 进入项目目录
cd /Users/qi/CodeBuddy/20260522084010/SmartClicker

# 运行设置脚本
./setup-github-actions.sh
```

**脚本会自动完成**:
1. 初始化Git仓库
2. 添加所有文件
3. 提交代码
4. 提示你创建GitHub仓库

---

### 第4步: 在GitHub创建仓库

1. **打开浏览器,访问**: https://github.com/new

2. **填写仓库信息**:
   - **Repository name**: `SmartClicker` (或任意名称)
   - **Description**: `智能屏幕点击器 - 免Root自动点击工具`
   - **选择Public或Private**(都行,Public更公开)
   - **⚠️ 不要勾选** "Initialize this repository with a README"
   - **⚠️ 不要勾选** "Add .gitignore"
   - **⚠️ 不要勾选** "Choose a license"

3. **点击 "Create repository"**

4. **复制仓库URL**,格式类似:
   ```
   https://github.com/你的用户名/SmartClicker.git
   ```

---

### 第5步: 推送代码到GitHub

回到终端,执行:

```bash
# 设置远程仓库(替换为你的仓库URL)
git remote add origin https://github.com/你的用户名/SmartClicker.git

# 创建主分支
git branch -M main

# 推送代码
git push -u origin main
```

**如果提示输入用户名和密码**:
- Username: 你的GitHub用户名
- Password: 点击"Use a personal access token instead"
- 生成token: https://github.com/settings/tokens
  - 勾选 `repo` 权限
  - 生成后复制粘贴

**推送成功后,你会看到类似输出**:
```
Enumerating objects: 100, done.
Counting objects: 100% ...
Compressing objects: 100% ...
Writing objects: 100% ...
To https://github.com/用户名/SmartClicker.git
 * [new branch]      main -> main
```

---

### 第6步: 触发APK构建

**方式A: 手动触发(推荐)**

1. **访问仓库页面**:
   ```
   https://github.com/你的用户名/SmartClicker
   ```

2. **点击 "Actions" 标签**

3. **找到 "Build SmartClicker APK" 工作流**

4. **点击 "Run workflow" 按钮**(蓝色按钮)

5. **选择分支**: `main`

6. **点击绿色 "Run workflow" 按钮**

**方式B: 自动触发**

推送代码后会自动触发,无需手动操作。

---

### 第7步: 等待构建完成

1. **在Actions页面查看进度**
   - 访问: https://github.com/你的用户名/SmartClicker/actions

2. **点击正在运行的工作流**

3. **查看构建日志**
   - 左侧点击 `build` → `Build Debug APK`
   - 右侧查看实时日志

4. **等待约10-20分钟**
   - 首次构建需要下载依赖,稍慢
   - 后续构建会快很多

5. **构建成功后**,你会看到:
   - ✅ 绿色对勾
   - 左侧显示 "Upload APK artifact"

---

### 第8步: 下载APK

1. **在Actions页面,点击工作流运行**

2. **找到 "Artifacts" 区域**(在页面底部)

3. **点击 "SmartClicker-debug-apk"**

4. **下载ZIP文件**

5. **解压ZIP文件**

6. **找到 `app-debug.apk` 文件**

---

### 第9步: 安装到手机

**方式A: USB传输**
```bash
# 用USB线连接手机
# 复制APK到手机
cp app-debug.apk /path/to/phone/
```

**方式B: 微信/QQ传输**
1. 通过微信"文件传输助手"发送APK
2. 在手机上下载安装

**方式C: GitHub下载后传输**
1. 在电脑浏览器下载APK
2. 传输到手机

**方式D: ADB直接安装**
```bash
# 如果已连接设备
adb install app-debug.apk
```

---

### 第10步: 配置权限并测试

1. **安装APK后打开应用**

2. **启用无障碍服务**:
   - 设置 → 辅助功能 → 无障碍
   - 找到"智能点击器"
   - 开启开关

3. **授予悬浮窗权限**:
   - 设置 → 应用 → 特殊权限 → 悬浮窗
   - 允许"智能点击器"

4. **开始测试功能!**
   - 点击"智能扫描"测试识别
   - 点击"添加步骤"测试点击

---

## 📸 图文步骤说明

### 步骤1: 创建仓库
```
访问: https://github.com/new
填写:
  Repository name: SmartClicker
  ☐ Public/Private (任选)
  ☒ 不要勾选 README
  ☒ 不要勾选 .gitignore
点击: Create repository
```

### 步骤2: 推送代码
```bash
git remote add origin https://github.com/用户名/SmartClicker.git
git branch -M main
git push -u origin main
```

### 步骤3: 触发构建
```
访问: https://github.com/用户名/SmartClicker/actions
点击: Run workflow → Run workflow
```

### 步骤4: 下载APK
```
在Actions页面底部
找到: Artifacts
点击: SmartClicker-debug-apk
下载ZIP并解压
```

---

## ⚡ 快速命令(复制粘贴版)

```bash
# 1. 进入项目
cd /Users/qi/CodeBuddy/20260522084010/SmartClicker

# 2. 初始化Git
git init

# 3. 添加文件
git add .

# 4. 提交
git commit -m "SmartClicker v1.0.0"

# 5. 创建主分支
git branch -M main

# 6. 添加远程仓库(替换为你的URL)
git remote add origin https://github.com/你的用户名/SmartClicker.git

# 7. 推送
git push -u origin main

# 8. 完成!去GitHub Actions页面下载APK
```

---

## 🆘 常见问题

### Q1: Git未安装怎么办?
**A**: 
```bash
# macOS
xcode-select --install

# Windows
下载 https://git-scm.com/download/win
```

### Q2: GitHub推送失败?
**A**: 
- 检查用户名和仓库名是否正确
- 检查网络连接
- 使用Personal Access Token代替密码

### Q3: 如何生成Personal Access Token?
**A**:
1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token (classic)"
3. 勾选 `repo` 权限
4. 点击 "Generate token"
5. 复制token,粘贴为密码

### Q4: Actions构建失败?
**A**:
- 检查代码是否推送成功
- 查看Actions页面的错误日志
- 确保 `.github/workflows/build.yml` 文件存在

### Q5: 找不到APK?
**A**:
- 在Actions页面找到成功运行的工作流
- 滚动到页面底部
- 点击 "Artifacts" 下的ZIP文件
- 解压后找到 `app-debug.apk`

### Q6: 构建太慢?
**A**:
- 首次构建约15-20分钟(需下载依赖)
- 后续构建约5-10分钟
- 这是正常的,需要耐心等待

### Q7: 可以自定义APK名称吗?
**A**:
可以,修改 `app/build.gradle.kts` 中的:
```kotlin
android {
    defaultConfig {
        applicationId "com.your.package"
        versionCode 1
        versionName "1.0.0"
    }
}
```

### Q8: 可以生成Release APK吗?
**A**:
Release APK需要签名配置,Debug APK即可测试使用。

---

## 🎯 替代方案

如果GitHub Actions不方便,还有这些方法:

### 方案B: 使用GitLab CI
1. 创建GitLab账号
2. 推送代码到GitLab
3. 创建 `.gitlab-ci.yml`
4. 自动构建APK

### 方案C: 使用Bitrise
1. 访问 https://bitrise.io
2. 连接GitHub仓库
3. 自动构建APK
4. 下载APK

### 方案D: 使用Codemagic
1. 访问 https://codemagic.io
2. 连接GitHub仓库
3. 自动构建APK
4. 下载APK

---

## 📞 需要帮助?

遇到问题?告诉我:
1. 你卡在哪一步?
2. 有什么错误信息?
3. 截图发给我

我会帮你解决! 🚀

---

## ✅ 检查清单

确认每一步都完成:

- [ ] 有GitHub账号
- [ ] 安装Git
- [ ] 配置Git用户名和邮箱
- [ ] 运行 `./setup-github-actions.sh`
- [ ] 在GitHub创建仓库
- [ ] 推送代码到GitHub
- [ ] 访问Actions页面
- [ ] 触发工作流
- [ ] 等待构建完成
- [ ] 下载APK
- [ ] 传输到手机
- [ ] 安装APK
- [ ] 启用无障碍服务
- [ ] 授予悬浮窗权限
- [ ] 测试功能

---

**祝你顺利获得APK! 🎉**
