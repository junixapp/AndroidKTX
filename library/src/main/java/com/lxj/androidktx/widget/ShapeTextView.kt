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

    var mDrawableWidth = 0
    var mDrawableHeight = 0
    //背景
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
        mLineSize = ta.getColor(R.styleable.ShapeTextView_stv_lineSize, mLineSize)
        ta.recycle()
        if (mDrawableWidth != 0 && mDrawableHeight != 0) {
            sizeDrawable(width = mDrawableWidth, height = mDrawableHeight)
        }
        applySelf()
    }


    private fun applySelf() {
        if (mSolid != 0 || mStroke != 0) {
            val drawable = createDrawable(color = mSolid, radius = mCorner.toFloat(), strokeColor = mStroke, strokeWidth = mStrokeWidth,
                    enableRipple = mEnableRipple, rippleColor = mRippleColor)
            setBackgroundDrawable(drawable)
        } else {
            if (Build.VERSION.SDK_INT >= 21 && mEnableRipple) {
                val rippleDrawable = RippleDrawable(ColorStateList.valueOf(mRippleColor),
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

    fun setup(drawableWidth: Int = mDrawableWidth, drawableHeight: Int = mDrawableHeight,
              solid: Int = mSolid, stroke: Int = mStroke, strokeWidth: Int = mStrokeWidth,
              corner: Int = mCorner, enableRipple: Boolean = mEnableRipple, rippleColor: Int = mRippleColor,
              topLineColor: Int = mTopLineColor, bottomLineColor: Int = mBottomLineColor, lineSize: Int = mLineSize){
        mDrawableWidth = drawableWidth
        mDrawableHeight = drawableHeight
        mSolid = solid
        mStroke = stroke
        mStrokeWidth = strokeWidth
        mCorner = corner
        mEnableRipple = enableRipple
        mRippleColor = rippleColor
        mTopLineColor = topLineColor
        mBottomLineColor = bottomLineColor
        mLineSize = lineSize
        applySelf()
    }
}
