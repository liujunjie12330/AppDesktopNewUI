package com.liujunjie.appdesktopnewui.adapter

import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.ColorSettingColorBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingOperationLayoutBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingShapeThickBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingTitleBinding
import com.liujunjie.appdesktopnewui.R
class PaintEditAdapter(
    val paintClickEvent: PaintClickEvent,
    val colorEditEvent: ColorEditEvent
) : ListAdapter<PaintEditItem, RecyclerView.ViewHolder>(PaintEditDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PaintEditType.TITLE.ordinal -> TitleViewHolder(
                ColorSettingTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            PaintEditType.SHAPE.ordinal, PaintEditType.THICK.ordinal -> ShapeThickViewHolder(
                ColorSettingShapeThickBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            PaintEditType.COLOR.ordinal -> ColorViewHolder(
                ColorSettingColorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            PaintEditType.OPERATION.ordinal -> OperationViewHolder(
                binding = ColorSettingOperationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is PaintTitle -> (holder as TitleViewHolder).bind(item)
            is PaintThick -> (holder as ShapeThickViewHolder).bind(item)
            is PaintColor -> (holder as ColorViewHolder).bind(
                item,
                click = { paintClickEvent.colorClick(it) },
                setPosition = { colorEditEvent.currentPosition() },
                colorSetting = { paintClickEvent.colorSetting(it) })

            is PaintOperation -> (holder as OperationViewHolder).bind(item)
            is PaintShape -> (holder as ShapeThickViewHolder).bind(item)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).getType()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return (getItem(position) as PaintEditItemBase).id.toLong()
    }

    class TitleViewHolder(
        val binding: ColorSettingTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintTitle) {
            binding.paintTitleText.text = item.title
        }
    }

    class ShapeThickViewHolder(
        val binding: ColorSettingShapeThickBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintShape) {
            binding.shape.setImageResource(item.shapeIcon)
        }

        fun bind(item: PaintThick) {
            binding.shape.setImageResource(item.thickIcon)
        }

        fun bind(item: PaintOperation) {
            binding.shape.setImageResource(item.addIcon)
        }
    }

    class OperationViewHolder(
        val binding: ColorSettingOperationLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintOperation) {
            binding.operate.setImageResource(item.addIcon)
        }
    }

    class ColorViewHolder(
        val binding: ColorSettingColorBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: PaintColor,
            click: (item: PaintColor) -> Unit,
            setPosition: () -> Unit,
            colorSetting: (item: PaintColor) -> Unit
        ) {
            Log.d("ColorViewHolder", "bind: ${item.color}")
            val context = binding.root.context
            binding.colorView.outlineWidth = 3F
            if (Color.luminance(item.color) < 0.9F) {
                binding.colorView.strokeColor = item.color
                binding.colorView.outlineColor = item.color
                binding.remove.imageTintList = context.resources.getColorStateList(R.color.white, null)
            } else {
                binding.colorView.strokeColor = context.resources.getColor(R.color.light_gray, null)
                binding.colorView.outlineColor = context.resources.getColor(R.color.light_gray, null)
                binding.remove.imageTintList = context.resources.getColorStateList(R.color.light_gray, null)
            }
            binding.colorView.background = null
            binding.colorView.isSelected = item.isSelected
            binding.colorView.setOnClickListener {
                Log.d("PaintEditAdapter", "点击了颜色：${item.index}")
                click(item)
                setPosition()
            }

            binding.colorView.setOnLongClickListener {
                Log.d("PaintEditAdapter", "点击了颜色：${item.index}")
                click(item)
                setPosition()
                colorSetting(item)
                true
            }
        }

    }
}

sealed interface PaintEditItem {
    fun getType(): Int
}

abstract class PaintEditItemBase(val id: Int) : PaintEditItem

data class PaintTitle(
    val index: Int = 0,
    val title: String
) : PaintEditItemBase(index) {
    override fun getType(): Int {
        return PaintEditType.TITLE.ordinal
    }
}

data class PaintThick(
    val index: Int = 0,
    val thick: Float = 0.0f,
    @DrawableRes val thickIcon: Int,
    val isSelected: Boolean = false
) : PaintEditItemBase(index) {
    override fun getType(): Int {
        return PaintEditType.THICK.ordinal
    }
}

data class PaintShape(
    val index: Int = 0,
    @DrawableRes val shapeIcon: Int,
    val isSelected: Boolean = false
) : PaintEditItemBase(index) {
    override fun getType(): Int {
        return PaintEditType.SHAPE.ordinal
    }
}

data class PaintColor(
    val index: Int,
    val color: Int,
    val isSelected: Boolean,
    val isEdit: Boolean
) : PaintEditItemBase(index) {
    override fun getType(): Int {
        return PaintEditType.COLOR.ordinal
    }
}

data class PaintOperation(
    val index: Int,
    @DrawableRes val addIcon: Int,
    @DrawableRes val completeIcon: Int,
    val isAdd: Boolean = true
) : PaintEditItemBase(index) {
    override fun getType(): Int {
        return PaintEditType.OPERATION.ordinal
    }
}

enum class PaintEditType {
    TITLE, THICK, SHAPE, COLOR, OPERATION
}


interface PaintClickEvent {
    fun colorSetting(item: PaintColor)
    fun setCurrentPosition(position: Rect)

    fun colorClick(item: PaintColor)
}

interface ColorEditEvent {
    fun currentPosition()
}

object PaintEditDiff : DiffUtil.ItemCallback<PaintEditItem>() {
    override fun areItemsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return (oldItem as PaintEditItemBase).id == (newItem as PaintEditItemBase).id
    }

    override fun areContentsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return when(oldItem) {
            is PaintColor -> {
                oldItem.isSelected == (newItem as PaintColor).isSelected && oldItem.isEdit == newItem.isEdit && oldItem.color == newItem.color
            }
            else -> {
                false
            }
        }
    }

}