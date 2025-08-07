package com.liujunjie.appdesktopnewui.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.uimodel.SideBarItem
import com.liujunjie.appdesktopnewui.databinding.NewUiSidebarItemBinding
import com.liujunjie.appdesktopnewui.uimodel.SideBarItems

class SideBarAdapter(
    private val sideBarEvent: SideBarEvent
) : ListAdapter<SideBarItem, SideBarAdapter.SideBarViewHolder>(SideBarDiffCallBack) {

    companion object {
        const val TAG = "SideBarAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideBarViewHolder {
        return SideBarViewHolder(NewUiSidebarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SideBarViewHolder, position: Int) {
        holder.bind(getItem(position), sideBarEvent)
    }

    inner class SideBarViewHolder(
        val binding: NewUiSidebarItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SideBarItem, sideBarEvent: SideBarEvent) {
            binding.sidebarItemImage.setImageResource(item.imageRes)
            binding.sidebarItemText.text = item.title
            binding.root.isSelected = item.isSelect
            binding.root.setOnClickListener {
                sideBarEvent.onSideBarItemClick(item)
            }
        }
    }
}

object SideBarDiffCallBack : DiffUtil.ItemCallback<SideBarItem>() {
    override fun areItemsTheSame(oldItem: SideBarItem, newItem: SideBarItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: SideBarItem, newItem: SideBarItem): Boolean {
        return oldItem.isSelect == newItem.isSelect
    }
}


interface SideBarEvent {
    fun onSideBarItemClick(item: SideBarItem)
}