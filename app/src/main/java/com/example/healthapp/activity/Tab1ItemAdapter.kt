package com.example.healthapp.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R

public interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class Tab1ItemAdapter(private val Tab1ItemList: List<Tab1Item>, private val listener: OnItemClickListener) :

    RecyclerView.Adapter<Tab1ItemAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
            val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
            val itemText: TextView = itemView.findViewById(R.id.itemText)
            init{
                itemView.setOnClickListener(this)
            }
            override fun onClick(v: View?){
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }
            }
        }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab1_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = Tab1ItemList[position]
        holder.itemImage.setImageResource(currentItem.imageResId)
        holder.itemText.text = currentItem.text
    }
    override fun getItemCount() = Tab1ItemList.size
}