package com.liujunjie.appdesktopnewui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.config.ColorConfig
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


    private var lastPaintItem  = PaintItem(-1,false, TrackType.ERASER, 1f,ColorConfig(Color.BLACK),-1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintItemViewHolder {

        val binding = SelectPaintItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        binding.root.translationY =  binding.root.context.dpToPx(25f) // 或者 binding.root.context.dpToPx(25f)

        return PaintItemViewHolder(binding)
    }




    override fun onBindViewHolder(holder: PaintItemViewHolder, position: Int) {
        val item = getItem(position)
        val type = getItemViewType(position)
        when (type) {
            TrackType.SMART_LINE.ordinal -> holder.bind(item = item)
            TrackType.ERASER.ordinal -> holder.bind(item = item)
            else -> holder.bind(item = item)
        }
        updateTranslate(item,holder)
    }


    private fun updateTranslate(item: PaintItem, holder: PaintItemViewHolder) {

    }
    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal
    inner class PaintItemViewHolder(private val binding: SelectPaintItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintItem) {
            binding.paint.setImageResource(item.icon)
            binding.root.setOnClickListener {
                paintSelectEvent.onItemClickOnce(item)
            }
            if (item.isSelected){
                Log.d(TAG,"${item.index} 选中了")
                binding.root.animate()
                    .translationY(-binding.root.context.dpToPx(25f))
                    .setDuration(200)
                    .start()
            }else{
                Log.d(TAG,"${item.index} 没选中")
                binding.root.animate()
                    .translationY(binding.root.context.dpToPx(25f))
                    .setDuration(200)
                    .start()
            }
            binding.root.setOnLongClickListener {
                paintSelectEvent.eraserSetting(item)
                true
            }
        }
    }


}


object PaintDiffCallback : DiffUtil.ItemCallback<PaintItem>() {

    override fun areItemsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: PaintItem, newItem: PaintItem): Boolean {
        return oldItem == newItem
    }
}


interface PaintSelectEvent {
    fun onItemClickOnce(item: PaintItem)
    fun eraserSetting(item: PaintItem)
    fun smartLineSetting(item: PaintItem)
    fun commonLineSetting(item: PaintItem)
}