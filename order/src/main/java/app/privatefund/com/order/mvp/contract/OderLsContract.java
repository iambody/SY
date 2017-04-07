package app.privatefund.com.order.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  订单的列表 此处用fragment
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-11:27
 */
public interface OderLsContract {
    interface  Presenter extends BasePresenter{

    }

    interface View extends BaseView {
        void loginSuccess();

        void loginFail();


    }
}
