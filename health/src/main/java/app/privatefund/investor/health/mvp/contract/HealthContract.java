package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import app.privatefund.investor.health.mvp.model.HealthEntity;


/**
 * @author chenlong
 *
 */
public interface HealthContract {

    interface Presenter extends BasePresenter {
        void getHealthList();

        void loadNextPage();
    }

    interface View extends BaseView {

        void requestDataSuccess(HealthEntity.Result healthData);

        void requestDataFailure(String errorMsg);
    }
}
