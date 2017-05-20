package app.privatefund.com.vido.mvc;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.OKHTTP;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.CommentDialog;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.tencent.qcload.playersdk.ui.UiChangeInterface;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.adapter.CommentAdapter;
import app.privatefund.com.vido.mvc.bean.Comment;
import app.privatefund.com.vido.mvc.bean.MyTaskBean;
import app.privatefund.com.vido.mvc.bean.PostZan;
import app.privatefund.com.vido.mvc.bean.ProductBean;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.litener.AudioListener;
import app.privatefund.com.vido.mvc.services.DownloadService;
import app.privatefund.com.vido.mvc.services.PlayerService;
import app.privatefund.com.vido.mvc.utils.ToolsUtils;
import app.privatefund.com.vido.mvc.widget.ListViewForScrollView;
import app.privatefund.com.vido.mvc.widget.ProgressWheel;



/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-19:17
 */

public class PlayVideoActivity extends BaseMvcActivity implements View.OnClickListener {


    private static final String TAG = PlayVideoActivity.class.getSimpleName();
    //intent传递过来的视频id数据
    public static final String videoIdtag = "videoId";
    //保存传递过来的视频
    private String inVideoId;

    private int playerCurrentTime;
    private VideoReceiver videoReceiver;
    private boolean returnFlag = false;
    private String SDVideoUrl;
    //    private DatabaseUtils dbUtils;
    //	private boolean pauseFlag = false;
    private boolean keyFlag = false;
    private HomeKeyRecevier homeKeyReceiver;
    private IntentFilter homeKeyFilter;
    private String tencentVideoId;
    private boolean windowFlag; //是否出现小球
    private ConnectionChangeReceiver connectionChangeReceiver;
    private boolean isFirst = true;
    private LinearLayout nowifiLayout;
    private TextView tuhaoplay;
    private String HDVideoUrl;
    private ImageView nowifiBg;
    private RelativeLayout qingxiduLayout;
    private TextView huancun, huancunB, huancuncount;
    private ImageView close_qingxidu, huancunIcon;
    private TextView huancungaoqing;
    private TextView huancunbiaoqing;
    private LinearLayout open_huancun_list;
    private RelativeLayout huancunLayout;
    private boolean localVideoFlag = false;  //播放本地文件标志
    private TextView wifiStatus;
    private int wifistate = 3;
    private AudioListener audioListener = new AudioListener();
    private boolean stopFlag = false;   //暂停播放标记
    private boolean windowChangeFlag = false;
    private long startTime;
    private String videoName = "";
    private RelativeLayout titleLayout;
    private ListViewForScrollView commentList;
    private ArrayList<Comment> commentData;
    private TextView creatTime;
    private TextView persenter;
    private LinearLayout presenterLayout;
    private LinearLayout productLayout;
    private TextView checkMore;
    private TextView commentB;
    private TextView shareB;
    private LinearLayout belowMenu;

    private RelativeLayout commentLayout;
    private CommentAdapter commentAdapter;
    private TextView productName;
    private TextView shouyijizhun;
    private TextView shengyushijian;
    private TextView chanpinqixian;
    private TextView shengyuedu;
    private RelativeLayout videoLayout;
    private String schemeId;
    private Button sendCommentC;
    private EditText editCommentC;
    private RelativeLayout commentNullLayout;

    private SchoolVideo schoolVideo;

    private String shareUrl;
    private String videoTitle;
    private String videoContent;
    private String shareImage;
    private LinearLayout commentBgC;
    private String canShare;
    private ImageView playZhiyin;
    private static VideoRootFrame player;
    private TextView title, proDetial;
    private TextView isZan;
    private int isLike;
    private String videoId;
    private String likes;

    private ScrollView scrollView;
    private ImageView imageView, yincang;
    private ProgressWheel progressWheel;
    private int index;

    /**
     * 视频分享的dialog
     *
     * @param @wyk
     */
//    private CommonShareDialog commonShareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        inVideoId = getIntent().getStringExtra("videoId");
//        dbUtils = new DatabaseUtils(this);
        bindView();
        requestData();
    }

    private void bindView() {
        initRegisterTitleBar();
        showTileMid("视频详情");
        showTileLeft();
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnFlag = true;
                finish();
            }
        });

        checkMore = (TextView) findViewById(R.id.check_more);
        commentList = (ListViewForScrollView) findViewById(R.id.comment_list);

        View headview = getLayoutInflater().inflate(R.layout.video_list_head, null);
        commentList.addHeaderView(headview);

        commentBgC = (LinearLayout) findViewById(R.id.comment_c);
        editCommentC = (EditText) findViewById(R.id.edit_comment_c);
        sendCommentC = (Button) findViewById(R.id.send_comment);
        commentLayout = (RelativeLayout) findViewById(R.id.comment_layout);
        belowMenu = (LinearLayout) findViewById(R.id.below_menu);
        commentB = (TextView) findViewById(R.id.pinglun);
        shareB = (TextView) findViewById(R.id.share);
        productLayout = (LinearLayout) headview.findViewById(R.id.product_layout);
        persenter = (TextView) findViewById(R.id.presenter);
        creatTime = (TextView) headview.findViewById(R.id.creat_time);
        titleLayout = (RelativeLayout) findViewById(R.id.title);
        player = (VideoRootFrame) findViewById(R.id.play);
        title = (TextView) findViewById(R.id.play_page_title);
        proDetial = (TextView) headview.findViewById(R.id.play_page_detial);
        isZan = (TextView) findViewById(R.id.play_page_zan);
        shengyushijian = (TextView) headview.findViewById(R.id.shengyushijian);
        chanpinqixian = (TextView) headview.findViewById(R.id.chanpinqixian);
        shengyuedu = (TextView) headview.findViewById(R.id.shengyuedu);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        imageView = (ImageView) findViewById(R.id.imagebanner);
        yincang = (ImageView) findViewById(R.id.yincang);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        nowifiLayout = (LinearLayout) findViewById(R.id.nowifi_layout);
        tuhaoplay = (TextView) findViewById(R.id.tuhao_play);
        nowifiBg = (ImageView) findViewById(R.id.nowifi_bg);
        qingxiduLayout = (RelativeLayout) findViewById(R.id.qingxidu_layout);
        huancun = (TextView) headview.findViewById(R.id.huancun_text);
        huancunB = (TextView) findViewById(R.id.huancun);
        shouyijizhun = (TextView) headview.findViewById(R.id.shouyijizhun);
        huancunLayout = (RelativeLayout) headview.findViewById(R.id.huancun_layout);
        huancungaoqing = (TextView) findViewById(R.id.gaoqing);
        videoLayout = (RelativeLayout) findViewById(R.id.video_layout);
        huancunbiaoqing = (TextView) findViewById(R.id.biaoqing);
        close_qingxidu = (ImageView) findViewById(R.id.close_qingxidu);
        open_huancun_list = (LinearLayout) findViewById(R.id.open_huancun_list);
        huancuncount = (TextView) findViewById(R.id.huancun_count);
        huancunIcon = (ImageView) findViewById(R.id.huancun_icon);
        wifiStatus = (TextView) findViewById(R.id.wifi_state);
        presenterLayout = (LinearLayout) headview.findViewById(R.id.presenter_layout);
        productName = (TextView) headview.findViewById(R.id.product_name);
        commentNullLayout = (RelativeLayout) findViewById(R.id.comment_null_layout);
        playZhiyin = (ImageView) findViewById(R.id.play_zhiyin);
        playZhiyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playZhiyin.setVisibility(View.GONE);
            }
        });

//        String isZhiyin = SPSave.getInstance(this).getString("v5.3.0play");
//        if (null != isZhiyin) {
//            playZhiyin.setVisibility(View.GONE);
//        } else {
//            SPSave.getInstance(this).putString("v5.3.0play", "5.3.0zhiyin");
//            playZhiyin.setVisibility(View.VISIBLE);
//        }

        tuhaoplay.setOnClickListener(this);
        shareB.setOnClickListener(this);
        WindowManager wm = PlayVideoActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int hight = width * 9 / 16;
        ViewGroup.LayoutParams params = player.getLayoutParams();
        params.height = hight;
        player.setLayoutParams(params);
        imageView.setLayoutParams(params);
        nowifiLayout.setLayoutParams(params);
    }

    private void initTitleButton() {
        //todo 记得修改titleButton
        if (AppManager.isInvestor(this)) {
            showTileRight("分享");
            titleRight.setVisibility(View.VISIBLE);
            showCompoundDrawable(titleRight, ContextCompat.getDrawable(baseContext, R.drawable.fenxiang_share_nor));
            titleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(shareUrl)) {
//                        DataStatistApiParam.onVideoShareToC(videoName);
                        if (canShare.equals("y")) {
                            //todo 视频分享
//                            DialogUtils.videoShare(context, videoName, videoContent, shareUrl);
                        } else {
//                            DialogUtils.videoShare(context, "n", videoName, videoContent, shareUrl);
                        }
                    }

                }
            });
        } else {
            titleRight.setVisibility(View.GONE);
        }
    }

    private void requestData() {

        addSubscription(ApiClient.getToCVideoInfo(inVideoId, baseContext.getApplicationContext(), AppManager.isInvestor(baseContext.getApplicationContext()) ? "c" : "b").subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String sss = object.getString("rows");
                    schoolVideo = new Gson().fromJson(sss, SchoolVideo.class);
                    bindData(schoolVideo);
                    initTitleButton();
                    scrollView.scrollTo(0, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.getvideoerror));
            }
        }));


    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(connectionChangeReceiver, filter);
    }

    private void unregisterReceiver() {
        this.unregisterReceiver(connectionChangeReceiver);
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3000) {
//                DatabaseUtils databaseUtils = new DatabaseUtils(PlayVideoActivity.this);
//                MyTaskBean myTask = databaseUtils.getMyTask("学习视频");
//                if (myTask != null) {
//                    getCoinTask(databaseUtils, myTask);
//                }
            }
        }

        ;
    };

    private void showCompoundDrawable(TextView textView, Drawable drawable) {
        if (textView != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        long endTime = System.currentTimeMillis();
        String time = String.format("%d", (endTime - startTime) / 1000);
        watchTimeDataStatist(time);

        Log.i(TAG, "onDestroy");
        try {
            if (null != connectionChangeReceiver) {
                unregisterReceiver();
            }

            if (null != videoReceiver) {
                unregisterReceiver(videoReceiver);
            }

            if (null != homeKeyReceiver) {
                unregisterReceiver(homeKeyReceiver);
            }

            audioListener.release();

            Log.i(TAG, "1.onDestroy playerCurrentTime=" + playerCurrentTime);
            int currenttime = player.getCurrentTime();

            if (null != player) {
                player.release();
                player = null;
                System.gc();
            }

            Log.i(TAG, "1.1 onDestroy player");
//            SchoolVideo video = dbUtils.getSchoolVideo(tencentVideoId);
//            if (null != video) {
//                if (currenttime > 0) {
//                    video.setCurrentTime(currenttime);
//                } else {
//                    video.setCurrentTime(playerCurrentTime);
//                }
//
//                if (video.getStatus() == 2 & video.getEncrypt() == 1) {
//                    video.setEncrypt(encrptyFile(video.getDownloadtype()));
//                }
//                Log.i(TAG, "2.onDestroy playerCurrentTime=" + playerCurrentTime + ",currenttime=" + currenttime);
//                dbUtils.saveSchoolVideo(video);
//            }
//            if(null != handler){
//                handler = null;
//            }

//            List<SchoolVideo> list = dbUtils.getSchoolVideoList();
//            Log.i(TAG, "onDestroy videolist size=" + list.size());
//            SchoolVideo video = dbUtils.getSchoolVideo(tencentVideoId);
//            if (null != video) {
//                playerCurrentTime = video.getCurrentTime();
//                Log.i(TAG, "SchoolVideo playerCurrentTime= " + playerCurrentTime);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.end(client, getIndexApiAction());
        long endTime = System.currentTimeMillis();
        if (endTime - startTime < 300000) {
            handler.removeMessages(3000);
        }
        Log.i(TAG, "onStop");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.disconnect();
    }

//    private void getCoinTask(final DatabaseUtils databaseUtils, final MyTaskBean myTaskBean) {
//        JSONObject js = new JSONObject();
//        try {
//            js.put("taskName", myTaskBean.getTaskName());
//            js.put("taskType", myTaskBean.getTaskType() + "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new CoinTask(context).start(js.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String ratio = response.getString("ratio");
//                    int coinRatioNum = response.getInt("coinRatioNum");
//                    int coinNum = response.getInt("coinNum");
//                    if (ratio.equals("1.0")) {
//                        Toast.makeText(baseContext, "完成【观看视频】任务，获得" + coinRatioNum + "个云豆", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(baseContext, "完成【观看视频】任务，获得" + coinRatioNum + "（" + coinNum + " X " + ratio + "）个云豆", Toast.LENGTH_SHORT).show();
//                    }
//                    MApplication.getUser().setMyPoint(MApplication.getUser().getMyPoint() + coinRatioNum);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                myTaskBean.setState(2);
//                databaseUtils.updataMyTask(myTaskBean);
//                EventBus.getDefault().post(new RefreshUserinfo());
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    JSONObject js = new JSONObject(error);
//                    String message = js.getString("message");
//                    if (message.contains("已领取过云豆")) {
//                        myTaskBean.setState(2);
//                        databaseUtils.updataMyTask(myTaskBean);
//                    }
////                    new MToast(context).show(message, 0);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    protected void onResume() {
        super.onResume();
        showCacheStatus();
        showDownloadCount();
    }

    protected void onRestart() {
        super.onRestart();
        qingxiduLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        initViewBC();
        try {
            if (keyFlag) {
                ToolsUtils.serviceStop(this);
                keyFlag = false;
            }
            Log.i(TAG, "onRestart playerCurrentTime=" + playerCurrentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        bindData();
    }


    protected void onStart() {
        super.onStart();
    }

    private void play() {
        List<VideoInfo> videos = new ArrayList<VideoInfo>();
        VideoInfo v1 = new VideoInfo();
        v1.description = "标清";
        v1.type = VideoInfo.VideoType.MP4;
        v1.url = SDVideoUrl;
        videos.add(v1);
        VideoInfo v2 = new VideoInfo();
        v2.description = "高清";
        v2.type = VideoInfo.VideoType.MP4;
        v2.url = HDVideoUrl;
        videos.add(v2);
        player.play(videos);
        nowifiLayout.setVisibility(View.GONE);
        if (nowifiBg != null) {
            nowifiBg.setVisibility(View.INVISIBLE);
        }

//        SchoolVideo schoolVideo = dbUtils.getSchoolVideo(tencentVideoId);
//        if (schoolVideo != null && schoolVideo.getCurrentTime() > 0) {
//                player.seekTo(schoolVideo.getCurrentTime());
//        }

        player.setToggleFullScreenHandler(new UiChangeInterface() {
            @Override
            public void OnChange() {
                if (player.isFullScreen()) { // 竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    WindowManager wm = PlayVideoActivity.this.getWindowManager();

                    int width = wm.getDefaultDisplay().getWidth();
                    int hight = width * 9 / 16;
                    ViewGroup.LayoutParams params = player.getLayoutParams();
                    ViewGroup.LayoutParams relativeLayoutParams = videoLayout.getLayoutParams();
                    relativeLayoutParams.height = hight;
                    params.height = hight;
                    player.setLayoutParams(params);
                    imageView.setLayoutParams(params);
                    nowifiLayout.setLayoutParams(params);
                    videoLayout.setLayoutParams(relativeLayoutParams);
                    resetStatusBar();
                    scrollView.setVisibility(View.VISIBLE);
                    yincang.setVisibility(View.VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    initViewBC();
                } else { // 横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    scrollView.setVisibility(View.GONE);
                    yincang.setVisibility(View.INVISIBLE);
                    titleLayout.setVisibility(View.GONE);
                    belowMenu.setVisibility(View.GONE);
                    commentLayout.setVisibility(View.GONE);
                    WindowManager wm = PlayVideoActivity.this.getWindowManager();
                    int width = wm.getDefaultDisplay().getWidth();
//                    int hight = width * 9 / 16;
//                    int hight = wm.getDefaultDisplay().getHeight();
                    int hight = wm.getDefaultDisplay().getHeight();
                    ViewGroup.LayoutParams params = player.getLayoutParams();
                    ViewGroup.LayoutParams relativeLayoutParams = videoLayout.getLayoutParams();
                    relativeLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    params.height = hight * 2;
                    player.setLayoutParams(params);
                    imageView.setLayoutParams(params);
                    nowifiLayout.setLayoutParams(params);
                    videoLayout.setLayoutParams(relativeLayoutParams);
                    View view = findViewById(R.id.main_main_container);
                    if (null != view) {
                        view.setPadding(0, 0, 0, 0);
                    }
                }
            }
        });
    }

    private boolean isFirstTime = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void bindData(SchoolVideo schoolVideo) {

        try {
            int serviceLive = getIntent().getIntExtra("ServiceLive", 0);
            index = getIntent().getIntExtra("index", 0);

            if (schoolVideo != null) {
                Log.i(TAG, "schoolVideo tencentVideoId= " + schoolVideo.getTencentVideoId());
                long finalPlayTime = System.currentTimeMillis();
                schoolVideo.setFinalPlayTime(finalPlayTime);
                shareUrl = schoolVideo.getShareUrl();
                shareImage = schoolVideo.getShareImg();
                videoTitle = schoolVideo.getShortName();
                videoContent = schoolVideo.getVideoSummary();
                SDVideoUrl = schoolVideo.getSDVideoUrl();
                likes = schoolVideo.getLikes();
                HDVideoUrl = schoolVideo.getHDVideoUrl();
                String videoSummary = schoolVideo.getVideoSummary();
                videoId = schoolVideo.getVideoId();
                isLike = schoolVideo.getIsLiked();
                String tencentAppId = schoolVideo.getTencentAppId();
                tencentVideoId = schoolVideo.getTencentVideoId();
                String coverImageUrl = schoolVideo.getCoverImageUrl();
                String shortName = schoolVideo.getShortName();
                videoName = schoolVideo.getVideoName();
                persenter.setText(schoolVideo.getPresenter());
                canShare = schoolVideo.getCanShare();
                if (schoolVideo.getComment() != null && schoolVideo.getComment().size() > 0) {
                    commentNullLayout.setVisibility(View.GONE);
                } else {
                    commentNullLayout.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(schoolVideo.getPresenter())) {
                    presenterLayout.setVisibility(View.GONE);
                }
                if (schoolVideo.getProduct() == null || schoolVideo.getProduct().size() == 0) {
                    productLayout.setVisibility(View.GONE);
                } else {
                    ProductBean productBean = schoolVideo.getProduct().get(0);
                    schemeId = productBean.getSchemeId();
                    productName.setText(productBean.getProductName());


                    if (productBean.getProductType().equals("1")) {
                        shouyijizhun.setText("收益基准：" + productBean.getIncomeMax());
                        shengyuedu.setText("剩余额度：" + productBean.getRemainingAmountStr());
                        shengyushijian.setText("剩余时间：" + getDeadLine(productBean.getRaiseEndTime()));
                        chanpinqixian.setText("产品期限：" + productBean.getTerm());
                    } else {
                        shouyijizhun.setText("累计净值：" + productBean.getNetAll());
                        shengyuedu.setText("剩余额度：" + productBean.getRemainingAmountStr());
                        shengyushijian.setText("剩余时间：" + getDeadLine(productBean.getRaiseEndTime()));
                        chanpinqixian.setText("产品期限：" + productBean.getTerm());
                    }

                }

                if (schoolVideo.getComment() != null && schoolVideo.getComment().size() > 3) {
                    checkMore.setVisibility(View.VISIBLE);
                }

                editCommentC.setOnClickListener(this);
                checkMore.setOnClickListener(this);
                commentB.setOnClickListener(this);
                productLayout.setOnClickListener(this);
                initViewBC();

                commentLayout.setOnClickListener(this);
                sendCommentC.setOnClickListener(this);
                commentBgC.setOnClickListener(this);

                SchoolVideo video = new SchoolVideo();//dbUtils.getSchoolVideo(tencentVideoId);

                if (null != video) {
                    //判断文件是否存在，修改数据库
                    video = ToolsUtils.isFileExist(this, video);

                    schoolVideo.setId(video.getId());

                    if (video.getStatus() != 2) {
                        registerReceiver();
                    }

                    if (video.getStatus() == 1) { //下载完成:2,下载中：1
//                        huancun.setText(getString(R.string.local_video_free_caching));
                        if (video.getDownloadtype() == 0) { //下载清晰度  0 表示高清 1 标清
                            setGaoqingTextBlue();
                        } else {
                            setBiaoqingTextBlue();
                        }
                        setViewDisable();
                    } else if (video.getStatus() == 2) {
                        huancun.setText(getString(R.string.local_video_free_cached));
                        huancunIcon.setBackgroundResource(R.drawable.yihuancun_c);
                        huancunB.setText(getString(R.string.local_video_free_cached));
                        huancunB.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.yihuancun_b, 0, 0);
                        huancunB.setEnabled(false);
                        huancunLayout.setEnabled(false);
                        huancun.setEnabled(false);
                        getEncryptStatus(video);
                    } else {
//                            huancunLayout.setVisibility(View.VISIBLE);
//                            huancun.setText(getString(R.string.local_video_free_cache));
                    }

                    //获取上次播放时间
                    playerCurrentTime = video.getCurrentTime();
                    Log.i(TAG, "SchoolVideo playerCurrentTime= " + playerCurrentTime + ",player=" + player);
                } else {
//                    dbUtils.saveSchoolVideo(schoolVideo);
//                    huancun.setText(getString(R.string.local_video_free_cache));
                    registerReceiver();
//                    play();
                }

                commentData = schoolVideo.getComment();
                if (commentData != null && commentData.size() == 4) {
                    commentData.remove(3);
                }
                commentAdapter = new CommentAdapter(this, commentData);
                commentList.setAdapter(commentAdapter);
                creatTime.setText(schoolVideo.getCreateDate() + " | 播放次数 " + schoolVideo.getPlayCount());

                BitmapUtils bu = new BitmapUtils(this);
//                bu.display(imageView, coverImageUrl);
                Imageload.display(baseContext, coverImageUrl, imageView);
                open_huancun_list.setOnClickListener(this);
//                fanhui.setOnClickListener(this);
                huancun.setOnClickListener(this);
                huancunB.setOnClickListener(this);
                huancunLayout.setOnClickListener(this);
//                zanCount.setOnClickListener(this);
                isZan.setOnClickListener(this);
                yincang.setOnClickListener(this);
                close_qingxidu.setOnClickListener(this);
                huancunbiaoqing.setOnClickListener(this);
                huancungaoqing.setOnClickListener(this);

                title.setText(videoName);
                proDetial.setText(videoSummary);
//                zanCount.setText(likes);
                if (TextUtils.isEmpty(schoolVideo.getLikes()) || schoolVideo.getLikes().equals("null")) {
                    isZan.setText("点赞");
                } else {
                    isZan.setText("点赞" + schoolVideo.getLikes());
                }

                if (isLike != 0) {
                    isZan.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.video_like, 0, 0);
                } else {
                    isZan.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.video_unlike, 0, 0);
                }
            }
            scrollView.scrollTo(0, 0);

            player.setListener(new PlayerListener() {

                @Override
                public void onError(Exception arg0) {
                    Log.i("PlayVideoActivity", "onError");
                    arg0.printStackTrace();
//                    new MToast(PlayVideoActivity.this).showLoginFailure("播放失败，请重新尝试！");
                    PromptManager.ShowCustomToast(baseContext, "播放失败，请重新尝试！");
                    player.pause();
                    progressWheel.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onStateChanged(int arg0) {
                /*
                 * 1 STATE_IDLE 播放器空闲，既不在准备也不在播放 2 STATE_PREPARING 播放器正在准备 3
				 * STATE_BUFFERING 播放器已经准备完毕，但无法立即播放。此状态
				 * 的原因有很多，但常见的是播放器需要缓冲更多数据才能开始播放 4 STATE_PAUSE 播放器准备好并可以立即播放当前位置
				 * 5 STATE_PLAY 播放器正在播放中 6 STATE_ENDED 播放已完毕
				 */
                    Log.i("PlayVideoActivity", "onStateChanged status=" + arg0);
                    switch (arg0) {
                        case 5:
                            imageView.setVisibility(View.INVISIBLE);
                            progressWheel.setVisibility(View.INVISIBLE);
                            nowifiBg.setVisibility(View.INVISIBLE);
                            nowifiLayout.setVisibility(View.INVISIBLE);
                            Seek(playerCurrentTime);
                            stopFlag = false;
                            if (isFirstTime) {
                                startTime = System.currentTimeMillis();
                                Message msg = new Message();
                                msg.what = 3000;
                                handler.sendMessageDelayed(msg, 300000);
                                isFirstTime = false;
                            }
                            Log.e("PlayState", "5");
                            break;
                        case 3:
                            progressWheel.setVisibility(View.INVISIBLE);
                            Log.e("PlayState", "3");
                            break;
                        case 4:
                            stopFlag = true;
                            Log.e("PlayState", "4");
                            break;
                        default:
                            break;
                    }
                }
            });

            homeKeyReceiver = new HomeKeyRecevier();
            homeKeyFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            registerReceiver(homeKeyReceiver, homeKeyFilter);
            ToolsUtils.serviceStop(this);

//            cacheReceiver = new CacheReceiver();
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Contant.ACTION_CACHE);
//            registerReceiver(cacheReceiver, filter);

            audioListener.setCallback(this, new AudioListener.ICallBack() {
                public void callback() {
                    if (null != player) {
//                        player.pause();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (MApplication.getUser() != null) {
////            String[] param = new String[]{videoName, MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName()};
////            HashMap<String, String> params = DataStatisticsUtils.getParams("1020", "10101", param);
////            DataStatisticsUtils.push(PlayVideoActivity.this, params);
//            DataStatistApiParam.onClickVideoToB(schoolVideo.getVideoName(), schoolVideo.getShortName());
//        }
    }

    private String getDeadLine(String data) {
        java.util.Date end_time = null;
        String dateString = "已结束";

        try {
            end_time = dateFormat.parse(data);
            long l = end_time.getTime() - System.currentTimeMillis();
            int day = (int) (l / 1000 / 60 / 60 / 24);
            int hour = (int) (l / 1000 % (3600 * 24) / 60 / 60);
            int min = (int) (l / 1000 % 3600 / 60);

            if (day > 0) {
                if (hour % 24 > 17) {
                    dateString = (day + 1) + "天";
                } else {
                    dateString = day + "天";
                }
            } else if (hour > 0) {
                dateString = hour + "小时";
            } else if (min >= 0) {
                dateString = min + "分";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initViewBC() {
        if (AppManager.isAdViser(baseContext)) {
            belowMenu.setVisibility(View.VISIBLE);
            commentLayout.setVisibility(View.GONE);
            yincang.setBackgroundResource(R.drawable.yincang_b);
            huancun.setVisibility(View.INVISIBLE);
            huancunIcon.setVisibility(View.INVISIBLE);
            checkMore.setTextColor(0xffea1202);
            checkMore.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.comment_more_b, 0, 0, 0);
        } else {
            commentLayout.setVisibility(View.VISIBLE);
            belowMenu.setVisibility(View.GONE);
            yincang.setBackgroundResource(R.drawable.service_play_icon);
            huancun.setVisibility(View.VISIBLE);
            checkMore.setTextColor(0xfff47900);
            checkMore.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.comment_more, 0, 0, 0);
            huancunIcon.setVisibility(View.VISIBLE);
        }
        scrollView.setVisibility(View.VISIBLE);
    }

    boolean seekFlag = true;

    protected void Seek(int i) {
        // TODO Auto-generated method stub
        if (seekFlag) {
            player.seekTo(i);
        }
        seekFlag = false;
    }

    @Override
    protected void onPause() {
        try {
            // TODO Auto-generated method stub
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int count;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zan);

        if (v.getId() == R.id.comment_c) {
            comment();
        } else if (v.getId() == R.id.send_comment) {
            comment();
        } else if (v.getId() == R.id.edit_comment_c) {
            comment();
        } else if (v.getId() == R.id.check_more) {
            requestComment();
            checkMore.setVisibility(View.INVISIBLE);
        } else if (v.getId() == R.id.product_layout) {
            jumpProduct();

        } else if (v.getId() == R.id.comment_layout) {
            comment();
        } else if (v.getId() == R.id.pinglun) {
            comment();
        } else if (v.getId() == R.id.share) {
            if (!TextUtils.isEmpty(shareUrl)) {
                ShareCommonShow();
            }

        } else if (v.getId() == R.id.play_page_zan) {
            if (isLike == 0) {
                count = Integer.parseInt(likes);
                isZan.setText("已赞" + (++count));

                isZan.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.video_like, 0, 0);
//                    isZan.setAnimation(animation);
                startTask();
                isLike = 1;
//                EventBus.getDefault().post(new PostZan(count, tencentVideoId));
            }
            //todo 此处有，埋点 需要注意
            if (AppManager.isInvestor(baseContext)) {
//                    DataStatistApiParam.onVideoSupportToC(videoName);
            } else {
//                    DataStatistApiParam.onVideoSupportToB(videoName);
            }
        } else if (v.getId() == R.id.yincang) {
            windowFlag = true;
            serviceStart();
            //todo 此处有埋点
            if (AppManager.isInvestor(baseContext)) {
//                    DataStatistApiParam.onVideoSmootToC(videoName);
            } else {
//                    DataStatistApiParam.onVideoSmootToB(videoName);
            }
            finish();
        } else if (v.getId() == R.id.tuhao_play) {
            if (wifistate == Contant.NET_STAUS_MOBILE) {
                if (isFirst) {
                    play();
                    isFirst = false;
                } else {
                    player.play();
                }
                nowifiLayout.setVisibility(View.INVISIBLE);
                nowifiBg.setVisibility(View.INVISIBLE);
            } else {
//                break;
            }
        } else if (v.getId() == R.id.huancun_layout) {
            clickHuancun();
//                String[] param2 = new String[]{"下载", MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName()};
//                HashMap<String, String> params2 = DataStatisticsUtils.getParams("1021", "10103", param2);
//                DataStatisticsUtils.push(PlayVideoActivity.this, params2);
            //todo 此处有埋点
//            if (AppManager.isInvestor(baseContext)) {
//                DataStatistApiParam.onVideoDetailDownLoadToC(schoolVideo.getVideoName());
//            } else {
//                DataStatistApiParam.onVideoDetailDownLoadToB(schoolVideo.getVideoName());
//            }
        } else if (v.getId() == R.id.close_qingxidu) {
            AnimationSet animationSet1 = new AnimationSet(true);
            TranslateAnimation translateAnimation1 =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f);
            translateAnimation1.setDuration(600);
            animationSet1.addAnimation(translateAnimation1);
            qingxiduLayout.setVisibility(View.GONE);
            qingxiduLayout.startAnimation(animationSet1);

            scrollView.setVisibility(View.VISIBLE);
            initViewBC();
        } else if (v.getId() == R.id.biaoqing) {
            setViewDisable();
            AnimationSet animationSet2 = new AnimationSet(true);
            TranslateAnimation translateAnimation2 =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, -0.5f);
            translateAnimation2.setDuration(300);
            TranslateAnimation translateAnimation3 =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, -0.5f,
                            Animation.RELATIVE_TO_PARENT, 1f);
            translateAnimation3.setStartOffset(300);
            translateAnimation3.setDuration(500);
            animationSet2.addAnimation(translateAnimation2);
            animationSet2.addAnimation(translateAnimation3);
            animationSet2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    huancunbiaoqing.setBackgroundColor(0x99999999);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setBiaoqingTextBlue();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            huancunbiaoqing.startAnimation(animationSet2);
            wifiStatus(1);
            showDownloadCountAdd();
        } else if (v.getId() == R.id.gaoqing) {
            setViewDisable();
            AnimationSet animationSet3 = new AnimationSet(true);
            TranslateAnimation translateAnimation5 =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, -0.5f);
            translateAnimation5.setDuration(300);
            TranslateAnimation translateAnimation6 =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, -0.5f,
                            Animation.RELATIVE_TO_PARENT, 1f);
            translateAnimation6.setStartOffset(300);
            translateAnimation6.setDuration(500);
            animationSet3.addAnimation(translateAnimation5);
            animationSet3.addAnimation(translateAnimation6);
            animationSet3.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    huancungaoqing.setBackgroundColor(0x99999999);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setGaoqingTextBlue();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            huancungaoqing.startAnimation(animationSet3);
            wifiStatus(0);
//                ToolsUtils.toDownloadService(this,tencentVideoId,0);
            showDownloadCountAdd();
        } else if (v.getId() == R.id.open_huancun_list) {
            qingxiduLayout.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, LocalVideoActivity.class);
            startActivity(intent);
            keyFlag = true;
            serviceStart();
        } else if (v.getId() == R.id.huancun_text) {
            clickHuancun();
//            //todo 此处有埋点
//            if (AppManager.isInvestor(baseContext)) {
//                DataStatistApiParam.onVideoDetailDownLoadToC(schoolVideo.getVideoName());
//            } else {
//                DataStatistApiParam.onVideoDetailDownLoadToB(schoolVideo.getVideoName());
//            }
        } else if (v.getId() == R.id.huancun) {
            clickHuancun();
        } else {
        }

//
    }

    private void jumpProduct() {
        if (AppManager.isAdViser(baseContext)) {//理财师
            JSONObject j = new JSONObject();

            try {
                j.put("schemeId", schemeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            new ProductDetailTask(context).start(j.toString(), new HttpResponseListener() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Gson g = new Gson();
//                    ProductBean productBean = g.fromJson(response.toString(), ProductBean.class);
//                    Intent i = new Intent(context, ProductDetailActivity.class);
//                    i.putExtra(Contant.product, productBean);
//                    context.startActivity(i);
//                    SPSave.getInstance(context).putString("myProductID", productBean.getSchemeId());
//                }
//
//                @Override
//                public void onErrorResponse(String error, int statueCode) {
//
//                }
//            });
        } else {//投资人
//            String url = Domain.baseParentUrl + "/app5.0/apptie/detail.html?schemeId=" + schemeId;
//            Intent i = new Intent(baseContext, PushMsgActivity.class);
//            i.putExtra(Contant.push_message_url, url);
//            i.putExtra(Contant.push_message_title, "产品详情");
//            i.putExtra(Contant.PAGE_SHOW_TITLE, true);
//            startActivityForResult(i, 300);
//
            NavigationUtils.startProductDetailActivity(baseContext, schemeId, "产品详情", 300);
        }

    }

    private void comment() {
        belowMenu.setVisibility(View.GONE);
        commentLayout.setVisibility(View.GONE);
        final CommentDialog commentDialog = new CommentDialog(this) {
            @Override
            public void left() {
                this.dismiss();
                initViewBC();
            }

            @Override
            public void right(String extra) {
                sendComment(extra);
                initViewBC();
                this.dismiss();
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

    private void sendComment(final String extra) {
        JSONObject js = new JSONObject();
        try {
            js.put("commentContent", extra);
            js.put("senderId", AppManager.getUserId(baseContext.getApplicationContext()));
            js.put("id", videoId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addSubscription(ApiClient.videoCommentAdd(extra, AppManager.getUserId(baseContext.getApplicationContext()), videoId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.addcommentsucced));
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject rows = object.getJSONObject("rows");
                    Gson g = new Gson();
                    Comment comment = g.fromJson(rows.toString(), Comment.class);
                    commentData.add(0, comment);
                    commentAdapter.notifyDataSetChanged();
                    commentNullLayout.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.addcommenterror));
            }
        }));
    }

    /**
     * 查看更多評論
     */
    private void requestComment() {

        addSubscription(ApiClient.videoCommentLs(videoId, commentData.get(commentData.size() - 1).getCommentId()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONArray rows = obj.getJSONArray("rows");
                    Gson g = new Gson();
                    ArrayList<Comment> comments = g.fromJson(rows.toString(), new TypeToken<List<Comment>>() {
                    }.getType());
                    commentData.addAll(comments);
                    commentAdapter.notifyDataSetChanged();
                    if (comments != null && comments.size() == 20) {
                        checkMore.setVisibility(View.VISIBLE);
                    } else {
                        checkMore.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", "s");
            }
        }));


    }

    private void startTask() {
        if (isLike == 0) {


            addSubscription(ApiClient.VideoDianZan(videoId).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    String aString = s.toString();
                    aString.substring(3);
                }

                @Override
                protected void onRxError(Throwable error) {
//        String aString = error;
//        aString.substring(3);
                }
            }));


        }
    }

    private void serviceStart() {
        Log.i(TAG, "serviceStart 1.playerCurrentTime=" + playerCurrentTime);
        playerCurrentTime = player.getCurrentTime();

        Intent intent = new Intent(PlayVideoActivity.this, PlayerService.class);
        intent.setAction(Contant.ACTION_PLAY);
        Log.i(TAG, "serviceStart 2.playerCurrentTime=" + playerCurrentTime);
        intent.putExtra("playerCurrentTime", playerCurrentTime);
        intent.putExtra("videoUrl", SDVideoUrl);
        intent.putExtra("tencentVideoId", tencentVideoId);
        intent.putExtra("windowFlag", windowFlag);
        intent.putExtra("stopFlag", stopFlag);
        startService(intent);

        videoReceiver = new VideoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contant.VIDEO_BROADCAST);
        registerReceiver(videoReceiver, filter);

    }


    public class VideoReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            try {
                Bundle bundle = intent.getExtras();
                int currentTime = bundle.getInt("playerCurrentTime");
                stopFlag = bundle.getBoolean("stopFlag");
                if (null != player) {
                    if (currentTime != 0) {
                        playerCurrentTime = currentTime;
                        player.seekTo(playerCurrentTime / 1000);
                    } else {
                        player.seekTo(playerCurrentTime);
                    }
                    if (!stopFlag) {
                        player.play();
                    }
                }
                Log.i(TAG, "videoReceiver playerCurrentTime=" + playerCurrentTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //监控Home键
    class HomeKeyRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null) {
//                    Log.e(TAG, "action:" + action + ",reason:" + reason);
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            Log.i(TAG, " HomeKeyRecevier 短按home键");
                            if (keyFlag == false) {
                                keyFlag = true;
                                windowFlag = false;
                                serviceStart();
                            }
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            Log.i(TAG, "HomeKeyRecevier 多任务键");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监控网络状态
     */
    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int netStatus = ToolsUtils.getWifiStatus(PlayVideoActivity.this);
            if (netStatus == Contant.NET_STAUS_NOT) {   //无网络
                nowifiLayout.setVisibility(View.VISIBLE);
                nowifiBg.setVisibility(View.VISIBLE);
                imageView.setBackgroundColor(0xff333333);
                wifiStatus.setText("网络未连接，请检查网络设置");
                tuhaoplay.setText("刷新重试");
                wifistate = Contant.NET_STAUS_NOT;
                pauseWithSaveCurrent();

            } else if (netStatus == Contant.NET_STAUS_WIFI) {   //wifi
                nowifiLayout.setVisibility(View.INVISIBLE);
                if (nowifiBg != null) {
                    nowifiBg.setVisibility(View.INVISIBLE);
                }
                if (isFirst) {
                    play();
                } else {
                    if (player.getCurrentStatus() == 5) {
                        return;
                    } else {
                        playToCurrent();
                    }
                }
                isFirst = false;
            } else if (netStatus == Contant.NET_STAUS_MOBILE) {   //手机流量
                pauseWithSaveCurrent();
                nowifiLayout.setVisibility(View.VISIBLE);
                if (nowifiBg != null) {
                    nowifiBg.setVisibility(View.VISIBLE);
                }
                wifiStatus.setText("您正在使用非WIFI网络，播放将产生费用...");
                tuhaoplay.setText("土豪请继续");
                wifistate = Contant.NET_STAUS_MOBILE;
            }
        }
    }

    /**
     * 从当前位置播放
     */
    private void playToCurrent() {
//        SchoolVideo schoolVideo = dbUtils.getSchoolVideo(tencentVideoId);
//        int currentTime = schoolVideo.getCurrentTime();
//        if ("null".equals(String.valueOf(currentTime))) {
//            player.seekTo(currentTime);
//        }
//        play();
    }

    /**
     * 暂停并且记录当前位置
     */
    private void pauseWithSaveCurrent() {
//        SchoolVideo schoolVideo = dbUtils.getSchoolVideo(tencentVideoId);
//        if (schoolVideo != null) {
//            schoolVideo.setCurrentTime(player.getCurrentTime());
//        } else {
//            if (getIntent().getSerializableExtra("schoolvideo") != null) {
//                schoolVideo = (SchoolVideo) getIntent().getSerializableExtra("schoolvideo");
//                schoolVideo.setCurrentTime(player.getCurrentTime());
//            }
//        }
//        dbUtils.saveSchoolVideo(schoolVideo);
//        player.pause();
    }

    private void downloadServiceStart(int videotype) {
        //0 表示高清 1 标清
        Intent intent = new Intent(PlayVideoActivity.this, DownloadService.class);
        intent.setAction(Contant.ACTION_DOWNLOAD);
        intent.putExtra("videotype", videotype);
        intent.putExtra("tencentVideoId", tencentVideoId);
        startService(intent);
    }

    //高清文字颜色
    private void setGaoqingTextBlue() {
        huancungaoqing.setBackgroundColor(0x00ffffff);
        huancungaoqing.setTextColor(0xff5ba8f3);
        huancunbiaoqing.setTextColor(0xffe6e6e6);
    }

    private void setBiaoqingTextBlue() {
        huancunbiaoqing.setBackgroundColor(0x00ffffff);
        huancunbiaoqing.setTextColor(0xff5ba8f3);
        huancungaoqing.setTextColor(0xffe6e6e6);
    }

    private void setInitTextColor() {
        huancunbiaoqing.setTextColor(0xff666666);
        huancungaoqing.setTextColor(0xff666666);
    }

    private void setViewDisable() {
        huancunbiaoqing.setEnabled(false);
        huancungaoqing.setEnabled(false);
    }

    private void setViewEnable() {
        huancunbiaoqing.setEnabled(true);
        huancungaoqing.setEnabled(true);
    }

    private int encrptyFile(int downloadtype) {
        boolean ret = ToolsUtils.getInstance().encryptFile(tencentVideoId, downloadtype);
        if (ret) {
            return 2;
        } else {
            return 1;
        }
    }

    private void getEncryptStatus(SchoolVideo video) {
////        if (video.getEncrypt() == 2) {
//        boolean ret = ToolsUtils.getInstance().decryptFile(tencentVideoId, video.getDownloadtype());
//        if (ret) {
//            localVideoFlag = true;
//            video.setEncrypt(1);
//            dbUtils.saveSchoolVideo(video);
//            String localVideoPath = ToolsUtils.getDownloadFilePath(tencentVideoId, video.getDownloadtype());
//            if (video.getDownloadtype() == 0) {
//                HDVideoUrl = localVideoPath;
//                SDVideoUrl = localVideoPath;
//            } else if (video.getDownloadtype() == 1) {
//                HDVideoUrl = localVideoPath;
//                SDVideoUrl = localVideoPath;
//            }
//            video.setSDVideoUrl(SDVideoUrl);
//            video.setHDVideoUrl(HDVideoUrl);
//            nowifiLayout.setVisibility(View.INVISIBLE);
//            play();
//        }
////        }
    }

    private int countCache = 0;

    private void showDownloadCount() {
//        countCache = dbUtils.getSchoolVideoCount();
//        huancuncount.setText(String.valueOf(countCache));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showDownloadCountAdd() {
        countCache++;
        huancuncount.setText(String.valueOf(countCache));
        huancun.setText(getString(R.string.local_video_free_caching));
        huancunB.setText(getString(R.string.local_video_free_caching));
        huancunB.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.huancunzhong_b, 0, 0);
        huancunIcon.setBackgroundResource(R.drawable.huancunzhong_c);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showCacheStatus() {
////
//        SchoolVideo video = dbUtils.getSchoolVideo(tencentVideoId);
//        if (null != video) {
//            video = ToolsUtils.isFileExist(this, video);
//            if (video.getStatus() == 1) { //下载完成:2,下载中：1
//                huancun.setText(getString(R.string.local_video_free_caching));
//                huancunIcon.setBackgroundResource(R.drawable.huancunzhong_c);
//                huancunB.setText(getString(R.string.local_video_free_caching));
//                huancunB.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.huancunzhong_b, 0, 0);
//
//            } else if (video.getStatus() == 0) {
//                setCacheInit();
//            } else if (video.getStatus() == 2) {
//                huancun.setText(getString(R.string.local_video_free_cached));
//                huancunIcon.setBackgroundResource(R.drawable.yihuancun_c);
//                huancunB.setText(getString(R.string.local_video_free_cached));
//                huancunB.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.yihuancun_b, 0, 0);
//                huancunB.setEnabled(false);
//                huancunLayout.setEnabled(false);
//                huancun.setEnabled(false);
//            }
//        } else {
//            setCacheInit();
//        }
    }

    //设置文字和图片显示缓存
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setCacheText() {
        huancun.setText(getString(R.string.local_video_free_cache));
        huancunIcon.setBackgroundResource(R.drawable.huancun_c);
        huancunB.setText(getString(R.string.local_video_free_cache));
        huancunB.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.huancun_b, 0, 0);
    }

    private void setCacheInit() {
        setCacheText();
        setViewEnable();
        setInitTextColor();
    }


    private void wifiStatus(int downloadtype) {
//        SchoolVideo video = dbUtils.getSchoolVideo(tencentVideoId);
//        if (ToolsUtils.getWifiStatus(this) == Contant.NET_STAUS_WIFI) {
//            if (null == video) {
//                dbUtils.saveSchoolVideo(schoolVideo);
//            }
//            ToolsUtils.getInstance().toDownloadService(this, tencentVideoId, downloadtype);
//        } else {
//            new MToast(this).showLoginFailure("已添加到缓存队列，非wifi环境下不自动下载");
//            video.setStatus(1);  //下载中
//            dbUtils.saveSchoolVideo(video);
//        }
    }

    private void clickHuancun() {
        belowMenu.setVisibility(View.GONE);
        commentLayout.setVisibility(View.GONE);
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 1f,
                        Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(600);
        animationSet.addAnimation(translateAnimation);
        scrollView.setVisibility(View.GONE);
        qingxiduLayout.setVisibility(View.VISIBLE);
        qingxiduLayout.startAnimation(animationSet);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            setIntent(intent);
            player.release();
            seekFlag = true;
            requestData();
//            bindData();
            Log.i(TAG, "playerCurrentTime=" + playerCurrentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        player.play();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
//        if (!windowChangeFlag) {
//            AppInfo.setStatusBarHeight(this);
//            windowChangeFlag = true;
//        }
    }

    /**
     * 观看时长埋点
     */
    private void watchTimeDataStatist(String time) {
//        if (SPSave.getInstance(PlayVideoActivity.this).getString(Contant.identify).equals(Contant.IdentityLicaishi)) {
//            DataStatistApiParam.watchVideoTimeB(schoolVideo.getVideoName(), time);
//        } else {
//            DataStatistApiParam.watchVideoTimeB(schoolVideo.getVideoName(), time);
//        }
    }

    /**
     * 视频分享
     */
    private void ShareCommonShow() {
//        context, videoName, videoContent, R.drawable.logoshare, shareUrl
//        BShare bShare = new BShare(videoName, videoContent, R.drawable.logoshare, shareUrl);
//        if (null != commonShareDialog) commonShareDialog = null;
//        commonShareDialog = new CommonShareDialog(PlayVideoActivity.this, bShare, CommonShareDialog.Tag_Style_Vido_WeiXin, new CommonShareDialog.CommentShareListener() {
//            @Override
//            public void onclick() {
//
//            }
//        });
//        commonShareDialog.show();
    }

}
