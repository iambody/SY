package com.cgbsoft.privatefund.mvp.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.IOSDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.login.BindPhoneContract;
import com.cgbsoft.privatefund.mvp.presenter.login.BindPhonePresenter;
import com.cgbsoft.privatefund.mvp.ui.home.MainPageActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 绑定手机号
 * Created by xiaoyu.zhang on 2016/11/29 14:21
 * Email:zhangxyfs@126.com
 *  
 */
public class BindPhoneActivity extends BaseActivity<BindPhonePresenter> implements BindPhoneContract.View {
    private final int COOL_DOWN_TIME = 60;
    @BindView(R.id.iv_ab_back)
    ImageView iv_ab_back;//返回

    @BindView(R.id.tv_ab_next)
    TextView tv_ab_next;//跳过

    @BindView(R.id.et_ab_username)
    EditText et_ab_username;//用户名

    @BindView(R.id.iv_ab_del_un)
    ImageView iv_ab_del_un;//删除用户名

    @BindView(R.id.et_ab_check)
    EditText et_ab_check;//code

    @BindView(R.id.btn_ab_check)
    Button btn_ab_check;//code按钮

    @BindView(R.id.btn_ab_ok)
    Button btn_ab_ok;//完成

    private LoadingDialog mLoadingDialog;//等待弹窗
    private boolean isUsernameInput, isCheckInput;
    private final int USERNAME_KEY = 1, CHECK_KEY = 3;
    private int identify = -1;
    private IOSDialog miOSDialog;
    private int countDownTime = COOL_DOWN_TIME;
    private Subscription countDownSub;
    private String UMENG_KEY = "logReg_click";

    private String lastInputPhoneNum;//刚才输入的手机号

    @Override
    protected int layoutID() {
        return R.layout.activity_bindphone;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        identify = getIntent().getIntExtra(IDS_KEY, -1);
        if (identify < 0) {
            identify = SPreference.getIdtentify(getApplicationContext());
        }
        switch (identify) {
            case IDS_ADVISER:
                iv_ab_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
                btn_ab_ok.setBackgroundResource(R.drawable.select_btn_advister);
                btn_ab_ok.setTextColor(0xff666666);
                tv_ab_next.setBackgroundResource(R.drawable.select_btn_advister);
                tv_ab_next.setTextColor(0xff666666);
                break;
            case IDS_INVERSTOR:
                iv_ab_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
                btn_ab_ok.setBackgroundResource(R.drawable.select_btn_inverstor);
                btn_ab_ok.setTextColor(0xffffffff);
                tv_ab_next.setBackgroundResource(R.drawable.select_btn_inverstor);
                tv_ab_next.setTextColor(0xffffffff);
                break;
        }
        et_ab_username.addTextChangedListener(new BindTextWatcher(USERNAME_KEY));
        et_ab_check.addTextChangedListener(new BindTextWatcher(CHECK_KEY));
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.sending_str), false, false);
        miOSDialog = new IOSDialog(this, "", getString(R.string.ra_send_code_str, VOICE_PHONE), getString(R.string.btn_cancel_str), getString(R.string.ra_enter_code_str)) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right() {
                this.dismiss();
                getPresenter().sendCode(mLoadingDialog, et_ab_username.getText().toString());
            }
        };
    }

    /**
     * 返回按钮点击
     */
    @OnClick(R.id.iv_ab_back)
    void backClick() {
        openActivity(LoginActivity.class);
        finish();
    }

    /**
     * 删除用户名按钮点击
     */
    @OnClick(R.id.iv_ab_del_un)
    void delUsernameClick() {
        if (et_ab_username.getText().toString().length() > 0) {
            et_ab_username.setText("");
        }
        iv_ab_del_un.setVisibility(View.GONE);
    }

    /**
     * 验证码按钮点击
     */
    @OnClick(R.id.btn_ab_check)
    void checkClick() {
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        miOSDialog.show();
    }

    /**
     * 完成按钮点击
     */
    @OnClick(R.id.btn_ab_ok)
    void okClick() {
        String userName = et_ab_username.getText().toString();
        String code = et_ab_check.getText().toString();
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isCheckInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.code_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!TextUtils.equals(userName, lastInputPhoneNum)) {
            MToast.makeText(this, "手机号码与验证码不匹配", Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().wxMergePhone(mLoadingDialog, userName, code);
    }

    /**
     * 验证码发送成功，开始倒计时
     */
    @Override
    public void sendSucc() {
        btn_ab_check.setEnabled(false);
        btn_ab_check.setBackgroundResource(R.drawable.bg_write_down);
        btn_ab_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
        lastInputPhoneNum = et_ab_username.getText().toString();
        countDownSub = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<Long>() {
                    @Override
                    protected void onEvent(Long aLong) {
                        if (countDownTime < 0) {
                            reSetVerificationState();
                        } else {
                            btn_ab_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {

                    }
                });
    }

    @Override
    public void margeSucc() {
        openActivity(MainPageActivity.class);
        finish();
    }

    /**
     * 验证码按钮可以重新点击
     */
    private void reSetVerificationState() {
        btn_ab_check.setEnabled(true);
        countDownTime = COOL_DOWN_TIME;
        countDownSub.unsubscribe();
        btn_ab_check.setBackgroundResource(R.drawable.shape_red_line);
        btn_ab_check.setTextColor(getResources().getColor(R.color.white));
        btn_ab_check.setText(R.string.ra_code_resend_str);
    }

    @Override
    protected BindPhonePresenter createPresenter() {
        return new BindPhonePresenter(this, this);
    }


    private class BindTextWatcher implements TextWatcher {
        private int which;

        BindTextWatcher(int which) {
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
                    iv_ab_del_un.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
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
