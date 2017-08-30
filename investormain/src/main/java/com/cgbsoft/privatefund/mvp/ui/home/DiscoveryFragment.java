package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.DiscoverModel;
import com.cgbsoft.lib.base.model.DiscoveryListModel;
import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.lib.widget.adapter.FragmentAdapter;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.DiscoverIndicatorAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryPresenter;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @author chenlong
 *         资讯页面
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoverContract.View {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.discover_bannerview)
    BannerView discoveryBannerView;

    @BindView(R.id.discover_list_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.discover_list_pager)
    ViewPager viewPager;

    CommonNavigator commonNavigator;
    FragmentAdapter fragmentAdapter;
    List<BaseLazyFragment> lazyFragments = new ArrayList<>();
    DiscoverIndicatorAdapter disCoveryNavigationAdapter;

    @Override
    protected int layoutID() {
        return R.layout.fragment_discover_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_SIHANG_ZX);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_SIHANG_ZX);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initIndicatorView();
        initViewPage();
        initCache();
        getPresenter().getDiscoveryFirstData();
    }

    private void initCache() {
        if (null != AppManager.getDiscoveryModleData(baseActivity)) {
            refrushModule(AppManager.getDiscoveryModleData(baseActivity));
        }
    }

    private void initIndicatorView() {
        commonNavigator = new CommonNavigator(baseActivity);
        disCoveryNavigationAdapter = new DiscoverIndicatorAdapter(getActivity(), viewPager);
        commonNavigator.setAdapter(disCoveryNavigationAdapter);
        commonNavigator.setSmoothScroll(true);
        magicIndicator.setNavigator(commonNavigator);
    }

    private void initViewPage() {
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), lazyFragments);
        viewPager.setOffscreenPageLimit(20);
        viewPager.setAdapter(fragmentAdapter);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter(getActivity(), this);
    }

    @Override
    public void requestFirstDataSuccess(DiscoverModel discoverModel) {
        AppInfStore.saveDiscoverModelData(getContext(), discoverModel);
        refrushModule(discoverModel);
    }

    private void refrushModule(DiscoverModel discoverModel) {
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) appBarLayout.getChildAt(0).getLayoutParams();
        if (CollectionUtils.isEmpty(discoverModel.banner)) {
            mParams.setScrollFlags(0);
        } else {
            mParams.setScrollFlags(5);
        }
        discoveryBannerView.setVisibility(CollectionUtils.isEmpty(discoverModel.banner) ? View.GONE : View.VISIBLE);
        initBanner(DiscoverModel.formatBanner(discoverModel.banner));
        if (CollectionUtils.isEmpty(lazyFragments)) {
            initIndicatorList(discoverModel);
        }
    }

    @Override
    public void requestFirstDataFailure(String errMsg) {
    }

    public void refrushListData() {
        getPresenter().getDiscoveryFirstData();
    }

    private void initBanner(List<BannerBean> valuelist) {
        discoveryBannerView.initShowImageForNet(getActivity(), valuelist);
        discoveryBannerView.setOnclickBannerItemView(bannerBean -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.push_message_url, bannerBean.getJumpUrl());
            hashMap.put(WebViewConstant.push_message_title, bannerBean.getTitle());
            NavigationUtils.startActivity(getActivity(), RightShareWebViewActivity.class, hashMap);
            DataStatistApiParam.operatePrivateBankDiscoverClick(bannerBean.getTitle());
        });
        if (discoveryBannerView != null) {
            discoveryBannerView.startBanner();
        }
    }

    private void initIndicatorList(DiscoverModel discoverModel) {
        lazyFragments = new ArrayList<>();
        for (int i = 0; i < discoverModel.category.size(); i++) {
            DiscoveryListFragment baseLazyFragment = new DiscoveryListFragment(this, discoverModel.category.get(i).value + "");
            lazyFragments.add(baseLazyFragment);
            if (0 == i) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(DiscoveryListFragment.INIT_LIST_DATA_PARAMS, (ArrayList<DiscoveryListModel>) discoverModel.informations);
                baseLazyFragment.setArguments(bundle);
            }
        }
        disCoveryNavigationAdapter.setData(discoverModel.category);
        fragmentAdapter.freshAp(lazyFragments);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (discoveryBannerView != null) {
            discoveryBannerView.endBanner();
        }
    }
}
