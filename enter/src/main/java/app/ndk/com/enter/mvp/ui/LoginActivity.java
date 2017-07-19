package app.ndk.com.enter.mvp.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.cgbsoft.lib.utils.cache.SPreference;
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
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.dialog.ProtocolDialog;
import com.cgbsoft.privatefund.bean.StrResult;
import com.cgbsoft.privatefund.bean.location.LocationBean;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.jhworks.library.ImageSelector;

import java.util.ArrayList;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.LoginContract;
import app.ndk.com.enter.mvp.presenter.LoginPresenter;
import app.privatefund.com.share.utils.WxAuthorManger;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongContext;


@Route(RouteConfig.GOTO_LOGIN)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    public static final String TAG_GOTOLOGIN = "insidegotologin";
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
    @BindView(R2.id.enter_login_wxlogin_lay)
    RelativeLayout enterLoginWxloginLay;
    @BindView(R2.id.btn_stroll)
    TextView btnStroll;
    //内容登录动作的布局
    @BindView(R2.id.login_cancle)
    ImageView loginCancle;

    //是否已经显示了微信登录的按钮  默认进来是不显示的
    private boolean isShowWxBt;
    //是否点击了游客模式的按钮
    boolean isVisitorLoginClick;

    private LoadingDialog mLoadingDialog;
    private int identity;
    private boolean isUsernameInput, isPasswordInput;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2;
    //    private UMShareAPI mUMShareAPI;
    private CustomDialog mCustomDialog;
    private CustomDialog.Builder mCustomBuilder;
    //公钥直接存内存中
    private String publicKey;

    private LocationManger locationManger;
    private ShakeListener mShakeListener;

    //是否是app内发起的 登录操作
    private boolean isFromInside;


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
        setIsNeedGoneNavigationBar(true);//不显示导航条

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initShakeListener();
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
            NavigationUtils.startActivityByRouter(LoginActivity.this, RouteConfig.SELECT_ADDRESS);
        }
    };

    @Override
    protected void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        initApplication = (InvestorAppli) getApplication();
        isFromInside = getIntent().getBooleanExtra(TAG_GOTOLOGIN, false);
        ShareSDK.initSDK(baseContext);
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(getApplicationContext());
        String loginName = AppManager.getUserAccount(this);
        if (userInfo != null && !AppManager.isVisitor(baseContext)) {
            et_al_username.setText(userInfo.userName);
        } else if (loginName != null && !AppManager.isVisitor(baseContext)) {
            et_al_username.setText(loginName);
        } else if (AppManager.isVisitor(baseContext)) {
            et_al_username.setText("");
        }
        if (!TextUtils.isEmpty(et_al_username.getText().toString())) {
            iv_al_del_un.setVisibility(View.VISIBLE);
            isUsernameInput = true;
        }

        if (isFromInside)
            initinSideComeId();
        et_al_username.addTextChangedListener(new LoginTextWatcher(USERNAME_KEY));
        et_al_password.addTextChangedListener(new LoginTextWatcher(PASSWORD_KEY));

        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.la_login_loading_str), false, false);

        mCustomDialog = new CustomDialog(this);
        mCustomBuilder = mCustomDialog.new Builder().setCanceledOnClickBack(true).setCanceledOnTouchOutside(true)
                .setTitle(getString(R.string.la_wxlogin_str)).setNegativeButton("", (dialog, which) -> dialog.dismiss());

        if (!SPreference.isVisableProtocol(getApplicationContext()))
            new ProtocolDialog(this, 0, null);
        //开始获取公钥publicKey
        getPresenter().toGetPublicKey();
        initLocation();
        //如果是正常进来的就偷偷加载游客信息
        if (!isFromInside)
            getPresenter().invisterLogin(baseContext);

    }

    private void initinSideComeId() {
        loginCancle.setVisibility(View.VISIBLE);
        btn_al_login.setBackground(getResources().getDrawable(R.drawable.select_btn_normal));
        btn_al_login.setTextColor(getResources().getColor(R.color.white));
        //开始展示
        enterLoginWxloginLay.setVisibility(View.GONE);
        enterLoginWxBtLay.setVisibility(View.VISIBLE);
        isShowWxBt = true;
        getPresenter().setAnimation(enterLoginWxBtLay);
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
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @OnClick(R2.id.iv_al_back)
    void backClick() {//返回到选择身份页面
//        openActivity(ChoiceIdentityActivity.class);
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
//        getPresenter().invisterLogin(baseContext);
//
//        dialg.show();
//        if (true) return;
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
                }
            }));
        }
        DataStatistApiParam.onStatisToCLoginClick();
    }

    private ArrayList<String> picLs = new ArrayList<>();
    private int REQUEST_CODE = 20;

    private void testSelectPic() {
        ImageSelector imageSelector = ImageSelector.create();
        imageSelector.single();
        imageSelector.origin(picLs);
        imageSelector.openCameraOnly(false);
        imageSelector.start(LoginActivity.this, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R2.id.tv_al_register)
    void registerClick() {//注册
//        toDataStatistics(1002, 10007, "注册用户");
//        toDataStatistics(1002, 10017, "选择注册");
        DataStatistApiParam.onStatisToCRegistClick();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.tv_al_forget)
    void forgetClick() {//忘记密码
//        toDataStatistics(1002, 10006, "忘记密码");
//        toDataStatistics(1002, 10018, "选择登录");
        DataStatistApiParam.onStatisToForgetPwdClick();
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    //点击微信上边布局 显示微信登录的按钮页面
    @OnClick(R2.id.enter_login_wxlogin_lay)
    public void onViewClickedlayout() {
        enterLoginWxloginLay.setVisibility(View.GONE);
        enterLoginWxBtLay.setVisibility(View.VISIBLE);
        isShowWxBt = true;
        getPresenter().setAnimation(enterLoginWxBtLay);
    }

    @Override
    public void loginSuccess() {
        if (AppManager.isVisitor(baseContext) && initApplication.isMainpage()) {
            AppInfStore.saveIsVisitor(baseContext, false);
//            RxBus.get().post(RxConstant.MAIN_FRESH_WEB_CONFIG, 1);
            RxBus.get().post(RxConstant.MAIN_FRESH_LAY, 1);

//            BaseWebview view=new BaseWebview(baseContext,false);
//            view.loadUrl(CwebNetConfig.pageInit);

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
        finish();
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

    }

    @OnClick(R2.id.enter_login_wx_bt_lay)
    public void onViewClicked() {
        mLoadingDialog.setLoading(getString(R.string.la_login_loading_str));
        mLoadingDialog.show();
        if (BStrUtils.isEmpty(publicKey)) {
            //开始获取公钥publicKey
            getPresenter().toGetPublicKey();
        }

        if (!Utils.isWeixinAvilible(this)) {
            mLoadingDialog.setResult(false, getString(R.string.la_no_install_wx_str), 1000);
            return;
        }
        WxAuthorManger wxAuthorManger = WxAuthorManger.getInstance(baseContext, new WxAuthorManger.AuthorUtilsResultListenr() {
            @Override
            public void getAuthorResult(int type, Platform platform) {
                mLoadingDialog.dismiss();
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
            }
        });
        wxAuthorManger.startAuth();
        DataStatistApiParam.onStatisToWXLoginClick();
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
//        openActivity(ChoiceIdentityActivity.class);
        if (isShowWxBt) {
            isShowWxBt = false;
            enterLoginWxloginLay.setVisibility(View.VISIBLE);
            enterLoginWxBtLay.setVisibility(View.GONE);
            return;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK && isShowWxBt) {
            isShowWxBt = false;
            enterLoginWxloginLay.setVisibility(View.VISIBLE);
            enterLoginWxBtLay.setVisibility(View.GONE);
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
        if (REQUEST_CODE == requestCode) {
            picLs = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
        }
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
}
