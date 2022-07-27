package com.lxj.androidktxdemo.vm

import androidx.lifecycle.viewModelScope
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

class UserVM: PageListVM<User>() {
    override fun load() {
        viewModelScope.launch {
            delay(1000)
            //mock data
            val data = arrayListOf<User>()
            if(listData.value!!.size < 30){
                (0..4).forEach {
                    data.add(User(name = "分页-${Random.nextInt(10000)}"))
                }
            }
            processData(ListWrapper(records = data, total = 1000, current = page, pages = 100))
        }
    }

    override fun getDiffCallback(old: List<User>, new: List<User>): DiffCallback<User>? {
        return UserDiffCallback(oldData, new)
    }

    override fun updateOldData() {
        oldData = listData.value!!.deepCopy<ArrayList<User>>()
    }
}