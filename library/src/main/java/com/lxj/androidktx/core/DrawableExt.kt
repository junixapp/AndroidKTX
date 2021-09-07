package com.lxj.androidktx.core

import android.graphics.Rect
import android.graphics.drawable.Drawable

fun Drawable.size(width: Int, height: Int): Drawable{
    bounds = Rect(0, 0, width, height)
    return this
}

fun Drawable.size(size: Int): Drawable{
    bounds = Rect(0, 0, size, size)
    return this
}