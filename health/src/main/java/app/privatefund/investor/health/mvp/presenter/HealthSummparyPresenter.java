package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import app.privatefund.investor.health.mvp.contract.HealthSummaryListContract;
import app.privatefund.investor.health.mvp.model.HealthProjectListEntity;

/**
 * @author chenlong
 * 健康业务实现
 */
public class HealthSummparyPresenter extends BasePresenterImpl<HealthSummaryListContract.View> implements HealthSummaryListContract.Presenter {

    private final static int PAGE_LIMIT = 20;
    private static final String PRE_HEALTH_MODEL = "healthModel.html";

    public HealthSummparyPresenter(@NonNull Context context, @NonNull HealthSummaryListContract.View view) {
        super(context, view);
    }

    @Override
    public void getHealthList(String offset) {
        getView().showLoadDialog();
        addSubscription(ApiClient.getHealthProjectList(ApiBusParam.getHealthSummaryDataParams(Integer.parseInt(offset), PAGE_LIMIT)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.d("HealthSummparyPresenter", "----"+ s.toString());
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String vas = jsonObject.getString("result");
                    HealthProjectListEntity Result = new Gson().fromJson(vas, new TypeToken<HealthProjectListEntity>() {}.getType());
                    getView().requestDataSuccess(Result);
                    getView().hideLoadDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().hideLoadDialog();
                    getView().requestDataFailure(e.getMessage());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestDataFailure(error.getMessage());
                getView().hideLoadDialog();
            }
        }));
    }

    /**
     * 是否有预留的健康模型页面
     * @return
     */
    public String getLocalHealthModelPath() {
        File resourceDir = BaseApplication.getContext().getDir(Constant.HEALTH_ZIP_DIR, Context.MODE_PRIVATE);
        return FileUtils.isExsitFileInFileDir(resourceDir.getPath(), PRE_HEALTH_MODEL);
    }
}
