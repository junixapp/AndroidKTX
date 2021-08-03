package com.lxj.androidktx.core

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.blankj.utilcode.util.ToastUtils
import com.lxj.androidktx.AndroidKTX
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupStatus

/**
 * 绑定LiveData的状态，警惕LoadingView过早关闭的时候，state的状态还未执行
 */
fun BasePopupView.bindState(liveData: StateLiveData<*>,
                               onLoading: (()->Unit)? = null,
                               onSuccess: (()->Unit)? = null,
                               onError: (()->Unit)? = null,
                               onEmpty: (()->Unit)? = null,
                               autoShowError: Boolean = false){
    val delay = XPopup.getAnimationDuration().toLong()+10
    when(liveData.state.value){
        StateLiveData.State.Loading->{
            show()
            onLoading?.invoke()
        }
        StateLiveData.State.Success->{
            //如果状态异常，已经关闭或正在关闭
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                onSuccess?.invoke()
            }else{
                delayDismissWith(delay){
                    onSuccess?.invoke()
                }
            }
        }
        StateLiveData.State.Empty->{
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                //如果已经关闭或正在关闭
                onEmpty?.invoke()
            }else{
                delayDismissWith(delay){
                    onEmpty?.invoke()
                }
            }
        }
        StateLiveData.State.Error->{
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                //如果已经关闭
                if(autoShowError && liveData.errMsg?.isNotEmpty()==true){
                    ToastUtils.showShort(liveData.errMsg)
                    liveData.errMsg = ""
                }
                onError?.invoke()
            }else{
                delayDismissWith(delay){
                    if(autoShowError && liveData.errMsg?.isNotEmpty()==true){
                        ToastUtils.showShort(liveData.errMsg)
                        liveData.errMsg = ""
                    }
                    onError?.invoke()
                }
            }
        }
    }
}

/**
 * 直接监听state状态
 */
fun BasePopupView.observeState(owner: LifecycleOwner,
                                  liveData: StateLiveData<*>,
                                  onLoading: (()->Unit)? = null,
                                  onSuccess: (()->Unit)? = null,
                                  onError: (()->Unit)? = null,
                                  onEmpty: (()->Unit)? = null,
                                  autoShowError: Boolean = false){
    liveData.state.observe(owner, Observer<StateLiveData.State> {
        bindState(liveData, onLoading = onLoading, onSuccess = onSuccess,
                onError = onError, onEmpty = onEmpty, autoShowError = autoShowError)
    })
}

//view model
fun <T: ViewModel> BasePopupView.getVM(clazz: Class<T>) = ViewModelProvider(context as FragmentActivity).get(clazz)

/**
 * saved state view model，要求ViewModel的构造必须接受SavedStateHandle类型的参数，比如：
 * ```
 * class TestVM( handler: SavedStateHandle): ViewModel()
 * ```
 */
fun <T: ViewModel> BasePopupView.getSavedStateVM(clazz: Class<T>) = ViewModelProvider(context as FragmentActivity, SavedStateViewModelFactory(
    AndroidKTX.context as Application, context as FragmentActivity)
).get(clazz)
