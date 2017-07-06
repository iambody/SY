package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.model.ElegantGoodsBeanInterface;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by sunfei on 2017/6/28 0028.
 *
 * 提取view模块和presenter模块暴露给对方的方法
 */

public interface ElegantGoodsContract {
    public interface ElegantGoodsView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void updateUi(List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categorys, List<ElegantGoodsBeanInterface> result);

        void updateUiMore(List<ElegantGoodsBeanInterface> allRows);

        void updateFirstError(Throwable error);

        void updateMoreError(Throwable error);
    }
    public interface ElegantGoodsPresenter extends BasePresenter{
        /**
         * 获取尚品首页数据
         */
        void getElegantGoodsFirst(int offset);
        /**
         * 获取尚品更多数据
         */
        void getElegantGoodsMore(int offset,String category);
    }
}
