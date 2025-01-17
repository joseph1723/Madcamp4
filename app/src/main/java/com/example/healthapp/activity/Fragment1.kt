package com.example.healthapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.R

class Fragment1 : Fragment(), OnItemClickListener {

    private lateinit var tab1ItemList: List<Tab1Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        tab1ItemList = getItemList()
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Tab1ItemAdapter(getItemList(), this)

        return view
    }
    private fun getItemList(): List<Tab1Item>{
        return listOf(
            Tab1Item(R.drawable.airsquaticon, "airsquat"),
            Tab1Item(R.drawable.overheadsquaticon, "overheadsquat"),
            Tab1Item(R.drawable.frontsquaticon, "frontsquat"),
            Tab1Item(R.drawable.deadlifticon, "deadlift"),
            Tab1Item(R.drawable.shoulderpressicon, "shoulderpress"),
            Tab1Item(R.drawable.pushpressicon, "pushpress"),
            Tab1Item(R.drawable.sumodeadliftpullhighicon, "sumodeadliftpullhigh"),
            Tab1Item(R.drawable.medicineballicon, "medicineball"),
            )
    }

    override fun onItemClick(position: Int) {
        val clickedItemText = tab1ItemList[position].text
        val intent = Intent(activity, SubmitActivity::class.java)
        intent.putExtra("ITEM_TEXT", clickedItemText)
        startActivity((intent))
    }
}