package com.cgbsoft.privatefund.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.privatefund.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-15:06
 */
public class FloatStewardView extends RelativeLayout implements View.OnClickListener {
    private boolean isVisitor;
    private SemicircleView semicircleview;

    private RoundImageView steward_round_iv;
    //外层轮廓,外层轮廓
    private LinearLayout rectangle_out_lay, rectangle_in_lay;

    private View baseView;

    private AnimatorSet openVisitorAnmatorSet, openUserAnmatorSet, closeVisitorAnmatorSet, closeUserAnmatorSet;

    private FloatStewardListener floatStewardListener;

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

        semicircleview = (SemicircleView) baseView.findViewById(R.id.semicircleview);
        steward_round_iv = (RoundImageView) baseView.findViewById(R.id.steward_round_iv);
        rectangle_out_lay = (LinearLayout) baseView.findViewById(R.id.rectangle_out_lay);
        steward_round_iv.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化
     **/
    public void initVisitor(boolean isvisitor, FloatStewardListener stewardListener) {
        this.isVisitor = isvisitor;
        this.floatStewardListener = stewardListener;
    }

    /**
     * 张开
     **/
    public void openVisitor() {
        if (isVisitor) {
//            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(rectangle_out_lay,"scaleX",1f,3f)
        } else {
        }
    }

    /**
     * 关闭
     **/
    public void closeVisitor() {
    }


    public interface FloatStewardListener {
        //头像点击
        void roundImageViewClick();

        //电话点击
        void phoneClick();

        //短信点击
        void noteClick();

        //im点击
        void imClick();
    }

}
