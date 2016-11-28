package com.cgbsoft.privatefund.mvp.presenter.home;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;

/**
 * 首页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/10 16:18
 *  
 */
public class MainPagePresenter extends BasePresenterImpl<MainPageContract.View> implements MainPageContract.Presenter{

    public MainPagePresenter(MainPageContract.View view) {
        super(view);
    }


    public void getDataList() {

    }


    private void getRongToken() {
        String rongExpired = OtherDataProvider.getRongTokenExpired(Appli.getContext());
        String rongUID = OtherDataProvider.getRongUid(Appli.getContext());
        String rongToken = OtherDataProvider.getRongToken(Appli.getContext());

        String userId = SPreference.getUserId(Appli.getContext());

        if (!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", rongExpired)) {
            OtherDataProvider.saveRongUid(Appli.getContext(), userId);
            OtherDataProvider.saveRongExpired(Appli.getContext(), "2");

            String needExpired = TextUtils.equals(rongExpired, "1") ? "1" : null;
            ApiClient.getRongToken(needExpired, userId).subscribe(new RxSubscriber<RongTokenEntity.Result>() {
                @Override
                protected void onEvent(RongTokenEntity.Result result) {
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
}
