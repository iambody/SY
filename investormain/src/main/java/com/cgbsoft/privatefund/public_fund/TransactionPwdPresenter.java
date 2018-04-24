package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdContract;
import com.cgbsoft.privatefund.public_fund.model.TransactionPwdListener;
import com.cgbsoft.privatefund.public_fund.model.impl.TransactionPwdImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * desc
 * author wangyongkui
 */
public class TransactionPwdPresenter extends BasePresenterImpl<TransactionPwdContract.transactionPwdView> implements TransactionPwdContract.transactionPwdPresenter, TransactionPwdListener {
    public TransactionPwdImpl transactionPwdModle;
    public TransactionPwdContract.transactionPwdView transactionpwdView;

    public TransactionPwdPresenter(@NonNull Context context, @NonNull TransactionPwdContract.transactionPwdView view) {
        super(context, view);
        transactionPwdModle = new TransactionPwdImpl();
        transactionpwdView = view;

    }

    @Override
    public void transactionPwdAction(HashMap<String, String> map) {
        transactionpwdView.showLoadDialog();
        transactionPwdModle.transactionPwdAction(getCompositeSubscription(), map, this);
    }

    @Override
    public void getOperation() {
        transactionPwdModle.getOperationInf(getCompositeSubscription(), this);
    }

    @Override
    public void getTransactionPwdSuccess(String str) {
        transactionpwdView.hideLoadDialog();
        transactionpwdView.getTransactionPwdSuccess(str);
    }

    @Override
    public void getTransactionPwdError(String error) {
        transactionpwdView.hideLoadDialog();
        transactionpwdView.getTransactionPwdError(error);
    }

    @Override
    public void getOperationSuccess(String str) {
        if (BStrUtils.isEmpty(str)) {
            transactionpwdView.getOperationError("信息失败");
            return;
        }
        try {
            JSONObject object = new JSONObject(str);

            transactionpwdView.getOperationInfSuccess(object.getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getOperationError(String error) {
        transactionpwdView.getOperationError(error);
    }
}
