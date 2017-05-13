package com.cgbsoft.privatefund.widget.mvc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.DubButtonWithLinkDialog;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.takwolf.android.lock9.Lock9View;

import butterknife.BindView;

/**
 * @author chenlong
 */
@Route("investornmain_gestureverifyactivity")
public class GestureVerifyActivity extends BaseActivity implements View.OnClickListener {
    public static final String FROM_EXCCEED_TIIME = "exceedTime";
    public static final String PARAM_CLOSE_PASSWORD = "PARAM_CLOSE_PASSWORD";
    public static final String PARAM_FROM_LOGIN = "PARAM_FROM_LOGIN";
    public static final String PARAM_FROM_SWITCH = "PARAM_FROM_SWITCH";

    private int count = 5;
    private boolean isFromResumeIntercepter;
    private boolean isFromCloseGesturePassword;
    private boolean modifyGesturePassword;
    private boolean isFromSwitch;

    @BindView(R.id.lock_9_view)
    Lock9View lock9View;

    @BindView(R.id.text_tip)
    TextView mTextTip;

    @BindView(R.id.text_forget_gesture)
    TextView mTextForget;

    @Override
    protected void before() {
        super.before();
        isFromResumeIntercepter = getIntent().getBooleanExtra(FROM_EXCCEED_TIIME, false);
        isFromCloseGesturePassword = getIntent().getBooleanExtra(PARAM_CLOSE_PASSWORD, false);
        modifyGesturePassword = getIntent().getBooleanExtra(GestureEditActivity.PARAM_FROM_MODIFY, false);
        isFromSwitch = getIntent().getBooleanExtra(PARAM_FROM_SWITCH, false);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_gesture_verify;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setUpViews();
        setUpListeners();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void setUpViews() {
        lock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if (!TextUtils.isEmpty(password) && password.equals(SPreference.getToCBean(GestureVerifyActivity.this).getGesturePassword())) {
//                        mGestureContentView.clearDrawlineState(0L);
                    Toast.makeText(GestureVerifyActivity.this, "密码校验成功", Toast.LENGTH_SHORT).show();
                    if (modifyGesturePassword) {
                        Intent intent = new Intent(GestureVerifyActivity.this, GestureEditActivity.class);
                        intent.putExtra(GestureEditActivity.PARAM_FROM_MODIFY, true);
                        startActivity(intent);
                        finish();
                    } else if (getIntent().getBooleanExtra(PARAM_FROM_LOGIN, false)) {
                        NavigationUtils.toMainActivity(GestureVerifyActivity.this);
                        return;
                    } else if (isFromSwitch) {
                        NavigationUtils.toMainActivity(GestureVerifyActivity.this);
                        finish();
                        return;
                    }

                    if (isFromResumeIntercepter) {
                        GestureVerifyActivity.this.finish();
                    } else if (isFromCloseGesturePassword) {
                        closeGesturePassword(false);
                    } else {
                        GestureVerifyActivity.this.finish();
                    }
                } else {
                    if (count == 1) {
                        DialogUtils.DialogSimplePrompt(GestureVerifyActivity.this, R.string.gesture_password_error_times, new DialogUtils.SimpleDialogListener() {
                            @Override
                            public void OnClickPositive() {
                                super.OnClickPositive();
                                ((BaseApplication) InvestorAppli.getContext()).getBackgroundManager().setExitCalendar(null);
                                closeGesturePassword(true);
                            }
                        });
                        return;
                    }
                    count -= 1;
//                        mGestureContentView.clearDrawlineState(1300L);
                    mTextTip.setVisibility(View.VISIBLE);
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>密码输入错误，你还可以输入" + count + "次</font>"));
                    Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                    mTextTip.startAnimation(shakeAnimation);

                }

            }
        });
        if (modifyGesturePassword) {
            mTextTip.setText(R.string.please_target_gesture_password);
            mTextTip.setVisibility(View.VISIBLE);
        }
        if (getIntent().getBooleanExtra(PARAM_FROM_LOGIN, false)) {
            mTextTip.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void closeGesturePassword(final boolean isFiveTimesError) {
        Toast.makeText(GestureVerifyActivity.this, "关闭手势密码成功", Toast.LENGTH_SHORT).show();
        finish();
//        new UpdateAdviserNameTask(GestureVerifyActivity.this).start(ApiParams.closeGesturePassword(), new HttpResponseListener() {
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("closeGesturePassword", "resopnse=" + response.toString());
//                    String result = response.getString("result");
//                    if ("suc".equals(result)) {
//                        MApplication.getUser().getToC().setGesturePassword("");
//                        MApplication.getUser().getToC().setGestureSwitch("2");
//                        EventBus.getDefault().post(new RefrushHtmlPage("2"));
//                        SaveBeanUtil.putBean(GestureVerifyActivity.this, "userInfo", MApplication.getUser());
//                        if (!isFiveTimesError) {
//                            new MToast(GestureVerifyActivity.this).show("关闭手势密码成功", 0);
//                            finish();
//                        } else {
//                            ReturnLogin returnLogin = new ReturnLogin();
//                            returnLogin.tokenExit(GestureVerifyActivity.this);
//                        }
//                    } else {
//                        new MToast(GestureVerifyActivity.this).show("关闭手势密码失败", 0);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onErrorResponse(String error, int statueCode) {
//                new MToast(GestureVerifyActivity.this).show(error, 0);
//            }
//        });
    }

//    public void onEventMainThread(ReturnLogin returnLogin) {
//        finish();
//    }

    private void setUpListeners() {
        mTextForget.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        baseActivity.finish();
        return;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_cancel:
                System.exit(0);
                break;
            case R.id.text_forget_gesture:
//                DataStatistApiParam.onForgetGesturePassword();
                if (SPreference.getBoolean(this, Constant.weixin_login)) {
                    DubButtonWithLinkDialog dialog = new DubButtonWithLinkDialog(this, getString(R.string.weixin_reset_gesture_password), getString(R.string.hotline), "取消", "确认") {
                        @Override
                        public void left() {
                            dismiss();
                        }

                        @Override
                        public void right() {
                            NavigationUtils.startDialgTelephone(getContext(), getContext().getResources().getString(R.string.hotline));
                            dismiss();
                        }
                    };
                    dialog.show();
                } else {
                    resetGesturePasswordDialog(GestureVerifyActivity.this);
                }
                break;
            default:
                break;
        }
    }

    private void resetGesturePasswordDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.gesture_password_dialog);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_reset_gesture_password, null);
        dialog.setContentView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.accuont);
        textView.setText("当前账号：" + SPreference.getToCBean(this).getCustomerName());
        final EditText et_password = (EditText) layout.findViewById(R.id.input_login_password);
        TextView reset_gesture_password_dialog_forget_pwd = ViewHolders.get(layout, R.id.reset_gesture_password_dialog_forget_pwd);
        reset_gesture_password_dialog_forget_pwd.setOnClickListener(this);
        TextView telPhone = (TextView) layout.findViewById(R.id.tel_phone);
        final TextView concel = (TextView) layout.findViewById(R.id.quxiao);
        TextView comfirm = (TextView) layout.findViewById(R.id.queren);
        dialog.setCancelable(false);
        dialog.show();
        concel.setOnClickListener(v -> dialog.dismiss());
        comfirm.setOnClickListener(v -> {
            String vaus = et_password.getText().toString();
            if (TextUtils.isEmpty(vaus)) {
                Toast.makeText(GestureVerifyActivity.this, "请输入登录密码!", Toast.LENGTH_LONG).show();
                return;
            }

            validateResetPassword(dialog, vaus);
        });
        telPhone.setOnClickListener(v -> NavigationUtils.startDialgTelephone(GestureVerifyActivity.this, GestureVerifyActivity.this.getString(R.string.hotline)));
        reset_gesture_password_dialog_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.startActivityByRouter(GestureVerifyActivity.this, "investornmain_forgetpasswordctivity", "password", "1");
            }
        });
    }

    private void validateResetPassword(final Dialog dialog, String password) {
//        new ValidateLoginPasswordTask(GestureVerifyActivity.this).start(ApiParams.validateLoginPassword(password), new HttpResponseListener() {
//            public void onResponse(JSONObject response) {
//                try {
//                    System.out.println("-------rss" + response.toString());
//                    final String result = response.getString("result");
//                    ThreadUtils.runOnMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dialog.dismiss();
//                            if ("1".equals(result)) {
//                                processResult();
//                            } else {
//                                MToast.makeText(GestureVerifyActivity.this, "输入登录密码错误，请重新输入！", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onErrorResponse(String error, int statueCode) {
//                new MToast(GestureVerifyActivity.this).show(error, 0);
//            }
//        });
    }

    private void processResult() {
        if (modifyGesturePassword) {
            Intent intent = new Intent(GestureVerifyActivity.this, GestureEditActivity.class);
            intent.putExtra(GestureEditActivity.PARAM_FROM_MODIFY, true);
            startActivity(intent);
            finish();
        } else if (getIntent().getBooleanExtra(PARAM_FROM_LOGIN, false)) {
            NavigationUtils.toMainActivity(GestureVerifyActivity.this);
            return;
        } else if (isFromSwitch) {
            NavigationUtils.toMainActivity(this);
            return;
        }

        if (isFromResumeIntercepter) {
            GestureVerifyActivity.this.finish();
        } else if (isFromCloseGesturePassword) {
            closeGesturePassword(false);
        } else {
            GestureVerifyActivity.this.finish();
        }
    }
}
