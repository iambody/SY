package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import app.privatefund.investor.health.adapter.CheckHealthAdapter;
import app.privatefund.investor.health.mvp.model.HealthIntroduceModel;


/**
 * @author chenlong
 *
 */
public interface HealthIntroduceContract {

    interface Presenter extends BasePresenter {

        void introduceHealth();
    }

    interface View extends BaseView {

        void requestDataSuccess(HealthIntroduceModel healthIntroduceModel);

        void requestDataFailure(String errorMsg);
    }
}
