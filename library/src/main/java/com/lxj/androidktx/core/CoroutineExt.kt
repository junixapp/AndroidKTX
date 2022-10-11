package com.lxj.androidktx.core

import kotlinx.coroutines.CompletableDeferred

/**
 * 将任意代码块转为CompletableDeferred对象，实现任意代码可以在coroutine中进行await()等待
 * 注意：你需要自己调用deffered.complete(T)
 */
fun <T> convertToDeferred(action: (deferred: CompletableDeferred<T>)->Unit): CompletableDeferred<T> {
    val deferred = CompletableDeferred<T>()
    deferred.invokeOnCompletion {}
    action(deferred)
    return deferred
}