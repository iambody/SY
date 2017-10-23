package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.webview.BaseWebview;
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
import java.util.HashMap;
import java.util.List;

import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceNavigationEntity;

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
//        getView().showLoadDialog();
//        addSubscription(ApiClient.getHealthIntruduce(new HashMap()).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                getView().hideLoadDialog();
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String vas = jsonObject.getString("result");
//                    HealthIntroduceModel result = new Gson().fromJson(vas, new TypeToken<HealthIntroduceModel>() {
//                    }.getType());
//                    getView().requestDataSuccess(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                getView().hideLoadDialog();
//                getView().requestDataFailure(error.getMessage());
//            }
//        }));
    }

    @Override
    public void introduceNavigation(String code) {
        getView().showLoadDialog();
        addSubscription(ApiClient.getNavigationThird(ApiBusParam.getNavigationThird(code)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().hideLoadDialog();
                System.out.println("-------HealthIntroducePresenter=" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String vas = jsonObject.getString("result");
                    List<HealthIntroduceNavigationEntity> resultList = new Gson().fromJson(vas, new TypeToken<List<HealthIntroduceNavigationEntity>>() {}.getType());
                    getView().requestNavigationSuccess(resultList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().hideLoadDialog();
                    getView().requestNavigationFailure("数据解析错误");
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().hideLoadDialog();
                getView().requestNavigationFailure(error.getMessage());
            }
        }));
    }

    @Override
    public void initNavigationContent(BaseWebview webview, HealthIntroduceNavigationEntity healthIntroduceNavigationEntity) {
        String url = healthIntroduceNavigationEntity.getUrl();
        String id = healthIntroduceNavigationEntity.getCode();
        String fileName = "";
        System.out.println("-------down url=" + url);
        if (!TextUtils.isEmpty(url)) {
            if (url.lastIndexOf("/") > 0 && url.lastIndexOf("/") < url.length()) {
                fileName = url.substring(url.lastIndexOf("/") + 1);
            } else {
                fileName = url;
            }
            System.out.println("------down fileName=" + fileName);
            if (!TextUtils.isEmpty(fileName)) {
//                File resourceDir = FileUtils.getResourceLocalTempFile(Constant.HEALTH_ZIP_DIR, "");
                File resourceDir = BaseApplication.getContext().getDir(Constant.HEALTH_ZIP_DIR, Context.MODE_PRIVATE);
                String findPath = FileUtils.isExsitFileInFileDir(resourceDir.getPath(), fileName);
                System.out.println("------down find resourceDir=" + resourceDir);
                System.out.println("------down find findPath=" + findPath);
                if (findPath != null && new File(findPath).exists()) {
                    webview.loadUrls("content://".concat(findPath).concat("?id=").concat(id));
                    return;
                }
            }
            if (url.startsWith("http")) {
                webview.loadUrls(url.concat("?id=").concat(id));
            }
        }
    }
}
