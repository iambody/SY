package app.ndk.com.enter.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/28 10:34
 *  Email:zhangxyfs@126.com
 *  
 */
public interface ChoiceIdentityContract {

    interface Presenter extends BasePresenter{
        void nextClick(int identity);

    }

    interface View extends BaseView{
        void finishThis();
    }
}
