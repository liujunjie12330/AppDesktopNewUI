package com.liujunjie.appdesktopnewui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.liujunjie.appdesktopnewui.adapter.SideBarAdapter
import cn.synway.module_sdirector_ui.new_ui.viewModel.SideBarViewModel
import com.liujunjie.appdesktopnewui.databinding.ActivityMainUiLayoutBinding
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem


class MainUIActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainUiLayoutBinding.inflate(layoutInflater)
    }

    private val sideBarViewModel by lazy {
        ViewModelProvider(this)[SideBarViewModel::class.java]

    }
    private val sideBarAdapter by lazy {
        SideBarAdapter() {
            sideBarViewModel.setSelectedItem(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.sidebarRecycleview.adapter = sideBarAdapter
        binding.sidebarRecycleview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        sideBarAdapter.submitList(SideBarItem.allItems())
        lifecycleScope.launchWhenStarted {
            sideBarViewModel.selectedItem.collect {
                sideBarAdapter.updateSelected(it)
            }
        }
    }
}