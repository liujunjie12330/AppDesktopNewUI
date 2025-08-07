package com.liujunjie.appdesktopnewui.uimodel.paint


import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.PopWindowStates


class  PaintItem(
    var isSelected: Boolean = false,
    var type: TrackType,
    var thickness: Float,
    var colorConfig: ColorConfig,
    @DrawableRes val icon: Int
)

class PaintUIItem(data: PaintItem): PopWindowStates<PaintItem>(false,data)