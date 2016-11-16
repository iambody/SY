package com.cgbsoft.privatefund.mvp.view;

import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

/**
 *  首页功能接口
 *  Created by xiaoyu.zhang on 2016/11/10 16:12
 *  
 */
public interface HomeView extends BaseView {
    <T> void getDataList(RxSubscriber<T> subscriber);
}
