package com.lxj.androidktxdemo

import android.content.Intent
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.BaseActivity
import com.lxj.androidktx.core.*
import com.lxj.share.Share
import com.lxj.androidktxdemo.databinding.ActivityMainBinding
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.fragment.*
import com.lxj.androidktxdemo.popup.TestPopup
import com.lxj.androidktxdemo.vm.TestVM
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


data class UserTest(
        var name: String,
        var age: Float,
)

data class RestResult(
        var code: Int = 0,
        var message: String = ""
)

class MainActivity : BaseActivity() {
    val pages = arrayListOf(
            PageInfo("Span相关", SpanExtPage()),
            PageInfo("View相关", ViewExtPage()),
            PageInfo("ImageView相关", ImageViewExtPage()),
            PageInfo("Fragment相关", FragmentExtPage()),
            PageInfo("Http相关", HttpExtFragment()),
            PageInfo("LiveDataBus", LiveDataBusDemo()),
            PageInfo("RecyclerView相关", RecyclerViewExtDemo()),
            PageInfo("ViewPager2", ViewPager2Demo()),
            PageInfo("播放器", PlayerFragment()),
            PageInfo("Uploader", UploaderFragment()),
    )
    override fun getLayoutId() = R.layout.activity_main

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) } }

    override fun initView() {
        binding.viewPager.bindFragment(this, fragments = pages.map { it.page!! })
        binding.viewPager.bindTabLayout(tabLayout, pages.map { it.title })
        val testVM = getSavedStateVM(TestVM::class.java)
        testVM.num.observe(this, androidx.lifecycle.Observer {
            btnTest.text = "Random: $it"
        })
        binding.btnTest.click {
            testVM.test()
        }
        val ut =  """
            {
                "name": "dasdsa",
                "age": 98182389985949
            }
        """.trimIndent().toBean<HashMap<String,Any>>()
        LogUtils.e("ut: ${ut.toJson()}")

//        XPopup.Builder(this).asCustom(TestPopup(this)).show()
    }

    override fun initData() {

    }
    val signKey = "babamamababamama"
    fun genSign(): String{
        //1. 生成10000以内的随机数
        val random = Random.Default.nextInt(10000)
        val b = "${10000 - random}".encryptAESBase64(signKey)
        val c = Random.Default.nextInt(10000)
        val json = mapOf<String,Any>(
                "a" to random,
                "b" to b,
                "c" to c,
                "d" to System.currentTimeMillis()
        ).toJson()
        return json.encryptAESBase64(signKey)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Share.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        doubleBackToFinish()
    }
}
