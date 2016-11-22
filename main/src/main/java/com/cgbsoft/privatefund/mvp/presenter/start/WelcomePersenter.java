package com.cgbsoft.privatefund.mvp.presenter.start;

import android.content.Context;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.view.start.WelcomeView;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 欢迎页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/16 09:04
 * Email:zhangxyfs@126.com
 *  
 */
public class WelcomePersenter extends BasePresenter<WelcomeView> implements RxConstant {
    private Observable<Boolean> welcomeFinishObservable;

    public WelcomePersenter(WelcomeView view) {
        super(view);
    }

    public void getData() {
        addSubscription(ApiClient.getAppResources().subscribe(new RxSubscriber<AppResourcesEntity.Result>() {
            @Override
            protected void onEvent(AppResourcesEntity.Result appResources) {
                getView().getDataSucc(appResources);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getDataError(error);
            }
        }));
    }

    public void createFinishObservable() {
        welcomeFinishObservable = RxBus.get().register(WELCOME_FINISH_OBSERVABLE, Boolean.class);
        welcomeFinishObservable.observeOn(AndroidSchedulers.mainThread())
                .filter(b -> b).subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                getView().finishThis();
            }

            @Override
            protected void onRxError(Throwable e) {

            }
        });
    }

    /**
     * 初始化一些信息
     *
     * @param context
     */
    public void toInitInfo(Context context) {
        addSubscription(ApiClient.getIP().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String json) {
                if (json.contains("var returnCitySN")) {
                    String substring = json.substring(19, json.length() - 1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(substring);
                        String cip = jsonObject.getString("cip");
                        String cname = jsonObject.getString("cname");
                        OtherDataProvider.saveCity(context.getApplicationContext(), cname);
                        OtherDataProvider.saveIP(context.getApplicationContext(), cip);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        }));

    }

}
