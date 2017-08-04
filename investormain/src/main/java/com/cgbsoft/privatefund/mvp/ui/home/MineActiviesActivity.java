package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.MineActivitesListAdapter;
import com.cgbsoft.privatefund.model.MineActivitesModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineActivitesContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MineActivitesPresenter;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.chenenyu.router.annotation.Route;

import java.util.List;

import app.privatefund.com.im.MessageListActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
@Route(RouteConfig.GOTO_MINE_ACTIVITY)
public class MineActiviesActivity extends BaseActivity<MineActivitesPresenter> implements MineActivitesContract.View, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;

    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;

    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout switpToLoadLayout;

    @BindView(R.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;

    @BindView(R.id.title_left)
    ImageView imageViewLeft;

    @BindView(R.id.iv_title_right)
    ImageView imageViewRight;

    @BindView(R.id.empty_ll)
    LinearLayout emptyLinearLayout;

    private UnreadInfoNumber unreadInfoNumber;


    private static final int PAGE_LIMIT = 20;
    public static final String INIT_LIST_DATA_PARAMS = "list_data_params";
    private LinearLayoutManager linearLayoutManager;
    public MineActivitesListAdapter mineActivitesListAdapter;

    //标记第几页的位置
    private int CurrentPostion = 0;
    //标记是否是架子啊更多
    private boolean isLoadMore;

    @Override
    protected int layoutID() {
        return R.layout.activity_mine_activites;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitleView();
        mineActivitesListAdapter = new MineActivitesListAdapter(this);
        swipeTarget.setAdapter(mineActivitesListAdapter);
        switpToLoadLayout.setOnLoadMoreListener(this);
        switpToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new SimpleItemDecoration(this, R.color.app_bg, R.dimen.ui_10_dip));
        mineActivitesListAdapter.setOnItemClickListener((position, mineActivitesItem) -> {
            Intent intent = new Intent(this, RightShareWebViewActivity.class);
            intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.activitesDeatil.concat("?id=").concat(mineActivitesItem.getId()));
            intent.putExtra(WebViewConstant.push_message_title, mineActivitesItem.getTitle());
            intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
            startActivity(intent);
        });
        getPresenter().getActivitesList(mineActivitesListAdapter, true);
        unreadInfoNumber = new UnreadInfoNumber(this, imageViewRight, true);
    }

    private void initTitleView() {
        imageViewLeft.setVisibility(View.VISIBLE);
        imageViewRight.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.title_mid)).setText("我的活动");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.initUnreadInfoAndPosition();
        }
     }

    @OnClick(R.id.title_left)
    public void backActivity() {
        finish();
    }

    @OnClick(R.id.iv_title_right)
    public void gotoConversation() {
        NavigationUtils.startActivity(this, MessageListActivity.class);
    }

    @Override
    protected MineActivitesPresenter createPresenter() {
        return new MineActivitesPresenter(this, this);
    }

    public void FreshAp(List<MineActivitesModel.ActivitesItem> activitesItemList, boolean isAdd) {
        mineActivitesListAdapter.refrushData(activitesItemList, !isAdd);
    }

    @Override
    public void onLoadMore() {
        CurrentPostion = CurrentPostion + 1;
        if (mineActivitesListAdapter != null) {
            getPresenter().getActivitesList(mineActivitesListAdapter, false);
        }
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = true;
        if (mineActivitesListAdapter != null) {
            getPresenter().getActivitesList(mineActivitesListAdapter, true);
        }
    }

    @Override
    public void requestDataSuccess(boolean isRef) {
        clodLsAnim(switpToLoadLayout);
        isLoadMore = false;
        if (isRef) {
            switpToLoadLayout.setVisibility(mineActivitesListAdapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);
            emptyLinearLayout.setVisibility(mineActivitesListAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void requestDataFailure(String errMsg) {
        clodLsAnim(switpToLoadLayout);
        isLoadMore = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.onDestroy();
        }
    }
}
