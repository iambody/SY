package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.IOSDialog;
import com.cgbsoft.lib.widget.LoadingDialog;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.login.BindPhoneContract;

/**
 * Created by xiaoyu.zhang on 2016/11/29 14:23
 * Email:zhangxyfs@126.com
 *  
 */
public class BindPhonePresenter extends BasePresenterImpl<BindPhoneContract.View> implements BindPhoneContract.Presenter {

    public BindPhonePresenter(@NonNull Context context, @NonNull BindPhoneContract.View view) {
        super(context, view);
    }

    @Override
    public void sendCode(@NonNull LoadingDialog loadingDialog, String un) {
        loadingDialog.setLoading(getContext().getString(R.string.sending_str));
        loadingDialog.show();
        addSubscription(ApiClient.sendCode(un, 3).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.setResult(true, getContext().getString(R.string.sending_succ_str), 1000, () -> getView().sendSucc());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, error.getMessage(), 1000);
            }
        }));
    }

    @Override
    public void wxMergePhone(@NonNull LoadingDialog loadingDialog, String un, String code) {
        loadingDialog.setLoading(getContext().getString(R.string.bind_phone_str));
        loadingDialog.show();
        addSubscription(ApiClient.wxMergePhone(un, code).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (TextUtils.equals(s, "1")) {//之前没有手机号账号，不需要合并数据。
                    loadingDialog.setResult(true, getContext().getString(R.string.bing_phone_succ_str), 1000, () -> getView().margeSucc());
                } else if (TextUtils.equals(s, "2")) {//有手机号账号，需要对合并数据进行确认
                    String vas = String.format(getContext().getResources().getString(R.string.account_merge_str), un);
                    loadingDialog.dismiss();
                    new IOSDialog(getContext(), getContext().getString(R.string.bpna_marge_str), vas,
                            getContext().getString(R.string.bpna_marge_no_str), getContext().getString(R.string.bpna_marge_yes_str)) {
                        @Override
                        public void left() {
                            this.dismiss();
                            getView().margeSucc();
                        }

                        @Override
                        public void right() {
                            this.dismiss();
                            loadingDialog.setLoading(getContext().getString(R.string.bpna_start_marge_str));
                            loadingDialog.show();
                            wxMergeConfirm(loadingDialog);
                        }
                    };
                } else if (TextUtils.equals(s, "3")) {//绑定中
                    loadingDialog.dismiss();
                    showToast(R.string.bind_phone_not_repeat_str);
                } else {
                    loadingDialog.setResult(false, getContext().getString(R.string.bing_phone_fail_str), 1000);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                MToast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void wxMergeConfirm(@NonNull LoadingDialog loadingDialog) {
        addSubscription(ApiClient.wxMergeConfirm().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (TextUtils.equals(s, "suc")) {
                    loadingDialog.setResult(true, "合并成功", 1000, () -> getView().margeSucc());
                } else {
                    loadingDialog.setResult(false, "合并手机号错误", 1000);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                MToast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
