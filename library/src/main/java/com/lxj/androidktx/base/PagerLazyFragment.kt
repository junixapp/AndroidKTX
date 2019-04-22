package com.lxj.androidktx.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


/**
 * Description: 使用ViewPager中的懒加载Fragment
 * Create by dance, at 2019/4/22
 */
abstract class PagerLazyFragment : Fragment() {
    protected var cacheView: View? = null
    protected var isInit = false

    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (cacheView == null) cacheView = inflater.inflate(layoutId, container, false)
        return cacheView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyInit()
    }

    private fun lazyInit() {
        if (cacheView!=null && userVisibleHint && !isInit ) {
            isInit = true
            init(cacheView!!)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyInit()
    }

    //执行初始化，只会执行一次
    abstract fun init(view: View)
}