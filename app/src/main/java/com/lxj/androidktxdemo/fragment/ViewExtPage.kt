package com.lxj.androidktxdemo.fragment


import android.graphics.Color
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.player.VideoPlayerActivity
import com.lxj.androidktx.widget.TabBar
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.adapter_pager2.*
import kotlinx.android.synthetic.main.fragment_view_ext.*

class ViewExtPage : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_view_ext

    override fun initView() {
        LogUtils.e("ViewExtPage  initView")
        val value = dp2px(150f)
        text1.width(value)
        text1.text = "自定义字体：text1.width($value)"


        text2.widthAndHeight(value, value)
        text2.text = "text2.widthAndHeight($value,$value)"
//        text2.click { it.animateGone() }

        text3.margin(leftMargin = value)
        text3.text = "text3.margin(leftMargin = $value)"



        text3.click {
            it.animateWidthAndHeight(600, 900, action = {

            })
        }

        text4.post {
            image.setImageBitmap(text4.toBitmap())
        }

        text4.setBackgroundDrawable(createDrawable(color = Color.GREEN,
                strokeColor = Color.RED, strokeWidth = dp2px(2f),
                radius = dp2px(30f).toFloat(), enableRipple = true))

//        tvVerify.mSolid = Color.parseColor("#ff0000")
        tvVerify.click {
            tvVerify.start()
        }

        verifyCodeInput.onInputFinish = {
            ToastUtils.showLong(it)
        }
        tabbarRadio.setTabs(listOf(
            TabBar.Tab(text = "啊啊", selectedIconRes = R.mipmap.checked, normalIconRes = R.mipmap.uncheck),
            TabBar.Tab(text = "大萨达", selectedIconRes = R.mipmap.checked, normalIconRes = R.mipmap.uncheck),
            TabBar.Tab(text = "Free", selectedIconRes = R.mipmap.checked, normalIconRes = R.mipmap.uncheck),
        ))

        tabbar.setTabs(listOf(
                TabBar.Tab(text = "Home" ,),
                TabBar.Tab( text = "Category" ,),
                TabBar.Tab( text = "Message" ,),
                TabBar.Tab( text = "My" ,),
//                TabBar.Tab( selectedIconRes = R.mipmap.ic_launcher_round),
//                TabBar.Tab( selectedIconRes = R.mipmap.ic_launcher_round)
                )){
            ToastUtils.showShort("选择了："+it)
            true
        }
        mtv.setup("床前明月光")
        mtv2.setup("床前明月光，疑是地上霜；举头望明月，低头思故乡。")


    }

    override fun initData() {
        super.initData()

        postDelay(1500){ mtv.startScroll() }
        postDelay(1500){ mtv2.startScroll() }
        postDelay(1500){ marqueeLayout.startScroll() }
    }

}