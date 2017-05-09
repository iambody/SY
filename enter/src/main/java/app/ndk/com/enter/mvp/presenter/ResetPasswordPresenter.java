package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.LoadingDialog;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.ResetPasswordContract;


/**
 * Created by xiaoyu.zhang on 2016/11/18 14:51
 * Email:zhangxyfs@126.com
 *  
 */
public class ResetPasswordPresenter extends BasePresenterImpl<ResetPasswordContract.View> implements ResetPasswordContract.Presenter {
private  Context context;
    public ResetPasswordPresenter(Context context, ResetPasswordContract.View view) {
        super(context, view);
        this.context=context;
    }

    @Override
    public void sendCode(@NonNull LoadingDialog loadingDialog, String un) {
        loadingDialog.setLoading(context.getString(R.string.sending_str));
        loadingDialog.show();
        addSubscription(ApiClient.sendTestCode(un, 0).subscribe(new RxSubscriber<String>() {
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

    @Override
    public void checkCode(@NonNull LoadingDialog loadingDialog, String un, String code) {
        loadingDialog.setLoading(getContext().getString(R.string.checking_str));
        loadingDialog.show();
        addSubscription(ApiClient.checkTestCode(un, code).subscribe(new RxSubscriber<String>() {
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
}
