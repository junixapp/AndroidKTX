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
 * @param url
 * @param placeholder 默认占位图
 * @param error 失败占位图
 * @param isCenterCrop
 * @param isCircle 是否是圆形
 * @param noTransition 是否禁用过渡动画
 */
fun ImageView.load(url: String, placeholder: Int = 0, error: Int = 0,
                   isCenterCrop: Boolean = true,
                   isCircle: Boolean = false,
                   noTransition: Boolean = false) {
    val options = RequestOptions().placeholder(placeholder).error(error).apply {
        if (isCenterCrop) centerCrop()
        if (isCircle) circleCrop()
    }
    Glide.with(context).load(url)
            .apply(options)
            .apply {
                if (noTransition) transition(DrawableTransitionOptions.withCrossFade(0))
                else transition(DrawableTransitionOptions.withCrossFade())
            }
            .into(this)
}