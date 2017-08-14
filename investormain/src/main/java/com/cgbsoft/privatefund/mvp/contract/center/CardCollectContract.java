package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by fei on 2017/8/10.
 */

public interface CardCollectContract {
    interface CardCollectView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();
        void getCardListSuccess(List<CardListEntity.CardBean> cardBeans);
        void getCardListError(Throwable error);
    }
    interface CardCollectPresenter extends BasePresenter{
        void getCardList(String indentityCode);
    }
}
