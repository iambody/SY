package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 */

public class BindBankCardInfoPresenterImpl extends BasePresenterImpl<BindBankCardInfoContract.BindBankCardInfoView> implements BindBankCardInfoContract.BindBankCardInfoPresenter {


    public BindBankCardInfoPresenterImpl(@NonNull Context context, @NonNull BindBankCardInfoContract.BindBankCardInfoView view) {
        super(context, view);
    }

    @Override
    public void requestBindBankCardInfo() {
        getView().showLoadDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        String cusno = AppManager.getPublicFundInf(getContext()) != null ? AppManager.getPublicFundInf(getContext()).getCustno() : "";
        hashMap.put("trantype", "520012");
        hashMap.put("custno", cusno);
        hashMap.put("signstatus", "1");
        addSubscription(ApiClient.directRequestJzServer(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonObject = jsonObject.getJSONObject("result");
                    if (jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("datasets");
                        jsonArray = jsonArray.getJSONArray(0);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            String[] perInfo = new String[3];
                            JSONObject dataJson = jsonArray.getJSONObject(0);
                            perInfo[0] = dataJson.getString("bankname");
                            perInfo[1] = dataJson.getString("depositcard");
                            perInfo[2] = "储蓄卡";
                            getView().requestInfoSuccess(perInfo);
                            return;
                        }
                    }
                    getView().requestInfoFailure("查询银行信息失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().requestInfoFailure(e.getMessage());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("s", "sss");
                getView().requestInfoFailure(error.getMessage());
            }
        }));

    }
}
