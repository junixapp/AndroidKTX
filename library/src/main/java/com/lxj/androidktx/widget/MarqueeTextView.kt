package com.lxj.androidktx.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.lxj.androidktx.R
import com.lxj.androidktx.core.sp

/**
 * 支持一段文字进行滚动的View
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textBounds = Rect()
    private var mText: String? = null
    private var mTextBold = false
    private var mTextColor = Color.BLACK
    private var mTextSize = (14.sp).toFloat()
    private var mTypefacePath: String? = null
    private var mLoop: Boolean = true
    private var mEnableFadeEdge: Boolean = true
    private var mDuration: Int = 0  //如果指定时间，则固定速度
    private var mSpeed: Float = 1f  //按速度滚动，越大越慢
    private var mScrollDelay = 400L
    private var mTextAlign = Paint.Align.LEFT
    private var mLetterSpacing = 0f

    private var scrolling = false
    private val mHandler = Handler(Looper.getMainLooper())
    private var first = true
    var animator: ValueAnimator? = null
    var onMoveEnd: ((Long) -> Unit)? = null

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MarqueeTextView)
        mText = ta.getString(R.styleable.MarqueeTextView_mtv_text)
        mTextBold = ta.getBoolean(R.styleable.MarqueeTextView_mtv_textBold, false)
        mTextColor = ta.getColor(R.styleable.MarqueeTextView_mtv_textColor, mTextColor)
        mTextSize = ta.getDimension(R.styleable.MarqueeTextView_mtv_textSize, mTextSize)
        mTypefacePath = ta.getString(R.styleable.MarqueeTextView_mtv_typefacePath)
        mLoop = ta.getBoolean(R.styleable.MarqueeTextView_mtv_loop, mLoop)
        mEnableFadeEdge = ta.getBoolean(R.styleable.MarqueeTextView_mtv_enableFadeEdge, mEnableFadeEdge)
        mDuration = ta.getInteger(R.styleable.MarqueeTextView_mtv_duration, mDuration)
        mSpeed = ta.getFloat(R.styleable.MarqueeTextView_mtv_speed, mSpeed)
        mScrollDelay = ta.getInteger(R.styleable.MarqueeTextView_mtv_scrollDelay, 400).toLong()
        val align =
            ta.getInteger(R.styleable.MarqueeTextView_mtv_textAlign, Paint.Align.LEFT.ordinal)
        mTextAlign = when (align) {
            0 -> Paint.Align.LEFT
            1 -> Paint.Align.CENTER
            else -> Paint.Align.RIGHT
        }
        mLetterSpacing = ta.getFloat(R.styleable.MarqueeTextView_mtv_letterSpacing, mLetterSpacing)
        ta.recycle()
        isHorizontalFadingEdgeEnabled = true
        applyAttr()
    }

    private fun applyAttr() {
        if (mTextBold) paint.isFakeBoldText = true
        paint.color = mTextColor
        paint.textSize = mTextSize
        paint.textAlign = mTextAlign
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.letterSpacing = mLetterSpacing
        }
        if (!mTypefacePath.isNullOrEmpty()) paint.typeface =
            Typeface.createFromAsset(context.assets, mTypefacePath)
        if (mText != null) paint.getTextBounds(mText, 0, mText!!.length, textBounds)
    }

    fun setup(
        text: String? = null, textColor: Int? = null, textSize: Float? = null,speed: Float? = null,
        typefacePath: String? = null, loop: Boolean? = null, duration: Int? = null,
        scrollDelay: Long? = null, textBold: Boolean? = null, textAlign: Paint.Align? = null
    ) {
        if (text != null) mText = text
        if (textColor != null) mTextColor = textColor
        if (textSize != null) mTextSize = textSize
        if (speed != null) mSpeed = speed
        if (typefacePath != null) mTypefacePath = typefacePath
        if (loop != null) mLoop = loop
        if (duration != null) mDuration = duration
        if (scrollDelay != null) mScrollDelay = scrollDelay
        if (textBold != null) mTextBold = textBold
        if (textAlign != null) mTextAlign = textAlign
        applyAttr()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width =
            if (widthMode != MeasureSpec.EXACTLY) (getTextWidth().toInt() + paddingLeft + paddingRight)
            else MeasureSpec.getSize(widthMeasureSpec)
        val height =
            if (heightMode != MeasureSpec.EXACTLY) (textBounds.height() + paddingTop + paddingBottom)
            else MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mText != null) {

            var forceLeft = false
            if (mTextAlign != Paint.Align.LEFT && (paint.measureText(mText) + paddingLeft + paddingRight) > measuredWidth) {
                //如果能滚动，则强制向左靠
                forceLeft = true
                paint.textAlign = Paint.Align.LEFT
            } else {
                paint.textAlign = mTextAlign
            }

            val clipLeft: Float = (scrollX).toFloat()
            val clipTop: Float = scrollY.toFloat()
            val clipRight: Float = (measuredWidth + scrollX).toFloat()
            val clipBottom: Float = (measuredHeight + scrollY).toFloat()
            canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom)
            val x = when (mTextAlign) {
                Paint.Align.LEFT -> 0f
                Paint.Align.CENTER -> measuredWidth / 2f
                else -> measuredWidth * 1f
            }
            val baseline: Float =
                measuredHeight / 2f + (Math.abs(paint.ascent()) - paint.descent()) / 2

            if (mLetterSpacing != 0f && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawSpacingText(canvas, mText ?: "", if (forceLeft) 0f else x, baseline)
            } else {
                canvas.drawText(mText ?: "", if (forceLeft) 0f else x, baseline, paint)
            }
        }
    }


    private fun drawSpacingText(
        canvas: Canvas,
        text: String,
        xOffset: Float,
        yOffset: Float
    ): Int {
        var xOffset = xOffset
        val textRect = Rect()
        var width = 0
        val space = Math.round(paint.measureText(" ") * mLetterSpacing)
        for (i in 0 until text.length) {
            canvas.drawText(text[i].toString(), xOffset, yOffset, paint)
            var charWidth: Int
            charWidth = if (text[i] == ' ') {
                Math.round(paint.measureText(text[i].toString())) + space
            } else {
                paint.getTextBounds(text, i, i + 1, textRect)
                textRect.width() + space
            }
            xOffset += charWidth.toFloat()
            width += charWidth
        }
        return width
    }

    fun startScroll() {
        if (scrolling) return
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({
            innerScroll()
        }, mScrollDelay)
    }

    override fun isPaddingOffsetRequired() = mEnableFadeEdge && animator?.isRunning==true
    override fun getRightFadingEdgeStrength() = if(isPaddingOffsetRequired) 0.5f else 0f
    override fun getLeftFadingEdgeStrength() = if(mEnableFadeEdge && canScroll()) 0.5f else 0f

    fun canScroll() : Boolean{
        return getTextWidth() > measuredWidth
    }

    private fun innerScroll() {
        val textWidth = getTextWidth()
        if (textWidth < measuredWidth) return
        scrolling = true
        animator?.removeAllListeners()
        val distance = if (mLoop) (textWidth) else (textWidth - measuredWidth)
        animator = ValueAnimator.ofInt(if (first) 0 else -measuredWidth, distance.toInt())
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
                    onMoveEnd?.invoke(animation?.duration ?: 0L)
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

    //    fun getTextWidth() = paint.measureText(mText)
    fun getTextWidth(): Float {
//        var w = textBounds.width()
        var w = paint.measureText(mText)
        if (mLetterSpacing != 0f && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val space = Math.round(paint.measureText(" ") * mLetterSpacing)
            if(!mText.isNullOrEmpty()) w += space * (mText!!.length)
        }
        return w.toFloat()
    }

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

