package com.lxj.androidktx.core

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.jaeger.ninegridimageview.NineGridImageView
import com.jaeger.ninegridimageview.NineGridImageViewAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.SmartGlideImageLoader

/**
 * 绑定数据
 */
fun <T> NineGridImageView<T>.setup(urls: List<T>, corner: Int = 0,
                                   placeholder: Int = 0, error: Int = 0,
                                   isCenterCrop: Boolean = true,
                                   isCrossFade: Boolean = true){
    setAdapter(object : NineGridImageViewAdapter<T>() {
        override fun onDisplayImage(context: Context?, imageView: ImageView?, t: T?) {
            imageView?.load(t, placeholder = placeholder, error = error, isCrossFade = isCrossFade,
                    roundRadius = corner, isCenterCrop = isCenterCrop)
        }

        override fun onItemImageClick(context: Context?, imageView: ImageView, index: Int, list: MutableList<T>) {
            super.onItemImageClick(context, imageView, index, list)
            XPopup.Builder(context).asImageViewer(imageView, index, list.toList(), false, true,
                    -1, -1, corner, true, Color.rgb(32, 36, 46) ,
            { popupView, position ->
                popupView.updateSrcView(getChildAt(position) as ImageView)
            }, SmartGlideImageLoader(error), null).show()
        }

        override fun generateImageView(context: Context?): ImageView {
            return ImageView(context)
        }
    })
    setImagesData(urls)
}