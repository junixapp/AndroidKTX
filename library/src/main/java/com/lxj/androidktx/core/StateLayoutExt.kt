package com.lxj.androidktx.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.statelayout.StateLayout


/**
 * 直接监听state状态
 */
fun StateLayout.observeState(owner: LifecycleOwner,
                                  liveData: StateLiveData<*>,
                                  onLoading: (()->Unit)? = null,
                                  onSuccess: (()->Unit)? = null,
                                  onError: (()->Unit)? = null,
                                  onEmpty: (()->Unit)? = null){
    liveData.state.observe(owner, Observer<StateLiveData.State> {
        when(liveData.state.value){
            StateLiveData.State.Loading->{
                showLoading()
                onLoading?.invoke()
            }
            StateLiveData.State.Success->{
                showContent()
                onSuccess?.invoke()
            }
            StateLiveData.State.Empty->{
                showEmpty()
                onEmpty?.invoke()
            }
            StateLiveData.State.Error->{
                showError()
                onError?.invoke()
            }
        }
    })
}
