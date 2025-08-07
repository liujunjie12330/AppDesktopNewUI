package com.liujunjie.appdesktopnewui.uimodel.paint

import com.liujunjie.appdesktopnewui.enums.TrackType
import com.liujunjie.appdesktopnewui.enums.WhiteboardConfig.WHITEBOARD_ERASER_TYPE
import com.liujunjie.appdesktopnewui.enums.WhiteboardConfig.WHITEBOARD_PAINT_TYPE
import com.liujunjie.appdesktopnewui.R

object Brush {

    const val MAX_PAINT_SIZE = 5

    fun getResId(trackType: TrackType): Int {
        return when (trackType) {
            WHITEBOARD_ERASER_TYPE -> R.drawable.paint_0_b
            WHITEBOARD_PAINT_TYPE -> R.drawable.paint_1_b
            TrackType.LASER_LINE -> R.drawable.paint_2_b
            TrackType.BRUSH_LINE -> R.drawable.paint_3_b
            TrackType.MARK_LINE -> R.drawable.paint_4_b
            TrackType.ROUND_LINE -> R.drawable.paint_shape_1_b
            TrackType.STRAIGHT_LINE -> R.drawable.paint_shape_2_b
            TrackType.ARROWHEAD -> R.drawable.paint_shape_3_b
            TrackType.DOUBLE_ARROWHEAD -> R.drawable.paint_shape_4_b
            TrackType.RECTANGLE_SOLID,
            TrackType.RECTANGLE -> R.drawable.paint_shape_5_b
            TrackType.SQUARE_SOLID,
            TrackType.SQUARE -> R.drawable.paint_shape_6_b
            TrackType.ELLIPSE_SOLID,
            TrackType.ELLIPSE -> R.drawable.paint_shape_7_b
            TrackType.CIRCLE_SOLID,
            TrackType.CIRCLE -> R.drawable.paint_shape_8_b
            else -> R.drawable.paint_1_b
        }
    }

    fun getResId2(trackType: TrackType): Int {
        return when (trackType) {
            WHITEBOARD_PAINT_TYPE -> R.drawable.paint_1_b
            TrackType.LASER_LINE -> R.drawable.paint_2_b
            TrackType.BRUSH_LINE -> R.drawable.paint_3_b
            TrackType.MARK_LINE -> R.drawable.paint_4_b
            TrackType.ROUND_LINE -> R.drawable.paint_shape_1_b
            TrackType.STRAIGHT_LINE -> R.drawable.paint_shape_2_b
            TrackType.ARROWHEAD -> R.drawable.paint_shape_3_b
            TrackType.DOUBLE_ARROWHEAD -> R.drawable.paint_shape_4_b
            TrackType.RECTANGLE_SOLID,
            TrackType.RECTANGLE -> R.drawable.paint_shape_5_b
            TrackType.SQUARE_SOLID,
            TrackType.SQUARE -> R.drawable.paint_shape_6_b
            TrackType.ELLIPSE_SOLID,
            TrackType.ELLIPSE -> R.drawable.paint_shape_7_b
            TrackType.CIRCLE_SOLID,
            TrackType.CIRCLE -> R.drawable.paint_shape_8_b
            else -> R.drawable.paint_1_b
        }
    }

    fun getVerticalResId(trackType: TrackType): Int {
        return when (trackType) {
            WHITEBOARD_PAINT_TYPE -> R.drawable.paint_1_vertical
            TrackType.LASER_LINE -> R.drawable.paint_2_vertical
            TrackType.BRUSH_LINE -> R.drawable.paint_3_vertical
            TrackType.MARK_LINE -> R.drawable.paint_4_vertical
            else -> R.drawable.paint_1_vertical
        }
    }

    fun getShapeResId(trackType: TrackType): Int {
        return when (trackType) {
            TrackType.ROUND_LINE -> R.drawable.icon_shape_round_line
            TrackType.STRAIGHT_LINE -> R.drawable.icon_shape_straight_line
            TrackType.ARROWHEAD -> R.drawable.icon_shape_arrow
            TrackType.DOUBLE_ARROWHEAD -> R.drawable.icon_shape_double_arrow
            TrackType.CIRCLE -> R.drawable.icon_shape_circle
            TrackType.RECTANGLE -> R.drawable.icon_shape_rectangle
            TrackType.ELLIPSE -> R.drawable.icon_shape_ellipse
            TrackType.SQUARE -> R.drawable.icon_shape_square
            else -> R.drawable.ic_shape_rounded_straight_line
        }
    }
/*
    fun getCircleDrawable(trackType: TrackType): PaintCircleIconDrawable {
        val drawable = ContextCompat.getDrawable(AppContext.getContext(), getResId2(trackType))!!
        return PaintCircleIconDrawable(drawable)
    }
*/
}