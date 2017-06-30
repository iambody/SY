package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import app.privatefund.investor.health.mvp.contract.HealthBespeakContract;

/**
 * @author chenlong
 *
 */
public class HealthBespeakPresenter extends BasePresenterImpl<HealthBespeakContract.View> implements HealthBespeakContract.Presenter {

    public HealthBespeakPresenter(@NonNull Context context, @NonNull HealthBespeakContract.View view) {
        super(context, view);
    }

    @Override
    public void commitHealthBespeak(String id, String name, String phone, String capta) {
        addSubscription(ApiClient.bespeakHealth(ApiBusParam.getBespeakHealthParams(id, name, phone, capta)).subscribe(new RxSubscriber<String>(){

            @Override
            protected void onEvent(String s) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject result = jsonObject.getJSONObject("result");
                        if (result != null && !TextUtils.isEmpty(result.getString("orderNo"))) {
                            getView().bespeakSuccess();
                        } else {
                            getView().bespeakFailure();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getView().bespeakError(e.getMessage());
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().bespeakFailure();
            }
        }));
    }

    @Override
    public void getValidateCode(String phoneNumber) {
        addSubscription(ApiClient.bespeakHealthValidatePhone(ApiBusParam.getBespeakHealthValidateParams(phoneNumber)).subscribe(new RxSubscriber<String>(){
            @Override
            protected void onEvent(String s) {
                Log.d("HealthBespeakPresenter", s);
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.d("HealthBespeakPresenter", error.getMessage());
            }
        }));
    }
}
