package com.liujunjie.appdesktopnewui.adapter

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.ColorSettingColorBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingOperationLayoutBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingShapeThickBinding
import com.liujunjie.appdesktopnewui.databinding.ColorSettingTitleBinding
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.util.ClickUtils

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
            is PaintShape -> (holder as ShapeThickViewHolder).bind(
                item,
                setSelectedShape = { paintClickEvent.setSelectedShape(it) })

            is PaintThick -> (holder as ShapeThickViewHolder).bind(
                item,
                setSelectedThick = { paintClickEvent.setSelectedThick(it) })

            is PaintColor -> {
                if (item.canDelete) {
                    (holder as ColorViewHolder).bindDel(item, delete = { paintClickEvent.delColor(it) })
                } else {
                    (holder as ColorViewHolder).bindAdd(
                        item,
                        setSelectedColor = { paintClickEvent.setSelectedColor(it) },
                        setColorToEditPop = { paintClickEvent.setColorToEditPop(it) },
                        changeColorToDel = { paintClickEvent.changeColorToDel() })
                }
            }

            is PaintOperation -> {
                if (item.isAdd) {
                    (holder as OperationViewHolder).bindAdd(item, addColor = { paintClickEvent.addColorToEditPop() })
                } else {
                    (holder as OperationViewHolder).bindDel(item, complete = { paintClickEvent.complete() })
                }
            }

            else -> throw IllegalArgumentException("Unknown view type")
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
}

/**
 * 选择类型和粗细的地方
 */
class ShapeThickViewHolder(
    val binding: ColorSettingShapeThickBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PaintShape, setSelectedShape: (item: PaintShape) -> Unit) {
        binding.shape.setImageResource(item.shapeIcon)
        binding.root.isSelected = item.isSelected
        binding.root.setOnClickListener {
            setSelectedShape(item)
        }
    }

    fun bind(item: PaintThick, setSelectedThick: (item: PaintThick) -> Unit) {
        binding.shape.setImageResource(item.thickIcon)
        binding.root.isSelected = item.isSelected
        binding.root.setOnClickListener {
            setSelectedThick(item)
        }
    }

}

class OperationViewHolder(
    val binding: ColorSettingOperationLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindAdd(item: PaintOperation, addColor: () -> Unit) {
        binding.operate.setImageResource(item.addIcon)
        binding.root.setOnClickListener {
            Log.d("OperationViewHolder", "add")
            addColor()
        }
    }

    fun bindDel(item: PaintOperation, complete: () -> Unit) {
        binding.operate.setImageResource(item.completeIcon)
        binding.root.setOnClickListener {
            complete()
        }
    }
}

class ColorViewHolder(
    val binding: ColorSettingColorBinding
) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 正常模式
     */
    fun bindAdd(
        item: PaintColor,
        setSelectedColor: (item: PaintColor) -> Unit,
        setColorToEditPop: (item: PaintColor) -> Unit,
        changeColorToDel: () -> Unit
    ) {
        binding.colorView.color = item.color
        binding.colorView.outlineColor = item.color
        binding.colorView.outlineWidth = 3F
        binding.root.isSelected = item.isSelected
        binding.remove.visibility = View.GONE
        binding.root.setOnClickListener {
            if (ClickUtils.isDoubleClick()) {
                setSelectedColor(item)
                setColorToEditPop(item)
            }else{
                setSelectedColor(item)
            }
        }
        binding.root.setOnLongClickListener {
            changeColorToDel()
            true
        }
    }

    /**
     * 删除模式
     */
    fun bindDel(item: PaintColor, delete: (item: PaintColor) -> Unit) {
        binding.colorView.color = item.color
        binding.colorView.outlineColor = item.color
        binding.colorView.outlineWidth = 3F
        binding.root.isSelected = item.isSelected
        binding.remove.visibility = View.VISIBLE
        binding.root.setOnClickListener {
            delete(item)
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
    val trackType: TrackType,
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
    val canDelete: Boolean
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
    /**
     * 选中一个笔尖形状，并且改变pop 的图标
     */
    fun setSelectedShape(item: PaintShape)

    /**
     * 选中一个粗细--UI 没有变化
     */
    fun setSelectedThick(item: PaintThick)

    /**
     * 选中一个颜色,这里单单选中，不弹出颜色选择器
     */
    fun setSelectedColor(item: PaintColor)

    /**
     * 设置当前的位置,后面的pop需要用到
     */
    fun setCurrentPosition(position: Rect)

    /**
     * 选中当前颜色并且弹出颜色选择-->1.选中当前颜色，2.弹出颜色选择器
     */
    fun setColorToEditPop(item: PaintColor)

    /**
     * 添加一个颜色，三个动作，1.添加一个color,2.选中当前颜色,3.弹出颜色选择器
     */
    fun addColorToEditPop()

    /**
     * 完成,注意当前已经选中的颜色不可删除,同时转换为不可删除
     */
    fun complete()

    /**
     * 改变颜色为可删除，这个时候点击颜色就是删除当前颜色
     */
    fun changeColorToDel()

    /**
     * 删除一个颜色
     */
    fun delColor(item: PaintColor)
}

interface ColorEditEvent {
    /**
     * 获取位置，从pop 传入
     */
    fun currentPosition()
}


object PaintEditDiff : DiffUtil.ItemCallback<PaintEditItem>() {
    override fun areItemsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return (oldItem as PaintEditItemBase).id == (newItem as PaintEditItemBase).id
    }

    override fun areContentsTheSame(oldItem: PaintEditItem, newItem: PaintEditItem): Boolean {
        return when {
            oldItem is PaintColor && newItem is PaintColor -> {
                oldItem.isSelected == newItem.isSelected && oldItem.canDelete == newItem.canDelete && oldItem.color == newItem.color
            }

            oldItem is PaintShape && newItem is PaintShape -> {
                oldItem.isSelected == newItem.isSelected
            }

            oldItem is PaintThick && newItem is PaintThick -> {
                oldItem.isSelected == newItem.isSelected
            }

            oldItem is PaintOperation && newItem is PaintOperation -> {
                oldItem.isAdd == newItem.isAdd
            }

            oldItem is PaintTitle && newItem is PaintTitle -> {
                false
            }

            else -> false
        }
    }


}