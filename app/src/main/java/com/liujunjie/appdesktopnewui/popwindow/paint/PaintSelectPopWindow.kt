package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.liujunjie.appdesktopnewui.databinding.PaintSelectPopWindowLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.SideBarBasePopWindow
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintUIItem


class PaintSelectPopWindow(
    val context: Context,
    val cancel:()-> Unit
) : SideBarBasePopWindow<PaintUIItem>(context,cancel) {
    private val binding = PaintSelectPopWindowLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        width = 772
        height = 110
        isTouchable = true
    }
    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: PaintUIItem) {
        Log.d("sss","sssssss")
    }



}