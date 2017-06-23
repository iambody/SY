package app.privatefund.com.vido.mvp.contract.video;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/23-14:43
 */
public interface VideoListContract {
    interface Presenter extends BasePresenter {
        void getVideoList();
    }

    interface View extends BaseView {
        void getVideoDataSucc(String data);

        void getVideoDataError(String message);
    }
}
