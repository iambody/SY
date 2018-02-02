package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundTradePwdModifyContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 */
public class PublicFundTradePwdModifyPresenterImpl extends BasePresenterImpl<PublicFundTradePwdModifyContract.PublicFundTradePwdModifyView> implements PublicFundTradePwdModifyContract.PublicFundTradePwdModifyoPresenter {

    private static final int PASSWORD_MODIFY_CODE = 0;
    private static final String KEY = "123456";

    public PublicFundTradePwdModifyPresenterImpl(@NonNull Context context, @NonNull PublicFundTradePwdModifyContract.PublicFundTradePwdModifyView view) {
        super(context, view);
    }

    @Override
    public void modifyPublicFundTradePwd(String identifyNo, String phoneNumber, String validateCode, String tradePwd) {
        getView().showLoadDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accttype", "7");
        hashMap.put("trantype", "520049");
        hashMap.put("mobileno", phoneNumber);
        hashMap.put("captcha", validateCode);
        hashMap.put("checkPhoneDuplicate", "901");
        hashMap.put("certificateno", identifyNo);
        hashMap.put("certificatetype", "0");
        hashMap.put("pwdtype", "0");
        hashMap.put("newpwd", tradePwd);
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
                            String modify = dataJson.getString("appsheetserialno");
                            if (!TextUtils.isEmpty(modify)) {
                                getView().modifyPwdSuccess("修改交易密码成功");
                                return;
                            }
                        }
                    }
                    getView().modifyPwdFailure("修改交易密码失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().modifyPwdFailure(e.getMessage());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("s", error.getMessage());
                getView().modifyPwdFailure(error.getMessage());
            }
        }));
    }

    @Override
    public void getPhoneValidateCode(String phone) {
        addSubscription(ApiClient.sendTestCode(phone, PASSWORD_MODIFY_CODE).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("getPhoneValidateCode", s);
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.d("getPhoneValidateCode", error.getMessage());
            }
        }));
    }
}
