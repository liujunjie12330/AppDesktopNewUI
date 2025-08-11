package com.liujunjie.appdesktopnewui.adapter

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
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem

class PaintEditAdapter(
    val paintEditEvent: PaintEditEvent
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

            PaintEditType.SHAPE.ordinal, PaintEditType.THICK.ordinal-> ShapeThickViewHolder(
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

            PaintEditType.OPERATION.ordinal-> OperationViewHolder(
                binding = ColorSettingOperationLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            PaintEditType.TITLE.ordinal -> (holder as TitleViewHolder).bind(getItem(position) as PaintTitle)
            PaintEditType.SHAPE.ordinal -> (holder as ShapeThickViewHolder).bind(getItem(position) as PaintShape)
            PaintEditType.THICK.ordinal -> (holder as ShapeThickViewHolder).bind(getItem(position) as PaintThick)
            PaintEditType.COLOR.ordinal -> (holder as ColorViewHolder).bind(getItem(position) as PaintColor) { paintEditEvent.colorSetting(getItem(position) as PaintColor) }
            PaintEditType.OPERATION.ordinal -> (holder as OperationViewHolder).bind(getItem(position) as PaintOperation)
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
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintOperation) {
            binding.operate.setImageResource(item.addIcon)
        }
    }

    class ColorViewHolder(
        val binding: ColorSettingColorBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaintColor, click: (item: PaintColor) -> Unit) {
            binding.colorView.setBackgroundColor(item.color)
            binding.colorView.setOnClickListener {
                Log.d("PaintEditAdapter", "点击了颜色：${item.index}")
                click(item)
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


interface PaintEditEvent {

    fun colorSetting(item: PaintColor)
}

object PaintEditDiff : DiffUtil.ItemCallback<PaintEditItem>() {
    override fun areItemsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return (oldItem as PaintEditItemBase).id == (newItem as PaintEditItemBase).id
    }

    override fun areContentsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return (oldItem as PaintEditItemBase).id == (newItem as PaintEditItemBase).id
    }

}