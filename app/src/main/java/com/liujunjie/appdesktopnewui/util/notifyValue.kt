package com.liujunjie.appdesktopnewui.util

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.util.Locale
import com.liujunjie.appdesktopnewui.R

fun <T> MutableLiveData<T>.notifyValue(value: T) {
    if (isMainThread()) {
        this.value = value
    } else {
        this.postValue(value)
    }
}

fun Float.to2Decimal(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun File.dotExtension(): String {
    return "." + this.extension.trim().lowercase()
}

fun Int.isValidColor(): Boolean {
    return this in 0..255
}

fun String.isValidColor(): Boolean {
    val hex = if (this.startsWith("#")) this.substring(1) else this
    if (hex.length !in 6..8) return false
    for (c in hex) {
        if (c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F') {
            continue
        } else {
            return false
        }
    }
    return true
}

fun Long.ms2HMS(): String {
    val s = this / 1000
    val hour = s / 3600
    val mint = s % 3600 / 60
    val sed = s % 60
    var hourStr = hour.toString()
    if (hour < 10) {
        hourStr = "0$hourStr"
    }
    var mintStr = mint.toString()
    if (mint < 10) {
        mintStr = "0$mintStr"
    }
    var sedStr = sed.toString()
    if (sed < 10) {
        sedStr = "0$sedStr"
    }
    return "$hourStr:$mintStr:$sedStr"
}

fun View.isBelongThisView(root: ViewGroup): Boolean {
    val parent = parent
    if (parent != null && parent is ViewGroup) {
        val group = parent as ViewGroup
        return if (group == root) {
            true
        } else {
            group.isBelongThisView(root)
        }
    }
    return false
}

private val EMPTY = arrayOf(IntArray(0))

fun Drawable.setTintColor(color: Int) {
    if (this is LayerDrawable) {
        val drawable = findDrawableByLayerId(R.id.tint)
        require(drawable != null) {
            "layer drawable findDrawableByLayerId(R.id.tint) is null"
        }
        drawable.setTintList(ColorStateList(EMPTY, intArrayOf(color)))
    } else {
        throw IllegalArgumentException("this is not a layerDrawable")
    }
}

class Combos {
    private var combosTimes = 0
    private val handler = Handler(Looper.getMainLooper())
    fun judgeCombosTimes(times: Int, function: () -> Unit) {
        ++combosTimes
        var clickSpan = 300L
        clickSpan += ((combosTimes - 1) * 100)
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            combosTimes = 0
        }, clickSpan)
        if (combosTimes == times) {
            function.invoke()
            combosTimes = 0
            handler.removeCallbacksAndMessages(null)
        }
    }
}