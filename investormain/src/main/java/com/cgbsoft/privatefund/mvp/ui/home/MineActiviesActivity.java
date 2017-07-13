package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.MineActivitesListAdapter;
import com.cgbsoft.privatefund.model.MineActivitesModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineActivitesContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MineActivitesPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * @author chenlong
 */
public class MineActiviesActivity extends BaseActivity<MineActivitesPresenter> implements MineActivitesContract.View, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private static final int PAGE_LIMIT = 20;

    public static final String INIT_LIST_DATA_PARAMS = "list_data_params";
    private LinearLayoutManager linearLayoutManager;
    public MineActivitesListAdapter mineActivitesListAdapter;

    //标记第几页的位置
    private int CurrentPostion = 0;
    //标记是否是架子啊更多

    private boolean isLoadMore;

    @Override
    protected int layoutID() {
        return R.layout.activity_mine_activites;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleView();
        mineActivitesListAdapter = new MineActivitesListAdapter(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new SimpleItemDecoration(this, R.color.gray_font, R.dimen.ui_1_dip));
        swipeTarget.setAdapter(mineActivitesListAdapter);
        mineActivitesListAdapter.setOnItemClickListener(new ListItemClickListener<MineActivitesModel.ActivitesItem>() {
            @Override
            public void onItemClick(int position, MineActivitesModel.ActivitesItem activitesItem) {
            }
        });
        getPresenter().getActivitesList(mineActivitesListAdapter, true);
    }

    private void initTitleView() {
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.title_mid)).setText("我的活动");
    }

    @Override
    protected MineActivitesPresenter createPresenter() {
        return new MineActivitesPresenter(this, this);
    }

    public void FreshAp(List<MineActivitesModel.ActivitesItem> activitesItemList, boolean isAdd) {
        mineActivitesListAdapter.refrushData(activitesItemList, !isAdd);
    }

    @Override
    public void onLoadMore() {
        CurrentPostion = CurrentPostion + 1;
        if (mineActivitesListAdapter != null) {
            getPresenter().getActivitesList(mineActivitesListAdapter, false);
        }
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = true;
        if (mineActivitesListAdapter != null) {
            getPresenter().getActivitesList(mineActivitesListAdapter, true);
        }
    }

    @Override
    public void requestDataSuccess(boolean isRef) {
        clodLsAnim(swipeToLoadLayout);
        isLoadMore = false;
    }

    @Override
    public void requestDataFailure(String errMsg) {
        clodLsAnim(swipeToLoadLayout);
        isLoadMore = false;
    }
}
