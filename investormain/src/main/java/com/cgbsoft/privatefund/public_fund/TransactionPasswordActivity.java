package com.cgbsoft.privatefund.public_fund;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdContract;
import com.cgbsoft.privatefund.public_fund.passworddiglog.CustomPasswordView;
import com.commui.prompt.PromptManager;

import java.util.HashMap;

/**
 * desc
 * author wangyongkui
 */
public class TransactionPasswordActivity extends BaseActivity<TransactionPwdPresenter> implements TransactionPwdContract.transactionPwdView, View.OnClickListener {
    CustomPasswordView transaction_password_input;

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
                // 密码正在输入
            }

            //输入密码完成时执行此方法
            public void onInputFinish(String psw) {
                PromptManager.ShowCustomToast(baseContext, psw);
                // 密码输入完成
                transaction_password_input.setPassword(psw);
//                if(mPassWordInputListener != null) mPassWordInputListener.onInputFinish(psw);
                Utils.hideKeyboard(baseContext);
            }
        });
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

    }

    //获取失败
    @Override
    public void getTransactionPwdError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction_pwd_confirm:
                HashMap<String, Object> obj = new HashMap<>();
                getPresenter().transactionPwdAction(obj);
                break;
        }

    }
}
