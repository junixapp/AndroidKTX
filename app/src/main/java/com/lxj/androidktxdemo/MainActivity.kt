package com.lxj.androidktxdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.widget.LoadingDialog
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


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
            PageInfo("Fragment相关", FragmentExtPage()),
            PageInfo("Http相关", HttpExtFragment()),
            PageInfo("LiveDataBus", LiveDataBusDemo()),
            PageInfo("RecyclerView相关", RecyclerViewExtDemo()),
            PageInfo("ViewPager2", ViewPager2Demo()),
            PageInfo("九宫格View", NineGridViewDemo())


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.bindFragment(fm = supportFragmentManager, fragments = pages.map { it.page!! }, pageTitles = pages.map { it.title })
        tabLayout.setupWithViewPager(viewPager)

//        viewPager.asCard()
//        viewPager.bind(10, bindView = {container, position ->
//            return@bind TextView(this)
//        })

//        toast("测试短吐司")
//        longToast("测试长吐司")

//        LoadingDialog(this).setMessage("阿萨啊").show()

        handler.post { toast("哈哈哈哈哈啊啊啊啊啊啊") }

        """{"age":25,"name":"李晓俊","date":"2020-05-12 13:37:33"}
        """.trimIndent().toBean<User>().toString().logw()
//        """{"age":25,"name":"李晓俊","date":"Mar 12, 1990 00:00:00"}
//        """.trimIndent().toBean<User>().toString().w()
//        "[{\"age\":25,\"name\":\"李晓俊\"}]".toBean<List<User>>().toString().e()



//        // 便捷处理
//        sp().getString("a", "default")
//        sp().getBoolean("b", false)
//        sp(name = "xxx.cfg").getBoolean("b", false)
//        //...
//
//        // 批处理
//        sp().edit {
//            putString("a", "1")
//            putBoolean("b", true)
//        }
//        // 清楚
//        sp().clear()

//        val stateLiveData = StateLiveData<String>()
//        stateLiveData.launchAndSmartPost {
//            "xxx".http().get<String>().await()
//        }


//        val u1 = UserTest("李晓俊", 25)
//        val u3 = u1.copy()
//        loge("u3： $u3   u1==u3: ${u1===u3}" )
//        val ed = "123456".encryptAES("babamamababamama")
//        loge("ed: ${ed}")
//        loge("data: ${ed.decryptAES("babamamababamama")}")

        loge("gen sign：${genSign()}")


    }
    val signKey = "babamamababamama"
    fun genSign(): String{
        //1. 生成10000以内的随机数
        val random = Random.Default.nextInt(10000)
        val b = "${10000 - random}".encryptAES(signKey)
        val c = Random.Default.nextInt(10000)
        val json = mapOf<String,Any>(
                "a" to random,
                "b" to b,
                "c" to c,
                "d" to System.currentTimeMillis()
        ).toJson()
        return json.encryptAES(signKey)
    }

}
