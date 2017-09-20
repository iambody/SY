package app.privatefund.com.vido.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.FloatView;

import java.io.IOException;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvp.ui.video.VideoDetailActivity;
import rx.Observable;


public class FloatVideoService extends Service implements MediaPlayer.OnPreparedListener {
    private FloatView floatView;
    private ImageView imageView;
    private DaoUtils daoUtils;
    private VideoInfoModel videoInfoModel;
    private static String mVideoId;
    private MediaPlayer mediaPlayer;
    private String playUrl;
    private boolean ringingFlag;
    private TelephonyManager telephonyManager;

    private ToPhoneStateListener toPhoneStateListener;
    private Observable<Boolean> stopObservable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        toPhoneStateListener = new ToPhoneStateListener();
        telephonyManager.listen(toPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        daoUtils = new DaoUtils(getApplicationContext(), DaoUtils.W_VIDEO);
        imageView = new ImageView(getApplicationContext());
        int size = Utils.convertDipOrPx(getApplicationContext(), 72);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        imageView.setBackgroundResource(R.drawable.select_float_btn);
        floatView = new FloatView(getApplicationContext(), 0, 0, imageView, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        videoInfoModel = daoUtils.getVideoInfoModel(mVideoId);
        playUrl = videoInfoModel.localVideoPath;
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = videoInfoModel.sdUrl;
        }
        stopObservable = RxBus.get().register(RxConstant.SCHOOL_VIDEO_PAUSE, Boolean.class);
        stopObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean b) {
                stopService();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        initMediaPlayer();

        floatView.addToWindow();
        floatView.setFloatViewClickListener(() -> {
            onDestroy();
            Intent intent = new Intent(getApplicationContext(), VideoDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("videoId", mVideoId);
            intent.putExtra("videoCoverUrl", videoInfoModel.videoCoverUrl);
            intent.putExtra("isPlayAnim", false);
            intent.putExtra("comeFrom", 1);
            startActivity(intent);
        });
    }



    //    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (videoInfoModel != null) {

                videoInfoModel.currentTime = videoInfoModel.currentTime + allKeepTime;
                daoUtils.saveOrUpdateVideoInfo(videoInfoModel);
            }

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (daoUtils != null) {
            daoUtils.destory();
            daoUtils = null;
        }

        RxCountDown.stopKeepTime();
        telephonyManager.listen(toPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        toPhoneStateListener = null;

        floatView.removeFromWindow();

        allKeepTime = 0;
    }
    static int allKeepTime = 0;
//    static int cuntTime = 1000000;

    public static void startService(String videoId) {

        if (!Utils.isServiceRunning(BaseApplication.getContext(), FloatVideoService.class.getName())) {
            BaseApplication.getContext().startService(new Intent(BaseApplication.getContext(), FloatVideoService.class));
        }
        mVideoId = videoId;


        RxCountDown.keepTimeDown(new RxCountDown.keepTimeInterface() {
            @Override
            public void allTime(int allTime) {
                allKeepTime=allKeepTime+1;
                Log.i("ssscsaac","时间："+allKeepTime);
            }
        });
    }

    public static void stopService() {

        if (Utils.isServiceRunning(BaseApplication.getContext(), FloatVideoService.class.getName())) {
            BaseApplication.getContext().stopService(new Intent(BaseApplication.getContext(), FloatVideoService.class));
        }

    }


    private void initMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(playUrl);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (videoInfoModel != null && videoInfoModel.currentTime > 0) {
            mediaPlayer.seekTo(videoInfoModel.currentTime * 1000);
        }
        mediaPlayer.start();

    }


    private class ToPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE://空闲状态。
                        if (ringingFlag) {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    mediaPlayer.start();
                                    ringingFlag = false;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING://铃响状态。
                        if (null != mediaPlayer) {
                            mediaPlayer.stop();
                        }
                        ringingFlag = true;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
