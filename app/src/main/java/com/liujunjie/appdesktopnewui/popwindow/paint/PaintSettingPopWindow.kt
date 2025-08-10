package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.liujunjie.appdesktopnewui.adapter.PaintEditAdapter
import com.liujunjie.appdesktopnewui.adapter.PaintEditEvent
import com.liujunjie.appdesktopnewui.adapter.PaintEditItem
import com.liujunjie.appdesktopnewui.databinding.PaintColorSelectPopBinding
import com.liujunjie.appdesktopnewui.databinding.PaintSelectPopWindowLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.BasePopupWindow

class PaintSettingPopWindow(
    val context: Context,
    val onCancel:()-> Unit,
    val paintEditEvent: PaintEditEvent
): BasePopupWindow<List<PaintEditItem>>(context,onCancel){

    companion object{
        const val TAG = "PaintSettingPopWindow"
    }
    private val binding = PaintColorSelectPopBinding.inflate(LayoutInflater.from(context))
    private val adapter = PaintEditAdapter(paintEditEvent)

    init {
        width = 339
        binding.settingRecycle.apply {
            adapter = this.adapter
            layoutManager = GridLayoutManager(context,5,GridLayoutManager.HORIZONTAL,false)
        }
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: List<PaintEditItem>) {
        adapter.submitList(state)
    }

}