package com.cgbsoft.privatefund.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenlong
 */
public class CustomViewPage extends ViewPager {

    private Map<Integer, Integer> map = new HashMap<>(2);
    private int currentPage;

    public CustomViewPage(Context context) {
        this(context, null);
        init(context);
    }

    public CustomViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        map.put(0, DimensionPixelUtil.dip2px(context, 110));
        map.put(0, DimensionPixelUtil.dip2px(context, 110));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (map.size() > currentPage) {
            height = map.get(currentPage);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        this.currentPage = current;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (map.size() > currentPage) {
            if (params == null) {
                params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, map.get(current));
            } else {
                params.height = map.get(current);
            }
            setLayoutParams(params);
        }
    }

    public void addHeight(int current, int height) {
        map.put(current, height);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}