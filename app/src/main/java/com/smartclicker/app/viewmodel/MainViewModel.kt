package com.smartclicker.app.viewmodel

import android.app.Application
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartclicker.app.di.DIContainer
import com.smartclicker.app.model.ClickSession
import com.smartclicker.app.model.ClickStep
import com.smartclicker.app.model.ScreenElement
import com.smartclicker.app.model.StepType
import com.smartclicker.app.service.SmartAccessibilityService
import kotlinx.coroutines.launch

/**
 * 主ViewModel
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val settingsRepository = DIContainer.settingsRepository
    private val sessionRepository = DIContainer.clickSessionRepository
    
    // 当前点击方案
    private val _currentSession = MutableLiveData<ClickSession?>()
    val currentSession: LiveData<ClickSession?> = _currentSession
    
    // 点击状态
    private val _isClicking = MutableLiveData<Boolean>()
    val isClicking: LiveData<Boolean> = _isClicking
    
    // 点击步骤列表
    private val _steps = MutableLiveData<List<ClickStep>>()
    val steps: LiveData<List<ClickStep>> = _steps
    
    // 扫描结果
    private val _scanResult = MutableLiveData<List<ScreenElement>>()
    val scanResult: LiveData<List<ScreenElement>> = _scanResult
    
    // 提示消息
    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage
    
    init {
        // 加载当前方案或创建新方案
        loadCurrentSession()
    }
    
    /**
     * 加载当前保存的方案
     */
    private fun loadCurrentSession() {
        viewModelScope.launch {
            val sessions = sessionRepository.loadAllSessions()
            if (sessions.isNotEmpty()) {
                _currentSession.value = sessions.first()
                _steps.value = sessions.first().steps
            } else {
                createNewSession()
            }
        }
    }
    
    /**
     * 创建新方案
     */
    fun createNewSession() {
        val newSession = ClickSession(
            name = "新方案 ${System.currentTimeMillis()}",
            steps = emptyList()
        )
        _currentSession.value = newSession
        _steps.value = emptyList()
    }
    
    /**
     * 添加点击步骤
     */
    fun addStep(step: ClickStep) {
        val currentSteps = _steps.value ?: emptyList()
        _steps.value = currentSteps + step
        
        // 更新当前方案
        _currentSession.value?.let { session ->
            val updatedSession = session.copy(
                steps = currentSteps + step,
                updatedAt = System.currentTimeMillis()
            )
            _currentSession.value = updatedSession
        }
    }
    
    /**
     * 移除点击步骤
     */
    fun removeStep(step: ClickStep) {
        val currentSteps = _steps.value?.filter { it.id != step.id } ?: emptyList()
        _steps.value = currentSteps
        
        // 更新当前方案
        _currentSession.value?.let { session ->
            val updatedSession = session.copy(
                steps = currentSteps,
                updatedAt = System.currentTimeMillis()
            )
            _currentSession.value = updatedSession
        }
    }
    
    /**
     * 开始点击
     */
    fun startClicking() {
        _isClicking.value = true
        SmartAccessibilityService.isClicking = true
        SmartAccessibilityService.shouldStop = false
    }
    
    /**
     * 停止点击
     */
    fun stopClicking() {
        _isClicking.value = false
        SmartAccessibilityService.isClicking = false
        SmartAccessibilityService.shouldStop = true
    }
    
    /**
     * 开始智能扫描
     */
    fun startScan(context: android.content.Context) {
        viewModelScope.launch {
            val service = SmartAccessibilityService.instance
            if (service != null) {
                val elements = service.scanClickableElements()
                _scanResult.value = elements
            }
        }
    }
    
    /**
     * 保存当前方案
     */
    fun saveCurrentSession() {
        _currentSession.value?.let { session ->
            viewModelScope.launch {
                val success = sessionRepository.saveSession(session)
                _snackbarMessage.value = if (success) {
                    "方案已保存"
                } else {
                    "保存失败"
                }
            }
        }
    }
    
    /**
     * 快速添加简单点击步骤
     */
    fun addSimpleClickStep(x: Float, y: Float) {
        val step = ClickStep(
            title = "点击 ($x, $y)",
            stepType = StepType.SIMPLE_CLICK,
            x = x,
            y = y,
            delay = 100,
            repeatCount = 1
        )
        addStep(step)
    }
    
    /**
     * 快速添加文字识别点击步骤
     */
    fun addTextRecognitionStep(text: String) {
        val step = ClickStep(
            title = "识别文字: $text",
            stepType = StepType.TEXT_RECOGNITION,
            searchText = text,
            delay = 200
        )
        addStep(step)
    }
}
