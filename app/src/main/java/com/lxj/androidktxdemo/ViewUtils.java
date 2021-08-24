package com.lxj.androidktxdemo;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class ViewUtils {

    public static Drawable generateBackgroundWithShadow(View view, int backgroundColor,
                                                         float cornerRadius,
                                                         int shadowColor,
                                                        int elevation,
                                                        int shadowGravity) {
        int shadowColorValue = shadowColor;
        int backgroundColorValue = backgroundColor;

        float[] outerRadius = {cornerRadius, cornerRadius, cornerRadius,
                cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                cornerRadius};

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(cornerRadius, 0, 0, 0);

        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = elevation;
        shapeDrawablePadding.right = elevation;

        int DY;
        switch (shadowGravity) {
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevation;
                shapeDrawablePadding.bottom = elevation;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevation*2;
                shapeDrawablePadding.bottom = elevation;
                DY = -1*elevation/3;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevation;
                shapeDrawablePadding.bottom = elevation*2;
                DY = elevation/3;
                break;
        }

        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadius/3, 0, DY, shadowColorValue);

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevation, elevation*2, elevation, elevation*2);

        return drawable;

    }
}