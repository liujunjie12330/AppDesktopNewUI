package cn.synway.module_sdirector_ui.new_ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SideBarViewModel(application: Application): AndroidViewModel(application) {
    private val _selectedItem = MutableStateFlow<SideBarItem>(SideBarItem.None)
    val selectedItem = _selectedItem.asStateFlow()

    fun setSelectedItem(item: SideBarItem){
        if (selectedItem!=item){
            _selectedItem.value = item
        }
    }
}