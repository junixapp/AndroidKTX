package com.lxj.androidktx.util

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import java.security.MessageDigest

class GlideBlurTransformation(var scale: Float = 0.5f, var blurRadius: Float = 20f,
    var roundRadius: Int = 0): CenterCrop() {

    val ID = "com.lxj.androidktx.util.GlideBlurTransformation"
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bmp = super.transform(pool, toTransform, outWidth, outHeight)
        var blur = ImageUtils.fastBlur(bmp, scale, blurRadius)
        if(roundRadius>0) blur = ImageUtils.toRoundCorner(blur, roundRadius.toFloat())
        return blur
    }

    override fun hashCode(): Int {
        return "${ID}-${scale}-${blurRadius}-${roundRadius}".hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray(CHARSET))
    }
}