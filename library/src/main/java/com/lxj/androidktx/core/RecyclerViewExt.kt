package com.lxj.androidktx.core

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Description: RecyclerView扩展
 * Create by lxj, at 2018/12/25
 */

/**
 * 垂直RecyclerView
 */
class VerticalRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    init {
        layoutManager = LinearLayoutManager(getContext())
        divider()
    }
}

/**
 * 横向RecyclerView
 */
class HorizontalRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    init {
        layoutManager = LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false)
    }
}

/**
 * 设置分割线
 */
fun RecyclerView.divider(dividerColor: Int = Color.parseColor("#CCCCCC"), orientation: Int =  DividerItemDecoration.VERTICAL): RecyclerView{
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(dividerColor)
        shape = GradientDrawable.RECTANGLE
        setSize(1, 1)
    })
    addItemDecoration(decoration)
    return this
}