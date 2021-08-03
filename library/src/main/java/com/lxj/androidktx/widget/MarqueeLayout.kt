package com.lxj.androidktx.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.LinearInterpolator

/**
 * 支持一段布局进行滚动的布局，
 * 有且只能有一个child
 */
class MarqueeLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeFrameLayout(context, attributeSet, defStyleAttr) {

    private var mLoop : Boolean = true
    private var mSlow: Float = 1.0f //缓慢系数，越大越慢
    private var mScrollDelay = 400L

    init {
        clipChildren = true
        clipToPadding = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true
        }
        setWillNotDraw(false)
    }

    fun setupSelf(loop: Boolean? = null, slow: Float? = null, scrollDelay: Long? = null){
        if(loop!=null) mLoop = loop
        if(slow!=null) mSlow = slow
        if(scrollDelay!=null) mScrollDelay = scrollDelay
    }

    private var scrolling = false
    private val mHandler = Handler(Looper.getMainLooper())
    private var first = true
    private var animator: ValueAnimator? = null
    var onMoveEnd: ((Long) -> Unit)? = null
    fun startScroll() {
        if (scrolling) return
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            innerScroll()
        }, mScrollDelay)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        getChildAt(0)?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED),
            heightMeasureSpec
        )
    }

    fun canScroll() = getChildWidth() > measuredWidth

    private fun innerScroll() {
        val childWidth = getChildWidth()
        if (childWidth <= measuredWidth) return
        scrolling = true
        animator?.removeAllListeners()
        val distance = if (mLoop) (childWidth).toInt() else (childWidth - measuredWidth).toInt()
        animator = ValueAnimator.ofInt(if (first) 0 else -measuredWidth, distance)
        animator!!.interpolator = LinearInterpolator()
        animator!!.addUpdateListener {
            scrollTo(it.animatedValue as Int, 0)
        }
        animator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                first = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                scrolling = false
                animator!!.removeAllListeners()
                if (mLoop) {
                    scrollTo(-measuredWidth, 0)
                    innerScroll()
                } else {
                    onMoveEnd?.invoke(animation?.duration?:0L)
                }
            }
        })
        //速度：假设整个View的宽度滚动完需要8秒，得出每秒滚动多少px
        val speed = measuredWidth / 8
        if(speed==0) return
        animator!!.duration = ((distance / speed) * 1000 * mSlow).toLong()
        animator!!.start()
    }


    fun getChildWidth() = getChildAt(0).measuredWidth + paddingStart + paddingEnd

    fun stopScroll() {
        mHandler.removeCallbacksAndMessages(null)
        animator?.removeAllListeners()
        animator?.end()
        scrollTo(0, 0)
        scrolling = false
        first = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopScroll()
    }

}

