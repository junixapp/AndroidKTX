package com.lxj.androidktx.core

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lxj.androidktx.livedata.StateLiveData
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupStatus
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File

/**
 * 绑定LiveData的状态，警惕LoadingView过早关闭的时候，state的状态还未执行
 */
fun BasePopupView.bindState(liveData: StateLiveData<*>,
                               onLoading: (()->Unit)? = null,
                               onSuccess: (()->Unit)? = null,
                               onError: (()->Unit)? = null,
                               onEmpty: (()->Unit)? = null,
                               autoShowError: Boolean = false){
    val delay = XPopup.getAnimationDuration().toLong()+10
    when(liveData.state.value){
        StateLiveData.State.Loading->{
            show()
            onLoading?.invoke()
        }
        StateLiveData.State.Success->{
            //如果状态异常，已经关闭或正在关闭
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                onSuccess?.invoke()
            }else{
                delayDismissWith(delay){
                    onSuccess?.invoke()
                }
            }
        }
        StateLiveData.State.Empty->{
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                //如果已经关闭或正在关闭
                onEmpty?.invoke()
            }else{
                delayDismissWith(delay){
                    onEmpty?.invoke()
                }
            }
        }
        StateLiveData.State.Error->{
            if(popupStatus==PopupStatus.Dismissing || popupStatus==PopupStatus.Dismiss){
                //如果已经关闭
                if(autoShowError && liveData.errMsg?.isNotEmpty()==true){
                    ToastUtils.showShort(liveData.errMsg)
                    liveData.errMsg = ""
                }
                onError?.invoke()
            }else{
                delayDismissWith(delay){
                    if(autoShowError && liveData.errMsg?.isNotEmpty()==true){
                        ToastUtils.showShort(liveData.errMsg)
                        liveData.errMsg = ""
                    }
                    onError?.invoke()
                }
            }
        }
    }
}

/**
 * 直接监听state状态
 */
fun BasePopupView.observeState(owner: LifecycleOwner,
                                  liveData: StateLiveData<*>,
                                  onLoading: (()->Unit)? = null,
                                  onSuccess: (()->Unit)? = null,
                                  onError: (()->Unit)? = null,
                                  onEmpty: (()->Unit)? = null,
                                  autoShowError: Boolean = false){
    liveData.state.observe(owner, Observer<StateLiveData.State> {
        bindState(liveData, onLoading = onLoading, onSuccess = onSuccess,
                onError = onError, onEmpty = onEmpty, autoShowError = autoShowError)
    })
}

class GlideImageLoader(var placeholder: Int = 0, var hasSuperImage: Boolean = false,
    var superImageUnit : Int = 10 * 1024 * 1024) : XPopupImageLoader {
    override fun loadImage(
            position: Int,
            url: Any,
            imageView: ImageView
    ) {
        if(!hasSuperImage){
            Glide.with(imageView).load(url)
                .apply(RequestOptions().placeholder(placeholder)
                    .override(Target.SIZE_ORIGINAL))
                .into(imageView)
        }else{
            //下面的写法，可以加载超级大图
            Glide.with(imageView).load(url).apply(buildOptions())
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        if (resource != null && resource is BitmapDrawable) {
                            val r = resource.bitmap.byteCount / superImageUnit
                            if (r >= 1) {
                                val w = resource.getIntrinsicWidth() / r
                                val h = resource.getIntrinsicHeight() / r
                                Glide.with(imageView).load(url).apply(buildOptions().override(w, h))
                                    .into(imageView)
                            } else {
                                imageView.setImageDrawable(resource)
                            }
                        } else {
                            imageView.setImageDrawable(resource)
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }
    private fun buildOptions(): RequestOptions {
        return RequestOptions()
            .dontAnimate()
            .dontTransform()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    }
    override fun getImageFile(
            context: Context,
            uri: Any
    ): File? {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}