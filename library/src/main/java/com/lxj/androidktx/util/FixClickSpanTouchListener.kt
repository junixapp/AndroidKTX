package com.lxj.androidktx.util

import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView

class FixClickSpanTouchListener : View.OnTouchListener {
    private var mLastActionDownTime: Long = -1
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action = event.action
        if(v is TextView){
            val text = v.text
            var link: Array<ClickableSpan>? = null
            if (text is Spanned) {
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= v.totalPaddingLeft
                y -= v.totalPaddingTop
                x += v.scrollX
                y += v.scrollY
                val layout = v.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                link = text.getSpans(off, off, ClickableSpan::class.java)
            }
            if (action == MotionEvent.ACTION_DOWN) {
                mLastActionDownTime = System.currentTimeMillis()
                return link!=null && link.isNotEmpty()
            } else {
                val actionUpTime = System.currentTimeMillis()
                if (mLastActionDownTime!=-1L && actionUpTime - mLastActionDownTime >= ViewConfiguration.getLongPressTimeout() ) {
                    mLastActionDownTime = -1
                    return v.performLongClick()
                }
                if (action == MotionEvent.ACTION_UP) {
                    if (link!=null && link.isNotEmpty()) {
                        link[0].onClick(v)
                    } else {
                        v.performClick()
                    }
                }
            }
        }
        return false
    }
}
