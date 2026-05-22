package com.smartclicker.app.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.smartclicker.app.R
import com.smartclicker.app.databinding.ActivitySettingsBinding
import com.smartclicker.app.viewmodel.SettingsViewModel

/**
 * 设置Activity
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViews()
    }

    private fun setupViews() {
        // 检查无障碍服务
        binding.btnEnableAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        // 检查悬浮窗权限
        binding.btnEnableFloatWindow.setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = android.net.Uri.parse("package:$packageName")
            })
        }

        // 保存设置
        binding.btnSaveSettings.setOnClickListener {
            val interval = binding.etAutoClickInterval.text.toString().toLongOrNull() ?: 1000
            val vibration = binding.swVibration.isChecked
            val sound = binding.swSound.isChecked
            
            viewModel.saveSettings(interval, vibration, sound)
            finish()
        }
    }
}
