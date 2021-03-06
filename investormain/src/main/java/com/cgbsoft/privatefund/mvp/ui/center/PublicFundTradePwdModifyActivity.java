package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundTradePwdModifyContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PublicFundTradePwdModifyPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @auther chenlong
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_TRADE_PWD_MODIFY_ACTIVITY)
public class PublicFundTradePwdModifyActivity extends BaseActivity<PublicFundTradePwdModifyPresenterImpl> implements PublicFundTradePwdModifyContract.PublicFundTradePwdModifyView {

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.et_identify_number)
    EditText et_identify_number;
    @BindView(R.id.et_phone_number)
    TextView et_phone_number;
    @BindView(R.id.get_phone_validate_code)
    TextView get_phone_validate_code;
    @BindView(R.id.et_validate_code)
    EditText et_validate_code;
    @BindView(R.id.et_trade_password)
    EditText et_trade_password;
    @BindView(R.id.commit)
    TextView commit;
    @BindView(R.id.hide_state)
    TextView hideState;

    private boolean hide_state = true;

    private CountDownTimer timer;
    private LoadingDialog mLoadingDialog;
    public static final int TIMER_TOTAL = 60 * 1000;
    public static final int TIMER_DELAYT = 1000;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_public_fund_trade_pwd_modify;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.modify_public_fund_trade_pwd));
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        PublicFundInf publicFundInf = AppManager.getPublicFundInf(getApplicationContext());
        et_phone_number.setText(publicFundInf.getMobileNo());
        timer = new CountDownTimer(TIMER_TOTAL, TIMER_DELAYT) {
            @Override
            public void onTick(long millisUntilFinished) {
                get_phone_validate_code.setText((String.valueOf(millisUntilFinished / 1000).concat("s")));
                get_phone_validate_code.setBackgroundResource(R.color.app_golden_disable);
                get_phone_validate_code.setEnabled(false);
            }

            @Override
            public void onFinish() {
                get_phone_validate_code.setText(R.string.get_phone_validate_code);
                get_phone_validate_code.setEnabled(true);
                get_phone_validate_code.setBackgroundResource(R.color.app_golden);
            }
        };
    }

    @OnClick(R.id.get_phone_validate_code)
    void getValidateCode() {
        if (TextUtils.isEmpty(et_phone_number.getText().toString())) {
            Toast.makeText(this, app.privatefund.investor.health.R.string.please_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        timer.start();
        getPresenter().getPhoneValidateCode(et_phone_number.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.hide_state)
    void hideState(){
        if (hide_state){
            hideState.setBackground(getDrawable(R.drawable.show_psw_icon));
            et_trade_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hide_state = false;
        }else {
            et_trade_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideState.setBackground(getDrawable(R.drawable.hide_icon));
            hide_state = true;
        }
    }

    @OnClick(R.id.commit)
    void commitModifyTradePwd() {
        if (TextUtils.isEmpty(et_identify_number.getText().toString())) {
            Toast.makeText(this, R.string.hint_identify_number, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(et_phone_number.getText().toString())) {
            Toast.makeText(this, R.string.hint_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_validate_code.getText().toString())) {
            Toast.makeText(this, R.string.hint_validate_code, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_trade_password.getText().toString())) {
            Toast.makeText(this, R.string.hint_trade_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ViewUtils.checkIdNumberRegex(et_identify_number.getText().toString())) {
            Toast.makeText(this, R.string.hint_identify_number_erro_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ViewUtils.checkPhoneNumberRegex(et_phone_number.getText().toString())) {
            Toast.makeText(this, R.string.hint_phone_number_error_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        getPresenter().modifyPublicFundTradePwd(et_identify_number.getText().toString(), et_phone_number.getText().toString(), et_validate_code.getText().toString(), et_trade_password.getText().toString());
    }

    @Override
    protected PublicFundTradePwdModifyPresenterImpl createPresenter() {
        return new PublicFundTradePwdModifyPresenterImpl(this, this);
    }

    @Override
    public void showLoadDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void modifyPwdSuccess(String info) {
        hideLoadDialog();
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void modifyPwdFailure(String message) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
