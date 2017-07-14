package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.OldSalonsAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.OldSalonsContract;
import com.cgbsoft.privatefund.mvp.presenter.home.OldSalonsPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public class OldSalonsActivity extends BaseActivity<OldSalonsPresenterImpl> implements OldSalonsContract.OldSalonsView,OnRefreshListener,OnLoadMoreListener{
    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    private LoadingDialog mLoadingDialog;

    private List<OldSalonsEntity.SalonItemBean> salons = new ArrayList<>();
    private OldSalonsAdapter salonsAdapter;
    private int offset=0;
    private int limit=20;
    private boolean isOver;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }
    @Override
    protected int layoutID() {
        return R.layout.activity_oldsalons;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.oldsalons_activities));
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setLoadMoreEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleItemDecoration(baseContext,android.R.color.transparent,R.dimen.ui_10_dip));
        salonsAdapter = new OldSalonsAdapter(baseContext, salons);
        recyclerView.setAdapter(salonsAdapter);
        salonsAdapter.setOnItemClickListener(new OldSalonsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, OldSalonsEntity.SalonItemBean bean) {

            }
        });
        getPresenter().getOldSalons(offset,limit);
    }

    @Override
    protected OldSalonsPresenterImpl createPresenter() {
        return new OldSalonsPresenterImpl(baseContext,this);
    }

    @Override
    public void showLoadDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void getDataSuccess(List<OldSalonsEntity.SalonItemBean> oldSalons) {
        clodLsAnim(mRefreshLayout);
        mRefreshLayout.setLoadMoreEnabled(true);
        if (null == oldSalons || oldSalons.size() == 0) {
            offset--;
            offset=offset<=0?0:offset;
            if (oldSalons.size() == 0) {
                isOver=true;
            }
            return;
        }

        if (offset == 0) {//下拉刷新成功
            salons.clear();
        }
        salons.addAll(oldSalons);
        salonsAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(Throwable error) {
        clodLsAnim(mRefreshLayout);
        if (offset == 0) {//下拉刷新错误

        } else {//上拉加载错误
            offset--;
        }
    }

    @Override
    public void onLoadMore() {
        offset++;
        getPresenter().getOldSalons(offset,limit);
    }

    @Override
    public void onRefresh() {
        offset=0;
        mRefreshLayout.setLoadMoreEnabled(false);
        getPresenter().getOldSalons(offset,limit);
    }
}
