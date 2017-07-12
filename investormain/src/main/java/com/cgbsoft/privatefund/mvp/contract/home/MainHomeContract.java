package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeContract {
    public interface View extends BaseView {
        public void getResultSucc(HomeEntity.Result data);

        public void getResultError(String error);

        public void getSignResult(String notemessage);
    }

    public interface Presenter extends BasePresenter {
        public void getHomeData();


        public void todoSign();
    }
}
