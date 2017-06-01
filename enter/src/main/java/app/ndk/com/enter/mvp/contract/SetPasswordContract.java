package app.ndk.com.enter.mvp.contract;

import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/28 12:06
 *  Email:zhangxyfs@126.com
 *  
 */
public interface SetPasswordContract {

    interface Presenter extends BasePresenter{
        /**
         * 重置密码
         * @param loadingDialog
         * @param un
         * @param pwd
         * @param code
         */
        void resetPwd(LoadingDialog loadingDialog, String un, String pwd, String code, boolean isGesture);

        /**
         * 登录
         * @param loadingDialog
         * @param un
         * @param pwd
         * @param isWx
         */
        void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx);

    }

    interface View extends BaseView{
        void toFinish();

        void setGesturePassword();
    }
}
