package com.lxj.androidktx.widget


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 侧滑布局
 */
class SlidingLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attributeSet, defStyleAttr){

    enum class SlideState{
        Close, Open, Dragging
    }

    private var contentView : View? = null
    private var rightView : View? = null
    var state = SlideState.Close
    private val viewDragHelper :ViewDragHelper by lazy {
        ViewDragHelper.create(this, cb)
    }
    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = getChildAt(0)
        rightView = getChildAt(1)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        contentView?.layout(0, 0, measuredWidth, measuredHeight)
        rightView?.layout(contentView!!.right, 0, contentView!!.right+rightView!!.measuredWidth, measuredHeight)
    }

    val cb = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var newLeft = left
            if(child==contentView){
                if(newLeft < -rightView!!.measuredWidth) newLeft = -rightView!!.measuredWidth
                if(newLeft > 0) newLeft = 0
            }else{
                if(newLeft < (contentView!!.measuredWidth -rightView!!.measuredWidth)) newLeft = contentView!!.measuredWidth -rightView!!.measuredWidth
                if(newLeft > contentView!!.measuredWidth) newLeft = contentView!!.measuredWidth
            }
            return newLeft
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (changedView==contentView){
                rightView!!.offsetLeftAndRight(dx)
            }else{
                contentView!!.offsetLeftAndRight(dx)
            }
            if(contentView!!.left==-rightView!!.measuredWidth){
                if(state!= SlideState.Open){
                    state = SlideState.Open
                    slideListener?.onOpen(this@SlidingLayout)
                }
                if(shareCache!=null && !shareCache!!.contains(this@SlidingLayout)){
                    shareCache!!.add(this@SlidingLayout)
                }
                shareCache?.forEach {
                    if(it!=this@SlidingLayout) it.close()
                }
            }else if(contentView!!.left==0){
                if(state!= SlideState.Close){
                    state = SlideState.Close
                    slideListener?.onClose(this@SlidingLayout)
                }
                if(shareCache?.contains(this@SlidingLayout)==true){
                    shareCache!!.remove(this@SlidingLayout)
                }
            }

            //close other
            if(contentView!!.left > -rightView!!.measuredWidth && contentView!!.left<0){
                slideListener?.onDragging(this@SlidingLayout)
                state = SlideState.Dragging

                if(dx<0){
                    //向左打开，尝试关闭
                    shareCache?.forEach {
                        if(it!=this@SlidingLayout) it.close()
                    }
                }
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if(contentView!!.left < -rightView!!.measuredWidth/2){
                viewDragHelper.smoothSlideViewTo(contentView!!, -rightView!!.measuredWidth, 0)
            }else{
                viewDragHelper.smoothSlideViewTo(contentView!!, 0, 0)
            }
            ViewCompat.postInvalidateOnAnimation(this@SlidingLayout)
        }
    }
    override fun computeScroll() {
        super.computeScroll()
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this@SlidingLayout)
        }
    }

    private var touchX = 0f
    private var touchY = 0f
    private var result = true
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(ev)
        when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                touchX = ev.x
                touchY = ev.y
                result = true
                requestDisallowInterceptTouchEvent(result)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x-touchX
                val dy = ev.y-touchY
                if(Math.abs(dy) >= Math.abs(dx) && dy!=0f){
                    result = false
                }else
                    if(contentView!!.left<0){
                    result = true
                }else {
                    result = dx <=0
                }

                requestDisallowInterceptTouchEvent(result)
//                touchX = ev.x
//                touchY = ev.y
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP->{
                requestDisallowInterceptTouchEvent(false)
            }
        }
        return result
    }
    fun open(){
        if(state!= SlideState.Close) return
        viewDragHelper.smoothSlideViewTo(contentView!!, -rightView!!.measuredWidth, 0)
        postInvalidateOnAnimation()
    }
    fun close(){
        if(state!= SlideState.Open) return
        viewDragHelper.smoothSlideViewTo(contentView!!, 0, 0)
        postInvalidateOnAnimation()
    }

    var shareCache: CopyOnWriteArrayList<SlidingLayout>? = null

    var slideListener: OnSlideListener? = null

    interface OnSlideListener{
        fun onClose(view: SlidingLayout)
        fun onOpen(view: SlidingLayout)
        fun onDragging(view: SlidingLayout)
    }
}