package com.smartclicker.app.di

import com.smartclicker.app.data.repository.ClickSessionRepository
import com.smartclicker.app.data.repository.SettingsRepository

/**
 * 依赖注入容器
 */
object DIContainer {
    
    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository.getInstance(SmartClickerApp.instance)
    }
    
    val clickSessionRepository: ClickSessionRepository by lazy {
        ClickSessionRepository.getInstance(SmartClickerApp.instance)
    }
}
