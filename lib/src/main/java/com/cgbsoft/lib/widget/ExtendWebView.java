package com.cgbsoft.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cgbsoft.lib.base.webview.BaseWebview;

/**
 * @author chenlong
 */
public class ExtendWebView extends BaseWebview {

    private OnScrollChangedCallback mOnScrollChangedCallback;
    private static final int SCROLL_MAX = 70;
//    private GestureDetector gestureDetector;

    public ExtendWebView(Context context) {
        super(context);
    }

    public ExtendWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback mOnScrollChangedCallback) {
        this.mOnScrollChangedCallback = mOnScrollChangedCallback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        float webcontent = getContentHeight() * getScale();
//        float webnow = getHeight() - DimensionPixelUtil.dp2px(getContext(), 110) + getScrollY();
//        if (Math.abs(webcontent - webnow) < 5) { // 处于底端
//            mOnScrollChangedCallback.onScrollToBottom();
//            System.out.println("----------onScrollToBottom");
//        } else if (getScrollY() == 0) {  // 处于顶端
//            mOnScrollChangedCallback.onScrollToTop();
//            System.out.println("----------onScrollToTop");
//        } else {
            int scrollX = l - oldl;
            int scrollY = t - oldt;
            System.out.println("---t=" + t + "----oldt=" + oldt +  "----scrollX=" + scrollX + "------srcollY=" + scrollY);
            if (Math.abs(scrollX)*1.5 < Math.abs(scrollY) && Math.abs(scrollY) > SCROLL_MAX) {
                if (scrollY < 0) {
                    System.out.println("----------onScrollDown");
                    mOnScrollChangedCallback.onScrollDown();
                } else {
                    System.out.println("----------onScrollUp");
                    mOnScrollChangedCallback.onScrollUp();
                }
            }
//        }
    }

    public interface OnScrollChangedCallback {
        void onScrollUp();
        void onScrollDown();
    }
}
