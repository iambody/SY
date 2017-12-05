package com.cgbsoft.privatefund.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.privatefund.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-15:06
 */
public class FloatStewardView extends RelativeLayout implements View.OnClickListener {
    private boolean isVisitor = false;
    private Context floatContext;
    private SemicircleView semicircleview;

    private RoundImageView steward_round_iv;
    //外层轮廓,外层轮廓
    private LinearLayout rectangle_in_lay;
    private RelativeLayout rectangle_out_lay;
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
        this.floatContext = context;
        baseView = View.inflate(context, R.layout.view_float_steward, this);

        semicircleview = (SemicircleView) baseView.findViewById(R.id.semicircleview);
        steward_round_iv = (RoundImageView) baseView.findViewById(R.id.steward_round_iv);
        rectangle_out_lay = (RelativeLayout) baseView.findViewById(R.id.rectangle_out_lay);
        rectangle_in_lay = (LinearLayout) baseView.findViewById(R.id.rectangle_in_lay);
        steward_round_iv.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.steward_round_iv:
                openFloat();
                break;
        }
    }

    /**
     * 初始化
     **/
    public void initFloat(boolean isvisitor, FloatStewardListener stewardListener) {
        this.isVisitor = isvisitor;
        this.floatStewardListener = stewardListener;
    }

    /**
     * 张开
     **/
    public void openFloat() {
        int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 160);
        int surplusWidth1 = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 120);
        if (isVisitor) {
            //游客
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width =  DimensionPixelUtil.dip2px(floatContext, 40) + (int) (surplusWidth * fraction);
                    rectangle_in_lay.requestLayout();
                }
            });
            valueAnimator.setInterpolator(new BounceInterpolator());//LinearInterpolator
            valueAnimator.setDuration(3 * 1000);
            valueAnimator.setTarget(rectangle_in_lay);
            valueAnimator.start();
        } else {
            //用户

            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width =  DimensionPixelUtil.dip2px(floatContext, 40) + (int) (surplusWidth * fraction);

                    rectangle_in_lay.requestLayout();

                    rectangle_out_lay.getLayoutParams().height= DimensionPixelUtil.dip2px(floatContext, 80)+(int)(fraction* DimensionPixelUtil.dip2px(floatContext, 20));
                    rectangle_out_lay.requestLayout();

                    semicircleview.getLayoutParams().height=rectangle_out_lay.getLayoutParams().height;
                    semicircleview.getLayoutParams().width=rectangle_out_lay.getLayoutParams().height;
                    semicircleview.requestLayout();

                }
            });
            valueAnimator.setInterpolator(new BounceInterpolator());//LinearInterpolator
            valueAnimator.setDuration(3 * 1000);
//            valueAnimator.setTarget(rectangle_in_lay,rectangle_out_lay);
            valueAnimator.start();

        }
    }

    /**
     * 关闭
     **/
    public void closeFloat() {
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
