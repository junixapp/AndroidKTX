package com.lxj.androidktxdemo.fragment

import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.jaeger.ninegridimageview.NineGridImageView
import com.jaeger.ninegridimageview.NineGridImageViewAdapter
import com.lxj.androidktx.core.*
import com.lxj.androidktxdemo.R
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.fragment_nine_gridview.*

data class Post(
        var urls: List<String>
)

class NineGridViewDemo : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_nine_gridview
    }

    val list = listOf(
            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=2259458612,1883079358&fm=26&gp=0.jpg"
            )),

            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=1621622369,980630947&fm=11&gp=0.jpg",
                    "http://img2.imgtn.bdimg.com/it/u=1805478546,1895925572&fm=26&gp=0.jpg"
            )),
            Post(urls = arrayListOf(
                    "http://img2.imgtn.bdimg.com/it/u=2381039437,3117031781&fm=26&gp=0.jpg",
                    "http://img2.imgtn.bdimg.com/it/u=3313838802,2768404782&fm=26&gp=0.jpg",
                    "http://img0.imgtn.bdimg.com/it/u=1067907827,2400030337&fm=26&gp=0.jpg",
                    "http://img0.imgtn.bdimg.com/it/u=3948481621,2037397389&fm=26&gp=0.jpg"
            )),

            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=1621622369,980630947&fm=11&gp=0.jpg",
                    "http://img1.imgtn.bdimg.com/it/u=3705843178,107182593&fm=26&gp=0.jpg"
            )),

            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=1621622369,980630947&fm=11&gp=0.jpg",
                    "http://img1.imgtn.bdimg.com/it/u=3705843178,107182593&fm=26&gp=0.jpg",
                    "http://img4.imgtn.bdimg.com/it/u=3376244564,1572447450&fm=11&gp=0.jpg"
            )),

            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=1621622369,980630947&fm=11&gp=0.jpg",
                    "http://img1.imgtn.bdimg.com/it/u=3705843178,107182593&fm=26&gp=0.jpg",
                    "http://img4.imgtn.bdimg.com/it/u=3376244564,1572447450&fm=11&gp=0.jpg",
                    "http://img5.imgtn.bdimg.com/it/u=3654454709,510765624&fm=26&gp=0.jpg",
                    "http://img3.imgtn.bdimg.com/it/u=3184788618,602910243&fm=26&gp=0.jpg"
            )),
            Post(urls = arrayListOf(
                    "http://img0.imgtn.bdimg.com/it/u=1067907827,2400030337&fm=26&gp=0.jpg"
            ))
    )

    override fun initView() {
        super.initView()
//        Glide.getPhotoCacheDir(context!!)?.deleteRecursively()
//        recyclerView.vertical()
//                .bindData(list, R.layout.adapter_nine_grid, bindFn = { holder, t, position ->
//                    holder.getView<NineGridImageView<String>>(R.id.nineGrid).apply {
////                        setShowStyle(NineGridImageView.STYLE_GRID)
//                        setSingleImgSize(AdaptScreenUtils.pt2Px(100f))
//                        setup(t.urls, corner = 36, placeholder = R.mipmap.ic_launcher)
//                    }
//
//                })
    }
}
