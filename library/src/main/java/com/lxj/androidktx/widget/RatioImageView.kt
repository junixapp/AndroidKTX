package com.lxj.androidktx.widget

/**
 * Description: 按指定宽高比自动设置高度的ImageView，默认情况下根据图片的内容来自适应
 * Create by dance, at 2019/7/2
 */
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.lxj.androidktx.R

class RatioImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attributeSet, defStyleAttr) {

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

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if(drawable==null || drawable.intrinsicHeight==0 || drawable.intrinsicWidth==0)return
        ratio = drawable.intrinsicWidth*1f/drawable.intrinsicHeight
        measure(0,0)
    }
}