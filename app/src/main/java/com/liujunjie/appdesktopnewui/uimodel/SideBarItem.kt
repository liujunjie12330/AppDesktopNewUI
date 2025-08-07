package com.liujunjie.appdesktopnewui.uimodel

import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.R

/**
 * 侧边栏ui data
 */
data class SideBarItem(
    val index: Int,
    val title: String,
    @DrawableRes val imageRes: Int,
    var isSelect: Boolean = false
)
object SideBarItems {
    val SceneItem = SideBarItem(0, "场景", R.drawable.image_sidebar_scene)
    val LayoutItem = SideBarItem(1, "布局", R.drawable.image_sidebar_layout)
    val DrawPaintItem = SideBarItem(2, "画笔", R.drawable.image_sidebar_draw_paint)
    val BackgroundItem = SideBarItem(3, "背景", R.drawable.image_sidebar_background)
    val VideoSourceItem = SideBarItem(4, "摄像头", R.drawable.image_sidebar_videosource)
    val FileItem = SideBarItem(5, "文件", R.drawable.image_sidebar_file)
    val ProjectionItem = SideBarItem(6, "投屏", R.drawable.image_sidebar_projection)
    val TeleprompterItem = SideBarItem(7, "提词器", R.drawable.image_sidebar_teleprompter)
    val CharactersItem = SideBarItem(8, "文字", R.drawable.image_sidebar_cheracter)
    val LayerItem = SideBarItem(9, "图层", R.drawable.image_sidebar_layer)
    val AppPreviewItem = SideBarItem(10, "应用", R.drawable.image_sidebar_app)
    val ClearScreenItem = SideBarItem(11, "清屏", R.drawable.image_sidebar_clean_sceen)

    val None = SideBarItem(-1, "", 0)

    fun allItems(): List<SideBarItem> {
        return listOf(
            SceneItem,
            LayoutItem,
            DrawPaintItem,
            BackgroundItem,
            VideoSourceItem,
            FileItem,
            ProjectionItem,
            TeleprompterItem,
            CharactersItem,
            LayerItem,
            AppPreviewItem,
            ClearScreenItem
        ).sortedBy { it.index }
    }
}
