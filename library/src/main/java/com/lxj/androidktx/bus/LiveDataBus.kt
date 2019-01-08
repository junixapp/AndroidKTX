package com.lxj.androidktx.bus

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.os.Looper
import java.util.HashMap
import java.util.Map
import java.util.concurrent.TimeUnit

/**
 * Description: Kotlin version of https://github.com/JeremyLiao/LiveEventBus.
 * Create by dance, at 2019/1/8
 */

object LiveDataBus {
    private val bus: MutableMap<String, BusMutableLiveData<Any>> = hashMapOf()

     fun < T> with(key: String): Observable<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData(key)
        }
        return bus[key] as Observable<T>
    }

    class BusMutableLiveData<T>(private val key: String) : MutableLiveData<T>(), Observable<T> {
        private val observerMap = HashMap<Observer<T>, Observer<T>>()
        private val mainHandler = Handler(Looper.getMainLooper())

        private inner class PostValueTask(private val newValue: T) : Runnable {
            override fun run() {
                value = newValue
            }
        }
        override fun postValue(value: T) {
            mainHandler.post(PostValueTask(value))
        }

        override fun postValueDelay(value: T, delay: Long, unit: TimeUnit) {
            mainHandler.postDelayed(PostValueTask(value), TimeUnit.MILLISECONDS.convert(delay, unit))
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            //保存LifecycleOwner的当前状态
            val lifecycle = owner.lifecycle
            val currentState = lifecycle.currentState
            val observerSize = getLifecycleObserverMapSize(lifecycle)
            val needChangeState = currentState.isAtLeast(Lifecycle.State.STARTED)
            if (needChangeState) {
                //把LifecycleOwner的状态改为INITIALIZED
                setLifecycleState(lifecycle, Lifecycle.State.INITIALIZED)
                //set observerSize to -1，否则super.observe(owner, observer)的时候会无限循环
                setLifecycleObserverMapSize(lifecycle, -1)
            }
            super.observe(owner, observer)
            if (needChangeState) {
                //重置LifecycleOwner的状态
                setLifecycleState(lifecycle, currentState)
                //重置observer size，因为又添加了一个observer，所以数量+1
                setLifecycleObserverMapSize(lifecycle, observerSize + 1)
                //把Observer置为active
                hookObserverActive(observer, true)
            }
            //更改Observer的version
            hookObserverVersion(observer)
        }

        override fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
            super.observe(owner, observer)
        }

        override fun observeForever(observer: Observer<T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer]!!)
        }

        override fun observeStickyForever(observer: Observer<T>) {
            super.observeForever(observer)
        }

        override fun removeObserver(observer: Observer<T>) {
            val realObserver: Observer<T>? = if (observerMap.containsKey(observer)) {
                observerMap.remove(observer)
            } else {
                observer
            }
            super.removeObserver(realObserver!!)
            if (!hasObservers()) {
                bus.remove(key)
            }
        }

        private fun setLifecycleObserverMapSize(lifecycle: Lifecycle?, size: Int) {
            if (lifecycle == null || lifecycle !is LifecycleRegistry) {
                return
            }
            try {
                val observerMapField = LifecycleRegistry::class.java.getDeclaredField("mObserverMap")
                observerMapField.isAccessible = true
                val mObserverMap = observerMapField.get(lifecycle)
                val superclass = mObserverMap.javaClass.superclass
                val mSizeField = superclass.getDeclaredField("mSize")
                mSizeField.isAccessible = true
                mSizeField.set(mObserverMap, size)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun getLifecycleObserverMapSize(lifecycle: Lifecycle?): Int {
            if (lifecycle == null || lifecycle !is LifecycleRegistry) {
                return 0
            }
            return try {
                val observerMapField = LifecycleRegistry::class.java.getDeclaredField("mObserverMap")
                observerMapField.isAccessible = true
                val mObserverMap = observerMapField.get(lifecycle)
                val superclass = mObserverMap.javaClass.superclass
                val mSizeField = superclass.getDeclaredField("mSize")
                mSizeField.isAccessible = true
                mSizeField.get(mObserverMap) as Int
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }

        private fun setLifecycleState(lifecycle: Lifecycle?, state: Lifecycle.State) {
            if (lifecycle == null || lifecycle !is LifecycleRegistry) {
                return
            }
            try {
                val mState = LifecycleRegistry::class.java.getDeclaredField("mState")
                mState.isAccessible = true
                mState.set(lifecycle, state)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @Throws(Exception::class)
        private fun getObserverWrapper(observer: Observer<T>): Any? {
            val fieldObservers = LiveData::class.java.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers.get(this)
            val classObservers = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            return objectWrapper
        }

        private fun hookObserverVersion(observer: Observer<T>) {
            try {
                //get wrapper's version
                val objectWrapper = getObserverWrapper(observer) ?: return
                val classObserverWrapper = objectWrapper.javaClass.superclass
                val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
                fieldLastVersion.isAccessible = true
                //get livedata's version
                val fieldVersion = LiveData::class.java.getDeclaredField("mVersion")
                fieldVersion.isAccessible = true
                val objectVersion = fieldVersion.get(this)
                //set wrapper's version
                fieldLastVersion.set(objectWrapper, objectVersion)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun hookObserverActive(observer: Observer<T>, active: Boolean) {
            try {
                //get wrapper's version
                val objectWrapper = getObserverWrapper(observer) ?: return
                val classObserverWrapper = objectWrapper.javaClass.superclass
                val mActive = classObserverWrapper.getDeclaredField("mActive")
                mActive.isAccessible = true
                mActive.set(objectWrapper, active)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>?) : Observer<T> {
        private val isCallOnObserve: Boolean
            get() {
                val stackTrace = Thread.currentThread().stackTrace
                if (stackTrace != null && stackTrace.isNotEmpty()) {
                    for (element in stackTrace) {
                        if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                            return true
                        }
                    }
                }
                return false
            }

        override fun onChanged(t: T?) {
            if (observer != null) {
                if (isCallOnObserve) {
                    return
                }
                observer.onChanged(t)
            }
        }
    }
    interface Observable<T> {

        fun setValue(value: T)

        fun postValue(value: T)

        fun postValueDelay(value: T, delay: Long, unit: TimeUnit)

        fun observe(owner: LifecycleOwner, observer: Observer<T>)

        fun observeSticky(owner: LifecycleOwner, observer: Observer<T>)

        fun observeForever(observer: Observer<T>)

        fun observeStickyForever(observer: Observer<T>)

        fun removeObserver(observer: Observer<T>)
    }
}
