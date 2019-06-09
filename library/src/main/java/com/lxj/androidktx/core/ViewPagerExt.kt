package com.lxj.androidktx.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * Description: ViewPager相关
 * Create by dance, at 2019/5/23
 */

/**
 * 给ViewPager绑定数据
 */
fun ViewPager.bind(count: Int, bindView: (container: ViewGroup, position: Int) -> View): ViewPager {
    adapter = object : PagerAdapter() {
        override fun isViewFromObject(v: View, p: Any) = v == p
        override fun getCount() = count
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = bindView(container, position)
            container.addView(view)
            return view
        }
        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
    return this
}

/**
 * 给ViewPager绑定Fragment
 */
fun ViewPager.bindFragment(fm: FragmentManager, fragments: List<Fragment>, pageTitles: List<String>? = null): ViewPager {
    adapter = object : FragmentPagerAdapter(fm) {
        override fun getItem(p: Int) = fragments[p]
        override fun getCount() = fragments.size
        override fun getPageTitle(p: Int) = if(pageTitles==null) null else pageTitles[p]
    }
    return this
}

/**
 * 让ViewPager展示卡片效果
 * @param pageMargin 用来调节卡片之间的距离
 * @param padding 用来调节ViewPager的padding
 */
fun ViewPager.asCard(pageMargin: Int = dp2px(30.toFloat()), padding: Int = dp2px(45.toFloat())): ViewPager {
    setPageTransformer(false, CardPagerTransformer(context))
    setPageMargin(pageMargin)
    clipToPadding = false
    setPadding(padding, padding, padding, padding)
    return this
}

class CardPagerTransformer(context: Context) : ViewPager.PageTransformer {
    private val maxTranslateOffsetX: Int = context.dp2px(180f)
    private var viewPager: ViewPager? = null

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }
        val leftInScreen = view.left - viewPager!!.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.38f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)
        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }
}
