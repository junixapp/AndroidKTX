package com.lxj.androidktx.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.lxj.androidktx.R
import com.lxj.androidktx.core.sp

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    val paint = Paint()
    var textBounds = Rect()
    var mText = ""
    var mTextBold = false
    var mTextColor = Color.BLACK
    var mTextSize = (14.sp).toFloat()
    var mTypefacePath: String? = null
    var mLoop : Boolean = true
    var mSlow: Float = 1.0f //缓慢系数，越大越慢
    var mScrollDelay = 400L

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MarqueeTextView)
        mText = ta.getString(R.styleable.MarqueeTextView_mtv_text) ?: ""
        mTextBold = ta.getBoolean(R.styleable.MarqueeTextView_mtv_textBold, false)
        mTextColor = ta.getColor(R.styleable.MarqueeTextView_mtv_textColor, mTextColor)
        mTextSize = ta.getDimension(R.styleable.MarqueeTextView_mtv_textSize, mTextSize)
        mTypefacePath = ta.getString(R.styleable.MarqueeTextView_mtv_typefacePath)
        mLoop = ta.getBoolean(R.styleable.MarqueeTextView_mtv_loop, mLoop)
        mSlow = ta.getFloat(R.styleable.MarqueeTextView_mtv_slow, mSlow)
        mScrollDelay = ta.getInteger(R.styleable.MarqueeTextView_mtv_scrollDelay, 400).toLong()
        ta.recycle()
        applyAttr()
    }

    private fun applyAttr(){
        if(mTextBold) paint.isFakeBoldText = true
        paint.color = mTextColor
        paint.textSize = mTextSize
        if(!mTypefacePath.isNullOrEmpty()) paint.setTypeface(Typeface.createFromAsset(context.assets, mTypefacePath))
        paint.getTextBounds(mText, 0 ,mText.length, textBounds)
    }

    fun setup(text: String? = null, textColor: Int? = null, textSize: Float? = null,
        typefacePath: String? = null, loop: Boolean? = null, slow: Float? = null,
        scrollDelay: Long? = null, textBold: Boolean? = null){
        if(text!=null) mText = text
        if(textColor!=null) mTextColor = textColor
        if(textSize!=null) mTextSize = textSize
        if(typefacePath!=null) mTypefacePath = typefacePath
        if(loop!=null) mLoop = loop
        if(slow!=null) mSlow = slow
        if(scrollDelay!=null) mScrollDelay = scrollDelay
        if(textBold!=null) mTextBold = textBold
        applyAttr()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = if(widthMode==MeasureSpec.UNSPECIFIED) (textBounds.width() + paddingLeft + paddingRight)
        else MeasureSpec.getSize(widthMeasureSpec)
        val height = if(heightMode==MeasureSpec.UNSPECIFIED) (textBounds.height() + paddingLeft + paddingRight)
        else MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val baseline: Float = measuredHeight/2f+(Math.abs(paint.ascent())-paint.descent())/2
        canvas.drawText(mText, 0f, baseline, paint)
    }


    private var scrolling = false
    private val mHandler = Handler(Looper.getMainLooper())
    private var first = true
    private var animator: ValueAnimator? = null
    var onMoveEnd: (() -> Unit)? = null
    fun startScroll() {
        if (scrolling) return
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            innerScroll()
        }, mScrollDelay)
    }

    fun canScroll() = getTextWidth() > measuredWidth

    private fun innerScroll() {
        val textWidth = getTextWidth()
        if (textWidth < measuredWidth) return
        scrolling = true
        animator?.removeAllListeners()
        val distance = if (mLoop) (textWidth).toInt() else (textWidth - measuredWidth).toInt()
        animator = ValueAnimator.ofInt(if (first) 0 else -measuredWidth, distance)
        animator!!.interpolator = LinearInterpolator()
        animator!!.addUpdateListener {
            invalidate()
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
                    onMoveEnd?.invoke()
                }
            }
        })
        //速度：假设整个View的宽度滚动完需要8秒，得出每秒滚动多少px
        val speed = measuredWidth / 8
        if(speed==0) return
        animator!!.duration = ((distance / speed) * 1000 * mSlow).toLong()
        animator!!.start()
    }

    fun getTextWidth() = paint.measureText(mText)

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