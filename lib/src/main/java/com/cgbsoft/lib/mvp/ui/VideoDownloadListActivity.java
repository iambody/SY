package com.cgbsoft.lib.mvp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.IOSDialog;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.lzy.okserver.download.DownloadManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.DOWNLOAD_TO_LIST_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.NOW_PLAY_VIDEOID_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_DOWNLOAD_REF_ONE_OBSERVABE;

/**
 * 视频下载列表 单线程下载
 * Created by xiaoyu.zhang on 2016/12/12 17:30
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListActivity extends BaseActivity<VideoDownloadListPresenter> implements VideoDownloadListContract.View, RecyclerControl.OnControlGetDataListListener, VideoDownloadListListener,
        Toolbar.OnMenuItemClickListener, RecyclerRefreshLayout.OnRefreshListener {
    @BindView(R2.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.tv_title)
    TextView tv_title;

    @BindView(R2.id.ll_avd_head)
    LinearLayout ll_avd_head;

    @BindView(R2.id.iv_avd_start)
    ImageView iv_avd_start;

    @BindView(R2.id.tv_avd_start_title)
    TextView tv_avd_start_title;

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

    @BindView(R2.id.tv_avd_allspace)
    TextView tv_avd_allspace;

    private RecyclerControl recyclerControl;
    private LinearLayoutManager linearLayoutManager;
    private VideoDownloadListAdapter videoDownloadListAdapter;

    private Observable<Integer> refItemObservable;
    private Observable<String> nowPlayVideoIdObservable;
    private Observable<Boolean> downloadToListObservable;
    private boolean isChoiceAll, isAllDownloadStart;
    private MenuItem deleteItem;
    private IOSDialog iosDialog;
    private String nowPlayVideoId;

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
        tv_title.setText(R.string.local_video_str);
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


        iosDialog = new IOSDialog(this, "", getString(R.string.vdla_is_sure_delete_str), getString(R.string.cancel_str), getString(R.string.enter_str)) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right() {
                getPresenter().delete(nowPlayVideoId);
                //向视频详情发消息，当前播放视频已经被删除
                RxBus.get().post(IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE, true);
                this.dismiss();
            }
        };
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
        nowPlayVideoIdObservable = RxBus.get().register(NOW_PLAY_VIDEOID_OBSERVABLE, String.class);
        nowPlayVideoIdObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                nowPlayVideoId = s;
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        downloadToListObservable = RxBus.get().register(DOWNLOAD_TO_LIST_OBSERVABLE, Boolean.class);
        downloadToListObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (aBoolean) {
                    if (isAllDownloadStart) {
                        startDownloadAllClick();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        onRefresh();
    }

    @OnClick(R2.id.ll_avd_head)
    void startDownloadAllClick() {//全部开始下载
        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        if (list.size() > 0 && list.get(0).type == VideoDownloadListModel.ERROR) {
            return;
        }
        if (isAllDownloadStart) {
            getPresenter().stopAllDownload();
        } else {
            getPresenter().startAllDownload(list);
        }

        isAllDownloadStart = !isAllDownloadStart;
        changeAllStart();

    }

    @OnClick(R2.id.tv_avd_choiceAll)
    void choiceAllClick() {//选择所有
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
    void deleteClick() {//删除
        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                if (TextUtils.equals(nowPlayVideoId, list.get(i).videoId)) {
                    iosDialog.show();
                } else {
                    getPresenter().delete(list.get(i).videoId);
                    if (list.get(i).status == VideoStatus.FINISH) {
                        File file = new File(list.get(i).localPath);
                        if (file.isFile() && file.exists()) {
                            file.delete();
                        }
                    }
                }
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
        if (dataList.size() == 0) {//没做分页所以直接判断就行
            unVisableBottomLayout();
        }
        if (isRef) {
            videoDownloadListAdapter.deleteAllData();
            videoDownloadListAdapter.refAllData(dataList);
        } else {
            videoDownloadListAdapter.appendToList(dataList);
        }

        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(this, false, videoDownloadListAdapter, new VideoDownloadListModel(), "", R.drawable.bg_no_video);

        if (getPresenter().isStartAllDownloading(dataList)) {
            isAllDownloadStart = true;
            changeAllStart();
        }
        getPresenter().bindDownloadCallback();

    }

    @Override
    public void getLocalListFail(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(this, true, videoDownloadListAdapter, new VideoDownloadListModel());
    }

    @Override
    public void onDownloadProgress(String videoId, long currentSize, long totalSize, float progress, long networkSpeed, int downloadState) {
        refItemUI(videoId, false, currentSize, totalSize, progress, networkSpeed, downloadState);
    }

    @Override
    public void onDownloadFinish(String videoId) {
        refItemUI(videoId);
    }

    @Override
    public void onDownloadError(String videoId) {

    }

    @Override
    public void allDownloadSucc() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        deleteItem = menu.findItem(R.id.firstBtn);
        MenuItem secItem = menu.findItem(R.id.secondBtn);
        deleteItem.setTitle(R.string.delete_str);
        deleteItem.setIcon(R.drawable.ic_local_delete);
        secItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {
            if (videoDownloadListAdapter.getList().size() == 1) {
                if (videoDownloadListAdapter.getList().get(0).type == VideoHistoryModel.ERROR)
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
    public void onItemClick(int position, ImageView iv_avd_cover, ImageView iv_avd_pause, TextView tv_avd_pause) {
        VideoDownloadListModel model = videoDownloadListAdapter.getList().get(position);
        String downloadingVideoId = getPresenter().isHasDownloading();
        boolean changeStatus;

        if (!TextUtils.isEmpty(downloadingVideoId)) {//如果当前有视频正在下载
            if (TextUtils.equals(downloadingVideoId, model.videoId)) {//如果正在下载的视频id和点击的视频id相同
                getPresenter().stopDownload(downloadingVideoId);//停止下载
                changeStatus = false;
            } else {//如果不相同的话需要将任务放到下载队列里
                if (getPresenter().isTaskWaiting(model.videoId)) {//如果任务在等待中
                    getPresenter().stopDownload(model.videoId);
                    changeStatus = false;
                } else {//将任务加入等待列表
                    getPresenter().updateStatus(model.videoId, VideoStatus.WAIT);
                    getPresenter().startDownload(model);
                    changeStatus = true;
                }
            }
            changeStart(changeStatus, iv_avd_pause, tv_avd_pause);
            return;
        }
        //没有正在下载的视频
        if (model.status == VideoStatus.FINISH) {//如果点击的视频已经下载完成
            Intent intent = new Intent(this, VideoDetailActivity.class);
            intent.putExtra("videoId", model.videoId);
            intent.putExtra("videoCoverUrl", model.videoCoverUrl);
            ActivityTransitionLauncher.with(this).from(iv_avd_cover).launch(intent);
        } else {
            getPresenter().startDownload(model);
            changeStart(true, iv_avd_pause, tv_avd_pause);
        }

        //关闭选择框
        if (videoDownloadListAdapter.getCheckStatus()) {
            setRefLayoutMarginBottom(0);
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
        getPresenter().getLocalDataList(isRef);
        tv_avd_allspace.setText(getPresenter().getSDCardSize());
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
        if (refItemObservable != null)
            RxBus.get().unregister(VIDEO_DOWNLOAD_REF_ONE_OBSERVABE, refItemObservable);


        if (nowPlayVideoIdObservable != null)
            RxBus.get().unregister(NOW_PLAY_VIDEOID_OBSERVABLE, nowPlayVideoIdObservable);

        if (downloadToListObservable != null)
            RxBus.get().unregister(DOWNLOAD_TO_LIST_OBSERVABLE, downloadToListObservable);
    }


    private void setRefLayoutMarginBottom(int dp) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll_avd.getLayoutParams();
        lp.height = Utils.convertDipOrPx(this, dp);
        ll_avd.setLayoutParams(lp);
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
        setRefLayoutMarginBottom(44);
        deleteItem.setIcon(null);
        deleteItem.setTitle(R.string.cancel_str);
    }

    private void unVisableBottomLayout() {
        setRefLayoutMarginBottom(0);
        deleteItem.setIcon(R.drawable.ic_local_delete);
        deleteItem.setTitle(R.string.delete_str);
    }

    private void changeAllStart() {
        if (isAllDownloadStart) {
            iv_avd_start.setImageResource(R.drawable.ic_video_stop);
            tv_avd_start_title.setText(R.string.vdla_stop_all_str);
        } else {
            iv_avd_start.setImageResource(R.drawable.ic_video_start);
            tv_avd_start_title.setText(R.string.avd_start_all);
        }
    }

    /**
     * 切换下载状态
     *
     * @param b true 正在下载或者在缓存队列里
     */
    private void changeStart(boolean b, ImageView iv_avd_pause, TextView tv_avd_pause) {
        if (b) {
            iv_avd_pause.setImageResource(R.drawable.ic_video_download_start);
            tv_avd_pause.setText(R.string.caching_str);
        } else {
            iv_avd_pause.setImageResource(R.drawable.ic_video_download_pause);
            tv_avd_pause.setText(R.string.paused_str);
        }
    }

    private void refItemUI(String videoId) {
        refItemUI(videoId, true, 0, 0, 0, 0, 0);
        int position = -1;
        int adapterSize = videoDownloadListAdapter.getList().size();
        for (int i = 0; i < adapterSize; i++) {
            VideoDownloadListModel vdlm = videoDownloadListAdapter.getList().get(i);
            if (TextUtils.equals(vdlm.videoId, videoId)) {
                position = i;
                break;
            }
        }
        VideoDownloadListModel model = getPresenter().getLocalVideoInfo(videoId);
        if (position == -1)
            return;
        videoDownloadListAdapter.getList().remove(position);
        videoDownloadListAdapter.getList().add(model);
        videoDownloadListAdapter.notifyDataSetChanged();
    }

    private void refItemUI(String videoId, boolean isFinish, long currentSize, long totalSize, float progress, long networkSpeed, int downloadState) {
        int layoutManagerSize = linearLayoutManager.getChildCount();
        View childView = null;
        for (int i = 0; i < layoutManagerSize; i++) {
            View view = linearLayoutManager.getChildAt(i);
            if (view != null) {
                TextView tv_avd_id = (TextView) view.findViewById(R.id.tv_avd_id);
                if (tv_avd_id != null && TextUtils.equals(videoId, tv_avd_id.getText().toString())) {
                    childView = view;
                }
            }
        }
        if (childView == null)
            return;
        LinearLayout ll_avd_pause = (LinearLayout) childView.findViewById(R.id.ll_avd_pause);
        ImageView iv_avd_pause = (ImageView) childView.findViewById(R.id.iv_avd_pause);
        TextView tv_avd_pause = (TextView) childView.findViewById(R.id.tv_avd_pause);
        ProgressBar pb_avd = (ProgressBar) childView.findViewById(R.id.pb_avd);
        TextView tv_avd_progress = (TextView) childView.findViewById(R.id.tv_avd_progress);
        TextView tv_avd_speed = (TextView) childView.findViewById(R.id.tv_avd_speed);

        if (isFinish) {
            ll_avd_pause.setVisibility(View.GONE);
            pb_avd.setVisibility(View.GONE);
            tv_avd_speed.setVisibility(View.GONE);
            changeStart(false, iv_avd_pause, tv_avd_pause);
            return;
        }
        ll_avd_pause.setVisibility(View.VISIBLE);
        pb_avd.setVisibility(View.VISIBLE);
        tv_avd_speed.setVisibility(View.VISIBLE);

        if (downloadState == DownloadManager.DOWNLOADING || downloadState == DownloadManager.WAITING) {
            changeStart(true, iv_avd_pause, tv_avd_pause);
        } else if (downloadState == DownloadManager.PAUSE) {
            changeStart(false, iv_avd_pause, tv_avd_pause);
        }
        if (pb_avd.getMax() != totalSize) {
            pb_avd.setMax((int) totalSize);
        }
        pb_avd.setProgress((int) currentSize);

        String downloadLength = Formatter.formatFileSize(this, currentSize);
        String totalLength = Formatter.formatFileSize(this, totalSize);

        tv_avd_progress.setText(totalLength + "/" + downloadLength);
        tv_avd_speed.setText(Formatter.formatFileSize(this, networkSpeed));

        if (downloadState == DownloadManager.NONE) {
            changeStart(false, iv_avd_pause, tv_avd_pause);
        }
    }

}
