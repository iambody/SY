package com.cgbsoft.adviser.mvp.ui.college;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgbsoft.adviser.R;
import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.contract.VideoListContract;
import com.cgbsoft.adviser.mvp.presenter.VideoListPresenter;
import com.cgbsoft.adviser.mvp.ui.college.adapter.VideoListAdapter;
import com.cgbsoft.adviser.mvp.ui.college.listener.VideoListListener;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import butterknife.BindView;

/**
 * 视频列表
 * Created by xiaoyu.zhang on 2016/12/7 15:01
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoListActivity extends BaseActivity<VideoListPresenter> implements VideoListContract.View, VideoListListener, RecyclerRefreshLayout.OnRefreshListener, RecyclerControl.OnControlGetDataListListener {
    @BindView(R2.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.tv_avl_title)
    TextView tv_avl_title;

    @BindView(R2.id.recyclerRefreshLayout)
    RecyclerRefreshLayout recyclerRefreshLayout;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;


    private RecyclerControl recyclerControl;
    private VideoListAdapter videoListAdapter;
    private GridLayoutManager gridLayoutManager;
    private String title;

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void getVideoListDataSucc(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
    }

    @Override
    public void getVideoListDataFail(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        title = getIntent().getStringExtra("title");
        tv_avl_title.setText(title);
        toolbar.setNavigationOnClickListener(v -> finish());

        videoListAdapter = new VideoListAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(videoListAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerRefreshLayout.setOnRefreshListener(this);
        recyclerControl = new RecyclerControl(recyclerRefreshLayout, gridLayoutManager, this);
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());

        onRefresh();
    }

    @Override
    protected VideoListPresenter createPresenter() {
        return new VideoListPresenter(this, this);
    }

    @Override
    public void onVideoListItemClick(int position) {

    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getVideoListData(videoListAdapter, title, isRef);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }
}
