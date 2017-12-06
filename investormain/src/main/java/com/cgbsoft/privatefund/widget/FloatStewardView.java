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
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
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
    private String serveCode, headerurl, userName;
    private Context floatContext;
    private SemicircleView semicircleview;

    private RoundImageView steward_round_iv;
    private LinearLayout rectangle_in_lay, cardnumber_lay, rectangle_in_text_lay, rectangle_in_user_text_lay;
    private TextView cardnumber_txt, rectangle_in_user_text, steward_phone_bt, steward_note_bt, steward_im_bt;
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


        steward_phone_bt = (TextView) findViewById(R.id.steward_phone_bt);
        steward_note_bt = (TextView) findViewById(R.id.steward_note_bt);
        steward_im_bt = (TextView) findViewById(R.id.steward_im_bt);
        cardnumber_txt = (TextView) findViewById(R.id.cardnumber_txt);
        cardnumber_lay = (LinearLayout) findViewById(R.id.cardnumber_lay);
        semicircleview = (SemicircleView) baseView.findViewById(R.id.semicircleview);
        steward_round_iv = (RoundImageView) baseView.findViewById(R.id.steward_round_iv);
        rectangle_out_lay = (RelativeLayout) baseView.findViewById(R.id.rectangle_out_lay);
        rectangle_in_lay = (LinearLayout) baseView.findViewById(R.id.rectangle_in_lay);
        rectangle_in_text_lay = (LinearLayout) findViewById(R.id.rectangle_in_text_lay);
        rectangle_in_user_text = (TextView) findViewById(R.id.rectangle_in_user_text);
        rectangle_in_user_text_lay = (LinearLayout) findViewById(R.id.rectangle_in_user_text_lay);

        steward_round_iv.setOnClickListener(this);
        cardnumber_lay.setOnClickListener(this);
        rectangle_in_text_lay.setOnClickListener(this);
        rectangle_in_lay.setOnClickListener(this);
        steward_phone_bt.setOnClickListener(this);
        steward_note_bt.setOnClickListener(this);
        steward_im_bt.setOnClickListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.steward_round_iv:
                if (null != floatStewardListener) floatStewardListener.roundImageViewClick();
                openFloat();
                break;
            case R.id.cardnumber_lay:
                closeFloat();
                break;
            case R.id.rectangle_in_lay:
                closeFloat();
                break;
            case R.id.steward_phone_bt:
                if (null != floatStewardListener) floatStewardListener.phoneClick();
                break;
            case R.id.steward_note_bt:
                if (null != floatStewardListener) floatStewardListener.noteClick();
                break;
            case R.id.steward_im_bt:
                if (null != floatStewardListener) floatStewardListener.imClick();
                break;


        }
    }

    /**
     * 初始化
     **/
    public void initFloat(boolean isvisitor, String code, String headerurl, String username, FloatStewardListener stewardListener) {
        this.isVisitor = isvisitor;
        this.floatStewardListener = stewardListener;
        this.serveCode = code;
        this.headerurl = headerurl;
        this.userName = username;
        inflateView();
    }

    /**
     * 刷新
     */
    public void refreshData(boolean isvisitor, String code, String headerurl, String username) {
        this.isVisitor = isvisitor;
        this.serveCode = code;
        this.headerurl = headerurl;
        this.userName = username;
        inflateView();
    }

    /**
     * 开始填充数据
     */
    private void inflateView() {
        if (!isVisitor) {
            BStrUtils.SetTxt(cardnumber_txt, serveCode);
            Imageload.display(floatContext, headerurl, steward_round_iv);
        }

    }


    /**
     * 张开
     **/
    public void openFloat() {
        int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 160);
        if (isVisitor) {
            //游客
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatorValue = (int) animation.getAnimatedValue();
                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 40) + (int) (surplusWidth * fraction);
                    rectangle_in_lay.requestLayout();
                    rectangle_in_text_lay.setVisibility(100 == animatorValue ? VISIBLE : GONE);
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
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 40) + (int) (surplusWidth * fraction);

                    rectangle_in_lay.requestLayout();

                    rectangle_out_lay.getLayoutParams().height = DimensionPixelUtil.dip2px(floatContext, 80) + (int) (fraction * DimensionPixelUtil.dip2px(floatContext, 20));
                    rectangle_out_lay.requestLayout();

                    semicircleview.getLayoutParams().height = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.getLayoutParams().width = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.requestLayout();
                    cardnumber_lay.setVisibility(30 < animatorValue ? VISIBLE : GONE);//(1==animatorValue);
                    rectangle_in_user_text_lay.setVisibility(80 < animatorValue ? VISIBLE : GONE);
                    rectangle_in_user_text.setVisibility(100 == animatorValue ? VISIBLE : INVISIBLE);
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
        int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 160);
        if (isVisitor) {
            //游客
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 40) + surplusWidth - (int) (surplusWidth * fraction);
                    rectangle_in_lay.requestLayout();

                    rectangle_in_text_lay.setVisibility(animatorValue > 10 ? GONE : VISIBLE);
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
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 40) + surplusWidth - (int) (surplusWidth * fraction);

                    rectangle_in_lay.requestLayout();

                    rectangle_out_lay.getLayoutParams().height = DimensionPixelUtil.dip2px(floatContext, 80) + DimensionPixelUtil.dip2px(floatContext, 20) - (int) (fraction * DimensionPixelUtil.dip2px(floatContext, 20));
                    rectangle_out_lay.requestLayout();

                    semicircleview.getLayoutParams().height = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.getLayoutParams().width = rectangle_out_lay.getLayoutParams().height;

                    semicircleview.requestLayout();
                    cardnumber_lay.setVisibility(50 < animatorValue ? GONE : VISIBLE);//(1==animatorValue);
                    rectangle_in_user_text_lay.setVisibility(20 < animatorValue ? GONE : VISIBLE);
                    rectangle_in_user_text.setVisibility(20 < animatorValue ? GONE : VISIBLE);
                }
            });
            valueAnimator.setInterpolator(new BounceInterpolator());//LinearInterpolator
            valueAnimator.setDuration(3 * 1000);
//            valueAnimator.setTarget(rectangle_in_lay,rectangle_out_lay);
            valueAnimator.start();
        }
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
