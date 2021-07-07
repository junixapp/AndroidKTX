package com.lxj.androidktx.popup

import android.content.Context
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.gone
import com.lxj.androidktx.util.CommonUpdateInfo
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main._popup_version_update.view.*

class VersionUpdatePopup (context: Context, var updateInfo: CommonUpdateInfo, var onOkClick: ((url:String)->Unit)): CenterPopupView(context){
    override fun getImplLayoutId(): Int {
        return R.layout._popup_version_update
    }

    override fun onCreate() {
        super.onCreate()
        tv_ok.setTextColor(XPopup.getPrimaryColor())
        tv_info.text = "${updateInfo.update_info}"
        if(updateInfo.force_update==true){
            tv_cancel.gone()
            vv.gone()
        }
        tv_cancel.click { dismiss() }
        tv_ok.click {
            onOkClick(updateInfo.download_url?:"")
            dismiss()
        }
    }
}