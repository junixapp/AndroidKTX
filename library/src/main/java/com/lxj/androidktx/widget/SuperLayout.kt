package com.lxj.androidktx.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
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
    private var leftImage: Drawable?
    private var leftImageSize = dp2px(34f)

    //左边文字
    private var leftText = ""
    private var leftTextColor = Color.parseColor("#222222")
    private var leftTextSize = sp2px(16f)
    private var leftTextMarginLeft = dp2px(8f)
    private var leftTextMarginRight = dp2px(8f)
    private var leftTextMarginTop = 0
    private var leftTextMarginBottom = 0

    //左边子文字
    private var leftSubText = ""
    private var leftSubTextColor = Color.parseColor("#777777")
    private var leftSubTextSize = sp2px(13f)

    //中间文字
    private var centerText = ""
    private var centerTextColor = Color.parseColor("#222222")
    private var centerTextSize = sp2px(15f)
    private var centerTextGravity = Gravity.LEFT.or(Gravity.CENTER_VERTICAL)
    private var centerTextBg: Drawable?

    //右边文字
    private var rightText = ""
    private var rightTextColor = Color.parseColor("#777777")
    private var rightTextSize = sp2px(15f)
    private var rightTextBg: Drawable?
    private var rightTextBgColor = 0
    private var rightTextVerticalPadding = 0
    private var rightTextHorizontalPadding = 0

    //右边图片
    private var rightImage: Drawable?
    private var rightImageSize = dp2px(20f)
    private var rightImageMarginLeft = dp2px(10f)

    //右边图片2
    private var rightImage2: Drawable?
    private var rightImage2Size = dp2px(55f)
    private var rightImage2MarginLeft = dp2px(10f)

    //背景
    private var solid = 0 //填充色
    private var stroke = 0 //边框颜色
    private var strokeWidth = 0 //边框大小
    private var corner = 0 //圆角

    //上下分割线
    private var topLineColor = 0
    private var bottomLineColor = 0
    var lineSize = dp2px(.6f)

    //是否启用水波纹
    private var enableRipple = true
    private var rippleColor = Color.parseColor("#88999999")
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SuperLayout)
        leftImage = ta.getDrawable(R.styleable.SuperLayout_sl_leftImageSrc)
        leftImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftImageSize, leftImageSize)

        leftText = ta.getString(R.styleable.SuperLayout_sl_leftText) ?: ""
        leftTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftTextColor, leftTextColor)
        leftTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextSize, leftTextSize)
        leftTextMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginLeft, leftTextMarginLeft)
        leftTextMarginRight = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginRight, leftTextMarginRight)
        leftTextMarginTop = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginTop, leftTextMarginTop)
        leftTextMarginBottom = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginBottom, leftTextMarginBottom)

        leftSubText = ta.getString(R.styleable.SuperLayout_sl_leftSubText) ?: ""
        leftSubTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftSubTextColor, leftSubTextColor)
        leftSubTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftSubTextSize, leftSubTextSize)

        centerText = ta.getString(R.styleable.SuperLayout_sl_centerText) ?: ""
        centerTextColor = ta.getColor(R.styleable.SuperLayout_sl_centerTextColor, centerTextColor)
        centerTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_centerTextSize, centerTextSize)
        centerTextGravity = ta.getInt(R.styleable.SuperLayout_sl_centerTextGravity, centerTextGravity)
        centerTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_centerTextBg)

        rightText = ta.getString(R.styleable.SuperLayout_sl_rightText) ?: ""
        rightTextColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextColor, rightTextColor)
        rightTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextSize, rightTextSize)
        rightTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_rightTextBg)
        rightTextBgColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextBgColor, rightTextBgColor)
        rightTextVerticalPadding = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextVerticalPadding, rightTextVerticalPadding)
        rightTextHorizontalPadding = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextHorizontalPadding, rightTextHorizontalPadding)

        rightImage = ta.getDrawable(R.styleable.SuperLayout_sl_rightImageSrc)
        rightImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageSize, rightImageSize)
        rightImageMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageMarginLeft, rightImageMarginLeft)

        rightImage2 = ta.getDrawable(R.styleable.SuperLayout_sl_rightImage2Src)
        rightImage2Size = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2Size, rightImage2Size)
        rightImage2MarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2MarginLeft, rightImage2MarginLeft)

        solid = ta.getColor(R.styleable.SuperLayout_sl_solid, solid)
        stroke = ta.getColor(R.styleable.SuperLayout_sl_stroke, stroke)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_strokeWidth, strokeWidth)
        corner = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_corner, corner)

        topLineColor = ta.getColor(R.styleable.SuperLayout_sl_topLineColor, topLineColor)
        bottomLineColor = ta.getColor(R.styleable.SuperLayout_sl_bottomLineColor, bottomLineColor)
        enableRipple = ta.getBoolean(R.styleable.SuperLayout_sl_enableRipple, enableRipple)
        rippleColor = ta.getColor(R.styleable.SuperLayout_sl_rippleColor, rippleColor)

        ta.recycle()
        inflate(context, R.layout._ktx_super_layout, this)
        applyAttr()
        applySelf()
    }

    fun setup(leftImageRes: Int = 0,
              leftText: String = "",
              leftSubText: String = "",
              centerText: String = "",
              rightText: String = "",
              rightImageRes: Int = 0,
              rightImage2Res: Int = 0) {
        if (leftImageRes != 0) leftImage = drawable(leftImageRes)
        if (rightImageRes != 0) rightImage = drawable(rightImageRes)
        if (rightImage2Res != 0) rightImage2 = drawable(rightImage2Res)
        this.leftText = leftText
        this.leftSubText = leftSubText
        this.centerText = centerText
        this.rightText = rightText
        applyAttr()
        applySelf()
    }

    private fun applySelf() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

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

    private fun applyAttr() {
        //左边图片
        if (leftImage == null) {
            ivLeftImage.gone()
        } else {
            ivLeftImage.visible()
            ivLeftImage.setImageDrawable(leftImage)
            ivLeftImage.widthAndHeight(leftImageSize, leftImageSize)
        }

        //左边文字
        if (leftText.isEmpty()) {
            tvLeftText.gone()
        } else {
            tvLeftText.visible()
            tvLeftText.text = leftText
            tvLeftText.setTextColor(leftTextColor)
            tvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize.toFloat())
            tvLeftText.margin(bottomMargin = leftTextMarginBottom, topMargin = leftTextMarginTop)
            llLeft.margin(leftMargin = leftTextMarginLeft, rightMargin = leftTextMarginRight)
        }

        //左边子文字
        if (leftSubText.isEmpty()) {
            tvLeftSubText.gone()
        } else {
            tvLeftSubText.visible()
            tvLeftSubText.text = leftSubText
            tvLeftSubText.setTextColor(leftSubTextColor)
            tvLeftSubText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftSubTextSize.toFloat())
        }

        //中间文字
        if (centerText.isEmpty()) {
            tvCenterText.invisible()
        } else {
            tvCenterText.visible()
            tvCenterText.text = centerText
            tvCenterText.setTextColor(centerTextColor)
            tvCenterText.gravity = centerTextGravity
            tvCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize.toFloat())
            if (centerTextBg != null) tvCenterText.setBackgroundDrawable(centerTextBg)
        }

        //右边文字
        if (rightText.isEmpty()) {
            tvRightText.gone()
        } else {
            tvRightText.visible()
            tvRightText.text = rightText
            tvRightText.setTextColor(rightTextColor)
            tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
            if (rightTextBg != null) tvRightText.setBackgroundDrawable(rightTextBg)
            tvRightText.setPadding(rightTextHorizontalPadding, rightTextVerticalPadding
                    , rightTextHorizontalPadding, rightTextVerticalPadding)
            if (rightTextBgColor != 0) tvRightText.setBackgroundColor(rightTextBgColor)
        }

        //右边图片
        if (rightImage == null) {
            ivRightImage.gone()
        } else {
            ivRightImage.visible()
            ivRightImage.setImageDrawable(rightImage)
            ivRightImage.widthAndHeight(rightImageSize, rightImageSize)
            ivRightImage.margin(leftMargin = rightImageMarginLeft)
        }

        //右边图片2
        if (rightImage2 == null) {
            ivRightImage2.gone()
        } else {
            ivRightImage2.visible()
            ivRightImage2.setImageDrawable(rightImage2)
            ivRightImage2.widthAndHeight(rightImage2Size, rightImage2Size)
            ivRightImage2.margin(leftMargin = rightImage2MarginLeft)
        }
    }

    private val paint = Paint()
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

    fun getLeftImageView() = ivLeftImage
    fun getRightImageView() = ivRightImage
    fun getRightImageView2() = ivRightImage2
}