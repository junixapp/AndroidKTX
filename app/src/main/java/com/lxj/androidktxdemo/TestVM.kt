package com.lxj.androidktxdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils

class TestVM( handler: SavedStateHandle): ViewModel() {
    val num : MutableLiveData<String> by lazy { handler.getLiveData("num") }
    val list = listOf("a", "b", "c", "d", "e", "f", "g")
    fun test(){
        num.postValue(list.random())
    }

    override fun onCleared() {
        super.onCleared()
        LogUtils.e("onCleared")
    }
}
open class BaseVM(handler: SavedStateHandle): ViewModel(){

}
class T (h: SavedStateHandle): BaseVM(h){
    val num : MutableLiveData<String> by lazy { h.getLiveData("num") }
}
