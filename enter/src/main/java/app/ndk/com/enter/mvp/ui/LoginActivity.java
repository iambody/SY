package app.ndk.com.enter.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.dialog.ProtocolDialog;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
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

/**
 * 登录
 * Created by xiaoyu.zhang on 2016/11/17 11:42
 * Email:zhangxyfs@126.com
 *  
 */
@Route("enter_loginactivity")
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

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

    //是否已经显示了微信登录的按钮  默认进来是不显示的
    private boolean isShowWxBt;

    private LoadingDialog mLoadingDialog;
    private int identity;
    private boolean isUsernameInput, isPasswordInput;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2;
    //    private UMShareAPI mUMShareAPI;
    private CustomDialog mCustomDialog;
    private CustomDialog.Builder mCustomBuilder;
    //公钥直接存内存中
    private String publicKey;

    @Override
    protected int layoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean getIsNightTheme() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        identity = getIntent().getIntExtra(IDS_KEY, -1);
        if (AppManager.isAdViser(this)) {
            //                iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
            btn_al_login.setBackgroundResource(R.drawable.select_btn_advister);
            btn_al_login.setTextColor(0xff666666);
        } else {
//           iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
            btn_al_login.setBackgroundResource(R.drawable.select_btn_inverstor);
            btn_al_login.setTextColor(0xffffffff);
        }

        if (savedInstanceState == null) {
            if (identity == IDS_ADVISER) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }

        ShareSDK.initSDK(baseContext);
        UserInfoDataEntity.UserInfo userInfo = SPreference.getUserInfoData(getApplicationContext());
        String loginName = AppManager.getUserAccount(this);
        if (userInfo != null) {
            et_al_username.setText(userInfo.userName);
        } else if (loginName != null) {
            et_al_username.setText(loginName);
        }
        if (!TextUtils.isEmpty(et_al_username.getText().toString())) {
            iv_al_del_un.setVisibility(View.VISIBLE);
            isUsernameInput = true;
        }

        et_al_username.addTextChangedListener(new LoginTextWatcher(USERNAME_KEY));
        et_al_password.addTextChangedListener(new LoginTextWatcher(PASSWORD_KEY));

        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.la_login_loading_str), false, false);
//        mUMShareAPI = UMShareAPI.get(this);

        mCustomDialog = new CustomDialog(this);
        mCustomBuilder = mCustomDialog.new Builder().setCanceledOnClickBack(true).setCanceledOnTouchOutside(true)
                .setTitle(getString(R.string.la_wxlogin_str)).setNegativeButton("", (dialog, which) -> dialog.dismiss());

        if (!SPreference.isVisableProtocol(getApplicationContext()))
            new ProtocolDialog(this, 0, null);
        //开始获取公钥publicKey
        getPresenter().toGetPublicKey();
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
//        toDataStatistics(1002, 10005, "登录");

//        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp8pDxAhjGPMxxU4VCWo1if4djzyqxM6dScGhb5q+/wqKfY/3SvPH7XKhtF1Q/0kRjCM3tFFpdGJyqXl0Bl34o3RlGoCeCGbUxVRj2IXvsryaOeF1yi8vW7DtOu2VPePS+Hv69SUCDaJYRdSz1+bhaa1ltFoYIvyTjhr+V8umjNQIDAQAB";
//        String privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKnykPECGMY8zHFThUJajWJ/h2PPKrEzp1JwaFvmr7/Cop9j/dK88ftcqG0XVD/SRGMIze0UWl0YnKpeXQGXfijdGUagJ4IZtTFVGPYhe+yvJo54XXKLy9bsO067ZU949L4e/r1JQINolhF1LPX5uFprWW0Whgi/JOOGv5Xy6aM1AgMBAAECgYBg9z7N1GVwTmZTztS83E/JQHxubVitjIxOlEZnEUN7xUDmcrXzVM04n1CWFfaDB6TvYKmmOLOqZI2XA4pLizV2iV3YaJUzv7M0kioZ/0+QG5NwGhaF4wCNOPK9MSmNcSX5qLm0PbOixDc/+E/YY8N9XwfmWgi/CxQs58vBK7Fo7QJBAO0ZafOd841e8YXT9pxZ6jIUAocgKWds9U7SJbxCN4VHHX+cvAUsLS/L9hWfZx0ejBcWrIFuW7hka2mrJfYnP28CQQC3fsIEzt2RVb9klmC4NyJdEA6M8fNF/wTuVwmLvUCPCUWhvEW34//Z5qlBxfPHA/uXuKsq/UiC+0O0xD+FH/WbAkEAlXCuOjG1H8bW3i4CQvvdQ+Ee0sJvtlOTrjGAPU9TJTr0mclVLMFyXazlly1YVZ86VxcgdZf0UZ1hokGQdLy6GwJAQ6OoJVmT9yTinlOIZ597PU7T7kSp5l1xFeJjlG04xQEn98yM7pJPF6WdMq+jgvMG5RCfmAMxnYa9mH7W4126jQJAOoXPUTZoGP1LYtIEUrNLIM5GsngmktrgjVj2HbzYhdvAVk8Zcbu0BoexC8Gg7Ka17sWWuCKqxGoKqLaV56X0Jw==";
//        String sss = null;
//        try {
//            sss = RSAUtils.encryptByPublicKey("{\"userName\":\"18900000001\",\"password\":\"9def65456fc2a68a\",\"client\":\"C\"}", publickey);
//            String dddd = new String(RSAUtils.decryptByPrivateKey(sss, privatekey));
//            LogUtils.Log("s", sss);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        LogUtils.Log("s", sss);

        getPresenter().toNormalLogin(mLoadingDialog, et_al_username.getText().toString(), et_al_password.getText().toString(), publicKey, false);

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
//        Toast.makeText(LoginActivity.this,"返回来了",Toast.LENGTH_LONG).show();
    }

    @OnClick(R2.id.tv_al_register)
    void registerClick() {//注册
        toDataStatistics(1002, 10007, "注册用户");
        toDataStatistics(1002, 10017, "选择注册");

        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.tv_al_forget)
    void forgetClick() {//忘记密码
        toDataStatistics(1002, 10006, "忘记密码");
        toDataStatistics(1002, 10018, "选择登录");

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
        if (GestureManager.intercepterGestureActivity(this, AppManager.getUserInfo(this), true)) {
            finish();
            return;
        }
        Router.build(RouteConfig.GOTOCMAINHONE).go(LoginActivity.this);
        finish();
    }

    @Override
    public void loginFail() {
        //todo 测试用
//        openActivity(MainPageActivity.class);
        finish();
    }

    @Override
    public void toBindActivity() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    @Override
    public void publicKeySuccess(String str) {
        publicKey = str;
    }

    @OnClick(R2.id.enter_login_wx_bt_lay)
    public void onViewClicked() {
//        mLoadingDialog.setLoading(getString(R.string.la_login_loading_str));
//        mLoadingDialog.show();

        if (!Utils.isWeixinAvilible(this)) {
            mLoadingDialog.setResult(false, getString(R.string.la_no_install_wx_str), 1000);
            return;
        }
        WxAuthorManger wxAuthorManger = WxAuthorManger.getInstance(baseContext, new WxAuthorManger.AuthorUtilsResultListenr() {
            @Override
            public void getAuthorResult(int type, Platform platform) {

                switch (type) {
                    case WxAuthorManger.WxAuthorOk:
                        String unionid = platform.getDb().get("unionid");
                        String userIcon = platform.getDb().getUserIcon();
                        String userGender = platform.getDb().getUserGender();
                        String userName = platform.getDb().getUserName();
                        String openid = platform.getDb().getUserId();
//                        LogUtils.Log("weixindenglu", "用户id" + userId + "；；；用户图标" + userIcon + ";用户性别" + userGender + ";用户名字" + userName);

                        String SexStr = BStrUtils.isEmpty(userGender) ? "2" : userGender.equals("m") ? "0" : "1";

                        if (!mCustomBuilder.isSetPositiveListener()) {
                            mCustomBuilder.setPositiveButton(getString(R.string.enter_str), (dialog, which) -> {
                                getPresenter().toDialogWxLogin(mLoadingDialog, unionid, SexStr, userName, userIcon,openid,publicKey);
                                dialog.dismiss();
                            });
                        }
//
//                        getPresenter().toDialogWxLogin(mLoadingDialog, userId, SexStr, userName, userIcon);

                        getPresenter().toWxLogin(mLoadingDialog, mCustomBuilder, unionid, SexStr, userName, userIcon,openid,publicKey);
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
}
