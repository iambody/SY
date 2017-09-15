package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceModel;

/**
 * @author chenlong
 *         健康介绍
 */
public class HealthIntroducePresenter extends BasePresenterImpl<HealthIntroduceContract.View> implements HealthIntroduceContract.Presenter {


    public HealthIntroducePresenter(@NonNull Context context, @NonNull HealthIntroduceContract.View view) {
        super(context, view);
    }

    @Override
    public void introduceHealth() {
        getView().showLoadDialog();
        addSubscription(ApiClient.getHealthIntruduce(new HashMap()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().hideLoadDialog();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String vas = jsonObject.getString("result");
                    HealthIntroduceModel result = new Gson().fromJson(vas, new TypeToken<HealthIntroduceModel>() {
                    }.getType());
                    getView().requestDataSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().hideLoadDialog();
                getView().requestDataFailure(error.getMessage());
            }
        }));
    }
}
