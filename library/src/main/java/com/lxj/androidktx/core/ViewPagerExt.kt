package com.lxj.androidktx.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


/**
 * Description: ViewPager相关
 * Create by dance, at 2019/5/23
 */

/**
 * 给ViewPager绑定数据
 */
fun ViewPager.bind(
    count: Int, bindView: (container: ViewGroup, position: Int) -> View,
    pageTitles: List<String>? = null
): ViewPager {
    offscreenPageLimit = count
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

        override fun getPageTitle(position: Int) =
            if (pageTitles == null) null else pageTitles[position]
    }
    return this
}

/**
 * 给ViewPager绑定Fragment
 */
fun ViewPager.bindFragment(
    fm: FragmentManager,
    fragments: List<Fragment>,
    pageTitles: List<String>? = null
): ViewPager {
    offscreenPageLimit = fragments.size
    adapter = object : FragmentPagerAdapter(fm) {
        override fun getItem(p: Int) = fragments[p]
        override fun getCount() = fragments.size
        override fun getPageTitle(p: Int) = if (pageTitles == null) null else pageTitles[p]
    }
    return this
}

/**
 * 让ViewPager展示卡片效果
 * @param pageMargin 用来调节卡片之间的距离
 * @param padding 用来调节ViewPager的padding
 */
fun ViewPager.asCard(
    pageMargin: Int = 30.dp,
    padding: Int = 45.dp
): ViewPager {
    setPageTransformer(false, CardPagerTransformer(context))
    setPageMargin(pageMargin)
    clipToPadding = false
    setPadding(padding, padding, padding, padding)
    return this
}

class CardPagerTransformer(context: Context) : ViewPager.PageTransformer {
    private val maxTranslateOffsetX: Int = 180.dp
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

/**
 * 给ViewPager2绑定Fragment
 */
fun ViewPager2.bindFragment(act: FragmentActivity, fragments: List<Fragment>): ViewPager2 {
    offscreenPageLimit = fragments.size
    adapter = object : FragmentStateAdapter(act) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
    return this
}
fun ViewPager2.bindFragment(frag: Fragment, fragments: List<Fragment>): ViewPager2 {
    offscreenPageLimit = fragments.size
    adapter = object : FragmentStateAdapter(frag) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
    return this
}

fun ViewPager2.bindTabLayout(tabLayout: TabLayout, titles: List<String>): ViewPager2 {
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = titles[position]
    }.attach()
    return this
}


/**
 * 让ViewPager展示卡片效果
 * @param pageMargin 用来调节卡片之间的距离
 * @param padding 用来调节ViewPager的padding
 */
fun ViewPager2.asCard(
    pageMargin: Int = 10f.dp, padding: Int = 45.dp,
    offsetVal: Int = 150.dp,
    scaleRatio: Float = 0.3f
): ViewPager2 {
    setPageTransformer(CardPager2Transformer(context, orientation = orientation,
    offsetVal = offsetVal, scaleRatio = scaleRatio, pageMargin = pageMargin))
    clipToPadding = false
    clipChildren = false
    setPadding(padding, padding, padding, padding)
    return this
}

/**
 * @param offsetVal page偏移出来的位置
 * @param scaleRatio page缩小到原来的多少倍
 * @param pageMargin page之间的间距
 */
class CardPager2Transformer(
    context: Context, var orientation: Int = ViewPager2.ORIENTATION_HORIZONTAL,
    var offsetVal: Int = 150.dp,
    var scaleRatio: Float = 0.3f, var pageMargin : Int = 10.dp
) : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        if(view.marginTop==0)view.margin(pageMargin, pageMargin, pageMargin, pageMargin)
        if(orientation == ViewPager2.ORIENTATION_HORIZONTAL){
            val leftInScreen = view.left - view.scrollX
            val centerXInViewPager = leftInScreen + view.measuredWidth / 2
            val offsetX = centerXInViewPager - view.measuredWidth / 2
            val offsetRate = offsetX.toFloat() * scaleRatio / view.measuredWidth
            val scaleFactor = 1 - Math.abs(offsetRate)
            if (scaleFactor > 0) {
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.translationX = -offsetVal * offsetRate
            }
        }else{
            val topInScreen = view.top - view.scrollY
            val centerYInViewPager = topInScreen + view.measuredHeight / 2
            val offsetY = centerYInViewPager - view.measuredHeight / 2
            val offsetRate = offsetY.toFloat() * scaleRatio / view.measuredHeight
            val scaleFactor = 1 - Math.abs(offsetRate)
            if (scaleFactor > 0) {
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.translationY = -offsetVal * offsetRate
            }
        }
    }
}
