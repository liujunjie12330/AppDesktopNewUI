package cn.synway.module_sdirector_ui.new_ui.dialog

import android.app.Dialog
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

abstract class BaseDialog<T>(context: Context, private val onCancel: () -> Unit) : Dialog(context) {

    suspend fun collectState(state: Flow<T?>) {
        try {
            state.collectLatest {
                withContext(Dispatchers.Main.immediate) {
                    updateState(it)
                }
            }
        } finally {
            dismiss()
        }
    }

    private fun updateState(state: T?) {
        if (state != null) {
            if (!isShowing) {
                show()
            }
            onStateChanged(state)
        } else {
            if (isShowing) {
                dismiss()
            }
        }
    }

    protected abstract fun onStateChanged(state: T)

    final override fun cancel() {
        onCancel()
    }

}