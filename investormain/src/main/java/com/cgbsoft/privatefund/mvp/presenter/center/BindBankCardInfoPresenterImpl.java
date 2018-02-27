package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;
import com.cgbsoft.privatefund.model.MineActivitesModel;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String, Object> hashMap = new HashMap<>();
        String cusno = AppManager.getPublicFundInf(getContext()) != null ? AppManager.getPublicFundInf(getContext()).getCustno() : "";
        hashMap.put("trantype", "520012");
        hashMap.put("custno", cusno);
        hashMap.put("signstatus", ""); // TODO
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
                            JSONObject dataJson = jsonArray.getJSONObject(0);
                            List<BindBankCardInfoBean> beanlist = new Gson().fromJson(dataJson.toString(), new TypeToken<List<BindBankCardInfoBean>>() {}.getType());
//                            perInfo[0] = dataJson.getString("bankname");
//                            perInfo[1] = dataJson.getString("depositacct");
                            getView().requestInfoSuccess(beanlist);
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
