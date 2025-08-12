package com.liujunjie.appdesktopnewui.popwindow.paint

import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liujunjie.appdesktopnewui.config.ColorConfig
import com.liujunjie.appdesktopnewui.databinding.PaintColorPickerLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.util.DisplayUtil
import com.liujunjie.appdesktopnewui.util.preMeasure

interface ColorSettingEvent {
    fun setColor(color: Int)
}

class ColorSettingPopWindow(
    val content: View,
    val onCancel: () -> Unit,
    val colorSettingEvent: ColorSettingEvent
) : BasePopupWindow<ColorConfig>(content, onCancel) {
    private val binding = PaintColorPickerLayoutBinding.inflate(LayoutInflater.from(content.context))

    companion object{
        const val TAG = "ColorSettingPopWindow"
    }

    init {
        binding.root.preMeasure()
        val rootWidth = 400
        val rootHeight = 400
        height = rootHeight
        width = rootWidth
        isFocusable = true
        isOutsideTouchable = true
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: ColorConfig) {
        Log.d(TAG, "onStateChanged: $state")
    }

    override fun showAtLocation(anchor: Rect) {
        val x = anchor.right+20
        val y = anchor.top
        showAtLocation(content, Gravity.NO_GRAVITY, x, y)
    }

}