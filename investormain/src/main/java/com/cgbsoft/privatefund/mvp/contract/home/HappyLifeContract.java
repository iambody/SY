package com.cgbsoft.privatefund.mvp.contract.home;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * Created by sunfei on 2017/6/28 0028.
 *
 * 提取view模块和presenter模块暴露给对方的方法
 */

public interface HappyLifeContract {
    public interface HappyLifeView extends BaseView{}
    public interface HappyLifePresenter extends BasePresenter{}
}
