package com.lxj.androidktxdemo.fragment

import android.graphics.*
import android.graphics.drawable.GradientDrawable
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.share.Share
import com.lxj.androidktx.widget.SuperDrawable
import com.lxj.androidktx.widget.SuperLayout
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
        tvSizeSpan.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK);

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
        Share.init(isDebug = true, umengAppKey = "5fadfcac43e9f56479c7262b",
                wxAppId = "wxd9f85e015cc9ed70", wxAppKey = "ff3b1319baf9adfa04d35093015cd693",
                qqAppId = "101909069", qqAppKey = "a5feb5c684a77bdeca609f71864cd526",
                weiboAppId = "1205214008", weiboAppKey = "159e9862228b99876da0a7d554835fbe", weiboCallbackUrl = "http://sg.qingjuyx.com"
        )
        // clickSpan
        tvClickSpan.text = """
            tv.clickSpan(str, 2..6, listener)
        """.trimIndent()
        tvClickResult.clickSpan(str = str, range = 2..6, color = Color.BLUE, clickAction = {
            ToastUtils.showShort("哈哈我被点击了".toColorSpan(0..2))
            Share.wxLogin(activity!!,callback = object : Share.ShareCallback{

            })
//            Share.shareImage(activity!!,platform = SharePlatform.QQ, bitmap = tvClickSpan.toBitmap(), cb = object : Share.ShareCallback{
//
//            })
//            Share.shareWeb(activity!!,platform = SharePlatform.QQ,
//                    url = "https://www.baidu.com", title = "三生三世",
//                    thumbRes = R.mipmap.ic_launcher, cb = object : Share.ShareCallback{
//
//            })
//            Share.sinaLogin(activity!!, callback = object : Share.ShareCallback{
//
//            })
//            start<DemoActivity>()
//            WebActivity.start(url = "https://www.baidu.com")
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

        val td = SuperDrawable()
            .setPadding(left = dp2px(8f), right = dp2px(8f), top = dp2px(4f), bottom = dp2px(4f))
            .setBg(bgRadius = dp2px(30f).toFloat(), gradientColors = intArrayOf(Color.RED, Color.BLUE),
            gradientOrientation = GradientDrawable.Orientation.TL_BR)
            .setText("Lv 12345", textColor = Color.WHITE, textSize = sp2px(13f).toFloat(),
            bold = true, typeface = Typeface.createFromAsset(context!!.assets,"FredokaOne-Regular.ttf"))
            .setLeftDrawable(R.mipmap._ktx_ic_clear, drawableWidth = dp2px(12f), drawableHeight = dp2px(12f),
            drawablePadding = dp2px(4f))
            .setRightDrawable(R.mipmap._ktx_ic_clear, drawableWidth = dp2px(12f), drawableHeight = dp2px(12f),
                drawablePadding = dp2px(4f))

        val td2 = SuperDrawable().setBgDrawable(R.mipmap.test, drawableWidth = dp2px(46f), drawableHeight = dp2px(15f))
        SpanUtils.with(tvDemo)
            .appendImage(td, SpanUtils.ALIGN_CENTER)
            .append("齐天大圣")
            .appendImage(td2, SpanUtils.ALIGN_CENTER)
            .appendSpace(dp2px(10f))
            .appendImage(R.mipmap._ktx_ic_switch)
            .append("齐天大圣说：")
            .append("哒哒哒哒哒所大所多所多撒大所大所大所大所大所大所大所大所大所").setForegroundColor(Color.parseColor("#cccccc"))
            .create()

    }
}