package com.lxj.androidktxdemo.fragment

import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_temp.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class TempFragment: BaseFragment(){
    companion object {
        @JvmStatic
        val Key1 = "Key1"
        val Key2 = "Key2"
    }

    override fun getLayoutId() = R.layout.fragment_temp

    override fun initView() {
        content.text = """
            $Key1 => ${requireArguments()!!.get(Key1)}
            $Key2 => ${requireArguments()!!.get(Key2)}
        """.trimIndent()
    }
}