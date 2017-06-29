package com.cgbsoft.privatefund.mvp.ui.home;

import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.mvp.ui.video.VideoSchoolFragment;
import app.product.com.mvp.ui.ProductFragment;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/27-20:38
 */
public class PrivateBanksFragment extends BasePageFragment {

    @Override
    protected int titleLayoutId() {
        return R.layout.title_fragment_privatebancks;
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<NavigationBean> navigationBeans = NavigationUtils.getNavigationBeans(getActivity());
        for (NavigationBean navigationBean : navigationBeans) {
            if (navigationBean.getTitle().equals(R.string.privatebanck_str)) {
                List<SecondNavigation> secondNavigations = navigationBean.getSecondNavigation();

            }
        }
        ArrayList<TabBean> tabBeens = new ArrayList<>();

        TabBean tabBeen1 = new TabBean("产品", new ProductFragment());
        TabBean tabBeen2 = new TabBean("资讯", new DiscoveryFragment());
//        TabBean tabBeen3 = new TabBean("视频", new VideoSchoolFragment());
        tabBeens.add(tabBeen1);
        tabBeens.add(tabBeen2);
//        tabBeens.add(tabBeen3);
        return tabBeens;
    }
}
