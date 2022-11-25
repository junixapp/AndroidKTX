package com.lxj.androidktxdemo.blibli

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.androidktx.base.BaseActivity
import com.lxj.androidktx.core.*
import com.lxj.androidktx.widget.behavior.AppBarScaleHeaderBehavior
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.activity_blibli_video.*

class BlibliVideoActivity: BaseActivity() {

    override fun getLayoutId() = R.layout.activity_blibli_video
    val behavior = AppBarScaleHeaderBehavior()
    override fun initView() {
        viewPager.bindFragment(
            fm = supportFragmentManager, fragments = listOf(BlibliFragment(), BlibliFragment()),
            pageTitles = listOf("评论", "相关产品"),
        )
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                appBar.setExpanded(false, true)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })

        tvScale.click {
            if (behavior.isFullscreenHeader){
                ScreenUtils.setPortrait(this)
                behavior.fullscreenHeader(false)
                toast("恢复正常播放")
            }else{
                ScreenUtils.setLandscape(this)
                behavior.fullscreenHeader(true) //禁用padding更新
                toast("模拟视频全屏播放")
            }
        }
        tvEnableDrag.click {
            behavior.enableAppBarDrag(!behavior.enableAppBarDrag)
            toast("AppBar区域${if(behavior.enableAppBarDrag) "可以" else "禁用"}拖拽")
        }
        (appBar.layoutParams as CoordinatorLayout.LayoutParams).behavior = behavior
        tvHideTab.click {
            tabLayout.visible(!tabLayout.isVisible)
        }
    }

    override fun initData() {}

    override fun onBackPressed() {
        if(behavior.isFullscreenHeader){
            tvScale.performClick()
            return
        }
        super.onBackPressed()
    }
}