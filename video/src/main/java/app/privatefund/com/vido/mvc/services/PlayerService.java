package app.privatefund.com.vido.mvc.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.Contant;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.litener.AudioListener;
import app.privatefund.com.vido.mvc.utils.ToolsUtils;
import io.rong.eventbus.EventBus;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-11:44
 */
public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener,View.OnTouchListener{

    private static final String TAG = PlayerService.class.getSimpleName();

    private MediaPlayer mediaplayer = null;
    private int playerCurrentTime;
    private TelephonyManager tm;
    private MyListener listener;
    private boolean ringingFlag = false;
    // 定义浮动窗口布局
    private RelativeLayout mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private float screenWidth;
    private float screenHight;
    private Button yincang;
    private String tencentVideoId;
//    private DatabaseUtils dbUtils;
    private SchoolVideo video;
    private float x;
    private float y;
    private int startX;
    private int startY;
    private int controlledSpace = 20;
    private float mTouchX;
    private float mTouchY;
    private int stateHight;
    private View.OnClickListener mClickListener;
    private boolean startFlag = false;
    private AudioListener audioListener = new AudioListener();
    private boolean stopFlag = false;
    public static final String CURRENT_PLAY_POSITION = "current_play_postion";

    public int onStartCommand(Intent intent, int flags, int startid) {

        Log.i(TAG, "onStartCommand startFlag=" + startFlag);
        try {
            if (intent.getAction().equals(Contant.ACTION_PLAY)) {
                playerCurrentTime = intent.getIntExtra("playerCurrentTime", 0);
                String url = intent.getStringExtra("videoUrl");
                tencentVideoId = intent.getStringExtra("tencentVideoId");
                stopFlag = intent.getBooleanExtra("stopFlag",false);
                Log.i(TAG, "onStartCommand playerCurrentTime=" + playerCurrentTime+",tencentVideoId="+tencentVideoId);
                mediaplayer = new MediaPlayer();
                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaplayer.setDataSource(url);
                mediaplayer.setOnPreparedListener(this);
                mediaplayer.prepareAsync(); // prepare async to not block main thread

//                SPSave.getInstance(MApplication.mContext).putString(Contant.currentTencentVideoId, tencentVideoId);

                tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
                listener = new MyListener();
                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

                startFlag = true;

                if(intent.getBooleanExtra("windowFlag",false)) {
                    createFloatView();
                    yincang.setOnTouchListener(this);
                }

                audioListener.setCallback(this, new AudioListener.ICallBack() {
                    public void callback() {
                        stopPlayer();
                    }
                });
                EventBus.getDefault().register(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

//    public void onEventMainThread(SingeVoice singeVoice) {
//        System.out.println("------current_postion=" + mediaplayer.getCurrentPosition());
//    }

    public void onPrepared(MediaPlayer player) {
        Log.i(TAG, "onPrepared start playerCurrentTime=" + playerCurrentTime);
        if (playerCurrentTime != 0) {
            player.seekTo(playerCurrentTime * 1000);
        }
        if(!stopFlag) {
            player.start();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        try {
            Log.i(TAG, "onDestroy destroy");
            if (mediaplayer != null) {
                if (null != mFloatLayout) {
                    mFloatLayout.setVisibility(View.INVISIBLE);
                    mFloatLayout = null;
                }
                Log.i(TAG, "onDestroy mediaplayer");
                sendTimeBroadcast();
                mediaplayer.stop();
                mediaplayer.release();
                mediaplayer = null;

                audioListener.release();

                System.gc();
            }

            tm.listen(listener, PhoneStateListener.LISTEN_NONE);
            listener = null;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void sendTimeBroadcast() {
        Intent intent = new Intent();
        playerCurrentTime = mediaplayer.getCurrentPosition();
        Log.i(TAG, "sendBroadcast playerCurrentTime=" + playerCurrentTime);
        intent.putExtra("playerCurrentTime", playerCurrentTime);
        intent.putExtra("stopFlag", stopFlag);
        intent.setAction(Contant.VIDEO_BROADCAST);
        sendBroadcast(intent);
    }

    private class MyListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE://空闲状态。
                        Log.i(TAG, "空闲状态 ringingFlag=" + ringingFlag + ",playerCurrentTime=" + playerCurrentTime);
                        if (ringingFlag) {
//                            mediaplayer.seekTo(playerCurrentTime);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                        mediaplayer.start();
                                        ringingFlag = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            Log.i(TAG, "空闲状态 ringingFlag=" + ringingFlag);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING://铃响状态。
                        Log.i(TAG, "铃响状态");
                        stopPlayer();
                        ringingFlag = true;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                        Log.i(TAG, "通话状态");
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPlayer(){
        if (null != mediaplayer) {
            mediaplayer.pause();
        }
    }

    public void onCreate() {
        super.onCreate();
//        Log.i(TAG, "PlayerService onCreate！！！");
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        // 设置window type
        // wmParams.type = LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        screenHight = mWindowManager.getDefaultDisplay().getHeight();

        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        // 获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.video_float_layout, null);
        // 添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        // 浮动窗口按钮
        yincang = (Button) mFloatLayout.findViewById(R.id.yincang);
//        int x = SPSave.getInstance(getApplicationContext()).getInt("WinLocY");
//
//        wmParams.x = (SPSave.getInstance(getApplicationContext()).getInt("WinLocX"));
//        wmParams.y = (SPSave.getInstance(getApplicationContext()).getInt("WinLocY"));
        mWindowManager.updateViewLayout(mFloatLayout, wmParams);

        yincang.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                toStopService();
            }
        });
    }

    private void toStopService(){
        savaPlayCurrentTime();
        ToolsUtils.toPlayVideoActivity(getBaseContext(), video);
        mFloatLayout.setVisibility(View.INVISIBLE);
        stopSelf();
        Log.i(TAG, "yincang setOnClickListener");
    }

    public void savaPlayCurrentTime(){
//        dbUtils = new DatabaseUtils(this);
//        video = dbUtils.getSchoolVideo(tencentVideoId);
//        playerCurrentTime = mediaplayer.getCurrentPosition();
//        if(null != video) {
//            if(playerCurrentTime > 0) {
//                video.setCurrentTime(playerCurrentTime / 1000);
//            }
//            Log.i(TAG, "savaPlayCurrentTime playerCurrentTime="+playerCurrentTime);
//            dbUtils.saveSchoolVideo(video);
//        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        x = event.getRawX();
        y = event.getRawY();

//        stateHight = SPSave.getInstance(this).getInt(Contant.statusBarHeight);
        Log.i(TAG,"Motion stateHight="+stateHight);
        int scroll = 40;
        if (screenHight > 2000) {
            stateHight = 105;
            scroll = 150;
        }

        Log.i(TAG,"Motion event="+event.getAction()+",x="+x+",y="+y+",screenWidth="+screenWidth+",screenHight="+screenHight);
        // TODO Auto-generated method stub
        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                Log.i(TAG,"Motion ACTION_DOWN mTouchX="+mTouchX+",mTouchY="+mTouchY+",startX="+startX+",startY="+startY);
                yincang.setBackgroundResource(R.drawable.xckbf_anxia);
                return true;
            case MotionEvent.ACTION_UP:
                yincang.setBackgroundResource(R.drawable.xckbf);
                double touX = Math.pow((event.getX() - mTouchX), 2);
                double touY = Math.pow((event.getY() - mTouchY), 2);
                Log.i(TAG,"Motion touX="+touX+",touY="+touY);
                Log.i(TAG,"Motion ACTION_UP mTouchX="+mTouchX+",mTouchY="+mTouchY+",getX="+event.getX()+",getY="+event.getY());
                if ((Math.pow((event.getX() - mTouchX), 2) + Math.pow((event.getY() - mTouchY), 2)) > scroll) {
                    mTouchY = mTouchX = 0;
                    if ((Math.pow((x - startX), 2) + Math.pow((y - startY), 2)) > controlledSpace) {
                        if (mClickListener != null) {
                            // mClickListener.onClick(this);
                        }
                    }
//                    Log.i("tag", "x=" + x + " startX+" + startX + " y=" + y + " startY=" + startY);
                    if (x <= screenWidth / 2) {
                        x = 0;
                    } else {
                        x = screenWidth;
                    }

                    wmParams.x = (int) x - yincang.getMeasuredWidth() / 2;
//                    Log.i("service", "RawX" + event.getRawX()+"X" + event.getX());
                    // 减去状态栏的高度
                    wmParams.y = (int) y - yincang.getMeasuredHeight() / 2 - stateHight;
//                    Log.i("service", "RawY" + event.getRawY()+"Y" + event.getY());
                    // 刷新
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
//                    SPSave.getInstance(getApplicationContext()).putInt("WinLocX", wmParams.x);
//                    SPSave.getInstance(getApplicationContext()).putInt("WinLocY", wmParams.y);
                } else {
                    Log.i(TAG,"Motion toStopService touX="+touX+",touY="+touY+",scroll="+scroll);
                    toStopService();
                }

                return false;
            case MotionEvent.ACTION_MOVE:
                wmParams.x = (int) event.getRawX() - yincang.getMeasuredWidth() / 2;
//                Log.i(TAG, "RawX" + event.getRawX()+"X" + event.getX());
                // 减去状态栏的高度

                wmParams.y = (int) event.getRawY() - yincang.getMeasuredHeight() / 2 - stateHight;
//                Log.i(TAG, "RawY" + event.getRawY()+"Y" + event.getY());
                // 刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return true; // 此处必须返回false，否则OnClickListener获取不到监听
            default:
                break;
        }
        return false;
    }

    public void setOnClickListener(View.OnClickListener l) {
        this.mClickListener = l;
    }
}
