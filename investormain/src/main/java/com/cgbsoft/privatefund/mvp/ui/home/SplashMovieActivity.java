package com.cgbsoft.privatefund.mvp.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import app.ndk.com.enter.mvp.ui.LoginActivity;

@Route(RouteConfig.SPLASH_MOVIE)
public class SplashMovieActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView surface_movie;
    private MediaPlayer mediaPlayer;
    private Button start_app;
    private LinearLayout licaishiLayout;
    private Button start_login;
    private Button start_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_movie);
        bindViews();
        SPreference.putString(this, "splash", "s");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void releasePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


 
    private void bindViews() {
        int resID = R.raw.movie_toc;
        
        surface_movie = (SurfaceView) findViewById(R.id.splish_movie);
        start_app = (Button) findViewById(R.id.start_app);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
        SurfaceHolder surfaceHolder = surface_movie.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        start_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlay();

                Intent intent = new Intent(SplashMovieActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashMovieActivity.this.finish();
            }
        });
        
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 把视频画面输出到SurfaceView
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(holder);
        //mp.prepare(); //缓冲。 如果获得本地raw下的视频文件,不能写这个方法，不然会报错。
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

}
