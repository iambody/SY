package app.ndk.com.enter.mvp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.SetPasswordContract;
import app.ndk.com.enter.mvp.presenter.SetPasswordPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重新设置密码
 * Created by xiaoyu.zhang on 2016/11/23 15:49
 * Email:zhangxyfs@126.com
 *  
 */
public class SetPasswordActivity extends BaseActivity<SetPasswordPresenter> implements SetPasswordContract.View {
    /**
     * 返回按钮
     */
    @BindView(R2.id.iv_as_back)
    ImageView iv_as_back;

    /**
     * 第一次输入密码
     */
    @BindView(R2.id.et_as_password1)
    EditText et_as_password1;

    /**
     * 删除第一次输入的密码
     */
    @BindView(R2.id.iv_as_del_pw1)
    ImageView iv_as_del_pw1;

    /**
     * 第二次输入密码
     */
    @BindView(R2.id.et_as_password2)
    EditText et_as_password2;

    /**
     * 删除第二次输入的密码
     */
    @BindView(R2.id.iv_as_del_pw2)
    ImageView iv_as_del_pw2;

    /**
     * 完成
     */
    @BindView(R2.id.btn_as_ok)
    Button btn_as_ok;

    private LoadingDialog mLoadingDialog;
    private boolean isPassword1Input, isPassword2Input;
    private final int PASSWORD1_KEY = 1, PASSWORD2_KEY = 2;
    private int identity;
    private String userName, code;

    @Override
    public void onBackPressed() {
        openActivity(ResetPasswordActivity.class);
        finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_setpwd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userName = getIntent().getStringExtra("userName");
        code = getIntent().getStringExtra("code");
        identity = getIntent().getIntExtra(IDS_KEY, -1);
        if (AppManager.isAdViser(this)) {
            iv_as_back.setImageResource(R.drawable.ic_toolbar_back_al_adviser);
            btn_as_ok.setBackgroundResource(R.drawable.select_btn_advister);
            btn_as_ok.setTextColor(0xff666666);
        } else {
            iv_as_back.setImageResource(R.drawable.ic_toolbar_back_al_investor);
            btn_as_ok.setBackgroundResource(R.drawable.select_btn_inverstor);
            btn_as_ok.setTextColor(0xffffffff);
        }
        et_as_password1.addTextChangedListener(new SetPasswordTextWatcher(PASSWORD1_KEY));
        et_as_password2.addTextChangedListener(new SetPasswordTextWatcher(PASSWORD2_KEY));
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.reseting_str), false, false);
    }

    @OnClick(R2.id.iv_as_back)
    void backClick() {
        openActivity(ResetPasswordActivity.class);
        finish();
    }

    @OnClick(R2.id.iv_as_del_pw1)
    void delPwd1Click() {
        if (et_as_password1.getText().toString().length() > 0) {
            et_as_password1.setText("");
        }
        iv_as_del_pw1.setVisibility(View.GONE);
    }

    @OnClick(R2.id.iv_as_del_pw2)
    void delPwd2Click() {
        if (et_as_password2.getText().toString().length() > 0) {
            et_as_password2.setText("");
        }
        iv_as_del_pw2.setVisibility(View.GONE);
    }

    @OnClick(R2.id.btn_as_ok)
    void okClick() {
        String pwd1 = et_as_password1.getText().toString();
        String pwd2 = et_as_password2.getText().toString();
        if (!isPassword1Input || isPassword2Input) {
            MToast.makeText(getApplicationContext(), getString(R.string.pw_null_str), Toast.LENGTH_SHORT);
            return;
        }
        if (!TextUtils.equals(pwd1, pwd2)) {
            MToast.makeText(getApplicationContext(), getString(R.string.as_nosame_str), Toast.LENGTH_SHORT);
            return;
        }

        getPresenter().resetPwd(mLoadingDialog, userName, pwd1, code);

    }

    @Override
    protected SetPasswordPresenter createPresenter() {
        return new SetPasswordPresenter(this, this);
    }

    @Override
    public void toFinish() {
        finish();
    }

    private class SetPasswordTextWatcher implements TextWatcher {
        private int which;

        SetPasswordTextWatcher(int which) {
            this.which = which;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean isTextHasLength = s.length() > 0;
            switch (which) {
                case PASSWORD1_KEY:
                    isPassword1Input = isTextHasLength;
                    iv_as_del_pw1.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
                case PASSWORD2_KEY:
                    isPassword2Input = isTextHasLength;
                    iv_as_del_pw2.setVisibility(isTextHasLength ? View.VISIBLE : View.GONE);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
