package com.liujunjie.appdesktopnewui.adapter

import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.SelectPaintItemLayoutBinding
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.paint.Brush

class PaintSelectAdapter(
    private val paintSelectEvent: PaintSelectEvent
) : ListAdapter<PaintItem, PaintSelectAdapter.PaintItemViewHolder>(PaintDiffCallback) {

    companion object {
        const val TAG = "PaintSelectAdapter"
    }

    private var selectedIndex = -1

    private fun updateSelected() {
        //直接进行替换
        if (selectedIndex == -1) {
            Log.d(TAG, "点击了切换动画")
        }
        //通知旧的进行更新，然后更新新的
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
            TrackType.SMART_LINE.ordinal->holder.bind(
                item = item,
                onItemClick = { paintSelectEvent.onItemClickOnce(it) },
                colorSetting = { paintSelectEvent.eraserSetting(it) }
            )
            TrackType.ERASER.ordinal->holder.bind(
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

    class PaintItemViewHolder(
        private val binding: SelectPaintItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintItem, onItemClick: (item: PaintItem) -> Unit, colorSetting: (item: PaintItem) -> Unit) {
            Log.d(TAG, "bind: ${item.type}")
            binding.paint.setImageResource(Brush.getResId(item.type))
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
    override fun areItemsTheSame(
        oldItem: PaintItem,
        newItem: PaintItem
    ): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: PaintItem,
        newItem: PaintItem
    ): Boolean {
        return oldItem == newItem
    }

}

interface PaintSelectEvent {
    fun onItemClickOnce(item: PaintItem)
    fun eraserSetting(item: PaintItem)
    fun smartLineSetting(item: PaintItem)
    fun commonLineSetting(item: PaintItem)
}