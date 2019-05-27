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
import android.widget.RelativeLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable

/**
 * Description: 可以设置Shape的RelativeLayout
 * Create by dance, at 2019/5/27
 */
class ShapeRelativeLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attributeSet, defStyleAttr) {

    //背景
    private var solid = 0 //填充色
    private var stroke = 0 //边框颜色
    private var strokeWidth = 0 //边框大小
    private var corner = 0 //圆角

    //上下分割线
    private var topLineColor = 0
    private var bottomLineColor = 0

    //是否启用水波纹
    private var enableRipple = true
    private var rippleColor = Color.parseColor("#88999999")

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeRelativeLayout)
        solid = ta.getColor(R.styleable.ShapeRelativeLayout_srl_solid, solid)
        stroke = ta.getColor(R.styleable.ShapeRelativeLayout_srl_stroke, stroke)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeRelativeLayout_srl_strokeWidth, strokeWidth)
        corner = ta.getDimensionPixelSize(R.styleable.ShapeRelativeLayout_srl_corner, corner)

        topLineColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_topLineColor, topLineColor)
        bottomLineColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_bottomLineColor, bottomLineColor)
        enableRipple = ta.getBoolean(R.styleable.ShapeRelativeLayout_srl_enableRipple, enableRipple)
        rippleColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_rippleColor, rippleColor)

        ta.recycle()
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
    private val paint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (topLineColor != 0) {
            paint.color = topLineColor
            canvas.drawRect(Rect(0, 0, measuredWidth, 1), paint)
        }
        if (bottomLineColor != 0) {
            paint.color = bottomLineColor
            canvas.drawRect(Rect(0, measuredHeight - 1, measuredWidth, measuredHeight), paint)
        }
    }
}