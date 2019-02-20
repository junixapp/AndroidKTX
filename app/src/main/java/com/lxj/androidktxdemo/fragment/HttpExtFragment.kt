package com.lxj.androidktxdemo.fragment

import android.arch.lifecycle.*
import android.os.Environment
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.d
import com.lxj.androidktx.core.v
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_http_ext.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.math.log

/**
 * Description: Okhttp扩展
 * Create by dance, at 2019/1/4
 */
class HttpExtFragment : BaseFragment() {
    private var vm: HttpExtVM? = null

    override fun getLayoutId() = R.layout.fragment_http_ext
    override fun initView() {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/t.jpg")
        vm = ViewModelProviders.of(this).get(HttpExtVM::class.java)

        vm!!.data.observe(this, Observer<String> {

            tvResponse.text = if (it == null) "请求失败" else JSONObject(it).toString(2)
        })





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

                val t = "http://api.sandbox.gulltour.com/v1/weibo/upload".http()
                        .params("weiboImage[]" to file)
                        .post<String>()
                        .await()

            }
            //callback style
//            "https://api.gulltour.com/v1/common/nations".http().get(object : HttpCallback<RestResult<List<DemoData>>> {
//                override fun onSuccess(t: RestResult<List<DemoData>>) {
//                    "size：${t.data.size}".d()
//                }
//
//                override fun onFail(e: IOException) {
//                    super.onFail(e)
//                }
//            })
        }
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

