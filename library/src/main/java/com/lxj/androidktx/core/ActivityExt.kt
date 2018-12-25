package com.lxj.androidktx.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View

/**
 * Description: Activity相关
 * Create by lxj, at 2018/12/7
 */

inline fun <reified T> Fragment.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null){
    activity?.startActivity<T>(flag, bundle)
}

inline fun <reified T> Context.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null){
    val intent = Intent(this, T::class.java).apply {
        if(flag!=-1) {
            this.addFlags(flag)
        }else if(this !is Activity){
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle!=null)putExtras(bundle.toBundle())
    }
    startActivity(intent)
}

inline fun <reified T> View.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null){
    context.startActivity<T>(flag, bundle)
}