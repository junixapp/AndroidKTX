package com.lxj.androidktx.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Description: 携带状态的LiveData
 * Create by lxj, at 2019/3/6
 */
class StateLiveData<T> : MutableLiveData<T>() {

    enum class State {
        Idle, Loading, Success, Error, Empty
    }

    val state = MutableLiveData<State>()

    init {
        clearState()
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }

    fun clearState() {
        state.postValue(State.Idle)
    }

    fun postLoading() {
        state.postValue(State.Loading)
    }

    fun postSuccess() {
        state.postValue(State.Success)
    }

    fun postError() {
        state.postValue(State.Error)
    }

    fun postEmpty() {
        state.postValue(State.Empty)
    }

    fun changeState(s: State) {
        state.postValue(s)
    }
}

/**  some extensions  **/
fun <T> StateLiveData<T>.bindStateLayout(owner: LifecycleOwner, stateLayout: StateLayout) {
    state.observe(owner, Observer {
        when (it) {
            StateLiveData.State.Loading -> stateLayout.showLoading()
            StateLiveData.State.Error -> stateLayout.showError()
            StateLiveData.State.Success -> stateLayout.showContent()
            StateLiveData.State.Empty -> stateLayout.showEmpty()
            else -> stateLayout.showLoading()
        }
    })
}

/**
 * 带绑定StateLayout
 */
fun <T> StateLiveData<T>.observeWithStateLayout(owner: LifecycleOwner, stateLayout: StateLayout, observer: Observer<T>) {
    bindStateLayout(owner, stateLayout)
    observe(owner, observer)
}

/**
 * 智能post值，能根据值进行智能的设置自己的状态，无需手工编写代码
 * @param dataValue 目标值，根据目标值去设置对应的state
 */
fun <T> StateLiveData<T>.smartPost(dataValue: T?){
    if(dataValue==null){
        postError()
    }else if(dataValue is Collection<*> && dataValue.isEmpty()){
        postEmpty()
    }else{
        postValueAndSuccess(dataValue)
    }
}

/**
 * 强大而实用的封装，启动协程执行逻辑（比如网络请求），并对逻辑结果进行智能post。示例如下：
 * launchAndSmartPost {
 *      "https://iandroid.xyz/api".http().get<T>().await()
 * }
 */
fun <T> StateLiveData<T>.launchAndSmartPost(block: suspend CoroutineScope.() -> T?): Job {
    postLoading()
    return GlobalScope.launch { smartPost(block()) }
}