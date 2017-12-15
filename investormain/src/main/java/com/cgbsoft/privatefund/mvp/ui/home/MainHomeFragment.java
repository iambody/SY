package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
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
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.lib.widget.MySwipeRefreshLayout;
import com.cgbsoft.lib.widget.SmartScrollView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.LiveInfBean;
import com.cgbsoft.privatefund.mvc.ui.MembersAreaActivity;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.FloatStewardView;

import java.util.ArrayList;
import java.util.List;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.vido.VideoNavigationUtils;
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
    @BindView(R.id.main_home_horizontalscrollview_lay)
    LinearLayout mainHomeHorizontalscrollviewLay;
    @BindView(R.id.main_home_horizontalscrollview)
    HorizontalScrollView mainHomeHorizontalscrollview;
    @BindView(R.id.view_home_level_str)
    TextView viewHomeLevelStr;
    @BindView(R.id.main_home_swiperefreshlayout)
    MySwipeRefreshLayout mainHomeSwiperefreshlayout;
    @BindView(R.id.main_home_smartscrollview)
    SmartScrollView mainHomeSmartscrollview;
    @BindView(R.id.main_home_new_iv)
    ImageView mainHomeNewIv;
    @BindView(R.id.view_live_title)
    TextView viewLiveTitle;
    View main_home_live_lay;
    @BindView(R.id.view_live_iv_lay)
    RelativeLayout viewLiveIvLay;
    @BindView(R.id.home_bannerview)
    BannerView homeBannerview;
    @BindView(R.id.home_floatstewardview)
    FloatStewardView home_floatstewardview;
    View main_home_level_lay;
    TextView view_live_title_tag;
    ImageView view_live_iv_bg, view_live_title_tag_iv;
    TextView view_live_title, view_live_content;

    private UnreadInfoNumber unreadInfoNumber;
    private Observable<LiveInfBean> liveObservable;
    private Observable<Integer> userLayObservable, bindAdviserObservable;


    private HomeEntity.Result homeData;
    private LiveInfBean homeliveInfBean;

    private boolean isLoading;
    private boolean bannerIsLeft;
    private boolean bannerIsRight;
    private boolean isRolling;
    protected boolean isVisible;
    private int downXPostion;
    private int lastXPostion;

    @Override

    protected int layoutID() {
        return R.layout.fragment_mainhome;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initConfig();
        mainhomeWebview.loadUrls(CwebNetConfig.HOME_URL);
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
//        CommonNewShareDialog h=new CommonNewShareDialog(baseActivity,CommonNewShareDialog.SHARE_WX,null,null);
//        h.show();
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

        RelativeLayout.LayoutParams bannerParames = new RelativeLayout.LayoutParams(screenWidth, (int) ((screenWidth * 61) / 75));
        homeBannerview.setLayoutParams(bannerParames);
        /* 直播 */
        view_live_title_tag = ViewHolders.get(mFragmentView, R.id.view_live_title_tag);
        view_live_iv_bg = ViewHolders.get(mFragmentView, R.id.view_live_iv_bg);
        view_live_title_tag_iv = ViewHolders.get(mFragmentView, R.id.view_live_title_tag_iv);
        //标题和内容
        view_live_title = ViewHolders.get(mFragmentView, R.id.view_live_title);
        view_live_content = ViewHolders.get(mFragmentView, R.id.view_live_content);
        /* 直播*/
        main_home_level_lay = mFragmentView.findViewById(R.id.main_home_level_lay);
        main_home_level_lay.setOnClickListener(this);
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
        main_home_live_lay = mFragmentView.findViewById(R.id.main_home_live_lay);
        main_home_live_lay.setOnClickListener(this);
        //游客模式下或者没有绑定过理财师需要
        initRxEvent();
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
                        main_home_live_lay.setVisibility(View.VISIBLE);
                        view_live_iv_bg = ViewHolders.get(mFragmentView, R.id.view_live_iv_bg);
                        Imageload.displayroud(baseActivity, liveInfBean.image, 2, view_live_iv_bg);
                        //标题和内容view_live_title
                        BStrUtils.SetTxt(view_live_title, "直播预告:");
                        BStrUtils.SetTxt(view_live_content, liveInfBean.title);
                        BStrUtils.SetTxt(view_live_title_tag, liveInfBean.create_time + "开播");

                        view_live_title_tag_iv.setVisibility(View.INVISIBLE);
                        break;
                    case 1://直播中
                        main_home_live_lay.setVisibility(View.VISIBLE);
                        main_home_live_lay.setClickable(true);

                        view_live_iv_bg = ViewHolders.get(mFragmentView, R.id.view_live_iv_bg);
                        Imageload.displayroud(baseActivity, liveInfBean.image, 2, view_live_iv_bg);
                        //标题和内容
                        BStrUtils.SetTxt(view_live_content, liveInfBean.title);
                        BStrUtils.SetTxt(view_live_title, "正在直播:");
                        BStrUtils.SetTxt(view_live_title_tag, "正在直播");
                        view_live_title_tag_iv.setVisibility(View.VISIBLE);
                        break;
                    case 2://无直播
                        main_home_live_lay.setVisibility(View.GONE);
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

//    /**
//     * 显示直播的布局
//     */
//    private void showLiveView() {
//        main_home_live_lay.setVisibility(View.VISIBLE);
//        int ivWidth = (int) (screenWidth * 2.6 / 5);
//        int ivHeight = (int) (ivWidth * 2.6 / 5);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ivWidth, ivHeight);
//        viewLiveIvLay.setLayoutParams(layoutParams);
//        //下边需要填充
//    }

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
        //横向轮播
        initHorizontalScroll(data.module);
        //banner
        initViewPage(data.banner);
        //用户等级信息
        initLevel(data.myInfo);
    }

    /**
     * 用户等级的数据填充
     */
    private void initLevel(HomeEntity.Level level) {
        BStrUtils.SetTxt1(viewHomeLevelStr, level.memberLevel);
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
        initHorizontalScroll(cachesData.module);
        //banner
        initViewPage(cachesData.banner);
        //用户等级信息
        initLevel(cachesData.myInfo);
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
     * 横向滑动时候的数据填充
     */

    public void initHorizontalScroll(List<HomeEntity.Operate> data) {
        mainHomeHorizontalscrollviewLay.removeAllViews();
        mainHomeHorizontalscrollviewLay.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                return false;
            }
        });
        mainHomeHorizontalscrollview.setOnTouchListener(new onOperationScrollImpl());

        int ivWidth = (int) (screenWidth / 4);

        for (int i = 0; i < data.size(); i++) {
            View view = LayoutInflater.from(baseActivity).inflate(R.layout.item_horizontal_lay, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ivWidth, ivWidth);

            params.setMargins(0 == i ? 0 : DimensionPixelUtil.dip2px(baseActivity, 6), 0, DimensionPixelUtil.dip2px(baseActivity, 6), 0);
            view.setLayoutParams(params);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_horizontal_img);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Imageload.display(baseActivity, data.get(i).imageUrl, imageView);
            view.setOnClickListener(new HorizontalItemClickListener(data.get(i), i));
            mainHomeHorizontalscrollviewLay.addView(view);
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
            case R.id.main_home_live_lay://直播
                if (null == homeliveInfBean) return;

                switch (homeliveInfBean.type) {
                    case 0://预告
                        PromptManager.ShowCustomToast(baseActivity, "直播暂未开始");
                        break;
                    case 1://直播

                        Intent intent = new Intent(baseActivity, LiveActivity.class);
                        intent.putExtra("liveJson", homeliveInfBean.jsonstr);
                        startActivity(intent);

                        SPreference.putString(baseActivity, Contant.CUR_LIVE_ROOM_NUM, homeliveInfBean.id);
                        DataStatistApiParam.homeliveclick();
                        break;
                    case 2://无直播
                        break;
                }
                DataStatistApiParam.homeliveclick();

                break;
        }
    }

    /**
     * 横向滑动的点击事件处理
     */
    private class HorizontalItemClickListener implements View.OnClickListener {
        private HomeEntity.Operate data;
        private int postion;

        public HorizontalItemClickListener(HomeEntity.Operate data, int postion) {
            this.data = data;
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
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


    class onOperationScrollImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction())
                downXPostion = (int) event.getX();
            if (MotionEvent.ACTION_MOVE == event.getAction()) {
                lastXPostion = (int) event.getX() - downXPostion;
                downXPostion = (int) event.getX();
            }
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (lastXPostion > 0) {
                    //向左滑动
                    TrackingDataManger.homeOperateLeft(baseActivity);
                } else {
                    //向右滑动
                    TrackingDataManger.homeOperateRight(baseActivity);
                }
            }
            return false;
        }


    }


}
