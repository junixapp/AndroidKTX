package com.lxj.androidktx.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

/**
 * 兼容低版本，可监听滚动
 */
class CompatNestedScrollView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : NestedScrollView(context, attributeSet, defStyleAttr){

    var onScroll: ((Int,Int,Int,Int) -> Unit)? = null
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScroll?.invoke(l, t, oldl, oldt)
    }
}