package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.privatefund.R;

/**
 * @author chenlong
 *
 * 我的fragment
 */
public class MineFragment extends BaseFragment {


    @Override
    protected int layoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

