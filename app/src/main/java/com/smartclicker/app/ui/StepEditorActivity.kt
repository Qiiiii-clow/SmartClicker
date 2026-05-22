package com.smartclicker.app.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.smartclicker.app.R
import com.smartclicker.app.databinding.ActivityStepEditorBinding
import com.smartclicker.app.model.ClickStep
import com.smartclicker.app.model.StepType
import com.smartclicker.app.viewmodel.StepEditorViewModel

/**
 * 步骤编辑器Activity
 * 支持在屏幕上点击选择位置
 */
class StepEditorActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var binding: ActivityStepEditorBinding
    private val viewModel: StepEditorViewModel by viewModels()

    // 是否处于选择位置模式
    private var isSelectingPosition = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        // 步骤类型选择
        binding.btnSimpleClick.setOnClickListener {
            selectStepType(StepType.SIMPLE_CLICK)
        }

        binding.btnContinuousClick.setOnClickListener {
            selectStepType(StepType.CONTINUOUS_CLICK)
        }

        binding.btnImageRecognition.setOnClickListener {
            showImageRecognitionDialog()
        }

        binding.btnColorRecognition.setOnClickListener {
            showColorRecognitionDialog()
        }

        binding.btnTextRecognition.setOnClickListener {
            showTextRecognitionDialog()
        }

        binding.btnLongPress.setOnClickListener {
            selectStepType(StepType.LONG_PRESS)
        }

        // 保存步骤
        binding.btnSaveStep.setOnClickListener {
            saveStep()
        }

        // 触摸监听 - 用于选择点击位置
        binding.touchArea.setOnTouchListener { _, event ->
            if (isSelectingPosition && event.action == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                viewModel.setSelectedPosition(x, y)
                binding.tvSelectedPosition.text = String.format("已选择: (%.0f, %.0f)", x, y)
                isSelectingPosition = false
                binding.touchArea.background?.alpha = 0 // 移除高亮
                true
            } else {
                true
            }
        }
    }

    private fun setupObservers() {
        // 观察选择的位置
        viewModel.selectedPosition.observe(this) { position ->
            position?.let { (x, y) ->
                // 在屏幕上显示选择点
                showSelectionPoint(x, y)
            }
        }
    }

    private fun selectStepType(type: StepType) {
        viewModel.setCurrentStepType(type)
        
        // 进入位置选择模式
        isSelectingPosition = true
        binding.touchArea.background = android.graphics.drawable.ColorDrawable(0x33000000)
        
        AlertDialog.Builder(this)
            .setTitle("选择点击位置")
            .setMessage("请在屏幕上点击选择要点击的位置")
            .setPositiveButton("取消") { _, _ ->
                isSelectingPosition = false
                binding.touchArea.background?.alpha = 0
            }
            .setNegativeButton("确认") { _, _ ->
                isSelectingPosition = false
                binding.touchArea.background?.alpha = 0
            }
            .show()
    }

    private fun showImageRecognitionDialog() {
        // TODO: 实现图片选择
    }

    private fun showColorRecognitionDialog() {
        // TODO: 实现颜色选择
    }

    private fun showTextRecognitionDialog() {
        AlertDialog.Builder(this)
            .setTitle("文字识别点击")
            .setMessage("请输入要识别并点击的文字")
            .setView(binding.etTextInput)
            .setPositiveButton("保存") { _, _ ->
                val text = binding.etTextInput.text.toString()
                if (text.isNotEmpty()) {
                    viewModel.setCurrentStepType(StepType.TEXT_RECOGNITION)
                    viewModel.setSearchText(text)
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun saveStep() {
        val step = viewModel.getCurrentStep()
        if (step != null) {
            // 返回步骤给MainActivity
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showSelectionPoint(x: Float, y: Float) {
        // TODO: 在屏幕上显示选择点标记
    }
}
