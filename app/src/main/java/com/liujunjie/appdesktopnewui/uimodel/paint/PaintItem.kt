package com.liujunjie.appdesktopnewui.uimodel.paint


import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType




data class PaintItem(
    val index: Int,
    val isSelected: Boolean = false,
    var type: TrackType,
    val thickness: Float,
    var colorConfig: ColorConfig,
    @DrawableRes val icon: Int
){
    fun getType(): Int {
       return when(type) {
            TrackType.LASER_LINE -> 0
            TrackType.BRUSH_LINE -> 1
            TrackType.MARK_LINE -> 2
            TrackType.ERASER -> 3
            TrackType.SMART_LINE -> 4
            else -> 5 // smart line
        }
    }
}