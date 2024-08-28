package com.lxj.widget.popup

import android.content.Context
import com.lxj.ext.click
import com.lxj.ext.gone
import com.lxj.widget.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main._popup_version_update.view.*

data class CommonUpdateInfo(
    var download_url: String? = null,
    var version_name: String? = null,
    var package_name: String? = null,
    var update_info: String? = null,
    var force_update: Boolean? = false
)
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