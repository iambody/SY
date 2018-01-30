package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
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
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;
import com.cgbsoft.privatefund.mvc.ui.MembersAreaActivity;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.FloatStewardView;
import com.chenenyu.router.Router;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View, SwipeRefreshLayout.OnRefreshListener, SmartScrollView.ISmartScrollChangedListener, View.OnClickListener {
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
    //公募的
    @BindView(R.id.view_public_fund_regist)
    LinearLayout viewPublicFundRegist;
    @BindView(R.id.view_home_public_fund_fundname)
    TextView viewHomePublicFundFundname;
    @BindView(R.id.view_home_public_fund_funddes)
    TextView viewHomePublicFundFunddes;
    @BindView(R.id.view_home_public_fund_leftvalues)
    TextView viewHomePublicFundLeftvalues;
    @BindView(R.id.view_home_public_fund_leftdes)
    TextView viewHomePublicFundLeftdes;
    @BindView(R.id.view_home_public_fund_rightvalues)
    TextView viewHomePublicFundRightvalues;
    @BindView(R.id.view_home_public_fund_rightdes)
    TextView viewHomePublicFundRightdes;
    @BindView(R.id.view_home_public_fund_shift)
    TextView viewHomePublicFundShift;
    @BindView(R.id.view_home_public_fund_detial_lay)
    LinearLayout viewHomePublicFundDetialLay;
    @BindView(R.id.view_home_public_fund_lay)
    LinearLayout viewHomePublicFundLay;
    //私募基金
    @BindView(R.id.home_product_title)
    TextView homeProductTitle;
    @BindView(R.id.home_peoduct_subtitle)
    TextView homePeoductSubtitle;
    @BindView(R.id.view_home_product_bg)
    ImageView viewHomeProductBg;
    @BindView(R.id.view_home_product_tag)
    TextView viewHomeProductTag;
    @BindView(R.id.view_home_product_name)
    TextView viewHomeProductName;
    @BindView(R.id.view_home_product_des)
    TextView viewHomeProductDes;
    @BindView(R.id.home_product_frist_up)
    TextView homeProductFristUp;
    @BindView(R.id.home_product_frist_down)
    TextView homeProductFristDown;
    @BindView(R.id.home_product_second_up)
    TextView homeProductSecondUp;
    @BindView(R.id.home_product_second_down)
    TextView homeProductSecondDown;
    @BindView(R.id.home_product_three_up)
    TextView homeProductThreeUp;
    @BindView(R.id.home_product_three_down)
    TextView homeProductThreeDown;

    //等级
    @BindView(R.id.view_home_member)
    TextView viewHomeMember;
    @BindView(R.id.view_home_level_arrow)
    ImageView viewHomeLevelArrow;
    @BindView(R.id.main_home_gvw)
    MyGridView mainHomeGvw;
    //新的直播新增
    @BindView(R.id.view_newlive_iv_bg)
    ImageView viewNewliveIvBg;
    @BindView(R.id.view_newlive_meng_bg)
    ImageView viewNewliveMengBg;
    @BindView(R.id.view_newlive_content)
    TextView viewNewliveContent;
    @BindView(R.id.view_newlive_tag)
    TextView viewNewliveTag;
    @BindView(R.id.view_newlive_title_tag)
    TextView viewNewliveTitleTag;
    @BindView(R.id.view_newlive_number)
    TextView viewNewliveNumber;
    @BindView(R.id.view_home_level_bg)
    ImageView viewHomeLevelBg;
    @BindView(R.id.home_newlive_foreshow_lay)
    RelativeLayout homeNewliveForeshowLay;
    @BindView(R.id.home_newlive_now_lay)
    RelativeLayout homeNewliveNowLay;
    @BindView(R.id.home_product_down_lay)
    LinearLayout homeProductDownLay;

    View home_product_view;
    View main_home_level_lay;
    View main_home_newlive_lay;
    //私募基金新增
    LinearLayout view_home_product_focus, view_home_private_fund_skip_lay;

    OperationAdapter operationAdapter;
    UnreadInfoNumber unreadInfoNumber;
    Observable<LiveInfBean> liveObservable;
    Observable<Integer> userLayObservable, bindAdviserObservable;

    HomeEntity.Result homeData;
    LiveInfBean homeliveInfBean;

    boolean isLoading;
    boolean bannerIsLeft;
    boolean bannerIsRight;
    boolean isRolling;
    boolean isVisible;


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
        getPresenter().getPublicFundRecommend();
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
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), mainHomeNewIv, false);
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
        main_home_newlive_lay.setOnClickListener(this);
        home_product_view = ViewHolders.get(mFragmentView, R.id.home_product_view);
        //标题和内容
        /* 直播*/
        main_home_level_lay = mFragmentView.findViewById(R.id.main_home_level_lay);
        main_home_level_lay.setOnClickListener(this);
        //等级
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
        operationAdapter = new OperationAdapter(baseActivity, R.layout.item_operation);
        mainHomeGvw.setAdapter(operationAdapter);

        initProduct(mFragmentView);
        //游客模式下或者没有绑定过理财师需要
        initRxEvent();
        mainHomeGvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeEntity.Operate data = (HomeEntity.Operate) parent.getItemAtPosition(position);
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
        });


        mainhomeWebview.loadUrls(CwebNetConfig.HOME_URL);
    }


    private void initProduct(View mFragmentView) {
        //私募基金
        view_home_product_focus = ViewHolders.get(mFragmentView, R.id.view_home_product_focus);
        view_home_private_fund_skip_lay = ViewHolders.get(mFragmentView, R.id.view_home_private_fund_skip_lay);


        view_home_product_focus.setOnClickListener(this);
        view_home_private_fund_skip_lay.setOnClickListener(this);


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
                        homeNewliveForeshowLay.setVisibility(View.VISIBLE);
                        viewNewliveMengBg.setVisibility(View.VISIBLE);
                        homeNewliveNowLay.setVisibility(View.GONE);
                        Imageload.display(baseActivity, liveInfBean.image, viewNewliveIvBg);
                        BStrUtils.setTv(viewNewliveTitleTag, liveInfBean.create_time + "开播");
                        viewNewliveTag.setText("直播预告");
                        BStrUtils.setTv(viewNewliveContent, liveInfBean.title);
                        break;
                    case 1://直播中
                        //新版直播
                        main_home_newlive_lay.setVisibility(View.VISIBLE);
                        homeNewliveForeshowLay.setVisibility(View.GONE);
                        homeNewliveNowLay.setVisibility(View.VISIBLE);
                        viewNewliveMengBg.setVisibility(View.GONE);
                        Imageload.display(baseActivity, liveInfBean.image, viewNewliveIvBg);
                        viewNewliveTag.setText("正在直播");
                        BStrUtils.setTv(viewNewliveContent, liveInfBean.title);
                        BStrUtils.setTv(viewNewliveNumber, BStrUtils.NullToStr(liveInfBean.visitors) + "人正在观看");

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
        BStrUtils.setTv(homeProductTitle, bank.title);
        BStrUtils.setTv(homePeoductSubtitle, bank.subtitle);
        home_product_view.setVisibility(View.VISIBLE);
        if (null == bank.product || null == bank.product.content) {
            baseActivity.findViewById(R.id.home_product_view).setVisibility(View.GONE);
            return;
        }
        Imageload.display(baseActivity, bank.product.content.marketingImageUrl, viewHomeProductBg);

        BStrUtils.setTv1(viewHomeProductName, bank.product.content.productName);
        BStrUtils.setTv1(viewHomeProductDes, bank.product.content.hotName);
        if (AppManager.isVisitor(baseActivity) || TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {
            homeProductDownLay.setVisibility(View.GONE);
        } else {
            homeProductDownLay.setVisibility(View.VISIBLE);
        }

        //第三个*******************************************************
        // BStrUtils.setTv(home_product_second_down, bank.product.content.term);
        String term = bank.product.content.term;
        if (BStrUtils.isChineseStr(term) && 0 != BStrUtils.postionChineseStr(term)) {
            homeProductSecondDown.setTextSize(11);
            SpannableString spannableStrings = SpannableUtils.setTextSize(term, 0, BStrUtils.postionChineseStr(term), DimensionPixelUtil.dip2px(baseActivity, 15));
            BStrUtils.setSp(homeProductSecondDown, spannableStrings);
        } else if (0 == BStrUtils.postionChineseStr(term) && BStrUtils.hasDigit(term)) {
            homeProductSecondDown.setTextSize(11);
            SpannableString spannableStrings = SpannableUtils.setTextSize(term, BStrUtils.beginPostionDigit(term), BStrUtils.lastPostionDigit(term) + 1, DimensionPixelUtil.dip2px(baseActivity, 15));
            BStrUtils.setSp(homeProductSecondDown, spannableStrings);
        } else {
            homeProductSecondDown.setTextSize(11);
            BStrUtils.setTv(homeProductSecondDown, term);


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
        BStrUtils.setSp(homeProductThreeDown, spannableStringmony);
        //第一个**********************拷贝过来的产品的一坨逻辑*************************************
        //显示下边具体数据的 一大坨的逻辑判断
        switch (bank.product.content.productType) {
            case "1":
                BStrUtils.setTv1(homeProductFristUp, "业绩基准");
                String yield = bank.product.content.expectedYield + "%";
                SpannableString spannableString = SpannableUtils.setTextSize(yield, 0, bank.product.content.expectedYield.length(), DimensionPixelUtil.dip2px(baseActivity, 15));
                BStrUtils.setSp(homeProductFristDown, spannableString);
//                BStrUtils.setTv1(homeProductFristDown, bank.product.content.expectedYield + "%");

                break;
            case "2":
                BStrUtils.setTv1(homeProductFristUp, "累计净值");
                String cumulativeNet = bank.product.content.cumulativeNet;
                homeProductFristDown.setTextSize(11);
                if (BStrUtils.isChineseStr(cumulativeNet)) {
                    SpannableString spannableStrings = SpannableUtils.setTextSize(cumulativeNet, 0, BStrUtils.postionChineseStr(cumulativeNet), DimensionPixelUtil.dip2px(baseActivity, 15));
                    BStrUtils.setSp(homeProductFristDown, spannableStrings);
                } else if (cumulativeNet.contains("%")) {
                    SpannableString spannableStrings = SpannableUtils.setTextSize(cumulativeNet, 0, cumulativeNet.length() - 2, DimensionPixelUtil.dip2px(baseActivity, 15));
                    BStrUtils.setSp(homeProductFristDown, spannableStrings);
                } else {
                    homeProductFristDown.setTextSize(15);
                    BStrUtils.setTv1(homeProductFristDown, bank.product.content.cumulativeNet);
                }

                break;
            case "3":
                BStrUtils.setTv1(homeProductFristUp, "剩余额度");
                String fudong = bank.product.content.expectedYield + "%+浮动";

                SpannableString spannableString2 = SpannableUtils.setTextSize(fudong, 0, bank.product.content.expectedYield.length(), DimensionPixelUtil.dip2px(baseActivity, 15));
                BStrUtils.setSp(homeProductFristDown, spannableString2);

                break;
        }


        if (!AppManager.getIsLogin(baseActivity)) {
            BStrUtils.setTv1(viewHomeProductTag, "风险评测后可见");

        } else if (TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {//需要风险测评
            BStrUtils.setTv1(viewHomeProductTag, "风险评测后可见");

        } else {//显示截至打款时间************拷贝过来的一坨产品逻辑***************
            try {
                HomeEntity.bankData productlsBean = bank.product.content;
                // 服务器返回的时间格式，需要转换为毫秒值，与当前时间相减得到时间差，显示到list里
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                if (productlsBean.raiseEndTime != null && !"".equals(productlsBean.raiseEndTime)) {
                    Date end_time = dateFormat.parse(productlsBean.raiseEndTime);
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
                        BStrUtils.setTv(viewHomeProductTag, "已截止");
                    } else {
                        BStrUtils.setTv(viewHomeProductTag, "截止" + dateString + "打款");
                    }
                } else {
                    viewHomeProductTag.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewHomeProductTag.setVisibility(View.GONE);
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
            mainHomeGvw.setNumColumns(number);
            operationAdapter.refreshData(module, number);
        }
    }

    /**
     * 用户等级的数据填充
     */
    private void initLevel(HomeEntity.Level level) {
        BStrUtils.setTv1(viewHomeLevelStr, level.memberLevel);
        viewHomeMember.setTextColor(getResources().getColor("0".equals(level.level) ? R.color.home_level_gray : R.color.home_level_golde));
        viewHomeLevelStr.setTextColor(getResources().getColor("0".equals(level.level) ? R.color.home_level_gray : R.color.home_level_white));
        viewHomeLevelBg.setImageResource("0".equals(level.level) ? R.drawable.home_level_normal_bg : R.drawable.home_level_level_bg);

        viewHomeLevelArrow.setImageResource("0".equals(level.level) ? R.drawable.home_level_gray_arrow : R.drawable.home_level_arrow);
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

    @Override
    public void getPublicFundResult(PublishFundRecommendBean publishFundRecommendBean) {
        initPublicFundData(false, publishFundRecommendBean);
    }

    PublishFundRecommendBean publishFundRecommend;

    /**
     * 初始化公募基金数据（历史原因需要进行单独请求）
     *
     * @param publishFundRecommendBean
     */
    private void initPublicFundData(boolean isCache, PublishFundRecommendBean publishFundRecommendBean) {
        if (isCache) {
        } else {

        }
        publishFundRecommend = publishFundRecommendBean;
        viewHomePublicFundLay.setVisibility(View.VISIBLE);
//        viewPublicFundRegist.setVisibility("1".equals(publishFundRecommendBean.getIsHaveAccount())?View.GONE:View.VISIBLE);
        BStrUtils.setTv(viewHomePublicFundFundname, publishFundRecommendBean.getFundName());
        BStrUtils.setTv(viewHomePublicFundFunddes, publishFundRecommendBean.getFundDes());
        BStrUtils.setTv(viewHomePublicFundLeftvalues, publishFundRecommendBean.getLeftUpValue());
        BStrUtils.setTv(viewHomePublicFundLeftdes, publishFundRecommendBean.getLeftDownDes());
        BStrUtils.setTv(viewHomePublicFundRightvalues, publishFundRecommendBean.getRightUpValue());
        BStrUtils.setTv(viewHomePublicFundRightdes, publishFundRecommendBean.getRightDownDes());

    }

    @Override
    public void getPublicFundError(String error) {

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
        getPresenter().getPublicFundRecommend();
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
                }
                TrackingDataManger.homeProduct(baseActivity);
                break;
            case R.id.view_home_private_fund_skip_lay:
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
     * 手动闭合
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


    /**
     * 公募基金的注册按钮
     */
    @OnClick(R.id.view_public_fund_regist)
    public void onViewClicked() {
        NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.publicFundRegistUrl, getResources().getString(R.string.public_fund_regist), false);
    }

    /**
     * 公募基金详情
     */
    @OnClick(R.id.view_home_public_fund_detial_lay)
    public void publicFundDetail() {

        NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.publicFundDetailUrl + "?fundcode=" + publishFundRecommend.getFundCode(), String.format("%s(%s)", publishFundRecommend.getFundName(), publishFundRecommend.getFundCode()), false);
    }

    /**
     * 公募基金转入
     */
    @OnClick(R.id.view_home_public_fund_shift)
    public void publicFundShift() {

    }


}
