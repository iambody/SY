package com.cgbsoft.adviser.mvp.ui.college;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.cgbsoft.adviser.R;
import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.contract.CollegeContract;
import com.cgbsoft.adviser.mvp.presenter.CollegePresenter;
import com.cgbsoft.adviser.mvp.ui.college.adapter.CollegeAdapter;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.adviser.mvp.ui.college.model.CollegeModel;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import java.util.List;

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


    @Override
    protected int layoutID() {
        return R.layout.fragment_college;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        toolbar.setOnMenuItemClickListener(this);

        collegeAdapter = new CollegeAdapter(this);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
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

    }

    @Override
    public void onErrorClickListener() {

    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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
    public void getCollegeHeadDataSucc(List<CollegeModel> modelList, boolean isRef) {

    }

    @Override
    public void getCollegeHeadDataFail(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
    }

}
