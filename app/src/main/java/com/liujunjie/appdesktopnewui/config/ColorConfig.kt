package com.liujunjie.appdesktopnewui.config

import android.graphics.Color

//使用地址比较对象是否相等
class ColorConfig(
    var color: Int = Color.BLACK,
){
    override fun toString(): String {
        return "ColorConfig(color=$color)"
    }
}