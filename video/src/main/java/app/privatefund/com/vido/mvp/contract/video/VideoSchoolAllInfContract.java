package app.privatefund.com.vido.mvp.contract.video;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-20:58
 */
public class VideoSchoolAllInfContract {
    public interface Presenter extends BasePresenter {
        void getVideoSchoolAllInf( );
    }

    public interface View extends BaseView {
        void getSchoolAllDataSucc(String data);

        void getSchoolAllDataError(String message);
    }
}
