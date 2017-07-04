package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * @author chenlong
 */
public interface HealthBespeakContract {

    interface Presenter extends BasePresenter {

        void commitHealthBespeak(String id, String name, String phone, String capta);

        void getValidateCode(String validateCode);
    }

    interface View extends BaseView {

        void bespeakSuccess();

        void bespeakFailure();

        void bespeakError(String error);
    }
}
