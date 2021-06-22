package com.lxj.androidktxdemo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Environment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.SmartViewModel
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktx.picker.ImagePicker
import com.lxj.androidktx.util.DirManager
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.User
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.android.synthetic.main.fragment_http_ext.*
import java.io.File
import kotlin.concurrent.fixedRateTimer

/**
 * Description: Okhttp扩展
 * Create by dance, at 2019/1/4
 */
class HttpExtFragment : BaseFragment() {
    val loginData = StateLiveData<User>()
    val imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560151504361&di=ce11648353ac380140bce2579683ed59&imgtype=0&src=http%3A%2F%2Fs14.sinaimg.cn%2Fmw690%2F001xcz9rzy6PqH78yLPed%26690"
    val zipFile = "http://samples.mplayerhq.hu/V-codecs/2vuy.avi"
    val demoVM: DemoVM by lazy { getVM(DemoVM::class.java) }
    val loadingDialog : LoadingPopupView by lazy { XPopup.Builder(requireContext()).dismissOnTouchOutside(false)
            .asLoading()}
    override fun getLayoutId() = R.layout.fragment_http_ext
    override fun initView() {


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
        loadingDialog.observeState(this, demoVM.testData, onSuccess = {
            tvHttpResult.text = demoVM.testData.value?:""
        }, onError = { ToastUtils.showShort("数据为空") })
        demoVM.downloadProgressData.observe(this, Observer {
            var str = "下载进度：${it.progress}%"
            if(it.file!=null) str = "${str}  文件大小：${FileUtils.getSize(it.file)}  路径: ${it.file?.absolutePath}"
            tvHttpResult.text = str
        })
        btnSend.click {
//            zipFile.http().savePath("${DirManager.tempDir}/xxx.avi")
//                .downloadListener(onProgress = {progressInfo ->
//                    LogUtils.e("下载进度：${progressInfo!!.percent}")
//                })
//                .get(object : HttpCallback<File>{
//                    override fun onSuccess(t: File) {
//                        LogUtils.e("下载完毕，文件大小：${FileUtils.getSize(t)}  文件路径: ${t.absolutePath}")
//                    }
//                })

            demoVM.loadBaiduHomePage()

//            ImagePicker.startRecord(this, 1)
//            WebActivity.start(url = "https://click.lixiaojun.xin/article/?posid=1")

//            CameraActivity.startFromFragment(this, 1)
//            VersionUpdateUtil.downloadAndInstallApk(context!!, CommonUpdateInfo(download_url = "https://lxj-bama-happy.oss-cn-zhangjiakou.aliyuncs.com/%E5%A4%A9%E5%A4%A9%E5%B9%BF%E5%9C%BA%E8%88%9E-1.0-2020_07_20_11_34_22.apk",
//            update_info = "大萨达所大撒大所大所"))
//            loginData.launchAndSmartPost {
//                val result = "http://47.111.131.25:8080/yezi-api/api/students/vcodeLogin".http()
//                        .params(mapOf(), isJson = true)
//                        .customReqBody(RequestBody.create(MediaType.parse("application/octet-stream"),File("")))
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
        btnSend2.click {
            demoVM.downloadRequest()
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
            val path  = ImagePicker.fetchRecordResult(data)

            LogUtils.e("视频路径：$path  文件大小：${FileUtils.getSize(File(path))}")
        }
    }

}


class DemoVM : SmartViewModel(){
    val zipFile = "http://samples.mplayerhq.hu/V-codecs/2vuy.avi"
    val testData = StateLiveData<String>()
    val downloadProgressData = StateLiveData<DownloadInfo>()

    init {
        downloadProgressData.value = DownloadInfo(0, null)
    }

    fun loadBaiduHomePage(){
        testData.launchHttp(this, "https://www.baidu.com"){
            val res = "https://www.baidu.com".http()
                .get<String>().await()
            LogUtils.e("res: $res")
            res
        }
    }

    fun downloadRequest(){
        downloadProgressData.launchHttp(this){
            val file = zipFile.http().savePath("${DirManager.tempDir}/xxx.avi")
                .downloadListener(onProgress = {progressInfo ->
                    LogUtils.e("下载进度：${progressInfo!!.percent}")
                    val info = downloadProgressData.value
                    info!!.progress = progressInfo.percent
                    downloadProgressData.postValueAndSuccess(info)
                }).get<File>().await()
            val info = downloadProgressData.value
            info!!.file = file
            info
        }

    }

}

data class DownloadInfo(
    var progress: Int,
    var file: File?
)

