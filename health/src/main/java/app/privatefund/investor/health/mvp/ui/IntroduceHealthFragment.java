package app.privatefund.investor.health.mvp.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.Utils;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceModel;
import app.privatefund.investor.health.mvp.presenter.HealthIntroducePresenter;
import butterknife.BindView;
import rx.Observable;

/**
 * @author chenlong
 */
public class IntroduceHealthFragment extends BaseFragment<HealthIntroducePresenter> implements PlayerListener, HealthIntroduceContract.View{

    @BindView(R2.id.introduce_health_text)
    TextView introduce_health_text;

    @BindView(R2.id.vrf_avd)
    VideoRootFrame videoRootFrame;

    @BindView(R2.id.rl_avd_head)
    RelativeLayout rl_avd_head;

    @BindView(R2.id.iv_mvv_cover)
    ImageView iv_mvv_cover;

    private boolean isSetFullscreenHandler;
    private Observable<Boolean> videoStateObservable;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        videoRootFrame.setListener(this);

        changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);
        if (!isSetFullscreenHandler) {
            isSetFullscreenHandler = true;
            videoRootFrame.setToggleFullScreenHandler(() -> {
                if (videoRootFrame.isFullScreen()) { // 竖屏
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                } else { // 横屏
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            });
        }
        videoStateObservable = RxBus.get().register(RxConstant.PAUSR_HEALTH_VIDEO, Boolean.class);
        videoStateObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean b) {
                if (b){
                    if (videoRootFrame!=null&&videoRootFrame.getCurrentStatus()==5){
                        videoRootFrame.pause();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        getPresenter().introduceHealth();
    }

    @Override
    public void requestDataSuccess(HealthIntroduceModel healthIntroduceModel) {
        if (healthIntroduceModel != null) {
            Imageload.display(getContext(), healthIntroduceModel.getImage(), iv_mvv_cover);
            introduce_health_text.setText(healthIntroduceModel.getText());
            List<VideoInfo> videos = new ArrayList<>();
            VideoInfo v1 = new VideoInfo();
            v1.description = "标清";
            v1.type = VideoInfo.VideoType.MP4;
            v1.url = healthIntroduceModel.getSdVideo();
            videos.add(v1);
            changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);
            videoRootFrame.play(videos);
//            videoRootFrame.pause();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

        }else {
            if (videoRootFrame!=null&&videoRootFrame.getCurrentStatus()==5){
                videoRootFrame.pause();
            }

        }
    }

    private void changeVideoViewSize(int orientation) {
        ViewGroup.LayoutParams lp = rl_avd_head.getLayoutParams();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.width = Utils.getRealScreenWidth(getActivity());
        } else {
            lp.width = Utils.getScreenWidth(getActivity());
        }
        lp.height = lp.width * 9 / 16;
        rl_avd_head.setLayoutParams(lp);
    }

    @Override
    protected HealthIntroducePresenter createPresenter() {
        return new HealthIntroducePresenter(getContext(), this);
    }

    @Override
    public void onError(Exception e) {
        Log.e("PlayVideo ERROR ", e.getMessage());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoRootFrame != null) {
            videoRootFrame.release();
            videoRootFrame = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        videoRootFrame.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoRootFrame.play();
    }

    @Override
    public void onStateChanged(int i) {
           /*
                 * 1 STATE_IDLE 播放器空闲，既不在准备也不在播放 2 STATE_PREPARING 播放器正在准备 3
				 * STATE_BUFFERING 播放器已经准备完毕，但无法立即播放。此状态
				 * 的原因有很多，但常见的是播放器需要缓冲更多数据才能开始播放 4 STATE_PAUSE 播放器准备好并可以立即播放当前位置
				 * 5 STATE_PLAY 播放器正在播放中 6 STATE_ENDED 播放已完毕
				 */
        switch (i) {
            case 5://播放中
                iv_mvv_cover.setVisibility(View.GONE);
                videoRootFrame.pause();
                break;
            case 4:
//                iv_mvv_cover.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
