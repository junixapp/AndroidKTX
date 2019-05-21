package com.lxj.androidktx.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.lxj.androidktx.R
import com.lxj.androidktx.core.sizeDrawable

/**
 * Description: 支持在布局中对Drawable宽高进行控制的TextView
 * Create by dance, at 2019/5/21
 */
class SizedTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attributeSet, defStyleAttr) {

    var drawableWidth = 0
    var drawableHeight = 0

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SizedTextView)
        drawableWidth = ta.getDimensionPixelSize(R.styleable.SizedTextView_drawableWidth, drawableWidth)
        drawableHeight = ta.getDimensionPixelSize(R.styleable.SizedTextView_drawableHeight, drawableHeight)
        var size = ta.getDimensionPixelSize(R.styleable.SizedTextView_drawableSize, 0)
        if (size != 0) {
            drawableWidth = size
            drawableHeight = size
        }
        ta.recycle()
        if (drawableWidth != 0 && drawableHeight != 0) {
            sizeDrawable(width = drawableWidth, height = drawableHeight)
        }
    }
}
