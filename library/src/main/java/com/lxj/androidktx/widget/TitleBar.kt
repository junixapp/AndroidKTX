package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.RelativeLayout
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*
import kotlinx.android.synthetic.main._ktx_titlebar.view.*


/**
 * Description: 通用标题栏
 * Create by dance, at 2019/5/20
 */
class TitleBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attributeSet, defStyleAttr) {

    // 左边文本
    var leftText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var leftTextColor = Color.BLACK
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var leftTextSize = 16
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var leftTextDrawable: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var leftTextDrawableSize = 0
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //左边图片
    var leftImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var leftImagePadding = 12
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //中间标题
    var title: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var titleSize = 18
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var titleColor = Color.BLACK
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var titleAlignLeft = false
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边文字
    var rightText: CharSequence = ""
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var rightTextColor = Color.BLACK
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var rightTextSize = 16
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    //右边图片
    var rightImage: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var rightImagePadding = 12
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    //右边图片2
    var rightImage2: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var rightImage2Padding = 12
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    //右边图片3
    var rightImage3: Drawable? = null
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }
    var rightImage3Padding = 12
        set(value) {
            field = value
            applyAttr()
            applySelf()
        }

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.TitleBar)
        leftText = ta.getString(R.styleable.TitleBar_leftText) ?: ""
        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor, leftTextColor)
        leftTextSize = ta.getDimensionPixelSize(R.styleable.TitleBar_leftTextSize, dp2px(leftTextSize.toFloat()))
        leftTextDrawable = ta.getDrawable(R.styleable.TitleBar_leftTextDrawable)
        leftTextDrawableSize = ta.getDimensionPixelSize(R.styleable.TitleBar_leftTextDrawableSize, leftTextDrawableSize)

        leftImage = ta.getDrawable(R.styleable.TitleBar_leftImageSrc)
        leftImagePadding = ta.getDimensionPixelSize(R.styleable.TitleBar_leftImagePadding, dp2px(leftImagePadding.toFloat()))

        title = ta.getString(R.styleable.TitleBar_title) ?: ""
        titleColor = ta.getColor(R.styleable.TitleBar_titleColor, titleColor)
        titleSize = ta.getDimensionPixelSize(R.styleable.TitleBar_titleSize, dp2px(titleSize.toFloat()))
        titleAlignLeft = ta.getBoolean(R.styleable.TitleBar_titleAlignLeft, titleAlignLeft)

        rightText = ta.getString(R.styleable.TitleBar_rightText) ?: ""
        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, rightTextColor)
        rightTextSize = ta.getDimensionPixelSize(R.styleable.TitleBar_rightTextSize, dp2px(rightTextSize.toFloat()))

        rightImage = ta.getDrawable(R.styleable.TitleBar_rightImageSrc)
        rightImagePadding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImagePadding, dp2px(rightImagePadding.toFloat()))
        rightImage2 = ta.getDrawable(R.styleable.TitleBar_rightImage2Src)
        rightImage2Padding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImage2Padding, dp2px(rightImage2Padding.toFloat()))
        rightImage3 = ta.getDrawable(R.styleable.TitleBar_rightImage3Src)
        rightImage3Padding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImage3Padding, dp2px(rightImage3Padding.toFloat()))

        ta.recycle()
        inflate(context, R.layout._ktx_titlebar, this)
        applyAttr()
        applySelf()
        initClick()
    }

    private fun applyAttr() {
        if(childCount==0)return
        applyLeftText()
        applyLeftImage()
        applyTitle()
        applyRightText()
        applyRightImage()
        applyRightImage2()
        applyRightImage3()
    }

    private fun applySelf() {
        if (background == null) setBackgroundColor(Color.WHITE)
        if (paddingLeft == 0) setPadding(dp2px(4f), paddingTop, paddingRight, paddingBottom)
        if (paddingRight == 0) setPadding(paddingLeft, paddingTop, dp2px(4f), paddingBottom)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp2px(48f), MeasureSpec.EXACTLY))
//    }

    fun setup(leftImageRes: Int = 0, leftText: CharSequence = "", title: CharSequence = "", rightImageRes: Int = 0,
              rightText: CharSequence = ""): TitleBar {
        if (leftImageRes != 0) this.leftImage = drawable(leftImageRes)
        if (rightImageRes != 0) this.rightImage = drawable(rightImageRes)
        if(title.isNotEmpty()) this.title = title
        if(leftText.isNotEmpty()) this.leftText = leftText
        if(rightText.isNotEmpty()) this.rightText = rightText
        applyAttr()
        return this
    }

    private fun applyLeftText() {
        if (leftText.isEmpty()) {
            tvLeftText.gone()
            return
        }
        tvLeftText.visible()
        tvLeftText.text = leftText
        tvLeftText.setTextColor(leftTextColor)
        tvLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize.toFloat())
        if (leftTextDrawable != null) {
            tvLeftText.setCompoundDrawablesWithIntrinsicBounds(leftTextDrawable, null, null, null)
            if (leftTextDrawableSize == 0 || leftTextDrawable!!.intrinsicHeight == 0) return
            val scale = leftTextDrawable!!.intrinsicWidth * 1f / leftTextDrawable!!.intrinsicHeight
            if (scale > 1) {
                tvLeftText.sizeDrawable(width = leftTextDrawableSize,
                        height = (leftTextDrawableSize / scale).toInt())
            } else {
                tvLeftText.sizeDrawable(width = (leftTextDrawableSize / scale).toInt(),
                        height = leftTextDrawableSize)
            }
        }
    }

    fun setupLeftText(text: CharSequence, textColor: Int = leftTextColor, textSize: Int = leftTextSize,
                      drawable: Drawable? = leftTextDrawable, drawableSize: Int = leftTextDrawableSize): TitleBar {
        this.leftText = text
        this.leftTextColor = textColor
        this.leftTextSize = textSize
        this.leftTextDrawable = drawable
        this.leftTextDrawableSize = drawableSize
        applyLeftText()
        return this
    }

    private fun applyLeftImage() {
        if (leftImage == null) {
            ivLeftImage.gone()
            return
        }
        ivLeftImage.visible()
        ivLeftImage.setImageDrawable(leftImage)
        if (leftImagePadding != 0) ivLeftImage.setPadding(leftImagePadding, leftImagePadding, leftImagePadding, leftImagePadding)
    }

    fun setupLeftImage(imageRes: Int, imagePadding: Int = leftImagePadding): TitleBar {
        this.leftImage = drawable(imageRes)
        this.leftImagePadding = imagePadding
        applyLeftImage()
        return this
    }

    private fun applyTitle() {
        if (title.isEmpty()) {
            tvTitle.gone()
            return
        }
        tvTitle.visible()
        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        if (titleAlignLeft) {
            tvTitle.gravity = Gravity.LEFT.or(Gravity.CENTER_VERTICAL)
        } else {
            tvTitle.gravity = Gravity.CENTER
        }
    }

    fun setupTitle(text: CharSequence, textColor: Int = titleColor, textSize: Int = titleSize, alignLeft: Boolean = titleAlignLeft): TitleBar {
        this.title = text
        this.titleColor = textColor
        this.titleSize = textSize
        this.titleAlignLeft = alignLeft
        applyTitle()
        return this
    }

    private fun applyRightText() {
        if (rightText.isEmpty()) {
            tvRightText.gone()
            return
        }
        tvRightText.visible()
        tvRightText.text = rightText
        tvRightText.setTextColor(rightTextColor)
        tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
    }

    fun setupRightText(text: CharSequence, textColor: Int = rightTextColor, textSize: Int = rightTextSize): TitleBar {
        this.rightText = text
        this.rightTextColor = textColor
        this.rightTextSize = textSize
        applyRightText()
        return this
    }

    private fun applyRightImage() {
        if (rightImage == null) {
            ivRightImage.gone()
            return
        }
        ivRightImage.visible()
        ivRightImage.setImageDrawable(rightImage)
        if (rightImagePadding != 0) ivRightImage.setPadding(rightImagePadding, rightImagePadding, rightImagePadding, rightImagePadding)
    }

    fun setupRightImage(imageRes: Int, imagePadding: Int = rightImagePadding): TitleBar {
        this.rightImage = drawable(imageRes)
        this.rightImagePadding = imagePadding
        applyRightImage()
        return this
    }

    private fun applyRightImage2() {
        if (rightImage2 == null) {
            ivRightImage2.gone()
            return
        }
        ivRightImage2.visible()
        ivRightImage2.setImageDrawable(rightImage2)
        if (rightImage2Padding != 0) ivRightImage2.setPadding(rightImage2Padding, rightImage2Padding, rightImage2Padding, rightImage2Padding)
    }

    fun setupRightImage2(imageRes: Int, imagePadding: Int = rightImage2Padding): TitleBar {
        this.rightImage2 = drawable(imageRes)
        this.rightImage2Padding = imagePadding
        applyRightImage2()
        return this
    }

    private fun applyRightImage3() {
        if (rightImage3 == null) {
            ivRightImage3.gone()
            return
        }
        ivRightImage3.visible()
        ivRightImage3.setImageDrawable(rightImage3)
        if (rightImage3Padding != 0) ivRightImage3.setPadding(rightImage3Padding, rightImage3Padding, rightImage3Padding, rightImage3Padding)
    }

    fun setupRightImage3(imageRes: Int, imagePadding: Int = rightImage3Padding): TitleBar {
        this.rightImage3 = drawable(imageRes)
        this.rightImage3Padding = imagePadding
        applyRightImage3()
        return this
    }

    private fun initClick() {
        tvLeftText.click { clickListener?.leftTextClick() }
        tvRightText.click { clickListener?.rightTextClick() }
        ivLeftImage.click { clickListener?.leftImageClick() }
        ivRightImage.click { clickListener?.rightImageClick() }
        ivRightImage2.click { clickListener?.rightImage2Click() }
        ivRightImage3.click { clickListener?.rightImage3Click() }
    }

    fun leftTextView() = tvLeftText
    fun rightTextView() = tvRightText
    fun leftImageView() = ivLeftImage
    fun rightImageView() = ivRightImage
    fun rightImageView2() = ivRightImage2
    fun rightImageView3() = ivRightImage3

    private var clickListener: ClickListener? = null
    fun clickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun leftTextClick() {}
        fun rightTextClick() {}
        fun leftImageClick() {}
        fun rightImageClick() {}
        fun rightImage2Click() {}
        fun rightImage3Click() {}
    }

}

