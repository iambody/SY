package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;

import app.privatefund.com.vido.mvp.ui.video.VideoSchoolFragment;
import app.product.com.mvp.ui.ProductFragment;

/**
 * @author chenlong
 *
 * 乐享生活
 */
public class HappyLifeFragment extends BasePageFragment {

    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        ((TextView)title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_happy_live_str);
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        TabBean tabBeen1 = new TabBean("生活家", new ProductFragment());
        TabBean tabBeen2 = new TabBean("尚品", new VideoSchoolFragment());
        tabBeens.add(tabBeen1);
        tabBeens.add(tabBeen2);
        return tabBeens;
    }

}
