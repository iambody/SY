package com.cgbsoft.adviser.mvp.ui.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cgbsoft.adviser.R;
import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.contract.CollegeContract;
import com.cgbsoft.adviser.mvp.presenter.CollegePresenter;
import com.cgbsoft.adviser.mvp.ui.college.adapter.CollegeAdapter;
import com.cgbsoft.adviser.mvp.ui.college.holder.CollegeHeadHolder;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.adviser.mvp.ui.college.model.CollegeModel;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.mvp.ui.video.VideoDetailActivity;
import com.cgbsoft.lib.mvp.ui.video.VideoDownloadListActivity;
import com.cgbsoft.lib.mvp.ui.video.VideoHistoryListActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;

/**
 * 学院
 * Created by xiaoyu.zhang on 2016/11/15 14:41
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeFragment extends BaseFragment<CollegePresenter> implements CollegeContract.View, CollegeListener, RecyclerRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, RecyclerControl.OnControlGetDataListListener {
    @BindView(R2.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.recyclerRefreshLayout)
    RecyclerRefreshLayout recyclerRefreshLayout;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    private CollegeAdapter collegeAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerControl recyclerControl;
    private String isColorCloud, organizationName;


    @Override
    protected int layoutID() {
        return R.layout.fragment_college;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        isColorCloud = SPreference.isColorCloud(getContext().getApplicationContext());
        UserInfoDataEntity.ToBBean toBBean = SPreference.getToBBean(getContext().getApplicationContext());
        organizationName = toBBean == null ? "" : toBBean.organizationName;
        setHasOptionsMenu(true);
        ((RxAppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((RxAppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((RxAppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        }
        toolbar.setOnMenuItemClickListener(this);

        collegeAdapter = new CollegeAdapter(this);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position < 2 || position == 6 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(collegeAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerRefreshLayout.setOnRefreshListener(this);
        recyclerControl = new RecyclerControl(recyclerRefreshLayout, gridLayoutManager, this);
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());

        onRefresh();
    }

    @Override
    protected CollegePresenter createPresenter() {
        return new CollegePresenter(getContext(), this);
    }


    @Override
    public void onHeadBtnClick(int which) {
        String[] param = new String[3];
        param[1] = isColorCloud;
        param[2] = organizationName;

        Intent intent = new Intent(getContext(), VideoListActivity.class);
        switch (which) {
            case CollegeHeadHolder.PRODUCT://产品培训
                param[0] = "产品培训";
                toDataStatistics(1019, 10097, param);
                break;
            case CollegeHeadHolder.FOREFRONT://财富前沿
                param[0] = "财富前沿";
                toDataStatistics(1019, 10098, param);
                break;
            case CollegeHeadHolder.MANAGER://管理培训
                param[0] = "管理培训";
                toDataStatistics(1019, 10099, param);
                break;
            case CollegeHeadHolder.KNOWLEDGE://金融知识
                param[0] = "金融知识";
                toDataStatistics(1019, 10100, param);
                break;
        }
        intent.putExtra("title", param[0]);
        startActivity(intent);
    }

    @Override
    public void onTitleClick() {
        Intent intent = new Intent(getContext(), VideoListActivity.class);
        intent.putExtra("title", "视频列表");
        startActivity(intent);
    }

    @Override
    public void onFirstVideoClick(ImageView iv_ich_bg) {
        if (collegeAdapter.getList().size() == 0) {
            return;
        }
        CollegeModel model = collegeAdapter.getList().get(0);
        if (model != null) {
            Intent intent = new Intent(getContext(), VideoDetailActivity.class);
            intent.putExtra("videoId", model.videoId);
            intent.putExtra("videoCoverUrl", model.headBgUrl);
            ActivityTransitionLauncher.with(getActivity()).from(iv_ich_bg).launch(intent);
        }
    }

    @Override
    public void onGridItemClick(int position, ImageView iv_icb_bg) {
        if (collegeAdapter.getList().size() == 0) {
            return;
        }
        CollegeModel model = collegeAdapter.getList().get(position);
        if (model != null) {
            Intent intent = new Intent(getContext(), VideoDetailActivity.class);
            intent.putExtra("videoId", model.videoId);
            intent.putExtra("videoCoverUrl", model.bottomVideoImgUrl);
            ActivityTransitionLauncher.with(getActivity()).from(iv_icb_bg).launch(intent);
        }
    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Class clazz = null;
        int id = item.getItemId();
        if (id == R.id.firstBtn) {
            clazz = VideoHistoryListActivity.class;
        } else if (id == R.id.secondBtn) {
            clazz = VideoDownloadListActivity.class;
        }
        if (clazz != null)
            openActivity(clazz);
        return false;
    }

    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getCollegeData(collegeAdapter, isRef);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }


    @Override
    public void getCollegeDataSucc(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(getContext(), false, collegeAdapter, new CollegeModel(), "", R.drawable.bg_no_data);
    }

    @Override
    public void getCollegeDataFail(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(getContext(), true, collegeAdapter, new CollegeModel());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            inflater.inflate(R.menu.page_menu, menu);
            MenuItem firstItem = menu.findItem(R.id.firstBtn);
            MenuItem secItem = menu.findItem(R.id.secondBtn);
            firstItem.setTitle(R.string.menu_play_history_str);
            secItem.setTitle(R.string.menu_download_video_str);
            firstItem.setIcon(R.drawable.ic_cache_list);
            secItem.setIcon(R.drawable.ic_download);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerControl.destory();
    }
}
