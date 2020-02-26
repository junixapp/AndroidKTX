package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
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
    var mleftImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftImageSize = dp2px(34f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //左边文字
    var mleftText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextColor = Color.parseColor("#222222")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextSize = sp2px(16f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextMarginLeft = dp2px(8f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextMarginRight = dp2px(8f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextMarginTop = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftTextMarginBottom = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //左边子文字
    var mleftSubText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftSubTextColor = Color.parseColor("#777777")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mleftSubTextSize = sp2px(13f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //中间文字
    var mcenterText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mcenterTextColor = Color.parseColor("#222222")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mcenterTextSize = sp2px(15f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mcenterTextBg: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边文字
    var mrightText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextColor = Color.parseColor("#777777")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextSize = sp2px(15f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextBg: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextBgColor = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextWidth = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightTextHeight = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边图片
    var mrightImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightImageSize = dp2px(20f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightImageMarginLeft = dp2px(10f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边图片2
    var mrightImage2: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightImage2Size = dp2px(55f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrightImage2MarginLeft = dp2px(10f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //背景
    var msolid = 0 //填充色
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mstroke = 0 //边框颜色
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mstrokeWidth = 0 //边框大小
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mcorner = 0 //圆角
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //上下分割线
    var mtopLineColor = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mbottomLineColor = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mlineSize = dp2px(.6f)
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //是否启用水波纹
    var menableRipple = true
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var mrippleColor = Color.parseColor("#88999999")
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SuperLayout)
        mleftImage = ta.getDrawable(R.styleable.SuperLayout_sl_leftImageSrc)
        mleftImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftImageSize, mleftImageSize)

        mleftText = ta.getString(R.styleable.SuperLayout_sl_leftText) ?: ""
        mleftTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftTextColor, mleftTextColor)
        mleftTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextSize, mleftTextSize)
        mleftTextMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginLeft, mleftTextMarginLeft)
        mleftTextMarginRight = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginRight, mleftTextMarginRight)
        mleftTextMarginTop = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginTop, mleftTextMarginTop)
        mleftTextMarginBottom = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftTextMarginBottom, mleftTextMarginBottom)

        mleftSubText = ta.getString(R.styleable.SuperLayout_sl_leftSubText) ?: ""
        mleftSubTextColor = ta.getColor(R.styleable.SuperLayout_sl_leftSubTextColor, mleftSubTextColor)
        mleftSubTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_leftSubTextSize, mleftSubTextSize)

        mcenterText = ta.getString(R.styleable.SuperLayout_sl_centerText) ?: ""
        mcenterTextColor = ta.getColor(R.styleable.SuperLayout_sl_centerTextColor, mcenterTextColor)
        mcenterTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_centerTextSize, mcenterTextSize)
        mcenterTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_centerTextBg)

        mrightText = ta.getString(R.styleable.SuperLayout_sl_rightText) ?: ""
        mrightTextColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextColor, mrightTextColor)
        mrightTextSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextSize, mrightTextSize)
        mrightTextBg = ta.getDrawable(R.styleable.SuperLayout_sl_rightTextBg)
        mrightTextBgColor = ta.getColor(R.styleable.SuperLayout_sl_rightTextBgColor, mrightTextBgColor)
        mrightTextWidth = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextWidth, mrightTextWidth)
        mrightTextHeight = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightTextHeight, mrightTextHeight)

        mrightImage = ta.getDrawable(R.styleable.SuperLayout_sl_rightImageSrc)
        mrightImageSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageSize, mrightImageSize)
        mrightImageMarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImageMarginLeft, mrightImageMarginLeft)

        mrightImage2 = ta.getDrawable(R.styleable.SuperLayout_sl_rightImage2Src)
        mrightImage2Size = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2Size, mrightImage2Size)
        mrightImage2MarginLeft = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_rightImage2MarginLeft, mrightImage2MarginLeft)

        msolid = ta.getColor(R.styleable.SuperLayout_sl_solid, msolid)
        mstroke = ta.getColor(R.styleable.SuperLayout_sl_stroke, mstroke)
        mstrokeWidth = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_strokeWidth, mstrokeWidth)
        mcorner = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_corner, mcorner)

        mtopLineColor = ta.getColor(R.styleable.SuperLayout_sl_topLineColor, mtopLineColor)
        mbottomLineColor = ta.getColor(R.styleable.SuperLayout_sl_bottomLineColor, mbottomLineColor)
        mlineSize = ta.getDimensionPixelSize(R.styleable.SuperLayout_sl_lineSize, mlineSize)
        menableRipple = ta.getBoolean(R.styleable.SuperLayout_sl_enableRipple, menableRipple)
        mrippleColor = ta.getColor(R.styleable.SuperLayout_sl_rippleColor, mrippleColor)

        ta.recycle()
        inflate(context, R.layout._ktx_super_layout, this)
        applyAttr()
        applySelf()
    }

    fun setup(leftImageRes: Int = 0,
              leftText: CharSequence = mleftText,
              leftSubText: CharSequence = mleftSubText,
              centerText: CharSequence = mcenterText,
              rightText: CharSequence = mrightText,
              rightImageRes: Int = 0,
              rightImage2Res: Int = 0) {
        if (leftImageRes != 0) mleftImage = drawable(leftImageRes)
        if (rightImageRes != 0) mrightImage = drawable(rightImageRes)
        if (rightImage2Res != 0) mrightImage2 = drawable(rightImage2Res)
        this.mleftText = leftText
        this.mleftSubText = leftSubText
        this.mcenterText = centerText
        this.mrightText = rightText
        applyAttr()
        applySelf()
    }

    fun applySelf() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        if (msolid != 0 || mstroke != 0) {
            val drawable = createDrawable(color = msolid, radius = mcorner.toFloat(), strokeColor = mstroke, strokeWidth = mstrokeWidth,
                    enableRipple = menableRipple, rippleColor = mrippleColor)
            setBackgroundDrawable(drawable)
        }
    }

    fun applyAttr() {
        if(childCount==0)return
        //左边图片
        if (mleftImage == null) {
            ivLeftImage.gone()
        } else {
            ivLeftImage.visible()
            ivLeftImage.setImageDrawable(mleftImage)
            ivLeftImage.widthAndHeight(mleftImageSize, mleftImageSize)
        }

        //左边文字
        if (mleftText.isEmpty()) {
            tvLeftText.gone()
        } else {
            tvLeftText.visible()
            tvLeftText.text = mleftText
            tvLeftText.setTextColor(mleftTextColor)
            tvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mleftTextSize.toFloat())
            tvLeftText.margin(bottomMargin = mleftTextMarginBottom, topMargin = mleftTextMarginTop)
            llLeft.margin(leftMargin = mleftTextMarginLeft, rightMargin = mleftTextMarginRight)
        }

        //左边子文字
        if (mleftSubText.isEmpty()) {
            tvLeftSubText.gone()
        } else {
            tvLeftSubText.visible()
            tvLeftSubText.text = mleftSubText
            tvLeftSubText.setTextColor(mleftSubTextColor)
            tvLeftSubText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mleftSubTextSize.toFloat())
        }

        //中间文字
        if (mcenterText.isEmpty()) {
            tvCenterText.invisible()
        } else {
            tvCenterText.visible()
            tvCenterText.text = mcenterText
            tvCenterText.setTextColor(mcenterTextColor)
            tvCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mcenterTextSize.toFloat())
            if (mcenterTextBg != null) tvCenterText.setBackgroundDrawable(mcenterTextBg)
        }

        //右边文字
        if (mrightText.isEmpty()) {
            tvRightText.gone()
        } else {
            tvRightText.visible()
            tvRightText.text = mrightText
            tvRightText.setTextColor(mrightTextColor)
            tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mrightTextSize.toFloat())
            if (mrightTextBg != null) tvRightText.setBackgroundDrawable(mrightTextBg)
            if(mrightTextWidth!=0)tvRightText.width(mrightTextWidth)
            if(mrightTextHeight!=0)tvRightText.height(mrightTextHeight)
            if (mrightTextBgColor != 0) tvRightText.setBackgroundColor(mrightTextBgColor)
        }

        //右边图片
        if (mrightImage == null) {
            ivRightImage.gone()
        } else {
            ivRightImage.visible()
            ivRightImage.setImageDrawable(mrightImage)
            ivRightImage.widthAndHeight(mrightImageSize, mrightImageSize)
            ivRightImage.margin(leftMargin = mrightImageMarginLeft)
        }

        //右边图片2
        if (mrightImage2 == null) {
            ivRightImage2.gone()
        } else {
            ivRightImage2.visible()
            ivRightImage2.setImageDrawable(mrightImage2)
            ivRightImage2.widthAndHeight(mrightImage2Size, mrightImage2Size)
            ivRightImage2.margin(leftMargin = mrightImage2MarginLeft)
        }
    }

    private val paint = Paint()
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mtopLineColor != 0) {
            paint.color = mtopLineColor
            canvas.drawRect(Rect(0, 0, measuredWidth, mlineSize), paint)
        }
        if (mbottomLineColor != 0) {
            paint.color = mbottomLineColor
            canvas.drawRect(Rect(0, measuredHeight - mlineSize, measuredWidth, measuredHeight), paint)
        }
    }

    fun leftImageView() = ivLeftImage
    fun leftTextView() = tvLeftText
    fun rightTextView() = tvRightText
    fun centerTextView() = tvCenterText
    fun rightImageView() = ivRightImage
    fun rightImageView2() = ivRightImage2
}