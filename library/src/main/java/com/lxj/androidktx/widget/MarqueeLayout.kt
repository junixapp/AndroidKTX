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
import com.lxj.androidktx.R

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
    private var mEnableFadeEdge : Boolean = true
    private var mDuration : Int = 0 //如果指定时间，则固定速度
    private var mSpeed : Float = 1f //指定速度的缓慢比例，越大越慢
    private var mScrollDelay = 400L

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MarqueeLayout)

        mLoop = ta.getBoolean(R.styleable.MarqueeLayout_ml_loop, mLoop)
        mEnableFadeEdge = ta.getBoolean(R.styleable.MarqueeLayout_ml_enableFadeEdge, mEnableFadeEdge)
        mDuration = ta.getInteger(R.styleable.MarqueeLayout_ml_duration, mDuration)
        mSpeed = ta.getFloat(R.styleable.MarqueeLayout_ml_speed, mSpeed)
        mScrollDelay = ta.getInteger(R.styleable.MarqueeLayout_ml_scrollDelay, 400).toLong()
        ta.recycle()
        clipChildren = true
        clipToPadding = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true
        }
        isHorizontalFadingEdgeEnabled = true
        setWillNotDraw(false)
        
    }

    fun setupSelf(loop: Boolean? = null, duration: Int? = null, scrollDelay: Long? = null,
                  enableFadeEdge: Boolean? = null, speed: Float? = null){
        if(loop!=null) mLoop = loop
        if(duration!=null) mDuration = duration
        if(enableFadeEdge!=null) mEnableFadeEdge = enableFadeEdge
        if(scrollDelay!=null) mScrollDelay = scrollDelay
        if(speed!=null) mSpeed = speed
    }

    private var scrolling = false
    private val mHandler = Handler(Looper.getMainLooper())
    private var first = true
    var animator: ValueAnimator? = null
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

    override fun isPaddingOffsetRequired() = mEnableFadeEdge && canScroll() && animator?.isRunning==true
    override fun getRightFadingEdgeStrength() = if(isPaddingOffsetRequired) 0.5f else 0f
    override fun getLeftFadingEdgeStrength() = if(mEnableFadeEdge && canScroll()) 0.5f else 0f

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
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                first = false
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                scrolling = false
                animator!!.removeAllListeners()
            }
            override fun onAnimationEnd(animation: Animator) {
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
        if(mDuration > 0){
            animator!!.duration = mDuration.toLong()
        }else{
            //速度：假设整个View的宽度滚动完需要8秒，得出每秒滚动多少px
            val speed = measuredWidth / 8
            val duration = ((distance / Math.max(speed, 50)) * 1000 * mSpeed).toLong()
            animator!!.duration = Math.max(duration, 600)
        }
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

