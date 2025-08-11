package com.liujunjie.appdesktopnewui.util

import android.content.Context
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import kotlin.math.abs


fun View.preMeasure(): IntArray {
    measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    return intArrayOf(measuredWidth, measuredHeight)
}

fun getWindowSize(context: Context): IntArray {
    val displayMetrics = DisplayMetrics()
    context.getSystemService(WindowManager::class.java).defaultDisplay.getRealMetrics(displayMetrics)
    return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun updatePositionInParent(target: View, rectF: RectF, parent: View) {
    val parentRectF = RectF(0F,0F,parent.width.toFloat(),parent.height.toFloat())
    if (target.measuredWidth == 0 &&
        target.measuredHeight == 0){
        target.preMeasure()
    }
    val rootWidth = target.measuredWidth
    val rootHeight = target.measuredHeight
    val halfWidth = rootWidth / 2
    var x = rectF.centerX() - halfWidth
    var y = rectF.bottom
    x = x.coerceIn(0F, abs(parentRectF.width() - rootWidth))
    y = y.coerceIn(0F, abs(parentRectF.height() - rootHeight))
    target.translationX = x
    target.translationY = y
}

fun updatePositionInWindow(target: View, rectF: RectF, w:Int, h:Int) {
    val context = target.context
    val windowSize = getWindowSize(context)
    val windowRectF = RectF(0F,0F,windowSize[0].toFloat(),windowSize[1].toFloat())
    val rootWidth = if (target.width == 0) w else target.width
    val rootHeight = if (target.height == 0) h else target.height
    val halfWidth = rootWidth / 2
    var x = rectF.centerX() - halfWidth
    var y = rectF.bottom
    x = x.coerceIn(0F, abs(windowRectF.width() - rootWidth))
    y = y.coerceIn(0F, abs(windowRectF.height() - rootHeight))
    target.translationX = x
    target.translationY = y
}