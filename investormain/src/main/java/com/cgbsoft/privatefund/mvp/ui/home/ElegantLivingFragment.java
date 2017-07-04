package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.RecyclerAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.ElegantLivingContract;
import com.cgbsoft.privatefund.mvp.presenter.home.ElegantLivingPresenterImpl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 生活家页面
 * Created by sunfei on 2017/6/29 0029.
 */

public class ElegantLivingFragment extends BaseFragment<ElegantLivingPresenterImpl> implements ElegantLivingContract.ElegantLivingView,RecyclerRefreshLayout.OnRefreshListener,RecyclerControl.OnControlGetDataListListener {
    @BindView(R.id.elegant_lining_rv)
    RecyclerView recyclerView;
    @BindView(R.id.elegant_layout_swipe_refresh)
    RecyclerRefreshLayout mRefreshLayout;
    private LoadingDialog mLoadingDialog;
    private ArrayList<ElegantLivingEntity.ElegantLivingBean> datas;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerControl recyclerControl;
    private int pageNo=0;
    private boolean isOver;

    @Override
    protected int layoutID() {
        return R.layout.elegantliving_fragment;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        datas = new ArrayList<>();
        mRefreshLayout.setOnRefreshListener(this);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
//        mRefreshLayout.setColorSchemeResources(
//                R.color.colorGreen,
//                R.color.colorYellow,
//                R.color.colorRed
//        );
        recyclerAdapter = new RecyclerAdapter(baseActivity,datas);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleItemDecoration(baseActivity,android.R.color.transparent,R.dimen.ui_10_dip));
        recyclerControl = new RecyclerControl(mRefreshLayout, linearLayoutManager, this);
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,ElegantLivingEntity.ElegantLivingBean elegantLivingBean) {
                //TODO banner点击事件处理
                gotoBannerDetail(elegantLivingBean);
            }
        });
        getPresenter().getElegantLivingBanners(pageNo);
        mRefreshLayout.setRefreshing(true);
    }

    private void gotoBannerDetail(ElegantLivingEntity.ElegantLivingBean elegantLivingBean) {
        HashMap hashMap = new HashMap();
//        hashMap.put(WebViewConstant.PAGE_SHOW_TITLE, true);
        hashMap.put(WebViewConstant.RIGHT_SHARE, true);
        hashMap.put(WebViewConstant.push_message_title, elegantLivingBean.getTitle());
        hashMap.put(WebViewConstant.push_message_url, elegantLivingBean.getUrl());
        NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
    }

    @Override
    protected ElegantLivingPresenterImpl createPresenter() {
        return new ElegantLivingPresenterImpl(baseActivity,this);
    }

    /**
     * 显示loading弹窗
     */
    @Override
    public void showLoadDialog() {
//        mLoadingDialog.show();
    }

    /**
     * 隐藏loading弹窗
     */
    @Override
    public void hideLoadDialog() {
//        mLoadingDialog.dismiss();
    }

    @Override
    public void updateUi(List<ElegantLivingEntity.ElegantLivingBean> data) {
        if (null == data||data.size() == 0) {
            pageNo-=LOAD_ELEGANT_LIVING_BANNER_lIMIT;
            pageNo=pageNo<0?0:pageNo;
            if (data.size() == 0) {
                isOver=true;
            }
            if (pageNo == 0) {
                recyclerControl.getDataComplete(true);
            } else {
                recyclerControl.getDataComplete(false);
            }
            return;
        }
        if (data.size() < Constant.LOAD_ELEGANT_LIVING_BANNER_lIMIT) {
            isOver=true;
        }
        boolean isRef;
        if (pageNo == 0) {
            datas.clear();
            datas.addAll(data);
            isRef=true;
        } else {
            datas.addAll(data);
            isRef=false;
        }
        recyclerAdapter.notifyDataSetChanged();
        recyclerControl.getDataComplete(isRef);
    }

    @Override
    public void updateError() {
        boolean isRef;
        if (pageNo == 0) {
            isRef=true;
        } else {
            isRef=false;
        }
        recyclerControl.getDataComplete(isRef);
    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }

    /**
     * 下拉刷新和上拉加载更多
     * @param isRef true 下拉刷新  false上拉加载更多
     */
    @Override
    public void onControlGetDataList(boolean isRef) {
        if (isRef) {
            pageNo=0;
            isOver=false;
        } else {
            if (isOver) {
                Toast.makeText(baseActivity.getApplicationContext(),"已经加载全部",Toast.LENGTH_SHORT).show();
                recyclerControl.getDataComplete(isRef);
                return;
            }
            pageNo+=LOAD_ELEGANT_LIVING_BANNER_lIMIT;
        }
        getPresenter().getElegantLivingBanners(pageNo);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }
}
