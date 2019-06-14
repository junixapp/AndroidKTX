package com.lxj.androidktx.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp2px
import com.lxj.androidktx.core.sizeDrawable

/**
 * Description: 能设置Shape的TextView
 * Create by dance, at 2019/5/21
 */
open class ShapeTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attributeSet, defStyleAttr) {

    var drawableWidth = 0
    var drawableHeight = 0
    //背景
    private var solid = 0 //填充色
    private var stroke = 0 //边框颜色
    private var strokeWidth = 0 //边框大小
    private var corner = 0 //圆角

    //是否启用水波纹
    private var enableRipple = true
    private var rippleColor = Color.parseColor("#88999999")
    //上下分割线
    private var topLineColor = 0
    private var bottomLineColor = 0
    private var lineSize = dp2px(.6f)

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeTextView)
        drawableWidth = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableWidth, drawableWidth)
        drawableHeight = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableHeight, drawableHeight)
        val size = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableSize, 0)
        if (size != 0) {
            drawableWidth = size
            drawableHeight = size
        }

        solid = ta.getColor(R.styleable.ShapeTextView_stv_solid, solid)
        stroke = ta.getColor(R.styleable.ShapeTextView_stv_stroke, stroke)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_strokeWidth, strokeWidth)
        corner = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_corner, corner)

        enableRipple = ta.getBoolean(R.styleable.ShapeTextView_stv_enableRipple, enableRipple)
        rippleColor = ta.getColor(R.styleable.ShapeTextView_stv_rippleColor, rippleColor)

        topLineColor = ta.getColor(R.styleable.ShapeTextView_stv_topLineColor, topLineColor)
        bottomLineColor = ta.getColor(R.styleable.ShapeTextView_stv_bottomLineColor, bottomLineColor)
        lineSize = ta.getColor(R.styleable.ShapeTextView_stv_lineSize, lineSize)
        ta.recycle()
        if (drawableWidth != 0 && drawableHeight != 0) {
            sizeDrawable(width = drawableWidth, height = drawableHeight)
        }
        applySelf()
    }


    private fun applySelf() {
        if (solid != 0 || stroke != 0) {
            val drawable = createDrawable(color = solid, radius = corner.toFloat(), strokeColor = stroke, strokeWidth = strokeWidth,
                    enableRipple = enableRipple, rippleColor = rippleColor)
            setBackgroundDrawable(drawable)
        } else {
            if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
                val rippleDrawable = RippleDrawable(ColorStateList.valueOf(rippleColor),
                        if (background != null) background else ColorDrawable(Color.TRANSPARENT), null)
                background = rippleDrawable
            }
        }
    }

    private val topLine = Rect(0,0,0,0)
    private val bottomLine = Rect(0,0,0,0)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        topLine.right = measuredWidth
        topLine.bottom = lineSize
        bottomLine.top = measuredHeight - lineSize
        bottomLine.right = measuredWidth
        bottomLine.bottom = measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (topLineColor != 0) {
            paint.color = topLineColor
            canvas.drawRect(topLine, paint)
        }
        if (bottomLineColor != 0) {
            paint.color = bottomLineColor
            canvas.drawRect(bottomLine, paint)
        }
    }
}
