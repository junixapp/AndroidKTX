package com.lxj.androidktx.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

/**
 * Description:
 * Create by dance, at 2019/5/16
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
    }

    private var lastBackPressTime = 0L
    /**
     * 两次返回退出Activity
     */
    protected fun doubleBackToFinish(duration: Long = 2000, toast: String = "再按一次退出") {
        if (!FragmentUtils.dispatchBackPress(supportFragmentManager)) {
            if(System.currentTimeMillis() - lastBackPressTime < duration){
                ToastUtils.cancel()
                super.onBackPressed()
            }else{
                lastBackPressTime = System.currentTimeMillis()
                ToastUtils.showShort(toast)
            }
        }
    }
    override fun onBackPressed() {
        if (!FragmentUtils.dispatchBackPress(supportFragmentManager)) {
            super.onBackPressed()
        }
    }
}