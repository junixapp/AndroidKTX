package com.lxj.androidktx.core

import android.graphics.Rect
import android.widget.TextView

/**
 * Description:
 * Create by lxj, at 2019/2/21
 */

fun TextView.sizeDrawable(width: Int, height: Int, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val rect = Rect(0, 0, width, height)
    setCompoundDrawables(
            if(left==0) null else drawable(left).apply { bounds = rect },
            if(top==0) null else drawable(top).apply { bounds = rect },
            if(right==0) null else drawable(right).apply { bounds = rect },
            if(bottom==0) null else drawable(bottom).apply { bounds = rect }
    )
}

fun TextView.sizeDrawable(size: Int, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    sizeDrawable(size, size, left, top, right, bottom)
}