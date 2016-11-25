package com.cgbsoft.privatefund.mvp.ui.login;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.login.AnimPresenter;
import com.cgbsoft.privatefund.mvp.view.login.AnimView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动画页
 * Created by xiaoyu.zhang on 2016/11/17 14:57
 * Email:zhangxyfs@126.com
 *  
 */
public class AnimActivity extends BaseActivity<AnimPresenter> implements AnimView, SurfaceHolder.Callback {
    @BindView(R.id.sv_aa)
    SurfaceView sv_aa;//影片显示的view

    @BindView(R.id.btn_aa_start_app)
    Button btn_aa_start_app;//立即启动按钮

    @BindView(R.id.ll_aa_ids)
    LinearLayout ll_aa_ids;

    @BindView(R.id.btn_aa_start_login)
    Button btn_aa_start_login;//登录按钮，和立即启动按钮功能一样

    @BindView(R.id.btn_aa_start_regist)
    Button btn_aa_start_regist;//注册按钮.

    private MediaPlayer mediaPlayer;//播放器

    @Override
    protected int layoutID() {
        return R.layout.activity_anim;
    }

    @Override
    protected void init() {
        int identity = getIntent().getIntExtra(IDS_KEY, -1);
        int resID = identity == IDS_INVERSTOR ? R.raw.movie_toc : R.raw.movie_tob;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);

        SurfaceHolder surfaceHolder = sv_aa.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btn_aa_start_login.setBackgroundResource(R.drawable.select_btn_advister);
        btn_aa_start_regist.setBackgroundResource(R.drawable.select_btn_advister);
        btn_aa_start_app.setBackgroundResource(R.drawable.bg_aa_btn_login_up);

        if (identity == IDS_ADVISER) {
            SPreference.savePlayAdviserAnim(this, true);
            ll_aa_ids.setVisibility(View.VISIBLE);
        } else {
            SPreference.savePlayInverstorAnim(this, true);
            btn_aa_start_app.setVisibility(View.VISIBLE);
        }

        btn_aa_start_app.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //更改为按下时的背景图片
                v.setBackgroundResource(R.drawable.bg_aa_btn_login_down);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //改为抬起时的图片
                v.setBackgroundResource(R.drawable.bg_aa_btn_login_up);
            }
            return false;
        });
    }

    @OnClick(R.id.btn_aa_start_login)
    public void startLogin() {
        openActivity(LoginActivity.class, IDS_ADVISER);
        toDataStatistics(1000, 10121, "登录");
    }

    @OnClick(R.id.btn_aa_start_regist)
    public void startRegister() {
        openActivity(RegisterActivity.class, IDS_ADVISER);
        toDataStatistics(1000, 10122, "注册");
    }

    @OnClick(R.id.btn_aa_start_app)
    public void startApp() {
        openActivity(LoginActivity.class, IDS_INVERSTOR);
        toDataStatistics(2001, 20001, "立即启动");
    }

    private void openActivity(Class clazz, int what) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(IDS_KEY, what);
        startActivity(intent);
        finish();
    }


    @Override
    protected AnimPresenter createPresenter() {
        return new AnimPresenter(this);
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
            mediaPlayer.release();
        }
    }
}
