package com.smartclicker.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartclicker.app.databinding.ItemStepBinding
import com.smartclicker.app.model.ClickStep
import com.smartclicker.app.model.StepType

/**
 * 步骤列表适配器
 */
class StepListAdapter(
    private val onItemClickListener: (ClickStep) -> Unit,
    private val onDeleteListener: (ClickStep) -> Unit
) : ListAdapter<ClickStep, StepListAdapter.StepViewHolder>(StepDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding = ItemStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StepViewHolder(
        private val binding: ItemStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(step: ClickStep) {
            binding.tvStepTitle.text = step.title
            binding.tvStepType.text = getStepTypeText(step.stepType)
            binding.tvStepDelay.text = "延迟: ${step.delay}ms"
            
            // 根据步骤类型设置不同颜色
            val colorRes = when (step.stepType) {
                StepType.SIMPLE_CLICK -> com.smartclicker.app.R.color.accent_green
                StepType.IMAGE_RECOGNITION -> com.smartclicker.app.R.color.primary_color
                StepType.COLOR_RECOGNITION -> com.smartclicker.app.R.color.accent_orange
                StepType.TEXT_RECOGNITION -> com.smartclicker.app.R.color.secondary_color
                else -> com.smartclicker.app.R.color.gray_dark
            }
            binding.ivStepIcon.setImageResource(getStepTypeIcon(step.stepType))
            binding.ivStepIcon.setTint(binding.root.context.getColor(colorRes))
            
            binding.root.setOnClickListener {
                onItemClickListener(step)
            }
            
            binding.btnDelete.setOnClickListener {
                onDeleteListener(step)
            }
        }
        
        private fun getStepTypeText(type: StepType): String {
            return when (type) {
                StepType.SIMPLE_CLICK -> "简单点击"
                StepType.CONTINUOUS_CLICK -> "连续点击"
                StepType.IMAGE_RECOGNITION -> "识图点击"
                StepType.COLOR_RECOGNITION -> "颜色识别"
                StepType.TEXT_RECOGNITION -> "文字识别"
                StepType.LONG_PRESS -> "长按"
                StepType.SWIPE -> "滑动"
            }
        }
        
        private fun getStepTypeIcon(type: StepType): Int {
            return when (type) {
                StepType.SIMPLE_CLICK -> android.R.drawable.ic_menu_mylocation
                StepType.CONTINUOUS_CLICK -> android.R.drawable.ic_menu_recent_history
                StepType.IMAGE_RECOGNITION -> android.R.drawable.ic_menu_gallery
                StepType.COLOR_RECOGNITION -> android.R.drawable.ic_menu_colors
                StepType.TEXT_RECOGNITION -> android.R.drawable.ic_menu_search
                StepType.LONG_PRESS -> android.R.drawable.ic_menu_manage
                StepType.SWIPE -> android.R.drawable.ic_menu_directions
            }
        }
    }
}

class StepDiffCallback : DiffUtil.ItemCallback<ClickStep>() {
    override fun areItemsTheSame(oldItem: ClickStep, newItem: ClickStep): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClickStep, newItem: ClickStep): Boolean {
        return oldItem == newItem
    }
}
