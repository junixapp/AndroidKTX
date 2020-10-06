package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.lxj.androidktx.core.updateData
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.statelayout.StateLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import java.io.Serializable

data class ListWrapper<T>(
        var records: List<T> = arrayListOf(),
        var total: Int = 10,
        var current: Int = 1,
        var pages: Int = 0
): Serializable

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
        emptyText: String = "暂无数据",
        onDataUpdate: (()->Unit)? = null
    ) {
        listData.observe(owner, Observer {
            rv?.adapter?.notifyDataSetChanged()
            onDataUpdate?.invoke()
            if(stateLayout!=null){
                stateLayout.config(emptyText = emptyText)
                if(listData.value.isNullOrEmpty()) stateLayout.showEmpty()
                else stateLayout.showContent()
            }
        })

        listData.state.observe(owner, Observer {
            smartRefresh?.finishRefresh()
            smartRefresh?.finishLoadMore()
            smartRefresh?.setNoMoreData(!hasMore)
        })

        smartRefresh?.setOnRefreshLoadMoreListener(this)
        smartRefresh?.post { smartRefresh.autoRefresh() }
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
            if (page == 1) listData.value?.clear()
            val list = listData.value
            if (!listWrapper.records.isNullOrEmpty()) {
                hasMore = true
                list?.addAll(listWrapper.records)
                listData.postValueAndSuccess(list!!)
            } else {
                hasMore = false
                listData.postEmpty(list)
            }
        } else {
            if(nullIsEmpty) listData.postEmpty(null)
            else listData.postError()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

}