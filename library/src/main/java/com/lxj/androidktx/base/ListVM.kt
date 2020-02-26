package com.lxj.androidktx.base

import androidx.lifecycle.ViewModel
import com.lxj.androidktx.livedata.StateLiveData


abstract class ListVM<T> :ViewModel(){
    var page = 1
    var hasMore = true
    var listData = StateLiveData<ArrayList<T>>()


    init {
        listData.value = arrayListOf<T>()
    }

    fun refresh(){
        page = 1
        load()
    }
    fun loadMore(){
        if(hasMore){
            page += 1
            load()
        }
    }
    abstract fun load()
}