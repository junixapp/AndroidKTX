package com.lxj.androidktxdemo.blibli

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout

class CustomCoordinatorLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : CoordinatorLayout(context, attributeSet, defStyleAttr){


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isEnabled && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isEnabled && super.onTouchEvent(ev)
    }

    fun resetTouchBehaviors() {
//        val childCount = childCount
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            val lp = child.layoutParams as LayoutParams
//            val b = lp.behavior
//            if (b != null) {
//                val now = SystemClock.uptimeMillis()
//                val cancelEvent = MotionEvent.obtain(
//                    now, now,
//                    MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0
//                )
//                    b.onInterceptTouchEvent(this, child, cancelEvent)
////                if (notifyOnInterceptTouchEvent) {
////                } else {
//                    b.onTouchEvent(this, child, cancelEvent)
////                }
//                cancelEvent.recycle()
//            }
//        }
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            val lp = child.layoutParams as LayoutParams
//            lp.resetTouchBehaviorTracking()
//        }
//        mBehaviorTouchView = null
//        mDisallowInterceptReset = false
    }

}
