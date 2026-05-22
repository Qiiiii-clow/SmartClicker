package com.smartclicker.app.service

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.smartclicker.app.R

/**
 * 悬浮窗服务 - 提供快捷操作入口
 */
class FloatWindowService(private val context: Context) {
    
    private var windowManager: WindowManager? = null
    private var floatView: View? = null
    private var isShowing = false
    
    // 悬浮窗参数
    private val params = WindowManager.LayoutParams().apply {
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL 
            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        format = PixelFormat.TRANSLUCENT
        gravity = Gravity.TOP or Gravity.START
    }
    
    /**
     * 显示悬浮窗
     */
    fun show() {
        if (isShowing) return
        
        try {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            
            floatView = LayoutInflater.from(context).inflate(R.layout.view_float_window, null).apply {
                // 拖拽功能
                setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_MOVE) {
                        params.x = event.rawX.toInt()
                        params.y = event.rawY.toInt()
                        windowManager?.updateViewLayout(this, params)
                        true
                    } else {
                        false
                    }
                }
                
                // 点击功能 - 打开应用
                setOnClickListener {
                    val intent = android.content.Intent(context, com.smartclicker.app.ui.MainActivity::class.java).apply {
                        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            }
            
            windowManager?.addView(floatView, params)
            isShowing = true
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 隐藏悬浮窗
     */
    fun hide() {
        if (!isShowing) return
        
        try {
            floatView?.let { view ->
                windowManager?.removeView(view)
            }
            isShowing = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 销毁悬浮窗
     */
    fun destroy() {
        hide()
        windowManager = null
        floatView = null
    }
}
