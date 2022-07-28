package com.lxj.androidktx.util

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

class SuperGlideTransformation(
    var isCenterCrop: Boolean = false, var scale: Float = 0f, var blurRadius: Float = 20f,
    var roundRadius: Int = 0, var borderColor: Int = 0, var borderSize: Int = 0
) :
    BitmapTransformation() {

    private val ID = "com.lxj.androidktx.util.SuperGlideTransformation"
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        var bmp = toTransform
        if (isCenterCrop) {
            bmp = TransformationUtils.centerCrop(pool, bmp, outWidth, outHeight)
        }
        if (roundRadius > 0) {
            bmp = TransformationUtils.roundedCorners(pool, bmp, roundRadius)
        }
        //blur
        if (scale > 0) {
            bmp = ImageUtils.fastBlur(bmp, scale, blurRadius)
        }
        //round, border
        if (roundRadius > 0 || (borderColor != 0 && borderSize > 0)) {
            bmp = ImageUtils.toRoundCorner(
                bmp,
                roundRadius.toFloat(),
                borderSize.toFloat(),
                borderColor
            )
        }
        return bmp
    }

    override fun hashCode(): Int {
        return "${ID}-${isCenterCrop}-${scale}-${blurRadius}-${roundRadius}-${borderColor}-${borderSize}".hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val fullID =
            "${ID}-${isCenterCrop}-${scale}-${blurRadius}-${roundRadius}-${borderColor}-${borderSize}"
        messageDigest.update(fullID.toByteArray(CHARSET))
    }
}