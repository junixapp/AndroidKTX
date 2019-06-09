package com.lxj.androidktx.widget

import android.content.Context
import android.util.AttributeSet
import com.lxj.androidktx.R

/**
 * Description:
 * Create by dance, at 2019/6/9
 */
/**
 * Description: 发送验证码View，功能如下：
 * 1. 显示默认文字：如发送验证码
 * 2. 执行倒计时功能
 * 3. 显示倒计时文字，如：60秒后重发
 * 4. 显示重发文字：如重新发送验证码
 * Create by dance, at 2019/6/9
 */
class VerifyView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeTextView(context, attributeSet, defStyleAttr){

    var defText = "发送验证码" //默认文字
    var countDownText = "秒后重新发送" //执行倒计时期间的文字，前面会拼上时间
    var resendText = "重新发送验证码" //重新发送的文字
    var resendDuration = 60 //重新发送的时间周期，默认是60秒
    var currTime = 0 //当前时间
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.VerifyView)
        defText = ta.getString(R.styleable.VerifyView_vv_defText) ?: defText
        countDownText = ta.getString(R.styleable.VerifyView_vv_countDownText) ?: countDownText
        resendText = ta.getString(R.styleable.VerifyView_vv_resendText) ?: resendText
        resendDuration = ta.getInt(R.styleable.VerifyView_vv_resendDuration, resendDuration)
        ta.recycle()
        currTime = resendDuration
        text = defText
    }

    fun startCountDown(){
        if(currTime==0){
            animate().alpha(1f).setDuration(300).start()
            isEnabled = true
            text = resendText
            currTime = resendDuration
            handler.removeCallbacksAndMessages(null)
            return
        }
        animate().alpha(.7f).setDuration(300).start()
        isEnabled = false
        text = "${currTime}${countDownText}"
        currTime--
        handler.postDelayed({startCountDown()}, 1000)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }
}