package app.privatefund.investor.health.mvp.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.Utils;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import butterknife.BindFloat;
import butterknife.BindView;

import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_LOCAL_REF_ONE_OBSERVABLE;
import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_PLAY5MINUTES_OBSERVABLE;

/**
 * @author chenlong
 */
public class IntroduceHealthFragment extends BaseFragment implements PlayerListener {

    @BindView(R2.id.introduce_health_text)
    TextView introduce_health_text;

    @BindView(R2.id.vrf_avd)
    VideoRootFrame videoRootFrame;

    @BindView(R2.id.rl_avd_head)
    RelativeLayout rl_avd_head;

    @BindView(R2.id.iv_mvv_cover)
    ImageView iv_mvv_cover;

    private boolean isSetFullscreenHandler;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        videoRootFrame.setListener(this);
        List<VideoInfo> videos = new ArrayList<>();
        VideoInfo v1 = new VideoInfo();
        VideoInfo v2 = new VideoInfo();
        v1.description = "标清";
        v1.type = VideoInfo.VideoType.MP4;
        v2.description = "高清";
        v2.type = VideoInfo.VideoType.MP4;
        v1.url = "http://flv.bn.netease.com/tvmrepo/2012/7/C/7/E868IGRC7-mobile.mp4";//videoInfoModel.sdUrl;
//        v2.url = //videoInfoModel.hdUrl;
        videos.add(v1);
//        videos.add(v2);
        changeVideoViewSize(Configuration.ORIENTATION_PORTRAIT);
        videoRootFrame.play(videos);
        videoRootFrame.pause();
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

        introduce_health_text.setText("adjfkldjfk;aldjfa;dfjl;adfj;a\n\n\n\n\n\n\n3\n\n\n\n\n\n3\n\n\n\n\n\n\n\n\n\n\n\n\n\n5\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nhjAFDLSFJAKLHDSFJ");
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
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onError(Exception e) {

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
                break;
            case 4:
                break;
            default:
                break;
        }
    }
}
