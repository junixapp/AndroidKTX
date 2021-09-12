package com.lxj.androidktx.core

import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lxj.androidktx.R
import com.lxj.androidktx.widget.ShapeImageView
import com.youth.banner.adapter.BannerAdapter

class CommonBannerAdapter(data: List<Any>, var margin: Rect = Rect(), var cornerRadius: Int = 0,
                          var pageElevation: Int = 0, var onItemClick: ((Int)->Unit)? = null) :
    BannerAdapter<Any, CommonBannerAdapter.BannerViewHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout._ktx_adapter_common_banner, parent, false)
        return BannerViewHolder(item)
    }

    override fun onBindView(holder: BannerViewHolder, t: Any, position: Int, size: Int) {
        holder.itemView.margin(margin.left, margin.top, margin.right, margin.bottom)
        (holder.itemView as ShapeImageView).apply {
            load(t, isCrossFade = true)
            setup(corner = cornerRadius)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = pageElevation.toFloat()
            }
            click { onItemClick?.invoke(position) }
        }
    }

    inner class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}

