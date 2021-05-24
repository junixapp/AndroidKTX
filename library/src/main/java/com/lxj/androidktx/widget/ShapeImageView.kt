package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable

/**
 * Description: 能设置Shape的TextView
 * Create by dance, at 2019/5/21
 */
open class ShapeImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attributeSet, defStyleAttr) {

    private var mSolid = 0 //填充色
    private var mStroke = 0 //边框颜色
    private var mStrokeWidth = 0 //边框大小
    private var mCorner = 0 //圆角
    private var mEnableRipple = true
    private var mRippleColor = Color.parseColor("#88999999")
    private var mGradientStartColor = 0
    private var mGradientCenterColor = 0
    private var mGradientEndColor = 0
    private var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT  //从左到右
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeImageView)
        mSolid = ta.getColor(R.styleable.ShapeImageView_siv_solid, mSolid)
        mStroke = ta.getColor(R.styleable.ShapeImageView_siv_stroke, mStroke)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeImageView_siv_strokeWidth, mStrokeWidth)
        mCorner = ta.getDimensionPixelSize(R.styleable.ShapeImageView_siv_corner, mCorner)

        mEnableRipple = ta.getBoolean(R.styleable.ShapeImageView_siv_enableRipple, mEnableRipple)
        mRippleColor = ta.getColor(R.styleable.ShapeImageView_siv_rippleColor, mRippleColor)

        mGradientStartColor = ta.getColor(R.styleable.ShapeImageView_siv_gradientStartColor, mGradientStartColor)
        mGradientCenterColor = ta.getColor(R.styleable.ShapeImageView_siv_gradientCenterColor, mGradientCenterColor)
        mGradientEndColor = ta.getColor(R.styleable.ShapeImageView_siv_gradientEndColor, mGradientEndColor)
        val orientation = ta.getInt(R.styleable.ShapeImageView_siv_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
        mGradientOrientation = when(orientation){
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
        if(Build.VERSION.SDK_INT >= 21){
            setClipToOutline(true)
        }
    }

    fun applySelf() {
        var color : Int? = null
        if (background !=null && background is ColorDrawable && mSolid==Color.TRANSPARENT){
            color = ( background as ColorDrawable) .color
        }
        val drawable = createDrawable(color = color ?: mSolid, radius = mCorner.toFloat(), strokeColor = mStroke, strokeWidth = mStrokeWidth,
            enableRipple = mEnableRipple, rippleColor = mRippleColor, gradientStartColor = mGradientStartColor,
            gradientEndColor = mGradientEndColor, gradientOrientation = mGradientOrientation)
        setBackgroundDrawable(drawable)
        if(Build.VERSION.SDK_INT >= 21){
            outlineProvider = object : ViewOutlineProvider(){
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, mCorner.toFloat())
                }
            }
            strokePaint.strokeWidth = mStrokeWidth.toFloat()
            strokePaint.color = mStroke
            strokePaint.style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas!!.drawRoundRect(0f,0f, measuredWidth.toFloat(), measuredHeight.toFloat(), mCorner.toFloat(),mCorner.toFloat(), strokePaint)
        }
    }

    fun setup(solid: Int = mSolid, stroke: Int = mStroke, strokeWidth: Int = mStrokeWidth,
              corner: Int = mCorner, enableRipple: Boolean = mEnableRipple, rippleColor: Int = mRippleColor,
              gradientStartColor: Int = mGradientStartColor, gradientEndColor: Int = mGradientEndColor,
              gradientOrientation: GradientDrawable.Orientation = mGradientOrientation){
        mSolid = solid
        mStroke = stroke
        mStrokeWidth = strokeWidth
        mCorner = corner
        mEnableRipple = enableRipple
        mRippleColor = rippleColor
        mGradientStartColor = gradientStartColor
        mGradientEndColor = gradientEndColor
        mGradientOrientation = gradientOrientation
        applySelf()
    }
}
