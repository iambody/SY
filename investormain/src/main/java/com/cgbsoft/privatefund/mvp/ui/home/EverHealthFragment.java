package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;

import app.privatefund.investor.health.mvp.ui.CheckHealthFragment;
import app.privatefund.investor.health.mvp.ui.IntroduceHealthFragment;

/**
 *@author chenlong
 *
 * 乐享生活
 */
public class EverHealthFragment extends BasePageFragment {

    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        ((TextView)title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_ever_ok_str);
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        Bundle checkBund = new Bundle();
        checkBund.putBoolean(CheckHealthFragment.FROM_CHECK_HEALTH, true);
        CheckHealthFragment checkHealthFragment = new CheckHealthFragment();
        checkHealthFragment.setArguments(checkBund);
        Bundle medical = new Bundle();
        medical.putBoolean(CheckHealthFragment.FROM_CHECK_HEALTH, false);
        CheckHealthFragment medicalHealthFragment = new CheckHealthFragment();
        medicalHealthFragment.setArguments(medical);

        TabBean tabBeen1 = new TabBean("介绍", new IntroduceHealthFragment());
        TabBean tabBeen2 = new TabBean("检查", checkHealthFragment);
        TabBean tabBeen3 = new TabBean("医疗", medicalHealthFragment);
        tabBeens.add(tabBeen1);
        tabBeens.add(tabBeen2);
        tabBeens.add(tabBeen3);
        return tabBeens;
    }

    @Override
    protected void bindTitle(View titleView) {

    }

    @Override
    protected int indexSel() {
        return 1;
    }

}
