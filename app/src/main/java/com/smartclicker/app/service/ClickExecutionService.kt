package com.smartclicker.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.smartclicker.app.model.ClickSession
import com.smartclicker.app.model.StepType
import kotlinx.coroutines.*

/**
 * 点击执行服务 - 在后台执行点击操作
 */
class ClickExecutionService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var executionJob: Job? = null
    
    companion object {
        private const val TAG = "ClickExecutionService"
        
        // 通过静态变量访问无障碍服务
        fun getService(): com.smartclicker.app.service.SmartAccessibilityService? {
            return com.smartclicker.app.service.SmartAccessibilityService.instance
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        android.util.Log.i(TAG, "点击执行服务已创建")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val session = intent?.getSerializableExtra("click_session") as? ClickSession
        
        if (session != null) {
            startExecution(session)
        }
        
        return START_NOT_STICKY
    }
    
    /**
     * 开始执行点击方案
     */
    private fun startExecution(session: ClickSession) {
        shouldStop = false
        isClicking = true
        
        executionJob = serviceScope.launch {
            android.util.Log.i(TAG, "开始执行方案: ${session.name}")
            
            for (step in session.steps) {
                if (shouldStop) {
                    android.util.Log.i(TAG, "执行被用户中断")
                    break
                }
                
                // 执行前延迟
                if (step.delay > 0) {
                    delay(step.delay)
                }
                
                // 重复执行步骤
                repeat(step.repeatCount) { repeatIndex ->
                    if (shouldStop) break
                    
                    executeStep(step)
                    
                    // 每次重复之间的间隔
                    if (repeatIndex < step.repeatCount - 1) {
                        delay(100)
                    }
                }
                
                if (shouldStop) break
            }
            
            isClicking = false
            shouldStop = false
            android.util.Log.i(TAG, "方案执行完成")
            
            // 停止服务
            stopSelf()
        }
    }
    
    /**
     * 执行单个步骤
     */
    private suspend fun executeStep(step: com.smartclicker.app.model.ClickStep) {
        val service = getService() ?: return
        
        withContext(Dispatchers.Main) {
            when (step.stepType) {
                StepType.SIMPLE_CLICK -> {
                    // 简单坐标点击
                    service.performClickAt(step.x.toInt(), step.y.toInt())
                }
                
                StepType.LONG_PRESS -> {
                    // 长按 - 需要知道坐标
                    service.performClickAt(step.x.toInt(), step.y.toInt())
                    delay(1000)
                }
                
                StepType.IMAGE_RECOGNITION -> {
                    // 识图点击 - 通过模板图片识别
                    // TODO: 实现图像匹配
                    android.util.Log.d(TAG, "识图点击: ${step.imageTemplatePath}")
                }
                
                StepType.COLOR_RECOGNITION -> {
                    // 颜色识别点击
                    // TODO: 实现颜色匹配
                    android.util.Log.d(TAG, "颜色识别: ${step.colorHex}")
                }
                
                StepType.TEXT_RECOGNITION -> {
                    // 文字识别点击
                    step.searchText?.let { text ->
                        val element = service.findElementByText(text)
                        if (element != null) {
                            service.performClick(element)
                        } else {
                            android.util.Log.w(TAG, "未找到文字: $text")
                        }
                    }
                }
                
                StepType.CONTINUOUS_CLICK -> {
                    // 连续点击
                    repeat(step.repeatCount) {
                        if (shouldStop) return@executeStep
                        service.performClickAt(step.x.toInt(), step.y.toInt())
                        delay(100)
                    }
                }
                
                StepType.SWIPE -> {
                    // 滑动 - 需要起点和终点坐标
                    // TODO: 实现滑动参数
                }
            }
        }
    }
    
    /**
     * 停止执行
     */
    fun stopExecution() {
        shouldStop = true
        executionJob?.cancel()
        android.util.Log.i(TAG, "停止执行")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        isClicking = false
        shouldStop = false
        android.util.Log.i(TAG, "点击执行服务已销毁")
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
