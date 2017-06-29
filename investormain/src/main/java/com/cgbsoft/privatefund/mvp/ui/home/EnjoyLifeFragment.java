package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.EnjoyLifeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.EnjoyLifePresenter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/28-11:27
 */
public class EnjoyLifeFragment extends BaseFragment<EnjoyLifePresenter>implements EnjoyLifeContract.View {
    @Override
    protected int layoutID() {
        return R.layout.fragment_enjoylife;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected EnjoyLifePresenter createPresenter() {
        return null;
    }
}
