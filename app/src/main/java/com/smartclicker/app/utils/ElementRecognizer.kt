package com.smartclicker.app.utils

import android.view.accessibility.AccessibilityNodeInfo
import com.smartclicker.app.model.ElementType

/**
 * 元素识别器 - 智能识别屏幕元素类型
 */
object ElementRecognizer {
    
    /**
     * 根据AccessibilityNodeInfo识别元素类型
     */
    fun recognizeElementType(node: AccessibilityNodeInfo): ElementType {
        val className = node.className?.toString()?.lowercase() ?: ""
        val text = node.text?.toString()?.lowercase() ?: ""
        val description = node.contentDescription?.toString()?.lowercase() ?: ""
        
        return when {
            // 按钮
            className.contains("button") || className.contains("btn") -> ElementType.BUTTON
            
            // 输入框
            className.contains("edittext") || className.contains("edit_text") -> ElementType.EDIT_TEXT
            
            // 图片/图片按钮
            className.contains("imageview") && !className.contains("avatar") -> ElementType.IMAGE
            className.contains("imagebutton") -> ElementType.IMAGE_BUTTON
            
            // 链接
            className.contains("textview") && (text.contains("http") || description.contains("link")) -> ElementType.LINK
            
            // 复选框
            className.contains("checkbox") || node.isCheckable -> ElementType.CHECKBOX
            
            // 单选框
            className.contains("radiobutton") -> ElementType.RADIO_BUTTON
            
            // 开关
            className.contains("switch") -> ElementType.SWITCH
            
            // 列表项
            className.contains("listitem") || className.contains("recyclerview") -> ElementType.LIST_ITEM
            
            // 默认未知
            else -> ElementType.UNKNOWN
        }
    }
    
    /**
     * 智能推荐点击目标 - 基于语义分析
     */
    fun recommendClickTargets(elements: List<com.smartclicker.app.model.ScreenElement>, keyword: String): List<com.smartclicker.app.model.ScreenElement> {
        val keywordLower = keyword.lowercase()
        
        return elements.filter { element ->
            val text = element.text.lowercase()
            val description = element.contentDescription.lowercase()
            
            // 关键词匹配
            text.contains(keywordLower) || description.contains(keywordLower)
        }.sortedByDescending { element ->
            // 按相关性排序
            val text = element.text.lowercase()
            val description = element.contentDescription.lowercase()
            
            when {
                text == keywordLower -> 3  // 完全匹配
                description == keywordLower -> 3
                text.startsWith(keywordLower) -> 2  // 前缀匹配
                description.startsWith(keywordLower) -> 2
                text.contains(keywordLower) -> 1  // 包含匹配
                description.contains(keywordLower) -> 1
                else -> 0
            }
        }
    }
    
    /**
     * 判断元素是否可能是广告或无关元素
     */
    fun isAdOrIrrelevant(element: com.smartclicker.app.model.ScreenElement): Boolean {
        val text = element.text.lowercase()
        val description = element.contentDescription.lowercase()
        val combined = "$text $description"
        
        // 常见广告关键词
        val adKeywords = listOf("广告", "ad ", "推广", "推荐", "赞助商", "sponsored")
        
        return adKeywords.any { keyword ->
            combined.contains(keyword)
        }
    }
}
