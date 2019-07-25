package com.lxj.androidktxdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.toast
import kotlinx.android.synthetic.main.demo.*

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
class DemoActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo)
        AppVM.data.observe(this, Observer{
            toast("it：$it")
        })


        btn.click {
            AppVM.data.postValue("第一次设置的值。。。")
        }

    }
}