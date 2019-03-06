package com.lxj.androidktx.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Description: 只执行一次的LiveData
 * Create by lxj, at 2019/3/6
 */
class OnceLiveData<T> : MutableLiveData<T>() {

    private var isRead: AtomicBoolean = AtomicBoolean(false)

    /**
     * ensure the event is non-null and can only been seen once
     */
    fun observeEvent(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, Observer {
            if (it != null && isRead.compareAndSet(false, true)) {
                observer.onChanged(it)
            }
        })
    }

    override fun postValue(value: T) {
        isRead.set(false)
        super.postValue(value)
    }

    override fun setValue(value: T) {
        isRead.set(false)
        super.setValue(value)
    }
}