package app.privatefund.com.vido.mvp.ui.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.share.dialog.CommonShareDialog;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.damp.SpringEffect;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.ProgressWheel;
import com.cgbsoft.lib.widget.dialog.CommentDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.ls.ListViewForScrollView;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.tencent.beacon.event.UserAction;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.VideoUtils;
import app.privatefund.com.vido.mvp.contract.video.VideoDetailContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoDetailPresenter;
import app.privatefund.com.vido.mvp.ui.video.adapter.CommentAdapter;
import app.privatefund.com.vido.service.FloatVideoService;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.cgbsoft.lib.utils.constant.RxConstant.IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.NOW_PLAY_VIDEOID_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_LOCAL_REF_ONE_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_PLAY5MINUTES_OBSERVABLE;

/**
 * 视频详情
 * 简述：首先获取本地数据（有：判断视频是否下载，如果下载则播放本地视频，否则播放网络视频），然后在获取网络数据（成功：更新本地数据）
 * 推出时候会保存当前视频的播放进度。
 * 如果希望视频在下载列表中出现那么起码得进一次视频详情。
 *  
 */
@Route(RouteConfig.GOTOVIDEOPLAY)
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements VideoDetailContract.View, PlayerListener {
    @BindView(R2.id.rl_avd_head)
    RelativeLayout rl_avd_head;

    @BindView(R2.id.vrf_avd)
    VideoRootFrame vrf_avd;

    @BindView(R2.id.iv_mvv_cover)
    ImageView iv_mvv_cover;

    @BindView(R2.id.pw_mvv_wait)
    ProgressWheel pw_mvv_wait;

    @BindView(R2.id.ll_mvv_nowifi)
    LinearLayout ll_mvv_nowifi;

    @BindView(R2.id.tv_mvv_no_wifi)
    TextView tv_mvv_no_wifi;

    @BindView(R2.id.tv_mvv_rich_go)
    TextView tv_mvv_rich_go;

    @BindView(R2.id.iv_avd_back)
    ImageView iv_avd_back;

    @BindView(R2.id.iv_avd_back_play)
    ImageView iv_avd_back_play;

    @BindView(R2.id.sv_avd)
    ScrollView sv_avd;

    @BindView(R2.id.tv_avd_like_num)
    TextView tv_avd_like_num;

    @BindView(R2.id.iv_avd_like)
    ImageView iv_avd_like;

    @BindView(R2.id.ll_avd_cache)
    LinearLayout ll_avd_cache;

    @BindView(R2.id.iv_avd_cache)
    ImageView iv_avd_cache;

    @BindView(R2.id.tv_avd_cache)
    TextView tv_avd_cache;

    @BindView(R2.id.tv_avd_title)
    TextView tv_avd_title;

    @BindView(R2.id.tv_avd_content)
    TextView tv_avd_content;

    @BindView(R2.id.rl_avd_download)
    RelativeLayout rl_avd_download;

    @BindView(R2.id.iv_avd_close)
    ImageView iv_avd_close;

    @BindView(R2.id.tv_avd_hd)
    TextView tv_avd_hd;

    @BindView(R2.id.tv_avd_sd)
    TextView tv_avd_sd;

    @BindView(R2.id.ll_avd_cache_open)
    LinearLayout ll_avd_cache_open;

    @BindView(R2.id.tv_avd_cache_num)
    TextView tv_avd_cache_num;
    //产品view=>名字
    @BindView(R2.id.view_videoplay_product_name)
    TextView viewVideoplayProductName;
    //产品view=>基准
    @BindView(R2.id.view_videoplay_product_income)
    TextView viewVideoplayProductIncome;
    //产品view=>剩余时间
    @BindView(R2.id.view_videoplay_product_day)
    TextView viewVideoplayProductDay;
    //产品view=>产品期限
    @BindView(R2.id.view_videoplay_product_allday)
    TextView viewVideoplayProductAllday;
    //剩余额度
    @BindView(R2.id.view_videoplay_product_edu)
    TextView viewVideoplayProductEdu;
    @BindView(R2.id.video_view_comment_null_layout)
    RelativeLayout videoViewCommentNullLayout;
    @BindView(R2.id.video_view_comment_list)
    ListViewForScrollView videoViewCommentList;
    @BindView(R2.id.video_view_check_more)
    TextView videoViewCheckMore;
    //评论信息
    @BindView(R2.id.video_videplay_edit_comment_c)
    TextView videoVideplayEditCommentC;
    //发送评论
    @BindView(R2.id.video_videplay_send_comment)
    TextView videoVideplaySendComment;
    @BindView(R2.id.video_videplay_edit_comment_lay)
    LinearLayout videoVideplayEditCommentLay;
    @BindView(R2.id.view_title_back_iv)
    ImageView viewTitleBackIv;
    @BindView(R2.id.view_title_right_txt)
    TextView viewTitleRightTxt;
    @BindView(R2.id.jiangjieren)
    TextView jiangjieren;
    @BindView(R2.id.jiangjieren_lay)
    LinearLayout jiangjierenLay;

    TextView video_videplay_time_playnumber_toc;

    private View title_videodetail_lay;
    //加载时候的dialog
    private LoadingDialog mLoadingDialog;
    //C端分享的dialog
    private CommonShareDialog commonShareDialog;
    //C端评论的列表Adapter
    private CommentAdapter commentAdapter;
    //C端需要的产品
    private View videplay_produxt_view;
    private View view_video_comment_lay;

    private String videoId, videoCoverUrl;
    private boolean isPlayAnim;
    private int comeFrom;
    private LoadingDialog loadingDialog;
    private boolean seekFlag = true, isPlaying, isSetDataSource;//是否播放器设置了数据;
    private int playerCurrentTime;
    private VideoInfoModel videoInfoModel;
    private VideoInfoEntity.Result videoAllInf;
    private long startPlayTime;//当前播放时间,用于做任务
    private long allPlayTime;//一共播放时间
    private long fiveMinutes = 5 * 60 * 1000;
    private boolean isSetFullscreenHandler;

    private ConnectionChangeReceiver connectionChangeReceiver;
    private ExitActivityTransition exitTransition;
    private Subscription delaySub, delaySub2;
    private boolean isFullscreen, isDisplayCover;
    private AnimationSet hdAnimationSet, sdAnimationSet, openAnimationSet, closeAnimationSet;
    private Observable<Boolean> isPlayVideoLocalDeleteObservable;
    //监听播放五分钟o
    private Observable<Long> isPlayFiveMinteObservable;

    private boolean isOnPause;
    private int onPausePlayStauts = -1;//默认为-1，没在播放为0 在播放为1
//    private String videoValidateResult; // 视频内容校验结果 1 ：通过， 0 不通过

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        getPresenter().addressValidateResult();
        videoId = getIntent().getStringExtra("videoId");
        videoCoverUrl = getIntent().getStringExtra("videoCoverUrl");
        isPlayAnim = getIntent().getBooleanExtra("isPlayAnim", true);
        comeFrom = getIntent().getIntExtra("comeFrom", -1);
        loadingDialog = LoadingDialog.getLoadingDialog(this, false, false);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);//this.getResources().getConfiguration().orientation
        changeVideoViewSize(getResources().getConfiguration().orientation);
        if (iv_mvv_cover.getVisibility() == View.VISIBLE && videoCoverUrl != null && !TextUtils.isEmpty(videoCoverUrl)) {
            Imageload.display(this, videoCoverUrl, 0, 0, 0, iv_mvv_cover, R.drawable.bg_default, R.drawable.bg_default);
            isDisplayCover = true;
        }
        if (isPlayAnim)
            exitTransition = ActivityTransition.with(getIntent()).duration(20).to(rl_avd_head).start(savedInstanceState);
        else
            sv_avd.setVisibility(View.VISIBLE);

        if (comeFrom == 1)
            FloatVideoService.stopService();

        if (TextUtils.isEmpty(videoId)) {
            loadingDialog.setResult(false, getString(R.string.no_videoid_str), 1000, this::finish);
            return;
        }
        findview();
        vrf_avd.setListener(this);

        getVideoDetailInfo();
        pw_mvv_wait.setVisibility(View.VISIBLE);
        getPresenter().bindDownloadCallback(videoId);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(connectionChangeReceiver, filter);
        SpringEffect.doEffectSticky(iv_avd_like, 1.2f, () -> getPresenter().toVideoLike());
//        tv_avd_cache_num.setText(String.valueOf(getPresenter().getCacheVideoNum()));
        tv_avd_cache_num.setText(String.valueOf(getsize()));

        FloatVideoService.stopService();
        UserAction.initUserAction(this. getApplicationContext());
    }

    public int getsize() {
        return getPresenter().daoUtils.getDownLoadVideoInfo().size();
    }

    private void findview() {
        video_videplay_time_playnumber_toc = (TextView) findViewById(R.id.video_videplay_time_playnumber_toc);
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.getvidoingloading), false, false);
        title_videodetail_lay = findViewById(R.id.title_videodetail_lay);
        videplay_produxt_view = findViewById(R.id.videplay_produxt_view);
        view_video_comment_lay = findViewById(R.id.view_video_comment_lay);
        videplay_produxt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != videoAllInf && null != videoAllInf.rows) {
                    VideoInfoEntity.ProductBean productBean = videoAllInf.rows.product.get(0);
                    String url = CwebNetConfig.productDetail.concat(productBean.schemeId);

                    Router.build(RouteConfig.GOTOPRODUCTDETAIL).with(WebViewConstant.push_message_url, url).with(WebViewConstant.PAGE_SHOW_TITLE, true).with(WebViewConstant.push_message_title, productBean.productName).go(baseContext);
                }
            }
        });
        initConfiguration();
    }

    @Override
    protected void data() {
        super.data();
        initAnim();
        isPlayVideoLocalDeleteObservable = RxBus.get().register(IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE, Boolean.class);
        isPlayVideoLocalDeleteObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (aBoolean) {
                    if (onPausePlayStauts > 0) {
                        vrf_avd.release();
                        playNetData();
                    }
                    if (onPausePlayStauts == 0) {
                        vrf_avd.pause();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        //播放五分钟的监听
        isPlayFiveMinteObservable = RxBus.get().register(VIDEO_PLAY5MINUTES_OBSERVABLE, Long.class);
        isPlayFiveMinteObservable.subscribe(new RxSubscriber<Long>() {
            @Override
            protected void onEvent(Long aLong) {
                TaskInfo.complentTask("学习视频");
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        onPausePlayStauts = 0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isOnPause = false;
        onPausePlayStauts = 1;
        vrf_avd.play();
//        sv_avd.smoothScrollTo(0,20);
        tv_avd_hd.setEnabled(true);
        tv_avd_sd.setEnabled(true);
        tv_avd_hd.setTextColor(getResources().getColor(R.color.black));
        tv_avd_sd.setTextColor(getResources().getColor(R.color.black));
        tv_avd_cache_num.setText(String.valueOf(getsize()));
        getLocalVideoInfoSucc(getPresenter().getLocalvideos(videoId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
        onPausePlayStauts = -1;


    }

    @OnClick(R2.id.iv_avd_back)
    void backClick() {
        toFinish();
    }

    @OnClick(R2.id.iv_avd_back_play)
    void iv_avd_back_play() {
//        toDataStatistics(1021, 10102, new String[]{"缩小", SPreference.isColorCloud(this), SPreference.getOrganizationName(this)});
        stopCountDown();
        getPresenter().updataNowPlayTime(vrf_avd.getCurrentTime() + 5);
        FloatVideoService.startService(videoId);

        finish();
        DataStatistApiParam.onStatisToCVideoDetailZoomClick(videoInfoModel.videoName);

    }

//    @Override
//    public void setAddressValidateResult(String values) {
//        videoValidateResult = values;
//    }

    @OnClick(R2.id.ll_mvv_nowifi)
    void noWifiClick() {
        play(false);
    }

    @OnClick(R2.id.ll_avd_cache)
    void cacheOpenClick() {
        toDataStatistics(1021, 10103, new String[]{"下载", SPreference.isColorCloud(this), SPreference.getOrganizationName(this)});
        if (!isCancache) return;
        openCacheView();
    }

    @OnClick(R2.id.iv_avd_close)
    void cacheCloseClick() {
        closeCacheView();
    }

    @OnClick(R2.id.tv_avd_sd)
    void sdClick() {
        tv_avd_hd.setEnabled(false);
        tv_avd_sd.setEnabled(false);
        tv_avd_sd.startAnimation(sdAnimationSet);
        isCancache = false;
        //todo 开始后台下载
        getPresenter().updataDownloadType(VideoStatus.SD);
        getPresenter().toDownload(videoId);


        tv_avd_cache.setText(R.string.caching_str);
        iv_avd_cache.setImageResource(R.drawable.ic_caching);
        tv_avd_cache.setClickable(false);
    }

    @OnClick(R2.id.tv_avd_hd)
    void hdClick() {
        tv_avd_hd.setEnabled(false);
        tv_avd_sd.setEnabled(false);
        tv_avd_hd.startAnimation(hdAnimationSet);
        isCancache = false;
        //todo 开始后台下载
        getPresenter().updataDownloadType(VideoStatus.HD);
        getPresenter().toDownload(videoId);

        tv_avd_cache.setText(R.string.caching_str);
        iv_avd_cache.setImageResource(R.drawable.ic_caching);
        tv_avd_cache.setClickable(false);
    }

    //打开下载列表页面
    @OnClick(R2.id.ll_avd_cache_open)
    void videoDownloadListOpenClick() {
        RxBus.get().post(NOW_PLAY_VIDEOID_OBSERVABLE, videoId);//发送消息，当前正在播放的视频id
        getPresenter().updataNowPlayTime(vrf_avd.getCurrentTime());//更新当前播放视频的位置
        openActivity(VideoDownloadListActivity.class);

        closeCacheView();

    }

    @Override
    public void onBackPressed() {
        toFinish();
    }

    private void toFinish() {
        if (isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            sv_avd.setVisibility(View.VISIBLE);
            iv_avd_back.setVisibility(View.VISIBLE);
        } else {
            if (isPlayAnim) {
//                sv_avd.setVisibility(View.GONE);
//                delaySub2 = Observable.just(1).delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new RxSubscriber<Integer>() {
//                            @Override
//                            protected void onEvent(Integer integer) {
                exitTransition.exit(VideoDetailActivity.this);
//                            }
//
//                            @Override
//                            protected void onRxError(Throwable error) {
//
//                            }
//                        });
            } else {
                finish();
            }
        }
    }

    private void getVideoDetailInfo() {
        if (isPlayAnim) {
            if (delaySub != null && !delaySub.isUnsubscribed()) {
                return;
            }
            delaySub = Observable.just(1).delay(50, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<Integer>() {
                        @Override
                        protected void onEvent(Integer integer) {
                            getPresenter().getVideoDetailInfo(mLoadingDialog, videoId);
                            sv_avd.setVisibility(View.VISIBLE);


                        }

                        @Override
                        protected void onRxError(Throwable error) {
                            this.unsubscribe();

                        }
                    });
        } else {
            getPresenter().getVideoDetailInfo(mLoadingDialog, videoId);
        }
    }

    @Override
    protected VideoDetailPresenter createPresenter() {
        return new VideoDetailPresenter(this, this);
    }

    boolean isCancache = true;

    @Override
    public void getLocalVideoInfoSucc(VideoInfoModel model) {


        videoInfoModel = model;
        playerCurrentTime = videoInfoModel.currentTime;
        setData();
        play(true);

        switch (videoInfoModel.status) {
            case VideoStatus.DOWNLOADING:

                tv_avd_cache.setText(R.string.caching_str);
                iv_avd_cache.setImageResource(R.drawable.ic_caching);

                if (videoInfoModel.downloadtype == 1) {
                    setSdTvBlue();
                } else {
                    setHdTvBlue();
                }
                tv_avd_hd.setEnabled(false);
                tv_avd_sd.setEnabled(false);
                tv_avd_cache.setClickable(false);//Enabled(false);
                isCancache = false;
                break;
            case VideoStatus.NONE:
                tv_avd_cache.setText(R.string.cache_str);
                iv_avd_cache.setImageResource(R.drawable.ic_cache);
                isCancache = true;

                break;
            case VideoStatus.FINISH:

                if (!TextUtils.isEmpty(videoInfoModel.localVideoPath)) {
                    File file = new File(videoInfoModel.localVideoPath);
                    if (file.isFile() && file.exists()) {
                        tv_avd_cache.setText(R.string.cached_str);
                        iv_avd_cache.setImageResource(R.drawable.ic_cached);

                        if (videoInfoModel.downloadtype == 1) {
                            setSdTvBlue();
                        } else {
                            setHdTvBlue();
                        }

                        tv_avd_hd.setEnabled(false);
                        tv_avd_sd.setEnabled(false);
                        tv_avd_cache.setClickable(false);
                        isCancache = false;
                        break;
                    }
                }
        }


    }

    private int cuntTime = 300;

    public void beginCuntDownTime() {
        RxCountDown.countTimeDown(cuntTime, new RxCountDown.ICountTime() {

            @Override
            public void onCompleted() {
                TaskInfo.complentTask("学习视频");
            }

            @Override
            public void onNext(int timer) {
                cuntTime = timer;
            }
        });
    }

    public void stopCountDown() {
        RxCountDown.stopCountTime();
    }

    //获取视频信息
    @Override
    public void getNetVideoInfoSucc(VideoInfoModel model, VideoInfoEntity.Result result) {
        videoInfoModel = model;
        videoAllInf = result;
        toDataStatistics(1020, 10101, new String[]{model.videoName, SPreference.isColorCloud(this), SPreference.getOrganizationName(this)});
        setData();
        play(true);

        if (videoInfoModel != null) {
            DataStatistApiParam.openVideoDetailActivityClick(videoInfoModel.videoName, (model != null && !TextUtils.isEmpty(model.categoryName)) ? model.categoryName : "全部");
        }
    }

    @Override
    public void getNetVideoInfoErr(String str) {

        if ((null != videoInfoModel) && (VideoStatus.FINISH == videoInfoModel.status)) {

        } else {
            PromptManager.ShowCustomToast(baseContext, "获取信息失败请重新尝试");
            baseContext.finish();
        }
    }

    @Override
    public void toVideoLikeSucc(int likeRes, int likeNum) {
        iv_avd_like.setImageResource(likeRes);
        tv_avd_like_num.setText(String.valueOf(likeNum));
//        toDataStatistics(1021, 10104, new String[]{videoInfoModel.videoName, SPreference.isColorCloud(this), SPreference.getOrganizationName(this)});
    }

    @Override
    public void onDownloadFinish(VideoInfoModel model) {
        videoInfoModel = model;
        if (!TextUtils.isEmpty(videoInfoModel.localVideoPath)) {
            File file = new File(videoInfoModel.localVideoPath);
            if (file.isFile() && file.exists()) {
                tv_avd_cache.setText(R.string.cached_str);
                iv_avd_cache.setImageResource(R.drawable.ic_cached);

                if (videoInfoModel.downloadtype == 1) {
                    setSdTvBlue();
                } else {
                    setHdTvBlue();
                }

                tv_avd_hd.setEnabled(false);
                tv_avd_sd.setEnabled(false);
            }
        }
    }

    @Override
    public void onDownloadVideoAdd() {
//        tv_avd_cache_num.setText(String.valueOf(getPresenter().getCacheVideoNum()));
        tv_avd_cache_num.setText(String.valueOf(getsize()));
        tv_avd_cache.setText(R.string.caching_str);
        iv_avd_cache.setImageResource(R.drawable.ic_caching);
        DataStatistApiParam.onStatisToCVideoDetailDownLoadClick(videoInfoModel.videoName);
    }

    @Override
    public void addCommontSucc(String commontsucc) {
        //添加视频成功后需要先判断原本是否有评论过的
        if (BStrUtils.isEmpty(commontsucc)) return;
        VideoInfoEntity.CommentBean comment;
        comment = new Gson().fromJson(commontsucc.toString(), VideoInfoEntity.CommentBean.class);
        if (null == comment) return;
        List<VideoInfoEntity.CommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(comment);


        if (null == commentAdapter) {//原来是没有视频的;
            commentAdapter = new CommentAdapter(baseContext, commentBeanList);
            videoViewCommentList.setVisibility(View.VISIBLE);
            videoViewCommentNullLayout.setVisibility(View.GONE);
            commentAdapter = new CommentAdapter(baseContext, commentBeanList);
            videoViewCommentList.setAdapter(commentAdapter);
        } else {//在原有的列表中进行添加
            commentAdapter.getData().add(0, comment);
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMoreCommontSucc(String moreCommontStr) {//获取更多评论
        if (BStrUtils.isEmpty(moreCommontStr)) {
            PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.nomorecommont));
            return;
        }

        try {

            List<VideoInfoEntity.CommentBean> comments = new Gson().fromJson(getV2String(moreCommontStr), new TypeToken<List<VideoInfoEntity.CommentBean>>() {
            }.getType());
            if (null == comments || 0 == comments.size()) {
                PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.nomorecommont));
                return;
            }
            commentAdapter.getData().addAll(comments);
            commentAdapter.notifyDataSetChanged();
            videoViewCheckMore.setVisibility(comments.size() == Contant.VIDEO_COMMENT_LIMIT ? View.VISIBLE : View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        new MToast(this).showLoginFailure("播放失败，请重新尝试！");
        vrf_avd.pause();
        pw_mvv_wait.setVisibility(View.GONE);
    }


    private void playNetData() {
        if (NetUtils.getNetState() != NetUtils.NetState.NET_WIFI) {
            ll_mvv_nowifi.setVisibility(View.VISIBLE);
            return;
        }
//        if (!urlValdateResult()) {
//            return;
//        }
        List<VideoInfo> videos = new ArrayList<>();
        VideoInfo v1 = new VideoInfo();
        VideoInfo v2 = new VideoInfo();
        v1.description = "标清";
        v1.type = VideoInfo.VideoType.MP4;
        v2.description = "高清";
        v2.type = VideoInfo.VideoType.MP4;
        v1.url = Utils.replaceDomainByIp(videoInfoModel.sdUrl);
        v2.url = Utils.replaceDomainByIp(videoInfoModel.hdUrl);
        videos.add(v1);
        videos.add(v2);

        vrf_avd.play(videos);
        ll_mvv_nowifi.setVisibility(View.GONE);
        pw_mvv_wait.setVisibility(View.GONE);
        isSetDataSource = true;

        if (!isSetFullscreenHandler) {
            isSetFullscreenHandler = true;
            vrf_avd.setToggleFullScreenHandler(() -> {
                if (vrf_avd.isFullScreen()) { // 竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    sv_avd.setVisibility(View.VISIBLE);
                    iv_avd_back.setVisibility(View.VISIBLE);
                    title_videodetail_lay.setVisibility(View.VISIBLE);

                } else { // 横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    sv_avd.setVisibility(View.GONE);
                    iv_avd_back.setVisibility(View.INVISIBLE);
                    title_videodetail_lay.setVisibility(View.GONE);
                }
            });
        }
    }

    private void play(boolean isCheckNet) {
        if (!isVideoDownload())
            if (NetUtils.getNetState() != NetUtils.NetState.NET_WIFI && isCheckNet) {
                ll_mvv_nowifi.setVisibility(View.VISIBLE);
                return;
            } else {
                ll_mvv_nowifi.setVisibility(View.GONE);
            }
        if (isPlaying) {
            return;
        }

//        if (!urlValdateResult()) {
//            return;
//        }

        int isLocalType = -1;
        boolean isCouldLocalPlay = false;

        if (videoInfoModel.status == VideoStatus.FINISH && !TextUtils.isEmpty(videoInfoModel.localVideoPath)) {
            File file = new File(videoInfoModel.localVideoPath);
            if (file.isFile() && file.exists()) {
                isLocalType = videoInfoModel.downloadtype;
                isCouldLocalPlay = true;
            }
        }
        List<VideoInfo> videos = new ArrayList<>();
        VideoInfo v1 = new VideoInfo();
        VideoInfo v2 = new VideoInfo();
        v1.description = "标清";
        v1.type = VideoInfo.VideoType.MP4;
        v2.description = "高清";
        v2.type = VideoInfo.VideoType.MP4;

        if (isCouldLocalPlay) {
            if (isLocalType == 0) {//高清
                v2.url =  Utils.replaceDomainByIp(videoInfoModel.localVideoPath);
                videos.add(v2);
            } else if (isLocalType == 1) {//标清
                v1.url = Utils.replaceDomainByIp(videoInfoModel.localVideoPath);
                videos.add(v1);
            }
        } else {
            v1.url = Utils.replaceDomainByIp(videoInfoModel.sdUrl);
            v2.url = Utils.replaceDomainByIp(videoInfoModel.hdUrl);
            videos.add(v1);
            videos.add(v2);
        }

        vrf_avd.play(videos);
        ll_mvv_nowifi.setVisibility(View.GONE);
        pw_mvv_wait.setVisibility(View.GONE);
        isSetDataSource = true;

        if (!isSetFullscreenHandler) {
            isSetFullscreenHandler = true;
            vrf_avd.setToggleFullScreenHandler(() -> {
                if (vrf_avd.isFullScreen()) { // 竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    sv_avd.setVisibility(View.VISIBLE);
                    iv_avd_back.setVisibility(View.VISIBLE);
                    title_videodetail_lay.setVisibility(View.VISIBLE);
                } else { // 横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    sv_avd.setVisibility(View.GONE);
                    iv_avd_back.setVisibility(View.INVISIBLE);
                    title_videodetail_lay.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onStateChanged(int arg0) {
                /*
                 * 1 STATE_IDLE 播放器空闲，既不在准备也不在播放 2 STATE_PREPARING 播放器正在准备 3
				 * STATE_BUFFERING 播放器已经准备完毕，但无法立即播放。此状态
				 * 的原因有很多，但常见的是播放器需要缓冲更多数据才能开始播放 4 STATE_PAUSE 播放器准备好并可以立即播放当前位置
				 * 5 STATE_PLAY 播放器正在播放中 6 STATE_ENDED 播放已完毕
				 */
        LogUtils.Log("playvideoo", "我的状态" + arg0);
        switch (arg0) {
            case 5://播放中
                startPlayTime = System.currentTimeMillis();
                iv_mvv_cover.setVisibility(View.GONE);
                pw_mvv_wait.setVisibility(View.GONE);
                seekToPlay(playerCurrentTime);
                isPlaying = true;

                LogUtils.Log("playvideoo", "播放中时间" + playerCurrentTime + "；；；播放开始时间" + startPlayTime);
//                allPlayTime += System.currentTimeMillis() - startPlayTime;
//                if (allPlayTime > fiveMinutes) {
//                    RxBus.get().post(VIDEO_PLAY5MINUTES_OBSERVABLE, allPlayTime);
//                }
                beginCuntDownTime();
                break;
            case 4:
                if (isOnPause) {
                    onPausePlayStauts = 1;
                }
                allPlayTime += System.currentTimeMillis() - startPlayTime;
                LogUtils.Log("playvideoo", "播放描述差时间" + allPlayTime + "；；；播放结束时间" + startPlayTime);
                if (allPlayTime > fiveMinutes) {
                    RxBus.get().post(VIDEO_PLAY5MINUTES_OBSERVABLE, allPlayTime);
                }
                isPlaying = false;
                stopCountDown();
                break;
            default:
                break;
        }
    }

//    private boolean urlValdateResult() {
//        if (TextUtils.equals(videoValidateResult, "0")) {
//            MToast.makeText(this, getResources().getString(R.string.video_play_env_exception), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

    protected void seekToPlay(int i) {
        if (seekFlag) {
            vrf_avd.seekTo(i);
        }
        seekFlag = false;
    }

    private void changeVideoViewSize(int orientation) {
        ViewGroup.LayoutParams lp = rl_avd_head.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.width = Utils.getRealScreenWidth(this);
//            lp.height=   (lp.width-getStatusBarHeight()) * 9 / 16;//-getStatusBarHeight();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            lp.width = Utils.getScreenWidth(this);

        }
        lp.height = lp.width * 9 / 16;
        rl_avd_head.setLayoutParams(lp);
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void openCacheView() {
        if (rl_avd_download.getVisibility() == View.VISIBLE) {
            return;
        }
        rl_avd_download.setVisibility(View.VISIBLE);
        rl_avd_download.startAnimation(openAnimationSet);

    }

    private void closeCacheView() {
        if (rl_avd_download.getVisibility() == View.GONE) {
            return;
        }
        rl_avd_download.setVisibility(View.GONE);
//        rl_avd_download.startAnimation(closeAnimationSet);
    }

    private void initAnim() {
        hdAnimationSet = new AnimationSet(true);
        TranslateAnimation hdTranslationAnim_1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, -0.5f);
        hdTranslationAnim_1.setDuration(300);
        TranslateAnimation hdTranslationAnim_2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -0.5f,
                Animation.RELATIVE_TO_PARENT, 1f);
        hdTranslationAnim_2.setStartOffset(300);
        hdTranslationAnim_2.setDuration(500);
        hdAnimationSet.addAnimation(hdTranslationAnim_1);
        hdAnimationSet.addAnimation(hdTranslationAnim_2);
        hdAnimationSet.setAnimationListener(new AnimListener(2));

        sdAnimationSet = new AnimationSet(true);
        TranslateAnimation sdTranslationAnim_1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, -0.5f);
        sdTranslationAnim_1.setDuration(300);
        TranslateAnimation sdTranslationAnim_2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -0.5f,
                Animation.RELATIVE_TO_PARENT, 1f);
        sdTranslationAnim_2.setStartOffset(300);
        sdTranslationAnim_2.setDuration(500);
        sdAnimationSet.addAnimation(sdTranslationAnim_1);
        sdAnimationSet.addAnimation(sdTranslationAnim_2);
        sdAnimationSet.setAnimationListener(new AnimListener(1));

        openAnimationSet = new AnimationSet(true);
        TranslateAnimation openTranslateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f);
        openTranslateAnim.setDuration(800);
        openAnimationSet.addAnimation(openTranslateAnim);


        closeAnimationSet = new AnimationSet(true);
        TranslateAnimation closeTranslateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        closeTranslateAnim.setDuration(800);
        closeAnimationSet.addAnimation(closeTranslateAnim);
    }

    //高清文字颜色
    private void setHdTvBlue() {
        tv_avd_hd.setBackgroundColor(0x00ffffff);
        tv_avd_hd.setTextColor(0xff5ba8f3);
        tv_avd_sd.setTextColor(0xffe6e6e6);
    }

    private void setSdTvBlue() {
        tv_avd_sd.setBackgroundColor(0x00ffffff);
        tv_avd_sd.setTextColor(0xff5ba8f3);
        tv_avd_hd.setTextColor(0xffe6e6e6);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && rl_avd_download.getVisibility() == View.VISIBLE) {
            rl_avd_download.setVisibility(View.GONE);
//            rl_avd_download.startAnimation(closeAnimationSet);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        stopCountDown();
        getPresenter().stopDownload(videoId);
        if (getPresenter().viModel.status != VideoStatus.FINISH) {
            getPresenter().updataNowStop();
        }
        if (vrf_avd != null) {
            getPresenter().updataNowPlayTime(vrf_avd.getCurrentTime());
            RxBus.get().post(VIDEO_LOCAL_REF_ONE_OBSERVABLE, vrf_avd.getCurrentTime());
            vrf_avd.release();
            vrf_avd = null;
        }
        getPresenter().updataFinalWatchTime();

        if (connectionChangeReceiver != null)
            unregisterReceiver(connectionChangeReceiver);

        if (allPlayTime < fiveMinutes) {
            allPlayTime += System.currentTimeMillis() - startPlayTime;
//            if (allPlayTime > fiveMinutes) {
//                RxBus.get().post(VIDEO_PLAY5MINUTES_OBSERVABLE, allPlayTime);
//            }
        } else {
            RxBus.get().post(VIDEO_PLAY5MINUTES_OBSERVABLE, allPlayTime);
        }

        if (delaySub != null && !delaySub.isUnsubscribed()) {
            delaySub.unsubscribe();
            delaySub = null;
        }

        if (delaySub2 != null && !delaySub2.isUnsubscribed()) {
            delaySub2.unsubscribe();
            delaySub2 = null;
        }

        if (isPlayVideoLocalDeleteObservable != null) {
            RxBus.get().unregister(IS_PLAY_VIDEO_LOCAL_DELETE_OBSERVABLE, isPlayVideoLocalDeleteObservable);
        }
        if (null != videoInfoModel)
            DataStatistApiParam.onStatisToCVideoDetailClose(videoInfoModel.videoName, allPlayTime);
        super.onDestroy();
    }

    private void setData() {
        jiangjierenLay.setVisibility(BStrUtils.isEmpty(videoInfoModel.lecturerRemark) ? View.GONE : View.VISIBLE);
        BStrUtils.SetTxt(jiangjieren, videoInfoModel.lecturerRemark);
        tv_avd_like_num.setText(String.valueOf(videoInfoModel.likeNum));
        tv_avd_title.setText(videoInfoModel.videoName);
        tv_avd_content.setText(videoInfoModel.content);
        if (!videoInfoModel.isLike) {
            iv_avd_like.setImageResource(R.drawable.ic_like_up);
        } else {
            iv_avd_like.setImageResource(R.drawable.ic_like_down);
            iv_avd_like.setEnabled(false);
        }
        if (!isDisplayCover && videoInfoModel.videoCoverUrl != null && !TextUtils.isEmpty(videoInfoModel.videoCoverUrl)) {
            isDisplayCover = true;
            Imageload.display(this, videoInfoModel.videoCoverUrl, 0, 0, 0, iv_mvv_cover, null, null);
        }
        //todo 商品
        if (null != videoAllInf && null != videoAllInf.rows && null != videoAllInf.rows.product && videoAllInf.rows.product.size() >= 1) {
            videplay_produxt_view.setVisibility(View.VISIBLE);
            VideoInfoEntity.ProductBean productBean = videoAllInf.rows.product.get(0);
            BStrUtils.SetTxt(viewVideoplayProductName, productBean.productName);
            BStrUtils.SetTxt(viewVideoplayProductIncome, String.format("收益基准：%s", "2".equals(productBean.productType) ? productBean.netUnit : productBean.incomeMin));
            BStrUtils.SetTxt(viewVideoplayProductDay, String.format("剩余时间：%s", VideoUtils.getDeadLine(productBean.raiseEndTime)));
            BStrUtils.SetTxt(viewVideoplayProductAllday, String.format("产品期限：%s", productBean.term));
            BStrUtils.SetTxt(viewVideoplayProductEdu, String.format("剩余额度：%s", productBean.remainingAmountStr));

        } else {
            videplay_produxt_view.setVisibility(View.GONE);

        }
        try {
            video_videplay_time_playnumber_toc.setText(videoAllInf.rows.createDate + "  |  播放次数" + videoAllInf.rows.playCount);
        } catch (Exception e) {
        }

        //剩余额度

        //todo 评论列表
        if (null != videoAllInf && null != videoAllInf.rows && null != videoAllInf.rows.comment && videoAllInf.rows.comment.size() >= 1) {
            List<VideoInfoEntity.CommentBean> commentBeanList = videoAllInf.rows.comment;
            videoViewCheckMore.setVisibility(commentBeanList.size() > 3 ? View.VISIBLE : View.GONE);


            videoViewCommentList.setVisibility(View.VISIBLE);
            videoViewCommentNullLayout.setVisibility(View.GONE);
            List<VideoInfoEntity.CommentBean> datt = new ArrayList<>();
            for (int i = 0; i < commentBeanList.size(); i++) {
                if (i == 3) break;
                datt.add(commentBeanList.get(i));
            }
            commentAdapter = new CommentAdapter(baseContext, datt);
            videoViewCommentList.setAdapter(commentAdapter);

        } else {
            videoViewCheckMore.setVisibility(View.GONE);
            videoViewCommentList.setVisibility(View.GONE);
            videoViewCommentNullLayout.setVisibility(View.VISIBLE);
            commentAdapter = null;

        }

    }

    private boolean isVideoDownload() {
        if (videoInfoModel.status == VideoStatus.FINISH) {
            if (TextUtils.isEmpty(videoInfoModel.localVideoPath)) return false;

            File file = new File(videoInfoModel.localVideoPath);
            if (file.isFile() && file.exists()) {
                return true;
            }
        }
        return false;
    }

    //查看就更多
    @OnClick(R2.id.video_view_check_more)
    public void onViewClicked() {

        if (null == commentAdapter || null == commentAdapter.getData() || 0 == commentAdapter.getData().size())
            return;

        if (null == videoAllInf || null == videoAllInf.videoId) return;

        getPresenter().getMoreCommont(videoAllInf.videoId, commentAdapter.getData().get(commentAdapter.getData().size() - 1).commentId);
    }


    @OnClick(R2.id.video_videplay_edit_comment_lay)
    public void oncommontClicked() {
        CommentDialog commentDialog = new CommentDialog(baseContext) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right(String extra) {
                this.dismiss();

                getPresenter().addCommont(extra, videoAllInf.videoId);
            }
        };

        commentDialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                commentDialog.showKeyboard();
            }
        }, 200);
    }


    @OnClick(R2.id.view_title_back_iv)
    public void onViewTitleBackIvClicked() {
        VideoDetailActivity.this.finish();
    }

    //点击视频详情页的分享按钮
    @OnClick(R2.id.view_title_right_txt)
    public void onViewTitleRightTxtClicked() {
        if (!NetUtils.isNetworkAvailable(baseContext)) {
            PromptManager.ShowCustomToast(baseContext, "请链接网络");
            return;
        }
        if (null == videoAllInf || null == videoAllInf.rows) return;
        if (null != commonShareDialog) commonShareDialog = null;
        ShareCommonBean commonShareBean = new ShareCommonBean(videoAllInf.rows.videoName, videoAllInf.rows.videoSummary, videoAllInf.rows.shareUrl, "");
        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, commonShareBean, null);
        commonShareDialog.show();

        DataStatistApiParam.onStatisToCVideoDetailShareClick(videoInfoModel.videoName, videoInfoModel.categoryName);
    }


    class AnimListener implements Animation.AnimationListener {
        int which;

        AnimListener(int which) {
            this.which = which;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (which == 1) {
                tv_avd_sd.setBackgroundColor(0x99999999);
            } else {
                tv_avd_hd.setBackgroundColor(0x99999999);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (which == 1) {
                setSdTvBlue();
            } else {
                setHdTvBlue();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class ConnectionChangeReceiver extends BroadcastReceiver {

        private boolean isVideoDownload() {
            if (videoInfoModel == null) {
                VideoInfoModel model = getPresenter().getVideoInfo(videoId);

                if (model != null && model.status == VideoStatus.FINISH) {
                    if (TextUtils.isEmpty(model.localVideoPath)) {
                        return false;
                    }
                    File file = new File(model.localVideoPath);
                    if (file.isFile() && file.exists()) {
                        return true;
                    }
                }
                return false;
            } else {
                if (videoInfoModel.status == VideoStatus.FINISH) {
                    if (TextUtils.isEmpty(videoInfoModel.localVideoPath)) {
                        return false;
                    }
                    File file = new File(videoInfoModel.localVideoPath);
                    if (file.isFile() && file.exists()) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            NetUtils.NetState netStatus = NetUtils.getNetState();
            if (netStatus == NetUtils.NetState.NET_NO) {   //无网络

                if (!isVideoDownload()) {
                    ll_mvv_nowifi.setVisibility(View.VISIBLE);
                    tv_mvv_no_wifi.setText(R.string.avd_no_net_str);
                    tv_mvv_rich_go.setText(R.string.avd_ref_str);
                }
                getPresenter().updataNowPlayTime(vrf_avd.getCurrentTime());

            } else if (netStatus == NetUtils.NetState.NET_WIFI) {   //wifi
                ll_mvv_nowifi.setVisibility(View.GONE);
                if (videoInfoModel != null && isSetDataSource) {
                    if (vrf_avd.getCurrentStatus() != 5) {
                        vrf_avd.seekTo(videoInfoModel.currentTime);
                        vrf_avd.play();
                    }
                }
            } else {   //手机流量
                if (videoInfoModel != null && isSetDataSource) {
                    getPresenter().updataNowPlayTime(vrf_avd.getCurrentTime());
                    vrf_avd.pause();
                }
                ll_mvv_nowifi.setVisibility(View.VISIBLE);
                tv_mvv_no_wifi.setText(R.string.avd_no_wifi_str);
                tv_mvv_rich_go.setText(R.string.avd_rich_go_str);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            sv_avd.setVisibility(View.GONE);
            title_videodetail_lay.setVisibility(View.GONE);
            isFullscreen = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            sv_avd.setVisibility(View.VISIBLE);
            title_videodetail_lay.setVisibility(View.VISIBLE);
            isFullscreen = false;
        }
        changeVideoViewSize(newConfig.orientation);
    }

    /**
     * 根据横竖屏相应操作
     */
    private void initConfiguration() {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            title_videodetail_lay.setVisibility(View.GONE);
            videoVideplayEditCommentLay.setVisibility(View.GONE);

        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            title_videodetail_lay.setVisibility(View.VISIBLE);
            videoVideplayEditCommentLay.setVisibility(View.VISIBLE);
        }

    }


}
