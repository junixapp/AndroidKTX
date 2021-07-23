package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.core.widget.doAfterTextChanged
import com.lxj.androidktx.R
import com.lxj.androidktx.core.dp
import com.lxj.androidktx.core.margin
import com.lxj.androidktx.core.maxLength
import com.lxj.androidktx.core.sp
import kotlinx.android.synthetic.main._ktx_number_edit_layout.view.*

class NumberEditLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout (context, attributeSet, defStyleAttr){

    var mHint = ""
    var mHintColor = Color.parseColor("#888888")
    var mInputTextColor = Color.parseColor("#232323")
    var mInputTextSize = 14.sp
    var mInputBgColor = Color.parseColor("#F1F1F1")
    var mInputCorner = 4.dp
    var mInputPadding = 10.dp
    var mNumberTextColor = Color.parseColor("#777777")
    var mNumberTextSize = 12.sp
    var mMaxTextNumber = 100
    var mNumberTopSpace = 10.dp
    
    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.NumberEditLayout)
        mHint = ta.getString(R.styleable.NumberEditLayout_nel_hint) ?: ""
        mHintColor = ta.getColor(R.styleable.NumberEditLayout_nel_hintColor, mHintColor)
        mInputTextColor = ta.getColor(R.styleable.NumberEditLayout_nel_inputTextColor, mInputTextColor)
        mInputTextSize = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputTextSize, mInputTextSize)
        mInputBgColor = ta.getColor(R.styleable.NumberEditLayout_nel_inputBgColor, mInputBgColor)
        mInputCorner = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputCorner, mInputCorner)
        mInputPadding = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_inputPadding, mInputPadding)
        mNumberTextColor = ta.getColor(R.styleable.NumberEditLayout_nel_numberTextColor, mNumberTextColor)
        mNumberTextSize = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_numberTextSize, mNumberTextSize)
        mMaxTextNumber = ta.getInt(R.styleable.NumberEditLayout_nel_maxTextNumber, mMaxTextNumber)
        mNumberTopSpace = ta.getDimensionPixelSize(R.styleable.NumberEditLayout_nel_numberTopSpace, mNumberTopSpace)

        orientation = LinearLayout.VERTICAL
        inflate(context, R.layout._ktx_number_edit_layout, this)
        
        applyAttr()
    }

    fun getEditText() = etContent
    fun getNumberView() = tvNum

    fun setupSelf(hint: String? = null, hintColor: Int? = null, inputTextColor: Int? = null,
        inputTextSize: Float? = null, inputBgColor: Int? = null, inputCorner: Int? = null, inputPadding: Int? = null,
        numberTextColor: Int? = null, numberTextSize: Float? = null, maxTextNumber: Int? = null,
        numberTopSpace: Int? = null){
        if(hint!=null) mHint = hint
        if(hintColor!=null) mHintColor = hintColor
        if(inputTextColor!=null) mInputTextColor = inputTextColor
        if(inputTextSize!=null) mInputTextSize = inputTextSize.toInt()
        if(inputBgColor!=null) mInputBgColor = inputBgColor
        if(inputCorner!=null) mInputCorner = inputCorner
        if(inputPadding!=null) mInputPadding = inputPadding
        if(numberTextColor!=null) mNumberTextColor = numberTextColor
        if(numberTextSize!=null) mNumberTextSize = numberTextSize.toInt()
        if(maxTextNumber!=null) mMaxTextNumber = maxTextNumber
        if(numberTopSpace!=null) mNumberTopSpace = numberTopSpace
        applyAttr()
    }
    
    private fun applyAttr() {
        etContent.hint = mHint
        etContent.setHintTextColor(mHintColor)
        etContent.setTextColor(mInputTextColor)
        etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize.toFloat())
        etContent.setup(solid = mInputBgColor, corner = mInputCorner)
        etContent.maxLength(mMaxTextNumber)
        etContent.setPadding(mInputPadding)
        tvNum.setTextColor(mNumberTextColor)
        tvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumberTextSize.toFloat())
        tvNum.margin(topMargin = mNumberTopSpace)

        val currentLength = etContent.text?.length ?:0
        tvNum.text = "${currentLength}/${mMaxTextNumber}"
        etContent.doAfterTextChanged {
            val currentLength = etContent.text?.length ?:0
            tvNum.text = "${currentLength}/${mMaxTextNumber}"
        }
    }
}