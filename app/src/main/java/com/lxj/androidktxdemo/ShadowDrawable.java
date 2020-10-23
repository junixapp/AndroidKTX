package com.lxj.androidktxdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ShadowDrawable
 * <p>
 * Created by lijiankun on 2018/9/28
 * Email: lijiankun03@meituan.com
 */
public class ShadowDrawable extends Drawable {

    private Paint mShadowPaint;
    private int mShape;
    private float mShadowRadius;
    private float mOffsetX;
    private float mOffsetY;
    private RectF mRect;

    public ShadowDrawable(int shape, int shadowColor, float shadowRadius, float offsetX, float offsetY) {
        this.mShape = shape;
        this.mShadowRadius = shadowRadius;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.TRANSPARENT);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds left " + left);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds top " + top);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds right " + right);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds bottom " + bottom);
        mRect = new RectF(left + mShadowRadius - mOffsetX, top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
                bottom - mShadowRadius - mOffsetY);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds mRect.left " + mRect.left);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds mRect.top " + mRect.top);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds mRect.right " + mRect.right);
        Log.i("ShadowLayout1", "ShadowDrawable1 setBounds mRect.bottom " + mRect.bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Log.i("ShadowLayout3", "ShadowDrawable1 draw " + canvas);

        if (mShape == ShadowLayout.SHAPE_RECTANGLE) {
            canvas.drawRect(mRect, mShadowPaint);
        } else if (mShape == ShadowLayout.SHAPE_OVAL) {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mShadowPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
