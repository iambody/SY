package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.google.gson.Gson;

/**
 * 首页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/10 16:18
 *  
 */
public class MainPagePresenter extends BasePresenterImpl<MainPageContract.View> implements MainPageContract.Presenter {

    public MainPagePresenter(Context context, MainPageContract.View view) {
        super(context, view);
    }


    public void getDataList() {

    }


    @Override
    public void getRongToken() {
        String rongExpired = OtherDataProvider.getRongTokenExpired(Appli.getContext());
        String rongUID = OtherDataProvider.getRongUid(Appli.getContext());
        String rongToken = OtherDataProvider.getRongToken(Appli.getContext());

        String userId = SPreference.getUserId(Appli.getContext());

        if (!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", rongExpired)) {
            OtherDataProvider.saveRongUid(Appli.getContext(), userId);
            OtherDataProvider.saveRongExpired(Appli.getContext(), "2");

            String needExpired = TextUtils.equals(rongExpired, "1") ? "1" : null;
            ApiClient.getTestRongToken(needExpired, userId).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    RongTokenEntity.Result result = new Gson().fromJson(s, RongTokenEntity.Result.class);
                    OtherDataProvider.saveRongToken(Appli.getContext(), result.rcToken);
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        } else {
            if (SPreference.getUserInfoData(Appli.getContext()) != null) {

            }
        }
    }

    //todo 测试用
    public void toSignIn(){
        String uid = SPreference.getUserId(Appli.getContext());
        ApiClient.testSignIn(uid).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                SignInEntity.Result result = new Gson().fromJson(s, SignInEntity.Result.class);

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
