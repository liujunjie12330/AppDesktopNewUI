package com.liujunjie.appdesktopnewui.viewModel.paint

import android.app.Application
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.shapes.Shape
import android.view.View
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
import com.liujunjie.appdesktopnewui.adapter.PaintEditType
import com.liujunjie.appdesktopnewui.adapter.PaintOperation
import com.liujunjie.appdesktopnewui.adapter.PaintShape
import com.liujunjie.appdesktopnewui.adapter.PaintThick
import com.liujunjie.appdesktopnewui.adapter.PaintTitle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock

class PaintViewModel(application: Application) : AndroidViewModel(application) {
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

    private var _selectedPaintRect: Rect? = null
    fun getSelectedRect(): Rect {
        val rect = _selectedPaintRect ?: Rect(0, 0, 1920, 1080)
        clearRect()
        return rect
    }

    fun setSelectedRect(rect: Rect) {
        _selectedPaintRect = rect
    }

    fun clearRect() {
        _selectedPaintRect = null
    }

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
                mergedList.mapIndexed { idx, item ->
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

    fun setShapeList() {
        viewModelScope
            .launch {
                mutex.withLock {
                    _paintShapeList.value = paintShapes
                    _paintThickList.value = paintThicks
                    _paintColorList.value = paintColors
                }
            }

    }


    fun selectPaint(item: PaintItem) {
        if (_selectedPaint.value != null && _selectedPaint.value!!.index == item.index) {
            return
        }
        _selectedPaint.value = item
        updatePaintItems(item)
    }

    fun addPaints() {
        _paintList.value = mutableListOf<PaintItem>().apply {
            addAll(paints)
        }
    }

    fun clear() {
        _paintList.value = null
    }

    private fun updatePaintItems(item: PaintItem) {
        if (_paintList.value == null) return
        val list = _paintList.value!!.map {
            it.copy(isSelected = it.index == item.index)
        }
        _paintList.value = list
    }


    /***************************数据模拟***************************************************/
    /**
     * 这里的数据后面到业务去拿
     */
    private val paints = listOf(
        PaintItem(
            0,
            false,
            TrackType.SMART_LINE,
            1.00f,
            ColorConfig(Color.BLACK),
            R.drawable.paint_brush_selected_ellipse_head
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
    private val thickTitle = PaintTitle(1, "大小")
    private val colorTitle = PaintTitle(2, "颜色")

    private val paintShapes = listOf(
        PaintShape(0, R.drawable.icon_shape_arrow, false),
        PaintShape(1, R.drawable.icon_shape_circle, false),
        PaintShape(2, R.drawable.icon_shape_double_arrow, false),
        PaintShape(3, R.drawable.icon_shape_ellipse, false),
        PaintShape(3, R.drawable.icon_shape_round_line, false),
        PaintShape(3, R.drawable.icon_shape_square, false),
        PaintShape(3, R.drawable.icon_shape_rectangle, false),
        PaintShape(3, R.drawable.icon_shape_straight_line, false)
    )

    private val paintThicks = listOf(
        PaintThick(0, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(0, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(0, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(0, 1.00f, R.drawable.icon_thick_1, false),
        PaintThick(0, 1.00f, R.drawable.icon_thick_1, false),
    )

    private val paintColors = listOf(
        PaintColor(0, Color.BLACK, false, false),
        PaintColor(0, Color.BLACK, false, false),
        PaintColor(0, Color.BLACK, false, false),
        PaintColor(0, Color.BLACK, false, false),
    )

    private val paintOperation =
        PaintOperation(1, R.drawable.icon_addition, R.drawable.icon_confirm)

}