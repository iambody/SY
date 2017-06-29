package app.privatefund.investor.health.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.privatefund.investor.health.mvp.contract.HealthContract;

/**
 * @author chenlong
 * 健康业务实现
 */
public class HealthPresenter extends BasePresenterImpl<HealthContract.View> implements HealthContract.Presenter {


    public HealthPresenter(@NonNull Context context, @NonNull HealthContract.View view) {
        super(context, view);
    }

    @Override
    public void getHealthList() {

    }

    @Override
    public void loadNextPage() {

    }
}
