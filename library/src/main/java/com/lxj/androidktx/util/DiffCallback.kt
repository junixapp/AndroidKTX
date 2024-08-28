package com.lxj.androidktx.util

import androidx.recyclerview.widget.DiffUtil


/**
 * 示例代码如下：
 * class UserDiffCallback(var oldData: List<User>?, var newData: List<User>?) : DiffUtil.Callback() {
override fun getOldListSize() = oldData?.size ?: 0
override fun getNewListSize() = newData?.size ?: 0

override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
if(oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
return oldData!![oldItemPosition].id == newData!![newItemPosition].id
}

override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
return oldData!![oldItemPosition].name == newData!![newItemPosition].name
}

//局部更新 areItemsTheSame==true && areContentsTheSame==false 调用
override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
val oldItem = oldData!![oldItemPosition]
val newItem = newData!![newItemPosition]
val bundle = Bundle()
if(oldItem.name != newItem.name){
bundle.putString("name", newItem.name)
}
return bundle
}
}
 *
 */
open class DiffCallback<T>(var oldData: List<T>?, var newData: List<T>?) : DiffUtil.Callback() {
    override fun getOldListSize() = oldData?.size ?: 0
    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
        return false
    }

    //局部更新
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = null
}
