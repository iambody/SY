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
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.MySwipeRefreshLayout;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.SmartScrollView;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
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

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View, SwipeRefreshLayout.OnRefreshListener, SmartScrollView.ISmartScrollChangedListener {
    public static final String LIVERXOBSERBER_TAG = "rxobserlivetag";

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
    //
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


    //名片动画展示时候需要的动画
    private ObjectAnimator adviserCardObjectAnimator;
    //是否已经展示出来名片
    private boolean isShowAdviserCard;
    //游客模式下是否已经展示出来
    private boolean isVisiterShow;


//    //每天一次的签到的dialog
//    private HomeSignDialog signDialog;

    // Banner适配器
    private BannerAdapter homeBannerAdapter;
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

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
        mainHomeBannerview.setPlayDelay(6 * 1000);

        //请求数据
        getPresenter().getHomeData();
    }


    boolean isBindAdviser;
    UserInfoDataEntity.UserInfo userInfo;

    /**
     * 配置view各种资源
     */
    private void initConfig() {
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
        main_home_live_lay = mFragmentView.findViewById(R.id.main_home_live_lay);
//        showLiveView();
        boolean isVisiter = AppManager.isVisitor(baseActivity);
        userInfo = AppManager.getUserInfo(baseActivity);
        isBindAdviser = AppManager.isBindAdviser(baseActivity);
        if (isVisiter || !isBindAdviser) {//游客模式下或者没有绑定过理财师需要
            //登录模式
            mainHomeLoginLay.setVisibility(View.GONE);
            //游客模式
            mainHomeVisterLay.setVisibility(View.VISIBLE);


//            Imageload.display(baseActivity, userInfo.headImageUrl, mainHomeVisterAdviserInfIv);
        } else {//登录模式下并且已经绑定过理财师
            //登录模式
            mainHomeLoginLay.setVisibility(View.VISIBLE);
            //游客模式
            mainHomeVisterLay.setVisibility(View.GONE);
            LogUtils.Log("lalal", "开始加载");
            //开始填充登录模式下理财师数据
            Imageload.displayListener(baseActivity, userInfo.bandingAdviserHeadImageUrl, mainHomeAdviserInfIv, new RequestListener() {
                @Override
                public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {

                    mainHomeAdviserInfLay.setVisibility(View.VISIBLE);
                    LogUtils.Log("lalal", "已经加载");
                    return false;
                }
            });
            BStrUtils.SetTxt(mainHomeCardnumberTxt, userInfo.bandingAdviserUniqueCode);

        }
        initRxEvent();
    }

    private Observable<LiveInfBean> liveObservable;

    /**
     * 注册监听事件
     */
    private void initRxEvent() {
        //直播状态监听
        liveObservable = RxBus.get().register(LIVERXOBSERBER_TAG, LiveInfBean.class);
        liveObservable.subscribe(new RxSubscriber<LiveInfBean>() {
            @Override
            protected void onEvent(LiveInfBean liveInfBean) {
                if (liveInfBean.isLiveing) {//直播中

                } else {//没直播
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    /**
     * 显示直播的布局
     */
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
        }

    }

    /**
     * 获取数据进行数据填充
     *
     * @param data
     */
    private void initResultData(HomeEntity.Result data) {
        //横向轮播
        initHorizontalScroll(data.module);
        //banner
        initViewPage(data.banner);
        //用户等级信息
        initLevel(data.member);
    }

    /**
     * 用户等级的数据填充
     */
    private void initLevel(HomeEntity.Level level) {
        BStrUtils.SetTxt(viewHomeLevelStr, level.levelName);
    }

    @Override
    public void getResultError(String error) {

    }

    /**
     * 签到动作的结果通知
     */
    @Override
    public void getSignResult(String message) {
        PromptManager.ShowCustomToast(baseActivity, message);

    }

    /**
     * 横向滑动时候的数据填充
     */
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

    /**
     * 开始展示下边大布局的animator
     */
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

    //下拉刷新展示
    @Override
    public void onRefresh() {
        mainHomeSwiperefreshlayout.setRefreshing(false);
        //刷新webview
        mainhomeWebview.loadUrl("javascript:refresh()");
        //请求数据
        getPresenter().getHomeData();
//        UiSkipUtils.toNextActivityWithIntent(baseActivity, new Intent(baseActivity, VideoHistoryListActivity.class));
    }

    ///scrollview滑动时候的监听
    @Override
    public void onSmartScrollListener(boolean isTop, boolean isBottom, int scrollX, int scrollY, int scrolloldX, int scrolloldY) {
        LogUtils.Log("scrolllll", "新Y" + scrollY + "原来的Y" + scrolloldY);
        if ((scrollY > scrolloldY) && scrollY >= 200) {
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
        } else if ((scrolloldY > scrollY) && scrollY <= 200) {
            if (mainHomeAdviserLayyy.getVisibility() == View.GONE) {
//                mainHomeCardLay.setVisibility(View.GONE);
//                initShowCardAnimator(mainHomeAdviserLayyy);
            }
        }
    }

    //点击消息
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

    //登录模式点击电话
    @OnClick(R.id.main_home_adviser_phone)
    public void onMainHomeAdviserPhoneClicked() {

        DefaultDialog dialog = new DefaultDialog(baseActivity, "是否联系投资顾问", "确消", "呼叫") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                dismiss();
                NavigationUtils.startDialgTelephone(baseActivity, userInfo.adviserPhone);
            }
        };
        dialog.show();
    }

    //登录模式点击短信
    @OnClick(R.id.main_home_adviser_note)
    public void onMainHomeAdviserNoteClicked() {
        Utils.sendSmsWithNumber(baseActivity, userInfo.adviserPhone);
    }

    //登录模式点击聊天
    @OnClick(R.id.main_home_adviser_im)
    public void onMainHomeAdviserImClicked() {
        RongIM.getInstance().startConversation(baseActivity, Conversation.ConversationType.PRIVATE, userInfo.toC.bandingAdviserId, userInfo.adviserRealName);
    }

    //非游客模式头像的点击事件
    @OnClick(R.id.main_home_adviser_inf_iv)
    public void onViewivClicked() {
        if (isShowAdviserCard) return;
        mainHomeAdviserLayyy.setVisibility(View.VISIBLE);
        isShowAdviserCard = true;
        initShowCardAnimator(mainHomeAdviserLayyy, false);
    }

    //游客模式点击头像
    @OnClick(R.id.main_home_vister_adviser_inf_iv)
    public void onViewvisterivClicked() {
        if (isVisiterShow) return;
        mainHomeVisterAdviserLayyy.setVisibility(View.VISIBLE);
        isVisiterShow = true;
        initShowCardAnimator(mainHomeVisterAdviserLayyy, true);

    }

    @OnClick(R.id.main_home_adviser_title)
    public void adviserTextClick() {
        NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.BindchiceAdiser, "选择投顾", false);
    }

    @OnClick(R.id.main_home_invisiter_txt_lay)
    public void onViewinvisitertxtlayClicked() {
        //游客模式或者未绑定需要跳转到未绑定的
//        NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.choiceAdviser, "选择投顾", false);
        //已经绑定过的
        if (isBindAdviser) {
            NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.BindchiceAdiser, "我的投顾", false);
        } else {
            NavigationUtils.gotoWebActivity(baseActivity, CwebNetConfig.choiceAdviser, "选择投顾", false);
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
            PromptManager.ShowCustomToast(baseActivity, "位置" + postion);
//            if (null == signDialog) signDialog = new HomeSignDialog(baseActivity);
//            signDialog.show();
            Intent intent = new Intent(baseActivity, LoginActivity.class);
            intent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
            UiSkipUtils.toNextActivityWithIntent(baseActivity, intent);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.Log("sssaa", "首页可见");
        if (getUserVisibleHint()) {
            isVisible = true;
            LogUtils.Log("sssaa", "首页可见");
            mainHomeBannerview.resume();
        } else {
            isVisible = false;
            LogUtils.Log("sssaa", "首页不可见");
            mainHomeBannerview.pause();
        }

    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getUserVisibleHint()) {
//            isVisible = true;
//            LogUtils.Log("sssaa", "首页可见");
//            mainHomeBannerview.resume();
//        } else {
//            isVisible = false;
//            LogUtils.Log("sssaa", "首页不可见");
//            mainHomeBannerview.pause();
//        }
//
//    }

    /**
     * banner
     */
    private class BannerAdapter extends StaticPagerAdapter {

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


}
