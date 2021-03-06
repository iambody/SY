package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.LockIndicator;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.ModifyUserInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.home.ModifyUserInfoPresenter;
import com.chenenyu.router.annotation.Route;
import com.takwolf.android.lock9.Lock9View;

import app.ndk.com.enter.mvp.ui.ResetPasswordActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 *         <p>
 *         手势密码设置页面
 */
@Route(RouteConfig.SET_GESTURE_PASSWORD)
public class GestureEditActivity extends BaseActivity<ModifyUserInfoPresenter> implements ModifyUserInfoContract.View {

    public static final String PARAM_FROM_SET_GESTURE = "PARAM_FROM_SET_GESTURE";
    public static final String PARAM_FROM_MODIFY = "PARAM_FROM_MODIFY";
    public static final String PARAM_FROM_SHOW_ASSERT = "PARAM_FROM_SHWO_ASSERT";
    public static final String PARAM_FROM_FORGET = "PARAM_FROM_FORGET";

    /**
     * 是否是手势密码=》忘记密码=》重置密码=》重置密码成功后=》重新设置密码（本界面）的流程进来的
     * 注意 理论上是 手势密码忘记密码进来的需要重新设置手势密码时候直接进入main页面 去掉存在手势密码的标识@TODO需要问龙哥 标识是怎么存储形式！！！！
     **/
    private boolean isFromVerifyForget;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private boolean fromSetPageSetGestrue = false;
    private boolean isModifyPassword = false;
    private String password;

    @BindView(R.id.lock_9_view)
    Lock9View lock9View;

    @BindView(R.id.title_back_left)
    ImageView mBackTitle;

    @BindView(R.id.text_tip)
    TextView mTextTip;

    @BindView(R.id.lock_indicator)
    LockIndicator mLockIndicator;

    private boolean isFromShowAssert;
    private String isFromGroupAssert;

    @Override
    protected void before() {
        super.before();
        fromSetPageSetGestrue = getIntent().getBooleanExtra(PARAM_FROM_SET_GESTURE, false);
        isFromShowAssert = getIntent().getBooleanExtra(PARAM_FROM_SHOW_ASSERT, false);
        isModifyPassword = getIntent().getBooleanExtra(PARAM_FROM_MODIFY, false);
        isFromGroupAssert = getIntent().getStringExtra(GestureManager.PARAM_FROM_GROUP_ASSERT);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(ResetPasswordActivity.FROMVERIFYTAG)) {
            isFromVerifyForget = getIntent().getStringExtra(ResetPasswordActivity.FROMVERIFYTAG).equals("1");
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_gesture_edit;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setUpViews();
        mBackTitle.setVisibility(View.VISIBLE);
        mBackTitle.setOnClickListener(v -> finish());
    }

    @Override
    protected ModifyUserInfoPresenter createPresenter() {
        return new ModifyUserInfoPresenter(getBaseContext(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		isModifyPassword = getIntent().getBooleanExtra(PARAM_FROM_MODIFY, false);
	}

    private void setUpViews() {
        lock9View.setCallBack(inputCode -> {
            if (!isInputPassValidate(inputCode)) {
                mTextTip.setText(Html.fromHtml(getString(R.string.please_right_gesture_password)));
                return;
            }
            if (mIsFirstInput) {
                mFirstPassword = inputCode;
                updateCodeList(inputCode);
                mTextTip.setText(isModifyPassword ? R.string.please_target_gesture_password_again : R.string.reset_gesture_code);
            } else {
                if (inputCode.equals(mFirstPassword)) {
                    updateGesturePassword(inputCode);
                } else {
                    mTextTip.setText(Html.fromHtml(getResources().getString(R.string.set_gesture_agin)));
                    Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                    mTextTip.startAnimation(shakeAnimation);
                }
            }
            mIsFirstInput = false;
        });
        mTextTip.setText(isModifyPassword ? R.string.please_new_gesture_password : R.string.set_gesture_pattern_reason);
        updateCodeList("");
    }

    private void updateCodeList(String inputCode) {
        mLockIndicator.setPath(inputCode);
    }

//    @OnClick(R.id.gesture_top_right)
//    public void nowToJumpPage() {
//        finish();
//        RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
//    }

//    @OnClick(R.id.text_reset)
//    void resetClick() {
//        if (SPreference.getBoolean(this, Constant.weixin_login)) {
//            DubButtonWithLinkDialog dialog = new DubButtonWithLinkDialog(this, getString(R.string.weixin_reset_gesture_password), getString(R.string.hotline), "取消", "确认") {
//                @Override
//                public void left() {
//                    dismiss();
//                }
//
//                @Override
//                public void right() {
//                    dismiss();
//                    NavigationUtils.startDialgTelephone(GestureEditActivity.this, getResources().getString(R.string.hotline));
//                }
//            };
//            dialog.show();
//        } else {
////        resetGesturePasswordDialog(this);
//        }
//    }

    @Override
    public void onBackPressed() {
//        if (fromSetPageSetGestrue) {
//            return;
//        }
        super.onBackPressed();
    }

    private void updateGesturePassword(final String newPassword) {
        password = newPassword;
        getPresenter().modifyUserInfo(ApiBusParam.gesturePasswordSetParams(AppManager.getUserId(this), newPassword), false);
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

    @Override
    public void modifyUserSuccess(boolean isFiveTimes) {
        Toast.makeText(GestureEditActivity.this, isModifyPassword ? R.string.modify_gesture_password_success : R.string.set_gesture_password_success, Toast.LENGTH_LONG).show();
        AppInfStore.updateUserGesturePassword(this, password);
        if(isFromVerifyForget){//是忘记密码进来的直接跳转首页
            NavigationUtils.toMainActivity(GestureEditActivity.this);
            finish();
            return;
        }

        if (!TextUtils.isEmpty(isFromGroupAssert)) {
            AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
            RxBus.get().post(RxConstant.SWITCH_GROUP_SHOW, isFromGroupAssert);
            finish();
            return;
        }

        if (isFromShowAssert) {
            RxBus.get().post(RxConstant.SWITCH_ASSERT_SHOW, true);
            AppInfStore.saveLastSetAndValidateTime(getAppli(), System.currentTimeMillis());
            finish();
        } else if (fromSetPageSetGestrue) {
            AppInfStore.updateUserGesturePassword(this, mFirstPassword);
            finish();
            RxBus.get().post(RxConstant.SET_PAGE_SWITCH_BUTTON, true);
        } else if (isModifyPassword){
            finish();
        }
    }

    @Override
    public void modifyUserFailure() {
        Toast.makeText(GestureEditActivity.this, isModifyPassword ? "修改手势密码失败" : "设置手势密码失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void validatePasswordSuccess() {}

    @Override
    public void validatePasswordFailure() {}

    @Override
    public void validatePasswordError(String errorMsg) {}
}
