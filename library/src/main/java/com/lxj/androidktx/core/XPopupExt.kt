package com.lxj.androidktx.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * 绑定LiveData的状态
 */
fun LoadingPopupView.bindState(state: StateLiveData.State,
                               onLoading: (()->Unit)? = null,
                               onSuccess: (()->Unit)? = null,
                               onError: (()->Unit)? = null,
                               onEmpty: (()->Unit)? = null){
    when(state){
        StateLiveData.State.Loading->{
            show()
            onLoading?.invoke()
        }
        StateLiveData.State.Success->{
            delayDismissWith(500){
                onSuccess?.invoke()
            }
        }
        StateLiveData.State.Empty->{
            delayDismissWith(500){
                onEmpty?.invoke()
            }
        }
        else->{
            delayDismissWith(500){
                onError?.invoke()
            }
        }
    }
}

/**
 * 直接监听state状态
 */
fun LoadingPopupView.observeState(owner: LifecycleOwner,
                                  state: MutableLiveData<StateLiveData.State>,
                                  onLoading: (()->Unit)? = null,
                                  onSuccess: (()->Unit)? = null,
                                  onError: (()->Unit)? = null,
                                  onEmpty: (()->Unit)? = null){
    state.observe(owner, Observer<StateLiveData.State> {
        bindState(it, onLoading = onLoading, onSuccess = onSuccess,
                onError = onError, onEmpty = onEmpty)
    })
}