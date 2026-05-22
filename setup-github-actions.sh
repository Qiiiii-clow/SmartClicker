#!/bin/bash

# SmartClicker GitHub Actions 快速设置脚本
# 用法: ./setup-github-actions.sh

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
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

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo ""
echo "======================================"
echo "  SmartClicker GitHub Actions 设置"
echo "======================================"
echo ""

# 检查是否已初始化git
if [ ! -d ".git" ]; then
    print_info "初始化Git仓库..."
    git init
    print_success "Git仓库已初始化"
fi

# 添加所有文件
print_info "添加文件到Git..."
git add .
git status

# 询问用户GitHub用户名
echo ""
print_info "请输入你的GitHub用户名:"
read -p "> " github_user

if [ -z "$github_user" ]; then
    print_error "GitHub用户名不能为空"
    exit 1
fi

# 创建仓库
echo ""
print_info "请在GitHub上创建新仓库:"
print_info "1. 访问 https://github.com/new"
print_info "2. 仓库名: SmartClicker"
print_info "3. 设为Public或Private"
print_info "4. 不要勾选'Initialize this repository with a README'"
echo ""
read -p "创建完成后按回车继续..."

# 设置远程仓库
REMOTE_URL="https://github.com/${github_user}/SmartClicker.git"
print_info "设置远程仓库: $REMOTE_URL"

# 检查是否已有远程仓库
if git remote get-url origin 2>/dev/null; then
    print_warning "已存在远程仓库,是否替换? (y/n)"
    read -p "> " confirm
    if [ "$confirm" == "y" ]; then
        git remote set-url origin $REMOTE_URL
    else
        print_info "跳过远程仓库设置"
    fi
else
    git remote add origin $REMOTE_URL
fi

# 提交代码
print_info "提交代码..."
git branch -M main
git commit -m "SmartClicker v1.0.0 - 智能屏幕点击器" --allow-empty-message 2>/dev/null || git commit -m "SmartClicker v1.0.0"

# 推送代码
print_info "推送代码到GitHub..."
git push -u origin main 2>/dev/null || {
    print_error "推送失败,请检查:"
    print_error "1. GitHub用户名是否正确"
    print_error "2. 仓库是否已创建"
    print_error "3. 是否有推送权限"
    echo ""
    print_info "手动推送命令:"
    echo "  git remote add origin https://github.com/${github_user}/SmartClicker.git"
    echo "  git branch -M main"
    echo "  git push -u origin main"
    exit 1
}

print_success "代码已推送到GitHub!"
echo ""

# 触发构建
print_info "现在触发APK构建..."
print_info "1. 访问 https://github.com/${github_user}/SmartClicker/actions"
print_info "2. 点击 'Build SmartClicker APK' 工作流"
print_info "3. 点击 'Run workflow' 按钮"
print_info "4. 等待10-20分钟"
print_info "5. 构建完成后在 'Artifacts' 下载APK"
echo ""

print_success "设置完成!"
echo ""
echo "======================================"
echo "  下一步操作"
echo "======================================"
echo ""
echo "  访问: https://github.com/${github_user}/SmartClicker"
echo "  构建: https://github.com/${github_user}/SmartClicker/actions"
echo "  APK:  构建完成后在 Artifacts 下载"
echo ""
print_info "需要帮助? 查看 如何获得APK.md"
echo ""
