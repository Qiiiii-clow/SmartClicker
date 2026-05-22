package com.smartclicker.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smartclicker.app.model.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * 设置仓库 - 管理应用设置
 */
class SettingsRepository private constructor(context: Context) {
    
    private val dataStore = context.dataStore
    
    companion object {
        private const val TAG = "SettingsRepository"
        
        private val ACCESSIBILITY_ENABLED_KEY = booleanPreferencesKey("accessibility_enabled")
        private val AUTO_CLICK_INTERVAL_KEY = longPreferencesKey("auto_click_interval")
        private val MAX_CLICK_COUNT_KEY = longPreferencesKey("max_click_count")
        private val VIBRATION_ENABLED_KEY = booleanPreferencesKey("vibration_enabled")
        private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
        private val FLOAT_WINDOW_ENABLED_KEY = booleanPreferencesKey("float_window_enabled")
        private val SMART_SCAN_ENABLED_KEY = booleanPreferencesKey("smart_scan_enabled")
        private val LAST_PACKAGE_NAME_KEY = stringPreferencesKey("last_package_name")
        
        @Volatile
        private var instance: SettingsRepository? = null
        
        fun getInstance(context: Context): SettingsRepository {
            return instance ?: synchronized(this) {
                SettingsRepository(context).also {
                    instance = it
                }
            }
        }
    }
    
    /**
     * 获取设置流
     */
    val settingsFlow: Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            accessibilityEnabled = preferences[ACCESSIBILITY_ENABLED_KEY] ?: false,
            autoClickInterval = preferences[AUTO_CLICK_INTERVAL_KEY] ?: 1000,
            maxClickCount = preferences[MAX_CLICK_COUNT_KEY]?.toInt() ?: 0,
            vibrationEnabled = preferences[VIBRATION_ENABLED_KEY] ?: true,
            soundEnabled = preferences[SOUND_ENABLED_KEY] ?: false,
            floatWindowEnabled = preferences[FLOAT_WINDOW_ENABLED_KEY] ?: false,
            smartScanEnabled = preferences[SMART_SCAN_ENABLED_KEY] ?: true,
            lastUsedPackageName = preferences[LAST_PACKAGE_NAME_KEY]
        )
    }
    
    /**
     * 更新设置
     */
    suspend fun updateSettings(settings: AppSettings) {
        dataStore.edit { preferences ->
            preferences[ACCESSIBILITY_ENABLED_KEY] = settings.accessibilityEnabled
            preferences[AUTO_CLICK_INTERVAL_KEY] = settings.autoClickInterval
            preferences[MAX_CLICK_COUNT_KEY] = settings.maxClickCount.toLong()
            preferences[VIBRATION_ENABLED_KEY] = settings.vibrationEnabled
            preferences[SOUND_ENABLED_KEY] = settings.soundEnabled
            preferences[FLOAT_WINDOW_ENABLED_KEY] = settings.floatWindowEnabled
            preferences[SMART_SCAN_ENABLED_KEY] = settings.smartScanEnabled
            preferences[LAST_PACKAGE_NAME_KEY] = settings.lastUsedPackageName
        }
    }
}
