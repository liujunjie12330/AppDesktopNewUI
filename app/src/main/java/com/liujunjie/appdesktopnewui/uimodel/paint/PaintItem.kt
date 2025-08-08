package com.liujunjie.appdesktopnewui.uimodel.paint


import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType


data class  PaintItem(
    val index: Int,
    var isSelected: Boolean = false,
    var type: TrackType,
    var thickness: Float,
    var colorConfig: ColorConfig,
    @DrawableRes val icon: Int
)