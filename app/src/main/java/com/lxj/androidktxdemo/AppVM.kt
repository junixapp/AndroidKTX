package com.lxj.androidktxdemo

import androidx.lifecycle.ViewModel
import com.lxj.androidktx.livedata.StateLiveData

/**
 * Description:
 * Create by dance, at 2019/7/11
 */
object AppVM: ViewModel(){
    val data = StateLiveData<String>()
}