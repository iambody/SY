package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeContract {
    public interface View extends BaseView {
        public void getResultSucc(HomeEntity.Result data);

        public void getResultError(String error);


        public void getCacheResult(HomeEntity.Result cachesData);

        public void getUseriInfsucc(int Type);

        public void getPublicFundResult(PublishFundRecommendBean publishFundRecommendBean);

        public void getPublicFundError(String error);
    }

    public interface Presenter extends BasePresenter {
        public void getHomeData();

        public void getHomeCache();

        public void getUserInf(int type);

        public void getPublicFundRecommend();



    }
}
