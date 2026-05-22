package com.smartclicker.app.model

/**
 * 点击步骤模型
 */
data class ClickStep(
    val id: Long = System.currentTimeMillis(),
    val title: String = "",
    val stepType: StepType = StepType.SIMPLE_CLICK,
    val x: Float = 0f,
    val y: Float = 0f,
    val delay: Long = 100,           // 执行前延迟(毫秒)
    val repeatCount: Int = 1,        // 重复次数
    val imageTemplatePath: String? = null,  // 识图点击模板图片路径
    val colorHex: String? = null,    // 颜色识别的色值
    val searchText: String? = null,  // 文字识别的搜索文本
    val packageName: String? = null, // 目标应用包名
    val className: String? = null    // 目标控件类名
)

/**
 * 步骤类型枚举
 */
enum class StepType {
    SIMPLE_CLICK,           // 简单点击
    CONTINUOUS_CLICK,       // 连续点击
    IMAGE_RECOGNITION,      // 识图点击
    COLOR_RECOGNITION,      // 颜色识别
    TEXT_RECOGNITION,       // 文字识别
    LONG_PRESS,             // 长按
    SWIPE                   // 滑动
}

/**
 * 屏幕元素模型
 */
data class ScreenElement(
    val className: String = "",
    val text: String = "",
    val contentDescription: String = "",
    val isClickable: Boolean = false,
    val bounds: Rect = Rect(),
    val packageName: String = "",
    val elementType: ElementType = ElementType.UNKNOWN,
    val confidence: Float = 1.0f  // 识别置信度
)

/**
 * 元素类型枚举
 */
enum class ElementType {
    BUTTON,
    EDIT_TEXT,
    IMAGE,
    LINK,
    CHECKBOX,
    RADIO_BUTTON,
    IMAGE_BUTTON,
    SWITCH,
    LIST_ITEM,
    UNKNOWN
}

/**
 * 屏幕元素边界
 */
data class Rect(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0
) {
    fun centerX(): Int = (left + right) / 2
    fun centerY(): Int = (top + bottom) / 2
    fun width(): Int = right - left
    fun height(): Int = bottom - top
}

/**
 * 点击方案模型
 */
data class ClickSession(
    val id: Long = System.currentTimeMillis(),
    val name: String = "我的方案",
    val steps: List<ClickStep> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

/**
 * 应用设置模型
 */
data class AppSettings(
    val accessibilityEnabled: Boolean = false,
    val autoClickInterval: Long = 1000,
    val maxClickCount: Int = 0,  // 0表示无限制
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = false,
    val floatWindowEnabled: Boolean = false,
    val smartScanEnabled: Boolean = true,
    val lastUsedPackageName: String? = null
)
