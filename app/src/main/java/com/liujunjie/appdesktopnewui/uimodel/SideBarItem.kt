package com.liujunjie.appdesktopnewui.uimodel
import androidx.annotation.DrawableRes
import com.liujunjie.appdesktopnewui.R


open class SideBarItem(
    val index:Int,
    val title: String,
    @DrawableRes val imageRes:Int
){
    object SceneItem: SideBarItem(0,"场景", R.drawable.image_sidebar_scene)
    object LayoutItem: SideBarItem(1,"布局",R.drawable.image_sidebar_layout)
    object DrawPaintItem: SideBarItem(2,"画笔",R.drawable.image_sidebar_draw_paint)
    object BackgroundItem: SideBarItem(3,"背景",R.drawable.image_sidebar_background)
    object VideoSourceItem: SideBarItem(4,"摄像头",R.drawable.image_sidebar_videosource)
    object FileItem: SideBarItem(5,"文件",R.drawable.image_sidebar_file)
    object ProjectionItem: SideBarItem(6,"投屏",R.drawable.image_sidebar_projection)
    object TeleprompterItem: SideBarItem(7,"提词器",R.drawable.image_sidebar_teleprompter)
    object CharactersItem: SideBarItem(8,"文字",R.drawable.image_sidebar_cheracter)
    object LayerItem: SideBarItem(9,"图层",R.drawable.image_sidebar_layer)
    object AppPreviewItem: SideBarItem(10,"应用",R.drawable.image_sidebar_app)
    object ClearScreenItem: SideBarItem(11,"清屏",R.drawable.image_sidebar_clean_sceen)
    object None: SideBarItem(-1,"",0)

    companion object{
        fun allItems():List<SideBarItem> {
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
}


