package app.ndk.com.enter.mvp.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.BdLocationListener;
import com.cgbsoft.lib.share.utils.WxAuthorManger;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.shake.ShakeListener;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LocationManger;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ZipUtils;
import com.cgbsoft.lib.widget.WeiChatLoginDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.bean.StrResult;
import com.cgbsoft.privatefund.bean.location.LocationBean;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.LoginContract;
import app.ndk.com.enter.mvp.presenter.LoginPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import rx.Observable;
import rx.schedulers.Schedulers;


@Route(RouteConfig.GOTO_LOGIN)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    public static final String TAG_GOTOLOGIN = "insidegotologin";
    public static final String TAG_GOTOLOGIN_FROMCENTER = "insidegotologincenter";
    public static final String TAG_BACK_HOME = "backgohome";
    @BindView(R2.id.et_al_username)
    EditText et_al_username;//用户名

    @BindView(R2.id.et_al_password)
    EditText et_al_password;//密码

    @BindView(R2.id.iv_al_del_un)
    ImageView iv_al_del_un;//用户名删除

    @BindView(R2.id.iv_al_del_pw)
    ImageView iv_al_del_pw;//密码清除

    @BindView(R2.id.btn_al_login)
    Button btn_al_login;//登录按钮

    @BindView(R2.id.tv_al_register)
    TextView tv_al_register;//注册按钮

    @BindView(R2.id.tv_al_forget)
    TextView tv_al_forget;//忘记密码

    @BindView(R2.id.enter_login_wx_bt_lay)
    RelativeLayout enterLoginWxBtLay;

    @BindView(R2.id.btn_stroll)
    TextView btnStroll;
    //内容登录动作的布局
    @BindView(R2.id.login_cancle)
    ImageView loginCancle;

    @BindView(R2.id.login_weixins_text)
    TextView loginWeixinsText;
    @BindView(R2.id.home_name_input)
    TextInputLayout homeNameInput;

    //是否已经显示了微信登录的按钮  默认进来是不显示的
    private boolean isShowWxBt;
    //是否点击了游客模式的按钮
    boolean isVisitorLoginClick, isVisitorBackHome;
    boolean fromValidatePassword;
    private LoadingDialog mLoadingDialog;
    private int identity;
    private boolean isUsernameInput, isPasswordInput;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2;
    private WeiChatLoginDialog mCustomDialog;
    private WeiChatLoginDialog.Builder mCustomBuilder;
    //公钥直接存内存中
    private String publicKey;
    private LocationManger locationManger;
    private ShakeListener mShakeListener;

    //是否是app内发起的 登录操作
    private boolean isFromInside;
    //是否从app内的我的进来的
    private boolean isFromInsidemy;
    //是否退出登录进来
    private boolean ialoginout;

    private boolean showSelectAddress;

    public static final int SELECT_ADDRESS = 3;

    private InvestorAppli initApplication;

    @Override
    protected int layoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean getIsNightTheme() {
        return true;
    }

    @Override
    protected void before() {
        super.before();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        initApplication = (InvestorAppli) getApplication();
        isFromInside = getIntent().getBooleanExtra(TAG_GOTOLOGIN, false);
        isFromInsidemy = getIntent().getBooleanExtra(TAG_GOTOLOGIN_FROMCENTER, false);
        isVisitorBackHome = getIntent().getBooleanExtra(TAG_BACK_HOME, false);
        ialoginout = getIntent().getBooleanExtra("ialoginout", false);
        fromValidatePassword = getIntent().getBooleanExtra("fromValidatePassword", false);

        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(getApplicationContext());
        String loginName = AppManager.getUserAccount(this);
        getPresenter().getNavigation();
        if (!TextUtils.isEmpty(et_al_username.getText().toString())) {
            iv_al_del_un.setVisibility(View.VISIBLE);
            isUsernameInput = true;
        }

        if (isFromInside) {
            initinSideComeIn();
        } else {
            initoutSideComeIn();
        }
        et_al_username.addTextChangedListener(new LoginTextWatcher(USERNAME_KEY));
        et_al_password.addTextChangedListener(new LoginTextWatcher(PASSWORD_KEY));

        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.la_login_loading_str), false, false);

        mCustomDialog = new WeiChatLoginDialog(this);
        mCustomBuilder = mCustomDialog.new Builder().setCanceledOnClickBack(true).setCanceledOnTouchOutside(true)
                .setTitle(getString(R.string.la_wxlogin_str)).setNegativeButton("", (dialog, which) -> dialog.dismiss());
        getPresenter().toGetPublicKey();
        initLocation();
        //如果是正常进来的就偷偷加载游客信息
        if (isFromInside) {
            btnStroll.setVisibility(View.INVISIBLE);
        } else {
            getPresenter().invisterLogin(baseContext);
        }
        initRxObservable();
        initShakeListener();

        et_al_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                homeNameInput.setHintEnabled(true);
                return false;
            }
        });


        //解压一些资源
        Observable.just(R.raw.ress).subscribeOn(Schedulers.io()).subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                String path = CacheManager.getCachePath(getApplicationContext(), CacheManager.RES);
                InputStream is = getResources().openRawResource(integer);
                try {
                    ZipUtils.unZip(is, path, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        if (!BStrUtils.isEmpty(AppManager.getLatestPhoneNum(baseContext))) {
            BStrUtils.setTv1(et_al_username,AppManager.getLatestPhoneNum(baseContext));


        }



}

    private Observable<Integer> killSelfRxObservable;

    private void initRxObservable() {
        killSelfRxObservable = RxBus.get().register(RxConstant.LOGIN_KILL, Integer.class);
        killSelfRxObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                baseContext.finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    private void initoutSideComeIn() {
        loginWeixinsText.setText(getResources().getString(R.string.al_wx_login_strss));
    }

    private void initinSideComeIn() {
        loginWeixinsText.setText(getResources().getString(R.string.al_wx_login_strs));
        loginCancle.setVisibility(View.VISIBLE);
    }

    private void initLocation() {
        locationManger = LocationManger.getInstanceLocationManger(baseContext);
        locationManger.startLocation(new BdLocationListener() {
            @Override
            public void getLocation(LocationBean locationBean) {
            }

            @Override
            public void getLocationerror() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManger.unregistLocation();
        if (null != killSelfRxObservable) {
            RxBus.get().unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, killSelfRxObservable);
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @OnClick(R2.id.iv_al_back)
    void backClick() {//返回到选择身份页面
        finish();
    }

    @OnClick(R2.id.iv_al_del_un)
    void delUsernameClick() {//删除用户名
        if (et_al_username.getText().toString().length() > 0) {
            et_al_username.setText("");
        }
        iv_al_del_un.setVisibility(View.GONE);
        toDataStatistics(1002, 10003, "登录用户名");
    }

    @OnClick(R2.id.iv_al_del_pw)
    void delPasswordClick() {//删除密码
        if (et_al_password.getText().toString().length() > 0) {
            et_al_password.setText("");
        }
        iv_al_del_pw.setVisibility(View.GONE);
        toDataStatistics(1002, 10004, "登录密码");
    }

    @OnClick(R2.id.btn_al_login)
    void loginClick() {//登录
        if (!isFixAdjustEd()) return;
        if (!NetUtils.isNetworkAvailable(baseContext)) return;
        String password = et_al_password.getText().toString().trim();
        //需要判断
        LocationBean bean = AppManager.getLocation(baseContext);
        if (!BStrUtils.isEmpty(publicKey))
            getPresenter().toNormalLogin(mLoadingDialog, et_al_username.getText().toString(), et_al_password.getText().toString(), publicKey, false);
        else {
            getPresenter().addSubscription(ApiClient.getLoginPublic().subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    StrResult result = new Gson().fromJson(s, StrResult.class);
                    publicKey = result.result;
                    AppInfStore.savePublicKey(baseContext.getApplicationContext(), publicKey);
                    getPresenter().toNormalLogin(mLoadingDialog, et_al_username.getText().toString(), et_al_password.getText().toString(), publicKey, false);
                }

                @Override
                protected void onRxError(Throwable error) {
                    LogUtils.Log("s", error.toString());
                }
            }));
        }
        DataStatistApiParam.onStatisToCLoginClick();
    }

    private ArrayList<String> picLs = new ArrayList<>();
    private int REQUEST_CODE = 20;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_DL);
        AppInfStore.saveDialogTag(LoginActivity.this, false);
    }

    @OnClick(R2.id.tv_al_register)
    void registerClick() {//注册
        DataStatistApiParam.onStatisToCRegistClick();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
//        finish();
    }

    @OnClick(R2.id.tv_al_forget)
    void forgetClick() {//忘记密码
        DataStatistApiParam.onStatisToForgetPwdClick();
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
    }

    @OnClick(R2.id.login_weixins_text)
    void weixinxins() {
        if (!NetUtils.isNetworkAvailable(baseContext)) return;
        isShowWxBt = true;
        enterLoginWxBtLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginSuccess() {
        if (AppManager.isVisitor(baseContext) && initApplication.isMainpage()) {
            AppInfStore.saveIsVisitor(baseContext, false);
            RxBus.get().post(RxConstant.MAIN_FRESH_LAY, isFromInsidemy ? 5 : 1);//刷新用户数据
            if (isVisitorBackHome) Router.build(RouteConfig.GOTOCMAINHONE).go(LoginActivity.this);
        } else {
            Router.build(RouteConfig.GOTOCMAINHONE).go(LoginActivity.this);
        }
        AppInfStore.saveIsVisitor(baseContext, false);

        finish();
    }

    @Override
    public void loginFail() {
        //todo 测试用
//        openActivity(MainPageActivity.class);
//        finish();


    }

    @Override
    public void toBindActivity() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
//        finish();

    }

    //游客登录成功
    @Override
    public void invisterloginSuccess() {
        if (isVisitorLoginClick) {//是点击游客模式登录按钮进入的
            visitorLogin();
        }
    }

    @Override
    public void invisterloginFail() {
        //游客登录失败
        LogUtils.Log("sss", "ss");
    }

    @Override
    public void publicKeySuccess(String str) {
        publicKey = str;
        AppInfStore.savePublicKey(baseContext.getApplicationContext(), str);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_DL);
    }

    @OnClick(R2.id.enter_login_wx_bt_lay)
    public void onViewClicked() {
//        mLoadingDialog.setLoading(getString(R.string.la_login_loading_str));
//        mLoadingDialog.show();
        if (BStrUtils.isEmpty(publicKey)) {
            //开始获取公钥publicKey
            getPresenter().toGetPublicKey();
        }

        if (!Utils.isWeixinAvilible(this)) {
            mLoadingDialog.setResult(false, getString(R.string.la_no_install_wx_str), 1000);
            return;
        }
        WxAuthorManger wxAuthorManger = WxAuthorManger.getInstance(baseContext, (type, platform) -> {
//            mLoadingDialog.dismiss();
            switch (type) {
                case WxAuthorManger.WxAuthorOk:
                    String unionid = platform.getDb().get("unionid");
                    String userIcon = platform.getDb().getUserIcon();
                    String userGender = platform.getDb().getUserGender();
                    String userName = platform.getDb().getUserName();
                    String openid = platform.getDb().getUserId();
                    String SexStr = BStrUtils.isEmpty(userGender) ? "2" : userGender.equals("m") ? "0" : "1";

                    if (!mCustomBuilder.isSetPositiveListener()) {
                        mCustomBuilder.setPositiveButton(getString(R.string.enter_str), (dialog, which) -> {
                            getPresenter().toDialogWxLogin(mLoadingDialog, unionid, SexStr, userName, userIcon, openid, publicKey);
                            dialog.dismiss();
                        });
                    }
                    getPresenter().toWxLogin(mLoadingDialog, mCustomBuilder, unionid, SexStr, userName, userIcon, openid, publicKey);
                    break;
                case WxAuthorManger.WxAuthorCANCLE:
                    mLoadingDialog.setResult(false, getString(R.string.author_error_str), 1000);
                    break;
                case WxAuthorManger.WxAuthorERROR:
                    mLoadingDialog.setResult(false, getString(R.string.author_error_str), 1000);
                    break;
            }
        });
        wxAuthorManger.startAuth();
        DataStatistApiParam.onStatisToWXLoginClick();
//        MobclickAgent.
    }


    @OnClick(R2.id.btn_stroll)
    public void onStrollClicked() {//游客模式点击登录
        isVisitorLoginClick = true;
        if (getPresenter().isvistorLogin()) {//如果已经登录成功就直接进行进入
            visitorLogin();
        } else {//如果没有就直接进行登录后才能进入主界面
            getPresenter().invisterLogin(baseContext);
        }
    }


    @OnClick(R2.id.login_cancle)
    public void onViewlogincancleClicked() {//登录取消
        LoginActivity.this.finish();
    }

    private class LoginTextWatcher implements TextWatcher {
        private int which;

        LoginTextWatcher(int which) {
            this.which = which;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean isTextHasLength = s.length() > 0;
            btn_al_login.setBackground(getResources().getDrawable(isFixAdjust() ? R.drawable.select_btn_normal : R.drawable.select_btn_apphnormal));
            btn_al_login.setTextColor(getResources().getColor(isFixAdjust() ? R.color.white : R.color.black));

            switch (which) {
                case USERNAME_KEY:
                    isUsernameInput = isTextHasLength;
                    iv_al_del_un.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
                case PASSWORD_KEY:
                    isPasswordInput = isTextHasLength;
                    iv_al_del_pw.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("-------onBackPressed-----fromValidatePassword=" + fromValidatePassword);
        if (fromValidatePassword) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }
        if (isShowWxBt) {
            isShowWxBt = false;
            enterLoginWxBtLay.setVisibility(View.GONE);
            return;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isShowWxBt) {
            isShowWxBt = false;
            enterLoginWxBtLay.setVisibility(View.GONE);
            return true;
        }

        System.out.println("-------onKeyDown-----fromValidatePassword=" + fromValidatePassword);

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && fromValidatePassword) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        if (identity == IDS_INVERSTOR) {
            toDataStatistics(2002, 20006, "返回");
        }
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_ADDRESS) {
            showSelectAddress = false;
            return;
        }

//        if (REQUEST_CODE == requestCode) {
//            picLs = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
//        }
        if (resultCode == RESULT_OK) {
            String result = data.getExtras().getString("result");
            PromptManager.ShowCustomToast(LoginActivity.this, result);
            LogUtils.Log("daa", result);
        }
    }

    /**
     * 游客模式进行登录
     */
    public void visitorLogin() {
        AppInfStore.saveIsVisitor(baseContext, true);
        AppInfStore.saveIsLogin(baseContext.getApplicationContext(), true);
        //xxxxxxxxxxx
        Router.build(RouteConfig.GOTOCMAINHONE).go(LoginActivity.this);
        finish();
    }

    /*是否符合条件*/
    boolean isFixAdjust() {
        String userName = et_al_username.getText().toString().trim();
        String userPwd = et_al_password.getText().toString().trim();
        if (BStrUtils.isEmpty(userName) || 11 != userName.length() || BStrUtils.isEmpty(userPwd)) {
            return false;
        }

        return true;
    }

    /*是否符合条件*/
    boolean isFixAdjustEd() {
        String userName = et_al_username.getText().toString().trim();
        String userPwd = et_al_password.getText().toString().trim();
        if (BStrUtils.isEmpty(userName) || 11 != userName.length()) {
            PromptManager.ShowCustomToast(baseContext, "请输入正确手机号");
            return false;
        }
        if (BStrUtils.isEmpty(userPwd) || userPwd.length() < 6 || userPwd.length() > 16) {
            PromptManager.ShowCustomToast(baseContext, "密码长度为6-16位");
            return false;
        }
        return true;
    }

    private void initShakeListener() {
        try {
            ApplicationInfo appInfo = RongContext.getInstance().getPackageManager().getApplicationInfo(RongContext.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("RONG_CLOUD_APP_KEY");
            if ("tdrvipksrbgn5".equals(msg) || Utils.isApkInDebug(this)) {
                mShakeListener = new ShakeListener(this);
                mShakeListener.setOnShakeListener(onShakeListener);
                mShakeListener.register();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ShakeListener.OnShakeListener onShakeListener = new ShakeListener.OnShakeListener() {
        @Override
        public void onShakeStart() {
        }

        @Override
        public void onShakeFinish() {
            if (showSelectAddress) return;
            if (!"SelectAddressActivity".equals(getAppli().getBackgroundManager().getCurrentActivity().getClass().getSimpleName())) {
                NavigationUtils.startActivityByRouterForResult(LoginActivity.this, RouteConfig.SELECT_ADDRESS, SELECT_ADDRESS);
                showSelectAddress = true;
            }
        }
    };
}
