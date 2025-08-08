package com.liujunjie.appdesktopnewui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.liujunjie.appdesktopnewui.adapter.PaintSelectEvent
import com.liujunjie.appdesktopnewui.adapter.SideBarAdapter
import com.liujunjie.appdesktopnewui.viewModel.SideBarViewModel
import com.liujunjie.appdesktopnewui.adapter.SideBarEvent
import com.liujunjie.appdesktopnewui.databinding.ActivityMainUiLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.paint.PaintSelectPopWindow
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.uimodel.SideBarItems
import com.liujunjie.appdesktopnewui.uimodel.paint.PaintItem
import com.liujunjie.appdesktopnewui.viewModel.paint.PaintViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainUIActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainUIActivity"
    }

    private val binding by lazy {
        ActivityMainUiLayoutBinding.inflate(layoutInflater)
    }

    private val sideBarViewModel by lazy {
        ViewModelProvider(this)[SideBarViewModel::class.java]

    }
    private val sideBarAdapter by lazy {
        SideBarAdapter(object : SideBarEvent {
            override fun onSideBarItemClick(item: SideBarItem) {
                sideBarViewModel.setSelectedItem(item)
            }
        })
    }

    private val paintViewModel by lazy {
        ViewModelProvider(this)[PaintViewModel::class.java]
    }

    private val paintPopWindow by lazy {
        PaintSelectPopWindow(
            context = this,
            cancel = {},
            paintSelectEvent = object : PaintSelectEvent {
                override fun onItemClickOnce(item: PaintItem) {
                    paintViewModel.selectPaint(item)
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.sidebarRecycleview.adapter = sideBarAdapter
        binding.sidebarRecycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            sideBarViewModel.sideBarItems.collectLatest {
                sideBarAdapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            sideBarViewModel.selectedItem.collectLatest {
                if (it == SideBarItems.DrawPaintItem) paintViewModel.addPaints()
                else paintViewModel.clear()

            }
        }

        lifecycleScope.launch {
            paintPopWindow.collectState(paintViewModel.paintList, binding.root)
        }
    }


}


fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}