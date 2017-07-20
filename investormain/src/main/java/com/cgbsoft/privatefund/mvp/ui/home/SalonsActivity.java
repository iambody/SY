package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.dialog.WheelDialogCity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.SalonsAdapter;
import com.cgbsoft.privatefund.bean.location.LocationBean;
import com.cgbsoft.privatefund.mvp.contract.home.SalonsContract;
import com.cgbsoft.privatefund.mvp.presenter.home.SalonsPresenterImpl;
import com.chenenyu.router.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动沙龙
 * Created by sunfei on 2017/7/13 0013.
 */
@Route(RouteConfig.GOTO_SALONS_ACTIVITY)
public class SalonsActivity extends BaseActivity<SalonsPresenterImpl> implements SalonsContract.SalonsView, OnRefreshListener,OnLoadMoreListener {

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R.id.ll_salon_city_all)
    LinearLayout salonCityAll;
    @BindView(R.id.tv_salon_city)
    TextView salonCity;
    private LoadingDialog mLoadingDialog;
    private List<SalonsEntity.SalonItemBean> salons = new ArrayList<>();
    private SalonsAdapter salonsAdapter;
    private String cityCode = "全国";
    private final int LIMIT_SALONS=8;
    private int limit = LIMIT_SALONS;
    private List<SalonsEntity.CityBean> citys=new ArrayList<>();
    private boolean isOver;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_salons;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.salons_activities));
        salonCityAll.setVisibility(View.VISIBLE);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        salonCity.setText(cityCode);
        LocationBean location = AppManager.getLocation(baseContext);
        if (null != location && !TextUtils.isEmpty(location.getLocationcity())) {
            cityCode = location.getLocationcity();
        }
        if (TextUtils.isEmpty(cityCode)) {
            cityCode="全国";
        }
        addBeanOfState();
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setLoadMoreEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleItemDecoration(baseContext, android.R.color.transparent, R.dimen.ui_10_dip));

        salonsAdapter = new SalonsAdapter(baseContext, salons);
        salonsAdapter.setOnItemClickListener(new SalonsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SalonsEntity.SalonItemBean bean) {
                String isButton = bean.getIsButton();
                if (TextUtils.isEmpty(isButton) || Integer.parseInt(isButton) == 1) {
                    //过往活动
                    gotoOldActivities();
                } else {
                    //活动详情
                    gotoSalonDetail(bean);
                }
            }
        });
        recyclerView.setAdapter(salonsAdapter);
        getPresenter().getSalonsAndCitys(cityCode, salons.size(), limit);
    }

    private void addBeanOfState() {
        SalonsEntity.CityBean cityBean = new SalonsEntity.CityBean();
        cityBean.setText("全国");
        this.citys.add(cityBean);
    }

    /**
     * 跳转到沙龙详情页面
     * @param bean
     */
    private void gotoSalonDetail(SalonsEntity.SalonItemBean bean) {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
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

    /**
     * 去过往活动页面
     */
    private void gotoOldActivities() {
        Intent intent = new Intent(this, OldSalonsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        salons.clear();
        getPresenter().getSalonsAndCitys(cityCode, salons.size(), limit);
    }

    @Override
    protected SalonsPresenterImpl createPresenter() {
        return new SalonsPresenterImpl(baseContext, this);
    }

    @Override
    public void showLoadDialog() {
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void getDataSuccess(List<SalonsEntity.SalonItemBean> salons, List<SalonsEntity.CityBean> citys) {
        clodLsAnim(mRefreshLayout);
        mRefreshLayout.setLoadMoreEnabled(true);
        if (salons.size() == 0 || salons.size() < LIMIT_SALONS) {
            isOver = true;
        }
        if (this.salons.size() > 0) {
            this.salons.remove(this.salons.size()-1);
        }
        this.salons.addAll(salons);
        if (this.salons.size() == 0) {
            mRefreshLayout.setLoadMoreEnabled(false);
        }
        SalonsEntity.SalonItemBean salonItemBean = new SalonsEntity.SalonItemBean();
        salonItemBean.setIsButton("1");
        this.salons.add(salonItemBean);
//        if (salons.size() == 0) {
//            mRefreshLayout.setLoadMoreEnabled(false);
//        }
        salonsAdapter.notifyDataSetChanged();
        this.citys.clear();
        addBeanOfState();
        this.citys.addAll(citys);
    }

    @Override
    public void getDataError(Throwable error) {
        if (this.salons.size() == 0) {

            SalonsEntity.SalonItemBean salonItemBean = new SalonsEntity.SalonItemBean();
            salonItemBean.setIsButton("1");
            this.salons.add(salonItemBean);
            salonsAdapter.notifyDataSetChanged();
        }
        clodLsAnim(mRefreshLayout);
    }

    @Override
    public void onRefresh() {
        salons.clear();
        isOver=false;
        mRefreshLayout.setLoadMoreEnabled(false);
        getPresenter().getSalonsAndCitys(cityCode, salons.size(), limit);
    }

    @OnClick(R.id.ll_salon_city_all)
    public void showCitySelect() {
//        if (null == citys || citys.size() == 0) {
//            Toast.makeText(getApplicationContext(), "暂无其它城市", Toast.LENGTH_SHORT).show();
//            return;
//        }
        WheelDialogCity wheelDialogCity = new WheelDialogCity(this);
        wheelDialogCity.setList(citys);
        wheelDialogCity.setTitle("请选择城市");
        wheelDialogCity.setConfirmCallback(new WheelDialogCity.ConfirmListenerInteface() {
            @Override
            public void confirm(SalonsEntity.CityBean result) {
                salonCity.setText(result.getText());
                cityCode=result.getText();
                salons.clear();
                getPresenter().getSalonsAndCitys(result.getText(), salons.size(), limit);
            }
        });
        wheelDialogCity.show();
    }

    @Override
    public void onLoadMore() {
        if (isOver) {
            Toast.makeText(baseContext.getApplicationContext(), "已经加载全部", Toast.LENGTH_SHORT).show();
            clodLsAnim(mRefreshLayout);
            return;
        }
        getPresenter().getSalonsAndCitys(cityCode, salons.size()-1, limit);
    }
}
