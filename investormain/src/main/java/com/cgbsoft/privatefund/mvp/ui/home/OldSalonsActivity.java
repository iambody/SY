package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.OldSalonsAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.OldSalonsContract;
import com.cgbsoft.privatefund.mvp.presenter.home.OldSalonsPresenterImpl;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
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
    @BindView(R.id.salon_no_data_all)
    RelativeLayout noDataAll;
    @BindView(R.id.salons_no_data_tag)
    ImageView noDataPic;
    @BindView(R.id.tv_salons_no_data_tip)
    TextView noDataStr;

    private List<OldSalonsEntity.SalonItemBean> salons = new ArrayList<>();
    private OldSalonsAdapter salonsAdapter;
    private final int LIMIT_SALONS=8;
    private int limit=LIMIT_SALONS;
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
                //活动详情
                gotoSalonDetail(bean);
            }
        });
        getPresenter().getOldSalons(salons.size(),limit);
    }
    /**
     * 跳转到沙龙详情页面
     * @param bean
     */
    private void gotoSalonDetail(OldSalonsEntity.SalonItemBean bean) {
        Intent intent = new Intent(this, RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_title, bean.getTitle());
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.salonDetail.concat(bean.getId()));
        startActivity(intent);
//        HashMap hashMap = new HashMap();
//        hashMap.put(WebViewConstant.RIGHT_SHARE, true);
//        hashMap.put(WebViewConstant.push_message_title, bean.getTitle());
//        hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.elegantGoodsDetail+bean.getId());
//        NavigationUtils.startActivityByRouter(baseContext, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
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
        noDataAll.setVisibility(View.GONE);
        clodLsAnim(mRefreshLayout);
        mRefreshLayout.setLoadMoreEnabled(true);
        if (salons.size() == 0 || salons.size() < LIMIT_SALONS) {
            isOver = true;
        }
        salons.addAll(oldSalons);
        if (salons.size() == 0) {
            mRefreshLayout.setLoadMoreEnabled(false);
            noDataAll.setVisibility(View.VISIBLE);
        } else {
            noDataAll.setVisibility(View.GONE);
        }
        salonsAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataError(Throwable error) {
        clodLsAnim(mRefreshLayout);
        noDataAll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMore() {
        if (isOver) {
            Toast.makeText(baseContext.getApplicationContext(), "已经加载全部", Toast.LENGTH_SHORT).show();
            clodLsAnim(mRefreshLayout);
            return;
        }
        getPresenter().getOldSalons(salons.size(),limit);
    }

    @Override
    public void onRefresh() {
        salons.clear();
        isOver=false;
        mRefreshLayout.setLoadMoreEnabled(false);
        getPresenter().getOldSalons(salons.size(),limit);
    }
}
