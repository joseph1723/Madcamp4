package com.example.healthapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.healthapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this)
            .load(R.drawable.bg_img)
            .into(findViewById(R.id.backgroundImage))
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)


        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        val tabTitles = arrayOf("운동평가", "운동찾기", "헬키네이터")
        TabLayoutMediator(tabLayout, viewPager) { tab, position->
            tab.text = tabTitles[position]
        }.attach()
    }
}
