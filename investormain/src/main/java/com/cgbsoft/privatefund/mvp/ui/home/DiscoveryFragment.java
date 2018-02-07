package com.cgbsoft.privatefund.mvp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.DiscoverModel;
import com.cgbsoft.lib.base.model.DiscoveryListModel;
import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.model.bean.StockIndexBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDiscoveryDataStatistics;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.adapter.FragmentAdapter;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.DiscoverIndicatorAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryPresenter;
import com.cgbsoft.privatefund.utils.receiver.HoriizontalItemDecoration;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.umeng.analytics.MobclickAgent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.lang.ref.WeakReference;
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

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.divide_stock_index)
    LinearLayout linearlayout;

    @BindView(R.id.discover_bannerview)
    BannerView discoveryBannerView;

    @BindView(R.id.discover_list_indicator)
    MagicIndicator magicIndicator;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.discover_list_pager)
    ViewPager viewPager;
    private int currentPosition = -1;

    CommonNavigator commonNavigator;
    FragmentAdapter fragmentAdapter;
    List<BaseLazyFragment> lazyFragments = new ArrayList<>();
    DiscoverIndicatorAdapter disCoveryNavigationAdapter;
    MyHolderAdapter myHolderAdapter;
    private boolean isStockLoading;

    private MyHandler myHandler;

    @Override
    protected int layoutID() {
        return R.layout.fragment_discover_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_SIHANG_ZX);
        myHandler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_SIHANG_ZX);
        myHandler.removeCallbacks(runnable);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initIndicatorView();
        initViewPage();
        initStockIndexView();
        initCache();
        getPresenter().getDiscoveryFirstData();
        myHandler = new MyHandler(getActivity());
    }

    Runnable runnable = () -> {
        if (!isStockLoading) {
            getPresenter().getStockIndex();
            isStockLoading = true;
        }
        myHandler.obtainMessage().sendToTarget();
    };

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

    private void initStockIndexView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(new HoriizontalItemDecoration(getActivity(), R.color.white, R.dimen.ui_40_dip));
        recyclerView.setLayoutManager(linearLayoutManager);
        myHolderAdapter = new MyHolderAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(myHolderAdapter);
    }

    private void initViewPage() {
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), lazyFragments);
        viewPager.setOffscreenPageLimit(8);
        viewPager.setAdapter(fragmentAdapter);
//        ViewPagerHelper.bind(magicIndicator, viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
                if (currentPosition == position) {
                    return;
                }
                if (currentPosition > position){
                    TrackingDiscoveryDataStatistics.discoveryLeftScroll(getContext());
                } else {
                    TrackingDiscoveryDataStatistics.discoveryRightScroll(getContext());
                }
                currentPosition = position;
            }

            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
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
    protected void viewBeShow() {
        super.viewBeShow();
        TrackingDiscoveryDataStatistics.discoveryClickFlag(getContext(), "资讯");
    }

    @Override
    public void requestFirstDataFailure(String errMsg) {
        MToast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestStockIndexSuccess(List<StockIndexBean> dataList) {
        isStockLoading = false;
        myHolderAdapter.setDataList(dataList);
    }

    @Override
    public void reqeustStockIndexFailure(String message) {
        isStockLoading = false;
        MToast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void viewBeHide() {
        super.viewBeHide();
    }

    private class MyHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private LayoutInflater mInflater;
        private List<StockIndexBean> mDatas;

        private MyHolderAdapter(Context context, List<StockIndexBean> datatsList) {
            mInflater = LayoutInflater.from(context);
            mDatas = datatsList;
        }

        private void setDataList(List<StockIndexBean> dataList) {
            if (!CollectionUtils.isEmpty(dataList)) {
                mDatas.clear();
                mDatas.addAll(dataList);
                notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
                linearlayout.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (mDatas == null) {
                return 0;
            }
            return mDatas.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.fragment_stock_index_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.stockValue = (TextView) view.findViewById(R.id.stockValue);
            viewHolder.increaseValue = (TextView) view.findViewById(R.id.increase_value);
            viewHolder.increatePercent = (TextView) view.findViewById(R.id.increase_percent);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            StockIndexBean stockIndexBean = mDatas.get(position);
            holder.name.setText(stockIndexBean.getName());
            holder.stockValue.setText(ViewUtils.formatNumberPatter(stockIndexBean.getIndex(), 2));
            holder.increaseValue.setText((!TextUtils.isEmpty(stockIndexBean.getGain()) && stockIndexBean.getGain().startsWith("-")) ? stockIndexBean.getGain() : "+".concat(stockIndexBean.getGain()));
            holder.increatePercent.setText((!TextUtils.isEmpty(stockIndexBean.getRate()) && stockIndexBean.getRate().startsWith("-")) ? stockIndexBean.getRate() : "+".concat(stockIndexBean.getRate()));
            setIndexValueColor(stockIndexBean.getRate(), holder.stockValue);
            setIndexValueColor(stockIndexBean.getRate(), holder.increaseValue);
            setIndexValueColor(stockIndexBean.getRate(), holder.increatePercent);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            this.rootView = arg0;
        }
        View rootView;
        TextView name;
        TextView stockValue;
        TextView increaseValue;
        TextView increatePercent;
    }

    private void setIndexValueColor(String indexValue, TextView textView) {
        if (!TextUtils.isEmpty(indexValue)) {
            textView.setTextColor(ContextCompat.getColorStateList(getActivity(), indexValue.startsWith("-") ? R.color.decrease_income_color : R.color.stock_red));
        }
    }

    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        private MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            myHandler.postDelayed(runnable, 5 * DateUtils.SECOND_IN_MILLIS);
        }
    }
}
