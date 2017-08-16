package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.model.DiscoveryListModel;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverListContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author chenlong
 *
 */
public class DiscoveryListPresenter extends BasePresenterImpl<DiscoverListContract.View> implements DiscoverListContract.Presenter {

    public DiscoveryListPresenter(@NonNull Context context, @NonNull DiscoverListContract.View view) {
        super(context, view);
    }

    @Override
    public void getDiscoveryListData(String offset, String category) {
        addSubscription(ApiClient.getDiscoverListData(ApiBusParam.getDiscoverListDataParams(offset, category)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    Log.d("DiscoveryListPresenter", "----"+ s.toString());
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    String values = result.getString("informations");
                    List<DiscoveryListModel> discoverList = new Gson().fromJson(values, new TypeToken<List<DiscoveryListModel>>() {}.getType());
                    if (result != null) {
                        getView().requestListDataSuccess(discoverList);
                    } else {
                        getView().requestListDataFailure("数据加载错误！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestListDataFailure(error.getMessage());
            }
        }));

    }
}
