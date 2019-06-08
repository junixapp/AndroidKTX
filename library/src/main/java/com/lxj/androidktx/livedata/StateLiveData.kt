package com.lxj.androidktx.livedata

import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

fun <T> StateLiveData<T>.launch(block: suspend CoroutineScope.() -> Unit): Job {
    postLoading()
    return GlobalScope.launch(block = block)
}