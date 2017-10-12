package app.privatefund.investor.health.mvp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.SoFileUtils;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.MyLoad;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tencent.qcload.playersdk.util.VideoInfo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceModel;
import app.privatefund.investor.health.mvp.presenter.HealthIntroducePresenter;
import app.privatefund.investor.health.videos.SampleListener;
import app.privatefund.investor.health.videos.SampleVideo;
import app.privatefund.investor.health.videos.SwitchVideoModel;
import butterknife.BindView;
import rx.Observable;

/**
 * @author chenlong
 */
public class IntroduceHealthFragment extends BaseFragment<HealthIntroducePresenter> implements HealthIntroduceContract.View {

    @BindView(R2.id.introduce_health_text)
    TextView introduce_health_text;

    @BindView(R2.id.detail_player)
    SampleVideo detailPlayer;

    @BindView(R2.id.rl_video_all)
    RelativeLayout videoAll;
    @BindView(R2.id.iv_video_first_play)
    ImageView videoFirstPlay;

    private Observable<Integer> videoStateObservable;
    private List<VideoInfo> videos;
    private LoadingDialog mLoadingDialog;
    private ImageView coverImageView;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private boolean isRelease;
    private boolean isReady;
    private boolean clickedNotPlayed;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);
        changeVideoViewSize(getResources().getConfiguration().orientation);
        videoFirstPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReady) {
                    clickedNotPlayed=false;
                    firstPlay();
                } else {
                    clickedNotPlayed=true;
                    loadData();
                }
            }
        });
        videoStateObservable = RxBus.get().register(RxConstant.PAUSR_HEALTH_VIDEO, Integer.class);
        videoStateObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                if (3 != integer&&null!=detailPlayer) {
                    if (detailPlayer.isIfCurrentIsFullscreen()) {
//                        onBackPressed(getContext());
                        detailPlayer.getFullWindowPlayer().onVideoPause();
                    } else {
                        detailPlayer.onVideoPause();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        initVideo();
    }

    private void firstPlay() {
        clickedNotPlayed=false;
        detailPlayer.startPlayLogic();
        videoFirstPlay.setVisibility(View.GONE);
    }

    @Override
    public void showLoadDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }
    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }
    @Override
    protected void loadData() {
        if (!SoFileUtils.isLoadSoFile(getActivity())) {
            RxBus.get().post(RxConstant.DOWN_DAMIC_SO,true);
        } else {
            getPresenter().introduceHealth();
        }
    }

    private void initVideo() {
        //增加封面
        coverImageView = new ImageView(getActivity());
        coverImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //coverImageView.setImageResource(R.mipmap.xxx1);
        detailPlayer.setThumbImageView(coverImageView);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(getActivity(), detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        detailPlayer.setIsTouchWiget(true);
        //detailPlayer.setIsTouchWigetFull(false);
        //关闭自动旋转
        detailPlayer.setRotateViewAuto(false);
        detailPlayer.setLockLand(false);
        detailPlayer.setShowFullAnimation(false);
        detailPlayer.setNeedLockFull(true);
        detailPlayer.setSeekRatio(1);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(getActivity(), true, true);
            }
        });
        detailPlayer.setSwitchShow(false);
        detailPlayer.setStandardVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
                LogUtils.Log("aaa","-----------onQuitFullscreen");
                detailPlayer.setSwitchShow(false);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                detailPlayer.setSwitchShow(true);
                LogUtils.Log("aaa","-----------onEnterFullscreen");
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });
    }

    @Override
    public boolean onBackPressed(Context context) {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(context)) {
            return true;
        }
        return false;
    }

    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(getActivity(), newConfig, orientationUtils);
        }
    }

    @Override
    public void requestDataSuccess(HealthIntroduceModel healthIntroduceModel) {
        if (healthIntroduceModel != null) {
            Imageload.display(getContext(), healthIntroduceModel.getImage(), coverImageView);
            introduce_health_text.setText(healthIntroduceModel.getText());
            if (healthIntroduceModel != null) {//埋点
                DataStatistApiParam.operateHealthIntroduceClick(healthIntroduceModel.getText());
            }

            String source1 =healthIntroduceModel.getSdVideo();
            String name = "标清";
            SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

            String source2 =healthIntroduceModel.getHdVideo();
            String name2 = "高清";
            SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

            List<SwitchVideoModel> list = new ArrayList<>();
            list.add(switchVideoModel);
            list.add(switchVideoModel2);
            detailPlayer.setIjkLibLoader(new MyLoad(SoFileUtils.loadSoToApp(getActivity())));
            isReady = detailPlayer.setUp(list, true, "");
            if (clickedNotPlayed) {
                firstPlay();
            }

//                videos = new ArrayList<>();
//                VideoInfo v1 = new VideoInfo();
//                VideoInfo v2 = new VideoInfo();
//                v1.description = "标清";
//                v1.type = VideoInfo.VideoType.MP4;
//                v2.description = "高清";
//                v2.type = VideoInfo.VideoType.MP4;
//                v1.url = healthIntroduceModel.getSdVideo();
//                v2.url = healthIntroduceModel.getHdVideo();
//                videos.add(v1);
//                videos.add(v2);
//                videoRootFrame.play(videos);
        }
    }

    @Override
    public void requestDataFailure(String errorMsg) {
//        List<VideoInfo> videos = new ArrayList<>();
//        VideoInfo v1 = new VideoInfo();
//        v1.description = "标清";
//        v1.type = VideoInfo.VideoType.MP4;
//        v1.url = "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4";//videoInfoModel.sdUrl;
//        videos.add(v1);
//        changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);
//        videoRootFrame.play(videos);
//        videoRootFrame.pause();
//        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void viewBeHide() {
        super.viewBeHide();
        if (null != detailPlayer) {
            detailPlayer.onVideoPause();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    private void changeVideoViewSize(int orientation) {
        ViewGroup.LayoutParams lp = videoAll.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.width = Utils.getRealScreenWidth(getActivity());
        } else {
            lp.width = Utils.getScreenWidth(getActivity());
        }
        lp.height = lp.width * 9 / 16;
        videoAll.setLayoutParams(lp);
    }

    @Override
    protected HealthIntroducePresenter createPresenter() {
        return new HealthIntroducePresenter(getContext(), this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        isRelease = true;
        LogUtils.Log("aaa","000000000000000----------ondestory");
        GSYVideoPlayer.releaseAllVideos();
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        MobclickAgent.onPageEnd(Constant.SXY_JIANKANG_JS);
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
        MobclickAgent.onPageStart(Constant.SXY_JIANKANG_JS);
    }
}
