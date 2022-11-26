package com.lxj.androidktx.widget.pager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

@Deprecated(message = "推荐使用ViewPager2")
class NoScrollViewPager @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewPager(context, attributeSet) {
    private var enableScroll = false
    fun enableScroll(scroll: Boolean){
        enableScroll = scroll
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(enableScroll) return super.onInterceptTouchEvent(ev)
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if(enableScroll) return super.onTouchEvent(ev)
        return false
    }
}