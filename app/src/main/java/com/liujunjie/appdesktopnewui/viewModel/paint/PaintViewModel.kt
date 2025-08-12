package com.liujunjie.appdesktopnewui.viewModel.paint

import android.app.Application
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.paint.Brush
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.liujunjie.appdesktopnewui.R
import com.liujunjie.appdesktopnewui.adapter.PaintColor
import com.liujunjie.appdesktopnewui.adapter.PaintEditItemBase
import com.liujunjie.appdesktopnewui.adapter.PaintOperation
import com.liujunjie.appdesktopnewui.adapter.PaintShape
import com.liujunjie.appdesktopnewui.adapter.PaintThick
import com.liujunjie.appdesktopnewui.adapter.PaintTitle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock

class PaintViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "PaintViewModel"
    }

    private val _paintList = MutableStateFlow<List<PaintItem>?>(null)
    val paintList = _paintList.asStateFlow()

    private val _selectedPaint = MutableStateFlow<PaintItem?>(null)
    val selectedPaint = _selectedPaint.asStateFlow()

    private val _paintShapeList = MutableStateFlow<List<PaintShape>?>(null)
    private val paintShapeList = _paintShapeList.asStateFlow()

    private val _paintThickList = MutableStateFlow<List<PaintThick>?>(null)
    private val paintThickList = _paintThickList.asStateFlow()

    private val _paintColorList = MutableStateFlow<List<PaintColor>?>(null)
    private val paintColorList = _paintColorList.asStateFlow()

    private val _selectedPaintRect = MutableStateFlow(Rect(0, 0, 1920, 1080))
    val selectedPaintRect = _selectedPaintRect.asStateFlow()


    private val _selectedPaintThick = MutableStateFlow<PaintThick?>(null)
    val selectedPaintThick = _selectedPaintThick.asStateFlow()

    private val _selectedPaintShape = MutableStateFlow<PaintShape?>(null)
    val selectedPaintShape = _selectedPaintShape.asStateFlow()

    /**
     * 当前选中的颜色。不跳转color edit
     */
    private val _selectedColor = MutableStateFlow<PaintColor?>(null)
    val selectedColor = _selectedColor.asStateFlow()


    private val mutex = Mutex()
    val smartLineUiData =
        combine(_paintShapeList, _paintThickList, _paintColorList) { shapes, thicks, colors ->
            mutex.withLock {
                if (shapes == null && thicks == null && colors == null) return@combine null
                val mergedList = mutableListOf<PaintEditItemBase>()
                mergedList.add(shapeTitle)
                shapes?.let { mergedList.addAll(shapes) }
                mergedList.add(thickTitle)
                thicks?.let { mergedList.addAll(thicks) }
                mergedList.add(colorTitle)
                colors?.let { mergedList.addAll(colors) }
                mergedList.add(paintOperation)
                //重排序
                return@combine mergedList.mapIndexed { idx, item ->
                    when (item) {
                        is PaintShape -> item.copy(index = idx)
                        is PaintThick -> item.copy(index = idx)
                        is PaintColor -> item.copy(index = idx)
                        is PaintTitle -> item.copy(index = idx)
                        is PaintOperation -> item.copy(index = idx)
                        else -> item
                    }
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val _selectedShape = MutableStateFlow<PaintShape?>(null)
    val selectedShape: StateFlow<PaintShape?> = _selectedShape

    /**
     * 设置当前选中的颜色，当前画笔的颜色需要改变
     */
    fun setSelectedColor(paintColor: PaintColor) {
        _selectedColor.value = paintColor.copy(isSelected = true)
        _selectedPaint.value!!.colorConfig.color = paintColor.color
        updatePaintItems(_selectedPaint.value!!)
        updatePaintColors(_selectedColor.value!!)
    }

    /**
     * 设置当前的位置
     */
    fun setRect(rect: Rect) {
        _selectedPaintRect.value = rect
    }

    /**
     * 设置当前选中的画笔
     */
    fun setSelectedPaint(paint: PaintItem) {
        _selectedPaint.value = paint.copy(isSelected = true)
        viewModelScope.launch {
            updatePaintItems(paint)
        }
    }


    /**
     * 选中粗细
     */
    fun setSelectedThick(thick: PaintThick) {
        _selectedPaintThick.value = thick.copy(isSelected = true)
        _selectedPaint.value = _selectedPaint.value!!.copy(thickness = thick.thick)
        updatePaintThicks(thick)
    }


    fun addColor() {
        if (_paintColorList.value == null) return
        val color = _paintColorList.value!!.last()
        val newColor = PaintColor(color.index + 1, color.color, isSelected = true, false)
        val updateList = _paintColorList.value!!.map {
            it.copy(isSelected = false)
        } + newColor  // 这里用 + 运算符添加元素

        _paintColorList.value = updateList
        Log.d("PaintViewModel", "addColor: ${updateList.size}")
        _selectedColor.value = newColor
    }


    private fun updatePaintThicks(thick: PaintThick) {
        if (_paintThickList.value == null) return
        _paintThickList.value = _paintThickList.value!!.map {
            if (it.index == thick.index) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
    }

    /**
     * 设置当前选中的形状
     */
    fun setSelectedShape(shape: PaintShape) {
        _selectedShape.value = shape.copy(isSelected = true)
        updatesPaintShapes(shape)
        _selectedPaint.value!!.type = shape.trackType
        Log.d("PaintViewModel", "setSelectedShape: ${_selectedPaint.value!!.getType()}")
        updatePaintItems(_selectedPaint.value!!)
    }


    fun initSmartLineUiData() {
        viewModelScope.launch {
            mutex.withLock {
                _paintShapeList.value = paintShapes
                _paintThickList.value = paintThicks
                _paintColorList.value = paintColors
            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            mutex.withLock {
                _paintShapeList.value = null
                _paintThickList.value = null
                _paintColorList.value = null
            }
        }
    }

    fun initPaints() {
        _paintList.value = paints
    }


    private fun updatePaintItems(paintItem: PaintItem) {
        if (_paintList.value == null) return
        val updateList = _paintList.value!!.map {
            if (it.index == paintItem.index) {
                it.copy(
                    index = it.index,
                    isSelected = true,
                    colorConfig = ColorConfig(color = paintItem.colorConfig.color),
                    type = paintItem.type
                )
            } else {
                it.copy(isSelected = false)
            }
        }
        Log.d("PaintViewModel", "updatePaintItems: $updateList")
        _paintList.value = updateList
    }

    private fun updatePaintColors(paintColor: PaintColor) {
        if (_paintColorList.value == null) return
        val updateList = _paintColorList.value!!.map {
            if (it.index == paintColor.index) {
                it.copy(isSelected = true)
            } else
                it.copy(isSelected = false)
        }
        _paintColorList.value = updateList
    }

    private fun updatesPaintShapes(paintShape: PaintShape) {
        if (_paintShapeList.value == null) return
        val updateList = _paintShapeList.value!!.map {
            if (it.index == paintShape.index) {
                it.copy(isSelected = true)
            } else
                it.copy(isSelected = false)
        }
        _paintShapeList.value = updateList
    }


    /***************************数据模拟***************************************************/
    /**
     * 这里的数据后面到业务去拿
     */
    private val paints = listOf(
        PaintItem(
            0,
            false,
            TrackType.ARROWHEAD,
            1.00f,
            ColorConfig(Color.BLACK),
            Brush.getResId(TrackType.ARROWHEAD)
        ),
        PaintItem(
            1,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
        ),
        PaintItem(
            2,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
        ),
        PaintItem(
            3,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
        ),
        PaintItem(
            4,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
        ),
        PaintItem(
            5,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
        )
    )

    private val shapeTitle = PaintTitle(0, "形状")
    private val thickTitle = PaintTitle(9, "大小")
    private val colorTitle = PaintTitle(15, "颜色")

    private val paintShapes = listOf(
        PaintShape(1, TrackType.ARROWHEAD, R.drawable.icon_shape_arrow, true),
        PaintShape(2, TrackType.CIRCLE, R.drawable.icon_shape_circle, false),
        PaintShape(3, TrackType.DOUBLE_ARROWHEAD, R.drawable.icon_shape_double_arrow, false),
        PaintShape(4, TrackType.ELLIPSE, R.drawable.icon_shape_ellipse, false),
        PaintShape(5, TrackType.RECTANGLE, R.drawable.icon_shape_round_line, false),
        PaintShape(6, TrackType.SQUARE, R.drawable.icon_shape_square, false),
        PaintShape(7, TrackType.RECTANGLE, R.drawable.icon_shape_rectangle, false),
        PaintShape(8, TrackType.STRAIGHT_LINE, R.drawable.icon_shape_straight_line, false)
    )

    private val paintThicks = listOf(
        PaintThick(10, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(11, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(12, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(13, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(14, 1.00f, R.drawable.icon_thick_1, false),
    )

    private val paintColors = listOf(
        PaintColor(16, Color.BLACK, isSelected = false, canDelete = false),
        PaintColor(17, Color.BLUE, isSelected = false, canDelete = false),
        PaintColor(18, Color.RED, isSelected = false, canDelete = false),
        PaintColor(19, Color.LTGRAY, isSelected = false, canDelete = false),
    )

    private val paintOperation =
        PaintOperation(1, R.drawable.icon_addition, R.drawable.icon_confirm)

}