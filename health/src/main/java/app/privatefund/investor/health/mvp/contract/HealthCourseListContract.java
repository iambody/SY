package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

import app.privatefund.investor.health.mvp.model.HealthCourseEntity;

/**
 * @author chenlong
 *
 */
public interface HealthCourseListContract {

    interface Presenter extends BasePresenter {

        void getHealthCourseList(String offset);
    }

    interface View extends BaseView {

        void requestDataSuccess(List<HealthCourseEntity.HealthCourseListModel> healthCourseListModels);

        void requestDataFailure(String errMsg);
    }
}
