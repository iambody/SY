package com.cgbsoft.privatefund.mvp.view;

import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * 欢迎页功能接口
 * Created by xiaoyu.zhang on 2016/11/16 09:03
 * Email:zhangxyfs@126.com
 *  
 */
public interface WelcomeView extends BaseView {
    void getDataSucc(AppResources result);
    void getDataError(Throwable error);
}
