package com.lxj.androidktx.share.wxapi

import android.content.Intent
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.weixin.view.WXCallbackActivity

/**
 * Description:
 * Create by dance, at 2019/4/1
 */
class WXEntryActivity: WXCallbackActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data)
    }


}