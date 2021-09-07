package com.lxj.androidktx.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.livedata.LifecycleHandler

/**
 * Description: Activity相关
 * Create by lxj, at 2018/12/7
 */

inline fun <reified T> Fragment.start(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    val intent = Intent(activity, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (bundle != null) putExtras(bundle.toBundle()!!)
    }
    startActivity(intent)
}

inline fun <reified T> Fragment.startForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    val intent = Intent(activity, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (bundle != null) putExtras(bundle.toBundle()!!)
    }
    startActivityForResult(intent, requestCode)
}

inline fun <reified T> Context.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (this !is Activity) {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle != null) putExtras(bundle.toBundle()!!)
    }
    startActivity(intent)
}

inline fun <reified T> View.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    context.startActivity<T>(flag, bundle)
}

inline fun <reified T> View.startForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    (context as Activity).startForResult<T>(flag, bundle, requestCode)
}

inline fun <reified T> Activity.startForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (bundle != null) putExtras(bundle.toBundle()!!)
    }
    startActivityForResult(intent, requestCode)
}

fun FragmentActivity.finishDelay(delay: Long = 1) {
    LifecycleHandler(this).postDelayed({ finish() }, delay)
}

//post, postDelay
fun FragmentActivity.post(action: ()->Unit){
    LifecycleHandler(this).post { action() }
}

fun FragmentActivity.postDelay(delay:Long = 0, action: ()->Unit){
    LifecycleHandler(this).postDelayed({ action() }, delay)
}

//view model
fun <T: ViewModel> FragmentActivity.getVM(clazz: Class<T>) = ViewModelProvider(this).get(clazz)

/**
 * saved state view model，要求ViewModel的构造必须接受SavedStateHandle类型的参数，比如：
 * ```
 * class TestVM( handler: SavedStateHandle): ViewModel()
 * ```
 */
fun <T: ViewModel> FragmentActivity.getSavedStateVM(clazz: Class<T>) = ViewModelProvider(this, SavedStateViewModelFactory(
    AndroidKTX.context as Application, this)
).get(clazz)


inline val FragmentActivity.handler
    get() = LifecycleHandler(this)

inline val Fragment.handler
    get() = LifecycleHandler(this)

/**
 * 将Activity移到前台，需将launchMode设置为SingleTop，否则会创建新实例
 */
fun Activity.moveTaskToFront(){
    val intent = Intent(AndroidKTX.context, this.javaClass)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
    AndroidKTX.context.startActivity(intent)
}