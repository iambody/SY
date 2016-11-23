package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.view.login.ForgetPasswordView;

/**
 * Created by xiaoyu.zhang on 2016/11/18 14:51
 * Email:zhangxyfs@126.com
 *  
 */
public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {
    private Context context;

    public ForgetPasswordPresenter(Context context, ForgetPasswordView view) {
        super(view);
        this.context = context;
    }

    public void sendCode(@NonNull LoadingDialog loadingDialog, String un) {
        loadingDialog.setLoading(context.getString(R.string.sending_str));
        loadingDialog.show();
        addSubscription(ApiClient.sendCode(un, 0).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.setResult(true, context.getString(R.string.sending_succ_str), 1000, () -> getView().sendSucc());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, error.getMessage(), 1000);
            }
        }));
    }

    public void checkCode(@NonNull LoadingDialog loadingDialog, String un, String code) {
        loadingDialog.setLoading(context.getString(R.string.checking_str));
        loadingDialog.show();
        addSubscription(ApiClient.checkCode(un, code).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.dismiss();
                getView().checkSucc();
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, error.getMessage(), 1000);
            }
        }));
    }

    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
