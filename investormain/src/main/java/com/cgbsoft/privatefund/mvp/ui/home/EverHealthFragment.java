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
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.chenenyu.router.Router;

import java.util.ArrayList;
import java.util.List;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;
import app.privatefund.investor.health.mvp.ui.CheckHealthFragment;
import app.privatefund.investor.health.mvp.ui.IntroduceHealthFragment;
import io.rong.imkit.RongIM;

/**
 *@author chenlong
 *
 * 乐享生活
 */
public class EverHealthFragment extends BasePageFragment implements View.OnClickListener {

    private final String NAVIGATION_CODE = "40";
    private final String HEALTE_INTRODUCTION_CODE = "4001";
    private final String HEALTH_CHECK_CODE = "4002";
    private final String HEALTH_MEDICAL_CODE = "4003";

    ImageView toolbarLeft;
    ImageView toolbarRight;
    private UnreadInfoNumber unreadInfoNumber;

    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        ((TextView)title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_ever_ok_str);
        toolbarLeft = (ImageView) title_layout.findViewById(R.id.iv_title_left);
        toolbarRight = (ImageView) title_layout.findViewById(R.id.iv_title_right);
        toolbarLeft.setVisibility(View.VISIBLE);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), toolbarRight);
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
    protected void bindTitle(View titleView) {

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
                case HEALTE_INTRODUCTION_CODE:
                    TabBean tabBeen1 = new TabBean(secondNavigation.getTitle(), new IntroduceHealthFragment(),Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen1);
                    break;
                case HEALTH_CHECK_CODE:
                    Bundle checkBund = new Bundle();
                    checkBund.putBoolean(CheckHealthFragment.FROM_CHECK_HEALTH, true);
                    CheckHealthFragment checkHealthFragment = new CheckHealthFragment();
                    checkHealthFragment.setArguments(checkBund);
                    TabBean tabBeen2 = new TabBean(secondNavigation.getTitle(), checkHealthFragment,Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen2);
                    break;
                case HEALTH_MEDICAL_CODE:
                    Bundle medical = new Bundle();
                    medical.putBoolean(CheckHealthFragment.FROM_CHECK_HEALTH, false);
                    CheckHealthFragment medicalHealthFragment = new CheckHealthFragment();
                    medicalHealthFragment.setArguments(medical);
                    TabBean tabBeen3 = new TabBean(secondNavigation.getTitle(), medicalHealthFragment,Integer.parseInt(secondNavigation.getCode()));
                    tabBeens.add(tabBeen3);
                    break;
            }
        }
        return tabBeens;
    }

    @Override
    protected int indexSel() {
        return 0;
    }

    public void setCode(int index){
        super.setIndex(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left://toolbar左边按钮点击事件
//                RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE,4);
                Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
                intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? "我的私人银行家" : "私人银行家");
                intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
                getActivity().startActivity(intent);
                break;
            case R.id.iv_title_right://toolbar右边按钮点击事件
                if (AppManager.isVisitor(InitApplication.getContext())) {
                    Intent intentRight = new Intent(getActivity(), LoginActivity.class);
                    intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
                    intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN_FROMCENTER, true);
                    UiSkipUtils.toNextActivityWithIntent(getActivity(), intentRight);
                    return;
                }
                NavigationUtils.startActivity(getActivity(), MessageListActivity.class);
//                Router.build(RouteConfig.GOTOCSETTINGACTIVITY).go(baseActivity);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.onDestroy();
        }
    }
}
