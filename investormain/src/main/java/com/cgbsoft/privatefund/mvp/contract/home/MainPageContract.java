package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import org.json.JSONObject;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/28 10:27
 *  Email:zhangxyfs@126.com
 *  
 */
public interface MainPageContract {

    interface Presenter extends BasePresenter{
        void getLiveList();

        void initDayTask();

        void getUserInfo(boolean tofreshHome);
    }

    interface View extends BaseView{
        //判断是否有直播
        void hasLive(boolean hasLive, JSONObject jsonObject);

        void signInSuc();

        void toFreshUserinfHome();

    }
}
