package com.lxj.androidktx.core

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.lxj.androidktx.util.FixClickSpanTouchListener
import com.lxj.xpopup.XPopup

/**
 * Description: View相关
 * Create by lxj, at 2018/12/4
 */

/**
 * 设置View的高度
 */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置View高度，限制在min和max范围之内
 * @param h
 * @param min 最小高度
 * @param max 最大高度
 */
fun View.limitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

/**
 * 设置View的宽度
 */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置View宽度，限制在min和max范围之内
 * @param w
 * @param min 最小宽度
 * @param max 最大宽度
 */
fun View.limitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

/**
 * 设置View的宽度和高度
 * @param width 要设置的宽度
 * @param height 要设置的高度
 */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置View的margin
 * @param leftMargin 默认保留原来的
 * @param topMargin 默认是保留原来的
 * @param rightMargin 默认是保留原来的
 * @param bottomMargin 默认是保留原来的
 */
fun View.margin(leftMargin: Int = Int.MAX_VALUE, topMargin: Int = Int.MAX_VALUE, rightMargin: Int = Int.MAX_VALUE, bottomMargin: Int = Int.MAX_VALUE): View {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (leftMargin != Int.MAX_VALUE)
        params.leftMargin = leftMargin
    if (topMargin != Int.MAX_VALUE)
        params.topMargin = topMargin
    if (rightMargin != Int.MAX_VALUE)
        params.rightMargin = rightMargin
    if (bottomMargin != Int.MAX_VALUE)
        params.bottomMargin = bottomMargin
    layoutParams = params
    return this
}

/**
 * 设置宽度，带有过渡动画
 * @param targetValue 目标宽度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidth(targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null,
                      action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                width(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置高度，带有过渡动画
 * @param targetValue 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateHeight(targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null, action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                height(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置宽度和高度，带有过渡动画
 * @param targetWidth 目标宽度
 * @param targetHeight 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidthAndHeight(targetWidth: Int, targetHeight: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null, action: ((Float) -> Unit)? = null) {
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthAndHeight(it.animatedValue as Int, evaluator.evaluate(it.animatedFraction, startHeight, targetHeight))
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置点击监听, 并实现事件节流，350毫秒内只允许点击一次
 */
var _viewClickFlag = false
var _clickRunnable = Runnable { _viewClickFlag = false }
fun View.click(duration: Long = 350, action: (view: View) -> Unit) {
    if(this is TextView)setOnTouchListener(FixClickSpanTouchListener())
    setOnClickListener {
        if (!_viewClickFlag) {
            _viewClickFlag = true
            action(it)
        }
        removeCallbacks(_clickRunnable)
        postDelayed(_clickRunnable, duration)
    }
}

/**
 * 设置长按监听
 */
fun View.longClick(action: (view: View) -> Boolean) {
    if(this is TextView)setOnTouchListener(FixClickSpanTouchListener())
    setOnLongClickListener {
        action(it)
    }
}


/*** 可见性相关 ****/
fun View.gone() {
    visibility = View.GONE
}
fun View.animateGone(duration: Long = 250) {
    TransitionManager.beginDelayedTransition(
        parent as ViewGroup, TransitionSet()
            .setDuration(duration)
            .addTransition(Fade())
            .addTransition(ChangeBounds())
    )
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}
fun View.animateVisible(duration: Long = 250) {
    TransitionManager.beginDelayedTransition(
        parent as ViewGroup, TransitionSet()
            .setDuration(duration)
            .addTransition(Fade())
            .addTransition(ChangeBounds())
    )
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
fun View.animateInvisible(duration: Long = 250) {
    TransitionManager.beginDelayedTransition(
        parent as ViewGroup, TransitionSet()
            .setDuration(duration)
            .addTransition(Fade())
            .addTransition(ChangeBounds())
    )
    visibility = View.INVISIBLE
}

val View.isGone: Boolean
    get() {
        return visibility == View.GONE
    }

val View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }

val View.isInvisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }

/**
 * 切换View的可见性
 */
fun View.toggleVisibility() {
    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
}


/**
 * 获取View的截图, 支持获取整个RecyclerView列表的长截图
 * 注意：调用该方法时，请确保View已经测量完毕，如果宽高为0，则将抛出异常
 */
fun View.toBitmap(): Bitmap {
    if (measuredWidth == 0 || measuredHeight == 0) {
        throw RuntimeException("调用该方法时，请确保View已经测量完毕，如果宽高为0，则抛出异常以提醒！")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val bmp = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)

            //draw default bg, otherwise will be black
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            this.draw(canvas)
            //恢复高度
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
            bmp //return
        }
        else -> {
            val screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(screenshot)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)// 将 view 画到画布上
            screenshot //return
        }
    }
}


// 所有子View
inline val ViewGroup.children
    get() = (0 until childCount).map { getChildAt(it) }


/**
 * 设置View不可用
 */
fun View.disable(value: Float = 0.5f) {
    isEnabled = false
    alpha = value
}

fun View.disableAll() {
    isEnabled = false
    alpha = 0.55f
    if(this is ViewGroup){
        children.forEach {
            it.disableAll()
        }
    }
}

/**
 * 设置View不可用
 */
fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.enableAll() {
    isEnabled = true
    alpha = 1f
    if(this is ViewGroup){
        children.forEach {
            it.enableAll()
        }
    }
}