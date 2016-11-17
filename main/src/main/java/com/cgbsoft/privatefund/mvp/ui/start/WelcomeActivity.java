package com.cgbsoft.privatefund.mvp.ui.start;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.start.WelcomePersenter;
import com.cgbsoft.privatefund.mvp.ui.home.MainPageActivity;
import com.cgbsoft.privatefund.mvp.ui.login.ChoiceIdentityActivity;
import com.cgbsoft.privatefund.mvp.view.start.WelcomeView;

import butterknife.ButterKnife;

/**
 * 欢迎页
 * Created by xiaoyu.zhang on 2016/11/16 09:01
 * Email:zhangxyfs@126.com
 *  
 */
public class WelcomeActivity extends BaseActivity implements WelcomeView {
    private boolean isLaunched;
    private RequestManager requestManager;
    private String[] PERMISSIONS = new String[]{PERMISSION_AUDIO, PERMISSION_CAMERA, PERMISSION_STORAGE, PERMISSION_PHONE, PERMISSION_LOCATION};

    private WelcomePersenter welcomePersenter;

    private WelcomeRunnable mBtnRunnable;
    private WelcomeRunnable mDefaultRunnable;
    private WelcomeRunnable mWaitRunnable;
    private WelcomeRunnable mNoNetRunnable;
    private WelcomeRunnable mTimeOutRunnable;
    private WeakHandler weakHandler;

    private boolean isStop = false;
    private int defaultTime = 7000;
    private int visableBtnTime = 2000;
    private int waitTime = 5000;
    private int noNetTime = 3000;
    private int outOfTime = 2000;

    private final int BUTTON_WAIT = 0;
    private final int DEFAULT_WAIT = 1;
    private final int WAIT = 2;
    private final int NO_NET = 3;
    private final int OUT_TIME = 4;

    private ImageView iv_wel_background;
    private ImageView iv_wel_bottom;
    private Button btn_wel_cancle;

    @Override
    public void before() {
        super.before();
        setIsNeedGoneNavigationBar(true);
        isLaunched = false;//OtherDataProvider.isFirstLaunched(getApplicationContext());
        weakHandler = new WeakHandler();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!OtherDataProvider.isFirstOpenApp(getApplicationContext())) {
            //TODO 不是第一次打开做一些事
        } else {
            SPreference.toDataMigration(this);
        }

        // 缺少权限时, 进入权限配置页面
        if (needPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_ASK_PERMISSIONS, PERMISSIONS);
        } else {
            beforeInit();
        }
    }

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void beforeInit() {
        requestManager = Glide.with(this);

        welcomePersenter = new WelcomePersenter(this);
        welcomePersenter.createFinishObservable();
        welcomePersenter.toInitInfo(this);

        if (isLaunched) {
            //TODO
        } else {
            welecomePage();
        }
    }

    @Override
    public void getDataSucc(AppResources result) {
        //todo 需要拿到显示的图片的url
        String imgUrl = "";
        requestManager.load(imgUrl).skipMemoryCache(true).centerCrop().listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if (weakHandler != null) {
                    weakHandler.postDelayed(mTimeOutRunnable, outOfTime);
                } else {
                    nextPage();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (weakHandler != null) {
                    weakHandler.postDelayed(mBtnRunnable, visableBtnTime);
                    weakHandler.removeCallbacks(mWaitRunnable);
                    weakHandler.postDelayed(mDefaultRunnable, defaultTime);
                } else {
                    nextPage();
                }
                return false;
            }
        }).into(iv_wel_background);
    }

    @Override
    public void getDataError(Throwable error) {
        Log.e("error", error.getMessage());

        if (weakHandler != null)
            weakHandler.postDelayed(mNoNetRunnable, noNetTime);
        else nextPage();
    }

    @Override
    public void finishThis() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (welcomePersenter != null)
            welcomePersenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else {
            beforeInit();
        }
    }

    //不论是否下载或显示都不能超过7秒
    private void welecomePage() {
        setContentView(R.layout.activity_welcome);
        mBtnRunnable = new WelcomeRunnable(BUTTON_WAIT);
        mDefaultRunnable = new WelcomeRunnable(DEFAULT_WAIT);
        mWaitRunnable = new WelcomeRunnable(WAIT);
        mNoNetRunnable = new WelcomeRunnable(NO_NET);
        mTimeOutRunnable = new WelcomeRunnable(OUT_TIME);

        iv_wel_background = ButterKnife.findById(this, R.id.iv_wel_background);
        btn_wel_cancle = ButterKnife.findById(this, R.id.btn_wel_cancle);

        if (Utils.checkNetWork(this)) {
            weakHandler.postDelayed(mWaitRunnable, waitTime);
            welcomePersenter.getData();
        } else {
            weakHandler.postDelayed(mNoNetRunnable, noNetTime);
        }
    }

    //跳到home页
    private void nextPage() {
        isStop = true;
        if (weakHandler == null) {
            return;
        }
        weakHandler.removeCallbacksAndMessages(null);

        iv_wel_background = null;
        btn_wel_cancle = null;
        weakHandler = null;

        if ((!SPreference.isLogin(this) || SPreference.getUserInfoData(this) == null)) {
            openActivity(ChoiceIdentityActivity.class);
        } else {
            openActivity(MainPageActivity.class);
        }
        finish();
    }


    class WelcomeRunnable implements Runnable {
        private int which;

        public WelcomeRunnable(int which) {
            this.which = which;
        }

        @Override
        public void run() {
            if (isStop) {
                return;
            }
            switch (which) {
                case BUTTON_WAIT:
                    btn_wel_cancle.setVisibility(View.VISIBLE);
                    break;
                case DEFAULT_WAIT:
                case WAIT:
                case NO_NET:
                case OUT_TIME:
                    nextPage();
                    break;
            }
        }
    }
}
