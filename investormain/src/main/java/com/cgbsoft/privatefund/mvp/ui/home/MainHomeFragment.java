package com.cgbsoft.privatefund.mvp.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.MySwipeRefreshLayout;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.SmartScrollView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.LiveInfBean;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;

import java.util.List;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View, SwipeRefreshLayout.OnRefreshListener, SmartScrollView.ISmartScrollChangedListener, View.OnClickListener {
    public static final String LIVERXOBSERBER_TAG = "rxobserlivetag";
    public final int PLAYDELAYTIME = 6;
    public final int ADVISERSHOWTIME = 5;
    public final int ADVISERLOADTIME = 3;
    @BindView(R.id.mainhome_webview)
    BaseWebview mainhomeWebview;
    @BindView(R.id.main_home_bannerview)
    RollPagerView mainHomeBannerview;
    @BindView(R.id.main_home_horizontalscrollview_lay)
    LinearLayout mainHomeHorizontalscrollviewLay;
    @BindView(R.id.main_home_horizontalscrollview)
    HorizontalScrollView mainHomeHorizontalscrollview;
    @BindView(R.id.main_home_adviser_inf_lay)
    LinearLayout mainHomeAdviserInfLay;
    //登录模式下
    @BindView(R.id.main_home_adviser_inf_iv)
    RoundImageView mainHomeAdviserInfIv;
    @BindView(R.id.main_home_adviser_layyy)
    LinearLayout mainHomeAdviserLayyy;
    @BindView(R.id.view_home_level_str)
    TextView viewHomeLevelStr;
    @BindView(R.id.main_home_swiperefreshlayout)
    MySwipeRefreshLayout mainHomeSwiperefreshlayout;
    @BindView(R.id.main_home_smartscrollview)
    SmartScrollView mainHomeSmartscrollview;
    //邀请码
    @BindView(R.id.main_home_cardnumber_txt)
    TextView mainHomeCardnumberTxt;
    //邀请码的布局
    @BindView(R.id.main_home_card_lay)
    LinearLayout mainHomeCardLay;
    @BindView(R.id.main_home_new_iv)
    ImageView mainHomeNewIv;
    @BindView(R.id.view_live_iv)
    ImageView viewLiveIv;
    @BindView(R.id.view_live_title)
    TextView viewLiveTitle;
    //直播预告
    View main_home_live_lay;

    @BindView(R.id.view_live_iv_lay)
    RelativeLayout viewLiveIvLay;
    @BindView(R.id.main_home_adviser_title)
    TextView mainHomeAdviserTitle;
    @BindView(R.id.main_home_adviser_phone)
    TextView mainHomeAdviserPhone;
    @BindView(R.id.main_home_adviser_note)
    TextView mainHomeAdviserNote;
    @BindView(R.id.main_home_adviser_im)
    TextView mainHomeAdviserIm;
    @BindView(R.id.main_home_adviser_relation_lay)
    LinearLayout mainHomeAdviserRelationLay;
    //登录模式下的视图布局
    @BindView(R.id.main_home_login_lay)
    RelativeLayout mainHomeLoginLay;
    @BindView(R.id.main_home_vister_adviser_layyy)
    LinearLayout mainHomeVisterAdviserLayyy;
    @BindView(R.id.main_home_vister_adviser_inf_lay)
    LinearLayout mainHomeVisterAdviserInfLay;
    //游客模式的头像
    @BindView(R.id.main_home_vister_adviser_inf_iv)
    RoundImageView mainHomeVisterAdviserInfIv;
    @BindView(R.id.main_home_vister_adviser_layy)
    LinearLayout mainHomeVisterAdviserLayy;
    //游客模式下的布局
    @BindView(R.id.main_home_vister_lay)
    RelativeLayout mainHomeVisterLay;
    @BindView(R.id.main_home_invisiter_txt_lay)
    LinearLayout mainHomeInvisiterTxtLay;

    //会员布局
    View main_home_level_lay;
    //名片动画展示时候需要的动画
    private ObjectAnimator adviserCardObjectAnimator;
    //是否已经展示出来名片
    private boolean isShowAdviserCard;
    //游客模式下是否已经展示出来
    private boolean isVisiterShow;
    // Banner适配器
    private BannerAdapter homeBannerAdapter;
    // Fragment当前状态是否可见
    protected boolean isVisible;
    //是否绑定理财师
    boolean isBindAdviser;
    UserInfoDataEntity.UserInfo userInfo;

    private Observable<LiveInfBean> liveObservable;
    private Observable<Integer> userLayObservable, infdataObservable;

    private boolean isLoading;

    @Override

    protected int layoutID() {
        return R.layout.fragment_mainhome;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initConfig();
        mainhomeWebview.loadUrls(CwebNetConfig.HOME_URL);
        homeBannerAdapter = new BannerAdapter();
        mainHomeBannerview.setAdapter(homeBannerAdapter);
        mainHomeBannerview.setHintView(new IconHintView(baseActivity, R.drawable.home_page_pre, R.drawable.home_page_nor));
        mainHomeBannerview.setPlayDelay(PLAYDELAYTIME * 1000);
        initshowlay();
        timeCountDown();
        //缓存
        initCache();
        //请求数据
        getPresenter().getHomeData();
    }

    /*游客模式游客布局显示 费游客模式非游客布局显示*/
    private void initshowlay() {

        if (AppManager.isVisitor(baseActivity)) {
            onViewvisterivClicked();
        } else {
            onViewivClicked();
        }
    }

    /*开始倒计时十秒*/
    private void timeCountDown() {
        RxCountDown.countdown(ADVISERSHOWTIME).doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                hindCard();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {


            }
        });
    }

    /*判断缓存*/
    private void initCache() {
        HomeEntity.Result data = AppManager.getHomeCache(baseActivity);
        if (null != data)
            initResultData(data);
    }

    /*登录模式点击短信*/
    @OnClick(R.id.main_home_adviser_note)
    public void onMainHomeAdviserNoteClicked() {
        Utils.sendSmsWithNumber(baseActivity, userInfo.adviserPhone);
    }

    /*登录模式点击聊天*/
    @OnClick(R.id.main_home_adviser_im)
    public void onMainHomeAdviserImClicked() {
        RongIM.getInstance().startConversation(baseActivity, Conversation.ConversationType.PRIVATE, userInfo.toC.bandingAdviserId, userInfo.adviserRealName);
    }

    /* 非游客模式头像的点击事件*/
    @OnClick(R.id.main_home_adviser_inf_iv)
    public void onViewivClicked() {
        if (isShowAdviserCard) return;
        mainHomeAdviserLayyy.setVisibility(View.VISIBLE);
        isShowAdviserCard = true;
        initShowCardAnimator(mainHomeAdviserLayyy, false);
    }

    /*游客模式点击头像*/
    @OnClick(R.id.main_home_vister_adviser_inf_iv)
    public void onViewvisterivClicked() {
        if (isVisiterShow) return;
        mainHomeVisterAdviserLayyy.setVisibility(View.VISIBLE);
        isVisiterShow = true;
        initShowCardAnimator(mainHomeVisterAdviserLayyy, true);
    }

    @OnClick(R.id.main_home_adviser_title)
    public void adviserTextClick() {
        NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.BindchiceAdiser, getResources().getString(R.string.select_adviser), false);
    }

    @OnClick(R.id.main_home_invisiter_txt_lay)
    public void onViewinvisitertxtlayClicked() {

        if (isBindAdviser) {
            NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.BindchiceAdiser, getResources().getString(R.string.my_adviser), false);
        } else {
            NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.choiceAdviser, getResources().getString(R.string.select_adviser), false);
        }
    }

    /* 登录模式点击电话*/
    @OnClick(R.id.main_home_adviser_phone)
    public void onMainHomeAdviserPhoneClicked() {
        getPresenter().gotoConnectAdviser();
    }


    /*点击消息*/
    @OnClick(R.id.main_home_new_iv)
    public void onNewClicked() {
        if (AppManager.isVisitor(baseActivity)) {//游客模式
            Intent intent = new Intent(baseActivity, LoginActivity.class);
            intent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
            UiSkipUtils.toNextActivityWithIntent(baseActivity, intent);
        } else {//非游客模式
            UiSkipUtils.toNextActivityWithIntent(baseActivity, new Intent(baseActivity, MessageListActivity.class));
        }
    }


   /*  配置view各种资源*/

    private void initConfig() {
        main_home_level_lay = mFragmentView.findViewById(R.id.main_home_level_lay);
        main_home_level_lay.setOnClickListener(this);
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
        main_home_live_lay = mFragmentView.findViewById(R.id.main_home_live_lay);
        main_home_live_lay.setOnClickListener(this);
        boolean isVisiter = AppManager.isVisitor(baseActivity);
        userInfo = AppManager.getUserInfo(baseActivity);
        isBindAdviser = AppManager.isBindAdviser(baseActivity);
        //游客模式下或者没有绑定过理财师需要
        if (isVisiter || !isBindAdviser) {
            //登录模式
            mainHomeLoginLay.setVisibility(View.GONE);
            //游客模式
            mainHomeVisterLay.setVisibility(View.VISIBLE);
        } else {//登录模式下并且已经绑定过理财师
            //登录模式
            mainHomeLoginLay.setVisibility(View.VISIBLE);
            //游客模式
            mainHomeVisterLay.setVisibility(View.GONE);
            //开始填充登录模式下理财师数据
            Imageload.displayListener(baseActivity, userInfo.bandingAdviserHeadImageUrl, mainHomeAdviserInfIv, new RequestListener() {
                @Override
                public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {

                    mainHomeAdviserInfLay.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            BStrUtils.SetTxt(mainHomeCardnumberTxt, userInfo.bandingAdviserUniqueCode);
        }
        initRxEvent();
//        showLiveView();
        mainHomeAdviserTitle.setText(String.format("尊敬的%s，我是您的专属私人银行家，很高兴为您服务", AppManager.getUserInfo(baseActivity).realName));
    }

    /*  注册监听事件*/
    private void initRxEvent() {
        //游客登录进入正常模式
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
                //开始刷新ui
                mainHomeAdviserInfLay.setVisibility(View.VISIBLE);
                //登录模式
                mainHomeLoginLay.setVisibility(View.VISIBLE);
                //游客模式
                mainHomeVisterLay.setVisibility(View.GONE);

                mainHomeAdviserTitle.setText(String.format("尊敬的%s，我是您的专属私人银行家，很高兴为您服务", AppManager.getUserInfo(baseActivity).realName));
                hindCard();
                initshowlay();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        //直播状态监听
        liveObservable = RxBus.get().register(LIVERXOBSERBER_TAG, LiveInfBean.class);
        liveObservable.subscribe(new RxSubscriber<LiveInfBean>() {
            @Override
            protected void onEvent(LiveInfBean liveInfBean) {
                if (liveInfBean.isLiveing) {//直播中
                    main_home_level_lay.setVisibility(View.GONE);
                } else {//没直播
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
    }
/* 显示直播的布局*/

    private void showLiveView() {
        main_home_live_lay.setVisibility(View.VISIBLE);
        int ivWidth = (int) (screenWidth * 2.6 / 5);
        int ivHeight = (int) (ivWidth * 2.6 / 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ivWidth, ivHeight);
        viewLiveIvLay.setLayoutParams(layoutParams);
        //下边需要填充
        //viewLiveIv 直播的图片
        //viewLiveTitle直播的title

    }

    //初始化banner
    private void initViewPage(List<HomeEntity.Banner> banner) {
        homeBannerAdapter.frash(banner);
    }

    @Override
    protected MainHomePresenter createPresenter() {
        return new MainHomePresenter(baseActivity, this);
    }


    @Override
    public void getResultSucc(HomeEntity.Result data) {
        if (null != data) {
            initResultData(data);
            //设置&&刷新缓存数据
            AppInfStore.saveHomeData(baseActivity, data);
        }

    }

   /* 获取数据进行数据填充*/

    private void initResultData(HomeEntity.Result data) {
        //横向轮播
        initHorizontalScroll(data.module);
        //banner
        initViewPage(data.banner);
        //用户等级信息
        initLevel(data.myInfo);
    }

    /* 用户等级的数据填充*/

    private void initLevel(HomeEntity.Level level) {
        BStrUtils.SetTxt(viewHomeLevelStr, level.memberLevel);
    }

    @Override
    public void getResultError(String error) {

    }

    /*获取缓存成功*/
    @Override
    public void getCacheResult(HomeEntity.Result cachesData) {
        if (null == cachesData) return;
     /*   处理缓存数据*/
        //横向轮播
        initHorizontalScroll(cachesData.module);
        //banner
        initViewPage(cachesData.banner);
        //用户等级信息
        initLevel(cachesData.myInfo);
    }



     /*横向滑动时候的数据填充*/

    public void initHorizontalScroll(List<HomeEntity.Operate> data) {
        mainHomeHorizontalscrollviewLay.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            View view = LayoutInflater.from(baseActivity).inflate(R.layout.item_horizontal_lay, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimensionPixelUtil.dip2px(baseActivity, 120), DimensionPixelUtil.dip2px(baseActivity, 100));
            params.setMargins(DimensionPixelUtil.dip2px(baseActivity, 10), 0, DimensionPixelUtil.dip2px(baseActivity, 10), 0);
            view.setLayoutParams(params);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_horizontal_img);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Imageload.display(baseActivity, data.get(i).imageUrl, imageView);
            view.setOnClickListener(new HorizontalItemClickListener(data.get(i), i));
            mainHomeHorizontalscrollviewLay.addView(view);
        }
    }

    /*开始展示下边大布局的animator*/

    public void initShowCardAnimator(View V, boolean isVisiter) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 0f, 0f, 1f);
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", -600f, 0f);
        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleY", 0.5f, 1f);
        AnimatorSet animationSet = new AnimatorSet();

        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator);
        animationSet.setDuration(1 * 1000);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isVisiter) {//游客模式下
                    mainHomeInvisiterTxtLay.setVisibility(View.VISIBLE);
                    getPresenter().showCardLayAnimation(mainHomeInvisiterTxtLay);
                } else {//非游客模式下
                    //动画结束开始
                    mainHomeCardLay.setVisibility(View.VISIBLE);
                    getPresenter().showCardLayAnimation(mainHomeCardLay);
                    //开始展示私人管家的信息
                    mainHomeAdviserRelationLay.setVisibility(View.VISIBLE);
                    getPresenter().showCardLayAnimation(mainHomeAdviserRelationLay);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.Log("saassaa", "resume");
//        mainHomeSmartscrollview.smoothScrollTo(0,20);
    }

    /*下拉刷新展示*/
    @Override
    public void onRefresh() {
        isLoading = true;
        mainHomeSwiperefreshlayout.setRefreshing(false);
        isLoading = false;
        //刷新webview
        mainhomeWebview.loadUrl("javascript:refresh()");
        //请求数据
        getPresenter().getHomeData();
    }

    /* scrollview滑动时候的监听*/
    @Override
    public void onSmartScrollListener(boolean isTop, boolean isBottom, int scrollX, int scrollY, int scrolloldX, int scrolloldY) {
        LogUtils.Log("scrolllll", "新Y" + scrollY + "原来的Y" + scrolloldY);
        if ((scrollY > scrolloldY) && scrollY >= 200) {
            hindCard();
        } else if ((scrolloldY > scrollY) && scrollY <= 200) {
            if (mainHomeAdviserLayyy.getVisibility() == View.GONE) {
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_level_lay://等级
                String url = CwebNetConfig.membercenter;
                NavigationUtils.gotoWebActivity(baseActivity, url, "会员", false);
                break;
            case R.id.main_home_live_lay://直播
//                if (liveJsonData != null) {
//                    liveDialog.setVisibility(View.GONE);
//                    liveDialog.clearAnimation();
//                    Intent intent = new Intent(this, LiveActivity.class);
//                    intent.putExtra("liveJson", liveJsonData.toString());
//                    intent.putExtra("type", "");
//                    startActivity(intent);
//                    try {
//                        SPreference.putString(this, Contant.CUR_LIVE_ROOM_NUM, liveJsonData.getString("id"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
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
                NavigationUtils.gotoWebActivity(baseActivity, data.url, data.title, false);
            } else if ("app".equals(data.jumpType)) {
                NavigationUtils.jumpNativePage(baseActivity, Integer.decode(data.jumpId));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean isVisibleToUser) {
        super.onHiddenChanged(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            LogUtils.Log("sssaa", "首页不可见");
            mainHomeBannerview.resume();
        } else {
            isVisible = false;
            LogUtils.Log("sssaa", "首页可见");
            mainHomeBannerview.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.Log("sssaa", "首页不可见");
    }

    public class BannerAdapter extends StaticPagerAdapter {
        List<HomeEntity.Banner> banners;

        public BannerAdapter() {
        }

        public BannerAdapter(List<HomeEntity.Banner> banners) {
            this.banners = banners;
        }

        public void frash(List<HomeEntity.Banner> datas) {
            this.banners = datas;
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(ViewGroup container, int position) {
            HomeEntity.Banner banner = banners.get(position);
            View view = LayoutInflater.from(baseActivity).inflate(R.layout.item_home_banner, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_homeimagecycleview_iv);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Imageload.display(baseActivity, banner.imageUrl, imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.gotoWebActivity(baseActivity, banner.url, banner.title, false);
                }
            });
            return view;
        }

        @Override
        public int getCount() {
            return null == banners ? 0 : banners.size();
        }

    }

    private void isUnvisibel(boolean isshow) {
        if (!isshow) {//不可见

        }
    }

    private void hindCard() {
        if (mainHomeAdviserLayyy.getVisibility() == View.VISIBLE) {
            //隐藏下边的布局文件
            mainHomeAdviserLayyy.setVisibility(View.GONE);
            isShowAdviserCard = false;
            //隐藏悬浮的服务码布局
            mainHomeCardLay.setVisibility(View.GONE);
            //todo 隐藏悬浮的理财师信息
            mainHomeAdviserRelationLay.setVisibility(View.GONE);
            //隐藏游客模式的右侧文字布局


        }
        if (mainHomeVisterAdviserLayyy.getVisibility() == View.VISIBLE) {
            mainHomeVisterAdviserLayyy.setVisibility(View.GONE);
            mainHomeInvisiterTxtLay.setVisibility(View.GONE);
            isVisiterShow = false;
        }
    }
}
