package com.liujunjie.appdesktopnewui.enums

import com.synway.whiteboard.core.track.data.TrackType

object WhiteboardConfig {

    const val WHITEBOARD_FRAME_RATE = 60

    const val WHITEBOARD_SINK_FRAME_RATE = 60

    const val DOCUMENT_SCREENSHOT_INTERVAL = 40L

    @JvmField
    val WHITEBOARD_PAINT_TYPE = TrackType.SMART_LINE

    @JvmField
    val WHITEBOARD_ERASER_TYPE = TrackType.ERASER

    @JvmField
    val WHITEBOARD_PAINT_SIZE_LIST = arrayListOf(48F, 24F, 12F, 6F, 3F)

    @JvmField
    val MARKER_PEN_SIZE_LIST = arrayListOf(60F, 45F, 30F)

    @JvmField
    val WHITEBOARD_ERASER_SIZE_LIST = arrayListOf(300F, 200F, 100F)
}