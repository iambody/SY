package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.Router;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.mvp.ui.ElegantGoodsFragment;
import app.mall.com.mvp.ui.ElegantLivingFragment;
import app.privatefund.com.im.MessageListActivity;

/**
 * @author chenlong
 *         <p>
 *         乐享生活
 */
public class HappyLifeFragment extends BasePageFragment implements View.OnClickListener {

    private final String NAVIGATION_CODE = "30";
    private final String LIFE_HOME_CODE = "3001";
    private final String LIFE_MALL_CODE = "3002";

    ImageView toolbarLeft;
    ImageView toolbarRight;

    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        ((TextView) title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_happy_live_str);
        toolbarLeft = (ImageView) title_layout.findViewById(R.id.iv_title_left);
        toolbarRight = (ImageView) title_layout.findViewById(R.id.iv_title_right);
        toolbarLeft.setVisibility(View.VISIBLE);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
    }

    @Override
    protected void setIndex(int code) {
        super.setIndex(code);
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
                case LIFE_HOME_CODE:
                    TabBean tabBeen1 = new TabBean(secondNavigation.getTitle(), new ElegantLivingFragment(), Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen1);
                    break;
                case LIFE_MALL_CODE:
                    TabBean tabBeen2 = new TabBean(secondNavigation.getTitle(), new ElegantGoodsFragment(), Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen2);
                    break;
            }
        }
        return tabBeens;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left://toolbar左边按钮点击事件
//                RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE,4);
                Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
                intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? "我的投顾" : "选择投顾");
                intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
                getActivity().startActivity(intent);
                break;
            case R.id.iv_title_right://toolbar右边按钮点击事件
                NavigationUtils.startActivity(getActivity(), MessageListActivity.class);
//                Router.build(RouteConfig.GOTOCSETTINGACTIVITY).go(baseActivity);
                break;
        }
    }

    @Override
    protected void bindTitle(View titleView) {

    }

    @Override
    protected int indexSel() {
        return 0;
    }

    public void setCode(int index) {
        super.setIndex(index);
    }

}
