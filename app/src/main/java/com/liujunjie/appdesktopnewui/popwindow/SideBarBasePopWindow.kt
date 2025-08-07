package com.liujunjie.appdesktopnewui.popwindow

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import com.liujunjie.appdesktopnewui.uimodel.PopWindowStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

abstract class SideBarBasePopWindow<T : PopWindowStates<*>>(
    private val context: Context,
    private val onCancel:()-> Unit
): PopupWindow(){
    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(0.toDrawable())
        setOnDismissListener { onCancel() }
    }

    abstract fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup

    abstract fun onStateChanged(state: T)


    suspend fun collectState(state: Flow<T?>,anchorView: View, gravity: Int = Gravity.CENTER) {
        try {
            state.collectLatest {
                withContext(Dispatchers.Main.immediate){
                    updateState(it,anchorView,gravity)
                }
            }
        }finally {
            dismiss()
        }
    }
    private fun updateState(state: T?, anchorView: View, gravity: Int) {
        if (state!!.show) {
            if (!isShowing) {
                contentView = createContentView(LayoutInflater.from(context), null)
                showAtLocation(anchorView, gravity, 0, 0)
            }
            onStateChanged(state)
        } else {
            if (isShowing) {
                dismiss()
            }
        }
    }
}