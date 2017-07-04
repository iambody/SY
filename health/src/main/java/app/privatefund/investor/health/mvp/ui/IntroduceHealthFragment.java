package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import butterknife.BindView;

/**
 *  @author chenlong
 */
public class IntroduceHealthFragment extends BaseFragment {

    @BindView(R2.id.webview)
    BaseWebview baseWebview;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        baseWebview.loadUrls(CwebNetConfig.clubPage);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
