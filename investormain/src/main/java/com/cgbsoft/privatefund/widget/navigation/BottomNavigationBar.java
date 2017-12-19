package com.cgbsoft.privatefund.widget.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cgbsoft.lib.utils.SkineColorManager;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class BottomNavigationBar extends FrameLayout implements RxConstant {
    @BindView(R.id.iv_bottom_nav_center)
    ImageView ivBottomNavCenter;

    private BottomClickListener bottomClickListener;

    @BindView(R.id.fl_bottom_nav_left_first)
    FrameLayout fl_bottom_nav_left_first;
    @BindView(R.id.fl_bottom_nav_left_second)
    FrameLayout fl_bottom_nav_left_second;
    @BindView(R.id.fl_bottom_nav_center)
    FrameLayout fl_bottom_nav_center;
    @BindView(R.id.fl_bottom_nav_right_first)
    FrameLayout fl_bottom_nav_right_first;
    @BindView(R.id.fl_bottom_nav_right_second)
    FrameLayout fl_bottom_nav_right_second;

    @BindView(R.id.iv_bottom_nav_left_first)
    ImageView iv_bottom_nav_left_first;
    @BindView(R.id.iv_bottom_nav_left_second)
    ImageView iv_bottom_nav_left_second;
    @BindView(R.id.iv_bottom_nav_right_first)
    ImageView iv_bottom_nav_right_first;
    @BindView(R.id.iv_bottom_nav_right_second)
    ImageView iv_bottom_nav_right_second;

    @BindView(R.id.tv_bottom_nav_left_first)
    TextView tv_bottom_nav_left_first;
    @BindView(R.id.tv_bottom_nav_center)
    TextView tv_bottom_nav_center;
    @BindView(R.id.tv_bottom_nav_left_second)
    TextView tv_bottom_nav_left_second;
    @BindView(R.id.tv_bottom_nav_right_first)
    TextView tv_bottom_nav_right_first;
    @BindView(R.id.tv_bottom_nav_right_second)
    TextView tv_bottom_nav_right_second;

    private static final int doubleClickTime = 3;
    private List<TextView> bottomls;
    //消息文本
//    @BindView(R.id.tv_bottom_nav_msgnum)
//    TextView tv_bottom_nav_msgnum;

    private Observable<Boolean> changeIdtentifyObservable;
    private RequestManager requestManager;
    private int nowPosition = 0;

    public BottomNavigationBar(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_navigation_bar, this);
        ButterKnife.bind(this);
        requestManager = Glide.with(context);
        changeResWithIdtentify();

        changeIdtentifyObservable = RxBus.get().register(BOTTOM_CHANGE_IDTENTIFY, Boolean.class);
        changeIdtentifyObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                changeResWithIdtentify();
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        bottomls = new ArrayList<>();
        bottomls.add(tv_bottom_nav_left_first);
        bottomls.add(tv_bottom_nav_left_second);
        bottomls.add(tv_bottom_nav_center);
        bottomls.add(tv_bottom_nav_right_first);
        bottomls.add(tv_bottom_nav_right_second);
    }

    public void setActivity() {
        doubleClickDetect(doubleClickTime, fl_bottom_nav_left_first);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_left_second);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_center);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_right_first);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_right_second);
    }

    public void setOnClickListener(BottomClickListener bottomClickListener) {
        this.bottomClickListener = bottomClickListener;
    }

    public interface BottomClickListener {
        void onTabSelected(int position, int code);
    }

    public void changeResWithIdtentify() {
        int leftFirstRes = 0, leftSecRes = 0, centerRes = 0, rightFirstRes = 0, rightSecRes = 0;
        int leftFirstStr, leftSecStr, centerStr, rightFirstStr, rightSecStr;

        boolean isAutumn = SkineColorManager.isautumnHoliay();
        isAutumn=true;
        if (isAutumn) {
            leftFirstRes = R.drawable.autumn_one;
            leftSecRes = R.drawable.autumn_two;
            centerRes = R.drawable.autumn_three;
            rightFirstRes = R.drawable.autumn_four;
            rightSecRes = R.drawable.autumn_five;
        } else {
            leftFirstRes = nowPosition == 0 ? R.drawable.icon_home_press : R.drawable.icon_home;
            leftSecRes = nowPosition == 1 ? R.drawable.icon_wealth_press : R.drawable.icon_wealth;
            centerRes = nowPosition == 2 ? R.drawable.icon_life_press : R.drawable.icon_life;
            rightFirstRes = nowPosition == 3 ? R.drawable.icon_health_press : R.drawable.icon_health;
            rightSecRes = nowPosition == 4 ? R.drawable.icon_my_press : R.drawable.icon_my;
        }
        leftFirstStr = R.string.vbnb_firsts_str;
        leftSecStr = R.string.vbnb_private_str;
        centerStr = R.string.vbnb_happy_live_str;
        rightFirstStr = R.string.vbnb_ever_ok_str;
        rightSecStr = R.string.vbnb_mine_str;

        requestManager.load(leftFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftFirstRes).into(iv_bottom_nav_left_first);
        requestManager.load(leftSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftSecRes).into(iv_bottom_nav_left_second);
        requestManager.load(centerRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(centerRes).into(ivBottomNavCenter);
        requestManager.load(rightFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightFirstRes).into(iv_bottom_nav_right_first);
        requestManager.load(rightSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightSecRes).into(iv_bottom_nav_right_second);

//        tv_bottom_nav_left_first.setText(leftFirstStr);
//        tv_bottom_nav_left_second.setText(leftSecStr);
//        tv_bottom_nav_center.setText(centerStr);
//        tv_bottom_nav_right_first.setText(rightFirstStr);
//        tv_bottom_nav_right_second.setText(rightSecStr);

        setBottonTxt(tv_bottom_nav_left_first, leftFirstStr, nowPosition == 0);
        setBottonTxt(tv_bottom_nav_left_second, leftSecStr, nowPosition == 1);
        setBottonTxt(tv_bottom_nav_center, centerStr, nowPosition == 2);
        setBottonTxt(tv_bottom_nav_right_first, rightFirstStr, nowPosition == 3);
        setBottonTxt(tv_bottom_nav_right_second, rightSecStr, nowPosition == 4);


    }

    public void setBottonTxt(TextView textView, int textId, boolean isselect) {
        textView.setText(getResources().getString(textId));
        textView.setTextColor(getResources().getColor(isselect ? R.color.app_golden : R.color.black));
    }

    public void selectNavaigationPostion(int index) {
        nowPosition = index;
        changeResWithIdtentify();
    }

    //双击后发送消息，单击后跳转页面
    private void doubleClickDetect(int time, View view) {
        Observable<Void> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(time, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<Void>>() {
                    @Override
                    protected void onEvent(List<Void> voids) {
                        if (voids.size() > 2) {
                            //在当前页面双击刷新该页面
                            switch (view.getId()) {
                                case R.id.fl_bottom_nav_left_first:
                                    RxBus.get().post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_FIRST, true);
                                    break;
                                case R.id.fl_bottom_nav_left_second:
                                    RxBus.get().post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_SEC, true);
                                    break;
                                case R.id.fl_bottom_nav_right_first:
                                    RxBus.get().post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_FIRST, true);
                                    break;
                                case R.id.fl_bottom_nav_right_second:
                                    RxBus.get().post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_SEC, true);
                                    break;
                                case R.id.fl_bottom_nav_center:
                                    break;
                            }
                        } else {
                            switch (view.getId()) {
                                case R.id.fl_bottom_nav_left_first:
                                    if (nowPosition != 0) {
                                        nowPosition = 0;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(0, 0);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_left_second:
                                    if (nowPosition != 1) {
                                        nowPosition = 1;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(1, 0);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_center:
                                    if (nowPosition != 2) {
                                        nowPosition = 2;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(2, 0);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_right_first:
                                    if (nowPosition != 3) {
                                        nowPosition = 3;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(3, 0);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_right_second:
                                    if (nowPosition != 4) {
                                        nowPosition = 4;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(4, 0);
                                    }
                                    break;
                            }
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {
                    }
                });
    }
}
