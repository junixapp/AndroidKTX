package com.lxj.androidktx.core

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.lxj.androidktx.widget.ShapeTextView

/**
 * Description:
 * Create by lxj, at 2019/2/21
 */

/**
 * 给TextView的drawable设置大小，Drawable如果不传的话会尝试使用TextView自己的Drawable
 * @param width Drawable的宽度
 * @param height Drawable的高度
 */
fun TextView.sizeDrawable(width: Int, height: Int, leftDrawable: Int = 0, topDrawable: Int = 0,
                          rightDrawable: Int = 0, bottomDrawable: Int = 0): TextView {
    val rect = Rect(0, 0, width, height)
    setCompoundDrawables(
            findDrawable(leftDrawable, 0, this)?.apply { bounds = rect },
            findDrawable(topDrawable, 1, this)?.apply { bounds = rect },
            findDrawable(rightDrawable, 2, this)?.apply { bounds = rect },
            findDrawable(bottomDrawable, 3, this)?.apply { bounds = rect }
    )
    return this
}

/**
 * 优先使用传入的，如果不传则尝试使用TextView自己的
 */
private fun findDrawable(drawableRes: Int, index:Int, textView: TextView): Drawable?{
    if(drawableRes!=0)return textView.drawable(drawableRes)
    if(textView.compoundDrawables.isNotEmpty())return textView.compoundDrawables[index]
    return null
}

/**
 * 给TextView的drawable设置大小，Drawable如果不传的话会尝试使用TextView自己的Drawable
 * @param size 会同时作用于Drawable宽高
 */
fun TextView.sizeDrawable(size: Int, leftDrawable: Int = 0, topDrawable: Int = 0,
                          rightDrawable: Int = 0, bottomDrawable: Int = 0): TextView {
    sizeDrawable(size, size, leftDrawable, topDrawable, rightDrawable, bottomDrawable)
    return this
}

fun ShapeTextView.disable(solid: Int? = null, textColor: Int? = null, alpha: Float = 1f){
    isEnabled = false
    setAlpha(alpha)
    setup(solid = solid)
    if(textColor!=null) setTextColor(textColor)
}

fun ShapeTextView.enable(solid: Int? = null, textColor: Int? = null, alpha: Float = 1f){
    isEnabled = true
    setAlpha(alpha)
    setup(solid = solid)
    if(textColor!=null) setTextColor(textColor)
}

/**
 * 根据输入框的内容来切决定是否禁用
 */
fun ShapeTextView.switchStateByEditText(et: EditText, minLength: Int = 1, enableSolid: Int? = null, disableSolid: Int? = null,
                                   enableTextColor: Int? = null, disableTextColor: Int? = null,
                                  onTextChange: ((Editable?)->Unit)? = null ){
    et.doAfterTextChanged {
        if(it?.length?:0 >= minLength){
            enable(solid = enableSolid, textColor = enableTextColor)
        }else{
            disable(solid = disableSolid, textColor = disableTextColor)
        }
        onTextChange?.invoke(it)
    }
}

/**
 * 动态设置最大长度限制
 */
fun TextView.maxLength(max: Int){
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max))
}
