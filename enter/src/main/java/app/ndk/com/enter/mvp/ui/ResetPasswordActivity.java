package app.ndk.com.enter.mvp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.chenenyu.router.annotation.Route;

import java.util.concurrent.TimeUnit;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.ResetPasswordContract;
import app.ndk.com.enter.mvp.presenter.ResetPasswordPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 重置密码
 * Created by xiaoyu.zhang on 2016/11/18 14:50
 * Email:zhangxyfs@126.com
 *  
 */
@Route(RouteConfig.FORGAT_PASSWORD)
public class ResetPasswordActivity extends BaseActivity<ResetPasswordPresenter> implements ResetPasswordContract.View {

    public static final String FROMVERIFYTAG = "from_verify_forget_pwd";
    @BindView(R2.id.et_usernames_del_un)
    ImageView etUsernameDelUn;
    /**
     * 标记是否从手势密码弹出框中的忘记密码进来的  需要在完成一些列操作之后重新设置一遍手势密码
     **/
    private boolean isFromVerifyFogetPwd;

    private final int COOL_DOWN_TIME = 60;

    @BindView(R2.id.iv_af_back)
    ImageView iv_af_back;//返回按钮

    @BindView(R2.id.et_af_username)
    EditText et_af_username;//用户名

    @BindView(R2.id.iv_af_del_un)
    ImageView iv_af_del_un;//删除用户名

    @BindView(R2.id.et_af_check)
    EditText et_af_check;//验证码

    @BindView(R2.id.btn_af_check)
    Button btn_af_check;//验证码按钮

    @BindView(R2.id.btn_af_next)
    Button btn_af_next;//下一步按钮

    private LoadingDialog mLoadingDialog;//等待弹窗
    private boolean isUsernameInput, isCheckInput;
    private final int USERNAME_KEY = 1, CHECK_KEY = 3;
    private int identity;
    private DefaultDialog defaultDialog;
    private int countDownTime = COOL_DOWN_TIME;
    private Subscription countDownSub;
    private String UMENG_KEY = "logReg_click";

    private String lastInputPhoneNum;//刚才输入的手机号

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
    }

    @Override
    public void onBackPressed() {
        if (!isFromVerifyFogetPwd) {
            openActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_resetpwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //获取是否从手势密码弹出框忘记密码进来的
        if (getIntent().getExtras().containsKey(FROMVERIFYTAG)) {
            isFromVerifyFogetPwd = getIntent().getStringExtra(FROMVERIFYTAG).equals("1");
        }
        identity = getIntent().getIntExtra(IDS_KEY, -1);
//        if (AppManager.isAdViser(this)) {
//            iv_af_back.setImageResource(R.drawable.ic_toolbar_back);
//            btn_af_next.setBackgroundResource(R.drawable.select_btn_advister);
//            btn_af_next.setTextColor(0xff666666);
//        } else {
//            iv_af_back.setImageResource(R.drawable.ic_toolbar_back);
//            btn_af_next.setBackgroundResource(R.drawable.select_btn_inverstor);
//            btn_af_next.setTextColor(0xffffffff);
//        }
        et_af_username.addTextChangedListener(new ForgetTextWatcher(USERNAME_KEY));
        et_af_check.addTextChangedListener(new ForgetTextWatcher(CHECK_KEY));
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.sending_str), false, false);
        defaultDialog = new DefaultDialog(this, getString(R.string.ra_send_code_str, VOICE_PHONE), getString(R.string.btn_cancel_str), getString(R.string.ra_enter_code_str)) {
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

    /**
     * 返回按钮点击
     */
    @OnClick(R2.id.iv_af_back)
    void backClick() {
//        openActivity(LoginActivity.class);
        DataStatistApiParam.onStaticToCFindPasswordBack();
        finish();
    }

    /**
     * 删除用户名按钮点击
     */
    @OnClick(R2.id.iv_af_del_un)
    void delUsernameClick() {
        if (et_af_check.getText().toString().length() > 0) {
            et_af_check.setText("");
        }
        iv_af_del_un.setVisibility(View.GONE);
    }

    /**
     * 验证码按钮点击
     */
    @OnClick(R2.id.btn_af_check)
    void checkClick() {
        if (!isUsernameInput) {
            MToast.makeText(getApplicationContext(), getString(R.string.un_null_str), Toast.LENGTH_SHORT);
            return;
        }
        defaultDialog.show();
    }

    /**
     * 下一步按钮点击
     */
    @OnClick(R2.id.btn_af_next)
    void nextClick() {
        String userName = et_af_username.getText().toString();
        String code = et_af_check.getText().toString();
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
        if (AppManager.isAdViser(this)) {
            toUmengStatistics(UMENG_KEY, "按钮", "找回下一步");
        } else {
            toDataStatistics(2005, 20011, "下一步");
        }
        getPresenter().checkCode(mLoadingDialog, userName, code);
        DataStatistApiParam.onStaticToCFindPasswordNext();
    }

    @Override
    protected ResetPasswordPresenter createPresenter() {
        return new ResetPasswordPresenter(this, this);
    }

    /**
     * 验证码发送成功，开始倒计时
     */
    @Override
    public void sendSucc() {
        btn_af_check.setEnabled(false);
        btn_af_check.setBackgroundResource(R.drawable.bg_write_down);
        btn_af_check.setTextColor(getResources().getColor(R.color.gray_font));
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

    /**
     * 验证通过，跳转到设置密码页
     */
    @Override
    public void checkSucc() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra("userName", et_af_username.getText().toString());
        intent.putExtra("code", et_af_check.getText().toString());
        intent.putExtra(FROMVERIFYTAG, isFromVerifyFogetPwd ? "1" : "0");
        startActivity(intent);
        finish();
    }

    /**
     * 验证码按钮可以重新点击
     */
    private void reSetVerificationState() {
        btn_af_check.setEnabled(true);
        countDownTime = COOL_DOWN_TIME;
        countDownSub.unsubscribe();
        btn_af_check.setBackgroundResource(R.drawable.shape_red_line);
        btn_af_check.setTextColor(getResources().getColor(R.color.white));
        btn_af_check.setText(R.string.ra_code_resend_str);
    }


    @OnClick(R2.id.et_usernames_del_un)
    public void onViewClicked() {

        if (et_af_username.getText().toString().length() > 0) {
            et_af_username.setText("");
        }
        etUsernameDelUn.setVisibility(View.GONE);
        isUsernameInput=false;
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
                    etUsernameDelUn.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
                case CHECK_KEY:

                    iv_af_del_un.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
