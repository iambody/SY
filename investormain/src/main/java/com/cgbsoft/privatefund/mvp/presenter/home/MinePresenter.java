package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.model.MineModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 *
 */
public class MinePresenter extends BasePresenterImpl<MineContract.View> implements MineContract.Presenter {

    public MinePresenter(@NonNull Context context, @NonNull MineContract.View view) {
        super(context, view);

    }

    @Override
    public void getMineData() {
        addSubscription(ApiClient.getMineData(new HashMap()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    Log.d("MinePresenter", "----"+ s.toString());
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    MineModel mineModel = new Gson().fromJson(result, new TypeToken<MineModel>() {}.getType());
                    if (result != null) {
                        getView().requestDataSuccess(mineModel);
                    } else {
                        getView().requestDataFailure("数据加载错误！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestDataFailure(error.getMessage());
            }
        }));
    }
}
