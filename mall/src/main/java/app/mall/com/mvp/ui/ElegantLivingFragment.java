package app.mall.com.mvp.ui;

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
import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.mall.com.mvp.adapter.RecyclerAdapter;
import app.mall.com.mvp.contract.ElegantLivingContract;
import app.mall.com.mvp.presenter.ElegantLivingPresenterImpl;
import butterknife.BindView;
import qcloud.mall.R;
import qcloud.mall.R2;

import static com.cgbsoft.lib.BaseApplication.getContext;


/**
 * 生活家页面
 * Created by sunfei on 2017/6/29 0029.
 */

public class ElegantLivingFragment extends BaseFragment<ElegantLivingPresenterImpl> implements ElegantLivingContract.ElegantLivingView,
        OnLoadMoreListener, OnRefreshListener {
    @BindView(R2.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R2.id.elegant_living_no_data_all)
    RelativeLayout noDataDefaultAll;
    @BindView(R2.id.elegant_living_no_data_tag)
    ImageView noDataPic;
    @BindView(R2.id.tv_elegant_living_no_data_tip)
    TextView noDataStr;
    @BindView(R2.id.tv_elegant_living_reload_tip)
    TextView noDataReloadStr;
    private LoadingDialog mLoadingDialog;
    private ArrayList<ElegantLivingEntity.ElegantLivingBean> datas = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isOver;

    @Override
    protected int layoutID() {
        return R.layout.elegantliving_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_SHENGHUO_SHJ);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_SHENGHUO_SHJ);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setLoadMoreEnabled(false);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
//        mRefreshLayout.setColorSchemeResources(
//                R.color.colorGreen,
//                R.color.colorYellow,
//                R.color.colorRed
//        );
        recyclerAdapter = new RecyclerAdapter(baseActivity, datas);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleItemDecoration(baseActivity, android.R.color.transparent, R.dimen.ui_10_dip));
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ElegantLivingEntity.ElegantLivingBean elegantLivingBean) {
                gotoBannerDetail(elegantLivingBean);
            }
        });
        getPresenter().getElegantLivingBanners(datas.size());
        DataStatistApiParam.intoElegantLiving();
    }

    private void gotoBannerDetail(ElegantLivingEntity.ElegantLivingBean elegantLivingBean) {
        //进入专题详情页面的埋点
        DataStatistApiParam.clickElegantLivingBanner(elegantLivingBean.getTitle());
        HashMap hashMap = new HashMap();
        hashMap.put(WebViewConstant.RIGHT_SHARE, true);
        hashMap.put(WebViewConstant.push_message_title, getResources().getString(R.string.banner_detail));
        hashMap.put(WebViewConstant.push_message_url, elegantLivingBean.getUrl().concat("?promotionCode=").concat(elegantLivingBean.getCode()));
        NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
    }

    @Override
    protected ElegantLivingPresenterImpl createPresenter() {
        return new ElegantLivingPresenterImpl(baseActivity, this);
    }

    /**
     * 显示loading弹窗
     */
    @Override
    public void showLoadDialog() {
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏loading弹窗
     */
    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void updateUi(List<ElegantLivingEntity.ElegantLivingBean> data) {
        clodLsAnim(mRefreshLayout);
        mRefreshLayout.setLoadMoreEnabled(true);
        if (data.size() == 0 || data.size() < Constant.LOAD_ELEGANT_LIVING_BANNER_lIMIT) {
            isOver = true;
        }
        datas.addAll(data);
        if (datas.size() == 0) {
            mRefreshLayout.setLoadMoreEnabled(false);
            noDataDefaultAll.setVisibility(View.VISIBLE);
            Imageload.display(baseActivity,R.drawable.no_product,noDataPic);
            noDataStr.setText(baseActivity.getResources().getString(R.string.no_product_str));
            noDataReloadStr.setVisibility(View.GONE);
        } else {
            noDataDefaultAll.setVisibility(View.GONE);
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateError() {
        showToast(R.string.load_fail);
        clodLsAnim(mRefreshLayout);
        noDataDefaultAll.setVisibility(View.VISIBLE);
        Imageload.display(baseActivity,R.drawable.net_err_tip,noDataPic);
        noDataStr.setText(baseActivity.getResources().getString(R.string.err_net_str));
        noDataReloadStr.setVisibility(View.VISIBLE);
        noDataReloadStr.setText(baseActivity.getResources().getString(R.string.reload_str));
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.setLoadMoreEnabled(false);
        datas.clear();
        isOver = false;
        getPresenter().getElegantLivingBanners(datas.size());
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        if (isOver) {
            Toast.makeText(baseActivity.getApplicationContext(), "已经加载全部", Toast.LENGTH_SHORT).show();
            clodLsAnim(mRefreshLayout);
            return;
        }
        getPresenter().getElegantLivingBanners(datas.size());
    }

    @Override
    protected void viewBeShow() {
        DataStatistApiParam.intoElegantLiving();
    }

}
