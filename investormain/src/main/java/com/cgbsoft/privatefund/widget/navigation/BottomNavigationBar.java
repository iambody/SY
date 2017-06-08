package com.cgbsoft.privatefund.widget.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.FestivalUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.FloatingActionMenu;
import com.cgbsoft.privatefund.widget.CloudMenuActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 底部导航栏
 * Created by xiaoyu.zhang on 2016/11/15 17:41
 * Email:zhangxyfs@126.com
 *  
 */
public class BottomNavigationBar extends FrameLayout implements RxConstant {
    private FloatingActionMenu floatingActionMenu;

    private BottomClickListener bottomClickListener;

    //左面第一个按钮
    @BindView(R.id.fl_bottom_nav_left_first)
    FrameLayout fl_bottom_nav_left_first;
    //左面第二个按钮
    @BindView(R.id.fl_bottom_nav_left_second)
    FrameLayout fl_bottom_nav_left_second;
    //右面第一个按钮
    @BindView(R.id.fl_bottom_nav_right_first)
    FrameLayout fl_bottom_nav_right_first;
    //右面第二个按钮
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
    @BindView(R.id.tv_bottom_nav_left_second)
    TextView tv_bottom_nav_left_second;
    @BindView(R.id.tv_bottom_nav_right_first)
    TextView tv_bottom_nav_right_first;
    @BindView(R.id.tv_bottom_nav_right_second)
    TextView tv_bottom_nav_right_second;

    private List<TextView> bottomls;
    //    //关闭背景
    @BindView(R.id.view_bottom_navigation_close)
    View view_bottom_navigation_close;
    //中间的按钮
    @BindView(R.id.iv_bottom_navigation_cloud)
    ImageView iv_bottom_navigation_cloud;
    //消息文本
    @BindView(R.id.tv_bottom_nav_msgnum)
    TextView tv_bottom_nav_msgnum;

    private Observable<Boolean> changeIdtentifyObservable;
    private RequestManager requestManager;
    private boolean isIdtentifyWithInvestor;

    private boolean isLookZhiBao;
    private boolean isLive;

    private int nowPosition = 0, doubleClickTime = 200;
    private long nowSystemTime;
    private boolean isSpringFestival;//是否春节

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
        isSpringFestival = checkSpringFestival();

        view_bottom_navigation_close.setVisibility(GONE);
        isIdtentifyWithInvestor = AppManager.isInvestor(context);
        changeResWithIdtentify();

        changeIdtentifyObservable = RxBus.get().register(BOTTOM_CHANGE_IDTENTIFY, Boolean.class);
        changeIdtentifyObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                isIdtentifyWithInvestor = aBoolean;
                changeResWithIdtentify();
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        bottomls = new ArrayList<>();
        bottomls.add(tv_bottom_nav_left_first);
        bottomls.add(tv_bottom_nav_left_second);
        bottomls.add(tv_bottom_nav_right_first);
        bottomls.add(tv_bottom_nav_right_second);
    }

    public void setOnClickListener(BottomClickListener bottomClickListener) {
        this.bottomClickListener = bottomClickListener;
    }

    public void setActivity(Activity activity) {
        //如果是投资者
        View callView = buildSubButton(activity, getResources().getString(R.string.vbnb_call_str), R.drawable.selector_bottom_call);
        View meetView = buildSubButton(activity, getResources().getString(R.string.vbnb_meet_str), R.drawable.selector_bottom_meet);
        View liveView = buildSubButton(activity, getResources().getString(R.string.vbnb_live_str), R.drawable.selector_bottom_live);
        View smsView = buildSubButton(activity, getResources().getString(R.string.vbnb_sms_str), R.drawable.selector_bottom_sms);
        View csView = buildSubButton(activity, getResources().getString(R.string.vbnb_cs_str), R.drawable.selector_bottom_cs);

        floatingActionMenu = new FloatingActionMenu.Builder(activity).addSubActionView(callView)
                .addSubActionView(meetView)
                .addSubActionView(liveView)
                .addSubActionView(smsView)
                .addSubActionView(csView)
                .attachTo(iv_bottom_navigation_cloud)
                .setStartAngle(200)//起始动画位置
                .setEndAngle(340)
                .setRadius(400)
                .build();
//
        view_bottom_navigation_close.setOnClickListener(v -> {
            long toTime = System.currentTimeMillis() - nowSystemTime;
            if (toTime > doubleClickTime * 4 && floatingActionMenu.isOpen()) {
                floatingActionMenu.close(true);
                view_bottom_navigation_close.setVisibility(GONE);
                iv_bottom_navigation_cloud.setImageResource(R.drawable.ic_bottom_cloud_investor);
            }
//                NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_CLOUD_MENU_ACTIVITY);
        });

        callView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onCloudMenuClick(0);
        });
        meetView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onCloudMenuClick(1);
        });
        liveView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onCloudMenuClick(2);
        });
        smsView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onCloudMenuClick(3);
        });
        csView.setOnClickListener(v -> {
            if (bottomClickListener != null)
                bottomClickListener.onCloudMenuClick(4);
        });

        doubleClickDetect(doubleClickTime, fl_bottom_nav_left_first);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_left_second);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_right_first);
        doubleClickDetect(doubleClickTime, fl_bottom_nav_right_second);
        if (AppManager.isInvestor(getContext())) {
            doubleClickDetect(doubleClickTime * 4, iv_bottom_navigation_cloud);
        } else {
            doubleClickDetect(doubleClickTime, iv_bottom_navigation_cloud);
        }
    }


    private View buildSubButton(Activity activity, String str, int drawableId) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_text, null);
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
        textView.setText(str);

        FrameLayout.LayoutParams frameLP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(activity, android.R.color.transparent));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        itemBuilder.setLayoutParams(params);
        return itemBuilder.setContentView(textView, frameLP).build();
    }

    public interface BottomClickListener {
        void onCloudMenuClick(int position);

        void onTabSelected(int position);
    }

    public void changeResWithIdtentify() {
        int centerRes = 0, leftFirstRes = 0, leftSecRes = 0, rightFirstRes = 0, rightSecRes = 0;
        int leftFirstStr, leftSecStr, rightFirstStr, rightSecStr;
        if (isIdtentifyWithInvestor) {
            if (!isSpringFestival) {
                centerRes = R.drawable.ic_bottom_cloud_investor;
                leftFirstRes = nowPosition == 0 ? R.drawable.ic_bottom_select_mine_down : R.drawable.ic_bottom_select_mine_up;
                leftSecRes = nowPosition == 1 ? R.drawable.ic_bottom_select_investor_product_down : R.drawable.ic_bottom_select_investor_product_up;
                rightFirstRes = nowPosition == 2 ? R.drawable.ic_bottom_select_inverstor_discovery_down : R.drawable.ic_bottom_select_inverstor_discovery_up;
                rightSecRes = nowPosition == 3 ? R.drawable.ic_bottom_club_down : R.drawable.ic_bottom_club_up;
            } else {
                //todo 春节时候的样式
            }

            leftFirstStr = R.string.vbnb_mine_str;
            leftSecStr = R.string.vbnb_product_str;
            rightFirstStr = R.string.vbnb_discovery_str;
            rightSecStr = R.string.vbnb_club_str;

            if (tv_bottom_nav_msgnum.getVisibility() == VISIBLE)
                tv_bottom_nav_msgnum.setVisibility(GONE);
        } else {
            if (!isSpringFestival) {
                centerRes = R.drawable.ic_bottom_cloud_adviser;
                leftFirstRes = nowPosition == 0 ? R.drawable.ic_bottom_select_msg_down : R.drawable.ic_bottom_select_msg_up;
                leftSecRes = nowPosition == 1 ? R.drawable.ic_bottom_select_adviser_product_down : R.drawable.ic_bottom_select_adviser_product_up;
                rightFirstRes = nowPosition == 2 ? R.drawable.ic_bottom_select_adviser_discovery_down : R.drawable.ic_bottom_select_adviser_discovery_up;
                rightSecRes = nowPosition == 3 ? R.drawable.ic_bottom_select_college_down : R.drawable.ic_bottom_select_college_up;
            } else {
                //todo  春节时候的样式
            }

            leftFirstStr = R.string.vbnb_msg_str;
            leftSecStr = R.string.vbnb_product_str;
            rightFirstStr = R.string.vbnb_discovery_str;
            rightSecStr = R.string.vbnb_college_str;

            if (tv_bottom_nav_msgnum.getVisibility() == GONE) {
                tv_bottom_nav_msgnum.setVisibility(VISIBLE);
                tv_bottom_nav_msgnum.setText("123");
            }
        }
        requestManager.load(centerRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(centerRes).into(iv_bottom_navigation_cloud);
        requestManager.load(leftFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftFirstRes).into(iv_bottom_nav_left_first);
        requestManager.load(leftSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftSecRes).into(iv_bottom_nav_left_second);
        requestManager.load(rightFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightFirstRes).into(iv_bottom_nav_right_first);
        requestManager.load(rightSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightSecRes).into(iv_bottom_nav_right_second);

        tv_bottom_nav_left_first.setText(leftFirstStr);
        tv_bottom_nav_left_second.setText(leftSecStr);
        tv_bottom_nav_right_first.setText(rightFirstStr);
        tv_bottom_nav_right_second.setText(rightSecStr);
    }

    public void selectNavaigationPostion(int index) {
//        int leftFirstRes = 0, leftSecRes = 0, rightFirstRes = 0, rightSecRes = 0;
//        leftFirstRes = index == 0 ? R.drawable.ic_bottom_select_mine_down : R.drawable.ic_bottom_select_mine_up;
//        leftSecRes = index == 1 ? R.drawable.ic_bottom_select_investor_product_down : R.drawable.ic_bottom_select_investor_product_up;
//        rightFirstRes = index == 2 ? R.drawable.ic_bottom_select_inverstor_discovery_down : R.drawable.ic_bottom_select_inverstor_discovery_up;
//        rightSecRes = index == 3 ? R.drawable.ic_bottom_club_down : R.drawable.ic_bottom_club_up;
//
//        requestManager.load(leftFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftFirstRes).into(iv_bottom_nav_left_first);
//        requestManager.load(leftSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(leftSecRes).into(iv_bottom_nav_left_second);
//        requestManager.load(rightFirstRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightFirstRes).into(iv_bottom_nav_right_first);
//        requestManager.load(rightSecRes).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(rightSecRes).into(iv_bottom_nav_right_second);
//        for (int i = 0; i < bottomls.size(); i++) {
//            bottomls.get(i).setSelected(index == i );
//        }

        nowPosition=index;
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
                                case R.id.iv_bottom_navigation_cloud:
                                    break;
                            }
                        } else {
                            switch (view.getId()) {
                                case R.id.fl_bottom_nav_left_first:
                                    if (nowPosition != 0) {
                                        nowPosition = 0;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(0);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_left_second:
                                    if (nowPosition != 1) {
                                        nowPosition = 1;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(1);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_right_first:
                                    if (nowPosition != 2) {
                                        nowPosition = 2;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(2);
                                    }
                                    break;
                                case R.id.fl_bottom_nav_right_second:
                                    if (nowPosition != 3) {
                                        nowPosition = 3;
                                        changeResWithIdtentify();
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(3);
                                    }
                                    break;
                                case R.id.iv_bottom_navigation_cloud:
                                    if (nowPosition != 4) {
                                        nowPosition = 4;
                                        changeResWithIdtentify();
                                    }
                                    if (isIdtentifyWithInvestor) {
//                                        if (SPreference.getToCBean(getContext()) != null && TextUtils.isEmpty(SPreference.getToCBean(getContext()).getBandingAdviserId())) {
//                                            HashMap<String, Object> hashMap = new HashMap<>();
//                                            hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.noBindUserInfo);
//                                            hashMap.put(WebViewConstant.push_message_title, "填写信息");
//                                            NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
//                                        } else {
//                                            getContext().startActivity(new Intent(getContext(), CloudMenuActivity.class));
//                                        }
                                        if (AppManager.getUserInfo(getContext()).getToC() != null && TextUtils.isEmpty(AppManager.getUserInfo(getContext()).getToC().getBandingAdviserId())) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.noBindUserInfo);
                                            hashMap.put(WebViewConstant.push_message_title, "填写信息");
                                            NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
                                        } else {
                                            if (isLive && !isLookZhiBao) {
                                                isLookZhiBao = true;
                                                //joinLive();
                                            } else {
                                                boolean needOpen;
                                                if (floatingActionMenu.isOpen()) {
                                                    needOpen = false;
                                                    view_bottom_navigation_close.setVisibility(GONE);
                                                    iv_bottom_navigation_cloud.setImageResource(R.drawable.ic_bottom_cloud_investor);
                                                } else {
                                                    needOpen = true;
                                                    nowSystemTime = System.currentTimeMillis();
                                                    view_bottom_navigation_close.setVisibility(VISIBLE);
                                                    iv_bottom_navigation_cloud.setImageResource(R.drawable.ic_bottom_close);
                                                }
                                                floatingActionMenu.toggle(needOpen);
                                            }
                                        }

                                    } else {
                                        if (bottomClickListener != null)
                                            bottomClickListener.onTabSelected(4);
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

    public void closeCloudeMenu() {
        view_bottom_navigation_close.setVisibility(GONE);
        floatingActionMenu.toggle(false);
        iv_bottom_navigation_cloud.setImageResource(R.drawable.ic_bottom_cloud_investor);
    }

    /**
     * 判断是否为春节，需要产品定制什么范围为春节前后
     *
     * @return
     */
    private boolean checkSpringFestival() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        FestivalUtils festivalUtils = new FestivalUtils(year, month, day);
        ArrayList<String> list = festivalUtils.getFestVals();
        for (String s : list) {
            if (TextUtils.equals(s, "春节")) {
                return true;
            }
        }
        return false;
    }

}
