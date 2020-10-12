package com.lxj.androidktx.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.google.android.flexbox.FlexboxLayoutManager
import com.lxj.androidktx.R
import com.lxj.androidktx.core.*
import com.lxj.xpopup.XPopup

/**
 * 图片上传UI组件
 */
class ImageUploader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {

    private var addImage: Drawable?
    private var delImage : Drawable?
    var delImageMargin = 0
    var imageRadius = 0
    var spanCount = 3
    var hSpace = 10 //横向间距
    var vSpace = 10
    var maxImages = 9
    private var add_button = "_add_button_"
    var paths = arrayListOf<String>(add_button)

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.ImageUploader)
        imageRadius = ta.getDimensionPixelSize(R.styleable.ImageUploader_imageRadius, dp2px(imageRadius.toFloat()))
        delImageMargin = ta.getDimensionPixelSize(R.styleable.ImageUploader_delImageMargin, dp2px(delImageMargin.toFloat()))
        addImage = ta.getDrawable(R.styleable.ImageUploader_addImageRes)
        delImage = ta.getDrawable(R.styleable.ImageUploader_delImageRes)
        spanCount = ta.getInt(R.styleable.ImageUploader_spanCount, spanCount)
        maxImages = ta.getInt(R.styleable.ImageUploader_maxImages, maxImages)
        hSpace = ta.getDimensionPixelSize(R.styleable.ImageUploader_hSpace, dp2px(hSpace.toFloat()))
        vSpace = ta.getDimensionPixelSize(R.styleable.ImageUploader_vSpace, dp2px(vSpace.toFloat()))

        ta.recycle()

        if(addImage==null)addImage = drawable(R.mipmap._ktx_ic_add_image)
        if(delImage==null)delImage = drawable(R.mipmap._ktx_ic_image_del)

        layoutManager = FlexboxLayoutManager(context)
        setupData()
    }

    fun setupData(urls: ArrayList<String>? = null){
        if(!urls.isNullOrEmpty()){
            paths.clear()
            paths.addAll(urls)
            paths.add(add_button)
        }
        bindData(paths, R.layout._ktx_adapter_image_uploader, bindFn = { holder, t, position ->
            val itemWidth = (measuredWidth - (spanCount - 1) * hSpace) / spanCount
            //计算每个条目的宽度
            holder.itemView.widthAndHeight(itemWidth, itemWidth)
            val isRowStart = (position) % spanCount == 0
            holder.itemView.margin(
                    leftMargin = if (isRowStart) 0 else hSpace,
                    bottomMargin = vSpace
            )
            holder.getView<ImageView>(R.id.close).setImageDrawable(delImage)
            if(delImageMargin>0){
                holder.getView<ImageView>(R.id.close).margin(rightMargin = delImageMargin, topMargin = delImageMargin)
            }
            if (t == add_button) {
                holder.getView<ImageView>(R.id.close).gone()
                holder.getView<RoundImageView>(R.id.image).apply {
                    setImageDrawable(addImage)
                    setCornerRadius(0)
                    click { addButtonClickAction?.invoke() }
                }
            } else {
                holder.getView<ImageView>(R.id.close).visible()
                holder.getView<RoundImageView>(R.id.image).apply {
                    load(t, isForceOriginalSize = true)
                    setCornerRadius(imageRadius)
                    click { XPopup.Builder(context).asImageViewer(it as ImageView, t,GlideImageLoader()).show() }
                }
            }
            holder.getView<ImageView>(R.id.close).click {
                paths.remove(t)
                adapter?.notifyDataSetChanged()
            }
        })
    }

    var addButtonClickAction: (() -> Unit)? = null

    fun addImage(path: String) {
        if (paths.size >= (maxImages + 1)) {
            //达到最大数量
            ToastUtils.showShort("最多添加${maxImages}张图片")
            return
        }
        paths.remove(add_button)
        paths.add(path)
        paths.add(add_button)
        adapter?.notifyDataSetChanged()
    }

    fun addImages(images: List<String>) {
        if (paths.size >= (maxImages + 1) ||
            (paths.size + images.size) > (maxImages + 1)
        ) {
            //达到最大数量
            ToastUtils.showShort("最多添加${maxImages}张图片")
            return
        }
        paths.remove(add_button)
        paths.addAll(images)
        paths.add(add_button)
        adapter?.notifyDataSetChanged()
    }

    fun getImagePaths() = paths.subList(0, paths.size - 1)
}