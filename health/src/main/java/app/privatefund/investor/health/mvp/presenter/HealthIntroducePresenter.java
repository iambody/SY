package app.privatefund.investor.health.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.net.ApiBusParam;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.ExtendWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
        String fileName = "";
        System.out.println("-------down url=" + url);
        String subUrl;
        String params = "";
        if (!TextUtils.isEmpty(url)) {
            if (url.lastIndexOf("/") > 0 && url.lastIndexOf("/") < url.length()) {
                fileName = url.substring(url.lastIndexOf("/") + 1);
            } else {
                fileName = url;
            }

            subUrl = fileName;
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("?"));
                params = subUrl.substring(subUrl.lastIndexOf("?") + 1);
            }

            System.out.println("------down fileName=" + fileName);
            if (!TextUtils.isEmpty(fileName)) {
                File resourceDir = BaseApplication.getContext().getDir(Constant.HEALTH_ZIP_DIR, Context.MODE_PRIVATE);
                String findPath = FileUtils.isExsitFileInFileDir(resourceDir.getPath(), fileName);
                System.out.println("------down find findPath=" + findPath);
                if (!TextUtils.isEmpty(findPath) && new File(findPath).exists()) {
                    webview.loadUrl("file://".concat(findPath).concat(TextUtils.isEmpty(params) ? "" : "?" + params));
                    return;
                }
            }
            if (url.startsWith("http")) {
                webview.loadUrl(url);
            } else {
                webview.loadUrl(BaseWebNetConfig.baseParentUrl.concat(url.startsWith("/") ? url.substring(1) : url));
            }
        }

//        String testParas = "";
//        if (url.contains("?")) {
//            testParas = url.substring(url.lastIndexOf("?") + 1);
//        }
//
//        if (url.contains("feedbackL1")) {
//            webview.loadUrls("file:///android_asset/health/health/feedbackL1.html".concat(!TextUtils.isEmpty(testParas) ? "?" + testParas : ""));
//        } else if (url.contains("healthConsultation2.html")) {
//            webview.loadUrls("file:///android_asset/health/health/healthConsultation2.html".concat(!TextUtils.isEmpty(testParas) ? "?" + testParas : ""));
//        } else if (url.contains("introduceL1.html")) {
//            webview.loadUrls("file:///android_asset/health/health/introduceL1.html".concat(!TextUtils.isEmpty(testParas) ? "?" + testParas : ""));
//        }
    }
}
