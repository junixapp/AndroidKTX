package com.lxj.androidktxdemo.fragment


import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_view_ext.*

class ViewExtPage: BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_view_ext

    override fun initView() {
        val value = context!!.dp2px(150f)
        text1.width(value)
        text1.text = "text1.width($value)"

        text2.widthAndHeight(value,value)
        text2.text = "text2.widthAndHeight($value,$value)"

        text3.margin(leftMargin = value)
        text3.text = "text3.margin(leftMargin = $value)"

        text4.post {
            image.setImageBitmap(text4.toBitmap())
        }

    }

}