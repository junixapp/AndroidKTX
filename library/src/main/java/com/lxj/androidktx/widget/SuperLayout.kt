package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*
import kotlinx.android.synthetic.main._ktx_super_layout.view.*

/**
 * Description: 超级布局，用来实现常见的横向图文布局
 * Create by dance, at 2019/5/21
 */
class SuperLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr) {

    //左边图片
    var mLeftImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftImageSize = dp2px(34f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //左边文字
    var mLeftText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextColor = Color.parseColor("#222222")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextSize = sp2px(16f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextMarginLeft = dp2px(8f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextMarginRight = dp2px(8f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextMarginTop = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftTextMarginBottom = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //左边子文字
    var mLeftSubText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftSubTextColor = Color.parseColor("#777777")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLeftSubTextSize = sp2px(13f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //中间文字
    var mCenterText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mCenterTextColor = Color.parseColor("#222222")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mCenterTextSize = sp2px(15f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mCenterTextBg: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边文字
    var mRightText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextColor = Color.parseColor("#777777")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextSize = sp2px(15f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextBg: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextBgColor = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextWidth = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightTextHeight = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边图片
    var mRightImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightImageSize = dp2px(20f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightImageMarginLeft = dp2px(10f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边图片2
    var mRightImage2: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightImage2Width = dp2px(25f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightImage2Height = dp2px(25f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRightImage2MarginLeft = dp2px(10f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //背景
    var mSolid = 0 //填充色
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mStroke = 0 //边框颜色
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mStrokeWidth = 0 //边框大小
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mCorner = 0 //圆角
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //上下分割线
    var mTopLine = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mBottomLineColor = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mLineSize = dp2px(.6f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //是否启用水波纹
    var mEnableRipple = true
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mRippleColor = Color.parseColor("#88999999")
        set(value) {
            field = value
            applyAttr()
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

    var leftTextBold = false
        set(value) {
            field = value
            applySelf()
        }
    var centerTextBold = false
        set(value) {
            field = value
            applySelf()
        }
    var rightTextBold = false
        set(value) {
            field = value
            applySelf()
        }

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SuperLayout)
        mLeftImage = ta.getDrawable(R.styleable.SuperLayout_sl_leftImageSrc)
        mLeftImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftImageSize, mLeftImageSize)

        mLeftText = ta.getString(R.styleable.SuperLayout_sl_leftText) ?: ""
        mLeftTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftTextColor, mLeftTextColor)
        mLeftTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextSize, mLeftTextSize)
        mLeftTextMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginLeft, mLeftTextMarginLeft)
        mLeftTextMarginRight = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginRight, mLeftTextMarginRight)
        mLeftTextMarginTop = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginTop, mLeftTextMarginTop)
        mLeftTextMarginBottom = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginBottom, mLeftTextMarginBottom)

        mLeftSubText = ta.getString(R.styleable.SuperLayout_sl_leftSubText) ?: ""
        mLeftSubTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftSubTextColor, mLeftSubTextColor)
        mLeftSubTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftSubTextSize, mLeftSubTextSize)

        mCenterText = ta.getString(R.styleable.SuperLayout_sl_centerText) ?: ""
        mCenterTextColor = ta.getColor(R.styleable.SuperLayout_sl_centerTextColor, mCenterTextColor)
        mCenterTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_centerTextSize, mCenterTextSize)
        mCenterTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_centerTextBg)

        mRightText = ta.getString(R.styleable.SuperLayout_sl_rightText) ?: ""
        mRightTextColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextColor, mRightTextColor)
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextSize, mRightTextSize)
        mRightTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_rightTextBg)
        mRightTextBgColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextBgColor, mRightTextBgColor)
        mRightTextWidth = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextWidth, mRightTextWidth)
        mRightTextHeight = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextHeight, mRightTextHeight)

        mRightImage = ta.getDrawable(R.styleable.SuperLayout_sl_rightImageSrc)
        mRightImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageSize, mRightImageSize)
        mRightImageMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageMarginLeft, mRightImageMarginLeft)

        mRightImage2 = ta.getDrawable(R.styleable.SuperLayout_sl_rightImage2Src)
        mRightImage2Width = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2Width, mRightImage2Width)
        mRightImage2Height = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2Height, mRightImage2Height)
        mRightImage2MarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2MarginLeft, mRightImage2MarginLeft)

        mSolid = ta.getColor(R.styleable.SuperLayout_sl_solid, mSolid)
        mStroke = ta.getColor(R.styleable.SuperLayout_sl_stroke, mStroke)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_strokeWidth, mStrokeWidth)
        mCorner = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_corner, mCorner)

        mTopLine = ta.getColor(R.styleable.SuperLayout_sl_topLineColor, mTopLine)
        mBottomLineColor = ta.getColor(R.styleable.SuperLayout_sl_bottomLineColor, mBottomLineColor)
        mLineSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_lineSize, mLineSize)
        mEnableRipple = ta.getBoolean(R.styleable.SuperLayout_sl_enableRipple, mEnableRipple)
        mRippleColor = ta.getColor(R.styleable.SuperLayout_sl_rippleColor, mRippleColor)

        mGradientStartColor = ta.getColor(R.styleable.SuperLayout_sl_gradientStartColor, mGradientStartColor)
        mGradientEndColor = ta.getColor(R.styleable.SuperLayout_sl_gradientEndColor, mGradientEndColor)
        val orientation = ta.getInt(R.styleable.SuperLayout_sl_gradientOrientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal)
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
        leftTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_leftTextBold, leftTextBold)
        centerTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_centerTextBold, centerTextBold)
        rightTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_rightTextBold, rightTextBold)

        ta.recycle()
        inflate(context, R.layout._ktx_super_layout, this)
        if(Build.VERSION.SDK_INT >= 21) setClipToOutline(true)
        applyAttr()
        applySelf()
    }

    fun setup(leftImageRes: Int = 0,
              leftText: CharSequence = mLeftText,
              leftSubText: CharSequence = mLeftSubText,
              centerText: CharSequence = mCenterText,
              rightText: CharSequence = mRightText,
              rightImageRes: Int = 0,
              rightImage2Res: Int = 0) {
        if (leftImageRes != 0) mLeftImage = drawable(leftImageRes)
        if (rightImageRes != 0) mRightImage = drawable(rightImageRes)
        if (rightImage2Res != 0) mRightImage2 = drawable(rightImage2Res)
        this.mLeftText = leftText
        this.mLeftSubText = leftSubText
        this.mCenterText = centerText
        this.mRightText = rightText
        applyAttr()
        applySelf()
    }

    fun applySelf() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        var color : Int? = null
        if (background !=null && background is ColorDrawable && mSolid==Color.TRANSPARENT){
            color = ( background as ColorDrawable) .color
        }
        val drawable = createDrawable(color = color?: mSolid, radius = mCorner.toFloat(), strokeColor = mStroke, strokeWidth = mStrokeWidth,
                enableRipple = mEnableRipple, rippleColor = mRippleColor, gradientStartColor = mGradientStartColor,
                gradientEndColor = mGradientEndColor, gradientOrientation = mGradientOrientation)
        setBackgroundDrawable(drawable)
    }

    fun applyAttr() {
        if (childCount == 0) return
        //左边图片
        if (mLeftImage == null) {
            ivLeftImage.gone()
        } else {
            ivLeftImage.visible()
            ivLeftImage.setImageDrawable(mLeftImage)
            ivLeftImage.widthAndHeight(mLeftImageSize, mLeftImageSize)
        }

        //左边文字
        if (mLeftText.isEmpty()) {
            tvLeftText.gone()
        } else {
            tvLeftText.visible()
            tvLeftText.text = mLeftText
            tvLeftText.setTextColor(mLeftTextColor)
            tvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize.toFloat())
            tvLeftText.margin(bottomMargin = mLeftTextMarginBottom, topMargin = mLeftTextMarginTop)
            llLeft.margin(leftMargin = mLeftTextMarginLeft, rightMargin = mLeftTextMarginRight)
            if (leftTextBold) tvLeftText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        //左边子文字
        if (mLeftSubText.isEmpty()) {
            tvLeftSubText.gone()
        } else {
            tvLeftSubText.visible()
            tvLeftSubText.text = mLeftSubText
            tvLeftSubText.setTextColor(mLeftSubTextColor)
            tvLeftSubText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftSubTextSize.toFloat())
        }

        //中间文字
        if (mCenterText.isEmpty()) {
            tvCenterText.invisible()
        } else {
            tvCenterText.visible()
            tvCenterText.text = mCenterText
            tvCenterText.setTextColor(mCenterTextColor)
            tvCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCenterTextSize.toFloat())
            if (mCenterTextBg != null) tvCenterText.setBackgroundDrawable(mCenterTextBg)
            if (centerTextBold) tvCenterText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        //右边文字
        if (mRightText.isEmpty()) {
            tvRightText.gone()
        } else {
            tvRightText.visible()
            tvRightText.text = mRightText
            tvRightText.setTextColor(mRightTextColor)
            tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
            if (mRightTextBg != null) tvRightText.setBackgroundDrawable(mRightTextBg)
            if (mRightTextWidth != 0) tvRightText.width(mRightTextWidth)
            if (mRightTextHeight != 0) tvRightText.height(mRightTextHeight)
            if (mRightTextBgColor != 0) tvRightText.setBackgroundColor(mRightTextBgColor)
            if (rightTextBold) tvRightText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        //右边图片
        if (mRightImage == null) {
            ivRightImage.gone()
        } else {
            ivRightImage.visible()
            ivRightImage.setImageDrawable(mRightImage)
            ivRightImage.widthAndHeight(mRightImageSize, mRightImageSize)
            ivRightImage.margin(leftMargin = mRightImageMarginLeft)
        }

        //右边图片2
        if (mRightImage2 == null) {
            ivRightImage2.gone()
        } else {
            ivRightImage2.visible()
            ivRightImage2.setImageDrawable(mRightImage2)
            ivRightImage2.widthAndHeight(mRightImage2Width, mRightImage2Height)
            ivRightImage2.margin(leftMargin = mRightImage2MarginLeft)
        }
    }

    private val paint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mTopLine != 0) {
            paint.color = mTopLine
            canvas.drawRect(Rect(0, 0, measuredWidth, mLineSize), paint)
        }
        if (mBottomLineColor != 0) {
            paint.color = mBottomLineColor
            canvas.drawRect(Rect(0, measuredHeight - mLineSize, measuredWidth, measuredHeight), paint)
        }
    }

    fun leftImageView() = ivLeftImage
    fun leftTextView() = tvLeftText
    fun leftSubTextView() = tvLeftSubText
    fun rightTextView() = tvRightText
    fun centerTextView() = tvCenterText
    fun rightImageView() = ivRightImage
    fun rightImageView2() = ivRightImage2
}