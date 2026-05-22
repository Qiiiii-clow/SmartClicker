#!/bin/bash

# SmartClicker - GitHub Actions 交互式引导脚本
# 这个脚本会一步步引导你完成APK构建

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_step() {
    echo ""
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${CYAN}  $1${NC}"
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo ""
echo "╔════════════════════════════════════════════════════╗"
echo "║                                                    ║"
echo "║   🚀  SmartClicker APK 构建向导                    ║"
echo "║   通过 GitHub Actions 免费获取APK                 ║"
echo "║                                                    ║"
echo "╚════════════════════════════════════════════════════╝"
echo ""

# 检查Git
print_step "第1步: 检查Git"
if command -v git &> /dev/null; then
    print_success "Git已安装: $(git --version)"
else
    print_error "Git未安装!"
    echo ""
    echo "macOS用户,请运行:"
    echo "  xcode-select --install"
    echo ""
    echo "Windows用户,请下载:"
    echo "  https://git-scm.com/download/win"
    echo ""
    exit 1
fi

# 获取GitHub用户名
print_step "第2步: 获取GitHub用户名"
echo ""
read -p "请输入你的GitHub用户名: " github_user

if [ -z "$github_user" ]; then
    print_error "GitHub用户名不能为空!"
    exit 1
fi

print_success "GitHub用户名: $github_user"

# 检查是否已初始化Git
print_step "第3步: 检查Git仓库"
if [ -d ".git" ]; then
    print_success "Git仓库已初始化"
else
    print_info "初始化Git仓库..."
    git init
    print_success "Git仓库已创建"
fi

# 添加文件
print_step "第4步: 添加文件到Git"
git add .
print_success "所有文件已添加到Git"

# 提交
print_step "第5步: 提交代码"
git commit -m "SmartClicker v1.0.0 - 智能屏幕点击器" 2>/dev/null || {
    print_warning "没有新的更改需要提交"
}
print_success "代码已提交"

# 创建分支
git branch -M main
print_success "主分支已创建"

# 创建仓库指引
print_step "第6步: 创建GitHub仓库"
echo ""
echo -e "${YELLOW}请按照以下步骤操作:${NC}"
echo ""
echo "1. 打开浏览器,访问: https://github.com/new"
echo ""
echo "2. 填写仓库信息:"
echo "   - Repository name: SmartClicker"
echo "   - Description: 智能屏幕点击器"
echo "   - 选择 Public 或 Private (都可以)"
echo ""
echo "3. ⚠️ 重要: 不要勾选以下选项:"
echo "   - ☒ 不要勾选 'Initialize this repository with a README'"
echo "   - ☒ 不要勾选 'Add .gitignore'"
echo "   - ☒ 不要勾选 'Choose a license'"
echo ""
echo "4. 点击 'Create repository' 按钮"
echo ""
echo "5. 创建完成后,按回车继续..."
read -p "> "

# 设置远程仓库
print_step "第7步: 推送代码到GitHub"
echo ""

REMOTE_URL="https://github.com/${github_user}/SmartClicker.git"

# 检查是否已有远程仓库
if git remote get-url origin &> /dev/null; then
    CURRENT_URL=$(git remote get-url origin)
    if [ "$CURRENT_URL" != "$REMOTE_URL" ]; then
        print_warning "已存在远程仓库: $CURRENT_URL"
        read -p "是否替换为 $REMOTE_URL? (y/n): " confirm
        if [ "$confirm" == "y" ]; then
            git remote set-url origin $REMOTE_URL
            print_success "远程仓库已更新"
        else
            print_info "保持原有远程仓库"
        fi
    fi
else
    git remote add origin $REMOTE_URL
    print_success "远程仓库已添加: $REMOTE_URL"
fi

echo ""
echo -e "${YELLOW}推送代码到GitHub...${NC}"
echo ""

# 推送代码
git push -u origin main 2>/dev/null || {
    print_error "推送失败!"
    echo ""
    echo "可能的原因:"
    echo "1. GitHub用户名或仓库名不正确"
    echo "2. 仓库未创建"
    echo "3. 需要认证(使用Personal Access Token)"
    echo ""
    echo "手动推送命令:"
    echo "  git remote add origin https://github.com/${github_user}/SmartClicker.git"
    echo "  git branch -M main"
    echo "  git push -u origin main"
    echo ""
    echo "如果需要生成Personal Access Token:"
    echo "  1. 访问 https://github.com/settings/tokens"
    echo "  2. 点击 'Generate new token (classic)'"
    echo "  3. 勾选 'repo' 权限"
    echo "  4. 生成token,使用token代替密码"
    echo ""
    exit 1
}

print_success "代码已推送到GitHub!"

# 触发构建
print_step "第8步: 触发APK构建"
echo ""
echo "访问以下网址:"
echo ""
echo -e "  ${CYAN}https://github.com/${github_user}/SmartClicker/actions${NC}"
echo ""
echo "操作步骤:"
echo ""
echo "1. 点击顶部的 'Actions' 标签"
echo ""
echo "2. 找到 'Build SmartClicker APK' 工作流"
echo ""
echo "3. 点击右侧的 'Run workflow' 按钮 (蓝色)"
echo ""
echo "4. 选择分支: main"
echo ""
echo "5. 点击绿色 'Run workflow' 按钮"
echo ""
echo "6. 等待10-20分钟,构建完成后:"
echo "   - 在Actions页面底部找到 'Artifacts'"
echo "   - 点击 'SmartClicker-debug-apk' 下载ZIP"
echo "   - 解压ZIP,找到 app-debug.apk"
echo ""

# 显示仓库信息
print_step "完成! 🎉"
echo ""
echo -e "${GREEN}代码已成功推送到GitHub!${NC}"
echo ""
echo "📦 仓库地址:"
echo -e "  ${CYAN}${REMOTE_URL}${NC}"
echo ""
echo "🔨 构建APK:"
echo -e "  ${CYAN}https://github.com/${github_user}/SmartClicker/actions${NC}"
echo ""
echo "📥 下载APK:"
echo "  1. 进入Actions页面"
echo "  2. 点击运行中的工作流"
echo "  3. 等待构建完成"
echo "  4. 在底部下载 'Artifacts'"
echo ""
echo "📱 安装到手机:"
echo "  1. 将APK传输到手机 (USB/微信/ADB)"
echo "  2. 安装APK"
echo "  3. 启用无障碍服务"
echo "  4. 授予悬浮窗权限"
echo "  5. 开始使用!"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "需要帮助? 查看文档:"
echo "  - GitHub_Actions教程.md (详细步骤)"
echo "  - 如何获得APK.md (所有方法)"
echo "  - START_HERE.md (项目入口)"
echo ""
echo -e "${GREEN}祝你顺利获得APK! 🚀${NC}"
echo ""
