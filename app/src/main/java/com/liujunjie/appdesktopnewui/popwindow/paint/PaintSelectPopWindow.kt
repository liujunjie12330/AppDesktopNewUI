package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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