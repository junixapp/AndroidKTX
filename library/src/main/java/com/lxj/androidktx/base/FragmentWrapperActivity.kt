package com.lxj.androidktx.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.R
import com.lxj.androidktx.core.replace

class FragmentWrapperActivity : TitleBarActivity() {

    companion object{
        fun start(title: String, fragmentName: String, bundle: Bundle? = null){
            val intent = Intent(AndroidKTX.context, FragmentWrapperActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("title", title)
                putExtra("fragment", fragmentName)
                putExtra("bundle", bundle)
            }
            if(fragmentName.isNullOrEmpty()) {
                ToastUtils.showShort("Fragment的名字不能为空")
                return
            }
            AndroidKTX.context.startActivity(intent)
        }
    }

    override fun getBodyLayout() = R.layout._ktx_activity_frag_wrapper

    override fun initData() {
        val bundle = intent?.getBundleExtra("bundle")
        titleBar().setup(title = intent.getStringExtra("title"), leftImageRes = R.mipmap._ktx_ic_back)
        replace(R.id.flWrapper, Fragment.instantiate(this, intent.getStringExtra("fragment"),
            bundle))
        LogUtils.e(bundle)
    }
}