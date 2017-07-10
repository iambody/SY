package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.ChangePsdContract;
import com.cgbsoft.privatefund.mvp.presenter.center.ChangePsdPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunfei on 2017/6/29 0029.
 */
@Route(RouteConfig.GOTO_CHANGE_PSD_ACTIVITY)
public class ChangeLoginPsdActivity extends BaseActivity<ChangePsdPresenterImpl> implements ChangePsdContract.ChangePsdView {
    @BindView(R.id.title_left)
    protected ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @Override
    protected int layoutID() {
        return R.layout.activity_changelogin_psd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.setting_item_change_pwd));
    }

    @Override
    protected ChangePsdPresenterImpl createPresenter() {
        return null;
    }
    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
    }
}
