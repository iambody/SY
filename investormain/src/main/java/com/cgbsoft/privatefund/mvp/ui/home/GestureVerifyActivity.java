package com.cgbsoft.privatefund.mvp.ui.home;

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
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogOutAccount;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.DubButtonWithLinkDialog;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.ModifyUserInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.home.ModifyUserInfoPresenter;
import com.chenenyu.router.annotation.Route;
import com.takwolf.android.lock9.Lock9View;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
@Route(RouteConfig.VALIDATE_GESTURE_PASSWORD)
public class GestureVerifyActivity extends BaseActivity<ModifyUserInfoPresenter> implements ModifyUserInfoContract.View {
    public static final String PARAM_CLOSE_PASSWORD = "PARAM_CLOSE_PASSWORD";
    public static final String PARAM_FROM_SHOW_ASSERT = "PARAM_FROM_SHWO_ASSERT";
    public static final String FROM_MODIFY_GESTURE = "PARAM_FROM_MODIFY";

    private int count;
    private boolean isFromShowAssert;
    private boolean isFromCloseGesturePassword;
    private boolean modifyGesturePassword;
    private String isFromAsertGroup;
    private Dialog dialog;

    @BindView(R.id.lock_9_view)
    Lock9View lock9View;

    @BindView(R.id.text_tip)
    TextView mTextTip;

    @BindView(R.id.text_forget_gesture)
    TextView mTextForget;

    @BindView(R.id.text_cancel)
    TextView backView;

    @BindView(R.id.app_image)
    RoundImageView imageLog;

    @Override
    protected void before() {
        super.before();
        isFromShowAssert = getIntent().getBooleanExtra(PARAM_FROM_SHOW_ASSERT, false);
        isFromCloseGesturePassword = getIntent().getBooleanExtra(PARAM_CLOSE_PASSWORD, false);
        modifyGesturePassword = getIntent().getBooleanExtra(FROM_MODIFY_GESTURE, false);
        isFromAsertGroup = getIntent().getStringExtra(GestureManager.PARAM_FROM_GROUP_ASSERT);
        count = AppManager.getValidateErrorNumber(this);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_gesture_verify;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setUpViews();
    }

    @Override
    protected ModifyUserInfoPresenter createPresenter() {
        return new ModifyUserInfoPresenter(getBaseContext(), this);
    }

    private void setUpViews() {
        if (!TextUtils.isEmpty(AppManager.getUserInfo(this).getHeadImageUrl())) {
            Imageload.display(this, AppManager.getUserInfo(this).getHeadImageUrl(), imageLog);
        }
        backView.setVisibility(isFromShowAssert || modifyGesturePassword || isFromCloseGesturePassword || !TextUtils.isEmpty(isFromAsertGroup) ? View.VISIBLE : View.GONE);
        lock9View.setCallBack(new Lock9View.CallBack() {
            @Override
            public void onFinish(String password) {
                if (!TextUtils.isEmpty(password) && password.equals(SPreference.getToCBean(GestureVerifyActivity.this).getGesturePassword())) {
                    Toast.makeText(GestureVerifyActivity.this, "密码校验成功", Toast.LENGTH_SHORT).show();
                    AppInfStore.saveValidateErrorNumber(GestureVerifyActivity.this, 5);
                    if (modifyGesturePassword) {
                        Intent intent = new Intent(GestureVerifyActivity.this, GestureEditActivity.class);
                        intent.putExtra(GestureEditActivity.PARAM_FROM_MODIFY, true);
                        startActivity(intent);
                        finish();
                    } else if (isFromShowAssert) {
                        GestureVerifyActivity.this.finish();
                        AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
                        RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
                    } else if (isFromCloseGesturePassword) {
                        closeGesturePassword(false);
                    } else {
                        if (!TextUtils.isEmpty(isFromAsertGroup)) {
                            AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
                            RxBus.get().post(RxConstant.SWITCH_GROUP_SHOW, isFromAsertGroup);
                        }
                        GestureVerifyActivity.this.finish();
                    }
                } else {
                    if (count == 1) {
                        DialogUtils.DialogSimplePrompt(GestureVerifyActivity.this, R.string.gesture_password_error_times, new DialogUtils.SimpleDialogListener() {
                            @Override
                            public void OnClickPositive() {
                                super.OnClickPositive();
                                closeGesturePassword(true);
                            }
                        });
                        AppInfStore.saveValidateErrorNumber(GestureVerifyActivity.this, 5);
                        return;
                    }
                    count -= 1;
                    AppInfStore.saveValidateErrorNumber(GestureVerifyActivity.this, count);
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
//        if (getIntent().getBooleanExtra(PARAM_FROM_LOGIN, false)) {
//            mTextTip.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void closeGesturePassword(final boolean isFiveTimesError) {
        Toast.makeText(GestureVerifyActivity.this, "关闭手势密码成功", Toast.LENGTH_SHORT).show();
        getPresenter().modifyUserInfo(ApiBusParam.gesturePasswordCloseParams(AppManager.getUserId(this)), isFiveTimesError);
    }

    @Override
    public void onBackPressed() {
        if (isFromShowAssert || modifyGesturePassword || !TextUtils.isEmpty(isFromAsertGroup) || isFromCloseGesturePassword) {
            GestureVerifyActivity.this.finish();
        }
        return;
    }

    @OnClick(R.id.text_forget_gesture)
    public void onForgetGesture() {
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
        DataStatistApiParam.forgetGesturePassword();
    }

    @OnClick(R.id.text_cancel)
    public void backText() {
        finish();
    }

    private void validateResetPassword(final Dialog dialog, String password) {
        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        this.dialog = dialog;
        getPresenter().validateUserPassword(ApiBusParam.gesturePasswordValidateParams(AppManager.getUserId(this), password));
    }

    @Override
    public void modifyUserSuccess(boolean isFiveTimesError) {
        AppInfStore.updateUserGesturePassword(this, "");
        RxBus.get().post(RxConstant.SET_PAGE_SWITCH_BUTTON, false);
        if (!isFiveTimesError) {
            Toast.makeText(GestureVerifyActivity.this, "关闭手势密码成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GestureVerifyActivity.this, "手势密码验证超过次数，请重新登录", Toast.LENGTH_SHORT).show();
            RxBus.get().post(RxConstant.CLOSE_SETTING_ACTIVITY_OBSERVABE, true);
            LogOutAccount returnLogin = new LogOutAccount(true);
            returnLogin.accounttExit(this);
        }
        finish();
    }

    @Override
    public void modifyUserFailure() {
        Toast.makeText(GestureVerifyActivity.this, "关闭手势密码失败", Toast.LENGTH_SHORT).show();
    }

    private void resetGesturePasswordDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.gesture_password_dialog);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_reset_gesture_password, null);
        dialog.setContentView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.accuont);
        textView.setText("当前账号：" + AppManager.getUserInfo(this).getPhoneNum());
        final EditText et_password = (EditText) layout.findViewById(R.id.input_login_password);
        TextView reset_gesture_password_dialog_forget_pwd = ViewHolders.get(layout, R.id.reset_gesture_password_dialog_forget_pwd);
        TextView telPhone = (TextView) layout.findViewById(R.id.tel_phone);
        final TextView concel = (TextView) layout.findViewById(R.id.quxiao);
        TextView comfirm = (TextView) layout.findViewById(R.id.queren);
        dialog.setCancelable(false);
        if (context != null) {
            dialog.show();
        }
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
        reset_gesture_password_dialog_forget_pwd.setOnClickListener(v -> {
            dialog.dismiss();
            NavigationUtils.startActivityByRouter(GestureVerifyActivity.this, RouteConfig.FORGAT_PASSWORD, "from_verify_forget_pwd", "1");
        });
    }

    private void processResult() {
        if (modifyGesturePassword) {
            DataStatistApiParam.realModifyGesturePassword();
            Intent intent = new Intent(GestureVerifyActivity.this, GestureEditActivity.class);
            intent.putExtra(GestureEditActivity.PARAM_FROM_MODIFY, true);
            startActivity(intent);
            finish();
        } else if (isFromShowAssert) {
            GestureVerifyActivity.this.finish();
            AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
            RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
        } else if (isFromCloseGesturePassword) {
            closeGesturePassword(false);
        } else {
            if (!TextUtils.isEmpty(isFromAsertGroup)) {
                AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
                RxBus.get().post(RxConstant.SWITCH_GROUP_SHOW, isFromAsertGroup);
            }
            GestureVerifyActivity.this.finish();
        }
    }

    @Override
    public void validatePasswordSuccess() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.dismiss();
        }
        processResult();
    }

    @Override
    public void validatePasswordFailure() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(GestureVerifyActivity.this, "输入登录密码错误，请重新输入！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void validatePasswordError(String errorMsg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(GestureVerifyActivity.this, errorMsg, Toast.LENGTH_LONG).show();
    }
}
