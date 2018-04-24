package com.cgbsoft.privatefund.public_fund;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.publicfund.BindCardOperationInf;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdContract;
import com.cgbsoft.privatefund.public_fund.passworddiglog.CustomPasswordView;
import com.commui.prompt.PromptManager;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * desc 交易密码
 * author wangyongkui
 */
public class TransactionPasswordActivity extends BaseActivity<TransactionPwdPresenter> implements TransactionPwdContract.transactionPwdView, View.OnClickListener {
    CustomPasswordView transaction_password_input;
    private String transactionPwd;


    @Override
    protected int layoutID() {
        return R.layout.activity_transaction_password;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title_mid)).setText("公募基金开户");
        transaction_password_input = (CustomPasswordView) findViewById(R.id.transaction_password_input);
        findViewById(R.id.transaction_pwd_confirm).setOnClickListener(this);
        //设置输入密码监听
        transaction_password_input.setOnPasswordChangedListener(new CustomPasswordView.OnPasswordChangedListener() {
            //正在输入密码时执行此方法
            public void onTextChanged(String psw) {
//                PromptManager.ShowCustomToast(baseContext, "正在输入文本中....."+psw);

                // 密码正在输入
            }

            //输入密码完成时执行此方法
            public void onInputFinish(String psw) {
//                PromptManager.ShowCustomToast(baseContext, psw);
                // 密码输入完成
                transactionPwd = psw;
                transaction_password_input.setPassword(psw);
//                if(mPassWordInputListener != null) mPassWordInputListener.onInputFinish(psw);
                Utils.hideKeyboard(baseContext);
            }
        });
        getPresenter().getOperation();
    }

    @Override
    protected TransactionPwdPresenter createPresenter() {
        return new TransactionPwdPresenter(this, this);
    }

    @Override
    public void showLoadDialog() {

    }

    @Override
    public void hideLoadDialog() {

    }

    //获取成功
    @Override
    public void getTransactionPwdSuccess(String str) {
        UiSkipUtils.gotoPublicFundRisk(TransactionPasswordActivity.this);
        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
    }

    //获取失败
    @Override
    public void getTransactionPwdError(String error) {
        Log.i("ssss", error);
    }

    BindCardOperationInf bindCardOperationInf;

    //运营信息成功
    @Override
    public void getOperationInfSuccess(String str) {
        if (BStrUtils.isEmpty(str)) return;
        bindCardOperationInf = new Gson().fromJson(str, BindCardOperationInf.class);
        BStrUtils.setTv((TextView) findViewById(R.id.transaction_pwd_declare), bindCardOperationInf.getPasswordSetupInfo());
        BStrUtils.setTv((TextView) findViewById(R.id.transaction_pwd_assist_declare), bindCardOperationInf.getPasswordSetupPswMessage());
        ((Button)findViewById(R.id.transaction_pwd_confirm)).setText(bindCardOperationInf.getPasswordAffirmBtnTitle());
        ((Button)findViewById(R.id.transaction_pwd_confirm)).setVisibility(View.VISIBLE);

    }

    @Override
    public void getOperationError(String error) {
        Log.i("ssss", error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction_pwd_confirm:
                if (BStrUtils.isEmpty(transactionPwd) || transactionPwd.length() < 6) {
                    PromptManager.ShowCustomToast(baseContext, getResources().getString(R.string.set_transaction_pwd));
                    return;
                }
                HashMap<String, String> obj = new HashMap<>();
                obj.put("tPasswd", transactionPwd);
                getPresenter().transactionPwdAction(obj);
                break;
        }

    }
}
