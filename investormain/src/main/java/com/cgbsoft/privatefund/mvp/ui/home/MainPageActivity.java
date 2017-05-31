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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.dialog.DownloadDialog;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MainPagePresenter;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.widget.navigation.BottomNavigationBar;
import com.chenenyu.router.annotation.Route;
import com.tencent.TIMUserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import app.privatefund.com.im.listener.MyConnectionStatusListener;
import app.privatefund.com.im.listener.MyConnectionStatusListener;
import app.privatefund.com.vido.service.FloatVideoService;
import app.privatefund.com.im.listener.MyGroupInfoListener;
import app.privatefund.com.im.listener.MyGroupMembersProvider;
import app.privatefund.com.im.listener.MyGroupUserInfoProvider;
import app.privatefund.com.im.listener.MyUserInfoListener;
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

@Route("investornmain_mainpageactivity")
public class MainPageActivity extends BaseActivity<MainPagePresenter> implements BottomNavigationBar.BottomClickListener, MainPageContract.View, LoginView,ProfileView{
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
    private Observable<Boolean> rongTokenRefushObservable;
    private Observable<Boolean> openMessageListObservable;
    private boolean isOnlyClose;
    private int currentResId;
    private JSONObject liveJsonData;
    private LoginHelper loginHelper;
    private ProfileInfoHelper profileInfoHelper;
    private Observable<Integer> showIndexObservable;

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
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        transaction.add(R.id.fl_main_content, mContentFragment);

        transaction.commitAllowingStateLoss();

        initRxObservable();

        loginLive();

        initDialog();

        initRongInterface();

        initDayTask();
    }

    /**
     * 初始化融云的接口信息
     */
    private void initRongInterface() {
        RongIM.setUserInfoProvider(new MyUserInfoListener(), false);
        RongIM.setGroupInfoProvider(new MyGroupInfoListener(), false);
        RongIM.setGroupUserInfoProvider(new MyGroupUserInfoProvider(this), false);
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
        bottomNavigationBar.setActivity(this);

        if (!SPreference.isThisRunOpenDownload(this))
            new DownloadDialog(this, true);
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

    private void todo() {
//        if (mContentFragment instanceof StarFragment) {
//            RxBus.get().post(RELEASE_STAR_OBSERVABLE, true);
//        }
    }

    @Override
    public void onCloudMenuClick(int position) {
        switch (position) {
            case 0://呼叫投资顾问

                break;
            case 1://对话

                break;
            case 2://直播

                break;
            case 3://短信

                break;
            case 4://客服

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            /**
             * 设置连接状态变化的监听器.
             */
            if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)
                RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int index = getIntent().getIntExtra("index", 0);
        onTabSelected(index);

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
//                if (1 == currentPostion && isHaveClickProduct) {
//                    MainTabManager.getInstance().getProductFragment().resetAllData();
//                }
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
    }

    private void initRxObservable() {
        closeMainObservable = RxBus.get().register(RxConstant.CLOSE_MAIN_OBSERVABLE, Boolean.class);
        closeMainObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                isOnlyClose = aBoolean;
                RongIM.getInstance().disconnect();
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

        rongTokenRefushObservable = RxBus.get().register(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, Boolean.class);
        rongTokenRefushObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                Log.i("MainPageActivity", String.valueOf(aBoolean));
                initPlatformCustomer();
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
    }

    private void initPlatformCustomer() {
            Log.e("MainPageActivity", "start checkKefu()");
            if (RongIMClient.getInstance() != null && !((InvestorAppli)InvestorAppli.getContext()).isRequestCustom()) {
                List<Conversation> conversationList =RongIMClient.getInstance().getConversationList();
                if (conversationList != null) {
                    for (int i = 0; i < conversationList.size(); i++) {
                        if (conversationList.get(i).getTargetId().equals("dd0cc61140504258ab474b8f0a38bb56")) {
                            return;
                        }
                    }
                }

                ApiClient.getTestGetPlatformCustomer(AppManager.getUserId(this)).subscribe(new RxSubscriber<String>() {
                    @Override
                    protected void onEvent(String s) {
                        List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
                        if (null != conversationList) {
                            Log.i("ConnectRongYun", "7 RongYun conversationList size= " + conversationList.size());
                        }
                        if (!((InvestorAppli)InvestorAppli.getContext()).isRequestCustom()) {
//                            EventBus.getDefault().post(new RefreshKefu());
                        }
                        ((InvestorAppli)InvestorAppli.getContext()).setRequestCustom(true);
                    }

                    @Override
                    protected void onRxError(Throwable error) {
                        Log.e("MainPageActivity", "----platformcustomer=" + error.getMessage());
                    }
                });
            }
    }

    private void gesturePasswordJumpPage() {
        if (SPreference.getToCBean(this) != null && "1".equals(SPreference.getToCBean(this).getGestureSwitch()) && mContentFragment == MainTabManager.getInstance().getFragmentByIndex(0)) {
            /*Intent intent = new Intent(context, GestureVerifyActivity.class);
            intent.putExtra(GestureVerifyActivity.FROM_EXCCEED_TIIME, true);
            context.startActivity(intent);*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (closeMainObservable != null) {
            RxBus.get().unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, closeMainObservable);
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

        MainTabManager.getInstance().destory();
        FloatVideoService.stopService();
        if (isOnlyClose) {
            return;
        }
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    @OnClick(R.id.video_live_pop)
    public void joinLive() {
        if (liveJsonData != null) {
            Intent intent = new Intent(this, LiveActivity.class);
            intent.putExtra("liveJson", liveJsonData.toString());
            startActivity(intent);

        }
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
        getPresenter().getLiveList();
    }

    @Override
    public void loginLiveFail() {
        getPresenter().getLiveList();
    }

    @Override
    public void getLiveSignSuc(String sign) {
        profileInfoHelper.setMyNickName(AppInfStore.getUserInfo(this).getNickName());
        profileInfoHelper.setMyAvator(AppInfStore.getUserInfo(this).getHeadImageUrl());
        loginHelper.imLogin(AppManager.getUserId(this), sign);
    }

    @Override
    public void hasLive(boolean hasLive, JSONObject jsonObject) {
        if (hasLive) {
            liveJsonData = jsonObject;
            liveDialog.setVisibility(View.VISIBLE);
            try {
                liveTitle.setText(jsonObject.getString("title"));
                Imageload.display(this, jsonObject.getString("image"), liveIcon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            liveJsonData = null;
            liveDialog.setVisibility(View.GONE);
        }

    }

    private void SsetBottomNavigation(){
//        bottomNavigationBar.
    }


    @Override
    public void updateProfileInfo(TIMUserProfile profile) {

    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {

    }
}
