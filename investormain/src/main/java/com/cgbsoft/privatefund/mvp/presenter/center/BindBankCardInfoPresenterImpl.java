package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;
import com.cgbsoft.privatefund.bean.DataDictionary;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        addSubscription(ApiClient.boundCardsPublicFund().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonArray != null) {
                        if (jsonArray != null && jsonArray.length() > 0) {
                            List<BindBankCardInfoBean> beanlist = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BindBankCardInfoBean>>() {}.getType());
                            getView().requestInfoSuccess(beanlist);
                            return;
                        }
                    }
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

    @Override
    public void unBindUserCard(String channelid, String custno, String depositacct, String tpasswd) {
        getView().showLoadDialog();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trantype", "520261");
        hashMap.put("channelid", channelid);
        hashMap.put("custno", custno);
        hashMap.put("depositacct", depositacct);
        hashMap.put("tpasswd", tpasswd);
        addSubscription(ApiClient.directRequestJzServer(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonObject = jsonObject.getJSONObject("result");
                    if (jsonObject != null) {
                        Log.i("unBindUserCard", jsonObject.toString());
                        String errorCode = jsonObject.optString("errorCode");
                        String erroMessage = jsonObject.optString("errorMessage");
                        if (TextUtils.equals(errorCode, "0000")) {
                            getView().unBindCardSuccess();
                        } else {
                            getView().unBindCardFailure(erroMessage);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().unBindCardFailure(e.getMessage());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().unBindCardFailure(error.getMessage());
            }
        }));
    }

    @Override
    public void requsetSubbranchBankInfo() {
        getView().showLoadDialog();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trantype", "520020");
        hashMap.put("dictitem", "110080");
        addSubscription(ApiClient.directRequestJzServer(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonObject = jsonObject.getJSONObject("result");
                    if (jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("datasets");
                        Log.i("SubbranchBankInfo", jsonArray.toString());
                        jsonArray = jsonArray.getJSONArray(0);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            List<DataDictionary> dataList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DataDictionary>>() {}.getType());
                            getView().requestSubbranchBankSuccess(dataList);
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().requestSubbranckBankFailure(e.getMessage());
                }
                getView().requestInfoFailure("查询银行信息失败");
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestSubbranckBankFailure(error.getMessage());
            }
        }));
    }
}
