package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.privatefund.R;

import butterknife.BindView;
import rx.Observable;

/**
 * @author chenlong
 *         公募基金
 */
public class PublicFundFragment extends BaseFragment {

    @BindView(R.id.webview)
    BaseWebview baseWebview;
    Observable<Integer> publicFundInfObservable;

    @Override
    protected int layoutID() {
        return R.layout.fragment_public_fund;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        baseWebview.loadUrl(CwebNetConfig.publicFundHomeUrl);
        String webPagepostion = "javascript:swiperPosition()";
        baseWebview.loadUrl(webPagepostion);
        TrackingDataManger.tabPublicFundClick(baseActivity);

        if (null == publicFundInfObservable) {
            publicFundInfObservable = RxBus.get().register(RxConstant.REFRESH_PUBLIC_FUND_INFO, Integer.class);
            publicFundInfObservable.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer publicFundInf) {
                    if (10 == publicFundInf) {
                        baseWebview.loadUrl("javascript:refresh()");
                    }

                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != publicFundInfObservable) {
            RxBus.get().unregister(RxConstant.REFRESH_PUBLIC_FUND_INFO, publicFundInfObservable);
        }
    }
}
