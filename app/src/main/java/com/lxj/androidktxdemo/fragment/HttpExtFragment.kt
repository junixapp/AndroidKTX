package com.lxj.androidktxdemo.fragment

import android.arch.lifecycle.*
import android.os.Environment
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.d
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktxdemo.R
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

    override fun getLayoutId() = R.layout.fragment_http_ext
    override fun initView() {
        val file = File("${Environment.getExternalStorageDirectory()}/a.txt")
        if(!file.exists()){
            file.createNewFile()
            file.writeText("床前明月光，疑是地上霜")
        }

        vm = ViewModelProviders.of(this).get(HttpExtVM::class.java)

        vm!!.data.observe(this, Observer<String> {

            tvResponse.text = JSONObject(it).toString(2)
        })


        // 全局header
        OkWrapper.headers("header1" to "a",
                "header2" to "b")

//        OkWrapper.headers("header1" to "a", "header2" to "b")
        OkWrapper.interceptors()

        btnSend.click {
            GlobalScope.launch {
                val data = "http://192.168.1.103:3000/json".http(tag = "abc")
                        .headers("device" to "HuaWeiMate20",
                                "abc" to "def")
                        .params("token" to "188sas9cf99a9d",
                                "uid" to 123213, "file" to file)
                        .post<String>()

                vm!!.data.postValue(data)
//              OkWrapper.cancel("abc") // 取消请求
            }
            //callback style
//            "http://192.168.1.103:3000/json".http().get(object : HttpCallback<String> {
//                override fun onSuccess(t: String) {
//                }
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

data class RestResult(
        var message: String = "",
        var code: Int = 0,
        var data: String = ""
)