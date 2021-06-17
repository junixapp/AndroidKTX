package com.lxj.androidktx.base

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment


/**
 * Description: 适用于ViewPager中的懒加载Fragment
 * Create by dance, at 2019/4/22
 */
abstract class PagerLazyFragment : Fragment() {
    protected var cacheView: View? = null
    protected var isInit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (cacheView == null) cacheView = inflater.inflate(getLayoutId(), container, false)
        return cacheView!!
    }

    override fun onResume() {
        super.onResume()
        lazyInit()
    }

    private fun lazyInit() {
        if (cacheView!=null && userVisibleHint && !isInit ) {
            initView()
            initData()
            isInit = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyInit()
        if(isInit){
            if(isVisibleToUser) onShow() else onHide()
        }
    }

    //执行初始化，只会执行一次
    protected abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
    open fun onShow(){}
    open fun onHide(){}
}