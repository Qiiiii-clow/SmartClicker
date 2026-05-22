package com.smartclicker.app.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.smartclicker.app.model.ScreenElement
import com.smartclicker.app.model.ElementType
import com.smartclicker.app.utils.ElementRecognizer

/**
 * 智能无障碍服务 - 核心服务
 * 负责屏幕元素识别、智能点击等功能
 */
class SmartAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "SmartAccessibilityService"
        
        var instance: SmartAccessibilityService? = null
        
        // 当前是否正在执行点击
        var isClicking = false
        
        // 是否停止点击的标志
        var shouldStop = false
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        android.util.Log.i(TAG, "无障碍服务已连接")
    }
    
    override fun onUnbind(intent: android.content.Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }
    
    /**
     * 处理无障碍事件
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 可以监听界面变化,更新UI等
    }
    
    override fun onInterrupt() {
        android.util.Log.w(TAG, "无障碍服务被中断")
    }
    
    /**
     * 扫描屏幕上的所有可点击元素
     */
    fun scanClickableElements(): List<ScreenElement> {
        val rootNode = rootInActiveWindow ?: return emptyList()
        val elements = mutableListOf<ScreenElement>()
        
        // 递归遍历所有节点
        traverseNode(rootNode, elements)
        
        android.util.Log.d(TAG, "扫描到 ${elements.size} 个元素, 可点击: ${elements.count { it.isClickable }}")
        return elements
    }
    
    /**
     * 递归遍历节点树
     */
    private fun traverseNode(node: AccessibilityNodeInfo, elements: MutableList<ScreenElement>) {
        // 如果元素可点击,添加到列表
        if (node.isClickable && node.boundsInScreen != null) {
            val element = ScreenElement(
                className = node.className?.toString() ?: "",
                text = node.text?.toString() ?: "",
                contentDescription = node.contentDescription?.toString() ?: "",
                isClickable = node.isClickable,
                bounds = Rect(
                    node.boundsInScreen.left,
                    node.boundsInScreen.top,
                    node.boundsInScreen.right,
                    node.boundsInScreen.bottom
                ),
                packageName = node.packageName?.toString() ?: "",
                elementType = ElementRecognizer.recognizeElementType(node),
                confidence = 1.0f
            )
            
            // 只添加有意义的元素
            if (element.text.isNotEmpty() || element.contentDescription.isNotEmpty() || 
                element.className.contains("Button", ignoreCase = true) ||
                element.className.contains("ImageView", ignoreCase = true)) {
                elements.add(element)
            }
        }
        
        // 递归遍历子节点
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { child ->
                traverseNode(child, elements)
            }
        }
    }
    
    /**
     * 智能查找元素 - 通过文本或描述
     */
    fun findElementByText(text: String): ScreenElement? {
        val elements = scanClickableElements()
        return elements.firstOrNull { element ->
            element.text.contains(text, ignoreCase = true) ||
            element.contentDescription.contains(text, ignoreCase = true)
        }
    }
    
    /**
     * 智能查找元素 - 通过类名
     */
    fun findElementsByClassName(className: String): List<ScreenElement> {
        return scanClickableElements().filter { element ->
            element.className.contains(className, ignoreCase = true)
        }
    }
    
    /**
     * 执行点击操作
     */
    fun performClick(element: ScreenElement): Boolean {
        return try {
            val centerX = element.bounds.centerX()
            val centerY = element.bounds.centerY()
            
            // 使用手势描述执行点击
            val builder = GestureDescription.Builder()
            val stroke = GestureDescription.StrokeDescription(
                android.graphics.Path().apply {
                    moveTo(centerX.toFloat(), centerY.toFloat())
                },
                0L,
                50L
            )
            builder.addStroke(stroke)
            
            val gesture = builder.build()
            dispatchGesture(gesture, null, null)
            
            android.util.Log.d(TAG, "执行点击: $text at ($centerX, $centerY)")
            true
        } catch (e: Exception) {
            android.util.Log.e(TAG, "点击失败", e)
            false
        }
    }
    
    /**
     * 执行坐标点击
     */
    fun performClickAt(x: Int, y: Int): Boolean {
        return try {
            val builder = GestureDescription.Builder()
            val stroke = GestureDescription.StrokeDescription(
                android.graphics.Path().apply {
                    moveTo(x.toFloat(), y.toFloat())
                },
                0L,
                50L
            )
            builder.addStroke(stroke)
            
            val gesture = builder.build()
            dispatchGesture(gesture, null, null)
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 长按操作
     */
    fun performLongPress(element: ScreenElement, duration: Long = 1000L): Boolean {
        return try {
            val centerX = element.bounds.centerX()
            val centerY = element.bounds.centerY()
            
            val builder = GestureDescription.Builder()
            val stroke = GestureDescription.StrokeDescription(
                android.graphics.Path().apply {
                    moveTo(centerX.toFloat(), centerY.toFloat())
                },
                0L,
                duration
            )
            builder.addStroke(stroke)
            
            val gesture = builder.build()
            dispatchGesture(gesture, null, null)
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 滑动操作
     */
    fun performSwipe(x1: Int, y1: Int, x2: Int, y2: Int, duration: Long = 500L): Boolean {
        return try {
            val builder = GestureDescription.Builder()
            val stroke = GestureDescription.StrokeDescription(
                android.graphics.Path().apply {
                    moveTo(x1.toFloat(), y1.toFloat())
                    lineTo(x2.toFloat(), y2.toFloat())
                },
                0L,
                duration
            )
            builder.addStroke(stroke)
            
            val gesture = builder.build()
            dispatchGesture(gesture, null, null)
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 停止所有操作
     */
    fun stopAllOperations() {
        shouldStop = true
        android.util.Log.i(TAG, "停止所有操作")
    }
}
