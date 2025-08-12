package com.liujunjie.appdesktopnewui.adapter

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.SelectPaintItemLayoutBinding
import com.liujunjie.appdesktopnewui.dpToPx
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import com.liujunjie.appdesktopnewui.util.setTintColor

class PaintSelectAdapter(
    private val paintSelectEvent: PaintSelectEvent
) : ListAdapter<PaintItem, PaintSelectAdapter.PaintItemViewHolder>(PaintDiffCallback) {

    companion object {
        const val TAG = "PaintSelectAdapter"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintItemViewHolder {
        Log.d("PaintDiffCallback","create")
        val binding = SelectPaintItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaintItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PaintItemViewHolder, position: Int, payloads: List<Any?>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
          val list =  payloads[0] as List<*>
            list.forEach { payload ->
                when (payload) {
                    "SELECTION_CHANGED" -> { // 选中状态动画
                        if (getItem(position).isSelected) {
                            holder.binding.paint.animate()
                                .translationY(0F)
                                .setDuration(150)
                                .start()
                        } else {
                            holder.binding.paint.animate()
                                .translationY(holder.binding.paint.context.dpToPx(25f))
                                .setDuration(150)
                                .start()
                        }
                    }
                    "COLOR_CHANGED" -> { // 颜色更新
                        val item = getItem(position)
                        val drawable = holder.binding.paint.drawable
                        if (drawable is LayerDrawable) {
                            drawable.setTintColor(item.colorConfig.color)
                        } else if (drawable is StateListDrawable) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val stateDrawable = drawable.getStateDrawable(1)
                                if (stateDrawable is LayerDrawable) {
                                    stateDrawable.setTintColor(item.colorConfig.color)
                                }
                            }
                        }
                       holder.binding.paint.postInvalidate()
                    }
                }
            }
        }
    }


    override fun onBindViewHolder(holder: PaintItemViewHolder, position: Int) {
        val item = getItem(position)
        val type = getItemViewType(position)
        when (type) {
            TrackType.SMART_LINE.ordinal -> holder.bind(
                item = item,
                itemClick = { paintSelectEvent.onItemClickOnce(it) },
                itemLongClick ={item, archView ->  paintSelectEvent.smartLineSetting(item,archView) }
            )

            TrackType.ERASER.ordinal -> holder.bind(
                item = item,
                itemClick = { paintSelectEvent.onItemClickOnce(it) },
                itemLongClick ={item, archView ->  paintSelectEvent.smartLineSetting(item,archView) }
            )

            else -> holder.bind(
                item = item,
                itemClick = { paintSelectEvent.onItemClickOnce(it) },
                itemLongClick ={item, archView ->  paintSelectEvent.smartLineSetting(item,archView) }
            )
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).index.toLong()
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal
    class PaintItemViewHolder(val binding: SelectPaintItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.paint.translationY = binding.paint.context.dpToPx(25f)
        }

        fun bind(item: PaintItem, itemClick: (item: PaintItem) -> Unit, itemLongClick: (item: PaintItem,archView:View) -> Unit) {
            binding.paint.setImageResource(item.icon)
            val drawable = binding.paint.drawable
            binding.root.setOnClickListener {
                itemClick(item)
            }
            binding.root.setOnLongClickListener {
                itemClick(item)
                itemLongClick(item,it)
                true
            }
            //todo 双击事件
        }
    }


}


object PaintDiffCallback : DiffUtil.ItemCallback<PaintItem>() {

    override fun areItemsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return  oldItem == newItem
    }

    override fun getChangePayload(oldItem: PaintItem, newItem: PaintItem): Any? {
        if (oldItem.isSelected != newItem.isSelected) {
            return listOf("SELECTION_CHANGED")
        }
        if (oldItem.colorConfig.color != newItem.colorConfig.color) {
            return listOf("COLOR_CHANGED")
        }
        return super.getChangePayload(oldItem, newItem)
    }

}


interface PaintSelectEvent {
    fun onItemClickOnce(item: PaintItem)
    fun eraserSetting(item: PaintItem,archView: View)
    fun smartLineSetting(item: PaintItem,archView: View)
    fun commonLineSetting(item: PaintItem,archView: View)

    fun setLocation(archView: View)
}

interface PaintOperateEvent {
    fun clearUp()
    fun revoke()
    fun restore()
    fun exit()
    fun retract()
}


private val EMPTY = arrayOf(IntArray(0))