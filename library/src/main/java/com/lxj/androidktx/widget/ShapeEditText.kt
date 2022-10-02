package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp
import com.lxj.androidktx.core.sizeDrawable

/**
 * Description: 能设置Shape的EditText
 * Create by dance, at 2019/5/21
 */
open class ShapeEditText @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatEditText(context, attributeSet, defStyleAttr) {

    private var mDrawableWidth = 0
    private var mDrawableHeight = 0
    private var mSolid = 0 //填充色
    private var mStroke = 0 //边框颜色
    private var mStrokeWidth = 0 //边框大小
    private var mCorner = 0 //圆角
    //是否启用水波纹
    private var mEnableRipple = false
    private var mRippleColor = Color.parseColor("#88999999")
    //上下分割线
    private var mTopLineColor = 0
    private var mBottomLineColor = 0
    private var mLineSize = 1.dp
    private var mGradientStartColor = 0
    private var mGradientCenterColor = 0
    private var mGradientEndColor = 0
    private var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT  //从左到右
    private var mTypefacePath: String? = null

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeEditText)
        mDrawableWidth = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_drawableWidth, mDrawableWidth)
        mDrawableHeight = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_drawableHeight, mDrawableHeight)
        val size = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_drawableSize, 0)
        if (size != 0) {
            mDrawableWidth = size
            mDrawableHeight = size
        }

        mSolid = ta.getColor(R.styleable.ShapeEditText_set_solid, mSolid)
        mStroke = ta.getColor(R.styleable.ShapeEditText_set_stroke, mStroke)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_strokeWidth, mStrokeWidth)
        mCorner = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_corner, mCorner)

        mEnableRipple = ta.getBoolean(R.styleable.ShapeEditText_set_enableRipple, mEnableRipple)
        mRippleColor = ta.getColor(R.styleable.ShapeEditText_set_rippleColor, mRippleColor)

        mTopLineColor = ta.getColor(R.styleable.ShapeEditText_set_topLineColor, mTopLineColor)
        mBottomLineColor = ta.getColor(R.styleable.ShapeEditText_set_bottomLineColor, mBottomLineColor)
        mLineSize = ta.getDimensionPixelSize(R.styleable.ShapeEditText_set_lineSize, mLineSize)

        mGradientStartColor = ta.getColor(R.styleable.ShapeEditText_set_gradientStartColor, mGradientStartColor)
        mGradientCenterColor = ta.getColor(R.styleable.ShapeEditText_set_gradientCenterColor, mGradientCenterColor)
        mGradientEndColor = ta.getColor(R.styleable.ShapeEditText_set_gradientEndColor, mGradientStartColor)
        val orientation = ta.getInt(R.styleable.ShapeEditText_set_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
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
        mTypefacePath = ta.getString(R.styleable.ShapeEditText_set_typefacePath)
        ta.recycle()
        isFocusable = true
        isFocusableInTouchMode = true
        applySelf()
    }

    fun applySelf() {
        if (mDrawableWidth != 0 && mDrawableHeight != 0) {
            sizeDrawable(width = mDrawableWidth, height = mDrawableHeight)
        }
        if(!mTypefacePath.isNullOrEmpty()) typeface = Typeface.createFromAsset(context.assets, mTypefacePath)
        var color : Int? = null
        if (background !=null && background is ColorDrawable && mSolid==Color.TRANSPARENT){
            color = ( background as ColorDrawable) .color
        }
        val drawable = createDrawable(color = color?:mSolid, radius = mCorner.toFloat(), strokeColor = mStroke, strokeWidth = mStrokeWidth,
                enableRipple = mEnableRipple, rippleColor = mRippleColor, gradientStartColor = mGradientStartColor,
                gradientEndColor = mGradientEndColor, gradientOrientation = mGradientOrientation)
        //目的是为了移除EditText在获取焦点时默认的背景色
        val selector = StateListDrawable()
        selector.addState(intArrayOf(android.R.attr.state_focused, android.R.attr.state_pressed), drawable)
        selector.addState(intArrayOf(), drawable)
        setBackgroundDrawable(selector)
    }

    val topLine = Rect(0, 0, 0, 0)
    val bottomLine = Rect(0, 0, 0, 0)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        topLine.right = measuredWidth
        bottomLine.right = measuredWidth
        bottomLine.bottom = measuredHeight
        topLine.bottom = mLineSize
        bottomLine.top = measuredHeight - mLineSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mTopLineColor != 0) {
            paint.color = mTopLineColor
            canvas.drawRect(topLine, paint)
        }
        if (mBottomLineColor != 0) {
            paint.color = mBottomLineColor
            canvas.drawRect(bottomLine, paint)
        }
    }

    fun setup(drawableWidth: Int? = null, drawableHeight: Int? = null,drawableSize: Int? = null,
              solid: Int? = null, stroke: Int? = null, strokeWidth: Int? = null,
              corner: Int? = null, enableRipple: Boolean? = null, rippleColor: Int? = null,
              topLineColor: Int? = null, bottomLineColor: Int? = null, lineSize: Int? = null,
              gradientOrientation: GradientDrawable.Orientation? = null,  gradientStartColor: Int? = null,
              gradientCenterColor: Int? = null,gradientEndColor: Int? = null, typefacePath: String? = null){
        if(drawableWidth!=null) mDrawableWidth = drawableWidth
        if(drawableHeight!=null) mDrawableHeight = drawableHeight
        if (drawableSize != null) {
            mDrawableWidth = drawableSize
            mDrawableHeight = drawableSize
        }
        if(solid!=null) mSolid = solid
        if(stroke!=null) mStroke = stroke
        if(strokeWidth!=null) mStrokeWidth = strokeWidth
        if(corner!=null) mCorner = corner
        if(enableRipple!=null) mEnableRipple = enableRipple
        if(rippleColor!=null) mRippleColor = rippleColor
        if(topLineColor!=null) mTopLineColor = topLineColor
        if(bottomLineColor!=null) mBottomLineColor = bottomLineColor
        if(lineSize!=null) {
            mLineSize = lineSize
            topLine.bottom = mLineSize
            bottomLine.top = measuredHeight - mLineSize
            invalidate()
        }
        if(gradientOrientation!=null) mGradientOrientation = gradientOrientation
        if(gradientStartColor!=null) mGradientStartColor = gradientStartColor
        if(gradientCenterColor!=null) mGradientCenterColor = gradientCenterColor
        if(gradientEndColor!=null) mGradientEndColor = gradientEndColor
        if(typefacePath!=null) mTypefacePath = typefacePath
        applySelf()
    }
}
