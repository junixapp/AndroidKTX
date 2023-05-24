package com.lxj.androidktxdemo.vm

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.base.ListVM
import com.lxj.androidktx.base.ListWrapper
import com.lxj.androidktx.base.PageListVM
import com.lxj.androidktx.core.DiffCallback
import com.lxj.androidktx.core.deepCopy
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.fragment.UserDiffCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class UserVM: ListVM<User>() {
    override fun load() {
        LogUtils.eTag("tag", "loaddata")
        viewModelScope.launch {
            delay(2000)
            //mock data
            val data = arrayListOf<User>()
            if(listData.value!!.size < 30){
                (0..4).forEach {
                    data.add(User(name = "分页-${Random.nextInt(10000)}"))
                }
            }
            insertList(data)
        }
    }

    override fun getDiffCallback(old: List<User>, new: List<User>): DiffCallback<User>? {
        return UserDiffCallback(oldData, new)
    }

    override fun updateOldData() {
        oldData = listData.value!!.deepCopy<ArrayList<User>>()
    }
}