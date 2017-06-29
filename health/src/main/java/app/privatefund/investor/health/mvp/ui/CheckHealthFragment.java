package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.mvp.contract.HealthContract;
import app.privatefund.investor.health.mvp.model.HealthEntity;
import app.privatefund.investor.health.mvp.presenter.HealthPresenter;


/**
 * @author chenlong
 */
public class CheckHealthFragment extends BaseFragment<HealthPresenter> implements HealthContract.View {

    @Override
    protected int layoutID() {
        return R.layout.fragment_healthlist;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected HealthPresenter createPresenter() {
        return null;
    }


    @Override
    public void requestDataSuccess(HealthEntity.Result healthData) {

    }

    @Override
    public void requestDataFailure(String errorMsg) {

    }

}
