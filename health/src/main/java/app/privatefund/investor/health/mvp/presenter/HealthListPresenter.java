package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.model.HealthEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

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

    public HealthListPresenter(@NonNull Context context, @NonNull HealthListContract.View view) {
        super(context, view);
    }

    @Override
    public void getHealthList(CheckHealthAdapter adapter, boolean isRef) {
        if (isRef) {
            index = 0;
        } else {
            index++;
        }
        addSubscription(ApiClient.getHealthDataList(ApiBusParam.getHealthDataParams(CHECK_HEALTH_PARAMS, index, PAGE_LIMIT)).subscribe(new RxSubscriber<HealthEntity.Result>() {
            @Override
            protected void onEvent(HealthEntity.Result s) {
//                List<HealthEntity.Row> rows = new Gson().fromJson(s, new TypeToken<List<HealthEntity.Row>>() {}.getType());
                Log.d("HealthPresenter", "--"+ s.toString());
                List<HealthEntity.Row> rows = s.getRows();
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
                } else {
                    adapter.appendToList(list);
                }
                getView().requestDataSuccess(isRef);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestDataFailure(isRef);
            }
        }));
    }
}
