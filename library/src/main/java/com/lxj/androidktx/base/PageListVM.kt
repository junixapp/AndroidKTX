package com.lxj.androidktx.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.lxj.androidktx.core.updateData
import com.lxj.androidktx.livedata.StateLiveData
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

    fun bindRecyclerView(
        owner: LifecycleOwner,
        rv: RecyclerView?,
        smartRefresh: SmartRefreshLayout?,
        onDataUpdate: (()->Unit)? = null
    ) {
        listData.observe(owner, Observer {
            rv?.updateData(listData.value!!)
            onDataUpdate?.invoke()
        })

        listData.state.observe(owner, Observer {
            smartRefresh?.finishRefresh()
            smartRefresh?.finishLoadMore()
            smartRefresh?.setNoMoreData(!hasMore)
        })

        smartRefresh?.setOnRefreshLoadMoreListener(this)
        smartRefresh?.post { smartRefresh.autoRefresh() }
    }

    fun refresh() {
        page = 1
        load()
    }

    fun loadMore() {
        if (hasMore) {
            page += 1
            load()
        }
    }

    abstract fun load()

    fun processData(listWrapper: ListWrapper<T>?, nullIsEmpty: Boolean = false) {
        if (listWrapper != null) {
            hasMore = page < listWrapper.pages
            var list = listData.value
            if (page == 1) list = arrayListOf()

            if (!listWrapper.records.isNullOrEmpty()) {
                list?.addAll(listWrapper.records)
                listData.postValueAndSuccess(list!!)
            } else {
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