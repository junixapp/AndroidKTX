package com.lxj.androidktx.core

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.*
import android.view.View
import com.lxj.easyadapter.*

/**
 * Description: RecyclerView扩展
 * Create by lxj, at 2018/12/25
 */

/**
 * 设置分割线
 */
fun RecyclerView.divider(color: Int = Color.parseColor("#DEDEDE"), size: Int = 1): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    addItemDecoration(decoration)
    return this
}


fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.data
    get() = (adapter as CommonAdapter<*>).datas

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


fun <T> RecyclerView.bindData(data: List<T>, layoutId: Int, bindFn: (holder: ViewHolder, t: T, position: Int) -> Unit): RecyclerView {
    adapter = object : CommonAdapter<T>(layoutId, data) {
        override fun bind(holder: ViewHolder, t: T, position: Int) {
            bindFn(holder, t, position)
        }
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addHeader(headerView: View): RecyclerView {
    adapter?.apply {
        (this as CommonAdapter<*>).addHeaderView(headerView)
    }
    return this
}

/**
 * 必须在bindData之后调用，并且需要hasHeaderOrFooter为true才起作用
 */
fun RecyclerView.addFooter(footerView: View): RecyclerView {
    adapter?.apply {
        (this as CommonAdapter<*>).addFootView(footerView)
    }
    return this
}

fun <T> RecyclerView.multiTypes(data: List<T>, itemDelegates: List<ItemViewDelegate<T>>): RecyclerView {
    adapter = MultiItemTypeAdapter<T>(data).apply {
        itemDelegates.forEach { addItemViewDelegate(it) }
    }
    return this
}

fun <T> RecyclerView.itemClick(listener: (data: List<T>, holder: RecyclerView.ViewHolder, position: Int) -> Unit): RecyclerView {
    adapter?.apply {
        (adapter as MultiItemTypeAdapter<*>).setOnItemClickListener(object : MultiItemTypeAdapter.SimpleOnItemClickListener() {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                listener(data as List<T>, holder, position)
            }
        })
    }
    return this
}
