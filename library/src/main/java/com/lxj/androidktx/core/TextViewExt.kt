package com.lxj.androidktx.core

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.TextView

/**
 * Description:
 * Create by lxj, at 2019/2/21
 */

/**
 * 给TextView的drawable设置大小，Drawable如果不传的话会尝试使用TextView自己的Drawable
 * @param width Drawable的宽度
 * @param height Drawable的高度
 */
fun TextView.sizeDrawable(width: Int, height: Int, leftDrawable: Int = 0, topDrawable: Int = 0,
                          rightDrawable: Int = 0, bottomDrawable: Int = 0): TextView {
    val rect = Rect(0, 0, width, height)
    setCompoundDrawables(
            findDrawable(leftDrawable, 0, this)?.apply { bounds = rect },
            findDrawable(topDrawable, 1, this)?.apply { bounds = rect },
            findDrawable(rightDrawable, 2, this)?.apply { bounds = rect },
            findDrawable(bottomDrawable, 3, this)?.apply { bounds = rect }
    )
    return this
}

/**
 * 优先使用传入的，如果不传则尝试使用TextView自己的
 */
private fun findDrawable(drawableRes: Int, index:Int, textView: TextView): Drawable?{
    if(drawableRes!=0)return textView.drawable(drawableRes)
    if(textView.compoundDrawables.isNotEmpty())return textView.compoundDrawables[index]
    return null
}

/**
 * 给TextView的drawable设置大小，Drawable如果不传的话会尝试使用TextView自己的Drawable
 * @param size 会同时作用于Drawable宽高
 */
fun TextView.sizeDrawable(size: Int, leftDrawable: Int = 0, topDrawable: Int = 0,
                          rightDrawable: Int = 0, bottomDrawable: Int = 0): TextView {
    sizeDrawable(size, size, leftDrawable, topDrawable, rightDrawable, bottomDrawable)
    return this
}