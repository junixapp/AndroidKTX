package com.lxj.androidktxdemo.fragment

import android.app.Activity
import android.content.Intent
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_uploader.*

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


