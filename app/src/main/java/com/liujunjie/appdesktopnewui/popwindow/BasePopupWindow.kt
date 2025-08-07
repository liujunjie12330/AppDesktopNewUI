package com.liujunjie.appdesktopnewui.popwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext


abstract class BasePopupWindow<T>(
    private val context: Context,
    private val onCancel: () -> Unit
) : PopupWindow(context) {

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(0))
        setOnDismissListener { onCancel() }
    }

    abstract fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup

    // 创建内容视图的抽象方法
    abstract fun onStateChanged(state: T)


    suspend fun collectState(state: Flow<T?>,anchorView: View, gravity: Int = Gravity.CENTER) {
        try {
            state.collectLatest {
                // 挂起函数，用于收集状态并更新弹窗内容
                withContext(Dispatchers.Main.immediate){
                    updateState(it,anchorView,gravity)
                    // 使用collectLatest函数，每次状态更新时都会执行lambda表达式
                }
                // 在主线程中更新弹窗内容
            }
        }finally {
            dismiss()
        }
    }
    // 最后关闭弹窗

    private fun updateState(state: T?, anchorView: View, gravity: Int) {
        if (state != null) {
            if (!isShowing) {
                // 根据状态更新弹窗内容的方法
                contentView = createContentView(LayoutInflater.from(context), null)
                showAtLocation(anchorView, gravity, 0, 0)
                // 如果状态不为空，且弹窗未显示，则创建内容视图并显示弹窗
            }
            onStateChanged(state)
        } else {
            if (isShowing) {
                dismiss()
            }
        }
    }
}
