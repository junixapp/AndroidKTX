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
        tvClickResult.clickSpan(str = str, range = 2..6, color = Color.BLUE, clickAction = {
            toast("哈哈我被点击了".toColorSpan(0..2))
        })

        tt.sizeDrawable(300)

        tvAppendResult.text = "演示一下appendXX方法的用法"
        tvAppendResult.appendSizeSpan("变大变大")
                .appendColorSpan("我要变色", color = Color.parseColor("#f0aafc"))
                .appendBackgroundColorSpan("我是有底色的", color = Color.parseColor("#cacee0"))
                .appendStrikeThrougthSpan("添加删除线哦哦哦哦")
                .appendClickSpan("来点我一下试试啊", isUnderlineText = true, clickAction = {
                    toast("哎呀，您点到我了呢，嘿嘿")
                } )
                .appendStyleSpan("我是粗体的")
    }
}