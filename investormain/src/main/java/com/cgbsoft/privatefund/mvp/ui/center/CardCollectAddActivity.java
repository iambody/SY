package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.CardListAdapter;
import com.cgbsoft.privatefund.mvp.contract.center.CardCollectContract;
import com.cgbsoft.privatefund.mvp.presenter.center.CardCollectPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY;
import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY_ADD;

/**
 * Created by fei on 2017/8/15.
 */

public class CardCollectAddActivity extends BaseActivity<CardCollectPresenterImpl> implements CardCollectContract.CardCollectView,OnRefreshListener {

    private LoadingDialog mLoadingDialog;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R.id.iv_card_list_empty)
    RelativeLayout emptyBg;

    private List<CardListEntity.CardBean> datas = new ArrayList<>();
    private CardListAdapter adapter;
    private String indentityCode;
    private Observable<Integer> register;

    @Override
    public void showLoadDialog() {
        try {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            mLoadingDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void getCardListSuccess(List<CardListEntity.CardBean> cardBeans) {
        clodLsAnim(mRefreshLayout);
        datas.clear();
        datas.addAll(cardBeans);
        if (datas.size() == 0) {
            emptyBg.setVisibility(View.VISIBLE);
        } else {
            emptyBg.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getCardListError(Throwable error) {
        clodLsAnim(mRefreshLayout);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_cardcollect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
        titleTV.setText(getResources().getString(R.string.card_collect_add));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        initView(savedInstanceState);
        register = RxBus.get().register(SELECT_INDENTITY_ADD, Integer.class);
        register.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                CardCollectAddActivity.this.finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != register) {
            RxBus.get().unregister(SELECT_INDENTITY_ADD,register);
        }
    }
    private void initView(Bundle savedInstanceState) {
        indentityCode = getIntent().getStringExtra("indentityCode");
        mRefreshLayout.setLoadMoreEnabled(false);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CardListAdapter(datas, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new CardListAdapter.CardListItemClick() {
            @Override
            public void itemClick(int position, CardListEntity.CardBean cardBean) {
                dealClick(cardBean);
            }
        });
    }

    private void dealClick(CardListEntity.CardBean cardBean) {
        Intent intent = new Intent(this, UploadIndentityCradActivity.class);
        intent.putExtra("credentialCode", cardBean.getCode());
        intent.putExtra("indentityCode", indentityCode);
        intent.putExtra("title", cardBean.getName());
        startActivity(intent);
//        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getCardListAdd(indentityCode);
    }
    @Override
    protected CardCollectPresenterImpl createPresenter() {
        return new CardCollectPresenterImpl(this,this);
    }

    @Override
    public void onRefresh() {
        getPresenter().getCardListAdd(indentityCode);
    }
}
