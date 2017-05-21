package app.mall.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.mall.com.mvp.contract.PayContract;
import app.mall.com.mvp.contract.PayMethodContract;

/**
 * desc
 * Created by yangzonghui on 2017/5/21 17:28
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayMethodPresenter extends BasePresenterImpl<PayMethodContract.View> implements PayMethodContract.Presenter{
    public PayMethodPresenter(@NonNull Context context, @NonNull PayMethodContract.View view) {
        super(context, view);
    }
}
