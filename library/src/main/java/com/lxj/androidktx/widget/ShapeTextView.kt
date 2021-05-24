package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.lxj.androidktx.R
import com.lxj.androidktx.core.createDrawable
import com.lxj.androidktx.core.dp2px
import com.lxj.androidktx.core.sizeDrawable

/**
 * Description: 能设置Shape的TextView
 * Create by dance, at 2019/5/21
 */
open class ShapeTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatTextView(context, attributeSet, defStyleAttr) {
    private var mDrawableWidth = 0
    private var mDrawableHeight = 0
    private var mSolid = 0 //填充色
    private var mStroke = 0 //边框颜色
    private var mStrokeWidth = 0 //边框大小
    private var mCorner = 0 //圆角
    //是否启用水波纹
    private var mEnableRipple = true
    private var mRippleColor = Color.parseColor("#88999999")
    //上下分割线
    private var mTopLineColor = 0
    private var mBottomLineColor = 0
    private var mLineSize = dp2px(.6f)
    private var mGradientStartColor = 0
    private var mGradientCenterColor = 0
    private var mGradientEndColor = 0
    private var mGradientOrientation = GradientDrawable.Orientation.LEFT_RIGHT  //从左到右
    private var mTypefacePath: String? = null

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeTextView)
        mDrawableWidth = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableWidth, mDrawableWidth)
        mDrawableHeight = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableHeight, mDrawableHeight)
        val size = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_drawableSize, 0)
        if (size != 0) {
            mDrawableWidth = size
            mDrawableHeight = size
        }

        mSolid = ta.getColor(R.styleable.ShapeTextView_stv_solid, mSolid)
        mStroke = ta.getColor(R.styleable.ShapeTextView_stv_stroke, mStroke)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_strokeWidth, mStrokeWidth)
        mCorner = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_corner, mCorner)

        mEnableRipple = ta.getBoolean(R.styleable.ShapeTextView_stv_enableRipple, mEnableRipple)
        mRippleColor = ta.getColor(R.styleable.ShapeTextView_stv_rippleColor, mRippleColor)

        mTopLineColor = ta.getColor(R.styleable.ShapeTextView_stv_topLineColor, mTopLineColor)
        mBottomLineColor = ta.getColor(R.styleable.ShapeTextView_stv_bottomLineColor, mBottomLineColor)
        mLineSize = ta.getDimensionPixelSize(R.styleable.ShapeTextView_stv_lineSize, mLineSize)

        mGradientStartColor = ta.getColor(R.styleable.ShapeTextView_stv_gradientStartColor, mGradientStartColor)
        mGradientCenterColor = ta.getColor(R.styleable.ShapeTextView_stv_gradientCenterColor, mGradientCenterColor)
        mGradientEndColor = ta.getColor(R.styleable.ShapeTextView_stv_gradientEndColor, mGradientEndColor)
        val orientation = ta.getInt(R.styleable.ShapeTextView_stv_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
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
        mTypefacePath = ta.getString(R.styleable.ShapeTextView_stv_typefacePath)
        ta.recycle()
        applySelf()
    }

    fun applySelf() {
        if (mDrawableWidth != 0 && mDrawableHeight != 0) {
            sizeDrawable(width = mDrawableWidth, height = mDrawableHeight)
        }
        if(!mTypefacePath.isNullOrEmpty()) typeface = Typeface.createFromAsset(context.assets, mTypefacePath)
        var color : Int? = null
        if (background !=null && background is ColorDrawable && mSolid==Color.TRANSPARENT){
            color = ( background as ColorDrawable).color
        }
        val drawable = createDrawable(color = color ?: mSolid, radius = mCorner.toFloat(), strokeColor = mStroke, strokeWidth = mStrokeWidth,
                enableRipple = mEnableRipple, rippleColor = mRippleColor, gradientStartColor = mGradientStartColor,
            gradientCenterColor = mGradientCenterColor, gradientEndColor = mGradientEndColor, gradientOrientation = mGradientOrientation)
        setBackgroundDrawable(drawable)
    }

    private val topLine = Rect(0, 0, 0, 0)
    private val bottomLine = Rect(0, 0, 0, 0)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        topLine.right = measuredWidth
        topLine.bottom = mLineSize
        bottomLine.top = measuredHeight - mLineSize
        bottomLine.right = measuredWidth
        bottomLine.bottom = measuredHeight
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
        if(lineSize!=null) mLineSize = lineSize
        if(gradientOrientation!=null) mGradientOrientation = gradientOrientation
        if(gradientStartColor!=null) mGradientStartColor = gradientStartColor
        if(gradientCenterColor!=null) mGradientCenterColor = gradientCenterColor
        if(gradientEndColor!=null) mGradientEndColor = gradientEndColor
        if(typefacePath!=null) mTypefacePath = typefacePath
        applySelf()
    }
}
