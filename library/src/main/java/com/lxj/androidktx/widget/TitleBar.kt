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
import kotlinx.android.synthetic.main.titlebar.view.*


/**
 * Description: 通用标题栏
 * Create by dance, at 2019/5/20
 */
class TitleBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attributeSet, defStyleAttr) {

    // 左边文本
    var leftText = ""
    var leftTextColor = Color.BLACK
    var leftTextSize = 16
    var leftTextDrawable: Drawable?
    var leftTextDrawableSize = 0

    //左边图片
    var leftImage: Drawable?
    var leftImagePadding = 12

    //中间标题
    var title = ""
    var titleSize = 18
    var titleColor = Color.BLACK
    var titleAlignLeft = false

    //右边文字
    var rightText = ""
    var rightTextColor = Color.BLACK
    var rightTextSize = 16

    //右边图片
    var rightImage: Drawable?
    var rightImagePadding = 12
    //右边图片2
    var rightImage2: Drawable?
    var rightImage2Padding = 12
    //右边图片3
    var rightImage3: Drawable?
    var rightImage3Padding = 12

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.TitleBar)
        leftText = ta.getString(R.styleable.TitleBar_leftText) ?: ""
        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor, leftTextColor)
        leftTextSize = ta.getDimensionPixelSize(R.styleable.TitleBar_leftTextSize, dp2px(leftTextSize))
        leftTextDrawable = ta.getDrawable(R.styleable.TitleBar_leftTextDrawable)
        leftTextDrawableSize = ta.getDimensionPixelSize(R.styleable.TitleBar_leftTextDrawableSize, leftTextDrawableSize)

        leftImage = ta.getDrawable(R.styleable.TitleBar_leftImageSrc)
        leftImagePadding = ta.getDimensionPixelSize(R.styleable.TitleBar_leftImagePadding, dp2px(leftImagePadding))

        title = ta.getString(R.styleable.TitleBar_title) ?: ""
        titleColor = ta.getColor(R.styleable.TitleBar_titleColor, titleColor)
        titleSize = ta.getDimensionPixelSize(R.styleable.TitleBar_titleSize, dp2px(titleSize))
        titleAlignLeft = ta.getBoolean(R.styleable.TitleBar_titleAlignLeft, titleAlignLeft)

        rightText = ta.getString(R.styleable.TitleBar_rightText) ?: ""
        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, rightTextColor)
        rightTextSize = ta.getDimensionPixelSize(R.styleable.TitleBar_rightTextSize, dp2px(rightTextSize))

        rightImage = ta.getDrawable(R.styleable.TitleBar_rightImageSrc)
        rightImagePadding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImagePadding, dp2px(rightImagePadding))
        rightImage2 = ta.getDrawable(R.styleable.TitleBar_rightImage2Src)
        rightImage2Padding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImage2Padding, dp2px(rightImage2Padding))
        rightImage3 = ta.getDrawable(R.styleable.TitleBar_rightImage3Src)
        rightImage3Padding = ta.getDimensionPixelSize(R.styleable.TitleBar_rightImage3Padding, dp2px(rightImage3Padding))

        ta.recycle()
        inflate(context, R.layout.titlebar, this)
        applyAttr()
        applySelf()
        initClick()
    }

    private fun applyAttr(){
        applyLeftText()
        applyLeftImage()
        applyTitle()
        applyRightText()
        applyRightImage()
        applyRightImage2()
        applyRightImage3()
    }

    private fun applySelf(){
        if(background==null)setBackgroundColor(Color.RED)
        if(paddingLeft==0)setPadding(dp2px(4), paddingTop, paddingRight, paddingBottom)
        if(paddingRight==0)setPadding(paddingLeft, paddingTop, dp2px(4), paddingBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp2px(48), MeasureSpec.EXACTLY))
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

    private fun applyLeftImage() {
        if (leftImage == null) {
            ivLeftImage.gone()
            return
        }
        ivLeftImage.visible()
        ivLeftImage.setImageDrawable(leftImage)
        if (leftImagePadding != 0) ivLeftImage.setPadding(leftImagePadding, leftImagePadding, leftImagePadding, leftImagePadding)
    }

    private fun applyTitle() {
        if(title.isEmpty()){
            tvTitle.gone()
            return
        }
        tvTitle.visible()
        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())
        if(titleAlignLeft){
            tvTitle.gravity = Gravity.LEFT.or(Gravity.CENTER_VERTICAL)
        }else{
            tvTitle.gravity = Gravity.CENTER
        }
    }

    private fun applyRightText() {
        if(rightText.isEmpty()){
            tvRightText.gone()
            return
        }
        tvRightText.visible()
        tvRightText.text = rightText
        tvRightText.setTextColor(rightTextColor)
        tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
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

    private fun applyRightImage2() {
        if (rightImage2 == null) {
            ivRightImage2.gone()
            return
        }
        ivRightImage2.visible()
        ivRightImage2.setImageDrawable(rightImage2)
        if (rightImage2Padding != 0) ivRightImage2.setPadding(rightImage2Padding, rightImage2Padding, rightImage2Padding, rightImage2Padding)
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

    private fun initClick(){
        if(tvLeftText.isVisible) tvLeftText.click { clickListener?.leftTextClick() }
        if(tvRightText.isVisible) tvRightText.click { clickListener?.rightTextClick() }
        if(ivLeftImage.isVisible) ivLeftImage.click { clickListener?.leftImageClick() }
        if(ivRightImage.isVisible) ivRightImage.click { clickListener?.rightImageClick() }
        if(ivRightImage2.isVisible) ivRightImage2.click { clickListener?.rightImage2Click() }
        if(ivRightImage3.isVisible) ivRightImage3.click { clickListener?.rightImage3Click() }
    }

    private var clickListener: ClickListener? = null
    fun clickListener(clickListener: ClickListener){
        this.clickListener = clickListener
    }

    interface ClickListener{
        fun leftTextClick(){}
        fun rightTextClick(){}
        fun leftImageClick(){}
        fun rightImageClick(){}
        fun rightImage2Click(){}
        fun rightImage3Click(){}
    }

}

