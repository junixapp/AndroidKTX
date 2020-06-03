package com.lxj.androidktxdemo

import androidx.lifecycle.Observer
import com.lxj.androidktx.base.TitleBarActivity
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.toast
import kotlinx.android.synthetic.main.demo.*

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
class DemoActivity : TitleBarActivity(){
    override fun getBodyLayout() = R.layout.demo

    override fun initData() {
        titleBar().setup(title = "大萨达撒大多撒")
        AppVM.data.observe(this, Observer{
            toast("it：$it")
        })

        btn.click {
            AppVM.data.postValue("第一次设置的值。。。")
        }
    }
}