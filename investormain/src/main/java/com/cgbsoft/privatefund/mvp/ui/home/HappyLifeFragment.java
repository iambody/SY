package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingLifeDataStatistics;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.mvp.ui.ElegantGoodsFragment;
import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;

/**
 * @author chenlong
 *         <p>
 *         乐享生活
 */
public class HappyLifeFragment extends BasePageFragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private final String NAVIGATION_CODE = "30";
    private final String LIFE_HOME_CODE = "3001";
    private final String LIFE_MALL_CODE = "3002";

    ImageView toolbarLeft;
    ImageView toolbarRight;
    private UnreadInfoNumber unreadInfoNumber;
    private Toolbar toolbar;
    private MenuItem rightItem;

    @Override
    protected int titleLayoutId() {
        return R.layout.title_normal_new;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);//加上这句话，menu才会显示出来
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
//        toolbar = (Toolbar) title_layout.findViewById(R.id.tb_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.select_happy_life_toolbar_left);
//        toolbar.setOnMenuItemClickListener(this);
        ((TextView) title_layout.findViewById(R.id.title_mid)).setText(R.string.vbnb_happy_live_str);
        toolbarLeft = (ImageView) title_layout.findViewById(R.id.iv_title_left);
        toolbarRight = (ImageView) title_layout.findViewById(R.id.iv_title_right);
        toolbarLeft.setVisibility(View.VISIBLE);
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarLeft.setOnClickListener(this);
        toolbarRight.setOnClickListener(this);
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), toolbarRight, true);
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

    @Override
    public void onResume() {
        super.onResume();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.initUnreadInfoAndPosition();
        }
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
//                    TabBean tabBeen1 = new TabBean(secondNavigation.getTitle(), new ElegantLivingFragment(), Integer.parseInt(secondNavigation.getCode()));
//                    tabBeens.add(tabBeen1);
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
                DataStatistApiParam.clickFPInElegantPage();
                Intent intent = new Intent(getActivity(), RightShareWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
                intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? "我的私人银行家" : "私人银行家");
                intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
                getActivity().startActivity(intent);
                TrackingLifeDataStatistics.homeClickAdviser(getContext());
                break;
            case R.id.iv_title_right://toolbar右边按钮点击事件
                DataStatistApiParam.clickMsgCenterInElegantPage();
                if (AppManager.isVisitor(InitApplication.getContext())) {
                    Intent intentRight = new Intent(getActivity(), LoginActivity.class);
                    intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
                    intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN_FROMCENTER, true);
                    UiSkipUtils.toNextActivityWithIntent(getActivity(), intentRight);
                    return;
                }
                NavigationUtils.startActivity(getActivity(), MessageListActivity.class);
                TrackingLifeDataStatistics.homeClickNews(getContext());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.onDestroy();
        }
    }

    @Override
    protected void clickTabButton(String tabName) {
        super.clickTabButton(tabName);
        DataStatistApiParam.clickElegantGoodsButton(tabName);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.elegant_menu, menu);
        rightItem = menu.findItem(R.id.firstBtn);
        Drawable drawable = getResources().getDrawable(R.drawable.select_happy_life_toolbar_right);
        rightItem.setIcon(drawable);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void viewBeShow() {
        super.viewBeShow();
        TrackingLifeDataStatistics.goLifeHomePage(getContext());
    }

    @Override
    public void viewBeHide() {
        super.viewBeHide();
    }
}
