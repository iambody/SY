package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by sunfei on 2017/7/13 0013.
 */

public interface OldSalonsContract {
    interface OldSalonsView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void getDataSuccess(List<OldSalonsEntity.SalonItemBean> oldSalons);
        void getDataError(Throwable error);
    }
    interface OldSalonsPresenter extends BasePresenter {
        void getOldSalons(int offset, int limit);
    }
}
