package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundInfoContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 */
public class PublicFundInfoPresenterImpl extends BasePresenterImpl<PublicFundInfoContract.PublicFundInfoView> implements PublicFundInfoContract.PublicFundInfoPresenter {


    public PublicFundInfoPresenterImpl(@NonNull Context context, @NonNull PublicFundInfoContract.PublicFundInfoView view) {
        super(context, view);
    }

    @Override
    public void requestPublicFundInfo() {
        getView().showLoadDialog();
        HashMap<String, Object> hashMap = new HashMap<>();
        String cusno = AppManager.getPublicFundInf(getContext()) != null ? AppManager.getPublicFundInf(getContext()).getCustno() : "";
        hashMap.put("trantype", "520101");
        hashMap.put("custno", cusno);
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
                            String[] perInfo = new String[2];
                            perInfo[0] = dataJson.getString("custfullname");
                            perInfo[1] = dataJson.getString("certificateno");
                            getView().requestInfoSuccess(perInfo);
                            return;
                        }
                    }
                    getView().requestInfoFailure("查询公募基金账号失败");
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
