package com.lxj.widget;

import android.content.res.Resources;
import android.graphics.Rect
import android.graphics.drawable.Drawable;
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

object WidgetUtil{
    fun  dp2px(dpValue: Float) : Int{
        var scale = Resources.getSystem().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f).toInt()
    }

    fun sizeDrawable(tv: TextView, width: Int, height: Int, startDrawable: Int = 0, topDrawable: Int = 0,
                                                 endDrawable: Int = 0, bottomDrawable: Int = 0): TextView {
        val rect = Rect(0, 0, width, height)
        tv.setCompoundDrawablesRelative(
                findDrawable(startDrawable, 0, tv)?.apply { bounds = rect },
        findDrawable(topDrawable, 1, tv)?.apply { bounds = rect },
        findDrawable(endDrawable, 2, tv)?.apply { bounds = rect },
        findDrawable(bottomDrawable, 3, tv)?.apply { bounds = rect }
    )
        return tv
    }

    /**
     * 优先使用传入的，如果不传则尝试使用TextView自己的
     */
    private fun findDrawable(drawableRes: Int, index:Int, textView: TextView): Drawable?{
        if(drawableRes!=0)return textView.resources.getDrawable(drawableRes)
        if(textView.compoundDrawablesRelative.isNotEmpty())return textView.compoundDrawablesRelative[index]
        return null
    }

    fun height(v: View, height: Int){
        val params = v.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.height = height
        v.layoutParams = params
    }
    fun widthAndHeight(v: View,width: Int, height: Int) {
        val params = v.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.width = width
        params.height = height
        v.layoutParams = params
    }
}
