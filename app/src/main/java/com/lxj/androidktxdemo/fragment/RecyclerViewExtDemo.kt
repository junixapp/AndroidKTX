package com.lxj.androidktxdemo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import com.lxj.androidktx.widget.SlidingLayout
import com.lxj.androidktxdemo.entity.User
import com.lxj.androidktxdemo.vm.UserVM
import com.lxj.easyadapter.ItemDelegate
import com.lxj.easyadapter.ViewHolder
import kotlinx.android.synthetic.main.fragment_recyclerview_ext.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList
import kotlin.random.Random


class RecyclerViewExtDemo : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_recyclerview_ext
    }

    val url =
        "https://img1.baidu.com/it/u=1925715390,133119052&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400"

    var layoutList = CopyOnWriteArrayList<SlidingLayout>()
    val userVM: UserVM by lazy { getVM(UserVM::class.java) }
    override fun initView() {
        super.initView()
        addBtn.click(50) {
//            val old = data.toJson().toBean<ArrayList<User>>()
//            val range = (0 until data.size)
//            data.add(
//                if (range.isEmpty()) 0 else range.random(),
//                User(id = UUID.randomUUID().toString(), name = "随机添加 - ${(0..1000).random()}")
//            )
//            recyclerView.diffUpdate(UserDiffCallback(old, data))

            userVM.insert(User(name = "随机添加-${Random.nextInt(1000)}"))
        }
        delBtn.click(50) {
//            if (data.isEmpty()) return@click
//            val old = data.toJson().toBean<ArrayList<User>>()
//            data.removeAt((0 until data.size).random())
//            recyclerView.diffUpdate(UserDiffCallback(old, data))
            if(userVM.listData.value.isNullOrEmpty()) return@click
            val randomPosition = Random.nextInt(userVM.listData.value!!.size)
            userVM.remove(randomPosition)
        }

        updateBtn.click(50) {
//            val old = userVM.listData.value!!.deepCopy<ArrayList<User>>()
//            val index = (0 until userVM.listData.value!!.size).random()
//            userVM.listData.value!![index].name = userVM.listData.value!![index].name + "- 局部字段替换"
//            recyclerView.diffUpdate(UserDiffCallback(old, userVM.listData.value!!))

            if(userVM.listData.value.isNullOrEmpty()) return@click
            val randomPosition = Random.nextInt(userVM.listData.value!!.size)
            val t = userVM.listData.value!![randomPosition].deepCopy<User>()
            t.name = "局部字段替换-${randomPosition}"
            userVM.update(randomPosition, t)
        }

        replaceBtn.click(50) {
//            if (data.isEmpty()) return@click
//            val old = data.deepCopy<ArrayList<User>>()
//            val index = (0 until data.size).random()
//            data[index] = User(name = "我是随机替换")
//            recyclerView.diffUpdate(UserDiffCallback(old, data))

            if(userVM.listData.value.isNullOrEmpty()) return@click
            val randomPosition = Random.nextInt(userVM.listData.value!!.size)
            val t = User(name = "随机替换-${randomPosition}")
            userVM.update(randomPosition, t)
        }

        moveBtn.click(50) {
//            if (data.isEmpty()) return@click
            val old = userVM.listData.value!!.deepCopy<ArrayList<User>>()
            old.shuffle()
            recyclerView.diffUpdate(UserDiffCallback(old, userVM.listData.value))
        }
        val header = TextView(context).apply {
            text = "我是header"
            setPadding(120, 120, 120, 120)
        }
        val footer = TextView(context).apply {
            text = "我是footer"
            setPadding(120, 120, 120, 120)
        }
        //notify
        recyclerView.vertical(spanCount = 2)
            .divider(color = Color.parseColor("#f1f1f1"), size = 1.dp)
            .bindData(userVM.listData.value!!, R.layout.adapter_rv,
                onPayloadsChange = { holder, t, position, payloads ->
                    val bundle = payloads[0] as Bundle
                    val name = bundle.getString("name") ?: ""
                    if (!name.isNullOrEmpty()) holder.setText(R.id.text, name)
                }) { holder, t, position ->
                holder.setText(R.id.text, t.name + " - $position")
                holder.getView<View>(R.id.text).click {
                    ToastUtils.showShort(t.name + " - $position")
                }
                Glide.with(requireContext())
                    .load(url).transition(DrawableTransitionOptions.withCrossFade(1000))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.getView<ImageView>(R.id.image))
                holder.getView<View>(R.id.tvDel).click {
                    (holder.itemView as SlidingLayout).close()
                    LogUtils.e("delete item : ${holder.adapterPosition}")
                    userVM.remove(holder.adapterPosition)
//                    val old = data.deepCopy<ArrayList<User>>()
//                    data.removeAt(holder.adapterPosition)
//                    recyclerView.diffUpdate(UserDiffCallback(old, data))
//                        recyclerView.adapter?.notifyItemRemoved(position)
//                        recyclerView.adapter?.notifyItemRangeChanged(position, data.size-position)
                }

//                (holder.itemView as SlidingLayout).shareCache = layoutList

            }
//                .multiTypes(data, listOf(HeaderDelegate(), ContentDelegate(), FooterDelegate()))
//                .addHeader(header) //必须在bindData之后调用
//                .addFooter(footer) //必须在bindData之后调用
            .itemClick<String> { data, holder, position ->

            }
//                .enableItemDrag(isDisableLast = true)


//        userVM.bindRecyclerView(this, rv = recyclerView)
    }

    var data: CopyOnWriteArrayList<User> = CopyOnWriteArrayList()
    fun bind1() {
        data = CopyOnWriteArrayList<User>().apply {
            add(User(id = UUID.randomUUID().toString(), name = "浏览"))
            add(User(id = UUID.randomUUID().toString(), name = "哇的"))
            add(User(id = UUID.randomUUID().toString(), name = "打撒所"))
            add(User(id = UUID.randomUUID().toString(), name = "电动车"))
            add(User(id = UUID.randomUUID().toString(), name = "CCTV"))
            add(User(id = UUID.randomUUID().toString(), name = "企鹅岛"))
            add(User(id = UUID.randomUUID().toString(), name = "连框架"))
            add(User(id = UUID.randomUUID().toString(), name = "坡度大"))
            add(User(id = UUID.randomUUID().toString(), name = "pop撒大声地"))
            add(User(id = UUID.randomUUID().toString(), name = "残破的"))
            add(User(id = UUID.randomUUID().toString(), name = "玫琳凯"))
        }



    }

    override fun initData() {
        super.initData()
        userVM.bindRecyclerView(this, rv = recyclerView, stateLayout = stateRv)
    }

    //
    class ContentDelegate : ItemDelegate<String> {
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
