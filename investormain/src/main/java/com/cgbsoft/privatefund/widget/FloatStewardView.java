package com.cgbsoft.privatefund.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.privatefund.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-15:06
 */
public class FloatStewardView extends RelativeLayout implements View.OnClickListener {
    private final int ANIMATOR_TIME = 1 * 600;
    private final int TIMECOUNTDOWN = 6;
    private boolean isVisitor;
    private boolean isOpen;
    private View baseView;

    private int ivHeight=30;
    private int layWidth=60;

    private Context floatContext;
    private SemicircleView semicircleview;
    private RelativeLayout rectangle_out_lay;
    private RoundImageView steward_round_iv;
    //    private ImageView steward_arrow_iv;
    private String serveCode, headerurl, userName;
    private LinearLayout rectangle_in_lay, cardnumber_lay, rectangle_in_text_lay, rectangle_in_user_text_lay;
    private TextView cardnumber_txt, rectangle_in_user_text, steward_phone_bt, steward_note_bt, steward_im_bt, steward_inf_bt;
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
        steward_inf_bt = (TextView) findViewById(R.id.steward_inf_bt);
//        cardnumber_txt = (TextView) findViewById(R.id.cardnumber_txt);
//        cardnumber_lay = (LinearLayout) findViewById(R.id.cardnumber_lay);
//        steward_arrow_iv = (ImageView) findViewById(R.id.steward_arrow_iv);
        semicircleview = (SemicircleView) baseView.findViewById(R.id.semicircleview);
        steward_round_iv = (RoundImageView) baseView.findViewById(R.id.steward_round_iv);
        rectangle_out_lay = (RelativeLayout) baseView.findViewById(R.id.rectangle_out_lay);
        rectangle_in_lay = (LinearLayout) baseView.findViewById(R.id.rectangle_in_lay);
        rectangle_in_text_lay = (LinearLayout) findViewById(R.id.rectangle_in_text_lay);
        rectangle_in_user_text = (TextView) findViewById(R.id.rectangle_in_user_text);
        rectangle_in_user_text_lay = (LinearLayout) findViewById(R.id.rectangle_in_user_text_lay);

        semicircleview.setOnClickListener(this);
        steward_round_iv.setOnClickListener(this);
//        cardnumber_lay.setOnClickListener(this);
        rectangle_in_text_lay.setOnClickListener(this);
        rectangle_in_lay.setOnClickListener(this);
        steward_phone_bt.setOnClickListener(this);
        steward_note_bt.setOnClickListener(this);
        steward_im_bt.setOnClickListener(this);
        steward_inf_bt.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.steward_round_iv://头像点击
//                if (null != floatStewardListener) floatStewardListener.roundImageViewClick();
                if (isOpen) closeFloat();
                else
                    openFloat();
                break;
//            case R.id.cardnumber_lay://绑定理财师的邀请码布局点击
////                closeFloat();
//                break;
            case R.id.rectangle_in_lay://绑定和未绑定公用的外层布局
//                closeFloat();
                break;
            case R.id.steward_phone_bt://电话点击
                if (null != floatStewardListener) floatStewardListener.phoneClick();
                break;
            case R.id.steward_note_bt://短信点击
                if (null != floatStewardListener) floatStewardListener.noteClick();
                break;
            case R.id.steward_im_bt://im点击
                if (null != floatStewardListener) floatStewardListener.imClick();
                break;
            case R.id.rectangle_in_text_lay://未绑定理财师或者游客模式的文字点击
                if (null != floatStewardListener) floatStewardListener.visitorTxtClick();
//                closeFloat();
                break;
            case R.id.steward_inf_bt://档案
                if (null != floatStewardListener) floatStewardListener.record();
                break;
            case R.id.semicircleview:
                closeFloat();
                break;


        }
    }

    /**
     * 初始化
     **/
    public void initFloat(boolean isvisitor, FloatStewardListener stewardListener) {
        this.isVisitor = isvisitor;
        this.floatStewardListener = stewardListener;
        this.serveCode = AppManager.getUserInfo(floatContext).bandingAdviserUniqueCode;
        this.headerurl = AppManager.getUserInfo(floatContext).bandingAdviserHeadImageUrl;
        this.userName = AppManager.getUserInfo(floatContext).nickName;
        inflateView();
    }

    /**
     * 刷新
     */
    public void refreshData(boolean isvisitor) {
        this.isVisitor = isvisitor;
        this.serveCode = AppManager.getUserInfo(floatContext).bandingAdviserUniqueCode;
        this.headerurl = AppManager.getUserInfo(floatContext).bandingAdviserHeadImageUrl;
        this.userName = AppManager.getUserInfo(floatContext).nickName;
        inflateView();
    }

    /**
     * 开始填充数据
     */
    private void inflateView() {
        if (!isVisitor) {
//            BStrUtils.SetTxt(cardnumber_txt, serveCode);
            Imageload.display(floatContext, headerurl, steward_round_iv);
            rectangle_in_user_text.setText(String.format("尊敬的%s，我是您的私人银行家，很高兴为您服务！", BStrUtils.NullToStr1(userName)));
        }

    }


    /**
     * 张开
     **/
    public void openFloat() {
        if (isOpen)
            return;
        isOpen = true;

        if (isVisitor) {
            int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 200);
            //游客
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatorValue = (int) animation.getAnimatedValue();
//                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 36) + (int) (surplusWidth * fraction);
                    rectangle_in_lay.requestLayout();
                    rectangle_in_text_lay.setVisibility(100 == animatorValue ? VISIBLE : GONE);
//                    steward_arrow_iv.setVisibility(100 == animatorValue ? VISIBLE : GONE);
                    if (100 == animatorValue) timeCount(true);

                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());//LinearInterpolator  BounceInterpolator
            valueAnimator.setDuration(ANIMATOR_TIME);
            valueAnimator.setTarget(rectangle_in_lay);
            valueAnimator.start();
        } else {
            int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 160);
            //用户
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
//                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 36) + (int) (surplusWidth * fraction);

                    rectangle_in_lay.requestLayout();

                    rectangle_out_lay.getLayoutParams().height = DimensionPixelUtil.dip2px(floatContext, 70) + (int) (fraction * DimensionPixelUtil.dip2px(floatContext, 20));
                    rectangle_out_lay.requestLayout();

                    semicircleview.getLayoutParams().height = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.getLayoutParams().width = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.requestLayout();
//                    cardnumber_lay.setVisibility(65 < animatorValue ? VISIBLE : GONE);//(1==animatorValue);
                    rectangle_in_user_text_lay.setVisibility(80 < animatorValue ? VISIBLE : GONE);
                    rectangle_in_user_text.setVisibility(100 <= animatorValue ? VISIBLE : INVISIBLE);
//                    steward_arrow_iv.setVisibility(100 == animatorValue ? VISIBLE : GONE);
                    if (100 == animatorValue) timeCount(true);
                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());//LinearInterpolator  BounceInterpolator
            valueAnimator.setDuration(ANIMATOR_TIME);
//            valueAnimator.setTarget(rectangle_in_lay,rectangle_out_lay);
            valueAnimator.start();

        }
        TrackingDataManger.homePersonOpen(floatContext);
    }

    /**
     * 关闭
     **/
    public void closeFloat() {
        timeCount(false);
        if (!isOpen) return;
        isOpen = false;

//        steward_arrow_iv.setVisibility(GONE);
        if (isVisitor) {
            int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 200);
            //游客
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
//                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 36) + surplusWidth - (int) (surplusWidth * fraction);
                    rectangle_in_lay.requestLayout();

                    rectangle_in_text_lay.setVisibility(animatorValue > 10 ? GONE : VISIBLE);

                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());//LinearInterpolator  BounceInterpolator
            valueAnimator.setDuration(ANIMATOR_TIME);
            valueAnimator.setTarget(rectangle_in_lay);
            valueAnimator.start();
        } else {
            int surplusWidth = Utils.getScreenWidth(floatContext) - DimensionPixelUtil.dip2px(floatContext, 160);
            //用户
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int animatorValue = (int) animation.getAnimatedValue();
//                    Log.i("sskkk", "位置" + animatorValue);
                    float fraction = animatorValue / 100f;
                    rectangle_in_lay.getLayoutParams().width = DimensionPixelUtil.dip2px(floatContext, 36) + surplusWidth - (int) (surplusWidth * fraction);

                    rectangle_in_lay.requestLayout();

                    rectangle_out_lay.getLayoutParams().height = DimensionPixelUtil.dip2px(floatContext, 70) + DimensionPixelUtil.dip2px(floatContext, 20) - (int) (fraction * DimensionPixelUtil.dip2px(floatContext, 20));
                    rectangle_out_lay.requestLayout();

                    semicircleview.getLayoutParams().height = rectangle_out_lay.getLayoutParams().height;
                    semicircleview.getLayoutParams().width = rectangle_out_lay.getLayoutParams().height;

                    semicircleview.requestLayout();
//                    cardnumber_lay.setVisibility(50 < animatorValue ? GONE : VISIBLE);//(1==animatorValue);
                    rectangle_in_user_text_lay.setVisibility(20 < animatorValue ? GONE : VISIBLE);
                    rectangle_in_user_text.setVisibility(20 < animatorValue ? GONE : VISIBLE);
                }
            });
            valueAnimator.setInterpolator(new LinearInterpolator());//LinearInterpolator  BounceInterpolator
            valueAnimator.setDuration(ANIMATOR_TIME);
//            valueAnimator.setTarget(rectangle_in_lay,rectangle_out_lay);
            valueAnimator.start();
        }

        TrackingDataManger.homePersonClose(floatContext);
    }


    public interface FloatStewardListener {
        //头像点击
        void record();

        //游客模式下点击文字
        void visitorTxtClick();

        //电话点击
        void phoneClick();

        //短信点击
        void noteClick();

        //im点击
        void imClick();
    }


    private void timeCount(boolean isClos) {
        if (isClos) {
            RxCountDown.countTimeDown(TIMECOUNTDOWN, new RxCountDown.ICountTime() {
                @Override
                public void onCompleted() {
                    closeFloat();
                }

                @Override
                public void onNext(int timer) {

                }
            });
        } else {
            RxCountDown.stopCountTime();
        }

    }
}
