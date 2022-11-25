package com.lxj.androidktxdemo.blibli

import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.lxj.androidktx.core.bindData
import com.lxj.androidktx.core.vertical
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_blibli.*

class BlibliFragment: BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_blibli

    override fun initView() {
        super.initView()
        rvBliBli.vertical()
            .bindData(listOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1), R.layout.adapter_blibli, bindFn = {holder, t, position ->
                (holder.itemView as TextView).setText("评论内容-${position}")
                (holder.itemView as TextView).setBackgroundColor(com.blankj.utilcode.util.ColorUtils.getRandomColor())
            })
    }
}