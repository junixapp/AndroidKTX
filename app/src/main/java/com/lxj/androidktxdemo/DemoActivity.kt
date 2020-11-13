package com.lxj.androidktxdemo

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.base.StateTitleBarActivity
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.observeState
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.android.synthetic.main.demo.*

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
class DemoActivity : StateTitleBarActivity(){
    override fun getBodyLayout() = R.layout.demo

    var isFirst = true
    val loadingPopupView: LoadingPopupView by lazy { XPopup.Builder(this).asLoading("加载中") }
    override fun initData() {
        titleBar().setup(title = "大萨达撒大多撒")
//        AppVM.data.observe(this, Observer{
//            toast("it：$it")
//        })
        loadingPopupView.observeState(this, AppVM.data, autoShowError = true, onSuccess = {
            ToastUtils.showShort("it：${ AppVM.data.value}")
        })

        btn.click {
            if(isFirst){
                AppVM.data.postError("错误信息。。。")
                isFirst = false
            }else{

                AppVM.data.postValueAndSuccess("第一次设置的值。。。")
            }
        }
    }

    override fun onConfigStateLayout() {
        super.onConfigStateLayout()
        stateLayout?.config()
    }

}