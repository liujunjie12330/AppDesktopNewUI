package com.liujunjie.appdesktopnewui.popwindow.paint

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.liujunjie.appdesktopnewui.adapter.PaintEditAdapter
import com.liujunjie.appdesktopnewui.adapter.PaintEditEvent
import com.liujunjie.appdesktopnewui.adapter.PaintEditItem
import com.liujunjie.appdesktopnewui.databinding.PaintColorSelectPopBinding
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
    private val paintEditAdapter = PaintEditAdapter(paintEditEvent)

    init {
        width = 339
        height = 549
        binding.settingRecycle.apply {
            adapter = paintEditAdapter
            layoutManager = GridLayoutManager(context,5,GridLayoutManager.VERTICAL,false)
        }
    }

    override fun createContentView(inflater: LayoutInflater, parent: ViewGroup?): ViewGroup {
        return binding.root
    }

    override fun onStateChanged(state: List<PaintEditItem>) {
        Log.d(TAG, "onStateChanged: $state")
        paintEditAdapter.submitList(state)
    }

}