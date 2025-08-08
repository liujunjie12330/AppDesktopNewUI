package com.liujunjie.appdesktopnewui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CircleColorView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var color = Color.WHITE
    var strokeColor = Color.WHITE
    var outlineColor = Color.WHITE
    var outlineWidth = 2F

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2F
        val cy = height / 2F
        val radius = min(cx, cy)
        canvas.drawCircle(cx, cy, radius - outlineWidth * 2F, paint.apply {
            color = this@CircleColorView.color
            style = Paint.Style.FILL
        })
        canvas.drawCircle(cx, cy, radius - outlineWidth * 2F, paint.apply {
            color = this@CircleColorView.strokeColor
            strokeWidth = 1F
            style = Paint.Style.STROKE
        })
        if (isSelected) {
            canvas.drawCircle(cx, cy, radius - outlineWidth / 2, paint.apply {
                color = this@CircleColorView.outlineColor
                strokeWidth = outlineWidth
                style = Paint.Style.STROKE
            })
        }
    }

}