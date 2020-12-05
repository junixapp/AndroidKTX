package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.AdaptScreenUtils
import com.lxj.androidktx.core.widthAndHeight
import com.lxj.androidktx.widget.RoundImageView

/**
 * 根据设置的图片大小，自动按比例限制在最大宽高范围内
 */
class LimitImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
)
    : AppCompatImageView(context, attributeSet, defStyleAttr) {
    var ratio = 0f //宽高比
    var limitWidth = 0
    var limitHeight = 0
    var imgWidth = 0
    var imgHeight = 0
    init {
        limitWidth = AdaptScreenUtils.pt2Px(160f)
        limitHeight = AdaptScreenUtils.pt2Px(160f)
    }

    fun setImageSize(w: Int, h: Int){
        this.imgWidth = w
        this.imgHeight = h
        ratio = imgWidth*1f/imgHeight
        var width = imgWidth
        var height = imgHeight
        if(ratio < 1){
            //按高度等比缩放
            if(limitHeight==0) return
            height = Math.min(height, limitHeight)
            val realW = (height*ratio).toInt()
            if(realW!=0 && height!=0){
                widthAndHeight(realW, height)
            }
        }else{
            //按宽度等比缩放
            if(limitWidth==0) return
            width = Math.min(width, limitWidth)
            val realH = (width/ratio).toInt()
            if(width!=0 && realH!=0){
                widthAndHeight(width, realH)
            }
        }
    }
}