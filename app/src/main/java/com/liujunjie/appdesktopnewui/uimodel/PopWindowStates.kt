package com.liujunjie.appdesktopnewui.uimodel

/**
 * 显示侧边栏弹窗的数据类
 */
open class PopWindowStates<T> (
    var show: Boolean = false,
    var data: T ?= null
){
    companion object{
        fun <T> translate(data:T): PopWindowStates<T>{
            return PopWindowStates<T>(false,data)
        }
    }
}