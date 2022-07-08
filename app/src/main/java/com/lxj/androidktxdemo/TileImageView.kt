package com.lxj.androidktxdemo

import android.content.Context
import android.graphics.*
import com.lxj.androidktx.widget.RatioImageView
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.blankj.utilcode.util.LogUtils
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.bitmap.BitmapResource


class TileImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageView(context, attributeSet, defStyleAttr){

    val tileBmp : Drawable by lazy { (resources.getDrawable(R.drawable.bg_tile) as BitmapDrawable) }
    init {
    }

    var clothesBmp: Bitmap? = null
    var clothesMatrix = Matrix()
    fun addClothesBmp(bmp: Bitmap){
        clothesBmp = bmp
        invalidate()
    }

    fun scale(scale: Float){
        clothesMatrix.postScale(scale, scale)
        invalidate()
    }

    fun move(x: Float, y: Float){
        clothesMatrix.postTranslate(x, y)
        invalidate()
    }

    fun rotate(d: Float){
        if(clothesBmp==null) return
        clothesMatrix.postRotate(d, clothesBmp!!.width/2f, clothesBmp!!.height/2f)
        invalidate()
    }


    val boundsF = RectF()
    val boundsI = Rect()
    override fun onDraw(canvas: Canvas?) {

        val bounds = drawable.bounds
        boundsF.set(bounds)
        getImageMatrix().mapRect(boundsF)
        boundsF.round(boundsI)
//        canvas!!.drawRect(boundsI, paint)
        tileBmp.setBounds(boundsI)
        tileBmp.draw(canvas!!)
//        canvas!!.drawBitmap(tileBmp, tileRect, boundsI, null)
        super.onDraw(canvas)
        LogUtils.e("boundsF: ${boundsF.toShortString()}   W:${measuredWidth}x${measuredHeight}")

        if(clothesBmp!=null){

        }
    }

}