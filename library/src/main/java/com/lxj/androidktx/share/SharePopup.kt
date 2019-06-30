package com.lxj.androidktx.share

import android.content.Context
import com.lxj.androidktx.R
import com.lxj.xpopup.core.BottomPopupView

/**
 * Description: 分享弹窗
 * Create by dance, at 2019/6/24
 */
class SharePopup(context: Context) : BottomPopupView(context){
    override fun getImplLayoutId(): Int {
        return R.layout._ktx_share_popup

    }
}
