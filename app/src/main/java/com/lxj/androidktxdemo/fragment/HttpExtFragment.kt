package com.lxj.androidktxdemo.fragment

import android.arch.lifecycle.*
import android.os.Environment
import com.lxj.androidktx.core.*
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktx.widget.TitleBar
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.User
import kotlinx.android.synthetic.main.fragment_http_ext.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.IOException

/**
 * Description: Okhttp扩展
 * Create by dance, at 2019/1/4
 */
class HttpExtFragment : BaseFragment() {
    private var vm: HttpExtVM? = null
    val imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560151504361&di=ce11648353ac380140bce2579683ed59&imgtype=0&src=http%3A%2F%2Fs14.sinaimg.cn%2Fmw690%2F001xcz9rzy6PqH78yLPed%26690"
    val zipFile = "https://github.com/li-xiaojun/XPopup/archive/master.zip"
    override fun getLayoutId() = R.layout.fragment_http_ext
    override fun initView() {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/mnt")
        vm = ViewModelProviders.of(this).get(HttpExtVM::class.java)

        vm!!.data.observe(this, Observer<String> {

            tvResponse.text = if (it == null) "请求失败" else JSONObject(it).toString(2)
        })
        tvResponse.sizeDrawable(dp2px(20f), topDrawable = R.mipmap.ic_launcher)

        // 全局header
        OkWrapper.headers("site" to "CN",
                "name" to "GullMap_world",
                "language" to "zh-CN",
                "currency" to "CNY",
                "device" to "6",
                "version" to "3.2.0",
                "token" to "OrE1GPYXyOb0z_w_s1dpq2rsM4t0DjwK_1538967658"
        )

//        OkWrapper.headers("header1" to "a", "header2" to "b")
        OkWrapper.interceptors()

        val map = hashMapOf<String, String>()
        map.put("xxx", "yyyy")

        btnSend.click {
            GlobalScope.launch {
                //                val data = "http://192.168.1.103:3000/json".http(tag = "abc")
//                        .headers("device" to "HuaWeiMate20",
//                                "abc" to "def")
//                        .params(
//                                "token" to "188sas9cf99a9d",
//                                "uid" to 123213
////                                "file" to  file
//                        ).params(map)
//                        .post<String>()
//
//                vm!!.data.postValue(data)
////              OkWrapper.cancel("abc") // 取消请求

//                val t = "http://api.sandbox.gulltour.com/v1/weibo/upload".http()
//                        .params("weiboImage[]" to file)
//                        .uploadListener(onProgress = {
//                            loge("upload progress: ${it?.percent}")
//                        })
//                        .downloadListener(onProgress = {
//                            loge("download progress: ${it?.percent}")
//                        })
//                        .post<String>()
//                        .await()

//                "https://api.gulltour.com/v1/common/nations".http().get<String>().await()


//                zipFile.http().savePath(Environment.getExternalStorageDirectory().toString() + "/xxx.zip")
//                        .downloadListener(onProgress = {
//                            loge("download progress: ${it?.percent}")
//                        })
//                        .get<File>()
//                        .await()
//                loge("done....")

                "http://39.107.78.243:8095/captcha".http()
                        .params(
                                "phone" to "15172321248",
                                "type" to "0"
                        ).post<String>().await()
            }
            //callback style
//            val json = User(name = "lxj", age = 33).toJson()
//            "https://api.gulltour.com/v1/common/nations".http()
//                    .uploadListener(onProgress = {
//                        loge("progress: ${it?.percent}")
//                    })
//                    .putJson<String>(json, object : HttpCallback<String> {
//                override fun onSuccess(t: String) {
//                    toast(t)
//                }
//                override fun onFail(e: IOException) {
//                    super.onFail(e)
//                }
//            })
        }

        btnSend2.click {
            GlobalScope.launch {
                "http://39.107.78.243:8095/loginC".http()
                        .params(
                                "phone" to "15172321248",
                                "codenum" to "343210"
                        ).post<String>().await()
            }
        }

        tt.setup(leftImageRes = R.mipmap.fx, title = "aaaaaaaaaaaa")
        //titleBar点击事件
        tt.clickListener(object : TitleBar.ClickListener {
            override fun leftTextClick() {
                toast("left text")
            }

            override fun leftImageClick() {
                toast("left image")
            }

            override fun rightTextClick() {
                toast("right text")
            }

            override fun rightImageClick() {
                toast("right image")
            }

            override fun rightImage2Click() {
                toast("right image22")
            }

            override fun rightImage3Click() {
                toast("right image333")
            }
        })

        slWeChat.rightTextView().click {
            toast("点击了")
        }

        sl.setup(rightText = "哈哈")
    }

    class HttpExtVM : ViewModel() {
        val data = MutableLiveData<String>()
    }

}

data class RestResult<T>(
        var message: String = "",
        var code: Int = 0,
        var data: T
)

//"id": 27,
//"name": "中国大陆",
//"code": "CN",
//"nation_flag": "86",
//"pattern": "^1\\d{10}$"
data class DemoData(
        var id: Int,
        var name: String,
        var code: String
)

