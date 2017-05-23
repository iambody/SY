package app.mall.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.mall.com.mvp.contract.PayResultContract;

/**
 * desc
 * Created by yangzonghui on 2017/5/23 19:08
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayResultPresenter extends BasePresenterImpl<PayResultContract.View> implements PayResultContract.Presenter {
    public PayResultPresenter(@NonNull Context context, @NonNull PayResultContract.View view) {
        super(context, view);
    }

}
