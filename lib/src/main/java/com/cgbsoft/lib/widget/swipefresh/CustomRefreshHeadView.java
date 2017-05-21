package com.cgbsoft.lib.widget.swipefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.Typefaces;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-14:54
 */
public class CustomRefreshHeadView extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {
    private ShimmerTextView titanicTextView;
    private Shimmer titanic = new Shimmer();
    private Context context;

    public CustomRefreshHeadView(Context context) {
        super(context,null,0);
        this.context=context;
        init();
    }

    public CustomRefreshHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        init();
    }

    public CustomRefreshHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.view_comment_list_head, null);
        addView(view, lp);
        titanicTextView= (ShimmerTextView) view.findViewById(R.id.view_comment_head_txt);
//        titanicTextView.setTypeface(Typefaces.get(context, "Satisfy-Regular.ttf"));
        titanic.setDuration(500l);

    }

    @Override
    public void onRefresh() {
        titanic.start(titanicTextView);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }


    @Override
    public void onRelease() {


    }

    @Override
    public void onComplete() {
        titanic.cancel();
    }


    @Override
    public void onReset() {

    }
}
