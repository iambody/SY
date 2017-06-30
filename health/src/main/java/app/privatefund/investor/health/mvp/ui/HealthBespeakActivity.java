package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;

import app.privatefund.investor.health.mvp.contract.HealthBespeakContract;
import app.privatefund.investor.health.mvp.presenter.HealthBespeakPresenter;

/**
 * @author chenlong
 */
public class HealthBespeakActivity extends BaseActivity<HealthBespeakPresenter> implements HealthBespeakContract.View {

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected HealthBespeakPresenter createPresenter() {
        return new HealthBespeakPresenter(this, this);
    }


    @Override
    public void bespeakSuccess() {

    }

    @Override
    public void bespeakFailure() {

    }
}
