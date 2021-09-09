package com.lxj.androidktx.widget

/**
 * Description:
 * Create by dance, at 2019/7/8
 */
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*

class TabBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    var isSelectBold = false
    var tabWidthEqual = true
    var iconWidth = 20.dp
    var iconHeight = 20.dp
    var iconPosition = 1
    var selectedColor = Color.RED
    var normalColor = Color.BLACK
    var selectedBgColor = 0
    var normalBgColor = 0
    var normalTextSize = 14.sp
    var selectTextSize = 14.sp
    var tabHeight = 0
    var iconSpace = 2.dp //图片和文字间距
    var tabSpace = 0  //Tab之间的间距
    var mTabs = listOf<Tab>()
    var tabIndex = -1
    var tabPadding = 0
    var typefacePath: String? = null
    private var customTypeface: Typeface? = null

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
        selectedBgColor = ta.getColor(R.styleable.TabBar_tb_selectedBgColor, selectedBgColor)
        normalBgColor = ta.getColor(R.styleable.TabBar_tb_normalBgColor, normalBgColor)
        tabHeight = ta.getDimension(R.styleable.TabBar_tb_tabHeight, tabHeight.toFloat()).toInt()
        tabPadding = ta.getDimensionPixelSize(R.styleable.TabBar_tb_tabPadding, tabPadding)
        tabSpace = ta.getDimensionPixelSize(R.styleable.TabBar_tb_tabSpace, tabSpace)
        tabWidthEqual = ta.getBoolean(R.styleable.TabBar_tb_tabWidthEqual, tabWidthEqual)
        typefacePath = ta.getString(R.styleable.TabBar_tb_typefacePath)
        if(!typefacePath.isNullOrEmpty()) customTypeface = Typeface.createFromAsset(context.assets, typefacePath)

        ta.recycle()
        orientation = HORIZONTAL
    }

    var mTabChangeListener: ((index: Int) -> Boolean)? = null
    fun setTabs(tabs: List<Tab>, defSelectFirst : Boolean = true, tabChangeListener: ((index: Int) -> Boolean)? = null): TabBar {
        mTabs = tabs
        mTabChangeListener = tabChangeListener
        mTabs.forEachIndexed { index, it ->
            val lp = LayoutParams(if(tabWidthEqual) 0 else LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            if(tabWidthEqual) lp.weight = 1f
            val wrapper = LinearLayout(context)
            wrapper.gravity = Gravity.CENTER
            if(index!=mTabs.lastIndex) lp.marginEnd = tabSpace
            if(tabPadding>0){
                wrapper.setPadding(tabPadding, tabPadding, tabPadding, tabPadding)
            }
            addView(wrapper, lp)

            wrapper.addView(ShapeTextView(context).apply {
                setup(enableRipple = false)
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                compoundDrawablePadding = iconSpace
                text = it.text
                setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize.toFloat())
                setTextColor(normalColor)
                if(!typefacePath.isNullOrEmpty()) {
                    typeface = customTypeface
                }else{
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
                if (tabHeight != 0) height(tabHeight)
                when (iconPosition) {
                    0 -> sizeDrawable(width = iconWidth, height = iconHeight, leftDrawable = mTabs[index].normalIconRes)
                    1 -> sizeDrawable(width = iconWidth, height = iconHeight, topDrawable = mTabs[index].normalIconRes)
                    2 -> sizeDrawable(width = iconWidth, height = iconHeight, rightDrawable = mTabs[index].normalIconRes)
                    3 -> sizeDrawable(width = iconWidth, height = iconHeight, bottomDrawable = mTabs[index].normalIconRes)
                }
            }, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            wrapper.apply {
//                val typedValue = TypedValue()
//                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
//                setBackgroundResource(typedValue.resourceId)
                click {
                    selectTab(index)
                }
            }
        }
        if(defSelectFirst)selectTab(0)
        return this
    }

    var vp: ViewPager? = null
    var vp2: ViewPager2? = null

    var pager2ChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(p: Int) {
            selectTab(p)
        }
    }
    var pagerChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(p: Int) {
            selectTab(p)
        }
    }
    fun setupWithViewPager(pager: ViewPager) {
        vp = pager
        pager.addOnPageChangeListener(pagerChangeListener)
    }
    fun setupWithViewPager2(pager2: ViewPager2) {
        vp2 = pager2
        pager2.registerOnPageChangeCallback(pager2ChangeListener)
    }
    fun selectTab(index: Int) {
        if (mTabChangeListener != null && !mTabChangeListener!!(index)) return
        if (tabIndex == index) return
        tabIndex = index
        vp?.currentItem = tabIndex
        vp2?.currentItem = tabIndex
        children.forEachIndexed { i, p ->
            val group = p as ViewGroup
            if(selectedBgColor!=0 || normalBgColor!=0){
                group.setBackgroundColor(if (index == i) selectedBgColor else normalBgColor)
            }
            (group.getChildAt(0) as TextView).apply {
                val icon = if(index == i) mTabs[i].selectedIconRes else mTabs[i].normalIconRes
                when (iconPosition) {
                    0 -> sizeDrawable(width = iconWidth, height = iconHeight, leftDrawable = icon)
                    1 -> sizeDrawable(width = iconWidth, height = iconHeight, topDrawable = icon)
                    2 -> sizeDrawable(width = iconWidth, height = iconHeight, rightDrawable = icon)
                    3 -> sizeDrawable(width = iconWidth, height = iconHeight, bottomDrawable = icon)
                }
                setTextColor(if (index == i) selectedColor else normalColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, if (index == i) selectTextSize.toFloat() else normalTextSize.toFloat())
                if(!typefacePath.isNullOrEmpty()) {
                    typeface = customTypeface
                }
                paint.isFakeBoldText = isSelectBold && index == i
            }
        }
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        vp?.removeOnPageChangeListener(pagerChangeListener)
        vp2?.unregisterOnPageChangeCallback(pager2ChangeListener)
    }

    data class Tab(
            var text: String? = "",
            var normalIconRes: Int = 0,
            var selectedIconRes: Int = 0
    )
}