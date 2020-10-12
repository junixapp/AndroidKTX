package com.lxj.androidktx.widget

/**
 * Description:
 * Create by dance, at 2019/7/8
 */
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*

class TabBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    var isSelectBold = false
    var iconWidth = dp2px(20f)
    var iconHeight = dp2px(20f)
    var iconPosition = 1
    var selectedColor = Color.RED
    var normalColor = Color.BLACK
    var normalTextSize = sp2px(14f)
    var selectTextSize = sp2px(14f)
    var tabHeight = 0
    var iconSpace = dp2px(2f) //图片和文字间距
    var mTabs = listOf<Tab>()
    var tabIndex = -1
    var tabPadding = 0

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.TabBar)
        iconPosition = ta.getInt(R.styleable.TabBar_tb_iconPosition, iconPosition)
        isSelectBold = ta.getBoolean(R.styleable.TabBar_tb_isSelectBold, false)
        val iconSize = ta.getDimensionPixelSize(R.styleable.TabBar_tb_iconSize, 0)
        iconWidth = ta.getDimensionPixelSize(R.styleable.TabBar_tb_iconWidth, iconWidth)
        iconHeight = ta.getDimensionPixelSize(R.styleable.TabBar_tb_iconHeight, iconHeight)
        if (iconSize != 0) {
            iconWidth = iconSize
            iconHeight = iconSize
        }

        val mTextSize = ta.getDimensionPixelSize(R.styleable.TabBar_tb_textSize, 0)
        normalTextSize = ta.getDimensionPixelSize(R.styleable.TabBar_tb_normalTextSize, normalTextSize)
        selectTextSize = ta.getDimensionPixelSize(R.styleable.TabBar_tb_selectTextSize, selectTextSize)
        if(mTextSize!=0) {
            normalTextSize = mTextSize
            selectTextSize = mTextSize
        }

        iconSpace = ta.getDimensionPixelSize(R.styleable.TabBar_tb_iconSpace, iconSpace)
        selectedColor = ta.getColor(R.styleable.TabBar_tb_selectedColor, selectedColor)
        normalColor = ta.getColor(R.styleable.TabBar_tb_normalColor, normalColor)
        tabHeight = ta.getDimension(R.styleable.TabBar_tb_tabHeight, tabHeight.toFloat()).toInt()
        tabPadding = ta.getDimensionPixelSize(R.styleable.TabBar_tb_tabPadding, tabPadding)

        ta.recycle()
        orientation = HORIZONTAL
    }

    var mTabChangeListener: ((index: Int) -> Boolean)? = null
    fun setTabs(tabs: List<Tab>, tabChangeListener: ((index: Int) -> Boolean)? = null): TabBar {
        mTabs = tabs
        mTabChangeListener = tabChangeListener
        mTabs.forEachIndexed { index, it ->
            val lp = LayoutParams(0, LayoutParams.MATCH_PARENT)
            lp.weight = 1f
            val wrapper = LinearLayout(context)
            wrapper.gravity = Gravity.CENTER
            if(tabPadding>0){
                wrapper.setPadding(if(iconPosition==0) tabPadding else 0,
                        if(iconPosition==1) tabPadding else 0,
                        if(iconPosition==2) tabPadding else 0,
                        if(iconPosition==3) tabPadding else 0)
            }
            addView(wrapper, lp)

            wrapper.addView(ShapeTextView(context).apply {
                enableRipple = false
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                compoundDrawablePadding = iconSpace
                text = it.text
                setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize.toFloat())
                setTextColor(normalColor)
                if (tabHeight != 0) height(tabHeight)
            }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            wrapper.apply {
                val typedValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
                setBackgroundResource(typedValue.resourceId)
                click {
                    selectTab(index)
                }
            }
        }
        selectTab(0)
        return this
    }

    var vp: ViewPager? = null
    var pagerChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(p: Int) {
            selectTab(p)
        }
    }
    fun setupWithViewPager(pager: ViewPager) {
        vp = pager
        pager.addOnPageChangeListener(pagerChangeListener)
    }

    fun selectTab(index: Int) {
        if (mTabChangeListener != null && !mTabChangeListener!!(index)) return
        if (tabIndex == index) return
        tabIndex = index
//        vp?.currentItem = tabIndex
        vp?.setCurrentItem(tabIndex, false)
        children.forEachIndexed { i, p ->
            val group = p as ViewGroup
            (group.getChildAt(0) as TextView).apply {
                val icon = if(index == i) mTabs[i].selectedIconRes else mTabs[i].normalIconRes
                when (iconPosition) {
                    0 -> sizeDrawable(width = iconWidth, height = iconHeight, leftDrawable = icon)
                    1 -> sizeDrawable(width = iconWidth, height = iconHeight, topDrawable = icon)
                    2 -> sizeDrawable(width = iconWidth, height = iconHeight, rightDrawable = icon)
                    3 -> sizeDrawable(width = iconWidth, height = iconHeight, bottomDrawable = icon)
                }
                setTextColor(if(index == i) selectedColor else normalColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, if(index == i) selectTextSize.toFloat() else normalTextSize.toFloat())
                typeface = if (isSelectBold && index == i) {
                    Typeface.defaultFromStyle(Typeface.BOLD)
                } else  {
                    Typeface.defaultFromStyle(Typeface.NORMAL)
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        vp?.removeOnPageChangeListener(pagerChangeListener)
    }

    data class Tab(
            var text: String? = "",
            var normalIconRes: Int = 0,
            var selectedIconRes: Int = 0
    )
}