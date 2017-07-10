package app.mall.com.mvp.contract;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by sunfei on 2017/6/28 0028.
 *
 * 提取view模块和presenter模块暴露给对方的方法
 */

public interface ElegantLivingContract {
    interface ElegantLivingView extends BaseView{
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void updateUi(List<ElegantLivingEntity.ElegantLivingBean> data);

        void updateError();
    }
    interface ElegantLivingPresenter extends BasePresenter{
        /**
         * 获取生活家banner
         */
        void getElegantLivingBanners(int offset);
    }
}
