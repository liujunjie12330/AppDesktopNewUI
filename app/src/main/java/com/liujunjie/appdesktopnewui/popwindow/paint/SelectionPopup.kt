package com.liujunjie.appdesktopnewui.popwindow.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liujunjie.appdesktopnewui.databinding.RecyclerViewLayoutBinding
import com.liujunjie.appdesktopnewui.R
class SelectionPopup(
    context: Context,
    private val anchorView: View,
    selections:ArrayList<Selection>,
    private val selectedListener:OnItemSelection,
    usedName: String
): PopupWindow(context){

    private val binding = RecyclerViewLayoutBinding.inflate(LayoutInflater.from(context))
    init {
        contentView = binding.root
        width = anchorView.width
        height = 54
        isOutsideTouchable = true
        isTouchable = true
        isFocusable = false
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.recycler.setPadding(0,0,0,0)
        binding.recycler.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        val listener = object : OnItemSelection {
            override fun onSelected(selection: Selection) {
                selectedListener.onSelected(selection)
                dismiss()
            }
        }
        val adapter = SelectionAdapter(selections,usedName,listener)
        binding.recycler.adapter = adapter
    }

    fun show(){
        showAsDropDown(anchorView)
    }

    inner class SelectionAdapter(
        private val selections:ArrayList<Selection>,
        private var usedName:String,
        private val onItemSelection: OnItemSelection): RecyclerView.Adapter<SelectionAdapter.ViewHolder>() {

        inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
            val key:TextView = view.findViewById(R.id.selection_key)
            val state:ImageView = view.findViewById(R.id.selection_state)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.selection_popup_layout,parent,false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return selections.size
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val context = holder.itemView.context
            val selection = selections[position]

            holder.state.isVisible = selection.name == usedName
            val state = usedName == selection.name
            holder.key.text = selection.name
            holder.key.setTextColor(if (state) Color.WHITE else Color.BLACK)
            val colorRes = if (state) R.color.deep_blue else R.color.white
            holder.itemView.setBackgroundColor(context.resources.getColor(colorRes,null))
            holder.itemView.setOnClickListener{
                onItemSelection.onSelected(selection)
                usedName = selection.name
                notifyDataSetChanged()
            }
        }
    }

    data class Selection(val name:String, val value:Any)

    interface OnItemSelection{
        fun onSelected(selection:Selection)
    }
}