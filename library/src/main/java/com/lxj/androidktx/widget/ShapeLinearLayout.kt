package com.lxj.androidktx.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp2px

/**
 * Description: 可以设置Shape的LinearLayout
 * Create by dance, at 2019/5/27
 */
class ShapeLinearLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr) {

    //背景
    var solid = 0 //填充色
        set(value) {
            field = value
            applySelf()
        }
    var stroke = 0 //边框颜色
        set(value) {
            field = value
            applySelf()
        }
    var strokeWidth = 0 //边框大小
        set(value) {
            field = value
            applySelf()
        }
    var corner = 0 //圆角
        set(value) {
            field = value
            applySelf()
        }

    //上下分割线
    var topLineColor = 0
        set(value) {
            field = value
            applySelf()
        }
    var bottomLineColor = 0
        set(value) {
            field = value
            applySelf()
        }
    var lineSize = dp2px(.6f)
        set(value) {
            field = value
            applySelf()
        }
    //是否启用水波纹
    var enableRipple = true
        set(value) {
            field = value
            applySelf()
        }
    var rippleColor = Color.parseColor("#88999999")
        set(value) {
            field = value
            applySelf()
        }

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeLinearLayout)
        solid = ta.getColor(R.styleable.ShapeLinearLayout_sll_solid, solid)
        stroke = ta.getColor(R.styleable.ShapeLinearLayout_sll_stroke, stroke)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_sll_strokeWidth, strokeWidth)
        corner = ta.getDimensionPixelSize(R.styleable.ShapeLinearLayout_sll_corner, corner)

        topLineColor = ta.getColor(R.styleable.ShapeLinearLayout_sll_topLineColor, topLineColor)
        bottomLineColor = ta.getColor(R.styleable.ShapeLinearLayout_sll_bottomLineColor, bottomLineColor)
        lineSize = ta.getColor(R.styleable.ShapeLinearLayout_sll_lineSize, lineSize)
        enableRipple = ta.getBoolean(R.styleable.ShapeLinearLayout_sll_enableRipple, enableRipple)
        rippleColor = ta.getColor(R.styleable.ShapeLinearLayout_sll_rippleColor, rippleColor)

        ta.recycle()
        applySelf()
    }

    fun applySelf() {
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
    val paint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (topLineColor != 0) {
            paint.color = topLineColor
            canvas.drawRect(Rect(0, 0, measuredWidth, lineSize), paint)
        }
        if (bottomLineColor != 0) {
            paint.color = bottomLineColor
            canvas.drawRect(Rect(0, measuredHeight - lineSize, measuredWidth, measuredHeight), paint)
        }
    }
}