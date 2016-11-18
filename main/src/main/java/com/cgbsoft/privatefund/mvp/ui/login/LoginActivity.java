package com.cgbsoft.privatefund.mvp.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.login.LoginPresenter;
import com.cgbsoft.privatefund.mvp.view.login.LoginView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登陆
 * Created by xiaoyu.zhang on 2016/11/17 11:42
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.iv_al_back)
    ImageView iv_al_back;//返回按钮

    @BindView(R.id.et_al_username)
    EditText et_al_username;//用户名

    @BindView(R.id.et_al_password)
    EditText et_al_password;//密码

    @BindView(R.id.iv_al_del_un)
    ImageView iv_al_del_un;//用户名删除

    @BindView(R.id.iv_al_del_pw)
    ImageView iv_al_del_pw;//密码清除

    @BindView(R.id.btn_al_login)
    Button btn_al_login;//登陆按钮

    @BindView(R.id.tv_al_register)
    TextView tv_al_register;//注册按钮

    @BindView(R.id.tv_al_forget)
    TextView tv_al_forget;//忘记密码

    @BindView(R.id.iv_al_wx)
    ImageView iv_al_wx;//微信登陆


    private LoadingDialog mLoadingDialog;
    private int identity;
    private boolean isUsernameInput, isPasswordInput;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2;

    @Override
    protected void before() {
        super.before();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected int layoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        identity = getIntent().getIntExtra(IDS_KEY, -1);
        if (identity < 0) {
            identity = SPreference.getIdtentify(getApplicationContext());
        }
        switch (identity) {
            case IDS_ADVISER:
                iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
                break;
            case IDS_INVERSTOR:
                iv_al_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
                break;
        }

        UserInfo userInfo = SPreference.getUserInfoData(getApplicationContext());
        if (userInfo != null) {
            et_al_username.setText(userInfo.getUserName());
        }

        et_al_username.addTextChangedListener(new LoginTextWatcher(USERNAME_KEY));
        et_al_password.addTextChangedListener(new LoginTextWatcher(PASSWORD_KEY));

        mLoadingDialog = LoadingDialog.getLoadingDialog(this.getApplicationContext(), getString(R.string.la_login_loading_str), false, false);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this, this);
    }

    @OnClick(R.id.iv_al_back)
    void backClick() {//返回到选择身份页面
        openActivity(ChoiceIdentityActivity.class);
        finish();
    }

    @OnClick(R.id.iv_al_del_un)
    void delUsernameClick() {//删除用户名
        if (et_al_username.getText().toString().length() > 0) {
            et_al_username.setText("");
        }
        iv_al_del_un.setVisibility(View.GONE);
        toDataStatistics(1002, 10003, "登录用户名");
    }

    @OnClick(R.id.iv_al_del_pw)
    void delPasswordClick() {//删除密码
        if (et_al_password.getText().toString().length() > 0) {
            et_al_username.setText("");
        }
        iv_al_del_pw.setVisibility(View.GONE);
        toDataStatistics(1002, 10004, "登录密码");
    }

    @OnClick(R.id.btn_al_login)
    void loginClick() {//登陆
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.la_un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isPasswordInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.la_pw_null_str), Toast.LENGTH_SHORT);
            return;
        }
        toDataStatistics(1002, 10005, "登录");
        getPresenter().toLogin(mLoadingDialog, et_al_username.getText().toString(), et_al_password.getText().toString(), false);

    }

    @OnClick(R.id.tv_al_register)
    void registerClick() {//注册
        toDataStatistics(1002, 10007, "注册用户");
        toDataStatistics(1002, 10017, "选择注册");

        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_al_forget)
    void forgetClick() {//忘记密码
        toDataStatistics(1002, 10006, "忘记密码");
        toDataStatistics(1002, 10018, "选择登录");

        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        intent.putExtra(IDS_KEY, identity);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.iv_al_wx)
    void weixinClick() {//微信登陆

        toDataStatistics(1002, 10008, "微信登录");
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
}
