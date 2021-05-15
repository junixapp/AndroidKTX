package com.lxj.androidktxdemo

import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.lxj.androidktx.base.StateTitleBarActivity
import com.lxj.androidktx.core.asCard
import com.lxj.androidktx.core.bindFragment
import com.lxj.androidktx.core.click
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.android.synthetic.main.demo.*

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
class DemoActivity : StateTitleBarActivity(){
    override fun getBodyLayout() = R.layout.demo
    override fun autoShowContent(): Boolean {
        return true
    }
    var isFirst = true
    val loadingPopupView: LoadingPopupView by lazy { XPopup.Builder(this).asLoading("加载中") }
    override fun initData() {
        titleBar().setup(title = "大萨达撒大多撒", rightImageRes = R.mipmap.gengxingb)
//        AppVM.data.observe(this, Observer{
//            toast("it：$it")
//        })
//        pager2.adapter = object : EasyAdapter<Int>(listOf(1,1,1,1,1), R.layout.adapter_pager2){
//            override fun bind(holder: ViewHolder, t: Int, position: Int) {
//                (holder.itemView as TextView).text = "${position}"
//            }
//
//        }
//        pager2.isUserInputEnabled = false

        btnSwitchOrientation.text = "切换为${if(pager2.orientation==0) "Vertical" else "Horizontal"}"
        btnSwitchOrientation.click {
            if(pager2.orientation==ORIENTATION_HORIZONTAL) pager2.orientation = ORIENTATION_VERTICAL
            else pager2.orientation = ORIENTATION_HORIZONTAL
            btnSwitchOrientation.text = "切换为${if(pager2.orientation==0) "Horizontal" else "Vertical"}"
            pager2.bindFragment(this, listOf(DemoFragment(0), DemoFragment(1), DemoFragment(2)))
            pager2.asCard()
        }

        pager2.offscreenPageLimit = 1

        pager2.bindFragment(this, listOf(DemoFragment(0), DemoFragment(1), DemoFragment(2)))
        pager2.asCard()
    }

    override fun onConfigStateLayout() {
        super.onConfigStateLayout()
        stateLayout?.config()
    }

}