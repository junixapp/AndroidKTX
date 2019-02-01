package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import kotlinx.android.synthetic.main.fragment_recyclerview_ext.*

class RecyclerViewExtDemo : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_ext
    }

    override fun initView() {
        super.initView()

        val data = arrayListOf<String>().apply {
            for (i in 0..30) {
                add("$i")
            }
        }

        recyclerView.vertical()
                .divider(color = Color.RED)
                .bindData(data, R.layout.adapter_rv) { holder, t, position ->
                    holder.setText(R.id.text, "模拟数据 - $t")
                            .setImageResource(R.id.image, R.mipmap.ic_launcher_round)
                }.itemClick { view, holder, position ->
                    toast("click ${data[position]}")
                }
    }

}
