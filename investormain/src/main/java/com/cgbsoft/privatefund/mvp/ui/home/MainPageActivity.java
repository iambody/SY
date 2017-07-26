package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.model.CommonEntity;
import com.cgbsoft.lib.base.model.bean.ConversationBean;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.BdLocationListener;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LocationManger;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.dialog.DownloadDialog;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.LiveInfBean;
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

import app.ndk.com.enter.mvp.ui.LoginActivity;
import app.privatefund.com.im.bean.SMMessage;
import app.privatefund.com.im.listener.MyGroupInfoListener;
import app.privatefund.com.im.listener.MyGroupMembersProvider;
import app.privatefund.com.im.listener.MyGroupUserInfoProvider;
import app.privatefund.com.im.listener.MyUserInfoListener;
import app.privatefund.com.im.utils.PushPreference;
import app.privatefund.com.im.utils.ReceiveInfoManager;
import app.privatefund.com.vido.service.FloatVideoService;
import butterknife.BindView;
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
//
//    @BindView(R.id.cmain_live_dialog)
//    LinearLayout liveDialog;

//    @BindView(R.id.video_live_close)
//    ImageView liveDialogClose;
//
//    @BindView(R.id.video_live_pop)
//    RelativeLayout livePop;

//    @BindView(R.id.live_head)
//    ImageView liveIcon;
//
//    @BindView(R.id.live_title)
//    TextView liveTitle;

    private Observable<Boolean> closeMainObservable;
    //    private Observable<Boolean> gestruePwdObservable;
    private Observable<Boolean> reRefrushUserInfoObservable;
    private Observable<Boolean> rongTokenRefushObservable;
    private Observable<Boolean> openMessageListObservable;
    private Observable<QrCodeBean> twoCodeObservable;
    private Observable<Integer> jumpIndexObservable;
    private Observable<ConversationBean> startConverstationObservable;
    private boolean isOnlyClose;
    private int currentResId;
    private JSONObject liveJsonData;
    private LoginHelper loginHelper;
    private ProfileInfoHelper profileInfoHelper;
    private Observable<Integer> showIndexObservable, freshWebObservable, userLayObservable;

    private LocationManger locationManger;
    private Subscription liveTimerObservable;
    private boolean hasLive = false;
    private int code;

    private InvestorAppli initApplication;

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
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        baseWebview.loadUrls(CwebNetConfig.pageInit);
        if (AppManager.isVisitor(baseContext) && 4 == currentPostion) {//是游客模式
            switchID = R.id.nav_left_first;
            currentPostion = 0;
            bottomNavigationBar.selectNavaigationPostion(0);
            switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
        }

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Log.i("MainPageActivity", "----init");
        initApplication = (InitApplication) getApplication();
        initApplication.setMainpage(true);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_left_first, code);

        code = getIntent().getIntExtra("code", 0);


//        initActionPoint();

        transaction.add(R.id.fl_main_content, mContentFragment);

        transaction.commitAllowingStateLoss();

        initRxObservable();

        initUserInfo();

//        initPlatformCustomer();

        showInfoDialog();


        initIndex(code);

        initLocation();

        //游客模式下禁止的Api 添加限制条件
        if (!AppManager.isVisitor(baseContext)) {
            loginLive();
            autoSign();
            initDayTask();
            initRongInterface();
        }
        RxBus.get().post(RxConstant.LOGIN_KILL, 1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        code = getIntent().getIntExtra("code", 0);
        initIndex(code);
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

        if (!SPreference.isThisRunOpenDownload(this))
            new DownloadDialog(this, true, false);
//        downloadDialog.show();

//        SignBean bean=new SignBean();
//        bean.resultMessage="ss";
//        bean.resultCode="2";
//        bean.coinNum=4;
//        HomeSignDialog homeSignDialog=new HomeSignDialog(baseContext,bean);
//        homeSignDialog.show();
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
    public void onTabSelected(int position, int code) {
        int switchID = -1;
        switch (position) {
            case 0://左1
                switchID = R.id.nav_left_first;
                currentPostion = 0;
                break;
            case 1://左2
                switchID = R.id.nav_left_second;
                currentPostion = 1;
                break;
            case 3://左3
                switchID = R.id.nav_right_first;
                currentPostion = 3;
                break;
            case 4://左4

                currentPostion = 4;
                if (AppManager.isVisitor(InitApplication.getContext())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
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
        jumpIndexObservable = RxBus.get().register(RxConstant.JUMP_INDEX, Integer.class);
        jumpIndexObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                initIndex(integer);
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
                switchID = R.id.nav_left_first;
                currentPostion = 0;
                bottomNavigationBar.selectNavaigationPostion(0);
                switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID, code));
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
//        //刷新webview的信息配置
//        freshWebObservable= RxBus.get().register(RxConstant.MAIN_FRESH_WEB_CONFIG, Integer.class);
//        freshWebObservable.subscribe(new RxSubscriber<Integer>() {
//            @Override
//            protected void onEvent(Integer integer) {
//                LogUtils.Log("JavaScriptObjectToc","开始调用");
//
////                baseWebview.reload();
//
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//
//            }
//        });
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
        if (null != liveTimerObservable) {
            liveTimerObservable.unsubscribe();
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
        MainTabManager.getInstance().destory();
        FloatVideoService.stopService();
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
//            liveDialog.setVisibility(View.GONE);
//            liveDialog.clearAnimation();
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
//
//    @OnClick(R.id.video_live_close)
//    public void closeLiveDialog() {
////        liveDialog.setVisibility(View.GONE);
//    }

    @Override
    public void onBackPressed() {
//        if (1 == currentPostion && MainTabManager.getInstance().getProductFragment().isShow()) {
//            MainTabManager.getInstance().getProductFragment().backClick();
//        } else
        exitBy2Click();
    }


    private void initDayTask() {
        getPresenter().initDayTask();
    }

    @Override
    public void loginLiveSucc() {
        liveTimerObservable = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
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
     *
     * @param liveState 0-->预告  1-->直播中  2-->无直播
     * @param jsonObject  有直播（预告） jsonObject不为空，无直播jsonObject为空
     */
    @Override
    public void hasLive(int liveState, JSONObject jsonObject) {
        Log.e("liveState", hasLive + "");
        if (jsonObject == null) {
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
        if (hasLive) {//有直播
            liveJsonData = jsonObject;
            LiveInfBean liveInfBean = new Gson().fromJson(liveJsonData.toString(), LiveInfBean.class);
            liveInfBean.isLiveing = true;
            RxBus.get().post(MainHomeFragment.LIVERXOBSERBER_TAG, liveInfBean);
//            liveDialog.setVisibility(View.VISIBLE);
//            Animation animation = AnimationUtils.loadAnimation(
//                    this, R.anim.live_dialog_anim);
//            liveDialog.startAnimation(animation);
//            try {
//                liveTitle.setText(jsonObject.getString("title"));
//                Imageload.display(this, jsonObject.getString("image"), liveIcon);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } else {
            LiveInfBean liveInfBeanerr = new LiveInfBean();
            liveInfBeanerr.isLiveing = false;
            RxBus.get().post(MainHomeFragment.LIVERXOBSERBER_TAG, liveInfBeanerr);
//            liveJsonData = null;
//            liveDialog.setVisibility(View.GONE);
//            liveDialog.clearAnimation();
        }

    }

    @Override
    public void signInSuc() {
        RxBus.get().post(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, true);
    }

    @Override
    public void toFreshUserinfHome() {
        RxBus.get().post(RxConstant.MAIN_FRESH_LAY, 5);
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
