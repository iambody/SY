package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import butterknife.BindView;

/**
 * @author chenlong
 */
public class IntroduceHealthFragmentTest extends BaseFragment{

    @BindView(R2.id.webview)
    protected BaseWebview baseWebview;

    @Override
    protected int layoutID() {
        return R.layout.health_troduce_test;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }


    @Override
    protected void loadData() {
        baseWebview.loadUrl("file:///android_asset/health/healthConsultation2.html?params=chenlong");
    }


}
