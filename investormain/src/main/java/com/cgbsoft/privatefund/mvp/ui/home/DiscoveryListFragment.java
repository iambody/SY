package com.cgbsoft.privatefund.mvp.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.DiscoveryListModel;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.TrackingDiscoveryDataStatistics;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.DiscoveryListAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverListContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryListPresenter;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class DiscoveryListFragment extends BaseLazyFragment<DiscoveryListPresenter> implements DiscoverListContract.View, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    public static final String INIT_LIST_DATA_PARAMS = "list_data_params";
    @BindView(R.id.fragment_videoschool_noresult)
    ImageView fragmentVideoschoolNoresult;
    @BindView(R.id.fragment_videoschool_noresult_lay)
    RelativeLayout fragmentVideoschoolNoresultLay;

    private LinearLayoutManager linearLayoutManager;
    private DiscoveryFragment discoveryFragment;

    /**
     * 类别的数据
     */
    public String CatoryValue;
    public DiscoveryListAdapter discoveryListAdapter;
    private int CurrentPostion = 0;
    private static int LIMIT_PAGE = 20;

    private boolean isLoadMore;

    public DiscoveryListFragment() {
    }

    @SuppressLint("ValidFragment")
    public DiscoveryListFragment(DiscoveryFragment discoveryFragment, String postionString) {
        super();
        this.CatoryValue = postionString;
        this.discoveryFragment = discoveryFragment;
    }

    @Override
    protected void create(Bundle Mybundle) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_fragment_discovery_list;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @Override
    protected void onFirstUserVisible() {
        LogUtils.Log("fffa", "第一次可见:" + CatoryValue);
        List<DiscoveryListModel> list = getArguments() != null ? getArguments().getParcelableArrayList(INIT_LIST_DATA_PARAMS) : null;
        discoveryListAdapter = new DiscoveryListAdapter(fBaseActivity, list == null ? new ArrayList<>() : list);
        CurrentPostion = CollectionUtils.isEmpty(list) ? 0 : 1;
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(fBaseActivity);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new SimpleItemDecoration(fBaseActivity, R.color.app_split_line, R.dimen.ui_z_dip));
        discoveryListAdapter.setOnItemClickListener((position, discoveryListModel) -> {
            HashMap<String, String> hashMap1 = new HashMap<>();
            hashMap1.put(WebViewConstant.push_message_url, CwebNetConfig.discoveryDetail.concat("?id=").concat(discoveryListModel.getId()).concat("&category=").concat(discoveryListModel.getCategory()));
            hashMap1.put(WebViewConstant.push_message_title, discoveryListModel.getLabel());
            NavigationUtils.startActivity(getActivity(), RightShareWebViewActivity.class, hashMap1);
            DataStatistApiParam.operatePrivateBankDiscoverDetailClick(discoveryListModel.getTitle(), discoveryListModel.getLabel());
            TrackingDiscoveryDataStatistics.gotoDiscoveryDetail(getContext(), discoveryListModel.getTitle());
        });
        swipeTarget.setAdapter(discoveryListAdapter);
        if (null == list) {
            getPresenter().getDiscoveryListData(String.valueOf(CurrentPostion * LIMIT_PAGE), CatoryValue);
        }
    }

    @Override
    protected void onUserVisible() {
        LogUtils.Log("fffa", "可见:" + CatoryValue);
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected void DetoryViewAndThing() {
        LogUtils.Log("fffa", "销毁:" + CatoryValue);
    }


    @Override
    protected DiscoveryListPresenter createPresenter() {
        return new DiscoveryListPresenter(fBaseActivity, this);
    }

    public void FreshAp(List<DiscoveryListModel> disCoveryListModels, boolean isAdd) {
        discoveryListAdapter.refrushData(disCoveryListModels, !isAdd);
    }

    @Override
    public void onLoadMore() {
        CurrentPostion = CurrentPostion + 1;
        isLoadMore = true;
        getPresenter().getDiscoveryListData(String.valueOf(CurrentPostion * LIMIT_PAGE), CatoryValue);
        DataStatistApiParam.operatePrivateBankDiscoverDownLoadClick();
        TrackingDiscoveryDataStatistics.discoveryUpload(getContext());
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = false;
        getPresenter().getDiscoveryListData(String.valueOf(CurrentPostion * LIMIT_PAGE), CatoryValue);
        if (discoveryFragment != null) {
            discoveryFragment.refrushListData();
        }
        DataStatistApiParam.operatePrivateBankDiscoverUpRefrushClick();
        TrackingDiscoveryDataStatistics.discoveryDownRefresh(getContext());
    }

    @Override
    public void requestListDataSuccess(List<DiscoveryListModel> discoveryListModel) {
        if (View.GONE == swipeToLoadLayout.getVisibility()) {//一直显示
            swipeToLoadLayout.setVisibility(View.VISIBLE);
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }
        if (View.VISIBLE == fragmentVideoschoolNoresultLay.getVisibility()) {//一直隐藏
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }

        clodLsAnim(swipeToLoadLayout);
        FreshAp(discoveryListModel, isLoadMore);
        isLoadMore = false;
    }

    @Override
    public void requestListDataFailure(String errMsg) {
        clodLsAnim(swipeToLoadLayout);
        if (!isLoadMore && 0 == discoveryListAdapter.getItemCount()) {
            fragmentVideoschoolNoresultLay.setVisibility(View.VISIBLE);
            swipeToLoadLayout.setVisibility(View.GONE);
        }
        isLoadMore = false;
    }

    @OnClick(R.id.fragment_videoschool_noresult)
    public void onViewnoresultClicked() {
        if (NetUtils.isNetworkAvailable(fBaseActivity)) {//有网
            if (discoveryListAdapter != null && discoveryListAdapter.getItemCount() == 0) {
                getPresenter().getDiscoveryListData(String.valueOf(CurrentPostion * LIMIT_PAGE), CatoryValue);
            }
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(app.privatefund.com.vido.R.string.error_net));
        }
    }

}
