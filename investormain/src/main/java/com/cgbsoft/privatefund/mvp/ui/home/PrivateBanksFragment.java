package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.vido.mvp.ui.video.VideoSchoolFragment;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.ui.ProductFragment;

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

    @Override
    protected int titleLayoutId() {
        return R.layout.title_fragment_privatebancks;
    }

    @Override
    protected ArrayList<TabBean> list() {
        ArrayList<NavigationBean> navigationBeans = NavigationUtils.getNavigationBeans(getActivity());
        ArrayList<TabBean> tabBeens = new ArrayList<>();
        if (navigationBeans != null) {
            for (NavigationBean navigationBean : navigationBeans) {
                if (navigationBean.getCode().equals(NAVIGATION_CODE)) {
                    return loadTab(tabBeens, navigationBean);
                }
            }
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void bindTitle(View titleView) {
        LinearLayout search = (LinearLayout) titleView.findViewById(R.id.search_layout_main);
        ImageView privatebank_title_left = (ImageView) titleView.findViewById(R.id.privatebank_title_left);
        privatebank_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
                intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? "我的投顾" : "选择投顾");
                intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
                getActivity().startActivity(intent);
            }
        });
        ImageView privatebank_title_right = (ImageView) titleView.findViewById(R.id.privatebank_title_right);
        privatebank_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.startActivity(getActivity(), MessageListActivity.class);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(baseActivity, SearchBaseActivity.class);
                i.putExtra(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.PRODUCT);
                baseActivity.startActivity(i);
                DataStatistApiParam.onStatisToCProductSearchClick();
            }
        });

    }

    @Override
    protected int indexSel() {
        return 0;
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
                    TabBean tabBeen1 = new TabBean(secondNavigation.getTitle(), new ProductFragment(), Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen1);
                    break;
                case INFOMATION_CODE:
                    TabBean tabBeen2 = new TabBean(secondNavigation.getTitle(), new DiscoveryFragment(), Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen2);
                    break;
                case VIDEO_CODE:
                    TabBean tabBeen3 = new TabBean(secondNavigation.getTitle(), new VideoSchoolFragment(), Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen3);
                    break;
            }
        }
        return tabBeens;
    }

    public void setCode(int index) {
        super.setIndex(index);
    }
}
