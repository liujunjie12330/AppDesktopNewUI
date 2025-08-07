package com.liujunjie.appdesktopnewui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.liujunjie.appdesktopnewui.adapter.SideBarAdapter
import com.liujunjie.appdesktopnewui.viewModel.SideBarViewModel
import com.liujunjie.appdesktopnewui.adapter.SideBarEvent
import com.liujunjie.appdesktopnewui.databinding.ActivityMainUiLayoutBinding
import com.liujunjie.appdesktopnewui.popwindow.paint.PaintSelectPopWindow
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.viewModel.paint.PaintViewModel
import kotlinx.coroutines.flow.collectLatest


class MainUIActivity : AppCompatActivity() {
   companion object{
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
        PaintSelectPopWindow(this) {
            Log.d("sssssssssss", "取消弹窗")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.sidebarRecycleview.adapter = sideBarAdapter
        binding.sidebarRecycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lifecycleScope.launchWhenStarted {
            sideBarViewModel.sideBarItems.collectLatest {
                sideBarAdapter.submitList(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            paintPopWindow.collectState(paintViewModel.paintList, binding.root)
        }
    }
}