//package com.lxj.androidktxdemo.blibli;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.OverScroller;
//
//import androidx.annotation.NonNull;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.core.math.MathUtils;
//
//import com.blankj.utilcode.util.ConvertUtils;
//import com.google.android.material.appbar.AppBarLayout;
//import com.lxj.androidktxdemo.R;
//
//import java.lang.reflect.Field;
//
//
//public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {
//
//    private static final String TAG = "FixBehavior";
//    private static final int TYPE_FLING = 1;
//    private boolean isFlinging;
//    private boolean shouldBlockNestedScroll;
//    int maxHeight = ConvertUtils.dp2px(510);
//    int minHeight = ConvertUtils.dp2px(210);
//    int maxPadding = 0;
//    ViewGroup video ;
//    AppBarLayout appbar ;
//    public FixAppBarLayoutBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        maxPadding = maxHeight - minHeight;
//        setDragCallback(new BaseDragCallback() {
//            @Override
//            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
//        video = child.findViewById(R.id.appbar_scale_header);
//        appbar = child;
//        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
//        LogUtil.d(TAG, "onInterceptTouchEvent:" + child.getTotalScrollRange());
////        shouldBlockNestedScroll = isFlinging;
//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
////                stopAppbarLayoutFling(child);
//                break;
//            default:
//                break;
//        }
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }
//
//    /**
//     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
//     * @return Field
//     */
//    private Field getFlingRunnableField() throws NoSuchFieldException {
//        Class<?> superclass = this.getClass().getSuperclass();
//        try {
//            // support design 27及一下版本
//            Class<?> headerBehaviorType = null;
//            if (superclass != null) {
//                headerBehaviorType = superclass.getSuperclass();
//            }
//            if (headerBehaviorType != null) {
//                return headerBehaviorType.getDeclaredField("mFlingRunnable");
//            }else {
//                return null;
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            // 可能是28及以上版本
//            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
//            if (headerBehaviorType != null) {
//                return headerBehaviorType.getDeclaredField("flingRunnable");
//            } else {
//                return null;
//            }
//        }
//    }
//
//    /**
//     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
//     * @return Field
//     */
//    private Field getScrollerField() throws NoSuchFieldException {
//        Class<?> superclass = this.getClass().getSuperclass();
//        try {
//            // support design 27及一下版本
//            Class<?> headerBehaviorType = null;
//            if (superclass != null) {
//                headerBehaviorType = superclass.getSuperclass();
//            }
//            if (headerBehaviorType != null) {
//                return headerBehaviorType.getDeclaredField("mScroller");
//            }else {
//                return null;
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            // 可能是28及以上版本
//            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
//            if (headerBehaviorType != null) {
//                return headerBehaviorType.getDeclaredField("scroller");
//            }else {
//                return null;
//            }
//        }
//    }
//
//    /**
//     * 停止appbarLayout的fling事件
//     * @param appBarLayout
//     */
//    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
//        //通过反射拿到HeaderBehavior中的flingRunnable变量
//        try {
//            Field flingRunnableField = getFlingRunnableField();
//            Runnable flingRunnable;
//            if (flingRunnableField != null) {
//                flingRunnableField.setAccessible(true);
//                flingRunnable = (Runnable) flingRunnableField.get(this);
//                if (flingRunnable != null) {
//                    LogUtil.d(TAG, "存在flingRunnable");
//                    appBarLayout.removeCallbacks(flingRunnable);
//                    flingRunnableField.set(this, null);
//                }
//            }
//
//            Field scrollerField = getScrollerField();
//            if (scrollerField != null) {
//                scrollerField.setAccessible(true);
//                OverScroller overScroller = (OverScroller) scrollerField.get(this);
//                if (overScroller != null && !overScroller.isFinished()) {
//                    overScroller.abortAnimation();
//                }
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
//                                       View directTargetChild, View target,
//                                       int nestedScrollAxes, int type) {
//        LogUtil.d(TAG, "onStartNestedScroll");
////        stopAppbarLayoutFling(child);
////        return super.onStartNestedScroll(parent, child, directTargetChild, target,
////                nestedScrollAxes, type);
//        return true;
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
//                                  AppBarLayout child, View target,
//                                  int dx, int dy, int[] consumed, int type) {
//        LogUtil.d(TAG, "onNestedPreScroll:" + child.getTotalScrollRange()
//                + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type);
//        //type返回1时，表示当前target处于非touch的滑动，
//        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动
//        //子类还未结束其自身的fling
//        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
//        if (type == TYPE_FLING) {
//            isFlinging = true;
//        }
//        if (!shouldBlockNestedScroll) {
//            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//        }
//        changeHeight(dy);
//    }
//
//    private void changeHeight(int dy){
////        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) vv.getLayoutParams();
////        params.height = MathUtils.clamp(vv.getHeight()+dy, 0, maxP);
//////        params.setScrollFlags(  params.height>= maxP ? AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL : AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
////        vv.setLayoutParams(params);
////        int p = video.getPaddingTop() + dy;
////        int r = 0;
////        if(p > maxP){
////            r = maxP - p;
////            p = maxP;
////        }else if (p < 0){
////            r = -p;
////            p = 0;
//////        }
////        int newP = MathUtils.clamp(p, 0, maxP);
////        video.setPadding(0, newP, 0,0);
////        appbar.offsetTopAndBottom(-r);
//    }
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
//                               View target, int dxConsumed, int dyConsumed, int
//                                       dxUnconsumed, int dyUnconsumed, int type) {
//        LogUtil.d(TAG, "onNestedScroll: target:" + target.getClass() + " ,"
//                + child.getTotalScrollRange() + " ,dxConsumed:"
//                + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type);
//        if (!shouldBlockNestedScroll) {
//            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
//                    dyConsumed, dxUnconsumed, dyUnconsumed, type);
//        }
//    }
//
//    @Override
//    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl,
//                                   View target, int type) {
//        LogUtil.d(TAG, "onStopNestedScroll");
//        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
//        isFlinging = false;
//        shouldBlockNestedScroll = false;
//    }
//
//    private static class LogUtil{
//        static void d(String tag, String string){
//            Log.d(tag,string);
//        }
//    }
//
//}