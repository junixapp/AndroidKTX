package com.lxj.androidktx.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Description: View相关
 * Create by lxj, at 2018/12/4
 */

/**
 * 设置View的高度
 */
fun View.setHeight(height: Int): View {
    val params = layoutParams
    params.height = height
    layoutParams = params
    return this
}

fun View.setLimitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

fun View.setWidth(width: Int): View {
    val params = layoutParams
    params.width = width
    layoutParams = params
    return this
}

fun View.setLimitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

/**
 * 获取View的截图, 支持获取整个RecyclerView列表的长截图
 */
fun View.toBitmap(): Bitmap = when (this) {
    is RecyclerView -> {
        this.scrollToPosition(0)
        this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        val bmp = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        //draw default bg, otherwise will be black
        if (background != null) {
            background.setBounds(0, 0, width, measuredHeight)
            background.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        this.draw(canvas)
        //恢复高度
        this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
        bmp //return
    }
    else -> {
        val screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(screenshot)
        if (background != null) {
            background.setBounds(0, 0, width, measuredHeight)
            background.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        draw(canvas)// 将 view 画到画布上
        screenshot //return
    }
}
