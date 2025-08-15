package com.liujunjie.appdesktopnewui.popwindow

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

open class PopupState<T>(
    var data: StateFlow<T?>,
    val anchor: StateFlow<Rect>
)

abstract class BasePopupWindow<T>(
    private val content: View,
    private val onCancel: () -> Unit
) : PopupWindow(content.context) {

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    abstract fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup
    abstract fun onStateChanged(state: T)


    abstract fun showAtLocation(anchor: Rect)

    suspend fun collectState(state: PopupState<T>) {
        try {
            state.data.collectLatest { dataValue ->
                if (dataValue == null) {
                    withContext(Dispatchers.Main.immediate) {
                        if (isShowing) dismiss()
                    }
                    return@collectLatest
                }
                withContext(Dispatchers.Main.immediate) {
                    updateState(state, dataValue)
                }
            }
        } finally {
            withContext(Dispatchers.Main.immediate) {
                if (isShowing) dismiss()
            }
        }

    }

    private fun updateState(state: PopupState<T>, dataValue: T) {

        if (!isShowing) {
            contentView = createContentView(LayoutInflater.from(content.context), null)
            showAtLocation(state.anchor.value)
        }
        onStateChanged(dataValue)
    }
}
