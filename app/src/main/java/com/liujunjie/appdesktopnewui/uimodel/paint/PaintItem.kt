package com.liujunjie.appdesktopnewui.uimodel.paint


import android.R.attr.data
import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.uimodel.PopWindowStates
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem


class  PaintItem(
    var isSelected: Boolean = false,
    var type: TrackType,
    var thickness: Float,
    var colorConfig: ColorConfig,
    @DrawableRes val icon: Int
)

class PaintUIItem(data: PaintItem): PopWindowStates<PaintItem>(false,data)
class PaintSelectUiItem(): PopWindowStates<SideBarItem>(false)