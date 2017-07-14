package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.privatefund.adapter.MineActivitesListAdapter;
import com.cgbsoft.privatefund.model.MineActivitesModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineActivitesContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chenlong
 */
public class MineActivitesPresenter extends BasePresenterImpl<MineActivitesContract.View> implements MineActivitesContract.Presenter {

    private int index = 0;
    private final static int PAGE_LIMIT = 20;

    public MineActivitesPresenter(@NonNull Context context, @NonNull MineActivitesContract.View view) {
        super(context, view);
    }

    @Override
    public void getActivitesList(MineActivitesListAdapter adapter, boolean isRef) {
        if (isRef) {
            index = 0;
        } else {
            index++;
        }
        addSubscription(ApiClient.getMineActivitesList(ApiBusParam.getActivitesListData(index * PAGE_LIMIT, PAGE_LIMIT)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    Log.d("MineActivitesPresenter", "----" + s.toString());
                    JSONObject jsonObject = new JSONObject(s);
                    String result = jsonObject.getString("result");
                    MineActivitesModel mineActivitesModel = new Gson().fromJson(result, new TypeToken<MineActivitesModel>() {
                    }.getType());
//                    if (mineActivitesModel != null && !CollectionUtils.isEmpty()) {
//                        getView().requestDataSuccess();
//                    }
                    if (isRef) {
                        adapter.refrushData(mineActivitesModel.getRows(), isRef);
                    } else {
                        if (!CollectionUtils.isEmpty(mineActivitesModel.getRows())) {
                            adapter.refrushData(mineActivitesModel.getRows(), false);
                            System.out.println("-----MineActivitesPresenter---length=" + mineActivitesModel.getRows().size());
                        }
                    }
                    getView().requestDataSuccess(isRef);
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
