package com.lxj.androidktx.widget

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.blankj.utilcode.constant.RegexConstants
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.RegexUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*

class VerifyCodeInput @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr), ClipboardManager.OnPrimaryClipChangedListener {

    var mCount = 4
    var mCorner = dp2px(5f)
    var mSolid = Color.parseColor("#E7E8EC")
    var mFocusBorder = Color.parseColor("#E7E8EC")
    var mSpace = dp2px(25f)
    var mSize = dp2px(40f)
    var mTextSize = sp2px(15f)
    var mTextColor = Color.parseColor("#232323")
    var mObserverClipboard = true

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.VerifyCodeInput)
        mCount = ta.getInt(R.styleable.VerifyCodeInput_vci_count, mCount)
        mCorner = ta.getDimensionPixelSize(R.styleable.VerifyCodeInput_vci_corner, mCorner)
        mSolid = ta.getColor(R.styleable.VerifyCodeInput_vci_solid, mSolid)
        mFocusBorder = ta.getColor(R.styleable.VerifyCodeInput_vci_focusBorder, mSolid)
        mSpace = ta.getDimensionPixelSize(R.styleable.VerifyCodeInput_vci_space, mSpace)
        mSize = ta.getDimensionPixelSize(R.styleable.VerifyCodeInput_vci_size, mSize)
        mTextSize = ta.getDimensionPixelSize(R.styleable.VerifyCodeInput_vci_textSize, mTextSize)
        mTextColor = ta.getColor(R.styleable.VerifyCodeInput_vci_textColor, mTextColor)
        mObserverClipboard = ta.getBoolean(R.styleable.VerifyCodeInput_vci_observerClipboard, mObserverClipboard)
        ta.recycle()
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        genEditText()
        if(mObserverClipboard){
            ClipboardUtils.addChangedListener(this)
        }

    }

    private fun genEditText() {
        (0 until mCount).forEach {
            val et = ShapeEditText(context)
            et.mSolid = mSolid
            et.mCorner = mCorner
            et.setTextColor(mTextColor)
            et.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            et.mEnableRipple = false
            et.mStrokeWidth = dp2px(1f)
            et.maxLines = 1
            et.gravity = Gravity.CENTER
            et.tag = it
            et.inputType = InputType.TYPE_CLASS_NUMBER
            et.setOnFocusChangeListener { v, hasFocus ->
                et.mStroke = if (hasFocus) mFocusBorder else mSolid
            }
            val ms = MeasureSpec.makeMeasureSpec(mSize, MeasureSpec.getMode(MeasureSpec.EXACTLY))
            val lp = MarginLayoutParams(ms, ms)
            if(it< mCount-1){
                lp.rightMargin = mSpace
            }
            et.layoutParams = lp
            //从第二个开始监听
            if(it>0){
                et.setOnKeyListener { v, keyCode, event ->
                    val index = et.tag as Int
                    //删除的时候让上一个获取焦点
                    if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN  && v.hasFocus() && et.text.isNullOrEmpty() && index>0) {
                        (getChildAt(index-1) as EditText).apply {
                            requestFocus()
                            setSelection(text.length)
                        }
                        return@setOnKeyListener true
                    }
                    false
                }
            }
            et.doAfterTextChanged {
                val index = et.tag as Int
                val length = et.text?.toString()?.length?:0
                if (length>0) {
                    if(length>1) {
                        et.setText(et.text.toString().substring(length-1))
                        et.setSelection(et.text?.length?:0)
                    }else{
                        if(index < mCount-1) {
                            (getChildAt(index+1) as EditText).apply {
                                requestFocus()
                                setSelection(text.length)
                            }
                        }
                        checkInputFinish()
                    }
                }
            }
            addView(et)
        }
        //默认第一个获取焦点
        if(childCount>0){
            val first = getChildAt(0)
            first.requestFocus()
        }
    }

    /**
     * 重新生成
     */
    fun regenerate(){
        removeAllViews()
        genEditText()
    }

    var onInputFinish: ((code: String)->Unit)? = null
    private fun checkInputFinish(){
        val code = children.map { (it as EditText).text.toString().trim() }.joinToString(separator = "")
        if(code.length == mCount && onInputFinish!=null) onInputFinish!!(code)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if(mObserverClipboard) ClipboardUtils.removeChangedListener(this)
    }

    override fun onPrimaryClipChanged() {
        if(!mObserverClipboard) return
        val clipboardText = ClipboardUtils.getText()
        if(!clipboardText.isNullOrEmpty() && clipboardText.length==mCount && RegexUtils.isMatch(RegexConstants.REGEX_POSITIVE_INTEGER, clipboardText)){
            //认为是验证码
            children.forEachIndexed { index, view ->
                (view as EditText).apply {
                    setText(clipboardText.get(index).toString())
                    setSelection(text.length)
                }
            }
        }
    }
}