package app.privatefund.com.order.mvp.ui;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;

import app.privatefund.com.order.mvp.presenter.OderPresenter;

/**
 * desc  B端订单的fragment
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-11:22
 */
public class OderFragment extends BaseLazyFragment<OderPresenter> {
    @Override
    protected void create(Bundle Mybundle) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    protected OderPresenter createPresenter() {
        return null;
    }
}
