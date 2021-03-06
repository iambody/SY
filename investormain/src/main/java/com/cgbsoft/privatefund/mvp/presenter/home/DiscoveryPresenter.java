package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.model.DiscoverModel;
import com.cgbsoft.lib.base.model.bean.StockIndexBean;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * @author chenlong
 *
 */
public class DiscoveryPresenter extends BasePresenterImpl<DiscoverContract.View> implements DiscoverContract.Presenter {
    public DiscoveryPresenter(@NonNull Context context, @NonNull DiscoverContract.View view) {
        super(context, view);
    }

    @Override
    public void getDiscoveryFirstData() {
        addSubscription(ApiClient.getDiscoverFirstData(new HashMap()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("DiscoveryPresenter", "----"+ s.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String vas = jsonObject.getString("result");
                    DiscoverModel result = new Gson().fromJson(vas, new TypeToken<DiscoverModel>() {}.getType());
                    System.out.println("-----result=" + result.informations.size());
                    getView().requestFirstDataSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestFirstDataFailure(error.getMessage());
            }
        }));
    }

    @Override
    public void getStockIndex() {
        addSubscription(ApiClient.getDiscoverStockIndex(new HashMap()).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    List<StockIndexBean> dataList = StockIndexBean.fillValueFromJson(jsonArray);
                    getView().requestStockIndexSuccess(dataList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().reqeustStockIndexFailure(e.getMessage());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().reqeustStockIndexFailure(error.getMessage());
            }
        }));
    }
}
