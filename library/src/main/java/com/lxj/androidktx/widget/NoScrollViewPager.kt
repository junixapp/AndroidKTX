package com.lxj.androidktx.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewPager(context, attributeSet) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}