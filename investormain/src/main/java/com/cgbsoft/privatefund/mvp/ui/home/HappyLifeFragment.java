package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.Router;

import java.util.ArrayList;

import app.mall.com.mvp.ui.ElegantGoodsFragment;
import app.mall.com.mvp.ui.ElegantLivingFragment;

/**
 *@author chenlong
 *
 * 乐享生活
 */
public class HappyLifeFragment extends BasePageFragment implements View.OnClickListener{

    ImageView toolbarLeft;
    ImageView toolbarRight;
    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        ((TextView)title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_happy_live_str);
        toolbarLeft = (ImageView) title_layout.findViewById(R.id.iv_title_left);
        toolbarRight = (ImageView) title_layout.findViewById(R.id.iv_title_right);
        toolbarLeft.setVisibility(View.VISIBLE);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        TabBean elegantLivingBeen = new TabBean(getResources().getString(R.string.elegantliving_str), new ElegantLivingFragment());
        TabBean elegantGoodsBeen = new TabBean(getResources().getString(R.string.elegantgoods_str), new ElegantGoodsFragment());
        tabBeens.add(elegantLivingBeen);
        tabBeens.add(elegantGoodsBeen);
        return tabBeens;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left://toolbar左边按钮点击事件
                RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE,4);
                break;
            case R.id.iv_title_right://toolbar右边按钮点击事件
                Router.build(RouteConfig.GOTOCSETTINGACTIVITY).go(baseActivity);
                break;
        }
    }
}
