package com.smartclicker.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartclicker.app.di.DIContainer
import kotlinx.coroutines.launch

/**
 * 设置ViewModel
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val settingsRepository = DIContainer.settingsRepository
    
    fun saveSettings(interval: Long, vibrationEnabled: Boolean, soundEnabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = com.smartclicker.app.model.AppSettings(
                autoClickInterval = interval,
                vibrationEnabled = vibrationEnabled,
                soundEnabled = soundEnabled
            )
            settingsRepository.updateSettings(currentSettings)
        }
    }
}
