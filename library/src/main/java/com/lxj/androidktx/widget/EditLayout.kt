package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.core.widget.doAfterTextChanged
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*
import kotlinx.android.synthetic.main._ktx_edit_layout.view.*

/**
 * Description: 可以设置Shape的FrameLayout
 * Create by dance, at 2019/5/27
 */
open class EditLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    var hint = ""
    var hintColor = Color.parseColor("#888888")
    var textColor = Color.parseColor("#222222")
    var textSize = sp2px(14f)
    var clearIconRes : Drawable? = null
    var searchIconRes : Drawable? = null
    var iconSize = 0
    var showIconWhenEmpty = true

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.EditLayout)
        hint = ta.getString(R.styleable.EditLayout_el_hint) ?: ""
        hintColor = ta.getColor(R.styleable.EditLayout_el_hintColor, hintColor)
        textColor = ta.getColor(R.styleable.EditLayout_el_textColor, textColor)
        textSize = ta.getDimensionPixelSize(R.styleable.EditLayout_el_textSize, textSize)
        clearIconRes = ta.getDrawable(R.styleable.EditLayout_el_clearIconRes) ?: drawable(R.mipmap._ktx_ic_clear)
        searchIconRes = ta.getDrawable(R.styleable.EditLayout_el_searchIconRes) ?: drawable(R.mipmap._ktx_ic_search)
        iconSize = ta.getDimensionPixelSize(R.styleable.EditLayout_el_iconSize, iconSize)
        showIconWhenEmpty = ta.getBoolean(R.styleable.EditLayout_el_showIconWhenEmpty, showIconWhenEmpty)
        ta.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        inflate(context, R.layout._ktx_edit_layout, this)
        initSelf()
    }

    fun initSelf(){
        if(iconSize>0) ivClear.widthAndHeight(iconSize, iconSize)
        ivClear.setImageDrawable(searchIconRes)
        if(!showIconWhenEmpty) ivClear.invisible()

        ivClear.click { et_content.setText("") }
        et_content.doAfterTextChanged {
            if(it.isNullOrEmpty()) {
                if(showIconWhenEmpty){
                    ivClear.setImageDrawable(searchIconRes)
                }else{
                    ivClear.invisible()
                }
            } else {
                ivClear.visible()
                ivClear.setImageDrawable(clearIconRes)
            }
        }

        et_content.hint = hint
        et_content.setHintTextColor(hintColor)
        et_content.setTextColor(textColor)
        et_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun getEditText() = et_content
    fun getClearView() = ivClear

    fun setReadMode(){
        ivClear.gone()
        et_content.isEnabled = false
    }
}