package com.cgbsoft.privatefund.mvp.ui.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.SmartScrollView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View, SwipeRefreshLayout.OnRefreshListener, SmartScrollView.ISmartScrollChangedListener {


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
    @BindView(R.id.main_home_adviser_inf_iv)
    RoundImageView mainHomeAdviserInfIv;
    @BindView(R.id.main_home_adviser_layyy)
    LinearLayout mainHomeAdviserLayyy;
    @BindView(R.id.view_home_level_str)
    TextView viewHomeLevelStr;
    @BindView(R.id.main_home_swiperefreshlayout)
    SwipeRefreshLayout mainHomeSwiperefreshlayout;
    @BindView(R.id.main_home_smartscrollview)
    SmartScrollView mainHomeSmartscrollview;


    //名片动画展示时候需要的动画
    private ObjectAnimator adviserCardObjectAnimator;
    //是否已经展示出来名片
    private boolean isShowAdviserCard;

    // Banner适配器
    private BannerAdapter homeBannerAdapter;

    @Override

    protected int layoutID() {
        return R.layout.fragment_mainhome;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initConfig();
        mainhomeWebview.loadUrls(CwebNetConfig.HOME_URL);
        //测试数据**********************
        List<HomeEntity.Operate> datas = new ArrayList<>();
        HomeEntity.Operate d1 = new HomeEntity.Operate();
        d1.imageUrl = "http://dimg08.c-ctrip.com/images/tg/132/715/635/f6d1d5683770473bb31d19743e7df6bd.jpg";
        HomeEntity.Operate d2 = new HomeEntity.Operate();
        d2.imageUrl = "http://youimg1.c-ctrip.com/target/fd/tg/g1/M04/7E/C3/CghzflVTERSAaOlcAAGrbgRCst0677.jpg";
        datas.add(d1);
        datas.add(d2);
        datas.add(d1);
        //测试数据**********************
        initHorizontalScroll(datas);

        //测试数据**********************
        List<HomeEntity.Banner> datasa = new ArrayList<>();
        HomeEntity.Banner d3 = new HomeEntity.Banner();
        d3.imageUrl = "http://dimg08.c-ctrip.com/images/tg/132/715/635/f6d1d5683770473bb31d19743e7df6bd.jpg";
        HomeEntity.Banner d4 = new HomeEntity.Banner();
        d4.imageUrl = "http://youimg1.c-ctrip.com/target/fd/tg/g1/M04/7E/C3/CghzflVTERSAaOlcAAGrbgRCst0677.jpg";
        datasa.add(d3);
        datasa.add(d4);
        datasa.add(d3);
        //测试数据**********************
        homeBannerAdapter = new BannerAdapter(datasa);
        mainHomeBannerview.setAdapter(homeBannerAdapter);
        initViewPage(datasa);
        //请求数据
        getPresenter().getHomeData();
    }

    /**
     * 配置view各种资源
     */
    private void initConfig() {
        mainHomeSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainHomeSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click);
        mainHomeSwiperefreshlayout.setOnRefreshListener(this);
        mainHomeSmartscrollview.setScrollChangedListener(this);
    }

    //初始化banner
    private void initViewPage(List<HomeEntity.Banner> banner) {

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
     * 横向滑动时候的数据填充
     */
    public void initHorizontalScroll(List<HomeEntity.Operate> data) {
//        mainHomeHorizontalscrollviewLay.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            View view = LayoutInflater.from(baseActivity).inflate(R.layout.item_horizontal_lay, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimensionPixelUtil.dip2px(baseActivity, 120), DimensionPixelUtil.dip2px(baseActivity, 100));
            params.setMargins(DimensionPixelUtil.dip2px(baseActivity, 10), 0, DimensionPixelUtil.dip2px(baseActivity, 10), 0);
            view.setLayoutParams(params);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_horizontal_img);
            Imageload.display(baseActivity, data.get(i).imageUrl, imageView);
            view.setOnClickListener(new HorizontalItemClickListener(data.get(i), i));
            mainHomeHorizontalscrollviewLay.addView(view);
        }
    }

    /**
     * 开始展示的animator
     */
    public void initShowCardAnimator(View V) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 0f, 0f, 1f);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", -600f, 0f);

        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleY", 0.5f, 1f);

        AnimatorSet animationSet = new AnimatorSet();

        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator);
        animationSet.setDuration(1 * 1000);
        animationSet.start();
    }

    /**
     * 消失的animator
     */
    public void initDismissCardAnimator(View V) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 1f, 0f);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", 0f, -600f);

        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleX", 1f, 0.9f, 0.5f, 0.3f, 0.2f, 0.1f, 0f);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator);
        animationSet.setDuration(1 * 1000);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                V.setVisibility(View.GONE);
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


    //理财师头像的点击事件
    @OnClick(R.id.main_home_adviser_inf_iv)
    public void onViewivClicked() {
        mainHomeAdviserLayyy.setVisibility(View.VISIBLE);
        initShowCardAnimator(mainHomeAdviserLayyy);
    }

    //下拉刷新展示
    @Override
    public void onRefresh() {
        mainHomeSwiperefreshlayout.setRefreshing(false);
    }

    ///scrollview滑动时候的监听
    @Override
    public void onSmartScrollListener(boolean isTop, boolean isBottom, int scrollX, int scrollY, int scrolloldX, int scrolloldY) {
        LogUtils.Log("scrolllll", "新Y" + scrollY + "原来的Y" + scrolloldY);
        if ((scrollY >scrolloldY ) && scrollY>= 200) {
            if (mainHomeAdviserLayyy.getVisibility() == View.VISIBLE)
                mainHomeAdviserLayyy.setVisibility(View.GONE);
        } else if ((scrolloldY >scrollY ) && scrollY <= 200) {
            if (mainHomeAdviserLayyy.getVisibility() == View.GONE)
                initShowCardAnimator(mainHomeAdviserLayyy);
        }
    }


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
        }
    }

    /**
     * banner
     */
    private class BannerAdapter extends StaticPagerAdapter {

        List<HomeEntity.Banner> banners;

        public BannerAdapter(List<HomeEntity.Banner> banners) {
            this.banners = banners;
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
