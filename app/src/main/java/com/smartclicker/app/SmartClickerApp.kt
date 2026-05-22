package com.smartclicker.app

import android.app.Application

/**
 * 全局应用类
 */
class SmartClickerApp : Application() {
    
    companion object {
        lateinit var instance: SmartClickerApp
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
