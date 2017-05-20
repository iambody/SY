package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.google.gson.Gson;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.ModifyPasswordContract;

/**
 * @author chenlong
 * 修改密码的presenter
 */
public class ModifyPasswordPresenter extends BasePresenterImpl<ModifyPasswordContract.View> implements ModifyPasswordContract.Presenter {

    public ModifyPasswordPresenter(@NonNull Context context, @NonNull ModifyPasswordContract.View view) {
        super(context, view);
    }

    @Override
    public void modifyPassword(@NonNull LoadingDialog loadingDialog, String userName, String oldPwd, String newPwd) {
        loadingDialog.setLoading(getContext().getString(R.string.ra_register_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toTestModifyPassword(userName, MD5Utils.getShortMD5(oldPwd), MD5Utils.getShortMD5(newPwd)).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                loadingDialog.setResult(true, getContext().getString(R.string.modify_password_success_str), 1000, () -> getView().regSucc());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.modify_password_failure_str), 1000, () -> getView().regFail());
            }
        }));
    }
}
