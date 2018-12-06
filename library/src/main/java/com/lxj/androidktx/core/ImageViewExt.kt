package com.lxj.androidktx.core

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * Description:
 * Create by lxj, at 2018/12/5
 */

/**
 * Glide加载图片
 * @param url 可以是网络，可以是File，可以是资源id
 * @param placeholder 默认占位图
 * @param error 失败占位图
 * @param isCenterCrop
 * @param isCircle 是否是圆形
 * @param roundRadius 圆角角度，默认为0，不带圆角
 * @param blurRadius 模糊半径，默认为0，不模糊
 * @param hasTransition 是否有过渡动画
 */
fun ImageView.load(url: Any, placeholder: Int = 0, error: Int = 0,
                   isCenterCrop: Boolean = true,
                   isCircle: Boolean = false,
                   roundRadius: Int = 0,
                   blurRadius: Int = 0,
                   isCrossFade: Boolean = false) {
    val options = RequestOptions().placeholder(placeholder).error(error).apply {
        if (isCenterCrop) centerCrop()
        if (isCircle) circleCrop()
    }
    Glide.with(context).load(url)
            .apply(options)
            .apply {
                if (isCrossFade) transition(DrawableTransitionOptions.withCrossFade())
            }
            .into(this)
}