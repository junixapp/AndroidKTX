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
import kotlin.math.min

open class RatioImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attributeSet, defStyleAttr) {

    var ratio = 0f //宽高比
    var expandWidth = false  //是否扩展宽度，默认根据宽高比扩展高度
    var limitWidth = 0f
    var limitHeight = 0f
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.RatioImageView)
        ratio = ta.getFloat(R.styleable.RatioImageView_riv_ratio, ratio)
        expandWidth = ta.getBoolean(R.styleable.RatioImageView_riv_expandWidth, expandWidth)
        limitWidth = ta.getDimension(R.styleable.RatioImageView_riv_limitWidth, limitWidth)
        limitHeight = ta.getDimension(R.styleable.RatioImageView_riv_limitHeight, limitHeight)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(ratio==0f)return
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if(expandWidth){
            //宽变高不变
            val realW = (height*ratio).toInt()
            setMeasuredDimension(if(limitWidth==0f) realW else min(realW, limitWidth.toInt()), height)
        }else{
            //高变宽不变
            val realH = (width/ratio).toInt()
            setMeasuredDimension(width, if(limitHeight==0f) realH else min(realH, limitHeight.toInt()))
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if(drawable==null || drawable.intrinsicHeight==0 || drawable.intrinsicWidth==0)return
        ratio = drawable.intrinsicWidth*1f/drawable.intrinsicHeight
        measure(0,0)
    }
}