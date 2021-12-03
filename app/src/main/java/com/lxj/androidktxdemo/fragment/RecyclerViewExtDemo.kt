package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import com.lxj.androidktx.widget.SlidingLayout
import com.lxj.easyadapter.ItemDelegate
import com.lxj.easyadapter.ViewHolder
import kotlinx.android.synthetic.main.fragment_recyclerview_ext.*
import java.util.concurrent.CopyOnWriteArrayList


class RecyclerViewExtDemo : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_ext
    }

    lateinit var data: ArrayList<String>
    var list = CopyOnWriteArrayList<SlidingLayout>()
    override fun initView() {
        super.initView()
        data = arrayListOf<String>().apply {
            for (i in 0..30) {
                add("侧滑删除-$i")
            }
        }
        val header = TextView(context).apply {
            text = "我是header"
            setPadding(120, 120, 120, 120)
        }
        val footer = TextView(context).apply {
            text = "我是footer"
            setPadding(120, 120, 120, 120)
        }

        btn.click {
            recyclerView.updateData(arrayListOf("3", "4"))
        }

        //notify
        recyclerView.vertical(spanCount = 2)
                .divider(color = Color.parseColor("#f1f1f1"), size = 1.dp)
                .bindData(data, R.layout.adapter_rv) { holder, t, position ->
                    holder.setText(R.id.text, t)
                            .setImageResource(R.id.image, R.mipmap.ic_launcher_round)
                    holder.getView<View>(R.id.tvDel).click {
                        (holder.itemView as SlidingLayout).close()
                        data.removeAt(position)
                        recyclerView.adapter?.notifyItemRemoved(position)
                        recyclerView.adapter?.notifyItemRangeChanged(position, data.size-position)
                    }
                    (holder.itemView as SlidingLayout).shareCache = list
                }
//                .multiTypes(data, listOf(HeaderDelegate(), ContentDelegate(), FooterDelegate()))
//                .addHeader(header) //必须在bindData之后调用
//                .addFooter(footer) //必须在bindData之后调用
                .itemClick<String> { data, holder, position ->
                }
//                .enableItemDrag(isDisableLast = true)


    }

//
    class ContentDelegate : ItemDelegate<String>{
        override fun bind(holder: ViewHolder, t: String, position: Int) {
            holder.setText(android.R.id.text1, t)
        }
        override fun isThisType(item: String, position: Int): Boolean {
            return item != "header" && item != "footer"
        }
        override fun getLayoutId(): Int {
            return android.R.layout.simple_list_item_1
        }
    }
//
//    class FooterDelegate : ItemViewDelegate<String>{
//        override fun bind(holder: ViewHolder, t: String, position: Int) {
//            holder.setText(android.R.id.text1, t)
//        }
//        override fun isForViewType(item: String, position: Int): Boolean {
//            return item == "footer"
//        }
//
//        override fun getLayoutId(): Int {
//            return android.R.layout.simple_list_item_1
//        }
//    }

}
