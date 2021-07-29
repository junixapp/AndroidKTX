package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.blankj.utilcode.util.SpanUtils
import com.lxj.androidktx.R
import com.lxj.androidktx.core.appendColorSpan
import com.lxj.androidktx.core.maxLength
import com.lxj.androidktx.core.sp
import kotlinx.android.synthetic.main._ktx_form_field.view.*

/**
 * Description: 表单输入
 * Create by dance, at 2019/5/27
 */
open class FormField @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    var mHint = ""
    var mHintColor = Color.parseColor("#999999")
    var mInputTextColor = Color.parseColor("#444444")
    var mInputTextSize = 14.sp
    var mInputMaxLines = 1
    var mInputMaxLength = 100
    var mFieldTextColor = Color.parseColor("#111111")
    var mFieldTextSize = 16.sp
    var mRequired = false
    var mRequiredColor = Color.parseColor("#FA5452")
    var mFieldTextBold = false
    var mInputText : String? = null
    var mFieldText : String? = null

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.FormField)
        mHint = ta.getString(R.styleable.FormField_ff_hint) ?: ""
        mHintColor = ta.getColor(R.styleable.FormField_ff_hintColor, mHintColor)
        mInputTextColor = ta.getColor(R.styleable.FormField_ff_inputTextColor, mInputTextColor)
        mInputTextSize = ta.getDimensionPixelSize(R.styleable.FormField_ff_inputTextSize, mInputTextSize)
        mFieldTextColor = ta.getColor(R.styleable.FormField_ff_fieldTextColor, mFieldTextColor)
        mRequiredColor = ta.getColor(R.styleable.FormField_ff_requiredColor, mRequiredColor)
        mFieldTextSize = ta.getDimensionPixelSize(R.styleable.FormField_ff_fieldTextSize, mFieldTextSize)
        mFieldTextBold = ta.getBoolean(R.styleable.FormField_ff_fieldTextBold, mFieldTextBold)
        mRequired = ta.getBoolean(R.styleable.FormField_ff_required, mRequired)
        mInputText = ta.getString(R.styleable.FormField_ff_inputText)
        mFieldText = ta.getString(R.styleable.FormField_ff_fieldText)
        mInputMaxLines = ta.getInteger(R.styleable.FormField_ff_inputMaxLines, mInputMaxLines)
        mInputMaxLength = ta.getInteger(R.styleable.FormField_ff_inputMaxLength, mInputMaxLength)

        ta.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        inflate(context, R.layout._ktx_form_field, this)
        applyAttr()
    }

    private fun applyAttr() {
        tvField.setTextColor(mFieldTextColor)
        tvField.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFieldTextSize.toFloat())
        tvField.paint.isFakeBoldText = mFieldTextBold
        if(mFieldText!=null) {
            tvField.text = mFieldText
        }
        if(mRequired) tvField.appendColorSpan(" *", color = mRequiredColor)

        etValue.hint = mHint
        etValue.setHintTextColor(mHintColor)
        etValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize.toFloat())
        etValue.setTextColor(mInputTextColor)
        if(mInputText!=null) etValue.setText(mInputText!!)
        etValue.maxLines = mInputMaxLines
        etValue.maxLength(mInputMaxLength)
    }

    fun setupSelf(fieldText: String? = null, fieldTextSize: Int? = null, fieldTextColor: Int? = null,
        fieldTextBold: Boolean? = null, hint: String? = null, hintColor: Int? = null, inputText: String? = null,
        inputTextSize: Int? = null, inputTextColor: Int? = null, inputMaxLines: Int? = null,
        inputMaxLength: Int? = null){
        if(fieldText!=null) mFieldText = fieldText
        if(fieldTextSize!=null) mFieldTextSize = fieldTextSize
        if(fieldTextColor!=null) mFieldTextColor = fieldTextColor
        if(fieldTextBold!=null) mFieldTextBold = fieldTextBold
        if(hint!=null) mHint = hint
        if(hintColor!=null) mHintColor = hintColor
        if(inputText!=null) mInputText = inputText
        if(inputTextSize!=null) mInputTextSize = inputTextSize
        if(inputTextColor!=null) mInputTextColor = inputTextColor
        if(inputMaxLines!=null) mInputMaxLines = inputMaxLines
        if(inputMaxLength!=null) mInputMaxLength = inputMaxLength
        applyAttr()
    }

    fun getEditText() = etValue
    fun getFieldView() = tvField


}