package app.product.com.mvp.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.presenter.BasePagePresenter;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.ArrayList;

import app.product.com.R;

/**
 *
 */
public class CProductFragment extends BasePageFragment {

    @Override
    protected int titleLayoutId() {
        return R.layout.title_product;
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        TabBean tabBeen1 = new TabBean("产品",new ProductFragment());
//        TabBean tabBeen2 = new TabBean("资讯",new DiscoverFragmentc());
        TabBean tabBeen3 = new TabBean("视频",new ProductFragment());
        tabBeens.add(tabBeen1);
        tabBeens.add(tabBeen3);
        return tabBeens;
    }
}
