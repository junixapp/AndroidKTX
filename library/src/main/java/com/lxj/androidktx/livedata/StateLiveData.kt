package com.lxj.androidktx.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lxj.statelayout.StateLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Description: 携带状态的LiveData
 * Create by lxj, at 2019/3/6
 */
class StateLiveData<T> : NoStickyLiveData<T>() {

    enum class State {
        Idle, Loading, Success, Error, Empty
    }

    val state = MutableLiveData<State>()
    var errMsg: String? = null

    init {
        state.value = State.Idle
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }
    fun postEmpty(t: T? = null){
        super.postValue(t)
        state.postValue(State.Empty)
    }

    fun clearState() {
        this.errMsg = null
        state.postValue(State.Idle)
    }

    fun postLoading() {
        state.postValue(State.Loading)
    }

    fun postSuccess() {
        state.postValue(State.Success)
    }

    fun postError(error: String? = null, postNull: Boolean = false) {
        if(error?.isNotEmpty()==true) this.errMsg = error
        if(postNull) super.postValue(null)
        state.postValue(State.Error)
    }

    fun changeState(s: State) {
        state.postValue(s)
    }

    fun bindStateLayout(owner: LifecycleOwner, stateLayout: StateLayout) {
        state.observe(owner, Observer {
            when (it) {
                State.Loading -> stateLayout.showLoading()
                State.Error -> stateLayout.showError()
                State.Success -> stateLayout.showContent()
                State.Empty -> stateLayout.showEmpty()
            }
        })
    }

    /**
     * 带绑定StateLayout
     */
    fun observeWithStateLayout(owner: LifecycleOwner, stateLayout: StateLayout, observer: Observer<T>) {
        bindStateLayout(owner, stateLayout)
        observe(owner, observer)
    }

    /**
     * 智能post值，能根据值进行智能的设置自己的状态，无需手工编写代码
     * @param dataValue 目标值，根据目标值去设置对应的state
     * @param nullIsEmpty 是否把null当做Empty状态，默认false
     */
    fun smartPost(dataValue: T?, nullIsEmpty: Boolean = false, postNull: Boolean = false){
        if(dataValue==null){
            if(nullIsEmpty){
                postEmpty(dataValue)
            }else{
                postError(postNull = postNull) //出错
            }
        }else if(dataValue is Collection<*> && dataValue.isEmpty()){
            postEmpty(dataValue) //数据成功但是空，需要传递空，UI需要刷新
        }else{
            postValueAndSuccess(dataValue)
        }
    }

    /**
     * 强大而实用的封装，启动协程执行逻辑（比如网络请求），并对逻辑结果进行智能post。示例如下：
     * launchAndSmartPost {
     *      "https://iandroid.xyz/api".http().get<T>().await()
     * }
     * @param block 执行块
     * @param nullIsEmpty 是否把null值当做Empty处理，默认false
     */
    fun launchAndSmartPost( postNull: Boolean = false,  nullIsEmpty: Boolean = false,
                            block: suspend CoroutineScope.() -> T?): Job {
        postLoading()
        return GlobalScope.launch { smartPost(block(), nullIsEmpty, postNull) }
    }

}

