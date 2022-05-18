package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.core.*
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.util.concurrent.CopyOnWriteArrayList

data class ListWrapper<T>(
    var records: List<T> = listOf<T>(),
    var total: Int = 10,
    var current: Int = 1,
    var pages: Int = 0
) : Serializable

/**
 * 注意一定要实现getDiffCallback，否则数据更新只能采用 notifyDataSetChanged()
 */
abstract class PageListVM<T>() : ViewModel(),
    OnRefreshLoadMoreListener {
    var firstLoad = true
    var page = 1
    var hasMore = true

    var listData = StateLiveData<CopyOnWriteArrayList<T>>()
    var oldData = arrayListOf<T>()
    init {
        listData.value = CopyOnWriteArrayList()
    }

    var onRefreshCB: (() -> Unit)? = null
    var onLoadMoreCB: (() -> Unit)? = null
    open fun bindRecyclerView(
        owner: LifecycleOwner,
        rv: RecyclerView?,
        smartRefresh: SmartRefreshLayout?,
        stateLayout: StateLayout? = null,
        firstShowLoading: Boolean = false,
        autoLoadData: Boolean = true,
        onRefresh: (() -> Unit)? = null,
        onLoadMore: (() -> Unit)? = null,
        onDataUpdate: (() -> Unit)? = null,
    ) {
        onRefreshCB = onRefresh
        onLoadMoreCB = onLoadMore
        listData.observe(owner, Observer {
            val diffCallback = getDiffCallback(oldData, it)
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
                    if(listData.value!!.isNullOrEmpty()) stateLayout?.showEmpty()
                    else stateLayout?.showContent()
                }
                StateLiveData.State.Error -> {
                    if(listData.value!!.isNullOrEmpty()) stateLayout?.showError()
                    else stateLayout?.showContent()
                }
                else -> stateLayout?.showContent()
            }
        })

        listData.state.observe(owner, Observer {
            smartRefresh?.finishRefresh()
            smartRefresh?.finishLoadMore()
            smartRefresh?.setNoMoreData(!hasMore)
        })

        smartRefresh?.setOnRefreshLoadMoreListener(this)
        if (firstShowLoading && stateLayout != null) {
            stateLayout.showLoading()
            if(autoLoadData)refresh()
        } else {
            if(autoLoadData)smartRefresh?.post { smartRefresh.autoRefresh() }
        }
    }

    open fun refresh() {
        page = 1
        load()
        onRefreshCB?.invoke()
    }


    /**
     * 新增数据
     */
    fun insert(t: T, position: Int? = null){
        val list = listData.value!!
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
     * 更新数据，如果直接修改指定位置的bean，会同步更新old；导致old和new是一样的数据。
     * 推荐将目标数据deepCopy()后再进行修改，这样不会同步更新old
     */
    fun update(position: Int, t: T){
        val list = listData.value!!
        if(position < 0 || list.size<= position) return
        updateOldData()
        list[position] = t
        listData.postValueAndSuccess(list)
    }

    /**
     * 删除数据
     */
    fun remove(position: Int){
        val list = listData.value!!
        if(position < 0 || list.size<= position) return
        updateOldData()
        list.removeAt(position)
        listData.postValueAndSuccess(list)
    }

    /**
     * 清空数据
     */
    fun clear(){
        val list = listData.value!!
        if(list.isEmpty()) return
        updateOldData()
        list.clear()
        listData.postValueAndSuccess(list)
    }

    open fun loadMore() {
        if (hasMore) {
            page += 1
            load()
            onLoadMoreCB?.invoke()
        }
    }

    abstract fun load()

    open fun processData(listWrapper: ListWrapper<T>?, nullIsEmpty: Boolean = false) {
        firstLoad = false
        if (listWrapper != null) {
            if (page == 1) listData.value!!.clear()
            val list = listData.value
            updateOldData()
            if (!listWrapper.records.isNullOrEmpty()) {
                hasMore = true
                list?.addAll(listWrapper.records)
                listData.postValueAndSuccess(list!!)
            } else {
                //listWrapper数据为空
                hasMore = false
                if(list!!.isEmpty()) listData.postEmpty(list)
                else listData.postValueAndSuccess(list)
            }
        } else {
            val list = listData.value
            updateOldData()
            if(list.isNullOrEmpty()){
                if (nullIsEmpty) listData.postEmpty(list)
                else listData.postError()
            }else{
                if (nullIsEmpty) listData.postValueAndSuccess(list)
                else listData.postError()
            }
        }
    }

    /**
     * 由于泛型无法继承，该方法必须子类来实现，实现的模板代码如下：
     * oldData = listData.value!!.deepCopy<ArrayList<T>>()
     */
    open fun updateOldData(){
        if(getDiffCallback(oldData, listData.value!!)==null) return
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

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

    fun reset(){
        page = 1
        hasMore = true
        listData.postValueAndSuccess(CopyOnWriteArrayList())
    }
}