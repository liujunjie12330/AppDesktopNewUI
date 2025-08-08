package com.liujunjie.appdesktopnewui.viewModel.paint

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.paint.Brush
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.liujunjie.appdesktopnewui.R

class PaintViewModel(application: Application) : AndroidViewModel(application) {
    private val _paintList = MutableStateFlow<List<PaintItem>?>(null)
    val paintList = _paintList.asStateFlow()

    private val _selectedPaint = MutableStateFlow<PaintItem?>(null)
    val selectedPaint = _selectedPaint.asStateFlow()
    /**
     * 这里的数据后面到业务去拿
     */
    private val paints = listOf(
        PaintItem(0,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK),R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(1,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(2,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(3,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(4,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(5,false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), R.drawable.paint_brush_selected_ellipse_head)
    )


    fun selectPaint(item: PaintItem){
       if (_selectedPaint.value!=null && _selectedPaint.value!!.index == item.index){
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

    private fun updatePaintItems(item: PaintItem){
        if (_paintList.value==null) return
        val list = _paintList.value!!.map {
            it.copy(isSelected = it.index == item.index)
        }
        _paintList.value = list
    }

}