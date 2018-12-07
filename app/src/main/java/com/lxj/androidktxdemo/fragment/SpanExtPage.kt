package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import android.view.View
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_span_ext.*

/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
class SpanExtPage: BaseFragment(){
    override fun getLayoutId(): Int = R.layout.fragment_span_ext

    override fun initView() {
        val str = "我是测试文字"

        // toSizeSpan
        tvSizeSpan.text = """
            tv.sizeSpan(str, 0..2)
        """.trimIndent()
        tvSizeResult.sizeSpan(str, 0..2)

        // toColorSpan
        tvColorSpan.text = """
            tv.colorSpan(str, 2..6)
        """.trimIndent()
        tvColorResult.colorSpan(str,2..6)

        // toBackgroundColorSpan
        tvBgColorSpan.text = """
            tv.backgroundColorSpan(str,2..6)
        """.trimIndent()
        tvBgColorResult.backgroundColorSpan(str,2..6)

        // toStrikethrougthSpan
        tvStrikethrougthSpan.text = """
            tv.strikethrougthSpan(str,2..6)
        """.trimIndent()
        tvStrikethrougthResult.strikeThrougthSpan(str,2..6)

        // clickSpan
        tvClickSpan.text = """
            tv.clickSpan(str, 2..6, listener)
        """.trimIndent()
        tvClickResult.clickSpan(str,2..6, View.OnClickListener {
            toast("哈哈我被点击了".toColorSpan(0..2))
        }, Color.BLUE)
    }
}