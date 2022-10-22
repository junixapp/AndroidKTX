package com.lxj.androidktx.core

import android.graphics.Rect
import android.os.Build
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.widget.ShapeImageView
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class CommonBannerAdapter<T>( var margin: Rect = Rect(), var cornerRadius: Int = 0,
                          var pageElevation: Int = 0, var onItemClick: ((Int)->Unit)? = null) :
    BaseBannerAdapter<T>() {
    override fun bindData(holder: BaseViewHolder<T>, data: T, position: Int, pageSize: Int) {
        holder.itemView.margin(margin.left, margin.top, margin.right, margin.bottom)
        (holder.itemView as ShapeImageView).apply {
            load(data, isCrossFade = true, roundRadius = cornerRadius)
            LogUtils.d("banner posi: $position  data: $data")
            setup(corner = cornerRadius)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = pageElevation.toFloat()
            }
            click { onItemClick?.invoke(position) }
        }
    }

    override fun getLayoutId(viewType: Int) = R.layout._ktx_adapter_common_banner
}

