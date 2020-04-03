package com.lxj.androidktx.core

import android.content.Context
import android.widget.ImageView
import com.jaeger.ninegridimageview.NineGridImageView
import com.jaeger.ninegridimageview.NineGridImageViewAdapter
import com.lxj.xpopup.XPopup

/**
 * 绑定数据
 */
fun <T> NineGridImageView<T>.setup(urls: List<T>, forceOriginalSize: Boolean = true, corner: Int = 0,
                                   placeholder: Int = 0){
    setAdapter(object : NineGridImageViewAdapter<String>(){
        override fun onDisplayImage(context: Context?, imageView: ImageView?, t: String?) {
            imageView?.load(t, isForceOriginalSize = forceOriginalSize, roundRadius = corner,
            placeholder = placeholder)
        }
        override fun onItemImageClick(context: Context?, imageView: ImageView, index: Int, list: MutableList<String>) {
            super.onItemImageClick(context, imageView, index, list)
            XPopup.Builder(context).asImageViewer(imageView, index, list.toList(), { popupView, position ->
                popupView.updateSrcView(getChildAt(position) as ImageView)
            },GlideImageLoader()).show()
        }
    })
    setImagesData(urls)
}