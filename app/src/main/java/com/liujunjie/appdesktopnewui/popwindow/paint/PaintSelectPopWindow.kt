package com.liujunjie.appdesktopnewui.popwindow.paint

import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.adapter.PaintOperateEvent
import com.liujunjie.appdesktopnewui.adapter.PaintSelectAdapter
import com.liujunjie.appdesktopnewui.adapter.PaintSelectEvent
import com.liujunjie.appdesktopnewui.databinding.PaintSelectPopWindowLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem

class PaintSelectPopWindow(
    val content: View,
    val cancel: () -> Unit,
    val paintSelectEvent: PaintSelectEvent,
    val paintOperateEvent: PaintOperateEvent
) : BasePopupWindow<List<PaintItem>>(content, cancel) {
    companion object {
        const val TAG = "PaintSelectPopWindow"
    }

    private val binding = PaintSelectPopWindowLayoutBinding.inflate(LayoutInflater.from(content.context))

    private val adapter = PaintSelectAdapter(paintSelectEvent)

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
                    val spacing = (totalWidth / itemCount) - view.layoutParams.width
                    val halfSpace = spacing / 2
                    outRect.left = if (position == 0) 0 else halfSpace
                    outRect.right = if (position == itemCount - 1) 0 else halfSpace
                }
            })
        }
        binding.paintEraser.setOnClickListener {
            paintOperateEvent.clearUp()
        }
        binding.paintRevoke.setOnClickListener {
            paintOperateEvent.revoke()
        }
        binding.paintRestore.setOnClickListener {
            paintOperateEvent.restore()
        }
        binding.paintExit.setOnClickListener {
            paintOperateEvent.exit()
        }
        binding.paintRetract.setOnClickListener {
            paintOperateEvent.retract()
        }
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: List<PaintItem>) {
        adapter.submitList(state)
    }


    override fun showAtLocation(anchor: Rect) {
        // 目标容器的宽高和坐标
        val anchorWidth = anchor.width()
        val anchorHeight = anchor.height()
        val anchorLeft = anchor.left
        val anchorTop = anchor.top

        // 弹窗宽高（必须先测量PopupWindow的contentView，否则width/height拿不到准确值）
        contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popupWidth = contentView.measuredWidth
        val popupHeight = contentView.measuredHeight

        // 计算弹窗显示的x坐标: 底部中间 => 目标左侧 + (目标宽度 - 弹窗宽度) / 2
        val x = anchorLeft + (anchorWidth - popupWidth) / 2

        // 计算弹窗显示的y坐标: 底部 => 目标底部 - 弹窗高度
        // 如果你想弹窗紧贴目标底部的外部，可以用 anchorTop + anchorHeight
        val y = anchorTop + anchorHeight - popupHeight

        // 使用 PopupWindow 自带的 showAtLocation(view, gravity, x, y) 方法
        // 这里传入一个根布局或者任何可用的view作为参考view，Gravity.NO_GRAVITY表示坐标已计算好
        showAtLocation(content, Gravity.NO_GRAVITY, x, y)
    }



}