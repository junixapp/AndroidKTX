package com.lxj.androidktx.core

import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.xpopup.impl.LoadingPopupView


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