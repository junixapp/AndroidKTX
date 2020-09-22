package com.lxj.androidktx.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lxj.androidktx.core.postDelay
import com.lxj.statelayout.StateLayout

/**
 * 自带StateLayout的Fragment基类
 */
abstract class StateFragment : Fragment() {
    protected var cacheView: View? = null
    protected var isInit = false
    protected var stateLayout: StateLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (cacheView == null) {
            cacheView = inflater.inflate(getLayoutId(), container, false)
            stateLayout = StateLayout(context!!).wrap(cacheView)
            onConfigStateLayout()
            stateLayout!!.showLoading()
        }
        return stateLayout!!
    }

    /**
     * 用来对StateLayout进行各种配置
     */
    open fun onConfigStateLayout(){

    }

    open fun showContent() = stateLayout?.showContent()
    open fun showLoading(showText: Boolean = true) = stateLayout?.showLoading(showText)
    open fun showError() = stateLayout?.showError()
    open fun showEmpty(noDataIconRes: Int = 0){
        if(noDataIconRes!=0){
            stateLayout?.showEmpty(noDataIconRes)
        }else{
            stateLayout?.showEmpty()
        }
    }

    //是否自动显示Content
    open fun autoShowContent() = false

    override fun onResume() {
        super.onResume()
        lazyInit()
    }

    private fun lazyInit() {
        if (cacheView != null && userVisibleHint && !isInit) {
            initView()
            initData()
            if(autoShowContent())postDelay(400){showContent()}
            isInit = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyInit()
    }

    //执行初始化，只会执行一次
    protected abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()

}