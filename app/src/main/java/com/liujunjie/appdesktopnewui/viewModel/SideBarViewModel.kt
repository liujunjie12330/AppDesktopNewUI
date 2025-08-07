package com.liujunjie.appdesktopnewui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.uimodel.SideBarItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SideBarViewModel(application: Application) : AndroidViewModel(application) {
    private val _selectedItem = MutableStateFlow(SideBarItems.None)
    val selectedItem = _selectedItem.asStateFlow()

    private val _sideBarItems = MutableStateFlow(SideBarItems.allItems())
    val sideBarItems = _sideBarItems.asStateFlow()

    fun setSelectedItem(item: SideBarItem) {
        updateSideBarItems(item)
        if (selectedItem != item) {
            _selectedItem.value = item
        }
    }

    private fun updateSideBarItems(item: SideBarItem) {
        if (_selectedItem.value.index == item.index) {
            return // 点击的是当前选中的，不更新
        }
        val updatedList = _sideBarItems.value.map {
            it.copy(isSelect = it.index == item.index)
        }
        _sideBarItems.value = updatedList
        _selectedItem.value = item
    }

}