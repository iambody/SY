package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.RxCountDown;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.widget.MySwipeRefreshLayout;
import com.cgbsoft.privatefund.R;

import butterknife.BindView;
import rx.Observable;

/**
 * @author chenlong
 *         公募基金
 */
public class PublicFundFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.webview)
    BaseWebview baseWebview;
    Observable<Integer> publicFundInfObservable;
    @BindView(R.id.main_publicfund_swiperefreshlayout)
    MySwipeRefreshLayout mainPublicfundSwiperefreshlayout;

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
        initConfig();
        initEvents();

    }

    private void initEvents() {
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
        baseWebview.setBaseWebViewScrollListener(new BaseWebview.OnBaseWebViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void scrollHeight(int h) {
                if (0 == h) {
                    mainPublicfundSwiperefreshlayout.setEnabled(true);
                } else {
                    mainPublicfundSwiperefreshlayout.setEnabled(false);
                }
            }
        });
    }

    private void initConfig() {
        //等级
        mainPublicfundSwiperefreshlayout.setProgressBackgroundColorSchemeResource(R.color.white);
        // 设置下拉进度的主题颜色
        mainPublicfundSwiperefreshlayout.setColorSchemeResources(R.color.app_golden_disable, R.color.app_golden, R.color.app_golden_click, R.color.app_golden_click);
        mainPublicfundSwiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mainPublicfundSwiperefreshlayout.setOnRefreshListener(this);
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

    @Override
    public void onRefresh() {

        RxCountDown.countdown(2).subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                if (null != mainPublicfundSwiperefreshlayout && mainPublicfundSwiperefreshlayout.isRefreshing())
                    mainPublicfundSwiperefreshlayout.setRefreshing(false);

                baseWebview.loadUrl("javascript:refresh()");
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
