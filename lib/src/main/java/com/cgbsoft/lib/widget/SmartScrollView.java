package com.cgbsoft.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/6-18:07
 */
public class SmartScrollView extends ScrollView {
    private boolean isScrolledToTop = true; // 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    //需要进行手势拦截解决和横向滑动布局的手势冲突
    private GestureDetector mGestureDetector;
    private View.OnTouchListener mGestureListener;

    public SmartScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {

        void onSmartScrollListener(boolean isTop, boolean isBottom, int scrollX, int scrollY, int scrolloldX, int scrolloldY);
    }

    public void setScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        if (scrollY == 0) {
//            isScrolledToTop = clampedY;
//            isScrolledToBottom = false;
//        } else {
//            isScrolledToTop = false;
//            isScrolledToBottom = clampedY;
//        }
//        notifyScrollChangedListeners();
//    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
        if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
            isScrolledToTop = true;
            isScrolledToBottom = false;
        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
            // 小心踩坑2: 这里不能是 >=
            // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
            isScrolledToBottom = true;
            isScrolledToTop = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = false;
        }
        notifyScrollChangedListeners(l, t, oldl, oldt);
//        }
        // 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }

    private void notifyScrollChangedListeners(int l, int t, int oldl, int oldt) {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onSmartScrollListener(true, false, l, t, oldl, oldt);
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onSmartScrollListener(false, true, l, t, oldl, oldt);
            }
        } else {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onSmartScrollListener(false, false, l, t, oldl, oldt);
            }
        }
    }

    /**
     * 触发拦截触摸事件
     * 一但返回True（代表事件在当前的viewGroup中会被处理），则向下传递之路被截断（所有子控件将没有机会参与Touch事件），
     * 同时把事件传递给当前的控件的onTouchEvent()处理；返回false，则把事件交给子控件的onInterceptTouchEvent()
     * <p>
     * <p>
     * onInterceptTouchEvent方法是关键，重写这个方法使如果ScrollView有touch事件时不被拦截，
     * 这样只要ScrollView有touch事件优先处理，这样就保证了滑动的流畅。
     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev)
//                && mGestureDetector.onTouchEvent(ev);
//    }
    private float xDistance, yDistance, lastX, lastY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }
}
