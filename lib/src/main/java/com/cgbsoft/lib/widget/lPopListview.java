package com.cgbsoft.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * desc  Pop里面用到的ls
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/10-17:47
 */
public class lPopListview extends ListView {

    public lPopListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public lPopListview(Context context) {
        super(context);
    }

    public lPopListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = meathureWidthByChilds() + getPaddingLeft()
                + getPaddingRight();
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY),
                heightMeasureSpec);
    }

    public int meathureWidthByChilds() {
        int maxWidth = 0;
        int maxheight=0;//h
        View view = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredHeight() > maxheight) {
                maxheight = view.getMeasuredWidth();

            }
        }
        return maxheight;
    }

}
