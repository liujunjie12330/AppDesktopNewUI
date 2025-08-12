package com.liujunjie.appdesktopnewui.popwindow.paint

import android.graphics.Rect
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.adapter.*
import com.liujunjie.appdesktopnewui.databinding.PaintColorSelectPopBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow
import com.liujunjie.appdesktopnewui.util.DisplayUtil

class PaintSettingPopWindow(
    val content: View,
    val onCancel: () -> Unit,
    val paintClickEvent: PaintClickEvent
) : BasePopupWindow<List<PaintEditItem>>(content, onCancel) {

    companion object {
        const val TAG = "PaintSettingPopWindow"
    }

    private val binding = PaintColorSelectPopBinding.inflate(LayoutInflater.from(content.context))
    private val paintEditAdapter = PaintEditAdapter(
        paintClickEvent = this.paintClickEvent,
        colorEditEvent = object : ColorEditEvent {
            override fun currentPosition() {
                val location = IntArray(2)
                binding.root.getLocationOnScreen(location)
                val left = location[0]
                val top = location[1]
                val right = left + binding.root.width
                val bottom = top + binding.root.height
                val rect = Rect(left, top, right, bottom)
                paintClickEvent.setCurrentPosition(rect)
            }
        }
    )

    init {
        width = 339
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.settingRecycle.apply {
            adapter = paintEditAdapter
            layoutManager = GridLayoutManager(context, 20, GridLayoutManager.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter!!.getItemViewType(position)) {
                            PaintEditType.TITLE.ordinal -> 20
                            PaintEditType.SHAPE.ordinal -> 5
                            PaintEditType.COLOR.ordinal, PaintEditType.THICK.ordinal, PaintEditType.OPERATION.ordinal -> 4
                            else -> 0
                        }
                    }
                }
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 30
                }
            })
        }
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }


    override fun onStateChanged(state: List<PaintEditItem>) {
        paintEditAdapter.submitList(state)
    }

    private var isFirstShow = true

    override fun showAtLocation(anchor: Rect) {
        val centerX = anchor.left + anchor.width() / 2
        val popupWidth = DisplayUtil.dp2px(content.context, 339)
        if (isFirstShow) {
            val offscreenX = -10000
            val offscreenY = -10000
            showAtLocation(content, Gravity.NO_GRAVITY, offscreenX, offscreenY)
            contentView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val popupHeight = contentView.height
                    val x = centerX - popupWidth / 2
                    val y = anchor.top - popupHeight
                    update(x, y-15, popupWidth, popupHeight)
                    isFirstShow = false
                }
            })

        } else {
            // 非第一次直接显示
            contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val popupHeight = contentView.measuredHeight
            val x = centerX - popupWidth / 2
            val y = anchor.top - popupHeight
            showAtLocation(content, Gravity.NO_GRAVITY, x, y-15)
        }
    }


}