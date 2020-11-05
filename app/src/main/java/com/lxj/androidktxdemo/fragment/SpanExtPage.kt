package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import com.blankj.utilcode.util.ShadowUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.base.WebActivity
import com.lxj.androidktx.core.*
import com.lxj.androidktx.util.QrCodeUtil
import com.lxj.androidktxdemo.DemoActivity
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_span_ext.*

/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
class SpanExtPage : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_span_ext

    override fun initView() {
        val str = "我是测试文字"

        // toSizeSpan
        tvSizeSpan.text = """
            tv.sizeSpan(str, 0..2)
        """.trimIndent()
        tvSizeResult.sizeSpan(str, 0..2).colorSpan(range = 0..1)

        // toColorSpan
        tvColorSpan.text = """
            tv.colorSpan(str, 2..6)
        """.trimIndent()
        tvColorResult.colorSpan(str, 2..6)

        // toBackgroundColorSpan
        tvBgColorSpan.text = """
            tv.backgroundColorSpan(str,2..6)
        """.trimIndent()
        tvBgColorResult.backgroundColorSpan(str, 2..6)

        // toStrikethrougthSpan
        tvStrikethrougthSpan.text = """
            tv.strikethrougthSpan(str,2..6)
        """.trimIndent()
        tvStrikethrougthResult.strikeThrougthSpan(str, 2..6)

        // clickSpan
        tvClickSpan.text = """
            tv.clickSpan(str, 2..6, listener)
        """.trimIndent()
        tvClickResult.clickSpan(str = str, range = 2..6, color = Color.BLUE, clickAction = {
            ToastUtils.showShort("哈哈我被点击了".toColorSpan(0..2))
            start<DemoActivity>()
            WebActivity.start(url = "https://www.baidu.com")
//            QrCodeUtil.start(this, 1)
//            PlayerActivity.start(url = "https://lxj-bama-happy.oss-cn-zhangjiakou.aliyuncs.com/e015e0a9-ed2d-47b6-9b70-e4feb263a09f.mp4",
//            title = "视频标题", cover = "https://lxj-bama-happy.oss-cn-zhangjiakou.aliyuncs.com/f156340b-d519-43a6-a714-1d24d3a37ed8.jpeg@s_0,w_460,h_260,q_80")
        })
        tvClickResult.click { ToastUtils.showShort("1111") }
//        tvClickResult.longClick {
//            ToastUtils.showShort("2222")
//            true
//        }


        tt.sizeDrawable(300)

        tvAppendResult.text = "演示一下appendXX方法的用法"
        tvAppendResult.appendSizeSpan("变大变大")
                .appendColorSpan("我要变色", color = Color.parseColor("#f0aafc"))
                .appendBackgroundColorSpan("我是有底色的", color = Color.parseColor("#cacee0"))
                .appendStrikeThrougthSpan("添加删除线哦哦哦哦")
                .appendClickSpan("来点我一下试试啊", isUnderlineText = true, clickAction = {
                    ToastUtils.showShort("哎呀，您点到我了呢，嘿嘿")
                })
                .appendStyleSpan("我是粗体的")
    }
}