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
    private val _paintList = MutableStateFlow<MutableList<PaintItem>?>(null)
    val paintList = _paintList.asStateFlow()

    private val paints = listOf(
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK),R.drawable.paint_brush_selected_ellipse_head),
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), Brush.getResId(TrackType.STRAIGHT_LINE)),
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), Brush.getResId(TrackType.STRAIGHT_LINE)),
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), Brush.getResId(TrackType.STRAIGHT_LINE)),
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), Brush.getResId(TrackType.STRAIGHT_LINE)),
        PaintItem(false, TrackType.SMART_LINE, 1.00f, ColorConfig(Color.BLACK), Brush.getResId(TrackType.STRAIGHT_LINE))
    )

    fun addPaints() {
        _paintList.value = mutableListOf<PaintItem>().apply {
            addAll(paints)
        }
    }

    fun clear() {
        _paintList.value = null
    }


}