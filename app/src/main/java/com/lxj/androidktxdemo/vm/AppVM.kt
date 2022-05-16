package com.lxj.androidktxdemo.vm

import androidx.lifecycle.ViewModel
import com.lxj.androidktx.livedata.StateLiveData

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
object AppVM: ViewModel(){
    val data = StateLiveData<String>()
}