package com.cgbsoft.privatefund.mvp.presenter;

import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.view.WelcomeView;

/**
 * 欢迎页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/16 09:04
 * Email:zhangxyfs@126.com
 *  
 */
public class WelcomePersenter extends BasePresenter<WelcomeView> {

    public WelcomePersenter(WelcomeView view) {
        super(view);
    }

    public void getData() {
        addSubscription(ApiClient.getAppResources(), new RxSubscriber<AppResources>() {
            @Override
            protected void onEvent(AppResources appResources) {
                getView().getDataSucc(appResources);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getDataError(error);
            }
        });
    }
}
