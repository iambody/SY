package app.privatefund.investor.health.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.base.webview.BaseWebview;

import java.util.List;

import app.privatefund.investor.health.mvp.model.HealthIntroduceNavigationEntity;

/**
 * @author chenlong
 *
 */
public interface HealthIntroduceContract {

    interface Presenter extends BasePresenter {

        void introduceHealth();

        void introduceNavigation(String code);

        void initNavigationContent(BaseWebview baseWebview, HealthIntroduceNavigationEntity healthIntroduceNavigationEntity);
    }

    interface View extends BaseView {
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

//        void requestDataSuccess(HealthIntroduceModel healthIntroduceModel);
//
//        void requestDataFailure(String errorMsg);

        void requestNavigationSuccess(List<HealthIntroduceNavigationEntity> list);

        void requestNavigationFailure(String errorMsg);
    }
}
