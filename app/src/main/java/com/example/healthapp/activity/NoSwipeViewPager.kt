package com.example.healthapp.activity

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {
    private var isPagingEnabled = true

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(ev)
    }

    fun setPagingEnabled(enabled: Boolean) {
        isPagingEnabled = enabled
    }
}
