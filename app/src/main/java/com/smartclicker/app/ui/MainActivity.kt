package com.smartclicker.app.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartclicker.app.R
import com.smartclicker.app.databinding.ActivityMainBinding
import com.smartclicker.app.model.ClickSession
import com.smartclicker.app.service.ClickExecutionService
import com.smartclicker.app.service.SmartAccessibilityService
import com.smartclicker.app.viewmodel.MainViewModel
import com.smartclicker.app.adapter.StepListAdapter

/**
 * 主Activity - 应用入口界面
 */
class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var stepAdapter: StepListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        setupObservers()
        checkPermissions()
    }
    
    private fun setupViews() {
        // 设置步骤列表
        stepAdapter = StepListAdapter(
            onItemClickListener = { step ->
                // TODO: 显示步骤详情
            },
            onDeleteListener = { step ->
                showDeleteConfirmation(step)
            }
        )
        
        binding.rvSteps.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = stepAdapter
        }
        
        // 开始/停止点击按钮
        binding.btnStartClicking.setOnClickListener {
            startClicking()
        }
        
        // 设置按钮
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        // 智能扫描按钮
        binding.btnSmartScan.setOnClickListener {
            startSmartScan()
        }
        
        // 添加步骤按钮
        binding.btnAddStep.setOnClickListener {
            startActivity(Intent(this, StepEditorActivity::class.java))
        }
        
        // 方案管理按钮
        binding.btnSessionManager.setOnClickListener {
            showSessionManager()
        }
    }
    
    private fun setupObservers() {
        // 观察点击状态
        viewModel.isClicking.observe(this) { isClicking ->
            if (isClicking) {
                binding.btnStartClicking.text = getString(R.string.main_stop_clicking)
                binding.btnStartClicking.setBackgroundColor(getColor(R.color.accent_red))
                binding.rvSteps.visibility = View.GONE
            } else {
                binding.btnStartClicking.text = getString(R.string.main_start_clicking)
                binding.btnStartClicking.setBackgroundColor(getColor(R.color.accent_green))
                binding.rvSteps.visibility = View.VISIBLE
            }
        }
        
        // 观察点击步骤
        viewModel.steps.observe(this) { steps ->
            stepAdapter.submitList(steps)
        }
        
        // 观察提示消息
        viewModel.snackbarMessage.observe(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
        
        // 观察扫描结果
        viewModel.scanResult.observe(this) { elements ->
            if (elements.isNotEmpty()) {
                binding.tvScanResult.visibility = View.VISIBLE
                binding.tvScanResult.text = getString(R.string.scanner_found_elements, elements.size)
            } else {
                binding.tvScanResult.visibility = View.VISIBLE
                binding.tvScanResult.text = getString(R.string.scanner_no_elements)
            }
        }
    }
    
    /**
     * 检查所需权限
     */
    private fun checkPermissions() {
        // 检查无障碍服务是否启用
        if (!isAccessibilityServiceEnabled()) {
            showAccessibilityDialog()
        }
        
        // 检查悬浮窗权限 (Android 6.0+)
        if (!Settings.canDrawOverlays(this)) {
            Snackbar.make(
                binding.root,
                "请在设置中授予悬浮窗权限",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("去设置") {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = android.net.Uri.parse("package:$packageName")
                })
            }.show()
        }
    }
    
    /**
     * 检查无障碍服务是否启用
     */
    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "com.smartclicker.app/.service.SmartAccessibilityService"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: ""
        
        return enabledServices.contains(service)
    }
    
    /**
     * 显示启用无障碍服务对话框
     */
    private fun showAccessibilityDialog() {
        AlertDialog.Builder(this)
            .setTitle("需要启用无障碍服务")
            .setMessage("智能点击器需要无障碍服务才能识别屏幕元素和执行点击操作。是否现在去设置?")
            .setPositiveButton("去设置") { _, _ ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    /**
     * 开始点击
     */
    private fun startClicking() {
        if (viewModel.isClicking.value == true) {
            // 停止点击
            viewModel.stopClicking()
            ClickExecutionService.getService()?.stopAllOperations()
        } else {
            // 开始点击
            if (!isAccessibilityServiceEnabled()) {
                Snackbar.make(binding.root, R.string.toast_no_permission, Snackbar.LENGTH_LONG).show()
                return
            }
            
            val currentSession = viewModel.currentSession.value
            if (currentSession != null && currentSession.steps.isNotEmpty()) {
                viewModel.startClicking()
                
                // 启动点击执行服务
                val intent = Intent(this, ClickExecutionService::class.java).apply {
                    putExtra("click_session", currentSession)
                }
                startService(intent)
            } else {
                Snackbar.make(binding.root, "请先添加点击步骤", Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    /**
     * 开始智能扫描
     */
    private fun startSmartScan() {
        if (!isAccessibilityServiceEnabled()) {
            Snackbar.make(binding.root, "请先启用无障碍服务", Snackbar.LENGTH_LONG).show()
            return
        }
        
        binding.btnSmartScan.isEnabled = false
        binding.btnSmartScan.text = getString(R.string.scanner_scanning)
        
        viewModel.startScan(this)
        
        // 3秒后恢复按钮状态
        android.os.Handler(mainLooper).postDelayed({
            binding.btnSmartScan.isEnabled = true
            binding.btnSmartScan.text = getString(R.string.scanner_start)
        }, 3000)
    }
    
    /**
     * 显示方案管理器
     */
    private fun showSessionManager() {
        AlertDialog.Builder(this)
            .setTitle("点击方案管理")
            .setPositiveButton("新建方案") { _, _ ->
                viewModel.createNewSession()
            }
            .setOnItemClickListener { _, which, position ->
                // TODO: 加载选中的方案
            }
            .show()
    }
    
    /**
     * 显示删除确认对话框
     */
    private fun showDeleteConfirmation(step: com.smartclicker.app.model.ClickStep) {
        AlertDialog.Builder(this)
            .setTitle("删除步骤")
            .setMessage("确定要删除这个步骤吗?")
            .setPositiveButton("删除") { _, _ ->
                viewModel.removeStep(step)
            }
            .setNegativeButton("取消", null)
            .show()
    }
}
