package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

import app.privatefund.investor.health.mvp.model.HealthListModel;

/**
 * @author chenlong
 *
 */
public interface HealthSummaryListContract {

    interface Presenter extends BasePresenter {

        void getHealthList(String offset);
    }

    interface View extends BaseView {

        void requestDataSuccess(List<HealthListModel> healthListModels);

        void requestDataFailure(String errMsg);
    }
}
