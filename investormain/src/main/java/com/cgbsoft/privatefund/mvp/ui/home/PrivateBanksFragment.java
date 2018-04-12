package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.model.NavigationBean;
import com.cgbsoft.lib.base.mvp.model.SecondNavigation;
import com.cgbsoft.lib.base.mvp.model.TabBean;
import com.cgbsoft.lib.base.mvp.ui.BasePageFragment;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.TrackingDiscoveryDataStatistics;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.vido.mvp.ui.video.VideoSchoolFragment;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.ui.OnLineProductListFragment;
import rx.Observable;

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
    public final String PUBLIC_FUND_CODE = "2004";//公募基金新增加的code

    private Observable<Integer> privateFundIdex, registLayFresh;
    private ImageView privatebank_title_right;
    private UnreadInfoNumber unreadInfoNumber;
    private boolean isDoneFreash;
//    volatile boolean isInit;

    @Override
    protected int titleLayoutId() {
//        isInit = true;
        return R.layout.title_fragment_privatebancks;
    }

    @Override
    public void viewBeShow() {
        super.viewBeShow();
    }

    @Override
    public void viewBeHide() {
        super.viewBeHide();
    }
    @Override
    protected ArrayList<TabBean> list() {
        isDoneFreash = true;
        ArrayList<NavigationBean> navigationBeans = NavigationUtils.getNavigationBeans(getActivity());
        ArrayList<TabBean> tabBeens = new ArrayList<>();

        //新添加了白名单的逻辑处理
//        if (!BStrUtils.isEmpty(AppManager.getPublicFundInf(baseActivity.getApplicationContext()).getWhiteUserListFlg()) && "1".equals(AppManager.getPublicFundInf(baseActivity.getApplicationContext()).getWhiteUserListFlg())) {
//            tabBeens.add(new TabBean("公募基金", new PublicFundFragment(), Integer.parseInt(PUBLIC_FUND_CODE)));
//        }

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
    protected void init(View view, Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), privatebank_title_right, true);
        privateFundIdex = RxBus.get().register(RxConstant.MAIN_FRESH_PRIVATE_IDEXLAY, Integer.class);
        privateFundIdex.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                switch (integer) {
                    case 1:
//                        isInit=true;
                        setCode(2001);

                        break;
                    case 2://公募 postion第一位置
                        setCode(2004);
//                        setIndex1(1);
                        break;
                }

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        if (null == registLayFresh) {
            registLayFresh = RxBus.get().register(RxConstant.REFRESH_PUBLIC_FUND_RESGIST_LAY, Integer.class);
            registLayFresh.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer publicFundIn) {
                    if (!isDoneFreash) {
                        inflaterData();
                        isDoneFreash = true;
                    }
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != privateFundIdex) {
            RxBus.get().unregister(RxConstant.MAIN_FRESH_PRIVATE_IDEXLAY, privateFundIdex);
        }


        if (null != registLayFresh) {
            RxBus.get().unregister(RxConstant.REFRESH_PUBLIC_FUND_RESGIST_LAY, registLayFresh);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.initUnreadInfoAndPosition();
        }
    }

    @Override
    protected void bindTitle(View titleView) {
        LinearLayout search = (LinearLayout) titleView.findViewById(R.id.search_layout_main);
        ImageView privatebank_title_left = (ImageView) titleView.findViewById(R.id.privatebank_title_left);
        privatebank_title_left.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RightShareWebViewActivity.class);
            intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
            intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? "我的私人银行家" : "私人银行家");
            intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
            getActivity().startActivity(intent);
            DataStatistApiParam.operatePrivateBankPersonalClick();
        });
        privatebank_title_right = (ImageView) titleView.findViewById(R.id.privatebank_title_right);
        privatebank_title_right.setOnClickListener(v -> {
            if (AppManager.isVisitor(InitApplication.getContext())) {
                Intent intentRight = new Intent(getActivity(), LoginActivity.class);
                intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
                intentRight.putExtra(LoginActivity.TAG_GOTOLOGIN_FROMCENTER, true);
                UiSkipUtils.toNextActivityWithIntent(getActivity(), intentRight);
                return;
            }
            NavigationUtils.startActivity(getActivity(), MessageListActivity.class);
            DataStatistApiParam.operatePrivateBankMessageClick();
        });
        search.setOnClickListener(v -> {
            Intent i = new Intent(baseActivity, SearchBaseActivity.class);
            i.putExtra(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.PRODUCT);
            baseActivity.startActivity(i);
            DataStatistApiParam.operatePrivateBankSearchClick();
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
                    TabBean tabBeen1 = new TabBean("私募基金", new OnLineProductListFragment(), Integer.parseInt(secondNavigation.getCode()));
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
//                case PUBLIC_FUND_CODE:
//                case PRODUCT_CODE:
//                    TabBean tabBeen4 = new TabBean(secondNavigation.getTitle(), new PublicFundFragment(), Integer.parseInt(secondNavigation.getCode()));
//                    tabBeens.add(tabBeen4);
//                    break;
            }
        }

        if (!BStrUtils.isEmpty(AppManager.getPublicFundInf(baseActivity.getApplicationContext()).getWhiteUserListFlg()) && "1".equals(AppManager.getPublicFundInf(baseActivity.getApplicationContext()).getWhiteUserListFlg())) {
            tabBeens.add(1, new TabBean("公募(内测)", new PublicFundFragment(), Integer.parseInt(PUBLIC_FUND_CODE)));
        }
        return tabBeens;
    }

    public void setCode(int index) {
//
//        if (!isInit) {
//            RxCountDown.countdown(2).doOnSubscribe(new Action0() {
//                @Override
//                public void call() {
//
//                }
//            }).subscribe(new Subscriber<Integer>() {
//                @Override
//                public void onCompleted() {
//                    setIndex1(1);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(Integer integer) {
//
//
//                }
//            });
//
//
//
//        } else
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
        DataStatistApiParam.honourPBitemClick(tabName);
        if (TextUtils.equals("资讯", tabName)) {
            TrackingDiscoveryDataStatistics.discoveryClickFlag(getContext(), tabName);
        } else if (TextUtils.equals("视频", tabName)) {
            TrackingDataManger.privateBanckVideoShow(getActivity());
        }
    }
}
