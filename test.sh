#!/bin/bash

# SmartClicker 快速测试脚本
# 用法: ./test.sh [install|run|log|clean|all]

set -e

APP_PACKAGE="com.smartclicker.app"
APP_NAME="智能点击器"
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
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

# 检查ADB连接
check_adb() {
    print_info "检查ADB连接..."
    if ! adb devices | grep -q "device"; then
        print_error "未检测到设备,请检查连接"
        exit 1
    fi
    print_success "设备连接正常"
}

# 清理项目
clean_project() {
    print_info "清理项目..."
    ./gradlew clean
    print_success "清理完成"
}

# 编译APK
build_apk() {
    print_info "开始编译APK..."
    ./gradlew assembleDebug
    if [ $? -eq 0 ]; then
        print_success "APK编译成功: $APK_PATH"
    else
        print_error "APK编译失败"
        exit 1
    fi
}

# 安装APK
install_apk() {
    print_info "安装APK..."
    
    if [ ! -f "$APK_PATH" ]; then
        print_warning "APK不存在,开始编译..."
        build_apk
    fi
    
    # 卸载旧版本
    adb uninstall $APP_PACKAGE 2>/dev/null || true
    
    # 安装新版本
    adb install -r "$APK_PATH"
    
    if [ $? -eq 0 ]; then
        print_success "APK安装成功"
    else
        print_error "APK安装失败"
        exit 1
    fi
}

# 启动应用
launch_app() {
    print_info "启动应用..."
    adb shell am start -n $APP_PACKAGE/.ui.MainActivity
    
    # 等待应用启动
    sleep 2
    
    # 检查应用是否运行
    if adb shell dumpsys window | grep -q "$APP_PACKAGE"; then
        print_success "应用启动成功"
    else
        print_warning "应用可能未正常启动"
    fi
}

# 查看日志
view_logs() {
    print_info "查看实时日志 (Ctrl+C 退出)..."
    adb logcat | grep --line-buffered "SmartClicker\|AccessibilityService"
}

# 配置无障碍服务
setup_accessibility() {
    print_info "配置无障碍服务..."
    print_warning "请手动在设置中启用无障碍服务"
    print_info "设置 → 辅助功能 → 无障碍 → 智能点击器 → 开启"
    
    # 等待用户确认
    read -p "是否已启用? (y/n): " confirm
    if [ "$confirm" == "y" ]; then
        print_success "无障碍服务已配置"
    else
        print_warning "请先配置无障碍服务再测试"
    fi
}

# 授予悬浮窗权限
grant_float_permission() {
    print_info "授予悬浮窗权限..."
    adb shell pm grant $APP_PACKAGE android.permission.SYSTEM_ALERT_WINDOW
    print_success "悬浮窗权限已授予"
}

# 运行完整测试流程
full_test() {
    echo ""
    echo "======================================"
    echo "  SmartClicker 完整测试流程"
    echo "======================================"
    echo ""
    
    # 1. 检查设备
    check_adb
    
    # 2. 清理和编译
    clean_project
    build_apk
    
    # 3. 安装应用
    install_apk
    
    # 4. 启动应用
    launch_app
    
    echo ""
    echo "======================================"
    echo "  测试包已安装: $APP_NAME"
    echo "======================================"
    echo ""
    print_info "接下来请手动测试以下步骤:"
    echo ""
    echo "1. 启动应用并检查界面"
    echo "2. 启用无障碍服务"
    echo "3. 授予悬浮窗权限"
    echo "4. 测试简单点击功能"
    echo "5. 测试智能扫描功能"
    echo "6. 测试文字识别功能"
    echo "7. 测试方案保存和加载"
    echo ""
    print_info "查看实时日志,请输入: ./test.sh log"
    echo ""
}

# 显示帮助信息
show_help() {
    echo "SmartClicker 测试脚本"
    echo ""
    echo "用法: ./test.sh [命令]"
    echo ""
    echo "命令:"
    echo "  install    - 编译并安装APK"
    echo "  run        - 安装并启动应用"
    echo "  log        - 查看实时日志"
    echo "  setup      - 配置服务和权限"
    echo "  clean      - 清理项目"
    echo "  build      - 编译APK"
    echo "  all        - 完整测试流程(推荐)"
    echo "  help       - 显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  ./test.sh all     # 完整测试流程"
    echo "  ./test.sh log     # 查看日志"
    echo "  ./test.sh setup   # 配置服务"
}

# 主函数
case "${1:-help}" in
    install)
        check_adb
        build_apk
        install_apk
        ;;
    run)
        check_adb
        build_apk
        install_apk
        launch_app
        ;;
    log)
        view_logs
        ;;
    setup)
        setup_accessibility
        grant_float_permission
        ;;
    clean)
        clean_project
        ;;
    build)
        build_apk
        ;;
    all)
        full_test
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "未知命令: $1"
        show_help
        exit 1
        ;;
esac
