package com.lxj.androidktx.share

import android.content.Context
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main._ktx_share_popup.view.*

/**
 * Description: 分享弹窗
 * Create by dance, at 2019/6/24
 */
class SharePopup(context: Context) : BottomPopupView(context){
    override fun getImplLayoutId(): Int {
        return R.layout._ktx_share_popup
    }

    override fun onCreate() {
        super.onCreate()
        tvCancel.click { dismiss() }
        tvWxFriend.click { dismiss() }
        tvWxCircle.click { dismiss() }
        tvQQ.click { dismiss() }
        tvSina.click { dismiss() }
        //保存 复制链接
    }
}
