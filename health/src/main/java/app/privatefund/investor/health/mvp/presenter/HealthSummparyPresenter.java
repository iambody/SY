package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.model.HealthEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.mvp.contract.HealthSummaryListContract;
import app.privatefund.investor.health.mvp.model.HealthListModel;

/**
 * @author chenlong
 * 健康业务实现
 */
public class HealthSummparyPresenter extends BasePresenterImpl<HealthSummaryListContract.View> implements HealthSummaryListContract.Presenter {

    private final static int PAGE_LIMIT = 20;

    public HealthSummparyPresenter(@NonNull Context context, @NonNull HealthSummaryListContract.View view) {
        super(context, view);
    }

    @Override
    public void getHealthList(String offset) {
        System.out.println("-----HealthListPresenter--offset="+ offset);
        addSubscription(ApiClient.getHealthDataList(ApiBusParam.getHealthSummaryDataParams(Integer.parseInt(offset), PAGE_LIMIT)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("HealthListPresenter", "----"+ s.toString());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String vas = jsonObject.getString("result");
                    HealthEntity.Result Result = new Gson().fromJson(vas, new TypeToken<HealthEntity.Result>() {}.getType());
                    List<HealthEntity.Row> rows = Result.getRows();
                    List<HealthListModel> list = new ArrayList<>();
                    for (int i = 0; i < rows.size(); i++) {
                        HealthListModel model = new HealthListModel();
                        model.type = HealthListModel.BOTTOM;
                        model.setCode(rows.get(i).getCode());
                        model.setId(rows.get(i).getId());
                        model.setImageUrl(rows.get(i).getImageUrl());
                        model.setTitle(rows.get(i).getTitle());
                        model.setUrl(rows.get(i).getUrl());
                        list.add(model);
                    }
                    getView().requestDataSuccess(list);
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
