package com.lxj.androidktxdemo.fragment


import android.graphics.Color
import com.lxj.androidktx.core.*
import com.lxj.androidktx.widget.TabBar
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_view_ext.*

class ViewExtPage : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_view_ext

    override fun initView() {
        val value = dp2px(150f)
        text1.width(value)
        text1.text = "text1.width($value)"

        text2.widthAndHeight(value, value)
        text2.text = "text2.widthAndHeight($value,$value)"

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
                toast("保存成功")
            })
        }


        tvVerify.click {
            tvVerify.start()
        }

        tabbar.setTabs(listOf(
                TabBar.Tab("首页", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round),
                TabBar.Tab("购物", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round),
                TabBar.Tab("视频", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round),
                TabBar.Tab("我的", R.mipmap.ic_launcher, R.mipmap.ic_launcher_round)
                )){
            toast("选择了："+it)
            loge("select: " + it)
            true
        }
    }

}