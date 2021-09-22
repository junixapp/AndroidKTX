package com.lxj.androidktxdemo.fragment


import android.animation.ValueAnimator
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
            image2.setImageBitmap(text4.toBitmap())
        }
        text4.click { ToastUtils.showShort("clicked") }

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
                )){position->
            ToastUtils.showShort("选择了："+position)
            (0..3).forEach {
                LogUtils.e("posi: $it   -> $position")
                tabbar.getChildAt(it).background = if (position == it) createDrawable(
                    strokeWidth = 2.pt,
                    strokeColor = Color.parseColor("#FF4B76DB")
                ) else null
            }
            true
        }
        mtv.setup("床前明月光")
        mtv2.setup("床前明月光，疑是地上霜；举头望明月，低头思故乡。")

        val list = listOf(
            "http://sealbox.oss-ap-southeast-1.aliyuncs.com/upload/20210830/27f4e97272474d38299240aa2c6f5678.png",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic33.nipic.com%2F20130924%2F12085979_105431188100_2.jpg&refer=http%3A%2F%2Fpic33.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634003717&t=1d514ffc2294afa6a28c6f5df20702b7",
        )
        banner.adapter = CommonBannerAdapter(list, cornerRadius = 10.dp)
    }

    override fun initData() {
        super.initData()

//        mtv2.setup(loop = false)
        postDelay(2000){ mtv.startScroll()
            mtv2.startScroll()
            marqueeLayout.startScroll()
        }

        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.addUpdateListener {
            progressBar?.progress = 100f - it.animatedValue as Float
        }
        animator.setDuration(10000)
        animator.start()
    }

}