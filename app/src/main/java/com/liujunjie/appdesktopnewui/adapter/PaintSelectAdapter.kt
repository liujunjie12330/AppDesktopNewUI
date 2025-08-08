package com.liujunjie.appdesktopnewui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.SelectPaintItemLayoutBinding
import com.liujunjie.appdesktopnewui.dpToPx
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem

class PaintSelectAdapter(
    private val paintSelectEvent: PaintSelectEvent
) : ListAdapter<PaintItem, PaintSelectAdapter.PaintItemViewHolder>(PaintDiffCallback) {

    companion object {
        const val TAG = "PaintSelectAdapter"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintItemViewHolder {
        return PaintItemViewHolder(
            SelectPaintItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PaintItemViewHolder, position: Int) {
        val item = getItem(position)
        val type = getItemViewType(position)
        when (type) {
            TrackType.SMART_LINE.ordinal -> holder.bind(
                item = item,
                onItemClick = { paintSelectEvent.onItemClickOnce(it) },
                colorSetting = { paintSelectEvent.eraserSetting(it) }
            )

            TrackType.ERASER.ordinal -> holder.bind(
                item = item,
                onItemClick = { paintSelectEvent.onItemClickOnce(it) },
                colorSetting = { paintSelectEvent.eraserSetting(it) }
            )

            else -> holder.bind(
                item = item,
                onItemClick = { paintSelectEvent.onItemClickOnce(it) },
                colorSetting = { paintSelectEvent.commonLineSetting(it) }
            )
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal
    inner class PaintItemViewHolder(
        private val binding: SelectPaintItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintItem, onItemClick: (item: PaintItem) -> Unit, colorSetting: (item: PaintItem) -> Unit) {
            Log.d(TAG, "bind: ${item.type}")
            binding.paint.setImageResource(item.icon)
            val targetTranslation = if (item.isSelected) 0f else binding.root.context.dpToPx(20f)
            binding.itemContainer.animate()
                .translationY(targetTranslation)
                .setDuration(200)
                .start()
            binding.root.setOnClickListener {
                onItemClick(item)
            }
            binding.root.setOnLongClickListener {
                colorSetting(item)
                true // 表示已经处理了事件
            }
            //双击事件
        }
    }

}


object PaintDiffCallback : DiffUtil.ItemCallback<PaintItem>() {
    override fun areItemsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem.isSelected == newItem.isSelected && oldItem.thickness== newItem.thickness && oldItem.colorConfig == newItem.colorConfig
    }

}

interface PaintSelectEvent {
    fun onItemClickOnce(item: PaintItem)
    fun eraserSetting(item: PaintItem)
    fun smartLineSetting(item: PaintItem)
    fun commonLineSetting(item: PaintItem)
}