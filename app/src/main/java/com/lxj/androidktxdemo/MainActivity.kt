package com.lxj.androidktxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import com.lxj.androidktx.core.*
import com.lxj.androidktx.okhttp.*
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.fragment.FragmentExtPage
import com.lxj.androidktxdemo.fragment.ImageViewExtPage
import com.lxj.androidktxdemo.fragment.SpanExtPage
import com.lxj.androidktxdemo.fragment.ViewExtPage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception


data class UserTest(
        var name: String,
        var age: Int
)

data class RestResult(
        var code: Int = 0,
        var message: String = ""
)

class MainActivity : AppCompatActivity() {
    val pages = arrayListOf(
            PageInfo("Span相关", SpanExtPage()),
            PageInfo("View相关", ViewExtPage()),
            PageInfo("ImageView相关", ImageViewExtPage()),
            PageInfo("Fragment相关", FragmentExtPage())

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount() = pages.size
            override fun getItem(position: Int) = pages[position].page
            override fun getPageTitle(position: Int) = pages[position].title
        }
        tabLayout.setupWithViewPager(viewPager)

        toast("测试短吐司")
        longToast("测试长吐司")

        UserTest("李晓俊", 25).toJson().v()
        "{\"age\":25,\"name\":\"李晓俊\"}".toBean<User>().toString().w()
        "[{\"age\":25,\"name\":\"李晓俊\"}]".toBean<List<User>>().toString().e()


        GlobalScope.launch {
//            val deferred = async {
//                "111111111111".e()
//                return@async "aaa"
//            }.await()
            "start coroutine...".e()
            async {
//                try {
//                    val result =
//                            "https://api.gulltour.com/v1/common/nations1".http()
//                                    .headers("a" to 1, "b" to 33)
//                                    .params("token" to 110312039)
//                                    .get<RestResult>()
//                    "coroutine threadId: ${Thread.currentThread().id} result: $result".e()
//                }catch (e: Exception){
//                    "error occur ---->".e()
//                }
            }

//            "https://api.gulltour.com/v1/common/nations".http("111").get<String>()
//            OkWrapper.cancel("111")
            "end coroutine...".e()

        }

        "https://api.gulltour.com/v1/common/nations".http("111")
                .headers("a" to 1, "b" to 33)
                .params("token" to "d8sa9d88sd8as90d8s"
                ,"uid" to 12)
                .post(object : HttpCallback<String>{
                    override fun onSuccess(t: String) {
                    }
                    override fun onFail(e: IOException) {
                        "callback onFail: ${e.message}".e()
                    }
                })
    }



}
