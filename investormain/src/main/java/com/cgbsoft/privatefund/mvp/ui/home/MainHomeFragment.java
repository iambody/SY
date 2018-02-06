package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.SkineColorManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.SpannableUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.lib.widget.MyGridView;
import com.cgbsoft.lib.widget.MySwipeRefreshLayout;
import com.cgbsoft.lib.widget.SmartScrollView;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.OperationAdapter;
import com.cgbsoft.privatefund.bean.LiveInfBean;
import com.cgbsoft.privatefund.mvc.ui.MembersAreaActivity;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.FloatStewardView;
import com.chenenyu.router.Router;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.vido.VideoNavigationUtils;
import app.product.com.utils.ProductNavigationUtils;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import qcloud.liveold.mvp.views.LiveActivity;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View, SwipeRefreshLayout.OnRefreshListener, SmartScrollView.ISmartScrollChangedListener, View.OnClickListener, SensorEventListener {
    public static final String LIVERXOBSERBER_TAG = "rxobserlivetag";
    public final int ADVISERSHOWTIME = 5;
    public final int ADVISERLOADTIME = 3;
    @BindView(R.id.mainhome_webview)
    BaseWebview mainhomeWebview;
    @BindView(R.id.main_home_new_iv)
    ImageView mainHomeNewIv;
    @BindView(R.id.home_bannerview)
    BannerView homeBannerview;
    @BindView(R.id.home_floatstewardview)
    FloatStewardView home_floatstewardview;
    @BindView(R.id.view_home_level_str)
    TextView viewHomeLevelStr;
    @BindView(R.id.main_home_swiperefreshlayout)
    MySwipeRefreshLayout mainHomeSwiperefreshlayout;
    @BindView(R.id.main_home_smartscrollview)
    SmartScrollView mainHomeSmartscrollview;
    @BindView(R.id.festival_style)
    LinearLayout festival_style;
    View home_product_view;
    View main_home_level_lay;
    View main_home_newlive_lay;
    //新的直播
    ImageView view_newlive_iv_bg;
    TextView view_newlive_content, view_newlive_tag;
    RelativeLayout home_newlive_foreshow_lay, home_newlive_now_lay;
    TextView view_newlive_title_tag, view_newlive_number;
    ImageView view_home_level_bg, view_newlive_meng_bg;


    TextView home_product_title;
    TextView home_peoduct_subtitle;

    ImageView view_home_product_bg;

    TextView view_home_product_tag;
    TextView view_home_product_name;
    TextView view_home_product_des;

    LinearLayout home_product_skip_bank, view_home_product_focus, home_product_down_lay;
    TextView home_product_frist_up, home_product_frist_down;
    TextView home_product_second_up, home_product_second_down;
    TextView home_product_three_up, home_product_three_down;

    TextView view_home_member;
    ImageView view_home_level_arrow;
    MyGridView main_home_gvw;
    OperationAdapter operationAdapter;
    private UnreadInfoNumber unreadInfoNumber;
    private Observable<LiveInfBean> liveObservable;
    private Observable<Integer> userLayObservable, bindAdviserObservable;


    private HomeEntity.Result homeData;
    private LiveInfBean homeliveInfBean;

    private int downXPostion;
    private int lastXPostion;
    private boolean isLoading;
    private boolean bannerIsLeft;
    private boolean bannerIsRight;
    private boolean isRolling;
    protected boolean isVisible;
    private ImageView[] imageViews;
    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;

    private static final float NS2S = 1.0f / 10000000.0f;
    private float timestamp;
    private float[] angle = {0f, 0f, 0f};


    @Override
    protected int layoutID() {
        return R.layout.fragment_mainhome;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initConfig();
        initshowlay();
        timeCountDown();
        initCache();
        getPresenter().getHomeData();
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), mainHomeNewIv, false);
        DataStatistApiParam.gohome();

    }

    @Override
    protected void viewBeShow() {
        super.viewBeShow();
    }

    @Override
    protected void viewBeHide() {
        super.viewBeHide();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.initUnreadInfoAndPosition();
        }
        try {
            homeBannerview.startBanner();
        } catch (Exception e) {

        }
    }

    private void initSensor() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        imageViews[0].setPivotY(imageViews[0].getHeight() / 22);
        imageViews[1].setPivotY(imageViews[1].getHeight() / 22);
        imageViews[2].setPivotY(imageViews[2].getHeight() / 22);
        imageViews[3].setPivotY(imageViews[3].getHeight() / 22);
    }


    @Override
    public void onHiddenChanged(boolean isVisibleToUser) {
        super.onHiddenChanged(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            homeBannerview.endBanner();
        } else {
            isVisible = false;
            LogUtils.Log("onHiddenChanged", "isVisible");
            homeBannerview.startBanner();
        }
    }

    /**
     * 游客模式游客布局显示 费游客模式非游客布局显示
     */
    private void initshowlay() {
        home_floatstewardview.initFloat(AppManager.isVisitor(baseActivity) || !AppManager.isBindAdviser(baseActivity), new FloatStewardView.FloatStewardListener() {
            @Override
            public void record() {
                if (AppManager.isBindAdviser(baseActivity)) {
                    VideoNavigationUtils.startInfomationDetailActivity(baseActivity, CwebNetConfig.BindchiceAdiser, getResources().getString(R.string.my_adviser), 200);
                } else {
                    VideoNavigationUtils.startInfomationDetailActivity(baseActivity, CwebNetConfig.choiceAdviser, getResources().getString(R.string.select_adviser), 200);
                }
                TrackingDataManger.homeDangAn(baseActivity);
            }

            @Override
            public void visitorTxtClick() {//游客模式下点击跳转理财师
                if (AppManager.isVisitor(baseActivity)) {
                    VideoNavigationUtils.startInfomationDetailActivity(baseActivity, CwebNetConfig.choiceAdviser, getResources().getString(R.string.select_adviser), 200);

                } else if (AppManager.isBindAdviser(baseActivity)) {
                    VideoNavigationUtils.startInfomationDetailActivity(baseActivity, CwebNetConfig.BindchiceAdiser, getResources().getString(R.string.my_adviser), 200);
                } else {
                    VideoNavigationUtils.startInfomationDetailActivity(baseActivity, CwebNetConfig.choiceAdviser, getResources().getString(R.string.select_adviser), 200);
                }
                TrackingDataManger.homeGreetings(baseActivity);
            }

            @Override
            public void phoneClick() {
                if (needPermissions(Constant.PERMISSION_CALL_PHONE)) {
                    PromptManager.ShowCustomToast(baseActivity, "请到设置允许拨打电话权限");
                    return;
                }
                getPresenter().gotoConnectAdviser();
                DataStatistApiParam.homeClickDuiHua();
                TrackingDataManger.homePhone(baseActivity);
            }

            @Override
            public void noteClick() {
                Utils.sendSmsWithNumber(baseActivity, AppManager.getUserInfo(baseActivity).adviserPhone);
                DataStatistApiParam.homeClickNote();
                TrackingDataManger.homeNote(baseActivity);

            }

            @Override
            public void imClick() {
                RongIM.getInstance().startConversation(baseActivity, Conversation.ConversationType.PRIVATE, AppManager.getUserInfo(baseActivity).toC.bandingAdviserId,
                        getString(R.string.private_bank_personal).concat(AppManager.getUserInfo(baseActivity).adviserRealName));
                TrackingDataManger.homedialogue(baseActivity);

            }
        });
        home_floatstewardview.openFloat();
    }


    /**
     * 判断缓存
     */
    private void initCache() {
        HomeEntity.Result data = AppManager.getHomeCache(baseActivity);
        if (null != data)
            initResultData(data);
    }

    /**
     * 点击消息
     */
    @OnClick(R.id.main_home_new_iv)
    public void onNewClicked() {

        if (AppManager.isVisitor(baseActivity)) {
            Intent intent = new Intent(baseActivity, LoginActivity.class);
            intent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
            UiSkipUtils.toNextActivityWithIntent(baseActivity, intent);
        } else {
            UiSkipUtils.toNextActivityWithIntent(baseActivity, new Intent(baseActivity, MessageListActivity.class));
        }
        DataStatistApiParam.homeClickNew();
        TrackingDataManger.homeNew(baseActivity);

    }

    /**
     * 配置view各种资源
     */
    private void initConfig() {
        homeBannerview.setChangeViewCallback(new BannerView.ChangeViewCallback() {
            @Override
            public void changeView(boolean left, boolean right) {
                bannerIsLeft = left;
                bannerIsRight = right;
                isRolling = true;
                if (bannerIsLeft) {
                }
                if (bannerIsRight) {
                }
            }

            @Override
            public void getCurrentPageIndex(int index) {
                if (null == homeData) return;
                try {
                    if (bannerIsLeft && isRolling) {
                        TrackingDataManger.homeBannerleft(baseActivity, homeData.banner.get(index).title);
                    }
                    if (bannerIsRight && isRolling) {
                        TrackingDataManger.homeBannerRight(baseActivity, homeData.banner.get(index).title);
                    }
                } catch (Exception e) {
                } finally {
                    isRolling = false;
                }
            }
        });

        RelativeLayout.LayoutParams bannerParames = new RelativeLayout.LayoutParams(screenWidth, (int) ((screenWidth * 120) / 188));
        homeBannerview.setLayoutParams(bannerParames);

        //新直播
        main_home_newlive_lay = mFragmentView.findViewById(R.id.main_home_newlive_lay);
        view_newlive_iv_bg = ViewHolders.get(mFragmentView, R.id.view_newlive_iv_bg);
        view_newlive_content = ViewHolders.get(mFragmentView, R.id.view_newlive_content);
        view_newlive_tag = ViewHolders.get(mFragmentView, R.id.view_newlive_tag);
        home_newlive_foreshow_lay = ViewHolders.get(mFragmentView, R.id.home_newlive_foreshow_lay);
        home_newlive_now_lay = ViewHolders.get(mFragmentView, R.id.home_newlive_now_lay);
        view_newlive_title_tag = ViewHolders.get(mFragmentView, R.id.view_newlive_title_tag);
        view_newlive_number = ViewHolders.get(mFragmentView, R.id.view_newlive_number);
        main_home_newlive_lay.setOnClickListener(this);
        home_product_view = ViewHolders.get(mFragmentView, R.id.home_product_view);
        view_home_level_arrow = ViewHolders.get(mFragmentView, R.id.view_home_level_arrow);
        view_newlive_meng_bg = ViewHolders.get(mFragmentView, R.id.view_newlive_meng_bg);
        //标题和内容
        /* 直播*/
        main_home_level_lay = mFragmentView.findViewById(R.id.main_home_level_lay);
        main_home_level_lay.setOnClickListener(this);
        //等级
        view_home_level_bg = ViewHolders.get(mFragmentView, R.id.view_home_level_bg);
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
        view_home_member = ViewHolders.get(mFragmentView, R.id.view_home_member);
        main_home_gvw = (MyGridView) mFragmentView.findViewById(R.id.main_home_gvw);
        operationAdapter = new OperationAdapter(baseActivity, R.layout.item_operation);
        main_home_gvw.setAdapter(operationAdapter);

        initProduct(mFragmentView);
        //游客模式下或者没有绑定过理财师需要
        initRxEvent();
        main_home_gvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeEntity.Operate data = (HomeEntity.Operate) parent.getItemAtPosition(position);
                setGVWClick(data);
            }
        });


        mainhomeWebview.loadUrls(CwebNetConfig.HOME_URL);
    }

    private void setGVWClick(HomeEntity.Operate data) {
        if ("0".equals(data.isVisitorVisible) && AppManager.isVisitor(baseActivity)) {//需要跳转到登录页面
            Intent toHomeIntent = new Intent(baseActivity, LoginActivity.class);
            toHomeIntent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
            UiSkipUtils.toNextActivityWithIntent(baseActivity, toHomeIntent);
            return;
        }
        if ("h5".equals(data.jumpType)) {//跳转h5
            if ("1004".equals(data.jumpId)) {// 云豆乐园 需要显示充值按钮
                NavigationUtils.gotoWebActivityWithPay(baseActivity, data.url, data.title);
            } else {
                NavigationUtils.gotoWebActivity(baseActivity, data.url, data.title, false);
            }

        } else if ("app".equals(data.jumpType)) {
            NavigationUtils.jumpNativePage(baseActivity, Integer.decode(data.jumpId));
            if (null != Integer.decode(data.jumpId) && Integer.decode(data.jumpId) == WebViewConstant.Navigation.TASK_PAGE)
                TrackingDataManger.homeTask(baseActivity);
        }
        DataStatistApiParam.operateBannerClick(null == data || BStrUtils.isEmpty(data.title) ? "" : data.title);
        TrackingDataManger.homeOperateItemClick(baseActivity, data.title);
    }


    private void initProduct(View mFragmentView) {
        view_home_product_focus = ViewHolders.get(mFragmentView, R.id.view_home_product_focus);
        home_product_skip_bank = ViewHolders.get(mFragmentView, R.id.home_product_skip_bank);
        home_product_title = ViewHolders.get(mFragmentView, R.id.home_product_title);
        home_peoduct_subtitle = ViewHolders.get(mFragmentView, R.id.home_peoduct_subtitle);
        view_home_product_bg = ViewHolders.get(mFragmentView, R.id.view_home_product_bg);
        view_home_product_tag = ViewHolders.get(mFragmentView, R.id.view_home_product_tag);

        view_home_product_name = ViewHolders.get(mFragmentView, R.id.view_home_product_name);
        view_home_product_des = ViewHolders.get(mFragmentView, R.id.view_home_product_des);

        home_product_down_lay = ViewHolders.get(mFragmentView, R.id.home_product_down_lay);

        home_product_frist_up = ViewHolders.get(mFragmentView, R.id.home_product_frist_up);
        home_product_frist_down = ViewHolders.get(mFragmentView, R.id.home_product_frist_down);

        home_product_second_up = ViewHolders.get(mFragmentView, R.id.home_product_second_up);
        home_product_second_down = ViewHolders.get(mFragmentView, R.id.home_product_second_down);

        home_product_three_up = ViewHolders.get(mFragmentView, R.id.home_product_three_up);
        home_product_three_down = ViewHolders.get(mFragmentView, R.id.home_product_three_down);


        view_home_product_focus.setOnClickListener(this);
        home_product_skip_bank.setOnClickListener(this);
    }


    /**
     * 注册监听事件
     */
    private void initRxEvent() {
        /**绑定理财师*/
        bindAdviserObservable = RxBus.get().register(RxConstant.BindAdviser, Integer.class);
        bindAdviserObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                getPresenter().getUserInf(1);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        /** 游客登录进入正常模式*/
        userLayObservable = RxBus.get().register(RxConstant.MAIN_FRESH_LAY, Integer.class);
        userLayObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                if (5 == integer) {//需要刷新动作
                    mainHomeSwiperefreshlayout.setRefreshing(true);
                    RxCountDown.countdown(ADVISERLOADTIME).doOnSubscribe(new Action0() {
                        @Override
                        public void call() {

                        }
                    }).subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            mainHomeSwiperefreshlayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mainHomeSwiperefreshlayout.setRefreshing(false);
                        }

                        @Override
                        public void onNext(Integer integer) {

                        }
                    });
                }
                home_floatstewardview.closeFloat();
                home_floatstewardview.refreshData(AppManager.isVisitor(baseActivity) || !AppManager.isBindAdviser(baseActivity));
                mainhomeWebview.loadUrl("javascript:refresh()");
                mainhomeWebview.reload();
                initshowlay();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        /**直播状态监听*/
        liveObservable = RxBus.get().register(LIVERXOBSERBER_TAG, LiveInfBean.class);
        liveObservable.subscribe(new RxSubscriber<LiveInfBean>() {
            @Override
            protected void onEvent(LiveInfBean liveInfBean) {

                homeliveInfBean = liveInfBean;
                switch (liveInfBean.type) {

                    case 0://预告
//                        //新版直播
                        main_home_newlive_lay.setVisibility(View.VISIBLE);
                        home_newlive_foreshow_lay.setVisibility(View.VISIBLE);
                        view_newlive_meng_bg.setVisibility(View.VISIBLE);
                        home_newlive_now_lay.setVisibility(View.GONE);
                        Imageload.display(baseActivity, liveInfBean.image, view_newlive_iv_bg);
                        BStrUtils.setTv(view_newlive_title_tag, liveInfBean.create_time + "开播");
                        view_newlive_tag.setText("直播预告");
                        BStrUtils.setTv(view_newlive_content, liveInfBean.title);
                        break;
                    case 1://直播中
                        //新版直播
                        main_home_newlive_lay.setVisibility(View.VISIBLE);
                        home_newlive_foreshow_lay.setVisibility(View.GONE);
                        home_newlive_now_lay.setVisibility(View.VISIBLE);
                        view_newlive_meng_bg.setVisibility(View.GONE);
                        Imageload.display(baseActivity, liveInfBean.image, view_newlive_iv_bg);
                        view_newlive_tag.setText("正在直播");
                        BStrUtils.setTv(view_newlive_content, liveInfBean.title);
                        BStrUtils.setTv(view_newlive_number, BStrUtils.NullToStr(liveInfBean.visitors) + "人正在观看");

                        break;
                    case 2://无直播

                        main_home_newlive_lay.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != liveObservable) {
            RxBus.get().unregister(LIVERXOBSERBER_TAG, liveObservable);
        }
        if (null != userLayObservable) {
            RxBus.get().unregister(RxConstant.MAIN_FRESH_LAY, userLayObservable);
        }
        if (unreadInfoNumber != null) {
            unreadInfoNumber.onDestroy();
        }
        if (null != bindAdviserObservable) {
            RxBus.get().unregister(RxConstant.BindAdviser, bindAdviserObservable);
        }
        if (null != sensorManager) {
            sensorManager.unregisterListener(this);
        }
    }


    /**
     * 初始化banner
     */
    private void initViewPage(List<HomeEntity.Banner> banner) {
        initBanner(valuelist(banner));
    }

    private List<BannerBean> valuelist(List<HomeEntity.Banner> banner) {
        List<BannerBean> bannerBeen = new ArrayList<>();
        if (null == banner || banner.size() == 0) return bannerBeen;

        for (HomeEntity.Banner h : banner) {
            BannerBean b = new BannerBean();
            b.setImageUrl(h.imageUrl);
            b.setJumpUrl(h.url);
            b.setTitle(h.title);
            bannerBeen.add(b);
        }
        return bannerBeen;
    }

    private void initBanner(List<BannerBean> valuelist) {
        homeBannerview.initShowImageForNet(getActivity(), valuelist);
        homeBannerview.setOnclickBannerItemView(bannerBean -> {
            NavigationUtils.gotoRightShareWebActivity(baseActivity, bannerBean.getJumpUrl(), bannerBean.getTitle());
            DataStatistApiParam.HomeBannerClick(bannerBean.getTitle());
            TrackingDataManger.homeBannerFocus(baseActivity, bannerBean.getTitle());
        });
        if (homeBannerview != null) {
            homeBannerview.startBanner();
        }
    }

    @Override
    protected MainHomePresenter createPresenter() {
        return new MainHomePresenter(baseActivity, this);
    }

    @Override
    public void getResultSucc(HomeEntity.Result data) {
        if (mainHomeSwiperefreshlayout.isRefreshing()) {
            mainHomeSwiperefreshlayout.setRefreshing(false);
        }
        if (null != data) {
            initResultData(data);
            //设置&&刷新缓存数据
            AppInfStore.saveHomeData(baseActivity, data);
        }
        boolean isAutumn = SkineColorManager.isautumnHoliay();
        if (isAutumn) {
            initSensor();
        }

    }

    /**
     * 获取数据进行数据填充
     */
    private void initResultData(HomeEntity.Result data) {
        homeData = data;
        //banner
        initViewPage(data.banner);
        initOperation(data.module);
        //用户等级信息
        initLevel(data.myInfo);
        initProduct(data.bank);

    }

    private void initProduct(HomeEntity.bankInf bank) {
        if (null == bank) {
            home_product_view.setVisibility(View.GONE);
            return;
        }
        BStrUtils.setTv(home_product_title, bank.title);
        BStrUtils.setTv(home_peoduct_subtitle, bank.subtitle);
        home_product_view.setVisibility(View.VISIBLE);
        if (null == bank.product || null == bank.product.content) {
            baseActivity.findViewById(R.id.home_product_view).setVisibility(View.GONE);
            return;
        }
        Imageload.display(baseActivity, bank.product.content.marketingImageUrl, view_home_product_bg);

        BStrUtils.setTv1(view_home_product_name, bank.product.content.productName);
        BStrUtils.setTv1(view_home_product_des, bank.product.content.hotName);
        if (AppManager.isVisitor(baseActivity) || TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {
            home_product_down_lay.setVisibility(View.GONE);
        } else {
            home_product_down_lay.setVisibility(View.VISIBLE);
        }

        //第三个*******************************************************
        // BStrUtils.setTv(home_product_second_down, bank.product.content.term);
        String term = bank.product.content.term;
        if (BStrUtils.isChineseStr(term) && 0 != BStrUtils.postionChineseStr(term)) {
            home_product_second_down.setTextSize(11);
            SpannableString spannableStrings = SpannableUtils.setTextSize(term, 0, BStrUtils.postionChineseStr(term), DimensionPixelUtil.dip2px(baseActivity, 15));
            BStrUtils.setSp(home_product_second_down, spannableStrings);
        } else if (0 == BStrUtils.postionChineseStr(term) && BStrUtils.hasDigit(term)) {
            home_product_second_down.setTextSize(11);
            SpannableString spannableStrings = SpannableUtils.setTextSize(term, BStrUtils.beginPostionDigit(term), BStrUtils.lastPostionDigit(term) + 1, DimensionPixelUtil.dip2px(baseActivity, 15));
            BStrUtils.setSp(home_product_second_down, spannableStrings);
        } else {
            home_product_second_down.setTextSize(11);
            BStrUtils.setTv(home_product_second_down, term);


        }
        //第三个*******************拷贝过来的产品的一坨逻辑************************************
        String raised_amt = BStrUtils.getYi(bank.product.content.remainingAmount);
        String mony;
        if (raised_amt.contains("亿")) {
            raised_amt = raised_amt.replace("亿", "");
            mony = raised_amt + "亿";
        } else {
            mony = raised_amt + "万";

        }

        SpannableString spannableStringmony = SpannableUtils.setTextSize(mony, 0, raised_amt.length(), DimensionPixelUtil.dip2px(baseActivity, 15));
        BStrUtils.setSp(home_product_three_down, spannableStringmony);
        //第一个**********************拷贝过来的产品的一坨逻辑*************************************
        //显示下边具体数据的 一大坨的逻辑判断
        switch (bank.product.content.productType) {
            case "1":
                BStrUtils.setTv1(home_product_frist_up, "业绩基准");
                String yield = bank.product.content.expectedYield + "%";
                SpannableString spannableString = SpannableUtils.setTextSize(yield, 0, bank.product.content.expectedYield.length(), DimensionPixelUtil.dip2px(baseActivity, 15));
                BStrUtils.setSp(home_product_frist_down, spannableString);
//                BStrUtils.setTv1(home_product_frist_down, bank.product.content.expectedYield + "%");

                break;
            case "2":
                BStrUtils.setTv1(home_product_frist_up, "累计净值");
                String cumulativeNet = bank.product.content.cumulativeNet;
                home_product_frist_down.setTextSize(11);
                if (BStrUtils.isChineseStr(cumulativeNet)) {
                    SpannableString spannableStrings = SpannableUtils.setTextSize(cumulativeNet, 0, BStrUtils.postionChineseStr(cumulativeNet), DimensionPixelUtil.dip2px(baseActivity, 15));
                    BStrUtils.setSp(home_product_frist_down, spannableStrings);
                } else if (cumulativeNet.contains("%")) {
                    SpannableString spannableStrings = SpannableUtils.setTextSize(cumulativeNet, 0, cumulativeNet.length() - 2, DimensionPixelUtil.dip2px(baseActivity, 15));
                    BStrUtils.setSp(home_product_frist_down, spannableStrings);
                } else {
                    home_product_frist_down.setTextSize(15);
                    BStrUtils.setTv1(home_product_frist_down, bank.product.content.cumulativeNet);
                }

                break;
            case "3":
                BStrUtils.setTv1(home_product_frist_up, "剩余额度");
                String fudong = bank.product.content.expectedYield + "%+浮动";

                SpannableString spannableString2 = SpannableUtils.setTextSize(fudong, 0, bank.product.content.expectedYield.length(), DimensionPixelUtil.dip2px(baseActivity, 15));
                BStrUtils.setSp(home_product_frist_down, spannableString2);

                break;
        }


        if (!AppManager.getIsLogin(baseActivity)) {
            BStrUtils.setTv1(view_home_product_tag, "风险评测后可见");

        } else if (TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {//需要风险测评
            BStrUtils.setTv1(view_home_product_tag, "风险评测后可见");

        } else {//显示截至打款时间************拷贝过来的一坨产品逻辑***************
            try {
                HomeEntity.bankData productlsBean = bank.product.content;
                // 服务器返回的时间格式，需要转换为毫秒值，与当前时间相减得到时间差，显示到list里
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                if (productlsBean.raiseEndTime != null && !"".equals(productlsBean.raiseEndTime)) {
                    java.util.Date end_time = dateFormat.parse(productlsBean.raiseEndTime);
                    long l = end_time.getTime() - System.currentTimeMillis();
                    String dateString = null;
                    int day = (int) (l / 1000 / 60 / 60 / 24);
                    int hour = (int) (l / 1000 / 60 / 60);
                    int min = (int) (l / 1000 / 60);

                    if (hour >= 72) {
                        dateString = day + "天";
                    } else if (hour > 0 && hour < 72) {
                        dateString = hour + "小时";
                    } else {
                        if (min == 0) {
                            dateString = 1 + "分钟";
                        } else {
                            dateString = min + "分钟";
                        }
                    }
                    if (l <= 0) {
                        BStrUtils.setTv(view_home_product_tag, "已截止");
                    } else {
                        BStrUtils.setTv(view_home_product_tag, "截止" + dateString + "打款");
                    }
                } else {
                    view_home_product_tag.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                view_home_product_tag.setVisibility(View.GONE);
            }


        }
    }

    private void initOperation(List<HomeEntity.Operate> module) {
        if (null != module && 0 != module.size()) {
            int sizeNumber = module.size();
            int number = module.size();
            if (sizeNumber <= 5) {
                number = sizeNumber;
            } else if (6 == sizeNumber) {
                number = 3;
            } else if (sizeNumber == 7 || sizeNumber == 8) {
                number = 4;
            } else if (sizeNumber > 8) {
                number = 5;
            }
            main_home_gvw.setNumColumns(number);
            operationAdapter.refreshData(module, number);

            boolean isAutumn = SkineColorManager.isautumnHoliay();
            if (!isAutumn) {
                main_home_gvw.setVisibility(View.VISIBLE);
                festival_style.setVisibility(View.GONE);
            } else {
                festival_style.removeAllViews();
                main_home_gvw.setVisibility(View.GONE);
                imageViews = new ImageView[4];
                for (int i = 0; i < module.size(); i++) {
                    LinearLayout inflate = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_operation_spring, festival_style, false);

                    ImageView imageView = (ImageView) inflate.findViewById(R.id.item_operation_iv);
                    TextView textView = (TextView) inflate.findViewById(R.id.item_operation_des);
                    textView.setText(module.get(i).title);
                    Imageload.display(getActivity(), getResource("main_icon_function_" + (i + 1)), imageView);
                    festival_style.addView(inflate);

                    imageViews[i] = imageView;
                    int finalI = i;
                    inflate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setGVWClick(module.get(finalI));
                        }
                    });
                }
                festival_style.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public int getResource(String imageName) {
        Context ctx = BaseApplication.getContext();
        int resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        return resId;
    }

    /**
     * 用户等级的数据填充
     */
    private void initLevel(HomeEntity.Level level) {
        BStrUtils.setTv1(viewHomeLevelStr, level.memberLevel);
        view_home_member.setTextColor(getResources().getColor("0".equals(level.level) ? R.color.home_level_gray : R.color.home_level_golde));
        viewHomeLevelStr.setTextColor(getResources().getColor("0".equals(level.level) ? R.color.home_level_gray : R.color.home_level_white));
        view_home_level_bg.setImageResource("0".equals(level.level) ? R.drawable.home_level_normal_bg : R.drawable.home_level_level_bg);

        view_home_level_arrow.setImageResource("0".equals(level.level) ? R.drawable.home_level_gray_arrow : R.drawable.home_level_arrow);
    }

    @Override
    public void getResultError(String error) {
        if (mainHomeSwiperefreshlayout.isRefreshing()) {
            mainHomeSwiperefreshlayout.setRefreshing(false);
        }
    }

    /**
     * 获取缓存成功
     */
    @Override
    public void getCacheResult(HomeEntity.Result cachesData) {
        if (null == cachesData) return;
        //横向轮播
//        initHorizontalScroll(cachesData.module);
        //banner
        initViewPage(cachesData.banner);
        //用户等级信息
        initLevel(cachesData.myInfo);
        //产品
        initProduct(cachesData.bank);
    }

    @Override
    public void getUseriInfsucc(int type) {
        switch (type) {
            case 1:
                home_floatstewardview.closeFloat();
                home_floatstewardview.refreshData(AppManager.isVisitor(baseActivity) || !AppManager.isBindAdviser(baseActivity));
                break;
        }
    }


    /**
     * 下拉刷新展示
     */
    @Override
    public void onRefresh() {
        isLoading = false;
        //刷新webview
        mainhomeWebview.loadUrl("javascript:refresh()");
        //请求数据
        getPresenter().getHomeData();
        RxBus.get().post(RxConstant.REFRESH_LIVE_DATA, true);
    }

    /**
     * scrollview滑动时候的监听
     */
    @Override
    public void onSmartScrollListener(boolean isTop, boolean isBottom, int scrollX, int scrollY, int scrolloldX, int scrolloldY) {
        LogUtils.Log("scrolllll", "新Y" + scrollY + "原来的Y" + scrolloldY);
        if ((scrollY > scrolloldY) && scrollY >= 200) {
            hindCard(scrollY);
        } else if ((scrolloldY > scrollY) && scrollY <= 200) {
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_level_lay://等级
                String url = CwebNetConfig.membercenter;
                UiSkipUtils.toNextActivity(baseActivity, MembersAreaActivity.class);
                TrackingDataManger.homeMember(baseActivity);
                break;
            case R.id.main_home_newlive_lay:
                if (null == homeliveInfBean) return;
                switch (homeliveInfBean.type) {
                    case 0://预告
                        PromptManager.ShowCustomToast(baseActivity, "直播暂未开始");
                        TrackingDataManger.homeLiveClick(baseActivity, homeliveInfBean.content, homeliveInfBean.id);

                        break;
                    case 1://直播

                        Intent intent = new Intent(baseActivity, LiveActivity.class);
                        intent.putExtra("liveJson", homeliveInfBean.jsonstr);
                        startActivity(intent);

                        SPreference.putString(baseActivity, Contant.CUR_LIVE_ROOM_NUM, homeliveInfBean.id);
                        DataStatistApiParam.homeliveclick();
                        TrackingDataManger.homeLiveClick(baseActivity, homeliveInfBean.content, homeliveInfBean.id);

                        break;
                    case 2://无直播
                        break;
                }
                DataStatistApiParam.homeliveclick();
                break;
            case R.id.view_home_product_focus://点击产品 先判断是否登录 和是否做过风险测评

                if (AppManager.isVisitor(baseActivity)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("insidegotologin", true);
                    map.put("backgohome", true);
                    NavigationUtils.startActivityByRouter(baseActivity, RouteConfig.GOTO_LOGIN, map);
                    return;
                } else if (TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {//需要风险测评
                    gotoRiskevalust();
                    return;
                }
                if (null != homeData && null != homeData.bank && null != homeData.bank.product && null != homeData.bank.product.content) {
                    HomeEntity.bankData productlsBean = homeData.bank.product.content;
                    ProductNavigationUtils.startProductDetailActivity(baseActivity, productlsBean.schemeId, productlsBean.productName, 100);
//                    DataStatistApiParam.onStatisToCProductItemClick(productlsBean.productId, productlsBean.shortName, "1".equals(productlsBean.isHotProduct));
                }
                TrackingDataManger.homeProduct(baseActivity);
                break;
            case R.id.home_product_skip_bank:
                NavigationUtils.jumpNativePage(baseActivity, WebViewConstant.Navigation.PRIVATE_BANK_PAGE);
                TrackingDataManger.homePrivateMore(baseActivity);
                break;
        }
    }

    private void gotoRiskevalust() {
        DefaultDialog dialog = new DefaultDialog(baseActivity, "请先填写调查问卷", "取消", "确定") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                Router.build(RouteConfig.GOTO_APP_RISKEVALUATIONACTIVITY).go(baseActivity);
                dismiss();

            }
        };
        dialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        homeBannerview.endBanner();
        LogUtils.Log("sssaa", "首页不可见");
    }


    private void hindCard(int Y) {
        if (200 == Y) {
            TrackingDataManger.homePersonClose(baseActivity);
        }
        home_floatstewardview.closeFloat();
    }

    /**
     * 开始倒计时十秒
     */
    private void timeCountDown() {
        RxCountDown.countdown(ADVISERSHOWTIME).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                hindCard(200);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {


            }
        });
    }

    private float lastRotate = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // x,y,z分别存储坐标轴x,y,z上的加速度
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 根据三个方向上的加速度值得到总的加速度值a
            float a = (float) Math.sqrt(x * x + y * y + z * z);
            System.out.println("a---------->" + a);
            // 传感器从外界采集数据的时间间隔为10000微秒
            System.out.println("magneticSensor.getMinDelay()-------->"
                    + magneticSensor.getMinDelay());
            // 加速度传感器的最大量程
            System.out.println("event.sensor.getMaximumRange()-------->"
                    + event.sensor.getMaximumRange());

            System.out.println("x------------->" + x);
            System.out.println("y------------->" + y);
            System.out.println("z------------->" + z);

            Log.d("jarlen", "x------------->" + x);
            Log.d("jarlen", "y------------>" + y);
            Log.d("jarlen", "z----------->" + z);


            if (Math.abs(x) < 5) {
                if (Math.abs(lastRotate - (x * 5)) > 1)
                    setRotate(lastRotate, x * 5);
                lastRotate = x * 5;
//                RotateAnimation animation = new RotateAnimation(-deValue, deValue, Animation.RELATIVE_TO_SELF,
//                        pxValue, Animation.RELATIVE_TO_SELF, pyValue);

//                imageView.setAnimation(animation);
//                imageView

            }

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            // 三个坐标轴方向上的电磁强度，单位是微特拉斯(micro-Tesla)，用uT表示，也可以是高斯(Gauss),1Tesla=10000Gauss
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 手机的磁场感应器从外部采集数据的时间间隔是10000微秒
            System.out.println("magneticSensor.getMinDelay()-------->"
                    + magneticSensor.getMinDelay());
            // 磁场感应器的最大量程
            System.out.println("event.sensor.getMaximumRange()----------->"
                    + event.sensor.getMaximumRange());
            System.out.println("x------------->" + x);
            System.out.println("y------------->" + y);
            System.out.println("z------------->" + z);

            // Log.d("TAG","x------------->" + x);
            // Log.d("TAG", "y------------>" + y);
            // Log.d("TAG", "z----------->" + z);

            // showTextView.setText("x---------->" + x + "\ny-------------->" +
            // y + "\nz----------->" + z);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
            if (timestamp != 0) {

                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                final float dT = (event.timestamp - timestamp) * NS2S;

                // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                angle[2] += event.values[2] * dT;

                // 将弧度转化为角度
                float anglex = (float) Math.toDegrees(angle[0]);

                float angley = (float) Math.toDegrees(angle[1]);

                float anglez = (float) Math.toDegrees(angle[2]);

                System.out.println("anglex------------>" + anglex);
                System.out.println("angley------------>" + angley);
                System.out.println("anglez------------>" + anglez);


                System.out.println("gyroscopeSensor.getMinDelay()----------->" +
                        gyroscopeSensor.getMinDelay());
            }
            //将当前时间赋值给timestamp
            timestamp = event.timestamp;
        }
    }

    private void setRotate(float lastRotate, float r) {
        float min, max;
        if (lastRotate < r) {
            max = r;
            min = lastRotate;
        } else {
            max = lastRotate;
            min = r;
        }
//        Log.e("setRotate --- ", " r = " + r + " lastRotate = " + lastRotate + "----" + (lastRotate-r));
//        for (float i = min; i < max; i = i + 1) {
            imageViews[0].setPivotX(imageViews[0].getWidth() / 2);
            imageViews[0].setRotation(r);
            imageViews[1].setPivotX(imageViews[1].getWidth() / 2);
            imageViews[1].setRotation(r);
            imageViews[2].setPivotX(imageViews[2].getWidth() / 2);
            imageViews[2].setRotation(r);
            imageViews[3].setPivotX(imageViews[3].getWidth() / 2);
            imageViews[3].setRotation(r);
//        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO Auto-generated method stub
    }


}
