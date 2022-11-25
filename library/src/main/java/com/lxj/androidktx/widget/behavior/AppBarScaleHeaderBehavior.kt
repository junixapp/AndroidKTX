package com.lxj.androidktx.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.appbar.AppBarLayout
import com.lxj.androidktx.core.*

/**
 * 让AppBar中的某个child有最大高度到最小高度限制的效果；并支持让header全屏展示效果，适用于视频播放场景
 * 注意点：
 * 1. child的tag必须是 ktx_appbar_scale_header
 * 2. 必须给child设置如下属性
        android:layout_height="200dp" //指定一个默认高度，可以动态设置
        app:layout_scrollFlags="scroll|exitUntilCollapsed"
 * 3. 将当前behavior设置给AppBarLayout，可以在布局设置，也可以在代码中动态设置：
 *    (appBar.layoutParams as CoordinatorLayout.LayoutParams).behavior = behavior
 * 4. 滚动源behavior一般是com.google.android.material.appbar.AppBarLayout$ScrollingViewBehavior
 */
class AppBarScaleHeaderBehavior(context: Context? = null, attrs: AttributeSet? = null) :
    AppBarLayout.Behavior(context, attrs) {

    private var maxHeight = 500.dp
    private var minHeight = 200.dp
    private var maxPadding = 0
    private var appbar: AppBarLayout? = null
    private var header: View? = null
    private var hasObserverOffset = false
    var enableScale = true //是否开启缩放
    var enableAppBarDrag = false //是否允许AppBar区域拖拽
    var isFullscreenHeader = false //header是否全屏显示
    private var paddingBeforeFullscreen = 0
    private var heightBeforeFullscreen = 0

    init {
        maxPadding = maxHeight - minHeight
        setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return enableAppBarDrag && !isFullscreenHeader && enableScale
            }
        })
    }

    fun getMaxHeight() = maxHeight
    fun getMinHeight() = minHeight

    fun setHeight(min: Int, max:Int){
        minHeight = min
        header?.minimumHeight = minHeight
        maxHeight = max
        maxPadding = maxHeight - minHeight
    }

    fun enableAppBarDrag(enable: Boolean){
        enableAppBarDrag = enable
    }

    /**
     * 是否开启缩放，如果关闭缩放，则AppBar将不会动
     */
    fun enableScale(enable: Boolean){
        enableScale = enable
    }

    /**
     * header是否开启全屏展示
     */
    fun fullscreenHeader(enable: Boolean){
        isFullscreenHeader = enable
        //如果header全屏，则隐藏AppBarLayout内除header外的其他View
        appbar?.children?.forEach {
            if(it!=header){
                it.visibleOrGone(!isFullscreenHeader)
            }
        }
        val act = context2Activity(appbar!!.context!!) ?: return
        if(isFullscreenHeader){
            ScreenUtils.setFullScreen(act)
            heightBeforeFullscreen = header?.measuredHeight?:0
            paddingBeforeFullscreen = header?.paddingTop ?:0
            header?.height(ScreenUtils.getAppScreenHeight())
            header?.setPadding(0,0,0,0)
        }else{
            ScreenUtils.setNonFullScreen(act)
            header?.height(heightBeforeFullscreen)
            header?.setPadding(0, paddingBeforeFullscreen,0,0)
        }
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        header = child.findViewWithTag("ktx_appbar_scale_header")
        header?.minimumHeight = minHeight
        appbar = child
        observerAppbarOffset(child)
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec,
            widthUsed, parentHeightMeasureSpec, heightUsed)
    }

    override fun setTopAndBottomOffset(offset: Int): Boolean {
        if(isFullscreenHeader || !enableScale) return true
        return super.setTopAndBottomOffset(offset)
    }

    private fun observerAppbarOffset(appBar: AppBarLayout){
        if (hasObserverOffset) return
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if(!enableScale) return
                if(!isFullscreenHeader){
                    val newP = MathUtils.clamp(-verticalOffset, 0, maxPadding)
                    header?.setPadding(0, newP, 0, 0)
                }else{
                    appBar.offsetTopAndBottom(-verticalOffset)
                    header?.setPadding(0, 0, 0, 0)
                }
            }
        })
        hasObserverOffset = true
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return !isFullscreenHeader && enableScale
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if(isFullscreenHeader || !enableScale)return
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if(isFullscreenHeader || !enableScale)return
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return isFullscreenHeader || !enableScale
    }

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return !isFullscreenHeader && enableScale
    }

}