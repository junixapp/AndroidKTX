package com.lxj.androidktxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import com.lxj.androidktx.AndroidKtxConfig
import com.lxj.androidktxdemo.fragment.StringExtPage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val pages = arrayListOf(
            PageInfo("String扩展", StringExtPage())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidKtxConfig.init()

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount() = pages.size
            override fun getItem(position: Int) = pages[position].page
            override fun getPageTitle(position: Int) = pages[position].title
        }
    }
}
