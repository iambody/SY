package app.ndk.com.enter.mvp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.IOSDialog;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.ProtocolDialog;

import java.util.concurrent.TimeUnit;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.RegisterContract;
import app.ndk.com.enter.mvp.presenter.RegisterPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 注册
 * Created by xiaoyu.zhang on 2016/11/17 18:24
 * Email:zhangxyfs@126.com
 *  
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    private final int COOL_DOWN_TIME = 60;

    @BindView(R2.id.iv_ar_back)
    ImageView iv_ar_back;//返回按钮

    @BindView(R2.id.et_ar_username)
    EditText et_ar_username;//用户名

    @BindView(R2.id.iv_ar_del_un)
    ImageView iv_ar_del_un;//删除用户名

    @BindView(R2.id.et_ar_password)
    EditText et_ar_password;//密码

    @BindView(R2.id.iv_ar_del_pw)
    ImageView iv_ar_del_pw;//删除密码

    @BindView(R2.id.et_ar_check)
    EditText et_ar_check;//验证码

    @BindView(R2.id.btn_ar_check)
    Button btn_ar_check;//验证码按钮

    @BindView(R2.id.btn_ar_register)
    Button btn_ar_register;//注册

    @BindView(R2.id.cb_ar)
    CheckBox cb_ar;

    @BindView(R2.id.tv_ar_proto)
    TextView tv_ar_proto;

    private LoadingDialog mLoadingDialog;
    private boolean isUsernameInput, isPasswordInput, isCheckInput, isCheckBoxSel = true;
    private final int USERNAME_KEY = 1, PASSWORD_KEY = 2, CHECK_KEY = 3;
    private int identity;
    private final String UMENG_KEY = "logReg_click";
    private IOSDialog miOSDialog;
    private int countDownTime = COOL_DOWN_TIME;
    private Subscription countDownSub;

    @Override
    public void onBackPressed() {
        openActivity(LoginActivity.class);
        finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        identity = getIntent().getIntExtra(IDS_KEY, -1);
        if (identity < 0) {
            identity = SPreference.getIdtentify(getApplicationContext());
        }
        switch (identity) {
            case IDS_ADVISER:
                iv_ar_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
                btn_ar_register.setBackgroundResource(R.drawable.select_btn_advister);
                btn_ar_register.setTextColor(0xff666666);
                break;
            case IDS_INVERSTOR:
                iv_ar_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
                btn_ar_register.setBackgroundResource(R.drawable.select_btn_inverstor);
                btn_ar_register.setTextColor(0xffffffff);
                break;
        }

        et_ar_username.addTextChangedListener(new RegisterTextWatcher(USERNAME_KEY));
        et_ar_password.addTextChangedListener(new RegisterTextWatcher(PASSWORD_KEY));
        et_ar_check.addTextChangedListener(new RegisterTextWatcher(CHECK_KEY));

        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.ra_register_loading_str), false, false);
        miOSDialog = new IOSDialog(this, "", getString(R.string.ra_send_code_str, VOICE_PHONE), getString(R.string.btn_cancel_str), getString(R.string.ra_enter_code_str)) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right() {
                this.dismiss();
                toDataStatistics(1002, 10010, "注册验证码获取");
                getPresenter().sendCode(mLoadingDialog, et_ar_username.getText().toString());
            }
        };

        cb_ar.setOnCheckedChangeListener((buttonView, isChecked) -> isCheckBoxSel = isChecked);
    }

    @OnClick(R2.id.iv_ar_back)
    void backClick() {
        openActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R2.id.et_ar_username)
    void usernameEtClick() {
        toDataStatistics(1002, 10009, "注册手机");
    }

    @OnClick(R2.id.et_ar_password)
    void passwordEtClick() {
        toDataStatistics(1002, 10011, "注册手机");
    }

    @OnClick(R2.id.iv_ar_del_un)
    void delUsernameClick() {
        if (et_ar_username.getText().toString().length() > 0) {
            et_ar_username.setText("");
        }
        iv_ar_del_un.setVisibility(View.GONE);
    }

    @OnClick(R2.id.iv_ar_del_pw)
    void delPasswordClick() {
        if (et_ar_password.getText().toString().length() > 0) {
            et_ar_password.setText("");
        }
        iv_ar_del_pw.setVisibility(View.GONE);
    }

    @OnClick(R2.id.btn_ar_check)
    void checkClick() {
        toUmengStatistics(UMENG_KEY, "按钮", "发送验证码");
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        miOSDialog.show();
    }

    @OnClick(R2.id.btn_ar_register)
    void registerClick() {
        String userName = et_ar_username.getText().toString();
        String password = et_ar_password.getText().toString();
        String code = et_ar_check.getText().toString();
        toUmengStatistics(UMENG_KEY, "按钮", "注册");
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isPasswordInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.pw_null_str), Toast.LENGTH_SHORT);
            return;
        } else if (password.length() > 16 || password.length() < 6) {
            MToast.makeText(getApplicationContext(), getString(R.string.pwd_noright_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isCheckInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.code_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!isCheckBoxSel) {
            MToast.makeText(getApplicationContext(), getString(R.string.proto_notread_str), Toast.LENGTH_SHORT);
            return;
        }
        toDataStatistics(1002, 10012, "提交注册");
        getPresenter().toRegister(mLoadingDialog, userName, password, code);
    }

    @OnClick(R2.id.tv_ar_proto)
    void protoClick() {
        new ProtocolDialog(this, 0, null);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this, this);
    }

    @Override
    public void regSucc() {
//        openActivity(MainPageActivity.class);
        finish();
    }

    @Override
    public void regFail() {
    }

    @Override
    public void sendSucc() {
        btn_ar_check.setEnabled(false);
        btn_ar_check.setBackgroundResource(R.drawable.bg_write_down);
        btn_ar_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
        countDownSub = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<Long>() {
                    @Override
                    protected void onEvent(Long aLong) {
                        if (countDownTime < 0) {
                            reSetVerificationState();
                        } else {
                            btn_ar_check.setText(String.valueOf("倒计时" + countDownTime-- + "s"));
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {

                    }
                });
    }

    private void reSetVerificationState() {
        btn_ar_check.setEnabled(true);
        countDownTime = COOL_DOWN_TIME;
        countDownSub.unsubscribe();
        btn_ar_check.setBackgroundResource(R.drawable.shape_red_line);
        btn_ar_check.setTextColor(getResources().getColor(R.color.white));
        btn_ar_check.setText(R.string.ra_code_resend_str);
    }

    private class RegisterTextWatcher implements TextWatcher {
        private int which;

        RegisterTextWatcher(int which) {
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
                    iv_ar_del_un.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
                case PASSWORD_KEY:
                    isPasswordInput = isTextHasLength;
                    iv_ar_del_pw.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
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
