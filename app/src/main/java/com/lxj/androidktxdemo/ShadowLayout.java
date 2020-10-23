package com.lxj.androidktxdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

/**
 * ShadowLayout
 * <p>
 * Created by lijiankun24 on 2018/9/25
 * Email: jiankunli24@gmail.com
 */
public class ShadowLayout extends FrameLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;

    /**
     * 阴影的颜色
     */
    private int mShadowColor = Color.TRANSPARENT;

    /**
     * 阴影的大小范围
     */
    private float mShadowRadius = 0;

    /**
     * 阴影 x 轴的偏移量
     */
    private float mShadowDx = 0;

    /**
     * 阴影 y 轴的偏移量
     */
    private float mShadowDy = 0;

    /**
     * 阴影显示的边界
     */
    private int mShadowSide = ALL;

    /**
     * 阴影的形状，圆形/矩形
     */
    private int mShadowShape = SHAPE_RECTANGLE;

    private ShadowDrawable mShadowDrawable;

    public ShadowLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(@Nullable AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadowShape, SHAPE_RECTANGLE);
            mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, 0);
            mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor,
                    getContext().getResources().getColor(android.R.color.black));
            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, 0);
            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, 0);
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide, ALL);

            typedArray.recycle();
        }

        mShadowDrawable = new ShadowDrawable(mShadowShape, mShadowColor,
                mShadowRadius, mShadowDx, mShadowDy);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);       // 关闭硬件加速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float effect = mShadowRadius;
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getMeasuredWidth();
        float rectBottom = this.getMeasuredHeight();
        Log.i("ShadowLayout", "ShadowLayout onMeasure getMeasuredWidth " + this.getMeasuredWidth());
        Log.i("ShadowLayout", "ShadowLayout onMeasure getMeasuredHeight " + this.getMeasuredHeight());
        this.getWidth();
        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = -effect;
        }
        if ((mShadowSide & TOP) == TOP) {
            rectTop = -effect;
        }
        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() + effect;
        }
        if ((mShadowSide & BOTTOM) == BOTTOM) {
            rectBottom = this.getMeasuredHeight() + effect;
        }
        if (mShadowDy != 0.0f) {
            rectBottom = rectBottom + mShadowDy;
        }
        if (mShadowDx != 0.0f) {
            rectRight = rectRight + mShadowDx;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize((int) (rectRight - rectLeft)), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize((int) (rectBottom - rectTop)), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("ShadowLayout", "ShadowLayout onMeasure getMeasuredWidth " + px2dip(this.getMeasuredWidth()));
        Log.i("ShadowLayout", "ShadowLayout onMeasure getMeasuredHeight " + px2dip(this.getMeasuredHeight()));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i("ShadowLayout", "ShadowLayout dispatchDraw");
        super.dispatchDraw(canvas);
        ViewCompat.setBackground(ShadowLayout.this, mShadowDrawable);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // NO OP
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // NO OP
    }

    /**
     * dip2px dp 值转 px 值
     *
     * @param dpValue dp 值
     * @return px 值
     */
    private float dp2Px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

