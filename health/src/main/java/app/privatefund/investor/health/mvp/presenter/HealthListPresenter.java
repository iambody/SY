package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.model.HealthEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.adapter.CheckHealthAdapter;
import app.privatefund.investor.health.mvp.contract.HealthListContract;
import app.privatefund.investor.health.mvp.model.HealthListModel;

/**
 * @author chenlong
 * 健康业务实现
 */
public class HealthListPresenter extends BasePresenterImpl<HealthListContract.View> implements HealthListContract.Presenter {

    private int index = 0;
    private final static int PAGE_LIMIT = 20;
    private final static String CHECK_HEALTH_PARAMS = "4002";
    private final static String CHECK_MEDICAL_PARAMS = "4003";
    private boolean isCheckHealth;

    public HealthListPresenter(@NonNull Context context, @NonNull HealthListContract.View view, boolean isCheckHealth) {
        super(context, view);
        this.isCheckHealth = isCheckHealth;
    }

    @Override
    public void getHealthList(CheckHealthAdapter adapter, boolean isRef) {
        if (isRef) {
            index = 0;
        } else {
            index += 1;
        }
        System.out.println("-----HealthListPresenter--index="+ index);
        addSubscription(ApiClient.getHealthDataList(ApiBusParam.getHealthDataParams(isCheckHealth ? CHECK_HEALTH_PARAMS : CHECK_MEDICAL_PARAMS, index * PAGE_LIMIT, PAGE_LIMIT)).subscribe(new RxSubscriber<String>() {
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
                    if (isRef) {
                        adapter.deleteAllData();
                        adapter.refAllData(list);
                        System.out.println("-----isRef-length=" + adapter.getList().size());
                    } else {
                        if (!CollectionUtils.isEmpty(list)) {
                            adapter.appendToList(list);
                            System.out.println("-----append-length=" + adapter.getList().size());
                        }
                    }
                    getView().requestDataSuccess(isRef);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestDataFailure(isRef);
            }
        }));
    }
}
