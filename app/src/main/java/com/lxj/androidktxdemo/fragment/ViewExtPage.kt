package com.lxj.androidktxdemo.fragment


import android.content.Intent
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.MainActivity
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_view_ext.*

class ViewExtPage: BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_view_ext

    override fun initView() {
        val value = dp2px(150f)
        text1.width(value)
        text1.text = "text1.width($value)"

        text2.widthAndHeight(value,value)
        text2.text = "text2.widthAndHeight($value,$value)"

        text3.margin(leftMargin = value)
        text3.text = "text3.margin(leftMargin = $value)"


        text4.post {
            image.setImageBitmap(text4.toBitmap())
        }

        text4.click {
//            startActivity(MainActivity::class, flag = Intent.FLAG_ACTIVITY_NEW_TASK, bundle = arrayOf(
//                    "a" to 1,
//                    "b" to "lala"
//            ))
            toast("w: ${windowWidth()} h:${windowHeight()}")
            "click just only 1 time in 350 ms".e()
//            text4.gone()
//            text4.isVisible.toString().v()

//            getString(R.string.app_name)
//            getStringArray(R.array.test)
//            getColor(R.id.color)
//            getDrawable(R.mipmap.ic_launcher)
//            getDimensionPx(R.dimen.abc)

            
        }


    }

}