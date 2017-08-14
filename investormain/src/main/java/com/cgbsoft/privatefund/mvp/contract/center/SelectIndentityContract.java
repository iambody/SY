package com.cgbsoft.privatefund.mvp.contract.center;

import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by fei on 2017/8/11.
 */

public interface SelectIndentityContract {
    interface SelectIndentityView extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        /**
         *
         */
        void getIndentityListSuccess(List<IndentityEntity.IndentityBean> indentityBeen);
        void getIndentityListError(Throwable error);
    }
    interface SelectIndentityPresenter extends BasePresenter {
        void getIndentityList();
    }
}
