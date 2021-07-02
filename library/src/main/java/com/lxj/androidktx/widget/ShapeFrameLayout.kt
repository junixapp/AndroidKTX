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
import android.widget.FrameLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp2px

/**
 * Description: 可以设置Shape的FrameLayout
 * Create by dance, at 2019/5/27
 */
open class ShapeFrameLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attributeSet, defStyleAttr) {

    private var mSolid = 0 //填充色
    private var mStroke = 0 //边框颜色
    private var mStrokeWidth = 0 //边框大小
    private var mCorner = 0 //圆角
    private var mTopLeftCorner = 0
    private var mTopRightCorner = 0
    private var mBottomLeftCorner = 0
    private var mBottomRightCorner = 0
    private var mTopLineColor = 0
    private var mBottomLineColor = 0
    private var mLineSize = context.dp2px(1f)
    private var mEnableRipple = true
    private var mRippleColor = Color.parseColor("#88999999")
    private var mGradientStartColor = 0
    private var mGradientCenterColor = 0
    private var mGradientEndColor = 0
    private var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT  //从左到右

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeFrameLayout)
        mSolid = ta.getColor(R.styleable.ShapeFrameLayout_sfl_solid, mSolid)
        mStroke = ta.getColor(R.styleable.ShapeFrameLayout_sfl_stroke, mStroke)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_strokeWidth, mStrokeWidth)
        mCorner = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_corner, mCorner)
        mTopLeftCorner = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_topLeftCorner, mTopLeftCorner)
        mTopRightCorner = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_topRightCorner, mTopRightCorner)
        mBottomRightCorner = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_bottomRightCorner, mBottomRightCorner)
        mBottomLeftCorner = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_bottomLeftCorner, mBottomLeftCorner)

        mTopLineColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_topLineColor, mTopLineColor)
        mBottomLineColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_bottomLineColor, mBottomLineColor)
        mLineSize = ta.getDimensionPixelSize(R.styleable.ShapeFrameLayout_sfl_lineSize, mLineSize)
        mEnableRipple = ta.getBoolean(R.styleable.ShapeFrameLayout_sfl_enableRipple, mEnableRipple)
        mRippleColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_rippleColor, mRippleColor)

        mGradientStartColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_gradientStartColor, mGradientStartColor)
        mGradientCenterColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_gradientCenterColor, mGradientCenterColor)
        mGradientEndColor = ta.getColor(R.styleable.ShapeFrameLayout_sfl_gradientEndColor, mGradientEndColor)
        val orientation = ta.getInt(R.styleable.ShapeFrameLayout_sfl_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
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
        if (background !=null && background is ColorDrawable && mSolid==Color.TRANSPARENT){
            color = ( background as ColorDrawable) .color
        }
        var cornerArr: Array<Float>? = null
        if(mTopLeftCorner>0 || mTopRightCorner>0 || mBottomLeftCorner>0 || mBottomRightCorner>0){
            cornerArr = arrayOf(mTopLeftCorner.toFloat(), mTopRightCorner.toFloat(), mBottomRightCorner.toFloat(),
                mBottomLeftCorner.toFloat())
        }
        val drawable = createDrawable(color = color ?: mSolid, radius = mCorner.toFloat(), cornerRadiusArray = cornerArr, strokeColor = mStroke, strokeWidth = mStrokeWidth,
                enableRipple = mEnableRipple, rippleColor = mRippleColor, gradientStartColor = mGradientStartColor,
                gradientEndColor = mGradientEndColor, gradientCenterColor = mGradientCenterColor, gradientOrientation = mGradientOrientation)
        setBackgroundDrawable(drawable)
    }

    val paint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mTopLineColor != 0) {
            paint.color = mTopLineColor
            canvas.drawRect(Rect(0, 0, measuredWidth, mLineSize), paint)
        }
        if (mBottomLineColor != 0) {
            paint.color = mBottomLineColor
            canvas.drawRect(Rect(0, measuredHeight - mLineSize, measuredWidth, measuredHeight), paint)
        }
    }

    fun setup(
              solid: Int? = null, stroke: Int? = null, strokeWidth: Int? = null,
              corner: Int? = null, cornerArr: Array<Int>? = null, enableRipple: Boolean? = null, rippleColor: Int? = null,
              topLineColor: Int? = null, bottomLineColor: Int? = null, lineSize: Int? = null,
              gradientOrientation: GradientDrawable.Orientation? = null,  gradientStartColor: Int? = null,
              gradientCenterColor: Int? = null,gradientEndColor: Int? = null){
        if(solid!=null) mSolid = solid
        if(stroke!=null) mStroke = stroke
        if(strokeWidth!=null) mStrokeWidth = strokeWidth
        if(corner!=null) mCorner = corner
        if(cornerArr!=null && cornerArr.size==4) {
            mTopLeftCorner = cornerArr[0]
            mTopRightCorner = cornerArr[1]
            mBottomRightCorner = cornerArr[2]
            mBottomLeftCorner = cornerArr[3]
        }
        if(enableRipple!=null) mEnableRipple = enableRipple
        if(rippleColor!=null) mRippleColor = rippleColor
        if(topLineColor!=null) mTopLineColor = topLineColor
        if(bottomLineColor!=null) mBottomLineColor = bottomLineColor
        if(lineSize!=null) mLineSize = lineSize
        if(gradientOrientation!=null) mGradientOrientation = gradientOrientation
        if(gradientStartColor!=null) mGradientStartColor = gradientStartColor
        if(gradientCenterColor!=null) mGradientCenterColor = gradientCenterColor
        if(gradientEndColor!=null) mGradientEndColor = gradientEndColor
        applySelf()
    }
}