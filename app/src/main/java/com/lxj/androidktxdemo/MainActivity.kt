package com.lxj.androidktxdemo

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.BaseActivity
import com.lxj.androidktx.core.*
import com.lxj.share.Share
import com.lxj.androidktxdemo.databinding.ActivityMainBinding
import com.lxj.androidktxdemo.entity.PageInfo
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.entity.UserGender
import com.lxj.androidktxdemo.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis


data class UserTest(
        var name: String,
        var age: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserTest> {
        override fun createFromParcel(parcel: Parcel): UserTest {
            return UserTest(parcel)
        }

        override fun newArray(size: Int): Array<UserTest?> {
            return arrayOfNulls(size)
        }
    }
}

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
            PageInfo("九宫格View", NineGridViewDemo()),
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


        Thread {
            val time = measureTimeMillis {
                (1..1000).forEach {
                    sp().putObject("aa", UserTest(name = "lxj", age = 30+it))
                }
            }
            LogUtils.e("sp time: $time  result: ${sp().getObject<UserTest>("aa")}")
        }.start()

        Thread {
            val time = measureTimeMillis {
                (1..1000).forEach {
                    mmkv().putParcelable("aa1", UserTest(name = "lxj", age = 30+it))
                }
            }
            LogUtils.e("mmkv time: $time  result: ${mmkv().getParcelable<UserTest>("aa1")}")
        }.start()

    }

    override fun initData() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Share.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        doubleBackToFinish()
    }
}
