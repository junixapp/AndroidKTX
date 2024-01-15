package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.statelayout.StateLayout
import java.lang.IllegalArgumentException
import java.util.concurrent.CopyOnWriteArrayList


/**
 * 注意一定要实现getDiffCallback，否则数据更新只能采用 notifyDataSetChanged()
 */
abstract class ListVM<T>() : ViewModel(){
    var firstLoad = true
    var listData = StateLiveData<CopyOnWriteArrayList<T>>(CopyOnWriteArrayList())
    var oldData = arrayListOf<T>()

    open fun bindRecyclerView(
        owner: LifecycleOwner,
        rv: RecyclerView?,
        stateLayout: StateLayout? = null,
        firstShowLoading: Boolean = true,
        autoLoadData: Boolean = true,
        onDataUpdate: (() -> Unit)? = null,
    ) {
        listData.observe(owner, Observer {
            firstLoad = false
            val diffCallback = getDiffCallback(oldData, it ?: listOf())
            if(diffCallback!=null){
                rv?.diffUpdate(diffCallback)
            }else{
                rv?.adapter?.notifyDataSetChanged()
            }
            onDataUpdate?.invoke()
        })
        listData.state.observe(owner, Observer {
            when(it){
                StateLiveData.State.Loading -> {
                    if(firstLoad && firstShowLoading){
                        stateLayout?.showLoading()
                    }
                }
                StateLiveData.State.Empty -> {
                    if(listData.value.isNullOrEmpty()) stateLayout?.showEmpty()
                    else stateLayout?.showContent()
                }
                StateLiveData.State.Error -> {
                    if(listData.value.isNullOrEmpty()) {
                        stateLayout?.showError()
                    } else {
                        stateLayout?.showContent()
                    }
                }
                else -> stateLayout?.showContent()
            }
        })
        if (firstLoad && firstShowLoading && stateLayout != null) {
            stateLayout.showLoading()
            if(autoLoadData)load()
        } else {
            if(autoLoadData) load()
        }
    }

    /**
     * 新增数据
     */
    fun insert(t: T, position: Int? = null){
        val list = listData.value ?: return
        if(position ==null){
            updateOldData()
            list.add(t)
            listData.postValueAndSuccess(list)
        }else if(position>=0 && list.size>position){
            updateOldData()
            list.add(position!!, t)
            listData.postValueAndSuccess(list)
        }
    }

    /**
     * 新增数据
     */
    fun insertList(t: List<T>, position: Int? = null){
        val list = listData.value ?: return
        if(position ==null){
            updateOldData()
            list.addAll(t)
            listData.postValueAndSuccess(list)
        }else if(position>=0 && list.size>position){
            updateOldData()
            list.addAll(position!!, t)
            listData.postValueAndSuccess(list)
        }
    }

    /**
     * 更新数据，如果直接修改指定位置的bean，会同步更新old；导致old和new是一样的数据。
     * 注意：需要将目标数据deepCopy()后再进行修改，这样不会同步更新old，产生diff
     * 如果直接修改原数据，则old数据也会修改，导致old和new数据一样，不会触发UI更新
     */
    fun update(position: Int, t: T){
        val list = listData.value ?: return
        if(position < 0 || list.size<= position) return
        updateOldData()
        list[position] = t
        listData.postValueAndSuccess(list)
    }

    fun replaceList(newList: List<T>){
        updateOldData()
        listData.value?.clear()
        listData.value?.addAll(newList)
        listData.postValueAndSuccess(listData.value?: CopyOnWriteArrayList())
    }


    /**
     * 删除数据
     */
    fun remove(position: Int){
        val list = listData.value ?: return
        if(position < 0 || list.size<= position) return
        updateOldData()
        list.removeAt(position)
        listData.postValueAndSuccess(list)
    }

    /**
     * 清空数据
     */
    fun clear(){
        val list = listData.value ?: return
        if(list.isEmpty()) return
        updateOldData()
        list.clear()
        listData.postValueAndSuccess(list)
    }

    abstract fun load()

    /**
     * 由于泛型无法继承，该方法必须子类来实现，实现的模板代码如下：
     * oldData = listData.value!!.deepCopy<ArrayList<T>>()
     */
    open fun updateOldData(){
        if(getDiffCallback(oldData, listData.value?: listOf())==null) return
        throw IllegalArgumentException("updateOldData方法未实现，实现方式固定为：oldData = listData.value!!.deepCopy<ArrayList<T>>() ")
    }
    
    /**
     * 需要同时配套实现 updateOldData() 方法，实现代码为固定写法 oldData = listData.value!!.deepCopy<ArrayList<T>>() 。
     * demo如下：
     * class UserDiffCallback(oldData: List<User>?, newData: List<User>?) : DiffCallback<User>(oldData, newData) {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                if(oldData.isNullOrEmpty() || newData.isNullOrEmpty()) return false
                return oldData!![oldItemPosition].id == newData!![newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldData!![oldItemPosition].name == newData!![newItemPosition].name
            }
            //局部更新
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
     */
    open fun getDiffCallback(old: List<T>, new: List<T>): DiffCallback<T>? = null

}