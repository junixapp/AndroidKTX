package com.lxj.androidktxdemo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Environment
import androidx.lifecycle.Observer
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lxj.androidktx.base.CameraActivity
import com.lxj.androidktx.base.WebActivity
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktx.util.CommonUpdateInfo
import com.lxj.androidktx.util.VersionUpdateUtil
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.HttpResult
import com.lxj.androidktxdemo.entity.User
import kotlinx.android.synthetic.main.fragment_http_ext.*
import kotlinx.android.synthetic.main.fragment_uploader.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * Description: Okhttp扩展
 * Create by dance, at 2019/1/4
 */
class UploaderFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_uploader
    override fun initView() {
        uploader.addButtonClickAction = {
            ImagePicker.startPicker(this, 1, maxNum = 9-uploader.getImagePaths().size)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            uploader.addImages(ImagePicker.fetchResult(data))
        }
    }

}


