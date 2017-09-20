package app.privatefund.com.vido.mvp.ui.video;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.lib.widget.recycler.MyScrollview;
import com.cgbsoft.lib.widget.swipefresh.FullyLinearLayoutManager;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.lzy.okserver.download.DownloadManager;

import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.mvp.contract.video.VideoDownloadListContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoDownloadListPresenter;
import app.privatefund.com.vido.mvp.ui.video.adapter.VideoDownloadListAdapter;
import app.privatefund.com.vido.mvp.ui.video.listener.VideoDownloadListListener;
import app.privatefund.com.vido.mvp.ui.video.model.VideoDownloadListModel;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.DOWNLOAD_TO_LIST_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.NOW_PLAY_VIDEOID_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_DOWNLOAD_REF_ONE_OBSERVABE;

/**
 * 视频下载列表 单线程下载
 *  
 */
public class VideoDownloadListActivity extends BaseActivity<VideoDownloadListPresenter> implements VideoDownloadListContract.View, VideoDownloadListListener {

    @BindView(R2.id.ll_avd_head)
    LinearLayout ll_avd_head;

    @BindView(R2.id.iv_avd_start)
    ImageView iv_avd_start;

    @BindView(R2.id.tv_avd_start_title)
    TextView tv_avd_start_title;

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
    //已经下载的列表
    @BindView(R2.id.donerecyclerView)
    RecyclerView donerecyclerView;

    //已下载的title展示view
    View donedownload_title_lay;
    //全部删除时候正在下载的选择数目
    int loadingCheckNum = 0;
    //已经下载的选择数目
    int DownCheckNum = 0;
    @BindView(R2.id.tv_nodata_lay)
    RelativeLayout tvNodataLay;

    //    private RecyclerControl recyclerControl;
    private FullyLinearLayoutManager linearLayoutManager;
    private FullyLinearLayoutManager donelinearLayoutManager;
    //标识未下载完成的
    private VideoDownloadListAdapter videoDownloadListAdapter;

    //标识已经下载的
    private VideoDownloadListAdapter videoHaveDownloadListAdapter;
    private Observable<Integer> refItemObservable;
    private Observable<String> nowPlayVideoIdObservable;
    private Observable<Boolean> downloadToListObservable;
    private boolean isChoiceAll, isAllDownloadStart;
    //    private MenuItem deleteItem;
    private DefaultDialog defaultDialog;
    private String nowPlayVideoId;
    TextView down_del_txt;
    ImageView down_del_iv;
    ImageView down_back_iv;
    MyScrollview down_myscrollview;

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
        initFindview();
//        tv_title.setText(R.string.local_video_str);
        videoDownloadListAdapter = new VideoDownloadListAdapter(this);
        videoHaveDownloadListAdapter = new VideoDownloadListAdapter(new HaveDownloadListListener());

        linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        donelinearLayoutManager = new FullyLinearLayoutManager(this);
        donelinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //正在下载的
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoDownloadListAdapter);
        //已经下载的
        donerecyclerView.setLayoutManager(donelinearLayoutManager);
        donerecyclerView.setAdapter(videoHaveDownloadListAdapter);

        recyclerView.setHasFixedSize(true);
        donerecyclerView.setHasFixedSize(true);

        defaultDialog = new DefaultDialog(this, getString(R.string.vdla_is_sure_delete_str), getString(R.string.cancel_str), getString(R.string.enter_str)) {
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


        down_del_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoHaveDownloadListAdapter.changeCheck();
                videoDownloadListAdapter.changeCheck();

                if (videoDownloadListAdapter.getCheckStatus() && videoHaveDownloadListAdapter.getCheckStatus()) {
                    visableBottomLayout();
                } else {
                    unVisableBottomLayout();
                }

            }
        });

        down_del_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoHaveDownloadListAdapter.changeCheck();
                videoDownloadListAdapter.changeCheck();

                if (videoDownloadListAdapter.getCheckStatus() && videoHaveDownloadListAdapter.getCheckStatus()) {
                    visableBottomLayout();
                } else {
                    unVisableBottomLayout();
                }

            }
        });
    }

    private void initFindview() {
        down_myscrollview = (MyScrollview) findViewById(R.id.down_myscrollview);
        down_back_iv = (ImageView) findViewById(R.id.down_back_iv);
        down_del_iv = (ImageView) findViewById(R.id.down_del_iv);
        down_del_txt = (TextView) findViewById(R.id.down_del_txt);

        donedownload_title_lay = findViewById(R.id.donedownload_title_lay);
        down_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseContext.finish();
            }
        });
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
//        onControlGetDataList(true);
        getHandler().postDelayed(() -> onControlGetDataList(true), 100);
//        onRefresh();
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
        List<VideoDownloadListModel> donelist = videoHaveDownloadListAdapter.getList();
//        list.addAll(null == donelist ? new ArrayList<VideoDownloadListModel>() : donelist);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isCheck = isChoiceAll;
        }
        for (int i = 0; i < donelist.size(); i++) {
            donelist.get(i).isCheck = isChoiceAll;
        }
        videoDownloadListAdapter.notifyDataSetChanged();
        videoHaveDownloadListAdapter.notifyDataSetChanged();
        if (isChoiceAll) {
            choiceChangeText(list.size() + donelist.size());
        } else {
            unChoiceChangeText();
        }
    }


    @OnClick(R2.id.tv_avd_delete)
    void deleteClick() {//删除

        List<VideoDownloadListModel> list = videoDownloadListAdapter.getList();
        List<VideoDownloadListModel> donelist = videoHaveDownloadListAdapter.getList();


        if (!getPresenter().isAnyChoice(list) && !getPresenter().isAnyChoice(donelist)) {
            return;
        }
//正在下载中***************************************

//        list.addAll(null == donelist ? new ArrayList<VideoDownloadListModel>() : donelist);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
//                if (TextUtils.equals(nowPlayVideoId, list.get(i).videoId)) {
//                    defaultDialog.show();
//                } else {
                getPresenter().delete(list.get(i).videoId);
//                if (list.get(i).status == VideoStatus.FINISH) {
//                    File file = new File(list.get(i).localPath);
//                    if (file.isFile() && file.exists()) {
//                        file.delete();
//                    }
////                    }
//                }
            }
        }
        //下载完毕********************************************
        for (int i = 0; i < donelist.size(); i++) {
            if (donelist.get(i).isCheck) {
//                if (TextUtils.equals(nowPlayVideoId, donelist.get(i).videoId)) {
//                    defaultDialog.show();
//                } else {
                getPresenter().delete(donelist.get(i).videoId);
//                if (donelist.get(i).status == VideoStatus.FINISH) {
//
//                    if (null == donelist.get(i).localPath || BStrUtils.isEmpty(donelist.get(i).localPath))
//                        break;
//                    File file = new File(donelist.get(i).localPath);
//                    if (file.isFile() && file.exists()) {
//                        file.delete();
//                    }
//                }
//                }
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
            ll_avd_head.setVisibility(View.GONE);
            donedownload_title_lay.setVisibility(View.GONE);
            tvNodataLay.setVisibility(View.VISIBLE);
//            deleteItem.setVisible(false);
            tv_avd_allspace.setVisibility(View.VISIBLE);
            donerecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            down_myscrollview.setVisibility(View.INVISIBLE);
        } else {
            down_myscrollview.setVisibility(View.VISIBLE);
            tvNodataLay.setVisibility(View.GONE);
//            deleteItem.setVisible(true);
            tv_avd_allspace.setVisibility(View.INVISIBLE);
        }
        //如果已经全部下载过了就全部开始不显示
        if (getPresenter().isAllDownLoadOver(dataList)) {
            unVisableBottomLayout();
            ll_avd_head.setVisibility(View.GONE);
            //只在已下载的列表显示
            videoHaveDownloadListAdapter.deleteAllData();
            videoHaveDownloadListAdapter.refAllData(dataList);
            recyclerView.setVisibility(View.GONE);

        } else {
            ll_avd_head.setVisibility(View.VISIBLE);
        }

        //获取未下载的数据
        List<VideoDownloadListModel> downloadingData = getPresenter().getVideoList(dataList, false);
        //获取已下载的数据
        List<VideoDownloadListModel> DownloadData = getPresenter().getVideoList(dataList, true);
        //展示已下载的view 的title
        donedownload_title_lay.setVisibility((null == DownloadData || 0 == DownloadData.size() ? View.GONE : View.VISIBLE));
        if (isRef) {
            //开始刷新未完成的数据列表
            videoDownloadListAdapter.deleteAllData();
            videoDownloadListAdapter.refAllData(downloadingData);//(dataList);
            //开始刷新已完成的数据列表
            videoHaveDownloadListAdapter.deleteAllData();
            videoHaveDownloadListAdapter.refAllData(DownloadData);
        } else {
            videoDownloadListAdapter.appendToList(dataList);
        }


        if (getPresenter().isStartAllDownloading(downloadingData)) {
            isAllDownloadStart = true;
            changeAllStart();
        }
        getPresenter().bindDownloadCallback();

    }

    @Override
    public void getLocalListFail(boolean isRef) {
//        recyclerControl.getDataComplete(isRef);
//        recyclerRefreshLayout.setEnabled(false);
//        recyclerControl.setError(this, true, videoDownloadListAdapter, new VideoDownloadListModel());
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

    //
    @Override
    public void onItemClick(int position, ImageView iv_avd_cover, ImageView iv_avd_pause, TextView tv_avd_pause) {
        VideoDownloadListModel model = videoDownloadListAdapter.getList().get(position);
        String downloadingVideoId = getPresenter().isHasDownloading();
        boolean changeStatus;
        if (videoDownloadListAdapter.getCheckStatus() && videoHaveDownloadListAdapter.getCheckStatus())
            return;
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
                    PromptManager.ShowCustomToast(baseContext, "等待下载...");
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
        List<VideoDownloadListModel> donelist = videoHaveDownloadListAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                choiceNum++;
            }
        }
        //记录正在下载的选择删除数目
        loadingCheckNum = choiceNum;

        if ((DownCheckNum + loadingCheckNum) == 0) {
            unChoiceChangeText();
        } else {
            choiceChangeText((DownCheckNum + loadingCheckNum));
        }

        if ((DownCheckNum + loadingCheckNum) != (list.size() + donelist.size())) {
            isChoiceAll = false;
            tv_avd_choiceAll.setText(R.string.choice_all_str);
        } else {
            isChoiceAll = true;
            tv_avd_choiceAll.setText(R.string.cancel_choice_all_str);
        }
    }

    @Override
    public void onErrorClickListener() {
//        onRefresh();

    }

    //    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getLocalDataList(isRef);
        tv_avd_allspace.setText(getPresenter().getSDCardSize());
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_avd.getLayoutParams();
        lp.height = Utils.convertDipOrPx(this, dp);
        ll_avd.setLayoutParams(lp);
    }

    private void choiceChangeText(int num) {
        tv_avd_choiceAll.setText(R.string.cancel_choice_all_str);
        tv_avd_delete.setTextColor(getResources().getColor(R.color.app_golden));
        tv_avd_delete.setText(getString(R.string.delete_1_str, String.valueOf(num)));
    }

    private void unChoiceChangeText() {


        tv_avd_choiceAll.setText(R.string.choice_all_str);
        tv_avd_delete.setTextColor(getResources().getColor(R.color.color_999999));
        tv_avd_delete.setText(R.string.delete_str);


    }

    private void visableBottomLayout() {
        setRefLayoutMarginBottom(44);
//        deleteItem.setIcon(null);/*
//        deleteItem.setTitle(R.string.cancel_str);*/

        down_del_iv.setVisibility(View.GONE);
        down_del_txt.setVisibility(View.VISIBLE);

    }

    private void unVisableBottomLayout() {
        setRefLayoutMarginBottom(0);


        down_del_iv.setVisibility(View.VISIBLE);
        down_del_txt.setVisibility(View.GONE);


//        deleteItem.setIcon(R.drawable.ic_local_delete);
//        deleteItem.setTitle(R.string.delete_str);
    }

    private void changeAllStart() {
        if (isAllDownloadStart) {
            iv_avd_start.setImageResource(R.drawable.pause_c);
            tv_avd_start_title.setText(R.string.vdla_stop_all_str);
        } else {
            iv_avd_start.setImageResource(R.drawable.play_c);
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
//            iv_avd_pause.setImageResource(R.drawable.i_cache_wait);
            iv_avd_pause.setImageResource(R.drawable.ic_cache_d);

            tv_avd_pause.setText(R.string.caching_str);

//            tv_avd_pause.setText("等待中");
        } else {
            iv_avd_pause.setImageResource(R.drawable.ic_video_download_pause);
            tv_avd_pause.setText(R.string.paused_str);
        }
    }

    private void waiting(ImageView iv_avd_pause, TextView tv_avd_pause) {
        iv_avd_pause.setImageResource(R.drawable.i_cache_wait);

        tv_avd_pause.setText("等待中");
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
        onControlGetDataList(true);
//        videoDownloadListAdapter.getList().remove(position);
////        videoDownloadListAdapter.getList().add(model);
//        videoDownloadListAdapter.notifyDataSetChanged();
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
//            onControlGetDataList(true);
            tv_avd_progress.setText(Formatter.formatFileSize(this, totalSize));
            return;
        }
        ll_avd_pause.setVisibility(View.VISIBLE);
        pb_avd.setVisibility(View.VISIBLE);
        tv_avd_speed.setVisibility(View.VISIBLE);

        if (downloadState == DownloadManager.DOWNLOADING) {
            changeStart(true, iv_avd_pause, tv_avd_pause);
        } else if (downloadState == DownloadManager.WAITING) {
            waiting(iv_avd_pause, tv_avd_pause);
        } else if (downloadState == DownloadManager.PAUSE) {
            changeStart(false, iv_avd_pause, tv_avd_pause);
        }
        if (pb_avd.getMax() != totalSize) {
            pb_avd.setMax((int) totalSize);
        }
        pb_avd.setProgress((int) currentSize);

        String downloadLength = Formatter.formatFileSize(this, currentSize);
        String totalLength = Formatter.formatFileSize(this, totalSize);

//        tv_avd_progress.setText(totalLength + "/" + downloadLength);
        tv_avd_progress.setText(downloadLength + "/" + totalLength);
        tv_avd_speed.setText(Formatter.formatFileSize(this, networkSpeed).contains("0.00") ? "暂停缓存" : Formatter.formatFileSize(this, networkSpeed));

        if (downloadState == DownloadManager.NONE) {
            changeStart(false, iv_avd_pause, tv_avd_pause);
        }

        if (isFinish) {
            onControlGetDataList(true);
        }
    }


    /**
     * 已下载的选择监听器
     */

    class HaveDownloadListListener implements VideoDownloadListListener {
        @Override
        public void onErrorClickListener() {

        }

        @Override
        public void onItemClick(int position, ImageView iv_avd_cover, ImageView iv_avd_pause, TextView tv_avd_pause) {
            VideoDownloadListModel model = videoHaveDownloadListAdapter.getList().get(position);


            //没有正在下载的视频
//            if (model.status == VideoStatus.FINISH) {//如果点击的视频已经下载完成
            Intent intent = new Intent(baseContext, VideoDetailActivity.class);
            intent.putExtra("videoId", model.videoId);
            intent.putExtra("videoCoverUrl", model.videoCoverUrl);
            ActivityTransitionLauncher.with(baseContext).from(iv_avd_cover).launch(intent);
//            } else {
//                getPresenter().startDownload(model);
//                changeStart(true, iv_avd_pause, tv_avd_pause);
//            }
//
//            //关闭选择框
//            if (videoDownloadListAdapter.getCheckStatus()) {
//                setRefLayoutMarginBottom(0);
//                unVisableBottomLayout();
//                unChoiceChangeText();
//                videoDownloadListAdapter.changeCheck();
//            }
        }

        @Override
        public void onCheck(int position, boolean isCheck) {
            videoHaveDownloadListAdapter.getList().get(position).isCheck = isCheck;

            int choiceNum = 0;
            List<VideoDownloadListModel> list = videoHaveDownloadListAdapter.getList();
            List<VideoDownloadListModel> loadinglist = videoDownloadListAdapter.getList();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck) {
                    choiceNum++;
                }
            }
            //记录已经下载的选择删除的数目
            DownCheckNum = choiceNum;

            if ((loadingCheckNum + DownCheckNum) == 0) {
                unChoiceChangeText();
            } else {
                choiceChangeText((loadingCheckNum + DownCheckNum));
            }

            if ((loadingCheckNum + DownCheckNum) != (list.size() + loadinglist.size())) {
                isChoiceAll = false;
                tv_avd_choiceAll.setText(R.string.choice_all_str);
            } else {
                isChoiceAll = true;
                tv_avd_choiceAll.setText(R.string.cancel_choice_all_str);
            }
        }
    }

}
