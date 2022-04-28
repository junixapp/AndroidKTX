package com.zhihu.matisse.imagezoom.graphics;

import android.graphics.Bitmap;

import com.zhihu.matisse.imagezoom.ImageViewTouchBase;

/**
 * Base interface used in the {@link ImageViewTouchBase} view
 *
 * @author alessandro
 */
public interface IBitmapDrawable {

    Bitmap getBitmap();
}
