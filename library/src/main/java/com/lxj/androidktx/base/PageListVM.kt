package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
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

abstract class PageListVM<T> : ViewModel(),
    OnRefreshLoadMoreListener {
    var page = 1
    var hasMore = true
    var listData = StateLiveData<ArrayList<T>>()

    init {
        listData.value = arrayListOf()
    }

    open fun bindRecyclerView(
        owner: LifecycleOwner,
        rv: RecyclerView?,
        smartRefresh: SmartRefreshLayout?,
        stateLayout: StateLayout? = null,
        firstShowLoading: Boolean = false,
        onDataUpdate: (() -> Unit)? = null
    ) {
        stateLayout?.observeState(owner, listData)
        listData.observe(owner, Observer {
            rv?.adapter?.notifyDataSetChanged()
            onDataUpdate?.invoke()
        })

        listData.state.observe(owner, Observer {
            smartRefresh?.finishRefresh()
            smartRefresh?.finishLoadMore()
            smartRefresh?.setNoMoreData(!hasMore)
        })

        smartRefresh?.setOnRefreshLoadMoreListener(this)
        if (firstShowLoading && stateLayout != null) {
            stateLayout.showLoading()
            refresh()
        } else {
            smartRefresh?.post { smartRefresh.autoRefresh() }
        }
    }

    open fun refresh() {
        page = 1
        load()
    }

    open fun loadMore() {
        if (hasMore) {
            page += 1
            load()
        }
    }

    abstract fun load()

    open fun processData(listWrapper: ListWrapper<T>?, nullIsEmpty: Boolean = false) {
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

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

}