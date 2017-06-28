package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainHomePresenter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:06
 */
public class MainHomeFragment extends BaseFragment<MainHomePresenter> implements MainHomeContract.View {
    BaseWebview a;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mainhome;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        a = (BaseWebview) mFragmentView.findViewById(R.id.a);
        a.loadUrls("http://www.sohu.com/");
    }

    @Override
    protected MainHomePresenter createPresenter() {
        return null;
    }


    @Override
    public void getResultSucc(HomeEntity.Result data) {

    }

    @Override
    public void getResultError(String error) {

    }
}
