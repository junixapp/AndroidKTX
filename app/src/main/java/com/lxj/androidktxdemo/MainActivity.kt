package com.lxj.androidktxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import com.google.gson.reflect.TypeToken
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.fragment.FragmentExtPage
import com.lxj.androidktxdemo.fragment.ImageViewExtPage
import com.lxj.androidktxdemo.fragment.SpanExtPage
import com.lxj.androidktxdemo.fragment.ViewExtPage
import kotlinx.android.synthetic.main.activity_main.*


data class UserTest(
        var name: String,
        var age: Int
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
        "{\"age\":25,\"name\":\"李晓俊\"}".toBean(User::class.java).toString().w()
        "[{\"age\":25,\"name\":\"李晓俊\"}]".toBean( object : TypeToken<List<User>>(){}.type).toString().e()
    }
}
