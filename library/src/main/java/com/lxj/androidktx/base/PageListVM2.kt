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

/**
 * 高效的列表数据更新模型
 */
abstract class PageListVM2<T>() : ViewModel(),
    OnRefreshLoadMoreListener {
    var page = 1
    var hasMore = true
    var listData = StateLiveData<ListData<T>>()

    init {
        listData.value = ListData()
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
        stateLayout?.observeState(owner, listData)
        listData.observe(owner, Observer {
            when(it.changeAction){
                ListDataChange.Change->rv?.adapter?.notifyItemRangeChanged(it.changeRange.start, it.changeRange.count)
                ListDataChange.Insert->rv?.adapter?.notifyItemRangeInserted(it.changeRange.start, it.changeRange.count)
                ListDataChange.Remove->rv?.adapter?.notifyItemRangeRemoved(it.changeRange.start, it.changeRange.count)
            }
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
        if (listWrapper != null) {
            if (!listWrapper.records.isNullOrEmpty()) {
                hasMore = true
                if(page==1 && listData.value!!.list.size>0){
                    listData.postValueAndSuccess(listData.value!!.fullReplace(listWrapper.records))
                }else{
                    listData.postValueAndSuccess(listData.value!!.insertRange(listWrapper.records))
                }
            } else {
                //listWrapper数据为空
                hasMore = false
                listData.postValueAndSuccess(listData.value!!.insertRange(listOf()))
            }
        } else {
            listData.postError()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

    fun reset(){
        page = 1
        hasMore = true
        listData.postValueAndSuccess(listData.value!!.fullReplace(listOf()))
    }
}