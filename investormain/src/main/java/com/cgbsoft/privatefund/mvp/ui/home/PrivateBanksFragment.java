package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.mvp.ui.video.VideoSchoolFragment;
import app.product.com.R2;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.ui.ProductFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/27-20:38
 */
public class PrivateBanksFragment extends BasePageFragment {

    private final String NAVIGATION_CODE = "20";
    private final String PRODUCT_CODE = "2001";
    private final String INFOMATION_CODE = "2002";
    private final String VIDEO_CODE = "2003";

    @BindView(R.id.search_layout)
    LinearLayout searchText;

    @Override
    protected int titleLayoutId() {
        return R.layout.title_fragment_privatebancks;
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<NavigationBean> navigationBeans = NavigationUtils.getNavigationBeans(getActivity());
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        for (NavigationBean navigationBean : navigationBeans) {
            if (navigationBean.getCode().equals(NAVIGATION_CODE)) {
                return loadTab(tabBeens, navigationBean);
            }
        }
        return null;
    }

    /**
     * 加载Tab数据
     *
     * @param tabBeens
     * @param navigationBean
     */
    private ArrayList<TabBean> loadTab(ArrayList<TabBean> tabBeens, NavigationBean navigationBean) {
        List<SecondNavigation> secondNavigations = navigationBean.getSecondNavigation();
        for (SecondNavigation secondNavigation : secondNavigations) {
            switch (secondNavigation.getCode()) {
                case PRODUCT_CODE:
                    TabBean tabBeen1 = new TabBean(secondNavigation.getTitle(), new ProductFragment());
                    tabBeens.add(tabBeen1);
                    break;
                case INFOMATION_CODE:
                    TabBean tabBeen2 = new TabBean(secondNavigation.getTitle(), new DiscoveryFragment());
                    tabBeens.add(tabBeen2);
                    break;
                case VIDEO_CODE:
//                            TabBean tabBeen3 = new TabBean(secondNavigation.getTitle(), new VideoSchoolFragment());
//                            tabBeens.add(tabBeen3);
                    break;
            }
        }
        return tabBeens;
    }


    @OnClick(R.id.search_layout)
    public void searchClick() {
        Intent i = new Intent(baseActivity, SearchBaseActivity.class);
        i.putExtra(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.PRODUCT);
        baseActivity.startActivity(i);
        DataStatistApiParam.onStatisToCProductSearchClick();
    }
}
