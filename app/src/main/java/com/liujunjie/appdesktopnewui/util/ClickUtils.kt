package com.liujunjie.appdesktopnewui.util

object ClickUtils {
    private var lastClickTime = 0L
    private const val DOUBLE_CLICK_INTERVAL = 300L // 双击间隔（毫秒）

    fun isDoubleClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        val isDouble = (currentTime - lastClickTime) <= DOUBLE_CLICK_INTERVAL
        lastClickTime = currentTime
        return isDouble
    }
}
