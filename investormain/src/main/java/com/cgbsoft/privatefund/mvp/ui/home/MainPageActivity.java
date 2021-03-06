package com.cgbsoft.privatefund.mvp.ui.home;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.bean.ConversationBean;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.BdLocationListener;
import com.cgbsoft.lib.utils.PackageIconUtils;
import com.cgbsoft.lib.utils.SkineColorManager;
import com.cgbsoft.lib.utils.StatusBarUtil;
import com.cgbsoft.lib.utils.ZipResourceDownload;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LocationManger;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.dialog.DownloadDialog;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.LiveInfBean;
import com.cgbsoft.privatefund.bean.commui.OpenWebBean;
import com.cgbsoft.privatefund.bean.location.LocationBean;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainPagePresenter;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.utils.PageJumpMananger;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;
import com.chenenyu.router.annotation.Route;
import com.cn.hugo.android.scanner.QrCodeBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.growingio.android.sdk.collection.GrowingIO;
import com.tencent.TIMUserProfile;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.bean.SMMessage;
import app.privatefund.com.im.listener.MyGroupInfoListener;
import app.privatefund.com.im.listener.MyGroupMembersProvider;
import app.privatefund.com.im.listener.MyGroupUserInfoProvider;
import app.privatefund.com.im.listener.MyUserInfoListener;
import app.privatefund.com.im.utils.PushPreference;
import app.privatefund.com.im.utils.ReceiveInfoManager;
import app.privatefund.com.im.utils.RongConnect;
import app.privatefund.com.im.utils.RongCouldUtil;
import app.privatefund.com.vido.service.FloatVideoService;
import app.privatefund.investor.health.mvp.ui.HealthCourseFragment;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.push.notification.PushNotificationMessage;
import qcloud.liveold.mvp.presenters.LoginHelper;
import qcloud.liveold.mvp.presenters.ProfileInfoHelper;
import qcloud.liveold.mvp.presenters.viewinface.LoginView;
import qcloud.liveold.mvp.presenters.viewinface.ProfileView;
import qcloud.liveold.mvp.views.LiveActivity;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

@Route(RouteConfig.GOTOCMAINHONE)
public class MainPageActivity extends BaseActivity<MainPagePresenter> implements BottomNavigationBar.BottomClickListener, MainPageContract.View, LoginView, ProfileView {
    private static final String FRAGMENTS_TAG = "android:support:fragments";
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    @BindView(R.id.webView)
    BaseWebview baseWebview;

    private Observable<Boolean> closeMainObservable;
    private Observable<Boolean> reRefrushUserInfoObservable;
    private Observable<Boolean> rongTokenRefushObservable;
    private Observable<Boolean> openMessageListObservable;
    private Observable<QrCodeBean> twoCodeObservable;
    private Observable<Integer> jumpIndexObservable;
    private Observable<ConversationBean> startConverstationObservable;
    private Observable<Boolean> hasReadResultObservable;
    private boolean isOnlyClose;
    private JSONObject liveJsonData;
    private LoginHelper loginHelper;
    private ProfileInfoHelper profileInfoHelper;
    private Observable<Integer> showIndexObservable, userLayObservable, killObservable, killstartObservable, publicFundInfObservable, goPublicBuyObservable;
    private Observable<Boolean> liveRefreshObservable;
    private LocationManger locationManger;
    private Subscription liveTimerObservable;
    private boolean hasLive = false;
    private int code;
    private InvestorAppli initApplication;

    private PackageIconUtils packageIconUtils;

    private Observable<Boolean> downDamicSoObservable;
    private ZipResourceDownload zipResourceDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable(FRAGMENTS_TAG, null);
        }
        super.onCreate(savedInstanceState);
    }


    /**
     * 定位管理器
     */
    @Override
    protected int layoutID() {
        return R.layout.activity_main_page;
    }

    @Override
    protected void after() {
        super.after();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        zipResourceDownload.initDownDialog();
        MobclickAgent.onResume(this);       //统计时长
        baseWebview.loadUrls(CwebNetConfig.pageInit);
        if (AppManager.isVisitor(baseContext) && 4 == currentPostion) { // 是游客模式
            switchID = R.id.nav_left_first;
            currentPostion = 0;
            bottomNavigationBar.selectNavaigationPostion(0);
            switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
        }
        if (!AppManager.isVisitor(baseContext) && 4 == currentPostion && switchID != R.id.nav_right_second) {
            switchID = R.id.nav_right_second;
            switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
        }
        if (!AppManager.isVisitor(baseContext)) {
            getPresenter().loadPublicFundInf();

        }
        getPresenter().loadRedPacket();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (null != savedInstanceState) {

        }

        StatusBarUtil.translucentStatusBar(this);
        initApplication = (InitApplication) getApplication();
        initApplication.setMainpage(true);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first, code);

        code = getIntent().getIntExtra("code", 0);

        zipResourceDownload = new ZipResourceDownload(this);


        transaction.add(R.id.fl_main_content, mContentFragment);

        transaction.commitAllowingStateLoss();

        initRxObservable();

        initUserInfo();

        RongConnect.initRongTokenConnect(AppManager.getUserId(getApplicationContext()));


        showInfoDialog();

        initIndex(code);

        initLocation();

        //游客模式下禁止的Api 添加限制条件
        if (!AppManager.isVisitor(baseContext)) {
            loginLive();
            autoSign();
            initDayTask();
            initRongInterface();
//            getPresenter().loadPublicFundInf();
            AppInfStore.saveLatestPhone(baseContext, AppManager.getUserInfo(baseContext).getPhoneNum());
        }
        RxBus.get().post(RxConstant.LOGIN_KILL, 1);
        // 推送过来的跳转
        jumpPushMessage();

        initLogo();

        zipResourceDownload.initZipResource();

        TrackingDataManger.gohome(baseContext);

    }

    private void initLogo() {
        //初始化log
        packageIconUtils = new PackageIconUtils(baseContext, baseContext.getPackageManager(), "com.cgbsoft.privatefund.MainActivity0", "com.cgbsoft.privatefund.MainActivity1");

        boolean isAutumn = SkineColorManager.isautumnHoliay();
//        isAutumn=true;
        if (isAutumn) {
            packageIconUtils.enableComponentName1();
            //需要重启默认的应用*****************************
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            List<ResolveInfo> resolves = baseContext.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo res : resolves) {
                if (res.activityInfo != null) {
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(res.activityInfo.packageName);
                }
            }
        } else {
            packageIconUtils.enableComponentDefault();
            //需要重启默认的应用*****************************
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            List<ResolveInfo> resolves = baseContext.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo res : resolves) {
                if (res.activityInfo != null) {
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    am.killBackgroundProcesses(res.activityInfo.packageName);
                }
            }
        }
    }

    private void jumpPushMessage() {
        PushNotificationMessage pushMessage = getIntent().getParcelableExtra(WebViewConstant.PUSH_MESSAGE_OBJECT_NAME);
        Uri uri = getIntent().getParcelableExtra(WebViewConstant.PUSH_MESSAGE_RONGYUN_URL_NAME);
        if (pushMessage != null && uri != null) {
            PageJumpMananger.jumpPageFromToMainActivity(this, pushMessage);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("tobuypublicfund")) {
            HashMap<String, Object> maps = ((BaseApplication) baseContext.getApplication()).getPublicBuyMaps();
            if (null != maps && !maps.isEmpty())
                NavigationUtils.startActivityByRouter(baseContext, RouteConfig.GOTO_PUBLIC_FUND_BUY, maps);
            return;
        }
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("goWebTab") && "1".equals(getIntent().getExtras().getString("goWebTab"))) {
            OpenWebBean webBean = ((InvestorAppli) InvestorAppli.getContext()).getWebBean();
            int switchTab=50;
            if (getIntent().getExtras().containsKey("switchTab")) {
                switchTab = getIntent().getExtras().getInt("switchTab");
            }

            initIndex(switchTab);
            if (null == webBean) return;
            Intent intentWeb = new Intent(baseContext, BaseWebViewActivity.class);
            intentWeb.putExtra(WebViewConstant.push_message_url, NetConfig.SERVER_ADD + webBean.getURL());
            intentWeb.putExtra(WebViewConstant.push_message_title, webBean.getTitle());
            intentWeb.putExtra(WebViewConstant.push_message_title_isdiv, webBean.isHasHTMLTag());
            intentWeb.putExtra(WebViewConstant.push_message_title_is_hidetoolbar, true);
            baseContext.startActivity(intentWeb);

            return;
        }

        if (getIntent().getBooleanExtra("gotoMainPage", false)) {
            RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE, 0);
        }
        Log.i("MainPageActivity", "----code=" + code);
        if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("code")) {
            code = getIntent().getIntExtra("code", 0);
            initIndex(code);
        }
    }

    private void initIndex(int code) {
        if (0 != code) {
            int substring = Integer.parseInt(String.valueOf(code).substring(0, 1));
            onTabSelected(substring - 1, code);
            bottomNavigationBar.selectNavaigationPostion(substring - 1);
        }
    }


    private void initActionPoint() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("client", "c");
        getPresenter().actionPoint(map);
    }

    private void initUserInfo() {
        RxBus.get().post(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, true);
    }

    private void autoSign() {
        if (!AppManager.isVisitor(baseContext) && "0".equals(AppManager.getUserInfo(this).getIsSingIn())) {
            getPresenter().toSignIn();
        }
    }

    /**
     * 初始化融云的接口信息
     */
    private void initRongInterface() {
        RongIM.setUserInfoProvider(new MyUserInfoListener(), true);
        RongIM.setGroupInfoProvider(new MyGroupInfoListener(), true);
        RongIM.setGroupUserInfoProvider(new MyGroupUserInfoProvider(this), true);
        RongIM.getInstance().setGroupMembersProvider(new MyGroupMembersProvider(this));
        initDayTask();
    }

    /**
     * 各种需要初始化判断是否显示dialog的 eg:风险测评
     */
    private void initDialog() {
    }

    private void loginLive() {
        loginHelper = new LoginHelper(this, this);
        profileInfoHelper = new ProfileInfoHelper(this);
        loginHelper.getLiveSign(AppManager.getUserId(this));
    }

    @Override
    protected MainPagePresenter createPresenter() {
        return new MainPagePresenter(this, this);
    }

    @Override
    protected void data() {
        bottomNavigationBar.setOnClickListener(this);
        bottomNavigationBar.setActivity();

        if (!SPreference.isThisRunOpenDownload(this)) {
            new DownloadDialog(this, true, false);
        }

    }

    private void switchFragment(Fragment to) {
        if (mContentFragment != to) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            setCustomAnimations(R.anim.home_fade_in, R.anim.home_fade_out);


            if (!to.isAdded()) {  // 先判断是否被add过
                transaction.hide(mContentFragment).add(R.id.fl_main_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContentFragment).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            todo();
            mContentFragment = to;
        }
    }

    private void showInfoDialog() {
        String pushInfo = PushPreference.getPushInfo(this);
        if (!TextUtils.isEmpty(pushInfo)) {
            Gson gson = new Gson();
            List<SMMessage> smMessageList = gson.fromJson(pushInfo, new TypeToken<List<SMMessage>>() {
            }.getType());
            SMMessage smMessage = smMessageList.get(smMessageList.size() == 0 ? 0 : smMessageList.size() - 1);
            android.os.Message message2 = android.os.Message.obtain();
            Bundle bundle2 = new Bundle();
            message2.what = TextUtils.isEmpty(smMessage.getShowType()) ? 0 : Integer.parseInt(smMessage.getShowType());
            bundle2.putString("jumpUrl", smMessage.getJumpUrl());
            bundle2.putString("detail", TextUtils.isEmpty(smMessage.getDialogSummary()) ? " " : smMessage.getDialogSummary());
            bundle2.putString("title", TextUtils.isEmpty(smMessage.getDialogTitle()) ? " " : smMessage.getDialogTitle());
            bundle2.putString("shareType", smMessage.getShareType());
            message2.setData(bundle2);
            ReceiveInfoManager.getInstance().getHandler().sendMessage(message2);
            PushPreference.savePushInfo(this, "");
        }
    }

    private void todo() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("testviewshowhind", "主页开始显示************");
        initUserInfo();
        if (null != liveTimerObservable) {
            liveTimerObservable = Observable.interval(0, 10000, TimeUnit.MILLISECONDS)
                    //延时0 ，每间隔5000，时间单位
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            getPresenter().getProLiveList();
                        }
                    });
        }

        RxBus.get().post(RxConstant.RefreshRiskState, true);

        (MainTabManager.getInstance().getFragmentByIndex(switchID, code)).viewBeShow();

    }

    int switchID = R.id.nav_left_first;
    int currentPostion = 0;

    boolean isHaveClickProduct;

    @Override
    public void onTabSelected(int position, int code) {
//        int switchID = -1;
        switch (position) {
            case 0://左1
                StatusBarUtil.translucentStatusBar(this);
                switchID = R.id.nav_left_first;
                currentPostion = 0;
                TrackingDataManger.gohome(baseContext);
                break;
            case 1://左2
                switchID = R.id.nav_left_second;
                currentPostion = 1;
//                RxBus.get().post(RxConstant.REFRESH_PRODUCT, true);
                break;
            case 3://左3
                switchID = R.id.nav_right_first;
                currentPostion = 3;
                break;
            case 4://左4
                StatusBarUtil.translucentStatusBar(this);
                currentPostion = 4;
                if (AppManager.isVisitor(InitApplication.getContext())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
                    intent.putExtra(LoginActivity.TAG_GOTOLOGIN_FROMCENTER, true);
                    UiSkipUtils.toNextActivityWithIntent(this, intent);
                    return;
                }

                switchID = R.id.nav_right_second;
                break;
            case 2://中间
                switchID = R.id.nav_center;
                currentPostion = 2;
                break;
        }
        switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
        RxBus.get().post(RxConstant.PAUSR_HEALTH_VIDEO, position);
        buryPoint(position);
    }

    /**
     * 埋点
     */
    private void buryPoint(int postion) {
        switch (postion) {
            case 0:
                DataStatistApiParam.onStatisToCTabMine();
                TrackingDataManger.tabHome(baseContext);
                break;
            case 1:
                DataStatistApiParam.onStatisToCTabProduct();
                TrackingDataManger.tabPrivateBanck(baseContext);
                break;
            case 2:
                DataStatistApiParam.onStatisToCTabDiscover();
                TrackingDataManger.tabLife(baseContext);
                break;
            case 3:
                DataStatistApiParam.onStatisToCTabClub();
                TrackingDataManger.tabHealth(baseContext);
                break;
            case 4:
                DataStatistApiParam.onStatisToCTabCloudKey();
                TrackingDataManger.tabCenter(baseContext);
                break;
        }
    }

    private void initRxObservable() {
        downDamicSoObservable = RxBus.get().register(RxConstant.DOWN_DAMIC_SO, boolean.class);
        downDamicSoObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (aBoolean) {
                    zipResourceDownload.downloadSoFileOnce();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        killstartObservable = RxBus.get().register(RxConstant.MAIN_PAGE_KILL_START, Integer.class);
        killstartObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        killObservable = RxBus.get().register(RxConstant.MAIN_PAGE_KILL, Integer.class);
        killObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                MainPageActivity.this.finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        jumpIndexObservable = RxBus.get().register(RxConstant.JUMP_INDEX, Integer.class);
        jumpIndexObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                baseContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initIndex(integer);
                    }
                });

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        closeMainObservable = RxBus.get().register(RxConstant.CLOSE_MAIN_OBSERVABLE, Boolean.class);
        closeMainObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                isOnlyClose = aBoolean;
                if (RongIM.getInstance().getRongIMClient() != null) {
                    RongIMClient.getInstance().clearConversations(new RongIMClient.ResultCallback() {
                        @Override
                        public void onSuccess(Object o) {
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
                }
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().disconnect();
                }
                loginHelper.imLogout();
                finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        // 刷新用户信息
        reRefrushUserInfoObservable = RxBus.get().register(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, Boolean.class);
        reRefrushUserInfoObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (aBoolean) {
                    getPresenter().getUserInfo(false);
                } else {//需要刷新数据
                    getPresenter().getUserInfo(true);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        rongTokenRefushObservable = RxBus.get().register(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, Boolean.class);
        rongTokenRefushObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {

                Log.i("MainPageActivity", String.valueOf(aBoolean));
                baseWebview.loadUrls(WebViewConstant.PAGE_INIT);
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        openMessageListObservable = RxBus.get().register(RxConstant.OPEN_MESSAGE_LIST_PAGE_OBSERVABLE, Boolean.class);
        openMessageListObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                Log.i("MainPageActivity", "----rxbus open messagelist");
                HashMap<String, Boolean> hashMap = new HashMap<>();
                hashMap.put(Conversation.ConversationType.PRIVATE.getName(), false);
                hashMap.put(Conversation.ConversationType.GROUP.getName(), true);
                hashMap.put(Conversation.ConversationType.DISCUSSION.getName(), false);
                hashMap.put(Conversation.ConversationType.SYSTEM.getName(), false);
                RongIM.getInstance().startConversationList(MainPageActivity.this, hashMap);
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        twoCodeObservable = RxBus.get().register(RxConstant.LOOK_TWO_CODE_OBSERVABLE, QrCodeBean.class);
        twoCodeObservable.subscribe(new RxSubscriber<QrCodeBean>() {
            @Override
            protected void onEvent(QrCodeBean qrCodeBean) {
                toJumpTouziren(qrCodeBean);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        startConverstationObservable = RxBus.get().register(RxConstant.START_CONVERSATION_OBSERVABLE, ConversationBean.class);
        startConverstationObservable.subscribe(new RxSubscriber<ConversationBean>() {
            @Override
            protected void onEvent(ConversationBean conversationBean) {
                RongIM.getInstance().startConversation(conversationBean.getContext(), Conversation.ConversationType.PRIVATE, conversationBean.getTargetId(), conversationBean.getName());
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        userLayObservable = RxBus.get().register(RxConstant.MAIN_FRESH_LAY, Integer.class);
        userLayObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                loginLive();
                autoSign();
                initDayTask();
                initRongInterface();

                if (5 == integer) {
                    switchID = R.id.nav_right_second;
                    currentPostion = 4;
                    bottomNavigationBar.selectNavaigationPostion(4);
                    switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
                    return;
                }

                switchID = R.id.nav_left_first;
                currentPostion = 0;
                bottomNavigationBar.selectNavaigationPostion(0);
                switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        liveRefreshObservable = RxBus.get().register(RxConstant.REFRESH_LIVE_DATA, Boolean.class);
        liveRefreshObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (null != liveTimerObservable) {
                    liveTimerObservable = Observable.interval(0, 10000, TimeUnit.MILLISECONDS)
                            //延时0 ，每间隔5000，时间单位
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    getPresenter().getProLiveList();
                                }
                            });
                }
//                getPresenter().getProLiveList();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        showIndexObservable = RxBus.get().register(RxConstant.INVERSTOR_MAIN_PAGE, Integer.class);
        showIndexObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                onTabSelected(integer, 0);
                bottomNavigationBar.selectNavaigationPostion(integer);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        if (hasReadResultObservable == null) {
            hasReadResultObservable = RxBus.get().register(RxConstant.REFRUSH_UNREADER_NUMBER_RESULT_OBSERVABLE, Boolean.class);
            hasReadResultObservable.subscribe(new RxSubscriber<Boolean>() {
                @Override
                protected void onEvent(Boolean booleanValue) {
                    System.out.println("----------unreadiNfo=---booleanValue" + booleanValue + "----getUnreadNoticeInfoCount=" + getUnreadNoticeInfoCount());
                    if (booleanValue) {
//                        RxBus.get().post(RxConstant.UNREAD_MESSAGE_OBSERVABLE, hasUnreadNumber);
                        RxBus.get().post(RxConstant.UNREAD_MESSAGE_OBSERVABLE, getUnreadNoticeInfoCount() > 0);
                    }
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
        //公募用户信息
        if (null == publicFundInfObservable) {
            publicFundInfObservable = RxBus.get().register(RxConstant.REFRESH_PUBLIC_FUND_INFO, Integer.class);
            publicFundInfObservable.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer publicFundInf) {
                    if (10 == publicFundInf) {
                        //刷新
                        getPresenter().loadPublicFundInf();
                    }
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }

        if (null != goPublicBuyObservable) {
            goPublicBuyObservable = RxBus.get().register(RxConstant.MAIN_PUBLIC_TO_BUY, Integer.class);
            goPublicBuyObservable.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer publicFundInf) {
                    if (11 == publicFundInf) {
                        HashMap<String, Object> maps = ((BaseApplication) baseContext.getApplication()).getPublicBuyMaps();
                        if (null != maps)
                            NavigationUtils.startActivityByRouter(baseContext, RouteConfig.GOTO_PUBLIC_FUND_BUY, maps);
                    }
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
//        if (null == jumpWebIndex) {
//            jumpWebIndex = RxBus.get().register(RxConstant.JUMP_H5_INDEX, Integer.class);
//            jumpWebIndex.subscribe(new RxSubscriber<Integer>() {
//                @Override
//                protected void onEvent(Integer integer) {
//                    baseContext.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//
//                }
//
//                @Override
//                protected void onRxError(Throwable error) {
//
//                }
//            });
//        }


    }

    private int getUnreadNoticeInfoCount() {
        List<Conversation> list = RongIM.getInstance().getConversationList();
        int result = 0;
        if (!CollectionUtils.isEmpty(list)) {
            for (Conversation conversation : list) {
                if (RongCouldUtil.getInstance().customConversation(conversation.getTargetId()) || Constant.msgSystemStatus.equals(conversation.getSenderUserId()) ||
                        AppManager.getUserInfo(this).getToC().getBandingAdviserId().equals(conversation.getSenderUserId())) {
                    result += conversation.getUnreadMessageCount();
                }
            }
        }
        return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != liveTimerObservable) {
            liveTimerObservable.unsubscribe();
        }
    }

    private void toJumpTouziren(QrCodeBean qrCodeBean) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_title, "投资者认证");
        hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.qrcoderesult + "adviserId=" + qrCodeBean.getFatherId() + "&bindChannel=4");
        NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
    }

    private void initPlatformCustomer() {
        Log.e("MainPageActivity", "start checkKefu()");
        if (RongIMClient.getInstance() != null && !((InvestorAppli) InvestorAppli.getContext()).isRequestCustom()) {
            List<Conversation> conversationList = RongIMClient.getInstance().getConversationList();
            if (conversationList != null) {
                for (int i = 0; i < conversationList.size(); i++) {
                    if (conversationList.get(i).getTargetId().equals("dd0cc61140504258ab474b8f0a38bb56")) {
                        return;
                    }
                }
            }
            ApiClient.getPlatformCustomer(AppManager.getUserId(this)).subscribe(new RxSubscriber<CommonEntity.Result>() {
                @Override
                protected void onEvent(CommonEntity.Result result) {
                    List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
                    if (null != conversationList) {
                        Log.i("ConnectRongYun", "7 RongYun conversationList size= " + conversationList.size());
                    }
                    if (!((InvestorAppli) InvestorAppli.getContext()).isRequestCustom()) {
//                            EventBus.getDefault().post(new RefreshKefu());
                    }
                    ((InvestorAppli) InvestorAppli.getContext()).setRequestCustom(true);
                }

                @Override
                protected void onRxError(Throwable error) {
                    Log.e("MainPageActivity", "----platformcustomer=" + error.getMessage());
                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        initApplication.setMainpage(false);
        if (null != downDamicSoObservable) {
            RxBus.get().unregister(RxConstant.DOWN_DAMIC_SO, downDamicSoObservable);
        }
        if (null != liveTimerObservable) {
            liveTimerObservable.unsubscribe();
        }

        if (null != liveRefreshObservable) {
            RxBus.get().unregister(RxConstant.REFRESH_LIVE_DATA, liveRefreshObservable);
        }
        if (closeMainObservable != null) {
            RxBus.get().unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, closeMainObservable);
        }

        if (reRefrushUserInfoObservable != null) {
            RxBus.get().unregister(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, reRefrushUserInfoObservable);
        }

        if (rongTokenRefushObservable != null) {
            RxBus.get().unregister(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, rongTokenRefushObservable);
        }

        if (openMessageListObservable != null) {
            RxBus.get().unregister(RxConstant.OPEN_MESSAGE_LIST_PAGE_OBSERVABLE, openMessageListObservable);
        }

        if (twoCodeObservable != null) {
            RxBus.get().unregister(RxConstant.LOOK_TWO_CODE_OBSERVABLE, twoCodeObservable);
        }

        if (startConverstationObservable != null) {
            RxBus.get().unregister(RxConstant.START_CONVERSATION_OBSERVABLE, startConverstationObservable);
        }
        if (null != userLayObservable) {
            RxBus.get().unregister(RxConstant.MAIN_FRESH_LAY, userLayObservable);
        }
        if (null != showIndexObservable) {
            RxBus.get().unregister(RxConstant.INVERSTOR_MAIN_PAGE, showIndexObservable);
        }
        if (null != killObservable) {
            RxBus.get().unregister(RxConstant.MAIN_PAGE_KILL, killObservable);
        }
        if (null != hasReadResultObservable) {
            RxBus.get().unregister(RxConstant.REFRUSH_UNREADER_NUMBER_RESULT_OBSERVABLE, hasReadResultObservable);
            hasReadResultObservable = null;
        }
        if (null != publicFundInfObservable) {
            RxBus.get().unregister(RxConstant.REFRESH_PUBLIC_FUND_INFO, publicFundInfObservable);
            publicFundInfObservable = null;

        }
        if (null != goPublicBuyObservable) {
            RxBus.get().unregister(RxConstant.MAIN_PUBLIC_TO_BUY, goPublicBuyObservable);
            goPublicBuyObservable = null;

        }


        MainTabManager.getInstance().destory();
        FloatVideoService.stopService();
        zipResourceDownload.closeDilaog();
        if (isOnlyClose) {
            return;
        }
        AppInfStore.saveLastSetAndValidateTime(this, System.currentTimeMillis());
        locationManger.unregistLocation();
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    //    @OnClick(R.id.video_live_pop)
    public void joinLive() {
        if (liveJsonData != null) {
            Intent intent = new Intent(this, LiveActivity.class);
            intent.putExtra("liveJson", liveJsonData.toString());
            intent.putExtra("type", "");
            startActivity(intent);
            try {
                SPreference.putString(this, Contant.CUR_LIVE_ROOM_NUM, liveJsonData.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);//结束统计时长
        try {
            if (liveJsonData != null)
                SPreference.putString(this, Contant.CUR_LIVE_ROOM_NUM, liveJsonData.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (((BaseFragment) mContentFragment).onBackPressed(MainPageActivity.this)) {
            return;
        }
        exitBy2Click();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCourseFragment.REQUEST_BACK_CODE) {
            RxBus.get().post(RxConstant.COURSE_HEALTH_LIST_REFRUSH_OBSERVABLE, true);
        }
    }

    private void initDayTask() {
        getPresenter().initDayTask();
    }

    @Override
    public void loginLiveSucc() {
        liveTimerObservable = Observable.interval(0, 10000, TimeUnit.MILLISECONDS)
                //延时0 ，每间隔5000，时间单位
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        getPresenter().getProLiveList();
                    }
                });
    }

    @Override
    public void loginLiveFail() {
//        getPresenter().getLiveList();

    }

    @Override
    public void getLiveSignSuc(String sign) {
        profileInfoHelper.setMyNickName(AppManager.getUserInfo(this).getNickName());
        profileInfoHelper.setMyAvator(AppManager.getUserInfo(this).getHeadImageUrl());
        loginHelper.imLogin(AppManager.getUserId(this) + "_C", sign);
    }

    /**
     * @param liveState  0-->预告  1-->直播中  2-->无直播
     * @param jsonObject 有直播（预告） jsonObject不为空，无直播jsonObject为空
     */
    @Override
    public void hasLive(int liveState, JSONObject jsonObject) {
        Log.e("liveState", hasLive + "");
        if (jsonObject == null) {
            LiveInfBean liveInfBean = new LiveInfBean();
            liveInfBean.type = 2;
            RxBus.get().post(MainHomeFragment.LIVERXOBSERBER_TAG, liveInfBean);
            return;
        }
        try {
            if (jsonObject.getString("id").equals(SPreference.getString(this, Contant.CUR_LIVE_ROOM_NUM) + "")) {
                hasLive = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.hasLive = hasLive;
        RxBus.get().post(RxConstant.ZHIBO_STATUES, hasLive);
        if (bottomNavigationBar != null) {
//            bottomNavigationBar.setLive(hasLive);
        }
//        if (hasLive) {//有直播
        liveJsonData = jsonObject;
        LiveInfBean liveInfBean = new LiveInfBean();//new Gson().fromJson(liveJsonData.toString(), LiveInfBean.class);

        try {
            liveInfBean.title = jsonObject.getString("title");
            liveInfBean.image = jsonObject.getString("image");
            liveInfBean.content = jsonObject.getString("slogan");
            liveInfBean.id = jsonObject.getString("id");
            liveInfBean.jsonstr = jsonObject.toString();
            liveInfBean.type = liveState;
            liveInfBean.create_time = jsonObject.getString("createTime");
            liveInfBean.visitors = jsonObject.getString("visitors");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RxBus.get().post(MainHomeFragment.LIVERXOBSERBER_TAG, liveInfBean);


    }

    @Override
    public void signInSuc() {
        RxBus.get().post(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, true);
    }

    @Override
    public void toFreshUserinfHome() {
        RxBus.get().post(RxConstant.MAIN_FRESH_LAY, 5);
    }

    @Override
    public void loadSoSuccess(String filePath) {
        LogUtils.Log("aaa", "loadSoSuccess");
    }

    @Override
    public void loadSoError() {
        LogUtils.Log("aaa", "loadSoError");
    }

    private void SsetBottomNavigation() {
//        bottomNavigationBar.
    }


    @Override
    public void updateProfileInfo(TIMUserProfile profile) {

    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {

    }

    public void initLocation() {
        locationManger = LocationManger.getInstanceLocationManger(baseContext);
        locationManger.startLocation(new BdLocationListener() {
            @Override
            public void getLocation(LocationBean locationBean) {
                GrowingIO.getInstance().setGeoLocation(locationBean.getLocationlatitude(), locationBean.getLocationlatitude());
                locationManger.unregistLocation();
            }

            @Override
            public void getLocationerror() {
                locationManger.unregistLocation();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        /**
         * 解决重影问题  手机内存不足时候回
         */
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
    }

    private float getRatio() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        return (float) heightPixels / (float) widthPixels;
    }

}
