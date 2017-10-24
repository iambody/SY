package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import app.privatefund.investor.health.mvp.contract.HealthCourseListContract;
import app.privatefund.investor.health.mvp.model.HealthCourseEntity;

/**
 * @author chenlong
 * 健康课堂实现
 */
public class HealthCoursePresenter extends BasePresenterImpl<HealthCourseListContract.View> implements HealthCourseListContract.Presenter {

    private final static int PAGE_LIMIT = 20;
    private int total;

    public HealthCoursePresenter(@NonNull Context context, @NonNull HealthCourseListContract.View view) {
        super(context, view);
    }

    @Override
    public void getHealthCourseList(String offset) {
        getView().showLoadDialog();
        addSubscription(ApiClient.getHealthCourseDataList(ApiBusParam.getHealthCourseDataParams(Integer.parseInt(offset))).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("HealthCoursePresenter", "----"+ s.toString());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String stringValue = jsonObject.getString("result");
                    HealthCourseEntity.Result result = new Gson().fromJson(stringValue, new TypeToken<HealthCourseEntity.Result>() {}.getType());
                    getView().requestDataSuccess(result.getRows(), result.getTotal());
                    getView().hideLoadDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().hideLoadDialog();
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
