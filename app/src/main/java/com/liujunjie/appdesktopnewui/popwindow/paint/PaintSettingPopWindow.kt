package com.liujunjie.appdesktopnewui.popwindow.paint

import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.liujunjie.appdesktopnewui.adapter.PaintEditAdapter
import com.liujunjie.appdesktopnewui.adapter.PaintEditEvent
import com.liujunjie.appdesktopnewui.adapter.PaintEditItem
import com.liujunjie.appdesktopnewui.adapter.PaintEditType
import com.liujunjie.appdesktopnewui.databinding.PaintColorSelectPopBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.popwindow.PopupState

class PaintSettingPopWindow(
    val content: View,
    val onCancel:()-> Unit,
    val paintEditEvent: PaintEditEvent
): BasePopupWindow<List<PaintEditItem>>(content,onCancel){

    companion object{
        const val TAG = "PaintSettingPopWindow"
    }
    private val binding = PaintColorSelectPopBinding.inflate(LayoutInflater.from(content.context))
    private val paintEditAdapter = PaintEditAdapter(paintEditEvent)

    init {
        width = 339
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.settingRecycle.apply {
            adapter = paintEditAdapter
            layoutManager = GridLayoutManager(context,20,GridLayoutManager.VERTICAL,false).apply {
                spanSizeLookup  =  object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when(adapter!!.getItemViewType(position)){
                            PaintEditType.TITLE.ordinal->20
                            PaintEditType.SHAPE.ordinal-> 5
                            PaintEditType.COLOR.ordinal,PaintEditType.THICK.ordinal,PaintEditType.OPERATION.ordinal-> 4
                            else -> 0
                        }
                    }
                }
            }
        }
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }


    override fun onStateChanged(state: List<PaintEditItem>) {
        Log.d(TAG, "onStateChanged: $state")
        paintEditAdapter.submitList(state)
    }

    override fun showAtLocation(anchor: Rect) {
        // 测量弹窗宽高
        contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupWidth = contentView.measuredWidth

        // 获取屏幕宽度
        val screenWidth = contentView.resources.displayMetrics.widthPixels

        val x = screenWidth - popupWidth
        val y = 0

        showAtLocation(contentView, Gravity.NO_GRAVITY, x, y)
    }




}