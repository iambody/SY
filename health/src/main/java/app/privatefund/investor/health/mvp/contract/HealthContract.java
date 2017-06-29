package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import app.privatefund.investor.health.adapter.CheckHealthAdapter;


/**
 * @author chenlong
 *
 */
public interface HealthContract {

    interface Presenter extends BasePresenter {

        void getHealthList(CheckHealthAdapter adapter, boolean isRef);
    }

    interface View extends BaseView {

        void requestDataSuccess( boolean isRef);

        void requestDataFailure(boolean isRef);
    }
}
