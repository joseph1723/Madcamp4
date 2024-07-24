package com.example.healthapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.healthapp.R
class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)
        val submitButton: Button = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val intent = Intent(activity, Tab2AnalyzeActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}