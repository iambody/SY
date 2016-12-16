package com.cgbsoft.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.tencent.qcload.playersdk.ui.UiChangeInterface;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频播放器封装
 * Created by xiaoyu.zhang on 2016/12/9 13:24
 * Email:zhangxyfs@126.com
 *  
 */
public class MediaVideoView extends RelativeLayout {

    @BindView(R2.id.rl_mvv_head)
    RelativeLayout rl_mvv_head;

    @BindView(R2.id.vrf_mvv)
    VideoRootFrame vrf_mvv;

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

    @BindView(R2.id.iv_mvv_back)
    ImageView iv_mvv_back;

    @BindView(R2.id.iv_mvv_back_play)
    ImageView iv_mvv_back_play;

    //是否显示返回按钮
    private boolean isBackBtnVisable = true;

    //全屏切换回掉
    private OnFullScreenListener mOnFullScreenListener;
    //获取数据失败的时候
    private OnNetErrorListener mOnNetErrorListener;

    private final int STATE_IDLE = 1, STATE_PREPARING = 2, STATE_BUFFERING = 3, STATE_PAUSE = 4, STATE_PLAY = 5, STATE_ENDED = 6;
    private int playStatus = STATE_IDLE;
    private final int WIFI_NO = 404, NET_NO = 505, PLAY_ERROR = 606;
    private int mNetStatus = WIFI_NO;
    private int mCurrentTime;
    private String hdUrl, sdUrl;
    private boolean isFirstPlay = true;

    public MediaVideoView(Context context) {
        super(context);
        init(context);
    }

    public MediaVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MediaVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_media_video, this);
        ButterKnife.bind(this, this);

        ViewGroup.LayoutParams lp = rl_mvv_head.getLayoutParams();
        lp.width = Utils.getScreenWidth(getContext());
        lp.height = lp.width * 9 / 16;
        rl_mvv_head.setLayoutParams(lp);


        vrf_mvv.setToggleFullScreenHandler(mUiChangeInterface);
        vrf_mvv.setListener(mPlayerListener);
    }

    @OnClick(R2.id.ll_mvv_nowifi)
    void noWifiClick() {
        switch (mNetStatus) {
            case NET_NO:

                break;
            case WIFI_NO:
            case PLAY_ERROR:
                toPlay();
                break;
        }
        ll_mvv_nowifi.setVisibility(GONE);
    }

    private UiChangeInterface mUiChangeInterface = new UiChangeInterface() {
        @Override
        public void OnChange() {
            if (vrf_mvv.isFullScreen()) { // 竖屏
                if (isBackBtnVisable)
                    iv_mvv_back.setVisibility(View.VISIBLE);
            } else { // 横屏
                if (isBackBtnVisable)
                    iv_mvv_back.setVisibility(View.INVISIBLE);
            }
            if (mOnFullScreenListener != null) {
                mOnFullScreenListener.onFullScreenChange(vrf_mvv.isFullScreen());
            }
        }
    };

    private PlayerListener mPlayerListener = new PlayerListener() {

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            mNetStatus = PLAY_ERROR;
            toVisableNoWifi(true);
            vrf_mvv.pause();
            pw_mvv_wait.setVisibility(View.GONE);
        }

        @Override
        public void onStateChanged(int state) {
            playStatus = state;
            switch (state) {
                case STATE_IDLE://播放器空闲，既不在准备也不在播放，初始化状态

                    break;
                case STATE_PREPARING://播放器正在准备
                case STATE_BUFFERING://播放器缓冲中
                    if (pw_mvv_wait.getVisibility() == GONE)
                        pw_mvv_wait.setVisibility(VISIBLE);

                    break;
                case STATE_PAUSE://播放器准备好并可以立即播放当前位置，可以播放,其实就是暂停

                    break;
                case STATE_PLAY://播放中
                    if (iv_mvv_cover.getVisibility() == VISIBLE)
                        iv_mvv_cover.setVisibility(GONE);
                    if (pw_mvv_wait.getVisibility() == VISIBLE)
                        pw_mvv_wait.setVisibility(GONE);
                    break;
                case STATE_ENDED://播放结束
                    if (pw_mvv_wait.getVisibility() == VISIBLE)
                        pw_mvv_wait.setVisibility(GONE);
                    break;
            }
        }
    };

    public void setDataSource(String sdUrl, String hdUrl, String videoCoverUrl) {
        Imageload.display(getContext(), videoCoverUrl, 0, 0, 0, iv_mvv_cover, null, null);
        this.sdUrl = sdUrl;
        this.hdUrl = hdUrl;

        if (!toVisableNoWifi(true)) {
            toPlay();
        }
    }

    public void toPlay() {
        if (isFirstPlay) {
            List<VideoInfo> videos = new ArrayList<>();
            VideoInfo v1 = new VideoInfo();
            v1.description = "标清";
            v1.type = VideoInfo.VideoType.MP4;
            v1.url = sdUrl;
            videos.add(v1);
            VideoInfo v2 = new VideoInfo();
            v2.description = "高清";
            v2.type = VideoInfo.VideoType.MP4;
            v2.url = hdUrl;
            videos.add(v2);
            vrf_mvv.play(videos);
            isFirstPlay = false;
        } else {
            vrf_mvv.play();
        }

    }

    private boolean toVisableNoWifi(boolean isCheckNet) {
        switch (mNetStatus) {
            case PLAY_ERROR:
                tv_mvv_no_wifi.setText(R.string.avd_play_error_str);
                tv_mvv_rich_go.setText(R.string.avd_try_again_str);
                break;
            case WIFI_NO:
                tv_mvv_no_wifi.setText(R.string.avd_no_wifi_str);
                tv_mvv_rich_go.setText(R.string.avd_rich_go_str);
                break;
            case NET_NO:
                tv_mvv_no_wifi.setText(R.string.avd_no_net_str);
                tv_mvv_rich_go.setText(R.string.avd_ref_str);
                break;
        }

        if (mNetStatus == WIFI_NO && NetUtils.getNetState() != NetUtils.NetState.NET_WIFI && isCheckNet
                || mNetStatus == PLAY_ERROR || mNetStatus == NET_NO) {
            ll_mvv_nowifi.setVisibility(View.VISIBLE);
            return true;
        } else {
            ll_mvv_nowifi.setVisibility(View.GONE);
            return false;
        }
    }


    public void setBackBtnVisable(boolean b) {
        this.isBackBtnVisable = b;
    }

    public void setOnFullScreenListener(OnFullScreenListener onFullScreenListener) {
        mOnFullScreenListener = onFullScreenListener;
    }

    public void setOnNetErrorListener(OnNetErrorListener onNetErrorListener){
        mOnNetErrorListener = onNetErrorListener;
    }

    public interface OnFullScreenListener {
        void onFullScreenChange(boolean isFull);
    }

    public interface OnNetErrorListener{
        void onNetError();
    }
}
