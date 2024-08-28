package com.lxj.widget.popup

import android.content.Context
import com.lxj.ext.click
import com.lxj.ext.gone
import com.lxj.widget.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main._popup_version_update.view.*

class VersionUpdatePopup (context: Context, var updateInfo: String,
                          var forceUpdate: Boolean = false,
                          var onOkClick: (()->Unit)): CenterPopupView(context){
    override fun getImplLayoutId(): Int {
        return R.layout._popup_version_update
    }

    override fun onCreate() {
        super.onCreate()
        tv_ok.setTextColor(XPopup.getPrimaryColor())
        tv_info.text = updateInfo
        if(forceUpdate){
            tv_cancel.gone()
            vv.gone()
        }
        tv_cancel.click { dismiss() }
        tv_ok.click {
            onOkClick()
            dismiss()
        }
    }
}