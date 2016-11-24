package com.cgbsoft.privatefund.mvp.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.iOSDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.login.ForgetPasswordPresenter;
import com.cgbsoft.privatefund.mvp.view.login.ForgetPasswordView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 忘记密码
 * Created by xiaoyu.zhang on 2016/11/18 14:50
 * Email:zhangxyfs@126.com
 *  
 */
public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordView {
    private final int COOL_DOWN_TIME = 60;

    @BindView(R.id.iv_af_back)
    ImageView iv_af_back;

    @BindView(R.id.et_af_username)
    EditText et_af_username;

    @BindView(R.id.iv_af_del_un)
    ImageView iv_af_del_un;

    @BindView(R.id.et_af_check)
    EditText et_af_check;

    @BindView(R.id.btn_af_check)
    Button btn_af_check;

    @BindView(R.id.btn_af_next)
    Button btn_af_next;

    private LoadingDialog mLoadingDialog;
    private boolean isUsernameInput, isCheckInput;
    private final int USERNAME_KEY = 1, CHECK_KEY = 3;
    private int identity;
    private iOSDialog miOSDialog;
    private int countDownTime = COOL_DOWN_TIME;
    private Subscription countDownSub;
    private String UMENG_KEY = "logReg_click";

    private String lastInputPhoneNum;//刚才输入的手机号

    @Override
    public void onBackPressed() {
        openActivity(LoginActivity.class);
        finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    protected void init() {
        identity = getIntent().getIntExtra(IDS_KEY, -1);
        if (identity < 0) {
            identity = SPreference.getIdtentify(getApplicationContext());
        }
        switch (identity) {
            case IDS_ADVISER:
                iv_af_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
                btn_af_next.setBackgroundResource(R.drawable.select_btn_advister);
                btn_af_next.setTextColor(0xff666666);
                break;
            case IDS_INVERSTOR:
                iv_af_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
                btn_af_next.setBackgroundResource(R.drawable.select_btn_inverstor);
                btn_af_next.setTextColor(0xffffffff);
                break;
        }
        et_af_username.addTextChangedListener(new ForgetTextWatcher(USERNAME_KEY));
        et_af_check.addTextChangedListener(new ForgetTextWatcher(CHECK_KEY));
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.sending_str), false, false);
        miOSDialog = new iOSDialog(this, "", getString(R.string.ra_send_code_str, VOICE_PHONE), getString(R.string.btn_cancel_str), getString(R.string.ra_enter_code_str)) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right() {
                this.dismiss();
                toDataStatistics(1002, 10014, "忘记验证码");
                getPresenter().sendCode(mLoadingDialog, et_af_username.getText().toString());
            }
        };
    }

    @OnClick(R.id.iv_af_back)
    void backClick() {
        openActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R.id.iv_af_del_un)
    void delUsernameClick() {
        if (et_af_username.getText().toString().length() > 0) {
            et_af_username.setText("");
        }
        iv_af_del_un.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_af_check)
    void checkClick() {
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        miOSDialog.show();
    }

    @OnClick(R.id.btn_af_next)
    void nextClick() {
        String userName = et_af_username.getText().toString();
        String code = et_af_check.getText().toString();
        if (!TextUtils.equals(userName, lastInputPhoneNum)) {
            MToast.makeText(this, "手机号码与验证码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SPreference.getIdtentify(getApplicationContext()) == IDS_ADVISER) {
            toUmengStatistics(UMENG_KEY, "按钮", "找回下一步");
        } else {
            toDataStatistics(2005, 20011, "下一步");
        }
        getPresenter().checkCode(mLoadingDialog, userName, code);
    }

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return new ForgetPasswordPresenter(this, this);
    }

    @Override
    public void sendSucc() {
        btn_af_check.setEnabled(false);
        btn_af_check.setBackgroundResource(R.drawable.bg_write_down);
        btn_af_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
        lastInputPhoneNum = et_af_username.getText().toString();
        countDownSub = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<Long>() {
                    @Override
                    protected void onEvent(Long aLong) {
                        if (countDownTime < 0) {
                            reSetVerificationState();
                        } else {
                            btn_af_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {

                    }
                });
    }

    @Override
    public void checkSucc() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra("userName", et_af_username.getText().toString());
        intent.putExtra("code", et_af_check.getText().toString());
        startActivity(intent);
        finish();
    }

    private void reSetVerificationState() {
        btn_af_check.setEnabled(true);
        countDownTime = COOL_DOWN_TIME;
        countDownSub.unsubscribe();
        btn_af_check.setBackgroundResource(R.drawable.shape_red_line);
        btn_af_check.setTextColor(getResources().getColor(R.color.white));
        btn_af_check.setText(R.string.ra_code_resend_str);
    }


    private class ForgetTextWatcher implements TextWatcher {
        private int which;

        ForgetTextWatcher(int which) {
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
                    iv_af_del_un.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
                case CHECK_KEY:
                    isCheckInput = isTextHasLength;
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}