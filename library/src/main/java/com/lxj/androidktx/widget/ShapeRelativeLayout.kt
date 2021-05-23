package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp2px

/**
 * Description: 可以设置Shape的RelativeLayout
 * Create by dance, at 2019/5/27
 */
open class ShapeRelativeLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attributeSet, defStyleAttr) {

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
    var mGradientStartColor = 0
        set(value) {
            field = value
            applySelf()
        }
    var mGradientEndColor = 0
        set(value) {
            field = value
            applySelf()
        }
    var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT  //从左到右
        set(value) {
            field = value
            applySelf()
        }

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeRelativeLayout)
        solid = ta.getColor(R.styleable.ShapeRelativeLayout_srl_solid, solid)
        stroke = ta.getColor(R.styleable.ShapeRelativeLayout_srl_stroke, stroke)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeRelativeLayout_srl_strokeWidth, strokeWidth)
        corner = ta.getDimensionPixelSize(R.styleable.ShapeRelativeLayout_srl_corner, corner)

        topLineColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_topLineColor, topLineColor)
        bottomLineColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_bottomLineColor, bottomLineColor)
        lineSize = ta.getDimensionPixelSize(R.styleable.ShapeRelativeLayout_srl_lineSize, lineSize)
        enableRipple = ta.getBoolean(R.styleable.ShapeRelativeLayout_srl_enableRipple, enableRipple)
        rippleColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_rippleColor, rippleColor)

        mGradientStartColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_gradientStartColor, mGradientStartColor)
        mGradientEndColor = ta.getColor(R.styleable.ShapeRelativeLayout_srl_gradientEndColor, mGradientEndColor)
        val orientation = ta.getInt(R.styleable.ShapeRelativeLayout_srl_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
        mGradientOrientation = when (orientation) {
            0 -> GradientDrawable.Orientation.TOP_BOTTOM
            1 -> GradientDrawable.Orientation.TR_BL
            2 -> GradientDrawable.Orientation.RIGHT_LEFT
            3 -> GradientDrawable.Orientation.BR_TL
            4 -> GradientDrawable.Orientation.BOTTOM_TOP
            5 -> GradientDrawable.Orientation.BL_TR
            6 -> GradientDrawable.Orientation.LEFT_RIGHT
            else -> GradientDrawable.Orientation.TL_BR
        }
        ta.recycle()
        applySelf()
        if(Build.VERSION.SDK_INT >= 21) setClipToOutline(true)
    }

    fun applySelf() {
        var color : Int? = null
        if (background !=null && background is ColorDrawable && solid==Color.TRANSPARENT){
            color = ( background as ColorDrawable) .color
        }
        val drawable = createDrawable(color = color?: solid, radius = corner.toFloat(), strokeColor = stroke, strokeWidth = strokeWidth,
                enableRipple = enableRipple, rippleColor = rippleColor, gradientStartColor = mGradientStartColor,
                gradientEndColor = mGradientEndColor, gradientOrientation = mGradientOrientation)
        setBackgroundDrawable(drawable)
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