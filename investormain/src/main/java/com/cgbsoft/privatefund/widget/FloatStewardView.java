package com.cgbsoft.privatefund.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cgbsoft.privatefund.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-15:06
 */
public class FloatStewardView extends RelativeLayout {

    private View baseView;

    public FloatStewardView(Context context) {
        super(context);
    }

    public FloatStewardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FloatStewardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 开始初始化view
     */
    private void initView(Context context) {
        baseView = View.inflate(context, R.layout.view_float_steward, this);

    }


}
