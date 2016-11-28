package com.cgbsoft.privatefund.mvp.presenter.start;

import android.content.Context;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.start.WelcomeContract;
import com.google.gson.Gson;

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
public class WelcomePersenter extends BasePresenterImpl<WelcomeContract.View> implements WelcomeContract.Persenter {
    private Observable<Boolean> welcomeFinishObservable;
    private DaoUtils daoUtils;

    public WelcomePersenter(Context context, WelcomeContract.View view) {
        super(view);
        daoUtils = new DaoUtils(context, DaoUtils.W_OTHER);
    }

    /**
     * 获取首页背景图片，应用升级信息。
     */
    public void getData() {
        addSubscription(ApiClient.getAppResources().subscribe(new RxSubscriber<AppResourcesEntity.Result>() {
            @Override
            protected void onEvent(AppResourcesEntity.Result appResources) {
                if (appResources != null) {
                    daoUtils.saveOrUpdataOther(DBConstant.APP_UPDATE_INFO, new Gson().toJson(appResources));
                }

                getView().getDataSucc(appResources);
            }

            @Override
            protected void onRxError(Throwable error) {
                OtherInfo otherInfo = daoUtils.getOtherInfo(DBConstant.APP_UPDATE_INFO);
                if (otherInfo != null) {
                    getView().getDataSucc(new Gson().fromJson(otherInfo.getContent(), AppResourcesEntity.Result.class));
                } else {
                    getView().getDataError(error);
                    //todo test
                    AppResourcesEntity.Result result = new AppResourcesEntity.Result();
                    result.img916 = "https://upload.simuyun.com/live/b0926657-6b81-4599-b2ed-de32ecd396c2.jpg";
                    result.version = "5.1.0";
                    result.adverts = "5.1.0更新内容：\n【新增】意见反馈\n【新增】会话页面可直接发送产品\n【新增】PDF分享给联系人\n【新增】资讯分享给联系人";
                    result.downUrl = "https://upload.simuyun.com/android/2d0b8355-4f83-46a9-b8f3-aa1758abee14.apk";
                    result.isMustUpdate = "n";
                    daoUtils.saveOrUpdataOther(DBConstant.APP_UPDATE_INFO, new Gson().toJson(result));
                }
            }
        }));
    }

    /**
     * 关闭当前页面
     */
    public void createFinishObservable() {
        welcomeFinishObservable = RxBus.get().register(WELCOME_FINISH_OBSERVABLE, Boolean.class);
        welcomeFinishObservable.observeOn(AndroidSchedulers.mainThread())
                .filter(b -> b).subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                RxBus.get().unregister(WELCOME_FINISH_OBSERVABLE, welcomeFinishObservable);
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

    @Override
    public void detachView() {
        super.detachView();
        daoUtils.destory();
        daoUtils = null;
    }
}
