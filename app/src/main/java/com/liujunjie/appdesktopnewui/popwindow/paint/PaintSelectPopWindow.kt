package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.adapter.PaintSelectAdapter
import com.liujunjie.appdesktopnewui.adapter.PaintSelectEvent
import com.liujunjie.appdesktopnewui.databinding.PaintSelectPopWindowLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem


class PaintSelectPopWindow(
    val context: Context,
    val cancel:()-> Unit
) : BasePopupWindow<MutableList<PaintItem>>(context,cancel) {
    companion object{
        const val TAG = "PaintSelectPopWindow"
    }
    private val binding = PaintSelectPopWindowLayoutBinding.inflate(LayoutInflater.from(context))

    private val adapter = PaintSelectAdapter( object : PaintSelectEvent {
        override fun onItemClickOnce(item: PaintItem) {

        }

        override fun eraserSetting(item: PaintItem) {
            TODO("Not yet implemented")
        }

        override fun smartLineSetting(item: PaintItem) {
            TODO("Not yet implemented")
        }

        override fun commonLineSetting(item: PaintItem) {
            TODO("Not yet implemented")
        }


    }
    )

    init {
        width = 772
        height = 110
        isTouchable = true
        binding.paintList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@PaintSelectPopWindow.adapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val totalWidth = parent.width
                    val itemCount = parent.adapter?.itemCount ?: 1
                    val position = parent.getChildAdapterPosition(view)

                    if (itemCount == 0) return

                    // 计算每个 item 之间应该留多少间距
                    val spacing = (totalWidth / itemCount) - view.layoutParams.width

                    val halfSpace = spacing / 2

                    outRect.left = if (position == 0) 0 else halfSpace
                    outRect.right = if (position == itemCount - 1) 0 else halfSpace
                }
            })
        }
    }
    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: MutableList<PaintItem>) {
        Log.d(TAG, "onStateChanged: $state")
        adapter.submitList(state)
    }



}