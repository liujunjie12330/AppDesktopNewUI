package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.liujunjie.appdesktopnewui.adapter.PaintSelectAdapter
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

    private val adapter = PaintSelectAdapter(
        selectPaint = { item -> Log.d("PaintSelectPopWindow", "selectPaint: ${item.type}") },
        eraserSetting = { item -> Log.d("PaintSelectPopWindow", "selectPaint: ${item.type}") },
        smartLineSetting ={ item -> Log.d("PaintSelectPopWindow", "selectPaint: ${item.type}") },
        commonLineSetting = { item -> Log.d("PaintSelectPopWindow", "selectPaint: ${item.type}") }
    )

    init {
        width = 772
        height = 110
        isTouchable = true
        adapter.apply {
            binding.paintList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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