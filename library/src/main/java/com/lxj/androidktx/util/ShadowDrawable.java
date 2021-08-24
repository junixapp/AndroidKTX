package com.lxj.androidktx.util;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

public class ShadowDrawable extends Drawable implements Drawable.Callback  {
    // used to calculate content padding
    private static final double COS_45 = Math.cos(Math.toRadians(45));

    private static final float SHADOW_MULTIPLIER = 1.5f;
    private final int mInsetShadow; // extra shadow to avoid gaps between card and shadow
    private Paint mPaint;

    private Paint mCornerShadowPaint;

    private Paint mEdgeShadowPaint;

    private final RectF mCardBounds;

    private float mCornerRadius = 1f;

    private Path mCornerShadowPath;

    // actual value set by developer
    private float mRawMaxShadowSize;

    // multiplied value to account for shadow offset
    private float mShadowSize;

    // actual value set by developer
    private float mRawShadowSize;

    private ColorStateList mBackground;

    private boolean mDirty = true;

    private final int mShadowStartColor;

    private final int mShadowEndColor;

    private boolean mAddPaddingForCorners = true;

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private boolean mPrintedShadowClipWarning = false;
    private Drawable mDrawable;
    public ShadowDrawable(Drawable innerDrawable, float radius,
                          float shadowSize) {
        this(innerDrawable, 0, radius, shadowSize, shadowSize);
    }

    public ShadowDrawable(Drawable innerDrawable, int shadowColor, float radius,
                          float shadowSize, float maxShadowSize) {
        mShadowStartColor = shadowColor == 0 ? Color.parseColor("#37000000") : shadowColor;
        mShadowEndColor = Color.parseColor("#01000000");
        mInsetShadow = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.TRANSPARENT);
        mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(Paint.Style.FILL);
        mCornerRadius = Math.max(1, (int) (radius + .5f));
        mCardBounds = new RectF();
        mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);

        this.setWrappedDrawable(innerDrawable);
    }

    public void setWrappedDrawable(Drawable drawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback((Callback) null);
        }

        this.mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

//    private void setBackground(ColorStateList color) {
//        mBackground = (color == null) ?  ColorStateList.valueOf(Color.TRANSPARENT) : color;
//        mPaint.setColor(mBackground.getColorForState(getState(), mBackground.getDefaultColor()));
//    }

    /**
     * Casts the value to an even integer.
     */
    private int toEven(float value) {
        int i = (int) (value + .5f);
        if (i % 2 == 1) {
            return i - 1;
        }
        return i;
    }

    void setAddPaddingForCorners(boolean addPaddingForCorners) {
        mAddPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        this.mDrawable.setAlpha(alpha);
        mPaint.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
//        this.mDrawable.setBounds(bounds);
        mDirty = true;
    }
    public void setChangingConfigurations(int configs) {
        this.mDrawable.setChangingConfigurations(configs);
    }

    public int getChangingConfigurations() {
        return this.mDrawable.getChangingConfigurations();
    }

    private void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0f) {
            throw new IllegalArgumentException("Invalid shadow size " + shadowSize
                    + ". Must be >= 0");
        }
        if (maxShadowSize < 0f) {
            throw new IllegalArgumentException("Invalid max shadow size " + maxShadowSize
                    + ". Must be >= 0");
        }
        shadowSize = toEven(shadowSize);
        maxShadowSize = toEven(maxShadowSize);
        if (shadowSize > maxShadowSize) {
            shadowSize = maxShadowSize;
            if (!mPrintedShadowClipWarning) {
                mPrintedShadowClipWarning = true;
            }
        }
        if (mRawShadowSize == shadowSize && mRawMaxShadowSize == maxShadowSize) {
            return;
        }
        mRawShadowSize = shadowSize;
        mRawMaxShadowSize = maxShadowSize;
        mShadowSize = (int) (shadowSize * SHADOW_MULTIPLIER + mInsetShadow + .5f);
        mDirty = true;
        invalidateSelf();
    }

    @Override
    public boolean getPadding(Rect padding) {
        int vOffset = (int) Math.ceil(calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius,
                mAddPaddingForCorners));
        int hOffset = (int) Math.ceil(calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius,
                mAddPaddingForCorners));
        padding.set(hOffset, vOffset, hOffset, vOffset);
        return true;
    }

    static float calculateVerticalPadding(float maxShadowSize, float cornerRadius,
                                          boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize * SHADOW_MULTIPLIER;
        }
    }

    static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius,
                                            boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize;
        }
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final int newColor = mBackground.getColorForState(stateSet, mBackground.getDefaultColor());
        if (mPaint.getColor() == newColor) {
            return false;
        }
        mPaint.setColor(newColor);
        mDirty = true;
        invalidateSelf();
        return true;
    }

    @Override
    public boolean isStateful() {
        return (mBackground != null && mBackground.isStateful()) || mDrawable.isStateful();
    }

    public boolean setState(int[] stateSet) {
        return this.mDrawable.setState(stateSet);
    }

    public int[] getState() {
        return this.mDrawable.getState();
    }
    @Override
    public void setColorFilter(ColorFilter cf) {
        mDrawable.setColorFilter(cf);
        mPaint.setColorFilter(cf);
    }
    public void jumpToCurrentState() {
        DrawableCompat.jumpToCurrentState(this.mDrawable);
    }

    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || this.mDrawable.setVisible(visible, restart);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
    public int getIntrinsicWidth() {
        Rect rect = new Rect();
        getPadding(rect);
        return this.mDrawable.getIntrinsicWidth() - rect.width();
    }

    public int getIntrinsicHeight() {
        Rect rect = new Rect();
        getPadding(rect);
        return this.mDrawable.getIntrinsicHeight() - rect.height();
    }

    public int getMinimumWidth() {
        Rect rect = new Rect();
        getPadding(rect);
        return this.mDrawable.getMinimumWidth() - rect.width();
    }

    public int getMinimumHeight() {
        Rect rect = new Rect();
        getPadding(rect);
        return this.mDrawable.getMinimumHeight() - rect.height();
    }
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }
    void setCornerRadius(float radius) {
        if (radius < 0f) {
            throw new IllegalArgumentException("Invalid radius " + radius + ". Must be >= 0");
        }
        radius = (int) (radius + .5f);
        if (mCornerRadius == radius) {
            return;
        }
        mCornerRadius = radius;
        mDirty = true;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDirty) {
            buildComponents(getBounds());
            mDirty = false;
        }
//        canvas.translate(0, mRawShadowSize / 2);
        drawShadow(canvas);
//        canvas.translate(0, -mRawShadowSize / 2);
        drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);

        this.mDrawable.draw(canvas);
    }

    private void drawShadow(Canvas canvas) {
        final float edgeShadowTop = -mCornerRadius - mShadowSize;
        final float inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2;
        final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
        final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
        // LT
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.width() - 2 * inset, -mCornerRadius,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RB
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
        canvas.rotate(180f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.width() - 2 * inset, -mCornerRadius + mShadowSize,
                    mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // LB
        saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
        canvas.rotate(270f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
        // RT
        saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
        canvas.rotate(90f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(0, edgeShadowTop,
                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
        }
        canvas.restoreToCount(saved);
    }

    private void buildShadowCorners() {
        RectF innerBounds = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        RectF outerBounds = new RectF(innerBounds);
        outerBounds.inset(-mShadowSize, -mShadowSize);

        if (mCornerShadowPath == null) {
            mCornerShadowPath = new Path();
        } else {
            mCornerShadowPath.reset();
        }
        mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        mCornerShadowPath.rLineTo(-mShadowSize, 0);
        // outer arc
        mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc
        mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        mCornerShadowPath.close();
        float startRatio = mCornerRadius / (mCornerRadius + mShadowSize);
        mCornerShadowPaint.setShader(new RadialGradient(0, 0, mCornerRadius + mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, startRatio, 1f},
                Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new LinearGradient(0, -mCornerRadius + mShadowSize, 0,
                -mCornerRadius - mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect bounds) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        final float verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER;
        mCardBounds.set(bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset,
                bounds.right - mRawMaxShadowSize, bounds.bottom - verticalOffset);
        mDrawable.setBounds((int) mCardBounds.left, (int) mCardBounds.top,
                (int) mCardBounds.right, (int) mCardBounds.bottom);
        buildShadowCorners();
    }
    final RectF mCornerRect = new RectF();
    public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
        float twoRadius = cornerRadius * 2.0F;
        float innerWidth = bounds.width() - twoRadius - 1.0F;
        float innerHeight = bounds.height() - twoRadius - 1.0F;
        if (cornerRadius >= 1.0F) {
            float roundedCornerRadius = cornerRadius + 0.5F;
            mCornerRect.set(-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius, roundedCornerRadius);
            int saved = canvas.save();
            canvas.translate(bounds.left + roundedCornerRadius, bounds.top + roundedCornerRadius);
            canvas.drawArc(mCornerRect, 180.0F, 90.0F, true, paint);
            canvas.translate(innerWidth, 0.0F);
            canvas.rotate(90.0F);
            canvas.drawArc(mCornerRect, 180.0F, 90.0F, true, paint);
            canvas.translate(innerHeight, 0.0F);
            canvas.rotate(90.0F);
            canvas.drawArc(mCornerRect, 180.0F, 90.0F, true, paint);
            canvas.translate(innerWidth, 0.0F);
            canvas.rotate(90.0F);
            canvas.drawArc(mCornerRect, 180.0F, 90.0F, true, paint);
            canvas.restoreToCount(saved);
            canvas.drawRect(bounds.left + roundedCornerRadius - 1.0F, bounds.top, bounds.right - roundedCornerRadius + 1.0F, bounds.top + roundedCornerRadius, paint);
            canvas.drawRect(bounds.left + roundedCornerRadius - 1.0F, bounds.bottom - roundedCornerRadius, bounds.right - roundedCornerRadius + 1.0F, bounds.bottom, paint);
        }

        canvas.drawRect(bounds.left, bounds.top + cornerRadius, bounds.right, bounds.bottom - cornerRadius, paint);
    }
    float getCornerRadius() {
        return mCornerRadius;
    }

    void getMaxShadowAndCornerPadding(Rect into) {
        getPadding(into);
    }

    void setShadowSize(float size) {
        setShadowSize(size, mRawMaxShadowSize);
    }

    void setMaxShadowSize(float size) {
        setShadowSize(mRawShadowSize, size);
    }

    public float getShadowSize() {
        return mRawShadowSize;
    }

    public float getMaxShadowSize() {
        return mRawMaxShadowSize;
    }

    public float getMinWidth() {
        final float content = 2
                * Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow + mRawMaxShadowSize / 2);
        return content + (mRawMaxShadowSize + mInsetShadow) * 2;
    }

    public float getMinHeight() {
        final float content = 2 * Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow
                + mRawMaxShadowSize * SHADOW_MULTIPLIER / 2);
        return content + (mRawMaxShadowSize * SHADOW_MULTIPLIER + mInsetShadow) * 2;
    }

//    void setColor(@Nullable ColorStateList color) {
//        setBackground(color);
//        invalidateSelf();
//    }


    @Override
    public void getOutline(@NonNull Outline outline) {
        super.getOutline(outline);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDrawable.getOutline(outline);
        }
    }

    public void setDither(boolean dither) {
        this.mDrawable.setDither(dither);
    }
    public void setFilterBitmap(boolean filter) {
        this.mDrawable.setFilterBitmap(filter);
    }
    ColorStateList getColor() {
        return mBackground;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        this.invalidateSelf();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        this.scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        this.unscheduleSelf(what);
    }
    public void setAutoMirrored(boolean mirrored) {
        DrawableCompat.setAutoMirrored(this.mDrawable, mirrored);
    }

    public boolean isAutoMirrored() {
        return DrawableCompat.isAutoMirrored(this.mDrawable);
    }

    public void setTint(int tint) {
        DrawableCompat.setTint(this.mDrawable, tint);
    }

    public void setTintList(ColorStateList tint) {
        DrawableCompat.setTintList(this.mDrawable, tint);
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        DrawableCompat.setTintMode(this.mDrawable, tintMode);
    }

    public void setHotspot(float x, float y) {
        DrawableCompat.setHotspot(this.mDrawable, x, y);
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        DrawableCompat.setHotspotBounds(this.mDrawable, left, top, right, bottom);
    }
}