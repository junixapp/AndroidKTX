package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.lxj.androidktx.core.DiffCallback
import com.lxj.androidktx.core.diffUpdate
import com.lxj.androidktx.core.observeState
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.io.Serializable

data class ListWrapper<T>(
    var records: List<T> = arrayListOf(),
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
    var listData = StateLiveData<ArrayList<T>>()

    init {
        listData.value = arrayListOf()
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
            val diffCallback = getDiffCallback()
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
                StateLiveData.State.Empty -> stateLayout?.showEmpty()
                StateLiveData.State.Error -> stateLayout?.showError()
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
    open fun getDiffCallback(): DiffCallback<T>? = null

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

    fun reset(){
        page = 1
        hasMore = true
        listData.postValueAndSuccess(arrayListOf())
    }
}