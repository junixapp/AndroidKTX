package com.lxj.androidktxdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.lxj.androidktx.bus.LiveDataBus
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.fragment.*
import kotlinx.android.synthetic.main.activity_main.*


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
            PageInfo("RecyclerView相关", RecyclerViewExtDemo())

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

        toast("测试短吐司")
        longToast("测试长吐司")

        UserTest("李晓俊", 25).toJson().v()
        "{\"age\":25,\"name\":\"李晓俊\"}".toBean<User>().toString().w()
        "[{\"age\":25,\"name\":\"李晓俊\"}]".toBean<List<User>>().toString().e()


        LiveDataBus.with<String>("key1").observe(this, observer = Observer {
            it?.v()
        })

        LiveDataBus.with<User>("key2").observe(this, Observer {
            it?.toJson()?.v()
        })


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

        mmkv().clearAll()

        mmkv().addToList("a", "哈哈")
        mmkv().addToList("a", "cc")
        mmkv().addToList("a", "呵呵")
        mmkv().addToList("a", "黄河")
        mmkv().addToList("a", "黄河")
        mmkv().addToList("a", "长江")
        mmkv().addToList("a", "长江")
//        mmkv().addToList("a", User(name = "ll", age = 11))
//        mmkv().addToList("a", User(name = "aa", age = 22))
//        mmkv().addToList("a", User(name = "bb", age = 33))
//        mmkv().addToList("a", User(name = "cc", age = 44))
//        mmkv().addToList("a", User(name = "aa", age = 22))

        "1: ${mmkv().getList<String>("a")}".e()

//        mmkv().removeFromList("a", User(name = "ll", age = 11))
        mmkv().removeFromList("a", "哈哈")
        "2: ${mmkv().getList<String>("a")}".e()



//        val stateLiveData = StateLiveData<String>()
//        stateLiveData.launchAndSmartPost {
//            "xxx".http().get<String>().await()
//        }

    }


}
