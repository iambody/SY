package app.ndk.com.enter.mvp.contract;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

/**
 * @author chenlong
 *         email chenlong@simuyun.com
 */

public interface ModifyPasswordContract {

    interface Presenter extends BasePresenter {
        /**
         * 修改密码
         *
         * @param loadingDialog
         * @param oldPwd        旧密码
         * @param newPwd        新密码
         */
        void modifyPassword(@NonNull LoadingDialog loadingDialog, String userName, String oldPwd, String newPwd);
    }

    interface View extends BaseView {
        void regSucc();

        void regFail();
    }

}
