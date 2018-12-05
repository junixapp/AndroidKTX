package com.lxj.androidktxdemo.fragment

import com.lxj.androidktx.core.toBackgroundColorSpan
import com.lxj.androidktx.core.toColorSpan
import com.lxj.androidktx.core.toSizeSpan
import com.lxj.androidktx.core.toStrikethrougthSpan
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
            "$str".toSizeSpan(0..2)
        """.trimIndent()
        tvSizeResult.text = str.toSizeSpan(0..2)

        // toColorSpan
        tvColorSpan.text = """
            "$str".toColorSpan(2..6)
        """.trimIndent()
        tvColorResult.text = str.toColorSpan(2..6)

        // toBackgroundColorSpan
        tvBgColorSpan.text = """
            "$str".toBackgroundColorSpan(2..6)
        """.trimIndent()
        tvBgColorResult.text = str.toBackgroundColorSpan(2..6)

        // toStrikethrougthSpan
        tvStrikethrougthSpan.text = """
            "$str".toStrikethrougthSpan(2..6)
        """.trimIndent()
        tvStrikethrougthResult.text = str.toStrikethrougthSpan(2..6)
    }
}