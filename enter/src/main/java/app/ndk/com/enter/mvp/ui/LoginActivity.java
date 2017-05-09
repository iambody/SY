package app.ndk.com.enter.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.ProtocolDialog;
import com.chenenyu.router.RouteTable;
import com.chenenyu.router.Router;
import com.cn.hugo.android.scanner.CaptureActivity;
import com.jhworks.library.ImageSelector;
import java.util.ArrayList;
import java.util.List;

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
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

//    @BindView(R2.id.iv_al_back)
//    ImageView iv_al_back;//返回按钮

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

    @BindView(R2.id.weixin_text)
    TextView weixin_text;//微信登录


    private LoadingDialog mLoadingDialog;
    private int identity;
    private boolean isUsernameInput, isPasswordInput;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2;
//    private UMShareAPI mUMShareAPI;
    private CustomDialog mCustomDialog;
    private CustomDialog.Builder mCustomBuilder;

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
        if (identity < 0) {
            identity = SPreference.getIdtentify(getApplicationContext());
        }
        switch (identity) {
            case IDS_ADVISER:
//                iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
                btn_al_login.setBackgroundResource(R.drawable.select_btn_advister);
                btn_al_login.setTextColor(0xff666666);
                break;
            case IDS_INVERSTOR:
//                iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
                btn_al_login.setBackgroundResource(R.drawable.select_btn_inverstor);
                btn_al_login.setTextColor(0xffffffff);
                break;
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
        String loginName = SPreference.getLoginName(getApplicationContext());
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
//        startActivityForResult(new Intent(LoginActivity.this, CaptureActivity.class), 0);
//testSelectPic();／／
//PromptManager.ShowCustomToast(LoginActivity.this,"ssaa坎坎坷坷扩扩");
//        Router.build("aks").go(LoginActivity.this);
        Router.build("investornmain_mainpageactivity").go(LoginActivity.this);
//testSelectPic();／／
        if (true) {
            return;
        }
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isPasswordInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.pw_null_str), Toast.LENGTH_SHORT);
            return;
        }
        toDataStatistics(1002, 10005, "登录");
//        Router.addRouteTable(new RouteTable() {
//            @Override
//            public void getAuthorResult(int type, Platform platform) {
//
//            }
//        });
//        authorUtils.WxAuth();



        if(true){return;}
        getPresenter().toNormalLogin(mLoadingDialog, et_al_username.getText().toString(), et_al_password.getText().toString(), false);

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

    @OnClick(R2.id.weixin_text)
    void weixinClick() {//微信登录
        toWxLogin();
        toDataStatistics(1002, 10008, "微信登录");
    }

    @Override
    public void loginSuccess() {
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


//    private void toWxLogin() {
//        mLoadingDialog.setLoading(getString(R.string.la_login_loading_str));
//        mLoadingDialog.show();
//
//        if (!Utils.isWeixinAvilible(this)) {
//            mLoadingDialog.setResult(false, getString(R.string.la_no_install_wx_str), 1000);
//            return;
//        }
//        if (mUMShareAPI.isAuthorize(this, SHARE_MEDIA.WEIXIN)) {
//            mUMShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new MUMAuthListener());
//        } else {
//            mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new MUMAuthListener());
//        }
//    }

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
        finish();
    }

    @Override
    public void finish() {
        if (identity == IDS_INVERSTOR) {
            toDataStatistics(2002, 20006, "返回");
        }
        super.finish();
    }
//
//    private class MUMAuthListener implements UMAuthListener {
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            String unionid = map.get("unionid");
//            String sex = map.get("sex");
//            String nickname = map.get("nickname");
//            String headimgurl = map.get("headimgurl");
//
//            if (!mCustomBuilder.isSetPositiveListener()) {
//                mCustomBuilder.setPositiveButton(getString(R.string.enter_str), (dialog, which) -> {
//                    getPresenter().toDialogWxLogin(mLoadingDialog, unionid, sex, nickname, headimgurl);
//                    dialog.dismiss();
//                });
//            }
//            getPresenter().toWxLogin(mLoadingDialog, mCustomBuilder, unionid, sex, nickname, headimgurl);
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            mLoadingDialog.setResult(false, getString(R.string.author_error_str), 1000);
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//            mLoadingDialog.setResult(false, getString(R.string.author_cancel_str), 1000);
//        }
//    }


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
