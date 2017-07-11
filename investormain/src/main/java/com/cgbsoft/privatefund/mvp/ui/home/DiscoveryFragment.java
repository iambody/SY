package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.lib.widget.adapter.FragmentAdapter;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.DiscoverIndicatorAdapter;
import com.cgbsoft.privatefund.model.DiscoverModel;
import com.cgbsoft.privatefund.model.DiscoveryListModel;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryPresenter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author chenlong
 *
 * 资讯页面
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoverContract.View {

    @BindView(R.id.discover_bannerview)
    BannerView discoveryBannerView;

    @BindView(R.id.discover_list_indicator)
    MagicIndicator magicIndicator;

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
    protected void init(View view, Bundle savedInstanceState) {
        commonNavigator = new CommonNavigator(baseActivity);
        disCoveryNavigationAdapter = new DiscoverIndicatorAdapter(getActivity(), viewPager);
        commonNavigator.setAdapter(disCoveryNavigationAdapter);
        magicIndicator.setNavigator(commonNavigator);
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), lazyFragments);
        viewPager.setOffscreenPageLimit(20);
        viewPager.setAdapter(fragmentAdapter);
        ViewPagerHelper.bind(magicIndicator, viewPager);
        getPresenter().getDiscoveryFirstData();
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return new DiscoveryPresenter(getActivity(), this);
    }

    @Override
    public void requestFirstDataSuccess(DiscoverModel discoverModel) {
        discoveryBannerView.setVisibility(CollectionUtils.isEmpty(discoverModel.banner) ? View.GONE : View.VISIBLE);
        initBanner(DiscoverModel.formatBanner(discoverModel.banner));
        initIndicatorList(discoverModel);
    }

    @Override
    public void requestFirstDataFailure(String errMsg) {
    }

    private void initBanner(List<BannerBean> list) {
//        List<BannerBean> list = new ArrayList<>();
//        list.add(new BannerBean(false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498574380597&di=9d45fb18e608e1102bb6951b884b10c6&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F04%2F15%2F146070551257980148.GIF", BannerBean.ViewType.OVAL));
//        list.add(new BannerBean(false, "http://dimg08.c-ctrip.com/images/tg/132/715/635/f6d1d5683770473bb31d19743e7df6bd.jpg", BannerBean.ViewType.OVAL));
//        list.add(new BannerBean(false, "http://img4.duitang.com/uploads/item/201509/04/20150904204338_RcSCF.jpeg", BannerBean.ViewType.OVAL));
//        list.add(new BannerBean(false, "http://h.hiphotos.baidu.com/lvpics/h=800/sign=d3cb72cb38292df588c3a1158c305ce2/b812c8fcc3cec3fdbb261091d488d43f8794273d.jpg", BannerBean.ViewType.OVAL));
//        list.add(new BannerBean(false, "http://pic.58pic.com/58pic/12/00/76/78b58PICVWs.jpg", BannerBean.ViewType.OVAL));
//        list.add(new BannerBean(false, "http://youimg1.c-ctrip.com/target/fd/tg/g1/M04/7E/C3/CghzflVTERSAaOlcAAGrbgRCst0677.jpg", BannerBean.ViewType.OVAL));
        discoveryBannerView.initShowImageForNet(getActivity(), list);
        discoveryBannerView.setOnclickBannerItemView(bannerBean -> {
            Toast.makeText(getActivity(), "你添加的是第".concat(String.valueOf(bannerBean.getPositon())).concat("个图片"), Toast.LENGTH_SHORT).show();
        });
        if (discoveryBannerView != null) {
            discoveryBannerView.startBanner();
        }
    }

    private void initIndicatorList(DiscoverModel discoverModel) {
        lazyFragments = new ArrayList<>();
        for (int i = 0; i < discoverModel.category.size(); i++) {
            DiscoveryListFragment baseLazyFragment = new DiscoveryListFragment(discoverModel.category.get(i).value + "");
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
