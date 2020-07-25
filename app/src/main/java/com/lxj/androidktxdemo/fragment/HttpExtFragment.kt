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
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktx.util.CommonUpdateInfo
import com.lxj.androidktx.util.VersionUpdateUtil
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.HttpResult
import com.lxj.androidktxdemo.entity.User
import kotlinx.android.synthetic.main.fragment_http_ext.*
import java.io.File

/**
 * Description: Okhttp扩展
 * Create by dance, at 2019/1/4
 */
class HttpExtFragment : BaseFragment() {
    val loginData = StateLiveData<User>()
    val imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560151504361&di=ce11648353ac380140bce2579683ed59&imgtype=0&src=http%3A%2F%2Fs14.sinaimg.cn%2Fmw690%2F001xcz9rzy6PqH78yLPed%26690"
    val zipFile = "https://github.com/li-xiaojun/XPopup/archive/master.zip"
    override fun getLayoutId() = R.layout.fragment_http_ext
    override fun initView() {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/mnt")
//        vm = ViewModelProviders.of(this).get(HttpExtVM::class.java)
//
//        vm!!.data.observe(this, Observer<String> {
//
//            tvResponse.text = if (it == null) "请求失败" else JSONObject(it).toString(2)
//        })
        tvResponse.sizeDrawable(dp2px(20f), topDrawable = R.mipmap.ic_launcher)

        // 全局header
        OkExt.headers("site" to "CN",
                "name" to "GullMap_world",
                "language" to "zh-CN",
                "currency" to "CNY",
                "device" to "6",
                "version" to "3.2.0",
                "token" to "OrE1GPYXyOb0z_w_s1dpq2rsM4t0DjwK_1538967658"
        )

//        OkWrapper.headers("header1" to "a", "header2" to "b")
        OkExt.interceptors()

        btnSend.click {


//            CameraActivity.startFromFragment(this, 1)
//            VersionUpdateUtil.downloadAndInstallApk(context!!, CommonUpdateInfo(download_url = "https://lxj-bama-happy.oss-cn-zhangjiakou.aliyuncs.com/%E5%A4%A9%E5%A4%A9%E5%B9%BF%E5%9C%BA%E8%88%9E-1.0-2020_07_20_11_34_22.apk",
//            update_info = "大萨达所大撒大所大所"))
//            loginData.launchAndSmartPost {
//                val result = "http://47.111.131.25:8080/yezi-api/api/students/vcodeLogin".http()
//                        .params(mapOf(), isJson = true)
//                        .post<HttpResult<User>>()
//                        .await()?.data
//                result
//                loge("result: $result")
//                if (result?.isSuccess() == true) {
//                    val user = result.data
//                    loge("user: $user")
//                } else {
//                    loginData.errMsg = result?.errmsg ?: "登录失败"
//                }
//                result?.data //内部会根据数据自动设置data的状态
//            }
        }
//        LiveEventBus.get(CameraActivity.CaptureVideo).observe(this, Observer {
//            val map = it as Map<String,String>
//            LogUtils.json(map)
//            LogUtils.d(FileUtils.getSize(map["path"]))
//        })
//        LiveEventBus.get(CameraActivity.CaptureImage).observe(this, Observer {
//            LogUtils.d(it)
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            val path  = data?.getStringExtra("path") ?:""
            LogUtils.d("path: $path")
        }
    }

}


