package com.lxj.androidktx.core

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.lxj.easyadapter.*
import java.lang.IllegalArgumentException

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
    }
}

/**
 * 横向RecyclerView
 */
class HorizontalRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    init {
        layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
    }
}

/**
 * 设置分割线
 */
fun RecyclerView.divider(color: Int = Color.parseColor("#CCCCCC"), orientation: Int = DividerItemDecoration.VERTICAL): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(1, 1)
    })
    addItemDecoration(decoration)
    return this
}

fun RecyclerView.vertical(): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    return this
}

fun RecyclerView.horizontal(): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    return this
}

inline val RecyclerView.data
    get() = (adapter as CommonAdapter<*>).datas

fun <T> RecyclerView.bindData(data: List<T>, layoutId: Int, bindFn: (holder: ViewHolder, t: T, position: Int) -> Unit): RecyclerView {
    adapter = object : CommonAdapter<T>(layoutId, data) {
        override fun convert(holder: ViewHolder, t: T, position: Int) {
            bindFn(holder, t, position)
        }
    }
    return this
}

fun <T> RecyclerView.multiTypes(data: List<T>, itemDelegates: List<ItemViewDelegate<T>>): RecyclerView {
    adapter = MultiItemTypeAdapter<T>(data).apply {
        itemDelegates.forEach { addItemViewDelegate(it) }
    }
    return this
}

fun RecyclerView.itemClick(listener: (view: View, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
    adapter?.apply {
        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object : MultiItemTypeAdapter.SimpleOnItemClickListener() {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                listener(view, holder, position)
            }
        })
    }
    return this
}
