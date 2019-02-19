package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import android.widget.TextView
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import com.lxj.easyadapter.ItemViewDelegate
import com.lxj.easyadapter.ViewHolder
import kotlinx.android.synthetic.main.fragment_recyclerview_ext.*
import java.util.*

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
                .divider(color = Color.parseColor("#eeeeee"), size = 20)
                .bindData(data, R.layout.adapter_rv) { holder, t, position ->
                    holder.setText(R.id.text, "模拟数据 - $t")
                            .setImageResource(R.id.image, R.mipmap.ic_launcher_round)
                }
//                .multiTypes(data, listOf(HeaderDelegate(), ContentDelegate(), FooterDelegate()))
                .itemClick<String> { data, holder, position ->
                    toast("click ${data[position]}")
                }

        //notify
//        recyclerView.adapter.notifyItemChanged()
    }

    class HeaderDelegate : ItemViewDelegate<String>{
        override fun convert(holder: ViewHolder, t: String, position: Int) {
            holder.setText(android.R.id.text1, t)
        }
        override fun isForViewType(item: String, position: Int): Boolean {
            return item == "header"
        }

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }
    }

    class ContentDelegate : ItemViewDelegate<String>{
        override fun convert(holder: ViewHolder, t: String, position: Int) {
            holder.setText(android.R.id.text1, t)
        }
        override fun isForViewType(item: String, position: Int): Boolean {
            return item != "header" && item != "footer"
        }

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }
    }

    class FooterDelegate : ItemViewDelegate<String>{
        override fun convert(holder: ViewHolder, t: String, position: Int) {
            holder.setText(android.R.id.text1, t)
        }
        override fun isForViewType(item: String, position: Int): Boolean {
            return item == "footer"
        }

        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }
    }
}
