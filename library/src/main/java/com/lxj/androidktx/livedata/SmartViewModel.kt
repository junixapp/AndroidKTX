package com.lxj.androidktx.livedata

import androidx.lifecycle.ViewModel
import com.lxj.androidktx.okhttp.OkExt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import java.io.IOException
import kotlin.coroutines.CoroutineContext

private const val SmartViewModelKEY = "com.lxj.androidktx.livedata.SmartViewModelKEY"

/**
 * 当ViewModel执行onClear的时候，会自动取消协程和http请求
 */
open class SmartViewModel : ViewModel() {
    var mBagOfTags2 = hashMapOf<String, Any>()

    @Volatile
    private var mCleared2 = false

    fun setTagIfAbsent2(key: String, newValue: Any): Any {
        var previous: Any?
        synchronized(mBagOfTags2) {
            previous = mBagOfTags2[key]
            if (previous == null) {
                mBagOfTags2[key] = newValue
            }
        }
        val result = if (previous == null) newValue else previous!!
        if (mCleared2) {
            closeWithRuntimeException2(result)
        }
        return result
    }

    fun getTag2(key: String?): Any? {
        synchronized(mBagOfTags2) { return mBagOfTags2[key] }
    }

    override fun onCleared() {
        super.onCleared()
        mCleared2 = true
        synchronized(mBagOfTags2) {
            for (value in mBagOfTags2.values) {
                closeWithRuntimeException2(value)
            }
        }
    }

    private fun closeWithRuntimeException2(obj: Any) {
        if (obj is Closeable) {
            try {
                obj.close()
            } catch (e: IOException) {
                throw java.lang.RuntimeException(e)
            }
        }
    }

    /**
     * 当ViewModel执行onClear的时候，会自动取消协程和http请求
     * @param url http的url
     */
    fun httpScope(url: String? = null): CoroutineScope {
        val scope: CoroutineScope? = this.getTag2(SmartViewModelKEY) as CoroutineScope?
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent2(
            SmartViewModelKEY,
            CloseableHttpCoroutineScope(SupervisorJob() + Dispatchers.IO, url = url)
        ) as CoroutineScope
    }
}

class CloseableHttpCoroutineScope(context: CoroutineContext, var url: String? = null) : Closeable,
    CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
        if (!url.isNullOrEmpty()) OkExt.cancel(url!!)
    }
}