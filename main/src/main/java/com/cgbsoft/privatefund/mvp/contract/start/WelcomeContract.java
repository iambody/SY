package com.cgbsoft.privatefund.mvp.contract.start;

import android.content.Context;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * 用于控制 view 和 presenter接口
 * Created by xiaoyu.zhang on 2016/11/28 10:09
 * Email:zhangxyfs@126.com
 *  
 */
public interface WelcomeContract {

    interface Persenter extends BasePresenter{
        void getData();

        void createFinishObservable();

        void toInitInfo(Context context);
    }

    interface View extends BaseView {
        void getDataSucc(AppResourcesEntity.Result result);
        void getDataError(Throwable error);

        void finishThis();
    }
}
