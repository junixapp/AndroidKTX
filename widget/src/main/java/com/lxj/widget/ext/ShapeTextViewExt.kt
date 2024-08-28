package com.lxj.widget.ext

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.lxj.widget.ShapeTextView


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