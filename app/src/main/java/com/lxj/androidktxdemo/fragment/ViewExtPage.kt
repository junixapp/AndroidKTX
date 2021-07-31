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

        text4.click {
            //            startActivity<MainActivity>(bundle = arrayOf(
//                    "a" to 1,
//                    "b" to "lala"
//            ))

//            startActivity<MainActivity>(flag = Intent.FLAG_ACTIVITY_CLEAR_TOP, bundle = arrayOf(
//                    "a" to 1,
//                    "b" to "lala"
//            ))
//            applicitionCtx.startActivity<MainActivity>(bundle = arrayOf(
//                    "a" to 1,
//                    "b" to "lala"
//            ))
            "click just only 1 time in 350 ms".loge()
//            text4.gone()
//            text4.isVisible.toString().v()

//            getString(R.string.app_name)
//            getStringArray(R.array.test)
//            getColor(R.id.color)
//            getDrawable(R.mipmap.ic_launcher)
//            getDimensionPx(R.dimen.abc)


        }

        text4.setBackgroundDrawable(createDrawable(color = Color.GREEN,
                strokeColor = Color.RED, strokeWidth = dp2px(2f),
                radius = dp2px(30f).toFloat(), enableRipple = true))



        saveToAlbum.click {
            text4.toBitmap().saveToAlbum(callback = { path, uri ->
                ToastUtils.showShort("保存成功")
            })
        }


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
    }

}