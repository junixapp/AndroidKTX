package com.lxj.androidktx.widget

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.lxj.androidktx.R
import kotlinx.android.synthetic.main._wx_progress_dialog.*


class WxProgressDialog @JvmOverloads constructor(context: Context, theme: Int = 0)
    : Dialog(context, theme){

    var message: String = ""
    init {
        setCanceledOnTouchOutside(false)
        setContentView(R.layout._wx_progress_dialog)
        val params = window.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params

    }

    fun setMessage(msg: String): WxProgressDialog{
        message = msg
        tvMsg.text = message
        return this
    }
}