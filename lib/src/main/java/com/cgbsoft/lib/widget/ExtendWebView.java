package com.cgbsoft.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.cgbsoft.lib.base.webview.BaseWebview;

/**
 * @author chenlong
 */
public class ExtendWebView extends BaseWebview {

    private OnScrollChangedCallback mOnScrollChangedCallback;
    private static final int SCROLL_MAX = 40;

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
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            int scrollX = l - oldl;
            int scrollY = t - oldt;
            if (Math.abs(scrollX)*1.5 < Math.abs(scrollY) && Math.abs(scrollY) > SCROLL_MAX) {
                if (scrollY < 0) {
                    mOnScrollChangedCallback.onScrollUp();
                } else {
                    mOnScrollChangedCallback.onScrollDown();
                }
            }
        }
    }

    public interface OnScrollChangedCallback {
        void onScrollUp();
        void onScrollDown();
    }
}
