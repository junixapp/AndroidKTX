package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*
import kotlinx.android.synthetic.main._ktx_super_layout.view.*

/**
 * Description: 超级布局，用来实现常见的横向图文布局
 * Create by dance, at 2019/5/21
 */
class SuperLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    //左边图片
    private var mLeftImage: Drawable? = null
    private var mLeftImageSize = 34.dp
    private var mLeftText: CharSequence = ""
    private var mLeftTextColor = Color.parseColor("#222222")
    private var mLeftTextSize = 16.sp
    private var mLeftTextMarginLeft = 8.dp
    private var mLeftTextMarginRight = 8.dp
    private var mLeftTextMarginBottom = 0
    private var mLeftTextMarginTop = 0
    private var mLeftSubText: CharSequence = ""
    private var mLeftSubTextColor = Color.parseColor("#777777")
    private var mLeftSubTextSize = 13.sp
    private var mCenterText: CharSequence = ""
    private var mCenterTextColor = Color.parseColor("#222222")
    private var mCenterTextSize = 15.sp
    private var mCenterTextBg: Drawable? = null
    private var mRightText: CharSequence = ""
    private var mRightTextColor = Color.parseColor("#777777")
    private var mRightTextSize = 15.sp
    private var mRightTextBg: Drawable? = null
    private var mRightTextBgColor = 0
    private var mRightTextWidth = 0
    private var mRightTextHeight = 0

    private var mRightImage: Drawable? = null
    private var mRightImageSize = 20.dp
    private var mRightImageMarginLeft = 2.dp
    private var mRightImage2: Drawable? = null
    private var mRightImage2Width = 2.dp
    private var mRightImage2Height = 2.dp
    private var mRightImage2MarginLeft = 10.dp
    private var mLeftTextBold = false
    private var mCenterTextBold = false
    private var mRightTextBold = false

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

        mLeftTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_leftTextBold, mLeftTextBold)
        mCenterTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_centerTextBold, mCenterTextBold)
        mRightTextBold = ta.getBoolean(R.styleable.SuperLayout_sl_rightTextBold, mRightTextBold)

        ta.recycle()
        inflate(context, R.layout._ktx_super_layout, this)
        applyAttr()
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setupContent(leftImageRes: Int? = null,
              leftText: CharSequence? = null,
              leftSubText: CharSequence? = null,
              centerText: CharSequence? = null,
              rightText: CharSequence? = null,
              rightImageRes: Int? = null,
              rightImage2Res: Int? = null,
              leftTextBold: Boolean? = null,
              centerTextBold: Boolean? = null,
              rightTextBold: Boolean? = null,
              ) {
        if (leftImageRes != null) mLeftImage = drawable(leftImageRes)
        if (rightImageRes != null) mRightImage = drawable(rightImageRes)
        if (rightImage2Res != null) mRightImage2 = drawable(rightImage2Res)
        if(leftText!=null)this.mLeftText = leftText
        if(leftSubText!=null)this.mLeftSubText = leftSubText
        if(centerText!=null)this.mCenterText = centerText
        if(rightText!=null)this.mRightText = rightText
        if(leftTextBold!=null)this.mLeftTextBold = leftTextBold
        if(centerTextBold!=null)this.mCenterTextBold = centerTextBold
        if(rightTextBold!=null)this.mRightTextBold = rightTextBold
        applyAttr()
    }

    fun applyAttr() {
        if (childCount == 0) return
        if(Build.VERSION.SDK_INT >= 21) clipToOutline = clipChildren
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
            if (mLeftTextBold) tvLeftText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
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
            if (mCenterTextBold) tvCenterText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
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
            if (mRightTextBold) tvRightText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
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

    fun leftImageView() = ivLeftImage
    fun leftTextView() = tvLeftText
    fun leftSubTextView() = tvLeftSubText
    fun rightTextView() = tvRightText
    fun centerTextView() = tvCenterText
    fun rightImageView() = ivRightImage
    fun rightImageView2() = ivRightImage2
}