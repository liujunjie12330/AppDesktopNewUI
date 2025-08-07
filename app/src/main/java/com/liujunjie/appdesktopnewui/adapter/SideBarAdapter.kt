package com.liujunjie.appdesktopnewui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.databinding.NewUiSidebarItemBinding

class SideBarAdapter(
    val setSelectItem: (item: SideBarItem) -> Unit
) : ListAdapter<SideBarItem, SideBarAdapter.SideBarViewHolder>(SideBarDiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideBarViewHolder {
        return SideBarViewHolder(NewUiSidebarItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    var selectedItem: SideBarItem = SideBarItem.None

    /**
     * 局部刷新
     */
    fun updateSelected(item: SideBarItem){
        if (item==selectedItem) return
        //直接刷新
        if (selectedItem== SideBarItem.None){
            selectedItem = item
            notifyItemChanged(item.index)
            return
        }
        val oldSelectedItem = selectedItem
        selectedItem = item
        notifyItemChanged(oldSelectedItem.index)
        notifyItemChanged(selectedItem.index)
    }

    override fun onBindViewHolder(holder: SideBarViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = item == selectedItem
        holder.bind(item,isSelected ,setSelectItem)
    }


    inner class SideBarViewHolder(
        val binding: NewUiSidebarItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SideBarItem, isSelected: Boolean, setSelectItem: (SideBarItem) -> Unit) {
            binding.sidebarItemImage.setImageResource(item.imageRes)
            binding.sidebarItemText.text = item.title
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                setSelectItem(item)
            }

        }
    }
}

    object SideBarDiffCallBack : DiffUtil.ItemCallback<SideBarItem>() {
        override fun areItemsTheSame(
            oldItem: SideBarItem,
            newItem: SideBarItem
        ): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(
            oldItem: SideBarItem,
            newItem: SideBarItem
        ): Boolean {
            return newItem.index == oldItem.index
        }
    }