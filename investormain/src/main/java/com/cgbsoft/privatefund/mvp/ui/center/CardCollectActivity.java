package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.CardListAdapter;
import com.cgbsoft.privatefund.mvp.contract.center.CardCollectContract;
import com.cgbsoft.privatefund.mvp.presenter.center.CardCollectPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qcloud.mall.R2;

/**
 * Created by fei on 2017/8/10.
 */

public class CardCollectActivity extends BaseActivity<CardCollectPresenterImpl> implements CardCollectContract.CardCollectView,OnRefreshListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    private LoadingDialog mLoadingDialog;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    private List<CardListEntity.CardBean> datas = new ArrayList<>();
    private CardListAdapter adapter;
    private String indentityCode;

    @Override
    protected int layoutID() {
        return R.layout.activity_cardcollect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        indentityCode = getIntent().getStringExtra("indentityCode");
        titleTV.setText(getResources().getString(R.string.card_collect));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CardListAdapter(datas, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new CardListAdapter.CardListItemClick() {
            @Override
            public void itemClick(int position, CardListEntity.CardBean cardBean) {
                goToUploadPage(cardBean);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getCardList(indentityCode);
    }

    /**
     * 点击条目跳转到上传照片页面
     * @param cardBean
     */
    private void goToUploadPage(CardListEntity.CardBean cardBean) {
        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
        intent.putExtra("credentialCode", cardBean.getCode());
        intent.putExtra("indentityCode", indentityCode);
        intent.putExtra("title", cardBean.getName());
        startActivity(intent);
    }

    @Override
    protected CardCollectPresenterImpl createPresenter() {
        return new CardCollectPresenterImpl(this,this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.page_menu, menu);
//        MenuItem rightItem = menu.findItem(com.cgbsoft.lib.R.id.firstBtn);
//        MenuItem secItem = menu.findItem(com.cgbsoft.lib.R.id.secondBtn);
//        secItem.setVisible(false);
//        rightItem.setTitle("添加");
//        rightItem.setVisible(true);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        if (item.getItemId() == com.cgbsoft.lib.R.id.firstBtn) {
//
//        }
//        return false;
//    }

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
    public void getCardListSuccess(List<CardListEntity.CardBean> cardBeans) {
        clodLsAnim(mRefreshLayout);
        if (null == cardBeans || cardBeans.size() == 0) {
            return;
        }
        datas.clear();
        datas.addAll(cardBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getCardListError(Throwable error) {
        clodLsAnim(mRefreshLayout);
    }

    @Override
    public void onRefresh() {
        getPresenter().getCardList(indentityCode);
    }
}
