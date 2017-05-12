package com.cgbsoft.privatefund.widget.mvc;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.widget.mvc.view.GestureContentView;
import com.cgbsoft.privatefund.widget.mvc.view.GestureDrawline;
import com.cgbsoft.privatefund.widget.mvc.view.LockIndicator;
import com.chenenyu.router.annotation.Route;

/**
 * @author chenlong
 *         <p>
 *         手势密码设置页面
 */
@Route("investornmain_gestureeditactivity")
public class GestureEditActivity extends BaseActivity implements OnClickListener {
    public static final String PARAM_FROM_REGIST_OR_LOGIN = "PARAM_FROM_REGEIST_OR_LOGIN";
    public static final String PARAM_FROM_MODIFY = "PARAM_FROM_MODIFY";
    private ImageView mBackTitle;
    private TextView mTextJump;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;

    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private boolean fromRegistOrLoginPage = false;
    private boolean isModifyPassword = false;

    /**
     * 是否是手势密码=》忘记密码=》重置密码=》重置密码成功后=》重新设置密码（本界面）的流程进来的
     * 注意 理论上是 手势密码忘记密码进来的需要重新设置手势密码时候直接进入main页面 去掉存在手势密码的标识@TODO需要问龙哥 标识是怎么存储形式！！！！
     **/
    private boolean isFromVerifyForget;

    @Override
    protected void before() {
        super.before();
        fromRegistOrLoginPage = getIntent().getBooleanExtra(PARAM_FROM_REGIST_OR_LOGIN, false);
        isModifyPassword = getIntent().getBooleanExtra(PARAM_FROM_MODIFY, false);
        //判断是否是 手势密码=》忘记密码=》重置密码=》重置密码成功后=》重新设置密码 流程进来的 标识
//        if (getIntent().getExtras().containsKey(ForgetPasswordActivity.FROMVERIFYTAG))
//            isFromVerifyForget = getIntent().getStringExtra(ForgetPasswordActivity.FROMVERIFYTAG).equals("1");
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_gesture_edit;
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

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		isModifyPassword = getIntent().getBooleanExtra(PARAM_FROM_MODIFY, false);
//	}

    private void setUpViews() {
        mBackTitle = (ImageView) findViewById(R.id.title_left);
        mTextJump = (TextView) findViewById(R.id.title_right);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                System.out.println("------inputCode=" + inputCode);
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml(getString(R.string.please_right_gesture_password)));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextTip.setText(isModifyPassword ? R.string.please_target_gesture_password_again : R.string.reset_gesture_code);
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        updateGesturePassword(inputCode);
                    } else {
                        mTextTip.setText(Html.fromHtml(getResources().getString(R.string.set_gesture_agin)));
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {
            }

            @Override
            public void checkedFail() {
            }
        });
        mGestureContentView.setParentView(mGestureContainer);
        mTextTip.setText(isModifyPassword ? R.string.please_new_gesture_password : R.string.set_gesture_pattern_reason);
        updateCodeList("");
        mTextJump.setVisibility(fromRegistOrLoginPage ? View.VISIBLE : View.GONE);
        mBackTitle.setVisibility(fromRegistOrLoginPage ? View.GONE : View.VISIBLE);
    }

    private void setUpListeners() {
        mBackTitle.setOnClickListener(this);
        mTextJump.setOnClickListener(this);
    }

    private void updateCodeList(String inputCode) {
        mLockIndicator.setPath(inputCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
//                if (fromRegistOrLoginPage) {
//                    ReturnLogin returnLogin = new ReturnLogin();
//                    returnLogin.tokenExit(GestureEditActivity.this);
//                } else {
//                    finish();
//                }
                break;
            case R.id.title_right:
                NavigationUtils.toMainActivity(this);
                break;
            case R.id.text_reset:
//				if (SPSave.getInstance(this).getBoolean(Contant.weixin_login)) {
//					DubButtonWithLinkDialog dialog = new DubButtonWithLinkDialog(this, getString(R.string.weixin_reset_gesture_password), getString(R.string.hotline), "取消", "确认") {
//						@Override
//						public void left() {
//							dismiss();
//						}
//
//						@Override
//						public void right() {
//							dismiss();
//							NavigationUtils.startDialgTelephone(getContext(), getContext().getResources().getString(R.string.hotline));
//						}
//					};
//					dialog.show();
//				} else {
//					resetGesturePasswordDialog(this);
//				}
//				break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (fromRegistOrLoginPage) {
            return;
        }
        super.onBackPressed();
    }

    private void updateGesturePassword(final String password) {
//        new UpdateAdviserNameTask(this).start(ApiParams.updateGesturePassword(password), new HttpResponseListener() {
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("updateGesturePassword", "resopnse=" + response.toString());
//                    String result = response.getString("result");
//                    if ("suc".equals(result)) {
//                        MToast.makeText(GestureEditActivity.this, isModifyPassword ? R.string.modify_gesture_password_success : R.string.set_gesture_password_success, Toast.LENGTH_LONG).show();
//                        mGestureContentView.clearDrawlineState(0L);
//                        MApplication.getUser().getToC().setGesturePassword(password);
//                        MApplication.getUser().getToC().setGestureSwitch("1");
//                        SaveBeanUtil.putBean(GestureEditActivity.this, "userInfo", MApplication.getUser());
//                        System.out.println("------ setGestureSwitch----success");
//                        if(isFromVerifyForget){//是忘记密码进来的直接跳转首页
//                            NavigationUtils.toMainActivity(GestureEditActivity.this);
//                            return;
//                        }
//                        if (fromRegistOrLoginPage) {
//                            NavigationUtils.toMainActivity(GestureEditActivity.this);
//
//                        } else {
//                            EventBus.getDefault().post(new RefrushHtmlPage("1"));
//                            finish();
//                        }
//                    } else {
//                        new MToast(GestureEditActivity.this).show(isModifyPassword ? "修改手势密码失败" : "设置手势密码失败", 0);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onErrorResponse(String error, int statueCode) {
//                new MToast(GestureEditActivity.this).show(error, 0);
//            }
//        });
    }

//    public void onEventMainThread(ReturnLogin returnLogin) {
//        finish();
//    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }
}
