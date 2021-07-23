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
import kotlinx.android.synthetic.main._ktx_search_layout.view.*

/**
 * Description: 搜索框
 * Create by dance, at 2019/5/27
 */
open class SearchLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeLinearLayout(context, attributeSet, defStyleAttr) {

    var hint = ""
    var hintColor = Color.parseColor("#888888")
    var textColor = Color.parseColor("#222222")
    var textSize = 14.sp
    var clearIcon: Drawable? = null
    var searchIcon: Drawable? = null
    var clearIconSize = 28.dp
    var searchIconSize =  28.dp
    var showClearIconWhenEmpty = false  //内容为空时是否显示删除按钮
    var showSearchIcon = true  //是否显示搜索图标
    var searchIconPosition = 0  //搜索框默认在右边
    var searchIconSpace = 0  //搜索框和文字的间距

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SearchLayout)
        hint = ta.getString(R.styleable.SearchLayout_sl_hint) ?: ""
        hintColor = ta.getColor(R.styleable.SearchLayout_sl_hintColor, hintColor)
        textColor = ta.getColor(R.styleable.SearchLayout_sl_textColor, textColor)
        textSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_textSize, textSize)
        clearIcon = ta.getDrawable(R.styleable.SearchLayout_sl_clearIcon)
            ?: drawable(R.mipmap._ktx_ic_clear)
        searchIcon = ta.getDrawable(R.styleable.SearchLayout_sl_searchIcon)
            ?: drawable(R.mipmap._ktx_ic_search)
        clearIconSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_clearIconSize, clearIconSize)
        searchIconSize = ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_searchIconSize, searchIconSize)
        showClearIconWhenEmpty = ta.getBoolean(R.styleable.SearchLayout_sl_showClearIconWhenEmpty, showClearIconWhenEmpty)
        showSearchIcon = ta.getBoolean(R.styleable.SearchLayout_sl_showSearchIcon, showSearchIcon)
        searchIconPosition = ta.getInt(R.styleable.SearchLayout_sl_searchIconPosition, searchIconPosition)
        searchIconSpace =
            ta.getDimensionPixelSize(R.styleable.SearchLayout_sl_searchIconSpace, searchIconSpace)
        ta.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL

        inflate(context, R.layout._ktx_search_layout, this)
        initSelf()
    }

    fun initSelf() {
        ivClear.widthAndHeight(clearIconSize, clearIconSize)
        ivClear.setImageDrawable(clearIcon)
        if(showSearchIcon){
            if(searchIconPosition==0){
                ivSearchLeft.visible()
                ivSearchLeft.widthAndHeight(searchIconSize, searchIconSize)
                ivSearchRight.gone()
                ivSearchLeft.setImageDrawable(searchIcon)
                ivSearchLeft.margin(rightMargin = searchIconSpace)
            }else{
                ivSearchRight.visible()
                ivSearchRight.widthAndHeight(searchIconSize, searchIconSize)
                ivSearchLeft.gone()
                ivSearchRight.setImageDrawable(searchIcon)
                ivSearchRight.margin(leftMargin = searchIconSpace)
            }
        }else{
            ivSearchLeft.gone()
            ivSearchRight.gone()
        }

        if(showClearIconWhenEmpty) {
            ivClear.visible()
            //如果搜索图标也在右边，则强制隐藏
            if(searchIconPosition==1) ivSearchRight.gone()
        } else {
            if(searchIconPosition==1) ivClear.gone()
            else ivClear.invisible()
        }

        ivClear.click { et_content.setText("") }
        et_content.doAfterTextChanged {
            if (!it.isNullOrEmpty() || showClearIconWhenEmpty) {
                 ivClear.visible()
                if(searchIconPosition==1) ivSearchRight.gone()
            } else {
                if(searchIconPosition==1) {
                    ivSearchRight.visible()
                    ivClear.gone()
                }else{
                    ivClear.invisible()
                }
            }
        }

        et_content.hint = hint
        et_content.setHintTextColor(hintColor)
        et_content.setTextColor(textColor)
        et_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun getEditText() = et_content
    fun getClearView() = ivClear

    fun setReadMode() {
        ivClear.gone()
        ivSearchLeft.gone()
        ivSearchRight.gone()
        et_content.isEnabled = false
    }
}