package com.cgbsoft.lib.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.mvp.contract.VideoDownloadListContract;
import com.cgbsoft.lib.mvp.presenter.VideoDownloadListPresenter;
import com.cgbsoft.lib.mvp.ui.adapter.VideoDownloadListAdapter;
import com.cgbsoft.lib.mvp.ui.listener.VideoDownloadListListener;
import com.cgbsoft.lib.mvp.ui.model.VideoDownloadListModel;
import com.cgbsoft.lib.mvp.ui.model.VideoHistoryModel;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.recycler.ErrorDataView;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_DOWNLOAD_REF_ONE_OBSERVABE;

/**
 * 视频下载列表
 * Created by xiaoyu.zhang on 2016/12/12 17:30
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListActivity extends BaseActivity<VideoDownloadListPresenter> implements VideoDownloadListContract.View , RecyclerControl.OnControlGetDataListListener, VideoDownloadListListener,
        Toolbar.OnMenuItemClickListener, RecyclerRefreshLayout.OnRefreshListener {
    @BindView(R2.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.tv_title)
    TextView tv_title;

    @BindView(R2.id.recyclerRefreshLayout)
    RecyclerRefreshLayout recyclerRefreshLayout;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R2.id.ll_avd)
    LinearLayout ll_avd;

    @BindView(R2.id.tv_avd_choiceAll)
    TextView tv_avd_choiceAll;

    @BindView(R2.id.tv_avd_delete)
    TextView tv_avd_delete;

    private RecyclerControl recyclerControl;
    private LinearLayoutManager linearLayoutManager;
    private VideoDownloadListAdapter videoDownloadListAdapter;

    private Observable<Integer> refItemObservable;
    private boolean isChoiceAll;

    private MenuItem deleteItem;

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_video_download;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        videoDownloadListAdapter = new VideoDownloadListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerControl = new RecyclerControl(recyclerRefreshLayout, linearLayoutManager, this);
        recyclerRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoDownloadListAdapter);
        recyclerView.setHasFixedSize(true);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> finish());

        tv_title.setText(R.string.local_video_str);
    }

    @Override
    protected void data() {
        super.data();
        refItemObservable = RxBus.get().register(VIDEO_DOWNLOAD_REF_ONE_OBSERVABE, Integer.class);
        refItemObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                getHandler().postDelayed(() -> onControlGetDataList(true), 100);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        onRefresh();
    }

    @OnClick(R2.id.tv_avd_choiceAll)
    void choiceAllClick() {
        isChoiceAll = !isChoiceAll;

        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isCheck = isChoiceAll;
        }
        videoDownloadListAdapter.notifyDataSetChanged();
        if (isChoiceAll) {
            choiceChangeText(list.size());
        } else {
            unChoiceChangeText();
        }
    }

    @OnClick(R2.id.tv_avd_delete)
    void deleteClick() {
        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                getPresenter().delete(list.get(i).videoId);
            }
        }
        unChoiceChangeText();
        getHandler().postDelayed(() -> onControlGetDataList(true), 100);
    }

    @Override
    protected VideoDownloadListPresenter createPresenter() {
        return new VideoDownloadListPresenter(this, this);
    }


    @Override
    public void getLocalListSucc(List<VideoDownloadListModel> dataList, boolean isRef) {
        if (isRef) {
            videoDownloadListAdapter.deleteAllData();
            videoDownloadListAdapter.refAllData(dataList);
        } else {
            videoDownloadListAdapter.appendToList(dataList);
        }

        recyclerControl.getDataComplete(isRef);
        setError(false);
    }

    @Override
    public void getLocalListFail(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        setError(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {
            if (videoDownloadListAdapter.getList().size() == 0) {
                return false;
            }
            videoDownloadListAdapter.changeCheck();

            if (videoDownloadListAdapter.getCheckStatus()) {
                visableBottomLayout();
            } else {
                unVisableBottomLayout();
            }
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {
        //todo
        if (videoDownloadListAdapter.getCheckStatus()) {
            ll_avd.setVisibility(View.GONE);
            unVisableBottomLayout();
            unChoiceChangeText();
            videoDownloadListAdapter.changeCheck();
        }
    }

    @Override
    public void onCheck(int position, boolean isCheck) {
        videoDownloadListAdapter.getList().get(position).isCheck = isCheck;

        int choiceNum = 0;
        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                choiceNum++;
            }
        }

        if (choiceNum == 0) {
            unChoiceChangeText();
        } else {
            choiceChangeText(choiceNum);
        }

        if (choiceNum != list.size()) {
            isChoiceAll = false;
            tv_avd_choiceAll.setText(R.string.choice_all_str);
        } else {
            isChoiceAll = true;
            tv_avd_choiceAll.setText(R.string.cancel_choice_all_str);
        }
    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    public void onControlGetDataList(boolean isRef) {

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refItemObservable != null) {
            RxBus.get().unregister(VIDEO_DOWNLOAD_REF_ONE_OBSERVABE, refItemObservable);
        }
    }


    private void setRefLayoutMarginBottom(int dp) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) recyclerRefreshLayout.getLayoutParams();
        lp.setMargins(0, 0, 0, Utils.convertDipOrPx(this, dp));
        recyclerRefreshLayout.setLayoutParams(lp);
    }

    //是无数据还是网络加载错误
    private void setError(boolean isError) {
        int listSize = 0;

        if (videoDownloadListAdapter != null) {
            listSize = videoDownloadListAdapter.getList().size();
        }

        VideoDownloadListModel model = new VideoDownloadListModel();
        model.isError = isError;
        if (listSize == 0) {
            if (!isError) {
                model.noDataIvSize = Utils.convertDipOrPx(this, 100);
                model.noDataIvResId = R.drawable.bg_no_video;
//                model.noDataTvStr = getString(R.string.person_home_no_blive);
                model.noDataBtnWidth = 0;
                model.noDataBtnHeight = 0;
                model.noDataBtnStr = "";
                model.type = VideoHistoryModel.ERROR;
            } else {
                model.errorStatus = ErrorDataView.ERROR_NET;
            }
            videoDownloadListAdapter.appendError(model, 0);
        } else {
            videoDownloadListAdapter.removeError();
        }
    }

    private void choiceChangeText(int num) {
        tv_avd_choiceAll.setText(R.string.cancel_choice_all_str);
        tv_avd_delete.setTextColor(getResources().getColor(R.color.color_f22502));
        tv_avd_delete.setText(getString(R.string.delete_1_str, String.valueOf(num)));
    }

    private void unChoiceChangeText() {
        tv_avd_choiceAll.setText(R.string.choice_all_str);
        tv_avd_delete.setTextColor(getResources().getColor(R.color.color_999999));
        tv_avd_delete.setText(R.string.delete_str);
    }

    private void visableBottomLayout() {
        ll_avd.setVisibility(View.VISIBLE);
        setRefLayoutMarginBottom(44);
        deleteItem.setIcon(null);
        deleteItem.setTitle(R.string.cancel_str);
    }

    private void unVisableBottomLayout() {
        ll_avd.setVisibility(View.GONE);
        setRefLayoutMarginBottom(0);
        deleteItem.setIcon(R.drawable.ic_local_delete);
        deleteItem.setTitle(R.string.delete_str);
    }
}
