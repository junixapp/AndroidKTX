package com.lxj.androidktx.widget

/**
 * Description: 按指定宽高比自动设置高度的ImageView
 * Create by dance, at 2019/7/2
 */
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.lxj.androidktx.R

class RatioImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageView(context, attributeSet, defStyleAttr) {

    var ratio = 0f

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.RatioImageView)
        ratio = ta.getFloat(R.styleable.RatioImageView_riv_ratio, ratio)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(ratio==0f)return
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width/ratio
        setMeasuredDimension(width, height.toInt())
    }
}