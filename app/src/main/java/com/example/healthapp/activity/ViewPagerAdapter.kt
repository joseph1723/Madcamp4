package com.example.healthapp.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        Fragment1(),
        Fragment2(),
        Fragment3()
    )

    override fun getItemCount(): Int{
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment{
        return fragments[position]
    }

}
