package com.lxj.androidktxdemo.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.widget.SeekBar
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ShadowUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.base.WebActivity
import com.lxj.androidktx.core.*
import com.lxj.androidktx.share.Share
import com.lxj.androidktx.widget.SuperDrawable
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.ShadowDrawable
import kotlinx.android.synthetic.main.fragment_span_ext.*


/**
 * Description:
 * Create by lxj, at 2018/12/4
 */
class SpanExtPage : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_span_ext

    val video1 = "https://vdn.vzuu.com/HD/5538387e-1735-11eb-ba6b-627074da3f61.mp4?disable_local_cache=1&auth_key=1622029639-0-0-048e4615994dda73bd629bd1062eb6c0&f=mp4&bu=http-com&expiration=1622029639&v=ali"
    val video2 = "https://vdn1.vzuu.com/HD/84dd1e18-c128-11ea-aa63-1efae2afc434.mp4?disable_local_cache=1&auth_key=1622029974-0-0-e94da64385079e4f31c3f58a036a3c8f&f=mp4&bu=http-com&expiration=1622029974&v=hw"
    override fun initView() {
        val str = "我是测试文字"

        // toSizeSpan
        tvSizeSpan.text = """
            tv.sizeSpan(str, 0..2)
        """.trimIndent()
        tvSizeResult.sizeSpan(str, 0..2).colorSpan(range = 0..1)
        tvSizeSpan.setShadowLayer(1.6f,1.5f,1.3f,Color.BLACK)
        tvSizeSpan.click {
            WebActivity.start(url = "https://www.baidu.com", hideTitleBar = true, keepMarginTop = true,
            statusBarColor = Color.RED,  isLightStatusBar = false)
//            VideoPlayerActivity.start(url = "android.resource://" + requireContext()!!.packageName + "/" + R.raw.heng, title = "视频敖德萨大所大所大所大所大所多")
//            VideoPlayerActivity.start(url = video2, title = "视频敖德萨大所大所大所大所大所多")
        }

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
//            Share.wxLogin(requireActivity()!!,callback = object : Share.ShareCallback{
                WebActivity.start(url = "https://www.baidu.com", designHeight = 960)
//            })
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
        tvClickResult.click {
            if(tt.isVisible) tt.animateGone()
            else tt.animateVisible()
        }
//        tvClickResult.longClick {
//            ToastUtils.showShort("2222")
//            true
//        }


        tt.sizeDrawable(300)
//        tt.setBackgroundColor(Color.RED)
        var radius = 10
        var shadowSize = 10
        tt.setup(solid = Color.WHITE, shadowSize = shadowSize, corner = radius)
        tt.click {  }

        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                radius = progress
                tt.setup(solid = Color.WHITE, shadowSize = shadowSize, corner = radius)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                shadowSize = progress
                tt.setup(solid = Color.WHITE, shadowSize = shadowSize, corner = radius)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

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
            .setText("Lv 123", textColor = Color.WHITE, textSize = sp2px(13f).toFloat(),
            bold = true, typeface = Typeface.createFromAsset(requireContext()!!.assets,"FredokaOne-Regular.ttf"))
            .setLeftDrawable(R.mipmap._ktx_ic_clear, drawableWidth = dp2px(12f), drawableHeight = dp2px(12f),
            drawablePadding = dp2px(4f))
            .setRightDrawable(R.mipmap._ktx_ic_clear, drawableWidth = dp2px(12f), drawableHeight = dp2px(12f),
                drawablePadding = dp2px(4f))

        var image1 = ImageUtils.getBitmap(R.mipmap.ic_launcher, 40.dp, 40.dp)
        val td2 = SuperDrawable().setBgDrawable(R.mipmap.test, drawableWidth = dp2px(46f), drawableHeight = dp2px(15f))
        SpanUtils.with(tvDemo)
            .appendImage(image1, SpanUtils.ALIGN_CENTER)
            .appendImage(td, SpanUtils.ALIGN_CENTER)
//            .appendSpace(dp2px(8f))
//            .appendImage(td2, SpanUtils.ALIGN_CENTER)
            .appendSpace(dp2px(5f))
            .append("齐天大圣说：").setForegroundColor(Color.RED)
//            .appendSpace(dp2px(8f))
//            .appendImage(R.mipmap._ktx_ic_switch ,SpanUtils.ALIGN_CENTER)
                //一下动态Drawable的使用，适用于直播场景的消息展示
            .append("演示一下动态Drawable的使用，适用于直播场景的消息展示一下动态Drawable的使用，适用于直播场景的消息展示")
            .setForegroundColor(Color.parseColor("#cccccc"))

            .create()

    }

    override fun onBackClick(): Boolean {
        ToastUtils.showShort("onBackPressed - ${javaClass.simpleName}")
        return true
    }

}