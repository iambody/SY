package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.bean.commui.SignBean;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.google.gson.Gson;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:08
 */
public class MainHomePresenter extends BasePresenterImpl<MainHomeContract.View> implements MainHomeContract.Presenter {
    public MainHomePresenter(@NonNull Context context, @NonNull MainHomeContract.View view) {
        super(context, view);
    }

    @Override
    public void getHomeData() {
        addSubscription(ApiClient.getSxyHomeData().subscribe(new RxSubscriber<HomeEntity.Result>() {
            @Override
            protected void onEvent(HomeEntity.Result result) {
                getView().getResultSucc(result);
                LogUtils.Log("s", "s");
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getResultError(error.getMessage());
            }
        }));
//        addSubscription(ApiClient.getSxyHomeDataTest().subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                LogUtils.Log("s", s);
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                LogUtils.Log("s", error.getMessage());
//            }
//        }));

    }

    //进行签到动作
    @Override
    public void todoSign() {
        addSubscription(ApiClient.userSign().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (!BStrUtils.isEmpty(s)) {
                    SignBean signBean = new Gson().fromJson(getV2String(s), SignBean.class);//

                    getView().getSignResult(signBean.resultMessage);
                } else {
                    getView().getSignResult("签到失败");
                }

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }
}
