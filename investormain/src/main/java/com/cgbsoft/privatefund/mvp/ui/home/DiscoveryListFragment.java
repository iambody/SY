package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;
import com.cgbsoft.privatefund.adapter.DiscoveryListAdapter;
import com.cgbsoft.privatefund.model.DiscoverModel;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverListContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryListPresenter;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryPresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.bean.VideoAllModel;
import app.privatefund.com.vido.mvp.contract.video.VideoListContract;
import butterknife.BindView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-18:08
 */
public class DiscoveryListFragment extends BaseLazyFragment<DiscoveryListPresenter> implements DiscoverListContract.View, OnLoadMoreListener, OnRefreshListener {
    @BindView(R2.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;
    @BindView(R2.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R2.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private LinearLayoutManager linearLayoutManager;

    /**
     * 类别的数据
     */
    public String CatoryValue;
    private String currentOffset;

    public DiscoveryListAdapter discoveryListAdapter;

    //标记第几页的位置
    private int CurrentPostion = 0;
    //标记是否是架子啊更多

    private boolean isLoadMore;


    public DiscoveryListFragment(String postionString) {
        super();
        this.CatoryValue = postionString;
    }

    @Override
    protected void create(Bundle Mybundle) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_fragment_video_ls;
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }

    @Override
    protected void onFirstUserVisible() {
        LogUtils.Log("fffa", "第一次可见:" + CatoryValue);
        discoveryListAdapter = new DiscoveryListAdapter(fBaseActivity);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(fBaseActivity);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new SimpleItemDecoration(fBaseActivity, R.color.gray_font, R.dimen.ui_1_dip));

        discoveryListAdapter.setOnItemClickListener(new ListItemClickListener<DiscoverModel.DiscoveryListModel>() {
            @Override
            public void onItemClick(int position, DiscoverModel.DiscoveryListModel discoveryListModel) {
//                NavigationUtils.startVideoInformationActivityu(fBaseActivity, discoveryListModel.getId(), discoveryListModel.getTitle());
            }
        });
        swipeTarget.setAdapter(discoveryListAdapter);
        //第一次显示的时候全部不需要加载数据  非全部需要进行请求网络数据
        if (0 == discoveryListAdapter.getItemCount()) {//
            getPresenter().getDiscoveryListData("0", CatoryValue);
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

    public void FreshAp(List<DiscoverModel.DiscoveryListModel> disCoveryListModels, boolean isAdd) {
        discoveryListAdapter.setData(disCoveryListModels, !isAdd);
    }

    @Override
    public void onLoadMore() {
        CurrentPostion = CurrentPostion + 1;
        getPresenter().getDiscoveryListData("0", CatoryValue);
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = true;
        getPresenter().getDiscoveryListData("0",CatoryValue);
    }

    @Override
    public void requestListDataSuccess(List<DiscoverModel.DiscoveryListModel> discoveryListModel) {
        clodLsAnim(swipeToLoadLayout);
        FreshAp(discoveryListModel, isLoadMore);
        isLoadMore = false;
    }

    @Override
    public void requestListDataFailure(String errMsg) {
        clodLsAnim(swipeToLoadLayout);
        isLoadMore = false;
    }
}
