package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import org.json.JSONObject;

/**
 * Created by xiaoyu.zhang on 2016/11/28 10:27
 * Email:zhangxyfs@126.com
 *  
 */
public interface MainPageContract {

    interface Presenter extends BasePresenter {
        void getProLiveList();

        void initDayTask();

        void getUserInfo(boolean tofreshHome);

        void loadRedPacket();

        void loadPublicFundInf();


    }

    interface View extends BaseView {
        //判断是否有直播

        /**
         * @param liveState  0-->预告  1-->直播中  2-->无直播
         * @param jsonObject
         */
        void hasLive(int liveState, JSONObject jsonObject);

        void signInSuc();

        void toFreshUserinfHome();

        void loadSoSuccess(String filePath);

        void loadSoError();

    }
}
