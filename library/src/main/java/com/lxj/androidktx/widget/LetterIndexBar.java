package com.lxj.androidktx.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.blankj.utilcode.util.ConvertUtils;

/**
 * 字母索引
 */
public class LetterIndexBar extends View {

    /**
     * 索引字母颜色
     */
    private static final int LETTER_COLOR = 0xFF222222;

    /**
     * 索引字母数组
     */
    public String[] indexs = new String[]{ "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 控件的宽高
     */
    private int mWidth;
    private int mHeight;
    private int fontSize = 12;

    /**
     * 单元格的高度
     */
    private float mCellHeight;
    private Paint mPaint;

    public LetterIndexBar(Context context) {
        super(context);
        init();
    }

    public LetterIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(LETTER_COLOR);
        mPaint.setTextSize(ConvertUtils.sp2px(fontSize));
        mPaint.setAntiAlias(true); // 去掉锯齿，让字体边缘变得平滑
    }

    public void setFontSize(int size){
        this.fontSize = size;
        mPaint.setTextSize(ConvertUtils.sp2px(fontSize));
        invalidate();
    }

    public void setIndexs(String[] indexs) {
        this.indexs = indexs;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //字母的坐标点：(x,y)
        if (indexs.length <= 0) {
            return;
        }
        for (int i = 0; i < indexs.length; i++) {
            String letter = indexs[i];
            float x = mWidth / 2 - getTextWidth(letter) / 2;
            float y = mCellHeight / 2 + getTextHeight(letter) / 2 + mCellHeight * i + getPaddingTop();
            canvas.drawText(letter, x, y, mPaint);
        }
    }

    /**
     * 获取字符的宽度
     *
     * @param text 需要测量的字母
     * @return 对应字母的高度
     */
    private float getTextWidth(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获取字符的高度
     *
     * @param text 需要测量的字母
     * @return 对应字母的高度
     */
    private float getTextHeight(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        mCellHeight = (mHeight * 1f / indexs.length);    //26个字母加上“#”
    }

    int letterIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 按下字母的下标
                int index = (int) ((event.getY() - getPaddingTop()) / mCellHeight);
                if(index!=letterIndex){
                    letterIndex = index;
                    // 判断是否越界
                    if (letterIndex >= 0 && letterIndex < indexs.length) {
                        //通过回调方法通知列表定位
                        if (mOnIndexChangedListener != null) {
                            mOnIndexChangedListener.onIndexChanged(indexs[letterIndex]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                letterIndex = -1;
                break;
        }

        return true;
    }

    public interface OnIndexChangedListener {
        /**
         * 按下字母改变了
         *
         * @param letter 按下的字母
         */
        void onIndexChanged(String letter);
    }

    private OnIndexChangedListener mOnIndexChangedListener;

    public void setOnIndexChangedListener(OnIndexChangedListener onIndexChangedListener) {
        this.mOnIndexChangedListener = onIndexChangedListener;
    }

}