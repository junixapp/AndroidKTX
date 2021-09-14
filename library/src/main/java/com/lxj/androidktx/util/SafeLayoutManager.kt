package com.lxj.androidktx.util

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.IndexOutOfBoundsException

class SafeLinearLayoutManager: LinearLayoutManager {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL, reverseLayout: Boolean = false)
            : super(context,orientation, reverseLayout) {
    }
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }
}

class SafeGridLayoutManager: GridLayoutManager {
    constructor(context: Context, spanCount: Int) : super(context, spanCount) {}
    constructor(context: Context, spanCount: Int, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL, reverseLayout: Boolean = false)
            : super(context,spanCount,orientation, reverseLayout) {
    }
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }
}

class SafeStaggeredGridLayoutManager: StaggeredGridLayoutManager {
    constructor(spanCount: Int, orientation: Int ) : super(spanCount, orientation) {}
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
                defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }
    }
}