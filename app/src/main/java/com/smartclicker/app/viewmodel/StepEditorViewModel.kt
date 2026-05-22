package com.smartclicker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smartclicker.app.model.ClickStep
import com.smartclicker.app.model.StepType

/**
 * 步骤编辑器ViewModel
 */
class StepEditorViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _currentStepType = MutableLiveData<StepType>()
    private val _selectedPosition = MutableLiveData<Pair<Float, Float>?>()
    private val _searchText = MutableLiveData<String?>()
    
    val selectedPosition: LiveData<Pair<Float, Float>?> = _selectedPosition
    
    fun setCurrentStepType(type: StepType) {
        _currentStepType.value = type
    }
    
    fun setSelectedPosition(x: Float, y: Float) {
        _selectedPosition.value = Pair(x, y)
    }
    
    fun setSearchText(text: String) {
        _searchText.value = text
    }
    
    fun getCurrentStep(): ClickStep? {
        val position = _selectedPosition.value ?: return null
        
        return ClickStep(
            title = "${getStepTypeText(_currentStepType.value ?: StepType.SIMPLE_CLICK)} ${position.first.toInt()},${position.second.toInt()}",
            stepType = _currentStepType.value ?: StepType.SIMPLE_CLICK,
            x = position.first,
            y = position.second,
            delay = 100,
            repeatCount = 1,
            searchText = _searchText.value
        )
    }
    
    private fun getStepTypeText(type: StepType): String {
        return when (type) {
            StepType.SIMPLE_CLICK -> "点击"
            StepType.CONTINUOUS_CLICK -> "连续点击"
            StepType.TEXT_RECOGNITION -> "识别点击"
            else -> "操作"
        }
    }
}
