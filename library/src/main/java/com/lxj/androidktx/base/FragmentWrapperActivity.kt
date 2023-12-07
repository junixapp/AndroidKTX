package com.lxj.androidktx.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.R
import com.lxj.androidktx.core.replace


/**
 * 将Fragment包裹为Activity
 */
class FragmentWrapperActivity : TitleBarActivity() {

    companion object{
        fun start(act: Activity?, fragmentName: String, title: String? = null, bundle: Bundle? = null,
                  landscape: Boolean = false){
            if(act==null) return
            if(fragmentName.isNullOrEmpty()) {
                ToastUtils.showShort("fragmentName can not be null or empty")
                return
            }
            val intent = Intent(AndroidKTX.context, FragmentWrapperActivity::class.java).apply {
                if(!title.isNullOrEmpty()) putExtra("title", title)
                if(bundle!=null) putExtra("bundle", bundle)
                putExtra("fragment", fragmentName)
                putExtra("landscape", landscape)
            }
            act.startActivity(intent)
        }
    }

    override fun getBodyLayout() = R.layout._ktx_activity_frag_wrapper

    override fun initData() {
        val bundle = intent?.getBundleExtra("bundle")
        val title = intent.getStringExtra("title")
        val landscape = intent.getBooleanExtra("landscape", false)
        if(landscape){
            ScreenUtils.setLandscape(this)
        }
        if(title.isNullOrEmpty()){
            hideTitleBar()
        }else{
            titleBar.setup(title = title, leftImageRes = R.mipmap._ktx_ic_back)
        }
        replace(R.id.flWrapper, Fragment.instantiate(this, intent.getStringExtra("fragment")?:"",
            bundle))
        LogUtils.e(bundle)
    }
}