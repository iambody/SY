package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.google.gson.Gson;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.SetPasswordContract;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by xiaoyu.zhang on 2016/11/23 15:50
 * Email:zhangxyfs@126.com
 *  
 */
public class SetPasswordPresenter extends BasePresenterImpl<SetPasswordContract.View> implements SetPasswordContract.Presenter {

    public SetPasswordPresenter(Context context, SetPasswordContract.View view) {
        super(context, view);
    }

    @Override
    public void resetPwd(final LoadingDialog loadingDialog, String un, String pwd, String code) {
        loadingDialog.setLoading(getContext().getString(R.string.reseting_str));
        loadingDialog.show();
        addSubscription(ApiClient.resetTestPwd(un, MD5Utils.getShortMD5(pwd), code).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
//                loadingDialog.setResult(true, "重置成功", 1000, () -> toNormalLogin(loadingDialog, un, pwd, false));
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(true, "重置失败", 1000);
            }
        }));
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    @Override
    public void toNormalLogin(@NonNull final LoadingDialog loadingDialog,final String un, String pwd, boolean isWx) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result loginBean) {
                SPreference.saveUserId(getContext().getApplicationContext(), loginBean.userId);
                SPreference.saveToken(getContext().getApplicationContext(), loginBean.token);

                SPreference.saveLoginFlag(getContext(), true);
                if (loginBean.userInfo != null) {
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                    SPreference.saveLoginName(getContext(), un);
                }
//                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> {
//                    getContext().startActivity(new Intent(getContext(), MainPageActivity.class));
//                    getView().toFinish();
//                });
            }

            @Override
            protected void onRxError(Throwable error) {
//                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000);
            }
        }));
    }


    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
