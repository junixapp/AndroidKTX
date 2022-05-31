package com.lxj.androidktx.core

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * Description: 资源操作相关
 * Create by dance, at 2018/12/11
 */

fun Context.color(id: Int) = resources.getColor(id)

fun Context.string(id: Int, vararg arg: Any? = arrayOf()) = if(arg.isNullOrEmpty()) resources.getString(id)
                else resources.getString(id, *arg)

fun Context.stringArray(id: Int) = resources.getStringArray(id)

fun Context.drawable(id: Int) = resources.getDrawable(id)

fun Context.dimenPx(id: Int) = resources.getDimensionPixelSize(id)


fun View.color(id: Int) = context.color(id)

fun View.string(id: Int, vararg arg: Any? = arrayOf()) = context?.string(id = id, arg = arg)

fun View.stringArray(id: Int) = context.stringArray(id)

fun View.drawable(id: Int) = context.drawable(id)

fun View.dimenPx(id: Int) = context.dimenPx(id)


fun Fragment.color(id: Int) = context?.color(id)

fun Fragment.string(id: Int, vararg arg: Any? = arrayOf()) = context?.string(id = id, arg = arg)

fun Fragment.stringArray(id: Int) = context?.stringArray(id)

fun Fragment.drawable(id: Int) = context?.drawable(id)

fun Fragment.dimenPx(id: Int) = context?.dimenPx(id)


fun RecyclerView.ViewHolder.color(id: Int) = itemView.color(id)

fun RecyclerView.ViewHolder.string(id: Int, vararg arg: Any? = arrayOf()) = itemView.string(id = id, arg = arg)

fun RecyclerView.ViewHolder.stringArray(id: Int) = itemView.stringArray(id)

fun RecyclerView.ViewHolder.drawable(id: Int) = itemView.drawable(id)

fun RecyclerView.ViewHolder.dimenPx(id: Int) = itemView.dimenPx(id)