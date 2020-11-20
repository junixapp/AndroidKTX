package com.lxj.androidktx.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class SuperWebView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attributeSet, defStyleAttr){
    var mOnScrollChangeListener: OnScrollChangeListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        // webview的高度
        val webcontent = contentHeight * scale
        // 当前webview的高度
        val webnow = height + scrollY.toFloat()
        if (Math.abs(webcontent - webnow) < 1) {
            //处于底端
            mOnScrollChangeListener!!.onPageEnd(l, t, oldl, oldt)
        } else if (scrollY == 0) {
            //处于顶端
            mOnScrollChangeListener!!.onPageTop(l, t, oldl, oldt)
        } else {
            mOnScrollChangeListener!!.onScrollChanged(l, t, oldl, oldt)
        }
    }

    interface OnScrollChangeListener {
        fun onPageEnd(l: Int, t: Int, oldl: Int, oldt: Int){}
        fun onPageTop(l: Int, t: Int, oldl: Int, oldt: Int){}
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int){}
    }

}