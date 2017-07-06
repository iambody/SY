package com.cgbsoft.privatefund.mvp.ui.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.bean.ConversationBean;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.BdLocationListener;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LocationManger;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.DownloadDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.location.LocationBean;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainPagePresenter;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;
import com.chenenyu.router.annotation.Route;
import com.cn.hugo.android.scanner.QrCodeBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.TIMUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.privatefund.com.im.bean.SMMessage;
import app.privatefund.com.im.listener.MyGroupInfoListener;
import app.privatefund.com.im.listener.MyGroupMembersProvider;
import app.privatefund.com.im.listener.MyGroupUserInfoProvider;
import app.privatefund.com.im.listener.MyUserInfoListener;
import app.privatefund.com.im.utils.PushPreference;
import app.privatefund.com.im.utils.ReceiveInfoManager;
import app.privatefund.com.vido.service.FloatVideoService;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
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
    private FragmentManager mFragmentManager;
    private Fragment mContentFragment;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    @BindView(R.id.webView)
    BaseWebview baseWebview;

    @BindView(R.id.cmain_live_dialog)
    LinearLayout liveDialog;

    @BindView(R.id.video_live_close)
    ImageView liveDialogClose;

    @BindView(R.id.video_live_pop)
    RelativeLayout livePop;

    @BindView(R.id.live_head)
    ImageView liveIcon;

    @BindView(R.id.live_title)
    TextView liveTitle;

    private Observable<Boolean> closeMainObservable;
    private Observable<Boolean> gestruePwdObservable;
    private Observable<Boolean> reRefrushUserInfoObservable;
    private Observable<Boolean> rongTokenRefushObservable;
    private Observable<Boolean> openMessageListObservable;
    private Observable<QrCodeBean> twoCodeObservable;
    private Observable<ConversationBean> startConverstationObservable;
    private boolean isOnlyClose;
    private int currentResId;
    private JSONObject liveJsonData;
    private LoginHelper loginHelper;
    private ProfileInfoHelper profileInfoHelper;
    private Observable<Integer> showIndexObservable;
    private LocationManger locationManger;
    private Subscription liveTimerObservable;
    private boolean hasLive = false;

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
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        baseWebview.loadUrls(CwebNetConfig.pageInit);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        baseWebview.loadUrls(CwebNetConfig.pageInit);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Log.i("MainPageActivity", "----init");
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first);

        showIndexObservable = RxBus.get().register(RxConstant.INVERSTOR_MAIN_PAGE, Integer.class);
        showIndexObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                onTabSelected(integer);
                bottomNavigationBar.selectNavaigationPostion(integer);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        initActionPoint();

        transaction.add(R.id.fl_main_content, mContentFragment);

        transaction.commitAllowingStateLoss();

        initRxObservable();

        initUserInfo();

        loginLive();

        initDialog();

        initRongInterface();

        initDayTask();

//        initPlatformCustomer();

        showInfoDialog();

        autoSign();
        initLocation();

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
        if ("0".equals(AppManager.getUserInfo(this).getIsSingIn())) {
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

        //是否需要风险评测d 弹出框
//        if (TextUtils.isEmpty(AppManager.getUserInfo(baseContext).getToC().getCustomerType())) {
//            RiskEvaluatDialog riskEvaluatDialog = new RiskEvaluatDialog(baseContext);
//            riskEvaluatDialog.show();
//        }
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
        bottomNavigationBar.setActivity(this);

        if (!SPreference.isThisRunOpenDownload(this))
            new DownloadDialog(this, true, false);
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
//        if (mContentFragment instanceof StarFragment) {
//            RxBus.get().post(RELEASE_STAR_OBSERVABLE, true);
//        }
    }

    //    @Override
    public void onCloudMenuClick(int position) {
        switch (position) {
            case 0://呼叫投资顾问
                NavigationUtils.startDialgTelephone(this, AppManager.getUserInfo(this).getAdviserPhone());
                bottomNavigationBar.closeCloudeMenu();
                DataStatistApiParam.onStatisToCMenuCallCustom();
                break;
            case 1://对话
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, AppManager.getUserInfo(this).getToC().getBandingAdviserId(), AppManager.getUserInfo(this).getAdviserRealName());
                bottomNavigationBar.closeCloudeMenu();
                DataStatistApiParam.onStatisToCMenuCallDuihua();
                break;
            case 2://直播
                if (!hasLive) {
                    Intent i = new Intent(this, BaseWebViewActivity.class);
                    i.putExtra(WebViewConstant.push_message_url, CwebNetConfig.mineTouGu);
                    i.putExtra(WebViewConstant.push_message_title, "我的投顾");
                    startActivityForResult(i, 300);

                } else {
                    Intent intent = new Intent(this, LiveActivity.class);
                    intent.putExtra("liveJson", liveJsonData.toString());
                    startActivity(intent);
                }
                DataStatistApiParam.onStatisToCMenuZhibo();
                bottomNavigationBar.closeCloudeMenu();
                break;
            case 3://短信
                NavigationUtils.startDialogSendMessage(this, AppManager.getUserInfo(this).getAdviserPhone());
                bottomNavigationBar.closeCloudeMenu();
                DataStatistApiParam.onStatisToCMenuMessage();
                break;
            case 4://客服
                RongIM.getInstance().startPrivateChat(this, "dd0cc61140504258ab474b8f0a38bb56", "平台客服");
                bottomNavigationBar.closeCloudeMenu();
                DataStatistApiParam.onStatisToCMenuKefu();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initUserInfo();
//        int index = getIntent().getIntExtra("index", 0);
//        onTabSelected(index);

    }

    int switchID = -1;
    int currentPostion = -1;
    boolean isHaveClickProduct;

    @Override
    public void onTabSelected(int position) {
        int switchID = -1;
        switch (position) {
            case 0://左1
                switchID = R.id.nav_left_first;
                currentPostion = 0;
                break;
            case 1://左2
                switchID = R.id.nav_left_second;
                if (1 == position && isHaveClickProduct) {
                    MainTabManager.getInstance().getProductFragment().resetAllData();
                }
                isHaveClickProduct = true;
                currentPostion = 1;
                break;
            case 2://左3
                switchID = R.id.nav_right_first;
                currentPostion = 2;
                break;
            case 3://左4
                switchID = R.id.nav_right_second;
                currentPostion = 3;
                break;
            case 4://中间
                // getPresenter().toSignIn();
                switchID = R.id.nav_center;
                currentPostion = 4;
                break;
        }
        switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID));
        buryPoint(position);
    }

    /**
     * 埋点
     */
    private void buryPoint(int postion) {
        switch (postion) {
            case 0:
                DataStatistApiParam.onStatisToCTabMine();
                break;
            case 1:
                DataStatistApiParam.onStatisToCTabProduct();
                break;
            case 2:
                DataStatistApiParam.onStatisToCTabDiscover();
                break;
            case 3:
                DataStatistApiParam.onStatisToCTabClub();
                break;
            case 4:
                DataStatistApiParam.onStatisToCTabCloudKey();
                break;
        }
    }

    private void initRxObservable() {
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
                finish();
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        gestruePwdObservable = RxBus.get().register(RxConstant.ON_ACTIVITY_RESUME_OBSERVABLE, Boolean.class);
        gestruePwdObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (SPreference.getToCBean(MainPageActivity.this) != null && "1".equals(SPreference.getToCBean(MainPageActivity.this).getGestureSwitch())) {
                    gesturePasswordJumpPage();
                }
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
                    getPresenter().getUserInfo();
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
    }

    private void toJumpTouziren(QrCodeBean qrCodeBean) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_title, "投资者认证");
        hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.invistorCertify + "?advisorId=" + qrCodeBean.getFatherId());
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
//            ApiClient.getTestGetPlatformCustomer(AppManager.getUserId(this)).subscribe(new RxSubscriber<String>() {
//                @Override
//                protected void onEvent(String s) {
//                    List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
//                    if (null != conversationList) {
//                        Log.i("ConnectRongYun", "7 RongYun conversationList size= " + conversationList.size());
//                    }
//                    if (!((InvestorAppli) InvestorAppli.getContext()).isRequestCustom()) {
////                            EventBus.getDefault().post(new RefreshKefu());
//                    }
//                    ((InvestorAppli) InvestorAppli.getContext()).setRequestCustom(true);
//                }
//
//                @Override
//                protected void onRxError(Throwable error) {
//                    Log.e("MainPageActivity", "----platformcustomer=" + error.getMessage());
//                }
//            });
        }
    }

    private void gesturePasswordJumpPage() {
        System.out.println("------intercetpergesturePasswordJumpPage");
        if (SPreference.getToCBean(this) != null && "1".equals(SPreference.getToCBean(this).getGestureSwitch())) {
            Intent intent = new Intent(this, GestureVerifyActivity.class);
            intent.putExtra(GestureVerifyActivity.FROM_EXCCEED_TIIME, true);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != liveTimerObservable) {
            liveTimerObservable.unsubscribe();
        }

        if (closeMainObservable != null) {
            RxBus.get().unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, closeMainObservable);
        }

        if (reRefrushUserInfoObservable != null) {
            RxBus.get().unregister(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, reRefrushUserInfoObservable);
        }

        if (gestruePwdObservable != null) {
            RxBus.get().unregister(RxConstant.ON_ACTIVITY_RESUME_OBSERVABLE, gestruePwdObservable);
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


        MainTabManager.getInstance().destory();
        FloatVideoService.stopService();
        if (isOnlyClose) {
            return;
        }
        AppInfStore.saveLastExitTime(this, System.currentTimeMillis());
        locationManger.unregistLocation();
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    @OnClick(R.id.video_live_pop)
    public void joinLive() {

        if (liveJsonData != null) {
            liveDialog.setVisibility(View.GONE);
//            liveDialog.clearAnimation();
            startOrOverAnimator(false);
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
        try {
            if (liveJsonData != null)
                SPreference.putString(this, Contant.CUR_LIVE_ROOM_NUM, liveJsonData.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.video_live_close)
    public void closeLiveDialog() {
        liveDialog.setVisibility(View.GONE);
        startOrOverAnimator(false);
    }

    @Override
    public void onBackPressed() {
        if (1 == currentPostion && MainTabManager.getInstance().getProductFragment().isShow()) {
            MainTabManager.getInstance().getProductFragment().backClick();
        } else
            exitBy2Click();
    }


    private void initDayTask() {
        getPresenter().initDayTask();
    }

    @Override
    public void loginLiveSucc() {
        liveDialog.setVisibility(View.VISIBLE);
        startOrOverAnimator(true);
        liveTimerObservable = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
                //延时3000 ，每间隔3000，时间单位
                .compose(this.<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        getPresenter().getLiveList();
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

    @Override
    public void hasLive(boolean hasLive, JSONObject jsonObject) {
        Log.e("liveState", hasLive + "");

        try {
            if ((SPreference.getString(this, Contant.CUR_LIVE_ROOM_NUM) + "").equals(jsonObject.getString("id"))) {
                hasLive = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.hasLive = hasLive;
        RxBus.get().post(RxConstant.ZHIBO_STATUES, hasLive);
        if (bottomNavigationBar != null) {
            bottomNavigationBar.setLive(hasLive);
        }
        if (hasLive) {
            liveJsonData = jsonObject;
            liveDialog.setVisibility(View.VISIBLE);
//            Animation animation = AnimationUtils.loadAnimation(
//                    this, R.anim.live_dialog_anim);
//            liveDialog.startAnimation(animation);
            startOrOverAnimator(true);
            try {
                liveTitle.setText(jsonObject.getString("title"));
                Imageload.display(this, jsonObject.getString("image"), liveIcon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            liveJsonData = null;
            liveDialog.setVisibility(View.GONE);
//            liveDialog.clearAnimation();
            startOrOverAnimator(false);
        }
//        liveDialog.setVisibility(View.VISIBLE);
//        startOrOverAnimator(true);
    }

    ObjectAnimator liveAnimator;

    public void startOrOverAnimator(boolean isOnlyClose) {
        if (isOnlyClose) {
            if(null==liveAnimator)
            liveAnimator = ObjectAnimator.ofFloat(liveDialog, "translationY", 0f, 50.0f, 0f);

            liveAnimator.setRepeatCount(ValueAnimator.INFINITE);
            liveAnimator.setDuration(1*1000);
            liveAnimator.start();
        } else {
            if (null == liveAnimator) return;
            liveAnimator.end();
        }
    }

    @Override
    public void signInSuc() {
        RxBus.get().post(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, true);
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
        LocationBean a = AppManager.getLocation(baseContext);
        locationManger = LocationManger.getInstanceLocationManger(baseContext);
        locationManger.startLocation(new BdLocationListener() {
            @Override
            public void getLocation(LocationBean locationBean) {

//                PromptManager.ShowCustomToast(baseContext,"定位成功");
                locationManger.unregistLocation();
//                LogUtils.Log("S", "s");
//                LogUtils.Log("location", "定位成功 城市：" + null == locationBean.getLocationcity() ? "空" : locationBean.getLocationcity());

            }

            @Override
            public void getLocationerror() {
//                LogUtils.Log("location", "定位失败");
            }
        });

    }
}
