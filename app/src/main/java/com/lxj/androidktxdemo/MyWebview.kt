package com.lxj.androidktxdemo

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.core.view.forEach
import androidx.core.view.iterator
import com.blankj.utilcode.util.LogUtils
import com.lxj.androidktx.core.toast

class MyWebview : WebView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing) {
    }
//
//    private var mActionMode: ActionMode? = null
//    private val mActionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
//        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
//            val inflater = mode.menuInflater
//            inflater.inflate(R.menu.tt, menu)
//            mActionMode = mode
////            (0..menu.size()).forEach {
////                menu.forEach {  }
////            }
////            val iterator = menu.iterator()
////            iterator.forEach {
////                LogUtils.dTag("xx", "iterator : ${it.title} - ${it.title}")
////            }
//            LogUtils.dTag("xx", "onCreateActionMode: ")
//            return true
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
//            LogUtils.dTag("xx", "onPrepareActionMode: ")
//            return false
//        }
//
//        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
//            when (item.itemId) {
//                R.id.add ->                     // do some stuf
//                    getSelectedData(item.title as String)
//                R.id.del -> {}
//                else ->                     // This essentially acts as a catch statement
//                    // If none of the other cases are true, return false
//                    // because the action was not handled
//                    return false
//            }
//            mode.finish()
//            return true
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode) {
//            LogUtils.dTag("xx", "onDestroyActionMode: ")
//            clearFocus() //
//            mActionMode = null
//        }
//    }
//
//    private fun getSelectedData(title: String) {
//        val js = """(function () {
//                    var txt;
//                    if (window.getSelection) {
//                        txt = window.getSelection().toString();
//                    } else if (window.document.getSelection) {
//                        txt = window.document.getSelection().toString();
//                    } else if (window.document.selection)
//                        txt = window.document.selection.createRange().text;
//                    }
//                    alert(txt);
//                    return txt;
//                })()"""
////        evaluateJavascript("javascript:$js") { value -> LogUtils.dTag("xx", "copy: $value") }
//        evaluateJavascript(js) { value -> LogUtils.dTag("xx", "copy: $value") }
//
//        evaluateJavascript("(function(){return window.getSelection().toString()})()",
//            object : ValueCallback<String?> {
//                override fun onReceiveValue(value: String?) {
//                    LogUtils.dTag("xx", "copy2: $value")
//                }
//            })
//    }

    override fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode {
        return doActionMode(super.startActionMode(callback, type))
    }

    private fun doActionMode(actionMode: ActionMode): ActionMode {
        var copyItem : MenuItem? = null
        actionMode.menu.forEach {
            LogUtils.dTag("xx", "groudId: ${it.groupId} itemId: ${it.itemId} order:${it.order}" +
                    "  titile: ${it.title}  androidId: ${android.R.id.copy}"  )
            if(it.title=="复制" || it.title=="COPY") {
                copyItem = it
            }
        }
        actionMode.menu.clear()
        if(copyItem!=null) actionMode.menu.add(copyItem!!.groupId, copyItem!!.itemId, copyItem!!.order, copyItem!!.title)
        return actionMode
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }
}