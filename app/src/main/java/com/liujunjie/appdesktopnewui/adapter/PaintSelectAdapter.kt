package com.liujunjie.appdesktopnewui.adapter

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
        if (!item.isSelected){
            val target = holder.itemView.context.dpToPx(25f)
            holder.itemView.animate()
                .translationY(target)
                .setDuration(300)
                .start()
        }
        when (type) {
            TrackType.SMART_LINE.ordinal -> holder.bind(item = item)
            TrackType.ERASER.ordinal -> holder.bind(item = item)
            else -> holder.bind(item = item)
        }
    }
    override fun onBindViewHolder(holder: PaintItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads.contains(PaintDiffCallback.PAYLOAD_SELECTION)) {
            // 只更新选中动画
            val item = getItem(position)
            Log.d(TAG,"onBindViewHolder: $item")
            if (item.isSelected) {
                holder.animateTranslation(up = true)
            } else {
                holder.animateTranslation(up = false)
            }
        } else {
            // 完整刷新
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal
    inner class PaintItemViewHolder(private val binding: SelectPaintItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintItem) {
            binding.paint.setImageResource(item.icon)
            binding.root.setOnClickListener {
                paintSelectEvent.onItemClickOnce(item)
            }
            binding.root.setOnLongClickListener {
                paintSelectEvent.eraserSetting(item)
                true
            }
        }

        fun animateTranslation(up: Boolean) {
            Log.d(TAG,"animateTranslation $up")
            val target = if (up) -binding.root.context.dpToPx(25f) else binding.root.context.dpToPx(25f)
            binding.itemContainer.animate()
                .translationY(target)
                .setDuration(300)
                .start()
        }
    }


}


object PaintDiffCallback : DiffUtil.ItemCallback<PaintItem>() {
    const val PAYLOAD_SELECTION = "PAYLOAD_SELECTION"

    override fun areItemsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PaintItem, newItem: PaintItem): Any? {
        return if (oldItem.isSelected != newItem.isSelected) PAYLOAD_SELECTION else null
    }
}


interface PaintSelectEvent {
    fun onItemClickOnce(item: PaintItem)
    fun eraserSetting(item: PaintItem)
    fun smartLineSetting(item: PaintItem)
    fun commonLineSetting(item: PaintItem)
}