package qcloud.liveold.mvp.views;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.chenenyu.router.annotation.Route;
import com.lidroid.xutils.BitmapUtils;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.av.TIMAvManager;
import com.tencent.av.sdk.AVView;
import com.tencent.av.utils.PhoneStatusTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import qcloud.liveold.R;
import qcloud.liveold.Util;
import qcloud.liveold.mvp.adapters.ChatMsgListAdapter;
import qcloud.liveold.mvp.adapters.MembersAdapter;
import qcloud.liveold.mvp.avcontrollers.QavsdkControl;
import qcloud.liveold.mvp.model.ChatEntity;
import qcloud.liveold.mvp.model.CurLiveInfo;
import qcloud.liveold.mvp.model.LiveInfoJson;
import qcloud.liveold.mvp.model.MemberInfo;
import qcloud.liveold.mvp.model.MySelfInfo;
import qcloud.liveold.mvp.presenters.EnterLiveHelper;
import qcloud.liveold.mvp.presenters.LiveContract;
import qcloud.liveold.mvp.presenters.LiveHelper;
import qcloud.liveold.mvp.presenters.LiveListViewHelper;
import qcloud.liveold.mvp.presenters.LivePresenter;
import qcloud.liveold.mvp.presenters.viewinface.EnterQuiteRoomView;
import qcloud.liveold.mvp.presenters.viewinface.LiveListView;
import qcloud.liveold.mvp.presenters.viewinface.LiveView;
import qcloud.liveold.mvp.presenters.viewinface.ProfileView;
import qcloud.liveold.mvp.utils.Constants;
import qcloud.liveold.mvp.utils.GlideCircleTransform;
import qcloud.liveold.mvp.utils.LogConstants;
import qcloud.liveold.mvp.utils.SxbLog;
import qcloud.liveold.mvp.utils.UIUtils;
import rx.Observable;


/**
 * Live直播类
 */
@Route(RouteConfig.GOTOLIVE)
public class LiveActivity extends BaseActivity<LivePresenter> implements EnterQuiteRoomView, LiveView, View.OnClickListener, ProfileView, QavsdkControl.onSlideListener, LiveListView, LiveContract.view {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private static final int GETPROFILE_JOIN = 0x200;

    private EnterLiveHelper mEnterRoomHelper;
    private LiveListViewHelper mLiveListViewHelper;
    private LiveHelper mLiveHelper;

    private ArrayList<ChatEntity> mArrayListChatEntity;
    private ChatMsgListAdapter mChatMsgListAdapter;
    private static final int MINFRESHINTERVAL = 500;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private static final int TIMEOUT_INVITE = 2;
    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = false;
    private final Timer mTimer = new Timer();
    private ArrayList<ChatEntity> mTmpChatList = new ArrayList<ChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;
    private static final int REFRESH_LISTVIEW = 5;
    private Dialog mMemberDg, closeCfmDg, inviteDg;
    private TextView mLikeTv;
    private HeartBeatTask mHeartBeatTask;//心跳
    private ImageView mHeadIcon;
    private TextView mHostNameTv;
    private LinearLayout mHostLayout, mHostLeaveLayout;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private long mSecond = 0;
    private String formatTime;
    private Timer mHearBeatTimer, mVideoTimer, mHeartClickTimer;
    private VideoTimerTask mVideoTimerTask;//计时器
    private TextView mVideoTime;
    private ObjectAnimator mObjAnim;
    private ImageView mRecordBall;
    private int thumbUp = 0;
    private long admireTime = 0;
    private int watchCount = 0;
    private static boolean mBeatuy = false;
    private static boolean mWhite = true;
    private boolean bCleanMode = false;
    private boolean mProfile;
    private boolean bFirstRender = true;
    private boolean bInAvRoom = false, bSlideUp = false, bDelayQuit = false;
    private boolean bReadyToChange = false;       // 正在切换房间
    private boolean isScreenShare = false;
    private String backGroundId;
    private InputMethodManager mInputKeyBoard;
    private boolean isOpenMenu = false;

    private TextView tvMembers;
    private TextView tvAdmires;

    private Dialog mDetailDialog;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<MemberInfo> mMemberList = new ArrayList<>();

    private ArrayList<String> mRenderUserList = new ArrayList<>();
    private RecyclerView memberList;
    private MembersAdapter membersAdapter;
    private Observable<String> getLiveObservable;


    @Override
    protected int layoutID() {
        return R.layout.av_activity;
    }

    @Override
    protected void before() {
        super.before();
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        RxBus.get().post(RxConstant.SCHOOL_VIDEO_PAUSE, true);
        initCurInfo();
    }

    private void initCurInfo() {
        String liveJsonStr = getIntent().getStringExtra("liveJson");
        JSONObject liveJson = null;
        try {
            
            liveJson = new JSONObject(liveJsonStr);
            MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
            MySelfInfo.getInstance().setJoinRoomWay(false);
            CurLiveInfo.setHostID(liveJson.getString("userId"));
            CurLiveInfo.setHostName(liveJson.getString("nickName"));
            CurLiveInfo.setHostAvator(liveJson.getString("headPortrait"));
            CurLiveInfo.setRoomNum(Integer.parseInt(liveJson.getString("id")));
            CurLiveInfo.setMembers(0);
            CurLiveInfo.setAdmires(11);
            CurLiveInfo.setIsShare(Integer.parseInt(liveJson.getString("isShare")));
            CurLiveInfo.setChatId(liveJson.getString("chat"));
            CurLiveInfo.setSlogan(liveJson.getString("slogan"));
            CurLiveInfo.setAllowChat(liveJson.getInt("allowChat"));
            CurLiveInfo.setEquipment(liveJson.getInt("equipment"));
            CurLiveInfo.setShareUrl(liveJson.getString("shareUrl"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        checkPermission();
        //进出房间的协助类
        mEnterRoomHelper = new EnterLiveHelper(this, this);
        //房间内的交互协助类
        mLiveHelper = new LiveHelper(this, this);

        mLiveListViewHelper = new LiveListViewHelper(this);

        initView();
        registerReceiver();
        backGroundId = CurLiveInfo.getHostID();
        //进入房间流程
        mEnterRoomHelper.startEnterRoom();

//        QavsdkControl.getInstance().setCameraPreviewChangeCallback();
        mLiveHelper.setCameraPreviewChangeCallback();
        registerOrientationListener();
        startOrientationListener();
        notificServer();
        initChatList();
//        ToolsUtils.serviceStop(this);
        checkRotation();
    }

    @Override
    protected LivePresenter createPresenter() {
        return new LivePresenter(this, this);
    }

    private int screenChange;

    private void checkRotation() {
        screenChange = 0;
        if (Build.VERSION.SDK_INT < 23) {
            try {
                screenChange = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
                if (screenChange == 1) {
                    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void initChatList() {
        mArrayListChatEntity = new ArrayList<ChatEntity>();
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setContext("<font color='#ffde00'>平台公告&盈泰财富云本着“连接人与财富”的理念在财富管理领域独创了B2B2C的商业模式，服务于资产管理机构、财富管理机构和高净值客户。</font>");
        chatEntity.setSenderName(SPreference.getString(LiveActivity.this, "liveHostId"));
        chatEntity.setSendId(SPreference.getString(LiveActivity.this, "liveHostId"));
        Log.d(TAG, "showTextMessage  isSelf " + true);
        mArrayListChatEntity.add(chatEntity);

        refreshChatAdapter();
    }

    private void refreshChatAdapter() {
        if (mArrayListChatEntity.size() > 30) {
            mArrayListChatEntity.remove(0);
        }
        if (mChatMsgListAdapter != null) {
            mChatMsgListAdapter.notifyDataSetChanged();
        } else {
            mChatMsgListAdapter = new ChatMsgListAdapter(LiveActivity.this, mArrayListChatEntity);
            mListViewMsgItems.setAdapter(mChatMsgListAdapter);
        }
        if (mArrayListChatEntity.size() > 1) {
            mListViewMsgItems.setSelection(mArrayListChatEntity.size() - 1);
        }
    }


    private void notificServer() {
        if (MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
            memberjoinRoom();
        }
    }

    private int total = 0;

    private void memberjoinRoom() {
        getPresenter().memberJoinRoom(CurLiveInfo.getRoomNum() + "", AppManager.getUserId(BaseApplication.getContext()));
    }

    private long offsetOrderId = 0;

    private void loadMemberData() {
        SPreference.putString(LiveActivity.this, "LiveRoomId", CurLiveInfo.getRoomNum() + "");
        getPresenter().getMemberList(
                CurLiveInfo.getRoomNum() + "",
                AppManager.getUserId(BaseApplication.getContext()),
                offsetOrderId,
                CurLiveInfo.getJoinTime()
        );

    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDAT_WALL_TIME_TIMER_TASK:
                    updateWallTime();
                    break;
                case REFRESH_LISTVIEW:
                    doRefreshListView();
                    break;
                case TIMEOUT_INVITE:
                    String id = "" + msg.obj;
                    cancelInviteView(id);
                    mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_HOST_CANCELINVITE, id);
                    break;
            }
            return false;
        }
    });

    /**
     * 时间格式化
     */
    private void updateWallTime() {
        String hs, ms, ss;

        long h, m, s;
        h = mSecond / 3600;
        m = (mSecond % 3600) / 60;
        s = (mSecond % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }

        if (Constants.HOST == MySelfInfo.getInstance().getIdStatus() && null != mVideoTime) {
            SxbLog.i(TAG, " refresh time ");
            mVideoTime.setText(formatTime);
        }
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //AvSurfaceView 初始化成功
            if (action.equals(Constants.ACTION_SURFACE_CREATED)) {
                //打开摄像头
                if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
                    mLiveHelper.openCameraAndMic();
                }
            }

            if (action.equals(Constants.ACTION_CAMERA_OPEN_IN_LIVE)) {//有人打开摄像头
                isScreenShare = false;
                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
                //如果是自己本地直接渲染
                for (String id : ids) {
                    if (!mRenderUserList.contains(id)) {
                        mRenderUserList.add(id);
                    }
                    updateHostLeaveLayout();

                    if (id.equals(MySelfInfo.getInstance().getId())) {
                        showVideoView(true, id);
                        return;
//                        ids.remove(id);
                    }
                }
                //其他人一并获取
                SxbLog.d(TAG, LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "somebody open camera,need req data"
                        + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "ids " + ids.toString());
                int requestCount = CurLiveInfo.getCurrentRequestCount();
                mLiveHelper.requestViewList(ids);
                requestCount = requestCount + ids.size();
                CurLiveInfo.setCurrentRequestCount(requestCount);
//                }
            }

            if (action.equals(Constants.ACTION_SCREEN_SHARE_IN_LIVE)) {//有屏幕分享
                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
                //如果是自己本地直接渲染
                for (String id : ids) {
                    if (!mRenderUserList.contains(id)) {
                        mRenderUserList.add(id);
                    }
                    updateHostLeaveLayout();

                    if (id.equals(MySelfInfo.getInstance().getId())) {
                        showVideoView(true, id);
                        return;
//                        ids.remove(id);
                    }
                }
                //其他人一并获取
                SxbLog.d(TAG, LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "somebody open camera,need req data"
                        + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "ids " + ids.toString());
                int requestCount = CurLiveInfo.getCurrentRequestCount();
                mLiveHelper.requestScreenViewList(ids);
                isScreenShare = true;
                requestCount = requestCount + ids.size();
                CurLiveInfo.setCurrentRequestCount(requestCount);
//                }
            }

            if (action.equals(Constants.ACTION_CAMERA_CLOSE_IN_LIVE)) {//有人关闭摄像头
                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
                //如果是自己本地直接渲染
                for (String id : ids) {
                    mRenderUserList.remove(id);
                }
                updateHostLeaveLayout();
            }

            if (action.equals(Constants.ACTION_SWITCH_VIDEO)) {//点击成员回调
                backGroundId = intent.getStringExtra(Constants.EXTRA_IDENTIFIER);
                SxbLog.v(TAG, "switch video enter with id:" + backGroundId);

                updateHostLeaveLayout();

                if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {//自己是主播
                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
                        mHostCtrView.setVisibility(View.VISIBLE);
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                    } else {//背景是其他成员
                        mHostCtrView.setVisibility(View.INVISIBLE);
                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                    }
                } else {//自己成员方式
                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                        mNomalMemberCtrView.setVisibility(View.INVISIBLE);
                    } else if (backGroundId.equals(CurLiveInfo.getHostID())) {//主播自己
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                        mNomalMemberCtrView.setVisibility(View.VISIBLE);
                    } else {
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                        mNomalMemberCtrView.setVisibility(View.INVISIBLE);
                    }

                }

            }
            if (action.equals(Constants.ACTION_HOST_LEAVE)) {//主播结束
                quiteLivePassively();
            }


            if (action.equals(Util.ACTION_LIVE_RECEVER_MSG)) {
                String text = intent.getStringExtra(Util.ACTION_LIVE_SEND_CONTENT);
                String senderId = intent.getStringExtra(Util.ACTION_LIVE_SEND_ID);
                ChatEntity entity = new ChatEntity();
                entity.setContext(text);
                entity.setSenderName("asd");
                entity.setSendId(senderId);

                Log.d(TAG, "showTextMessage  isSelf " + true);
                if (!text.equals(mArrayListChatEntity.get(mArrayListChatEntity.size() - 1).getContext())) {
                    mArrayListChatEntity.add(entity);
                    refreshChatAdapter();
                }
            }

            if (action.equals(Util.ACTION_LIVE_SEND_MSG)) {
                String extra = intent.getStringExtra(Util.ACTION_LIVE_SEND_CONTENT);
                try {
                    JSONObject json = new JSONObject(extra);
                    String room_id = json.getString("roomId");
                    String message = json.getString("message");
                    String nickName = json.getString("nickName");
//                    if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
//                        TIMTextElem elem = new TIMTextElem();
//                        elem.setText(nickName + "&" + message);
//                        ChatEntity entity = new ChatEntity();
//                        entity.setElem(elem);
//                        entity.setIsSelf(true);
//                        entity.setSenderName(nickName);
//                        Log.d(TAG, "showTextMessage  isSelf " + true);
//                        mArrayListChatEntity.add(entity);
//                        if (mArrayListChatEntity.size() > 30) {
//                            mArrayListChatEntity.remove(0);
//                        }
//                        mChatMsgListAdapter.notifyDataSetChanged();
//                        mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
//                    }
                    if (String.valueOf(CurLiveInfo.getRoomNum()).equals(room_id)) {
                        sendMsg(nickName + "&" + message);
                    }

//                    RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "0003fce75cd122ceaf1ac2d721a5f78e");
//                    RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "0003fce75cd122ceaf1ac2d721a5f78e");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        getLiveObservable = RxBus.get().register(RxConstant.GET_LIVE_PDF_LIST_TASK, String.class);
        getLiveObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getPresenter().getLivePdf(CurLiveInfo.getRoomNum() + "");
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });


    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_SURFACE_CREATED);
        intentFilter.addAction(Constants.ACTION_HOST_ENTER);
        intentFilter.addAction(Constants.ACTION_CAMERA_OPEN_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_CAMERA_CLOSE_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_SWITCH_VIDEO);
        intentFilter.addAction(Constants.ACTION_HOST_LEAVE);
        intentFilter.addAction(Util.ACTION_LIVE_RECEVER_MSG);
        intentFilter.addAction(Constants.ACTION_SCREEN_SHARE_IN_LIVE);
        intentFilter.addAction(Util.ACTION_LIVE_SEND_MSG);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    private void unregisterReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 初始化UI
     */
    private View avView;
    private TextView BtnBack, BtnInput, Btnflash, BtnSwitch, BtnBeauty, BtnWhite, BtnMic, BtnScreen, BtnHeart, BtnNormal, mVideoChat, BtnCtrlVideo, BtnCtrlMic, BtnHungup;
    private TextView inviteView1, inviteView2, inviteView3;
    private ListView mListViewMsgItems;
    private LinearLayout mHostCtrView, mNomalMemberCtrView, mVideoMemberCtrlView;
    private FrameLayout mFullControllerUi, mBackgound;
    //    private SeekBar mBeautyBar;
    private int mBeautyRate, mWhiteRate;
    private TextView pushBtn, recordBtn, speedBtn;

    private void showHeadIcon(ImageView view, String avatar) {
        if (TextUtils.isEmpty(avatar)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logoshare);
            Bitmap cirBitMap = UIUtils.createCircleImage(bitmap, 0);
            view.setImageBitmap(cirBitMap);
        } else {
            SxbLog.d(TAG, "load icon: " + avatar);
//            if (isDestroyed() == true) return;
            RequestManager req = Glide.with(this);
            req.load(avatar).transform(new GlideCircleTransform(this)).into(view);
        }
    }

    private void updateHostLeaveLayout() {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            return;
        } else {
            // 退出房间或主屏为主播且无主播画面显示主播已离开
//            if (!bInAvRoom || (CurLiveInfo.getHostID().equals(backGroundId) && !mRenderUserList.contains(backGroundId))) {
//                mHostLeaveLayout.setVisibility(View.VISIBLE);
//            } else {
//                mHostLeaveLayout.setVisibility(View.GONE);
//            }
        }
    }


    private List<RelativeLayout> viewList;
    private ViewPager pager;
    private RelativeLayout view1, view2;
    private LinearLayout bottomLayout, menuLayout, inputLayout, praiseLayout;
    private ImageView hostHead, mVideoHead1, mVideoHead2, mVideoHead3, shareLive;
    private TextView mButtonMute, mButtonBeauty, mClockTextView, mPraiseNum, mMemberListButton, tvShowTips;
    private ImageButton openMenu, pdfBtn, sendImg, mButtonPraise;
    private EditText mEditTextInputMs, mEditTextInputMsg, editCommit;
    private FrameLayout mBottomBar, mInviteMastk1, mInviteMastk2, mInviteMastk3;
    private Button mButtonSendMsg;

    /**
     * 初始化界面
     */
    private void initView() {
        pager = (ViewPager) findViewById(R.id.live_pager);
        LayoutInflater inflater = getLayoutInflater();
        viewList = new ArrayList<>();
        view1 = (RelativeLayout) inflater.inflate(R.layout.live_layout1, null);
        view2 = (RelativeLayout) inflater.inflate(R.layout.live_layout2, null);
        ImageButton close2 = (ImageButton) view2.findViewById(R.id.close2);
        close2.setOnClickListener(this);
        viewList.add(view1);
        viewList.add(view2);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));

                return viewList.get(position);
            }
        };

        pager.setAdapter(pagerAdapter);
        TextView mButtonSwitchCamera;
        bottomLayout = (LinearLayout) view1.findViewById(R.id.bottom_layout);
        hostHead = (ImageView) view1.findViewById(R.id.host_head);
        mButtonMute = (TextView) view1.findViewById(R.id.mic_btn);
        shareLive = (ImageView) view1.findViewById(R.id.live_share_btn);
        mButtonBeauty = (TextView) view1.findViewById(R.id.beauty_btn);
        openMenu = (ImageButton) view1.findViewById(R.id.open_menu);
        pdfBtn = (ImageButton) view1.findViewById(R.id.pdf_btn);
//        mButtonBeauty.setOnClickListener(this);
        mButtonSwitchCamera = (TextView) view1.findViewById(R.id.qav_topbar_switchcamera);
        mListViewMsgItems = (ListView) view1.findViewById(R.id.comment_list);
        mEditTextInputMsg = (EditText) view1.findViewById(R.id.qav_bottombar_msg_input);
        mBottomBar = (FrameLayout) view1.findViewById(R.id.qav_bottom_bar);
        menuLayout = (LinearLayout) view1.findViewById(R.id.menu_layout);
        editCommit = (EditText) view1.findViewById(R.id.edit_comment);
        inputLayout = (LinearLayout) view1.findViewById(R.id.live_input_layout);
        sendImg = (ImageButton) view1.findViewById(R.id.send_comment);
        memberList = (RecyclerView) view1.findViewById(R.id.member_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        memberList.setLayoutManager(linearLayoutManager);
        sendImg.setOnClickListener(this);
        openMenu.setOnClickListener(this);
        pdfBtn.setOnClickListener(this);
        shareLive.setOnClickListener(this);
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.display(hostHead, SPreference.getString(LiveActivity.this, "liveHostUrl"));

        if ("1".equals(CurLiveInfo.isShare + "")) {
            shareLive.setVisibility(View.VISIBLE);
        } else {
            shareLive.setVisibility(View.INVISIBLE);
        }
        Imageload.display(this, CurLiveInfo.getHostAvator(), hostHead);

//        hostHead
        hostHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEdit();
                new LiveUserInfoDialog(LiveActivity.this, CurLiveInfo.getHostAvator(), CurLiveInfo.getHostName()) {
                    @Override
                    public void left() {
                        this.dismiss();
                    }
                }.show();
                String videoName = CurLiveInfo.getTitle();
                if (AppManager.isInvestor(BaseApplication.getContext())) {
                    DataStatistApiParam.onClickLiveRoomHeadImageToC(videoName);
                } else {
                    DataStatistApiParam.onClickLiveRoomHeadImageToC(videoName);
                }
            }
        });

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            openMenu.setVisibility(View.VISIBLE);
            sendImg.setVisibility(View.GONE);
        } else {
            openMenu.setVisibility(View.GONE);
            sendImg.setVisibility(View.VISIBLE);
        }
        if (CurLiveInfo.getAllowChat() == 0) {
            sendImg.setVisibility(View.INVISIBLE);
        }
//        if (allowChat.equals("0")) {
//            mListViewMsgItems.setVisibility(View.GONE);
//            sendImg.setVisibility(View.GONE);
//        } else {
        mListViewMsgItems.setVisibility(View.VISIBLE);
        sendImg.setVisibility(View.VISIBLE);
//        }
        mListViewMsgItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeEdit();
            }
        });
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEdit();
            }
        });


//        mBeautyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SxbLog.d("SeekBar", "onStopTrackingTouch");
//                if (mProfile == mBeatuy) {
//                    Toast.makeText(LiveActivity.this, "beauty " + mBeautyRate + "%", Toast.LENGTH_SHORT).show();//美颜度
//                } else {
//                    Toast.makeText(LiveActivity.this, "white " + mWhiteRate + "%", Toast.LENGTH_SHORT).show();//美白度
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                SxbLog.d("SeekBar", "onStartTrackingTouch");
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress,
//                                          boolean fromUser) {
//                Log.i(TAG, "onProgressChanged " + progress);
//                if (mProfile == mBeatuy) {
//                    mBeautyRate = progress;
//                    QavsdkControl.getInstance().getAVContext().getVideoCtrl().inputBeautyParam(getBeautyProgress(progress));//美颜
//                } else {
//                    mWhiteRate = progress;
//                    QavsdkControl.getInstance().getAVContext().getVideoCtrl().inputWhiteningParam(getBeautyProgress(progress));//美白
//                }
//            }
//        });


        mInviteMastk1 = (FrameLayout) view1.findViewById(R.id.inviteMaskItem1);
        mInviteMastk2 = (FrameLayout) view1.findViewById(R.id.inviteMaskItem2);
        mInviteMastk3 = (FrameLayout) view1.findViewById(R.id.inviteMaskItem3);
        mVideoHead1 = (ImageView) view1.findViewById(R.id.inviteMaskHead1);
        mVideoHead2 = (ImageView) view1.findViewById(R.id.inviteMaskHead2);
        mVideoHead3 = (ImageView) view1.findViewById(R.id.inviteMaskHead3);
        mInviteMastk1.setVisibility(View.GONE);
        mInviteMastk2.setVisibility(View.GONE);
        mInviteMastk3.setVisibility(View.GONE);

        mEditTextInputMsg.setOnClickListener(this);

        view1.findViewById(R.id.qav_topbar_hangup).setOnClickListener(this);
        view1.findViewById(R.id.qav_topbar_push).setOnClickListener(this);
        view1.findViewById(R.id.qav_topbar_record).setOnClickListener(this);
        view1.findViewById(R.id.qav_topbar_streamtype).setOnClickListener(this);
        if (!(MySelfInfo.getInstance().getIdStatus() == Constants.HOST)) {
            view1.findViewById(R.id.qav_topbar_push).setVisibility(View.GONE);
            view1.findViewById(R.id.qav_topbar_streamtype).setVisibility(View.GONE);
            view1.findViewById(R.id.qav_topbar_record).setVisibility(View.GONE);
        }
        praiseLayout = (LinearLayout) view1.findViewById(R.id.praise_layout);
        mButtonSendMsg = (Button) view1.findViewById(R.id.qav_bottombar_send_msg);
        mButtonSendMsg.setOnClickListener(this);
        mClockTextView = (TextView) view1.findViewById(R.id.qav_timer);
        mPraiseNum = (TextView) view1.findViewById(R.id.text_view_live_praise);
        mMemberListButton = (TextView) view1.findViewById(R.id.btn_member_list);
//        mMemberListButton.setOnClickListener(this);

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            mButtonMute.setOnClickListener(this);
            mButtonSwitchCamera.setOnClickListener(this);
            mButtonPraise.setEnabled(false);

        } else {
            mButtonSwitchCamera.setOnClickListener(this);
        }

        //默认不显示键盘
        mInputKeyBoard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViewById(R.id.av_screen_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideMsgIputKeyboard();
                mEditTextInputMsg.setVisibility(View.VISIBLE);
                editCommit.setFocusable(false);
                return false;
            }
        });

        mVideoTimer = new Timer(true);
        mHeartClickTimer = new Timer(true);

        editCommit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.e("hasFocus", "true");
                    sendViewChange();
                } else {
                    Log.e("hasFocus", "false");
                    closeEdit();
                }
            }
        });
        tvTipsMsg = (TextView) view1.findViewById(R.id.qav_tips_msg);
        tvTipsMsg.setTextColor(Color.GREEN);
        tvShowTips = (TextView) view1.findViewById(R.id.param_video);
        tvShowTips.setOnClickListener(this);
        avView = findViewById(R.id.av_video_layer_ui);//surfaceView;
    }





        /*
        mHostCtrView = (LinearLayout) findViewById(R.id.host_bottom_layout);
        mNomalMemberCtrView = (LinearLayout) findViewById(R.id.member_bottom_layout);
        mVideoMemberCtrlView = (LinearLayout) findViewById(R.id.video_member_bottom_layout);
        mHostLeaveLayout = (LinearLayout) findViewById(R.id.ll_host_leave);
        mVideoChat = (TextView) findViewById(R.id.video_interact);
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        mVideoTime = (TextView) findViewById(R.id.broadcasting_time);
        mHeadIcon = (ImageView) findViewById(R.id.head_icon);
        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
        mHostNameTv = (TextView) findViewById(R.id.host_name);
        tvMembers = (TextView) findViewById(R.id.member_counts);
        tvAdmires = (TextView) findViewById(R.id.heart_counts);

        speedBtn = (TextView) findViewById(R.id.speed_test_btn);
        speedBtn.setOnClickListener(this);

        BtnCtrlVideo = (TextView) findViewById(R.id.camera_controll);
        BtnCtrlMic = (TextView) findViewById(R.id.mic_controll);
        BtnHungup = (TextView) findViewById(R.id.close_member_video);
        BtnCtrlVideo.setOnClickListener(this);
        BtnCtrlMic.setOnClickListener(this);
        BtnHungup.setOnClickListener(this);
        TextView roomId = (TextView) findViewById(R.id.room_id);
        roomId.setText(CurLiveInfo.getChatRoomId());

        //for 测试用
        TextView paramVideo = (TextView) findViewById(R.id.param_video);
        paramVideo.setOnClickListener(this);
        tvTipsMsg = (TextView) findViewById(R.id.qav_tips_msg);
        tvTipsMsg.setTextColor(Color.GREEN);
        paramTimer.schedule(task, 1000, 1000);
        pushBtn = (TextView) findViewById(R.id.push_btn);
        pushBtn.setVisibility(View.VISIBLE);
        pushBtn.setOnClickListener(this);
        initPushDialog();

        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            mHostCtrView.setVisibility(View.VISIBLE);
            mNomalMemberCtrView.setVisibility(View.GONE);
            mRecordBall = (ImageView) findViewById(R.id.record_ball);
            Btnflash = (TextView) findViewById(R.id.flash_btn);
            BtnSwitch = (TextView) findViewById(R.id.switch_cam);
            BtnBeauty = (TextView) findViewById(R.id.beauty_btn);
            BtnWhite = (TextView) findViewById(R.id.white_btn);
            BtnMic = (TextView) findViewById(R.id.mic_btn);
            BtnScreen = (TextView) findViewById(R.id.fullscreen_btn);
            mVideoChat.setVisibility(View.VISIBLE);
            Btnflash.setOnClickListener(this);
            BtnSwitch.setOnClickListener(this);
            BtnBeauty.setOnClickListener(this);
            BtnWhite.setOnClickListener(this);
            BtnMic.setOnClickListener(this);
            BtnScreen.setOnClickListener(this);
            mVideoChat.setOnClickListener(this);
            inviteView1 = (TextView) findViewById(R.id.invite_view1);
            inviteView2 = (TextView) findViewById(R.id.invite_view2);
            inviteView3 = (TextView) findViewById(R.id.invite_view3);
            inviteView1.setOnClickListener(this);
            inviteView2.setOnClickListener(this);
            inviteView3.setOnClickListener(this);
            pushBtn.setVisibility(View.VISIBLE);


            recordBtn = (TextView) findViewById(R.id.record_btn);
            recordBtn.setVisibility(View.VISIBLE);
            recordBtn.setOnClickListener(this);

            initBackDialog();
            initDetailDailog();
            initRecordDialog();


            mMemberDg = new MembersDialog(this, R.style.floag_dialog, this);
            mMemberDg.setCanceledOnTouchOutside(true);
            startRecordAnimation();
            showHeadIcon(mHeadIcon, MySelfInfo.getInstance().getAvatar());
            mBeautySettings = (LinearLayout) findViewById(R.id.qav_beauty_setting);
            mBeautyConfirm = (TextView) findViewById(R.id.qav_beauty_setting_finish);
            mBeautyConfirm.setOnClickListener(this);
            mBeautyBar = (SeekBar) (findViewById(R.id.qav_beauty_progress));
            mBeautyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    SxbLog.d("SeekBar", "onStopTrackingTouch");
                    if (mProfile == mBeatuy) {
                        Toast.makeText(LiveActivity.this, "beauty " + mBeautyRate + "%", Toast.LENGTH_SHORT).show();//美颜度
                    } else {
                        Toast.makeText(LiveActivity.this, "white " + mWhiteRate + "%", Toast.LENGTH_SHORT).show();//美白度
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    SxbLog.d("SeekBar", "onStartTrackingTouch");
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    Log.i(TAG, "onProgressChanged " + progress);
                    if (mProfile == mBeatuy) {
                        mBeautyRate = progress;
                        QavsdkControl.getInstance().getAVContext().getVideoCtrl().inputBeautyParam(getBeautyProgress(progress));//美颜
                    } else {
                        mWhiteRate = progress;
                        QavsdkControl.getInstance().getAVContext().getVideoCtrl().inputWhiteningParam(getBeautyProgress(progress));//美白
                    }
                }
            });
        } else {
            LinearLayout llRecordTip = (LinearLayout) findViewById(R.id.record_tip);
            llRecordTip.setVisibility(View.GONE);
            mHostNameTv.setVisibility(View.VISIBLE);
            initInviteDialog();
            mNomalMemberCtrView.setVisibility(View.VISIBLE);
            mHostCtrView.setVisibility(View.GONE);
            BtnInput = (TextView) findViewById(R.id.message_input);
            BtnInput.setOnClickListener(this);
            mLikeTv = (TextView) findViewById(R.id.member_send_good);
            mLikeTv.setOnClickListener(this);
            mVideoChat.setVisibility(View.GONE);
            BtnScreen = (TextView) findViewById(R.id.clean_screen);


            List<String> ids = new ArrayList<>();
            ids.add(CurLiveInfo.getHostID());
            showHeadIcon(mHeadIcon, CurLiveInfo.getHostAvator());
            mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostName(), 10));

            mHostLayout = (LinearLayout) findViewById(R.id.head_up_layout);
            mHostLayout.setOnClickListener(this);
            BtnScreen.setOnClickListener(this);
        }


        BtnNormal = (TextView) findViewById(R.id.normal_btn);
        BtnNormal.setOnClickListener(this);
        mFullControllerUi = (FrameLayout) findViewById(R.id.controll_ui);
        avView = findViewById(R.id.av_video_layer_ui);//surfaceView;
        BtnBack = (TextView) findViewById(R.id.btn_back);
        BtnBack.setOnClickListener(this);

        mListViewMsgItems = (ListView) findViewById(R.id.im_msg_listview);
        mArrayListChatEntity = new ArrayList<ChatEntity>();
        mChatMsgListAdapter = new ChatMsgListAdapter(this, mListViewMsgItems, mArrayListChatEntity);
        mListViewMsgItems.setAdapter(mChatMsgListAdapter);

        tvMembers.setText("" + CurLiveInfo.getMembers());
        tvAdmires.setText("" + CurLiveInfo.getAdmires());
    }
            */


    @Override
    protected void onResume() {
        super.onResume();
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            QavsdkControl.getInstance().getAVContext().getAudioCtrl().enableMic(true);
        } else {
            QavsdkControl.getInstance().getAVContext().getAudioCtrl().enableMic(false);
        }
        mLiveHelper.resume();
        QavsdkControl.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != QavsdkControl.getInstance().getAVContext()) {   // 帐号被踢下线时，AVContext为空
            QavsdkControl.getInstance().getAVContext().getAudioCtrl().enableMic(false);
            mLiveHelper.pause();
            QavsdkControl.getInstance().onPause();
        }
    }


    private void closeEdit() {
        bottomLayout.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.GONE);
        editCommit.setFocusable(false);
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendViewChange() {
        editCommit.setFocusable(true);
        bottomLayout.setVisibility(View.INVISIBLE);
        inputLayout.setVisibility(View.VISIBLE);
        editCommit.setFocusableInTouchMode(true);
        editCommit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editCommit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editCommit, 0);
    }


    /**
     * 直播心跳
     */
    private class HeartBeatTask extends TimerTask {
        @Override
        public void run() {
            String host = CurLiveInfo.getHostID();
            SxbLog.i(TAG, "HeartBeatTask " + host);
            hostHeartRequest();
        }
    }

    private void hostHeartRequest() {
        getPresenter().hostHeartStatus(CurLiveInfo.getRoomNum() + "", AppManager.getUserId(BaseApplication.getContext()));
    }


    public boolean hideMsgIputKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                mInputKeyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                return true;
            }
        }

        return false;
    }

    /**
     * 记时器
     */
    private class VideoTimerTask extends TimerTask {
        public void run() {
            SxbLog.i(TAG, "timeTask ");
            ++mSecond;
            if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST)
                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    @Override
    protected void onDestroy() {
        stopOrientationListener();
        watchCount = 0;
        super.onDestroy();
        if (null != mHearBeatTimer) {
            mHearBeatTimer.cancel();
            mHearBeatTimer = null;
        }
        if (null != mVideoTimer) {
            mVideoTimer.cancel();
            mVideoTimer = null;
        }
        if (null != paramTimer) {
            paramTimer.cancel();
            paramTimer = null;
        }
        if (null != getLiveObservable) {
            RxBus.get().unregister(RxConstant.GET_LIVE_PDF_LIST_TASK, getLiveObservable);
        }
        mLiveHelper.stopRecord();

        inviteViewCount = 0;
        thumbUp = 0;
        CurLiveInfo.setMembers(0);
        CurLiveInfo.setAdmires(0);
        CurLiveInfo.setCurrentRequestCount(0);
        unregisterReceiver();
        mLiveHelper.onDestory();
        mEnterRoomHelper.onDestory();
        QavsdkControl.getInstance().clearVideoMembers();
        QavsdkControl.getInstance().onDestroy();
//        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, screenChange);
    }


    /**
     * 点击Back键
     */
    @Override
    public void onBackPressed() {
        if (bInAvRoom) {
            bDelayQuit = false;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            if (inputLayout.getVisibility() == View.VISIBLE) {
                // 隐藏软键盘
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
                bottomLayout.setVisibility(View.VISIBLE);
                inputLayout.setVisibility(View.GONE);
                editCommit.setFocusable(false);
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    if (MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
                        memberCloseAlertDialog();
                    } else
                        hostCloseAlertDialog();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        } else {
            finish();
        }
    }

    /**
     * 主动退出直播
     */
    private void quiteLiveByPurpose() {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
        } else {
            mLiveHelper.perpareQuitRoom(true);
//            mEnterRoomHelper.quiteLive();
        }
    }

    /**
     * 被动退出直播
     */
    private void quiteLivePassively() {
        Toast.makeText(this, "Host leave Live ", Toast.LENGTH_SHORT);
        mLiveHelper.perpareQuitRoom(false);
//        mEnterRoomHelper.quiteLive();
    }

    @Override
    public void readyToQuit() {
        mEnterRoomHelper.quiteLive();
    }

    /**
     * 完成进出房间流程
     *
     * @param id_status
     * @param isSucc
     */
    @Override
    public void enterRoomComplete(int id_status, boolean isSucc) {
//        Toast.makeText(LiveActivity.this, "EnterRoom  " + id_status + " isSucc " + isSucc, Toast.LENGTH_SHORT).show();
        //必须得进入房间之后才能初始化UI
        mEnterRoomHelper.initAvUILayer(avView);
        QavsdkControl.getInstance().setSlideListener(this);
        bInAvRoom = true;
        bDelayQuit = true;
        updateHostLeaveLayout();

        //设置预览回调，修正摄像头镜像
        mLiveHelper.setCameraPreviewChangeCallback();
        if (isSucc == true) {
            //IM初始化
            mLiveHelper.initTIMListener("" + CurLiveInfo.getChatId());

            if (id_status == Constants.HOST) {//主播方式加入房间成功
                //开启摄像头渲染画面
                SxbLog.i(TAG, "createlive enterRoomComplete isSucc" + isSucc);
            } else {
                //发消息通知上线
                //TODO 打开
//                mLiveHelper.sendGroupMessage(Constants.AVIMCMD_EnterLive, "");
            }
        }

        bReadyToChange = false;
    }

    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            if ((getBaseContext() != null) && (null != mDetailDialog) && (mDetailDialog.isShowing() == false)) {
                SxbLog.d(TAG, LogConstants.ACTION_HOST_QUIT_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "quite room callback"
                        + LogConstants.DIV + LogConstants.STATUS.SUCCEED + LogConstants.DIV + "id status " + id_status);
                mDetailTime.setText(formatTime);
                mDetailAdmires.setText("" + CurLiveInfo.getAdmires());
                mDetailWatchCount.setText("" + watchCount);
                mDetailDialog.show();
            }
        } else {
            //finish();
            if (bInAvRoom) {
                if (bReadyToChange) {
                    clearOldData();
                    mLiveListViewHelper.getPageData();
                }
            } else {
                bReadyToChange = false;
            }
            if (bDelayQuit) {
                updateHostLeaveLayout();
            } else {
                finish();
            }
        }
        bInAvRoom = false;
    }


    private TextView mDetailTime, mDetailAdmires, mDetailWatchCount;

    private void initDetailDailog() {
//        mDetailDialog = new Dialog(this, R.style.dialog);
//        mDetailDialog.setContentView(R.layout.dialog_live_detail);
//        mDetailTime = (TextView) mDetailDialog.findViewById(R.id.tv_time);
//        mDetailAdmires = (TextView) mDetailDialog.findViewById(R.id.tv_admires);
//        mDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);
//
//        mDetailDialog.setCancelable(false);
//
//        TextView tvCancel = (TextView) mDetailDialog.findViewById(R.id.btn_cancel);
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDetailDialog.dismiss();
//                finish();
//            }
//        });
//        mDetailDialog.show();
    }

    private String joinId = "";

    /**
     * 成员状态变更
     *
     * @param id
     * @param name
     */
    @Override
    public void memberJoin(String id, String name, String headImg) {
        if (joinId.equals(id)) {
            return;
        }
        joinId = id;
        SxbLog.d(TAG, LogConstants.ACTION_VIEWER_ENTER_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "on member join" +
                LogConstants.DIV + "join room " + id);
        watchCount++;
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(id);
        memberInfo.setAvatar(headImg);
        memberInfo.setUserName(name);
        mMemberList.add(0, memberInfo);
        if (membersAdapter == null) {
            membersAdapter = new MembersAdapter(LiveActivity.this, mMemberList);
            memberList.setAdapter(membersAdapter);
        } else {
            membersAdapter.notifyDataSetChanged();
        }
//        refreshTextListView(TextUtils.isEmpty(name) ? id : name, "join live", Constants.MEMBER_ENTER);

        CurLiveInfo.setMembers(CurLiveInfo.getMembers() + 1);
        mMemberListButton.setText(String.format("%d", CurLiveInfo.getMembers()));
    }

    private String quitId = "";

    @Override
    public void memberQuit(String id, String name) {
        if (quitId.equals(id)) {
            return;
        }
        quitId = id;
        if (id.equals(CurLiveInfo.getHostID())) {
            mLiveHelper.perpareQuitRoom(false);
            Intent in = new Intent(LiveActivity.this, EndLiveActivity.class);
            in.putExtra("userNum", CurLiveInfo.getMembers());
            startActivity(in);
        } else {
//            refreshTextListView(TextUtils.isEmpty(name) ? id : name, "quite live", Constants.MEMBER_EXIT);
            if (mMemberList != null) {

                if (CurLiveInfo.getMembers() > mMemberList.size()) {
                    CurLiveInfo.setMembers(CurLiveInfo.getMembers() - 1);
                    mMemberListButton.setText(String.format("%d", CurLiveInfo.getMembers()));
                }
                for (int i = 0; i < mMemberList.size(); i++) {
                    if (id.equals(mMemberList.get(i).getUserId())) {
                        mMemberList.remove(i);
                    }
                }
                membersAdapter.notifyDataSetChanged();
                //如果存在视频互动，取消
                QavsdkControl.getInstance().closeMemberView(id);
            }

        }
    }

    @Override
    public void hostLeave(String id, String name) {
//        refreshTextListView(TextUtils.isEmpty(name) ? id : name, "leave for a while", Constants.HOST_LEAVE);
    }

    @Override
    public void hostBack(String id, String name) {
//        refreshTextListView(TextUtils.isEmpty(name) ? id : name, "is back", Constants.HOST_BACK);
    }

    /**
     * 有成员退群
     *
     * @param list 成员ID 列表
     */
    @Override
    public void memberQuiteLive(String[] list) {
        if (list == null) return;
        for (String id : list) {
            SxbLog.i(TAG, "memberQuiteLive id " + id);
            if (CurLiveInfo.getHostID().equals(id)) {
                if (MySelfInfo.getInstance().getIdStatus() == Constants.MEMBER)
                    quiteLivePassively();
            } else {
                memberQuit(id, "");
            }
        }
    }


    /**
     * 有成员入群
     *
     * @param list 成员ID 列表
     */
    @Override
    public void memberJoinLive(final String[] list) {

    }

    @Override
    public void alreadyInLive(String[] list) {
        for (String id : list) {
            if (id.equals(MySelfInfo.getInstance().getId())) {
                QavsdkControl.getInstance().setSelfId(MySelfInfo.getInstance().getId());
                QavsdkControl.getInstance().setLocalHasVideo(true, MySelfInfo.getInstance().getId());
            } else {
                QavsdkControl.getInstance().setRemoteHasVideo(true, id, AVView.VIDEO_SRC_TYPE_CAMERA);
            }
        }

    }

    /**
     * 红点动画
     */
    private void startRecordAnimation() {
        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();
    }

    private static int index = 0;

    /**
     * 加载视频数据
     *
     * @param isLocal 是否是本地数据
     * @param id      身份
     */
    @Override
    public void showVideoView(boolean isLocal, String id) {
        SxbLog.i(TAG, "showVideoView " + id);
        //渲染本地Camera
        if (isLocal == true) {
            SxbLog.i(TAG, "showVideoView host :" + MySelfInfo.getInstance().getId());
            QavsdkControl.getInstance().setSelfId(MySelfInfo.getInstance().getId());
            QavsdkControl.getInstance().setLocalHasVideo(true, MySelfInfo.getInstance().getId());
            //主播通知用户服务器
            if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
                if (bFirstRender) {
                    mEnterRoomHelper.notifyServerCreateRoom();

                    //主播心跳
                    mHearBeatTimer = new Timer(true);
                    mHeartBeatTask = new HeartBeatTask();
                    mHearBeatTimer.schedule(mHeartBeatTask, 1000, 5 * 1000);

                    //直播时间
                    mVideoTimer = new Timer(true);
                    mVideoTimerTask = new VideoTimerTask();
                    mVideoTimer.schedule(mVideoTimerTask, 1000, 1000);
                    bFirstRender = false;
                }
            }
        } else {
//            QavsdkControl.getInstance().addRemoteVideoMembers(id);
            if (isScreenShare) {
                QavsdkControl.getInstance().setRemoteHasVideo(true, id, AVView.VIDEO_SRC_TYPE_SCREEN);
                isScreenShare = false;
            } else {
                QavsdkControl.getInstance().setRemoteHasVideo(true, id, AVView.VIDEO_SRC_TYPE_CAMERA);
            }
        }

    }


    private float getBeautyProgress(int progress) {
        SxbLog.d("shixu", "progress: " + progress);
        return (9.0f * progress / 100.0f);
    }


    @Override
    public void showInviteDialog() {
        if ((inviteDg != null) && (getBaseContext() != null) && (inviteDg.isShowing() != true)) {
            inviteDg.show();
        }
    }

    @Override
    public void hideInviteDialog() {
        if ((inviteDg != null) && (inviteDg.isShowing() == true)) {
            inviteDg.dismiss();
        }
    }


    @Override
    public void refreshText(String text, String name) {
        if (text != null) {
//            refreshTextListView(name, text, Constants.TEXT_TYPE);
        }
    }

    @Override
    public void refreshThumbUp() {
        CurLiveInfo.setAdmires(CurLiveInfo.getAdmires() + 1);
        if (!bCleanMode) {      // 纯净模式下不播放飘星动画
//            mHeartLayout.addFavor();
        }
        tvAdmires.setText("" + CurLiveInfo.getAdmires());
    }

    @Override
    public void refreshUI(String id) {
        //当主播选中这个人，而他主动退出时需要恢复到正常状态
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST)
            if (!backGroundId.equals(CurLiveInfo.getHostID()) && backGroundId.equals(id)) {
                backToNormalCtrlView();
            }
    }


    private int inviteViewCount = 0;

    @Override
    public boolean showInviteView(String id) {
        SxbLog.d(TAG, LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "invite up show" +
                LogConstants.DIV + "id " + id);
        int index = QavsdkControl.getInstance().getAvailableViewIndex(1);
        if (index == -1) {
            Toast.makeText(LiveActivity.this, "the invitation's upper limit is 3", Toast.LENGTH_SHORT).show();
            return false;
        }
        int requetCount = index + inviteViewCount;
        if (requetCount > 3) {
            Toast.makeText(LiveActivity.this, "the invitation's upper limit is 3", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (hasInvited(id)) {
            Toast.makeText(LiveActivity.this, "it has already invited", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (requetCount) {
            case 1:
                inviteView1.setText(id);
                inviteView1.setVisibility(View.VISIBLE);
                inviteView1.setTag(id);

                break;
            case 2:
                inviteView2.setText(id);
                inviteView2.setVisibility(View.VISIBLE);
                inviteView2.setTag(id);
                break;
            case 3:
                inviteView3.setText(id);
                inviteView3.setVisibility(View.VISIBLE);
                inviteView3.setTag(id);
                break;
        }
        mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MUlTI_HOST_INVITE, "", id);
        inviteViewCount++;
        //30s超时取消
        Message msg = new Message();
        msg.what = TIMEOUT_INVITE;
        msg.obj = id;
        mHandler.sendMessageDelayed(msg, 30 * 1000);
        return true;
    }


    /**
     * 判断是否邀请过同一个人
     *
     * @param id
     * @return
     */
    private boolean hasInvited(String id) {
        if (id.equals(inviteView1.getTag())) {
            return true;
        }
        if (id.equals(inviteView2.getTag())) {
            return true;
        }
        if (id.equals(inviteView3.getTag())) {
            return true;
        }
        return false;
    }

    @Override
    public void cancelInviteView(String id) {
        if ((inviteView1 != null) && (inviteView1.getTag() != null)) {
            if (inviteView1.getTag().equals(id)) {
            }
            if (inviteView1.getVisibility() == View.VISIBLE) {
                inviteView1.setVisibility(View.INVISIBLE);
                inviteView1.setTag("");
                inviteViewCount--;
            }
        }

        if (inviteView2 != null && inviteView2.getTag() != null) {
            if (inviteView2.getTag().equals(id)) {
                if (inviteView2.getVisibility() == View.VISIBLE) {
                    inviteView2.setVisibility(View.INVISIBLE);
                    inviteView2.setTag("");
                    inviteViewCount--;
                }
            } else {
                Log.i(TAG, "cancelInviteView inviteView2 is null");
            }
        } else {
            Log.i(TAG, "cancelInviteView inviteView2 is null");
        }

        if (inviteView3 != null && inviteView3.getTag() != null) {
            if (inviteView3.getTag().equals(id)) {
                if (inviteView3.getVisibility() == View.VISIBLE) {
                    inviteView3.setVisibility(View.INVISIBLE);
                    inviteView3.setTag("");
                    inviteViewCount--;
                }
            } else {
                Log.i(TAG, "cancelInviteView inviteView3 is null");
            }
        } else {
            Log.i(TAG, "cancelInviteView inviteView3 is null");
        }
    }

    @Override
    public void cancelMemberView(String id) {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
        } else {
            //TODO 主动下麦 下麦；
            SxbLog.d(TAG, LogConstants.ACTION_VIEWER_UNSHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "start unShow" +
                    LogConstants.DIV + "id " + id);
            mLiveHelper.changeAuthandRole(false, Constants.NORMAL_MEMBER_AUTH, Constants.NORMAL_MEMBER_ROLE);
//            mLiveHelper.closeCameraAndMic();//是自己成员关闭
        }
        mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, id);
        QavsdkControl.getInstance().closeMemberView(id);
        backToNormalCtrlView();
    }

    private void showHostDetail() {
//        Dialog hostDlg = new Dialog(this, R.style.host_info_dlg);
//        hostDlg.setContentView(R.layout.host_info_layout);

//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        Window dlgwin = hostDlg.getWindow();
//        WindowManager.LayoutParams lp = dlgwin.getAttributes();
//        dlgwin.setGravity(Gravity.TOP);
//        lp.width = (int) (display.getWidth()); //设置宽度
//
//        hostDlg.getWindow().setAttributes(lp);
//        hostDlg.show();
//
//        TextView tvHost = (TextView) hostDlg.findViewById(R.id.tv_host_name);
//        tvHost.setText(CurLiveInfo.getHostName());
//        ImageView ivHostIcon = (ImageView) hostDlg.findViewById(R.id.iv_host_icon);
//        showHeadIcon(ivHostIcon, CurLiveInfo.getHostAvator());
//        TextView tvLbs = (TextView) hostDlg.findViewById(R.id.tv_host_lbs);
//        tvLbs.setText(UIUtils.getLimitString(CurLiveInfo.getAddress(), 6));
    }

    private boolean checkInterval() {
        if (0 == admireTime) {
            admireTime = System.currentTimeMillis();
            return true;
        }
        long newTime = System.currentTimeMillis();
        if (newTime >= admireTime + 1000) {
            admireTime = newTime;
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.live_share_btn) {
            showShareDialog();

//        } else if (i == R.id.btn_back) {//                quiteLiveByPurpose();
//            onBackPressed();
//
//        } else if (i == R.id.message_input) {
//            inputMsgDialog();
//
//        } else if (i == R.id.switch_cam) {
//            mLiveHelper.switchCamera();
//
//        } else if (i == R.id.mic_btn) {
//            Toast.makeText(this, mLiveHelper.isMicOpen() + "", Toast.LENGTH_SHORT).show();
//            if (mLiveHelper.isMicOpen() == true) {
//                mLiveHelper.muteMic();
//                Drawable drawable = getResources().getDrawable(R.drawable.mic3);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    mButtonMute.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
//                }
//            } else {
//                mLiveHelper.openMic();
//                Drawable drawable = getResources().getDrawable(R.drawable.mic1);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    mButtonMute.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
//                }
//            }
//
//        } else if (i == R.id.head_up_layout) {
//            showHostDetail();
//
//        } else if (i == R.id.clean_screen || i == R.id.fullscreen_btn) {
//            bCleanMode = true;
//            mFullControllerUi.setVisibility(View.INVISIBLE);
//            BtnNormal.setVisibility(View.VISIBLE);
//
//        } else if (i == R.id.normal_btn) {
//            bCleanMode = false;
//            mFullControllerUi.setVisibility(View.VISIBLE);
//            BtnNormal.setVisibility(View.GONE);
//
//        } else if (i == R.id.video_interact) {
//            mMemberDg.setCanceledOnTouchOutside(true);
//            mMemberDg.show();
//
//        } else if (i == R.id.camera_controll) {
//            Toast.makeText(LiveActivity.this, "切换" + backGroundId + "camrea 状态", Toast.LENGTH_SHORT).show();
//            if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//自己关闭自己
//                mLiveHelper.toggleCamera();
//            } else {
//                mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MULTI_HOST_CONTROLL_CAMERA, backGroundId, backGroundId);//主播关闭自己
//            }
//
//        } else if (i == R.id.mic_controll) {
//            Toast.makeText(LiveActivity.this, "切换" + backGroundId + "mic 状态", Toast.LENGTH_SHORT).show();
//            if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//自己关闭自己
//                mLiveHelper.toggleMic();
//            } else {
//                mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MULTI_HOST_CONTROLL_MIC, backGroundId, backGroundId);//主播关闭自己
//            }
//
//        }
        } else if (i == R.id.param_video) {
            showTips = !showTips;

        } else if (i == R.id.send_comment) {
            sendViewChange();
//                String[] param = new String[]{"评论", AppManager.getUserInfo(LiveActivity.this).getToB().isColorCloud(), AppManager.getUserInfo(LiveActivity.this).getToB().getOrganizationName()};
//                DataStatisticsUtils.push(LiveActivity.this, DataStatisticsUtils.getParams("1023", "10107", param));
            String videoName = CurLiveInfo.getTitle();
            if (AppManager.isInvestor(LiveActivity.this)) {
                DataStatistApiParam.onClickLiveCommentToC(videoName);
            } else {
                DataStatistApiParam.onClickLiveCommentToB(videoName);
            }

        } else if (i == R.id.close2) {
            if (!(MySelfInfo.getInstance().getIdStatus() == Constants.HOST)) {
                memberCloseAlertDialog();
            } else
                hostCloseAlertDialog();

        } else if (i == R.id.qav_topbar_hangup) {
            if (!(MySelfInfo.getInstance().getIdStatus() == Constants.HOST)) {
                memberCloseAlertDialog();
            } else
                hostCloseAlertDialog();

        } else if (i == R.id.open_menu) {
            if (isOpenMenu) {
                Drawable drawable = getResources().getDrawable(R.drawable.menu_normal);
                openMenu.setBackgroundDrawable(drawable);
                isOpenMenu = false;
                menuLayout.setVisibility(View.INVISIBLE);

            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.menu_down);
                openMenu.setBackgroundDrawable(drawable);
                isOpenMenu = true;
                menuLayout.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.qav_topbar_switchcamera) {
            mLiveHelper.switchCamera();

        } else if (i == R.id.qav_bottombar_send_msg) {
            mButtonSendMsg.setEnabled(false);
            onSendMsg();

//            case R.id.qav_bottombar_msg_input:
//                mIsClicked = true;
//                break;
//            case R.id.image_btn_praise:
//                onSendPraise();
//                break;
//            case R.id.qav_topbar_push:
//                Push();
//                break;
//            case R.id.qav_topbar_streamtype:
//                switch (StreamType) {
////                    case 1:
////                        ((Button) findViewById(R.id.qav_topbar_streamtype)).setText("FLV");
////                        StreamType = 2;
////                        StreamTypeCode = DemoConstants.FLV;
////                        break;
//                    case 2:
//                        ((Button) view1.findViewById(R.id.qav_topbar_streamtype)).setText("RTMP");
//                        StreamType = 5;
//                        StreamTypeCode = DemoConstants.RTMP;
//                        break;
//                    case 5:
//                        ((Button) view1.findViewById(R.id.qav_topbar_streamtype)).setText("HLS");
//                        StreamTypeCode = DemoConstants.HLS;
//                        StreamType = 2;
//                        break;
//                }
//                break;
        } else if (i == R.id.btn_member_list) {//                showMemberList();

        } else if (i == R.id.pdf_btn) {
            String strTips = QavsdkControl.getInstance().getQualityTips();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //TODO
            fragmentTransaction.add(R.id.fragment_contain, new PDFListFragment()).addToBackStack(null);
            fragmentTransaction.commit();
//                String[] param1 = new String[]{"PDF", AppManager.getUserInfo(LiveActivity.this).getToB().isColorCloud(), AppManager.getUserInfo(LiveActivity.this).getToB().getOrganizationName()};
//                HashMap<String, String> params = DataStatisticsUtils.getParams("1023", "10108", param1);
//                DataStatisticsUtils.push(LiveActivity.this, params);
            String vaName = CurLiveInfo.getTitle();
            if (AppManager.isInvestor(BaseApplication.getContext())) {
                DataStatistApiParam.onClickLivePDFToC(vaName, "");
            } else {
                DataStatistApiParam.onClickLivePDFToB(vaName, "");
            }

        } else {
        }
    }

    /**
     * 显示分享弹窗
     */
    private void showShareDialog() {
        dataStatistApiParam();
        LiveShareDialog liveShareDialog = new LiveShareDialog(this);
        liveShareDialog.setData(
                this,
                CurLiveInfo.getShareUrl(),
                Contant.LIVE_SHARE_TITLE,
                CurLiveInfo.getSlogan(),
                AppManager.getUserInfo(BaseApplication.getContext()).getHeadImageUrl()
        );
        liveShareDialog.show();
    }

    private void dataStatistApiParam() {
        if (AppManager.isInvestor(LiveActivity.this)) {
            DataStatistApiParam.liveShareC(CurLiveInfo.getTitle());
        } else {
            DataStatistApiParam.liveShareB(CurLiveInfo.getTitle());
        }
    }

    private void hostCloseAlertDialog() {

        new DefaultDialog(this, "确定结束直播？", "取消", "确定") {
            @Override
            public void left() {
                startOrientationListener();

                this.dismiss();
            }

            @Override
            public void right() {
//                onCloseVideo();
                if (null != mLiveHelper) {
                    mLiveHelper.perpareQuitRoom(true);
                    if (isPushed) {
                        mLiveHelper.stopPushAction();
                    }
                }
                getPresenter().hostCloseLive(CurLiveInfo.getRoomNum() + "", AppManager.getUserId(BaseApplication.getContext()));

//                String[] param = new String[]{"关闭", AppManager.getUserInfo(LiveActivity.this).getToB().isColorCloud(), AppManager.getUserInfo(LiveActivity.this).getToB().getOrganizationName()};
//                HashMap<String, String> params = DataStatisticsUtils.getParams("1023", "10109", param);
//                DataStatisticsUtils.push(LiveActivity.this, params);
                String videoName = CurLiveInfo.getTitle();
                if (AppManager.isInvestor(LiveActivity.this)) {
                    DataStatistApiParam.onClickLiveRoomCloseToC(videoName);
                } else {
                    DataStatistApiParam.onClickLiveRoomCloseToB(videoName);
                }
                this.dismiss();

            }
        }.show();
    }

    private void memberCloseAlertDialog() {
        new DefaultDialog(this, "确认退出吗？", "继续观看", "结束观看") {
            @Override
            public void left() {
                startOrientationListener();
                this.dismiss();
            }

            @Override
            public void right() {
                quiteLiveByPurpose();
                stopOrientationListener();
                getPresenter().memberExitRoom(CurLiveInfo.getRoomNum() + "", AppManager.getUserId(BaseApplication.getContext()));
                this.dismiss();
            }
        }.show();


    }


    private void onSendMsg() {
        final String msg = editCommit.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            new MToast(this).show("消息不能为空", Toast.LENGTH_SHORT);
            mButtonSendMsg.setEnabled(true);
            return;
        }
        if (msg.length() > 0) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_id", AppManager.getUserId(BaseApplication.getContext()));
            if (TextUtils.isEmpty(AppManager.getUserInfo(LiveActivity.this).getNickName())) {
                hashMap.put("user_name", "私享云用户");
            } else {
                hashMap.put("user_name", AppManager.getUserInfo(LiveActivity.this).getNickName());
            }
            hashMap.put("room_id", CurLiveInfo.getRoomNum());
            hashMap.put("wechat_id", CurLiveInfo.getRoomNum() + "");
            if (TextUtils.isEmpty(AppManager.getUserInfo(LiveActivity.this).getPhoneNum())) {
                hashMap.put("telephone", "未知");
            } else {
                hashMap.put("telephone", AppManager.getUserInfo(LiveActivity.this).getPhoneNum());
            }
//            if (TextUtils.isEmpty(AppManager.getUserInfo(LiveActivity.this).getOrganizationName())) {
            hashMap.put("org_name", "未知");
//            } else {
//                hashMap.put("org_name", AppManager.getUserInfo(LiveActivity.this).getOrganizationName());
//            }
//            if (TextUtils.isEmpty(AppManager.getUserInfo(LiveActivity.this).getOrganizationId())) {
            hashMap.put("org_id", "未知");
//            } else {
//                hashMap.put("org_id", AppManager.getUserInfo(LiveActivity.this).getOrganizationId());
//            }
            hashMap.put("message", msg);
            getPresenter().sendMsg(hashMap);
        }
    }

    /**
     * @param msg
     */
    private void sendMsg(String msg) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "input message too long", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(msg);
        if (Nmsg.addElement(elem) != 0) {
            return;
        }
        mLiveHelper.sendGroupText(Nmsg);
    }

    //for 测试获取测试参数
    private boolean showTips = false;
    private TextView tvTipsMsg;
    Timer paramTimer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (showTips) {
                        if (tvTipsMsg != null) {
                            String strTips = QavsdkControl.getInstance().getQualityTips();
                            strTips = praseString(strTips);
                            if (!TextUtils.isEmpty(strTips)) {
                                tvTipsMsg.setText(strTips);
                            }
                        }
                    } else {
                        tvTipsMsg.setText("");
                    }
                }
            });
        }
    };

    //for 测试 解析参数
    private String praseString(String video) {
        if (video.length() == 0) {
            return "";
        }
        String result = "";
        String splitItems[];
        String tokens[];
        splitItems = video.split("\\n");
        for (int i = 0; i < splitItems.length; ++i) {
            if (splitItems[i].length() < 2)
                continue;

            tokens = splitItems[i].split(":");
            if (tokens[0].length() == "mainVideoSendSmallViewQua".length()) {
                continue;
            }
            if (tokens[0].endsWith("BigViewQua")) {
                tokens[0] = "mainVideoSendViewQua";
            }
            if (tokens[0].endsWith("BigViewQos")) {
                tokens[0] = "mainVideoSendViewQos";
            }
            result += tokens[0] + ":\n" + "\t\t";
            for (int j = 1; j < tokens.length; ++j)
                result += tokens[j];
            result += "\n\n";
            //Log.d(TAG, "test:" + result);
        }
        //Log.d(TAG, "test:" + result);
        return result;
    }


    private void backToNormalCtrlView() {
        if (MySelfInfo.getInstance().getIdStatus() == Constants.HOST) {
            backGroundId = CurLiveInfo.getHostID();
            mHostCtrView.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        } else {
            backGroundId = CurLiveInfo.getHostID();
            mNomalMemberCtrView.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        }
    }


    /**
     * 发消息弹出框
     */
    private void inputMsgDialog() {
//        InputTextMsgDialog inputMsgDialog = new InputTextMsgDialog(this, R.style.inputdialog, mLiveHelper, this);
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp = inputMsgDialog.getWindow().getAttributes();
//
//        lp.width = (int) (display.getWidth()); //设置宽度
//        inputMsgDialog.getWindow().setAttributes(lp);
//        inputMsgDialog.setCancelable(true);
//        inputMsgDialog.show();
    }


    /**
     * 主播邀请应答框
     */
    private void initInviteDialog() {
//        inviteDg = new Dialog(this, R.style.dialog);
//        inviteDg.setContentView(R.layout.invite_dialog);
//        TextView hostId = (TextView) inviteDg.findViewById(R.id.host_id);
//        hostId.setText(CurLiveInfo.getHostID());
//        TextView agreeBtn = (TextView) inviteDg.findViewById(R.id.invite_agree);
//        TextView refusebtn = (TextView) inviteDg.findViewById(R.id.invite_refuse);
//        agreeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mVideoMemberCtrlView.setVisibility(View.VISIBLE);
////                mNomalMemberCtrView.setVisibility(View.INVISIBLE);
//                SxbLog.d(TAG, LogConstants.ACTION_VIEWER_SHOW + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "accept invite" +
//                        LogConstants.DIV + "host id " + CurLiveInfo.getHostID());
//                //上麦 ；TODO 上麦 上麦 上麦 ！！！！！；
//                mLiveHelper.changeAuthandRole(true, Constants.VIDEO_MEMBER_AUTH, Constants.VIDEO_MEMBER_ROLE);
//                inviteDg.dismiss();
//            }
//        });
//
//        refusebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MUlTI_REFUSE, "", CurLiveInfo.getHostID());
//                inviteDg.dismiss();
//            }
//        });
//
//        Window dialogWindow = inviteDg.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setAttributes(lp);
    }


    /**
     * 消息刷新显示
     *
     * @param name    发送者
     * @param context 内容
     * @param type    类型 （上线线消息和 聊天消息）
     */
    public void refreshTextListView(String name, String context, int type) {
//        ChatEntity entity = new ChatEntity();
//        entity.setSenderName(name);
//        entity.setContext(context);
//        entity.setType(type);
//        notifyRefreshListView(entity);
//
//        mListViewMsgItems.setVisibility(View.VISIBLE);
//        SxbLog.d(TAG, "refreshTextListView height " + mListViewMsgItems.getHeight());
//
//        if (mListViewMsgItems.getCount() > 1) {
//            if (true)
//                mListViewMsgItems.setSelection(0);
//            else
//                mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
//        }
    }


    /**
     * 通知刷新消息ListView
     */
    private void notifyRefreshListView(ChatEntity entity) {
        mBoolNeedRefresh = true;
        mTmpChatList.add(entity);
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
    }


    /**
     * 刷新ListView并重置状态
     */
    private void doRefreshListView() {
        if (mBoolNeedRefresh) {
            mBoolRefreshLock = true;
            mBoolNeedRefresh = false;
            mArrayListChatEntity.addAll(mTmpChatList);
            mTmpChatList.clear();
            mChatMsgListAdapter.notifyDataSetChanged();

            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    SxbLog.v(TAG, "doRefreshListView->task enter with need:" + mBoolNeedRefresh);
                    mHandler.sendEmptyMessage(REFRESH_LISTVIEW);
                }
            };
            //mTimer.cancel();
            mTimer.schedule(mTimerTask, MINFRESHINTERVAL);
        } else {
            mBoolRefreshLock = false;
        }
    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {

    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {
        if (null != profiles) {
            switch (requestCode) {
                case GETPROFILE_JOIN:
                    for (TIMUserProfile user : profiles) {
                        mMemberListButton.setText(String.format("%d", CurLiveInfo.getMembers()));
                        SxbLog.w(TAG, "get nick name:" + user.getNickName());
                        SxbLog.w(TAG, "get remark name:" + user.getRemark());
                        SxbLog.w(TAG, "get avatar:" + user.getFaceUrl());
//                        if (!TextUtils.isEmpty(user.getNickName())) {
//                            refreshTextListView(user.getNickName(), "join live", Constants.MEMBER_ENTER);
//                        } else {
//                            refreshTextListView(user.getIdentifier(), "join live", Constants.MEMBER_ENTER);
//                        }
                    }
                    break;
            }

        }
    }

    //旁路直播
    private static boolean isPushed = false;

    /**
     * 旁路直播 退出房间时必须退出推流。否则会占用后台channel。
     */
    public void pushStream() {
        if (!isPushed) {
            if (mPushDialog != null)
                mPushDialog.show();
        } else {
            mLiveHelper.stopPushAction();
        }
    }

    private Dialog mPushDialog;

    private void initPushDialog() {
//        mPushDialog = new Dialog(this, R.style.dialog);
//        mPushDialog.setContentView(R.layout.push_dialog_layout);
//        final TIMAvManager.StreamParam mStreamParam = TIMAvManager.getInstance().new StreamParam();
//        final EditText pushfileNameInput = (EditText) mPushDialog.findViewById(R.id.push_filename);
//        final RadioGroup radgroup = (RadioGroup) mPushDialog.findViewById(R.id.push_type);
//
//
//        Button recordOk = (Button) mPushDialog.findViewById(R.id.btn_record_ok);
//        recordOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (pushfileNameInput.getText().toString().equals("")) {
//                    Toast.makeText(LiveActivity.this, "name can't be empty", Toast.LENGTH_SHORT);
//                    return;
//                } else {
//                    mStreamParam.setChannelName(pushfileNameInput.getText().toString());
//                }
//
//                if (radgroup.getCheckedRadioButtonId() == R.id.hls) {
//                    mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS);
//                } else {
//                    mStreamParam.setEncode(TIMAvManager.StreamEncode.RTMP);
//                }
////                mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS);
//                SxbLog.d(TAG, LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "start push stream"
//                        + LogConstants.DIV + "room id " + MySelfInfo.getInstance().getMyRoomNum());
//                mLiveHelper.pushAction(mStreamParam);
//                mPushDialog.dismiss();
//            }
//        });
//
//
//        Button recordCancel = (Button) mPushDialog.findViewById(R.id.btn_record_cancel);
//        recordCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mPushDialog.dismiss();
//            }
//        });
//
//        Window dialogWindow = mPushDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setAttributes(lp);
//        mPushDialog.setCanceledOnTouchOutside(false);
    }


    /**
     * 推流成功
     *
     * @param streamRes
     */
    @Override
    public void pushStreamSucc(TIMAvManager.StreamRes streamRes) {
//        List<TIMAvManager.LiveUrl> liveUrls = streamRes.getUrls();
//        isPushed = true;
//        pushBtn.setText(R.string.live_btn_stop_push);
//        int length = liveUrls.size();
//        String url = null;
//        String url2 = null;
//        Log.i(TAG, "pushStreamSucc: " + length);
//        if (length == 1) {
//            TIMAvManager.LiveUrl avUrl = liveUrls.get(0);
//            url = avUrl.getUrl();
//        } else if (length == 2) {
//            TIMAvManager.LiveUrl avUrl = liveUrls.get(0);
//            url = avUrl.getUrl();
//            TIMAvManager.LiveUrl avUrl2 = liveUrls.get(1);
//            url2 = avUrl2.getUrl();
//        }
//        ClipToBoard(url, url2);
    }

    /**
     * 将地址黏贴到黏贴版
     *
     * @param url
     * @param url2
     */
    private void ClipToBoard(final String url, final String url2) {
//        SxbLog.i(TAG, "ClipToBoard url " + url);
//        SxbLog.i(TAG, "ClipToBoard url2 " + url2);
//        if (url == null) return;
//        final Dialog dialog = new Dialog(this, R.style.dialog);
//        dialog.setContentView(R.layout.clip_dialog);
//        TextView urlText = ((TextView) dialog.findViewById(R.id.url1));
//        TextView urlText2 = ((TextView) dialog.findViewById(R.id.url2));
//        Button btnClose = ((Button) dialog.findViewById(R.id.close_dialog));
//        urlText.setText(url);
//        urlText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ClipboardManager clip = (ClipboardManager) getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
//                clip.setText(url);
//                Toast.makeText(LiveActivity.this, getResources().getString(R.string.clip_tip), Toast.LENGTH_SHORT).show();
//            }
//        });
//        if (url2 == null) {
//            urlText2.setVisibility(View.GONE);
//        } else {
//            urlText2.setText(url2);
//            urlText2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ClipboardManager clip = (ClipboardManager) getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
//                    clip.setText(url2);
//                    Toast.makeText(LiveActivity.this, getResources().getString(R.string.clip_tip), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

    }


    private Dialog recordDialog;
    private TIMAvManager.RecordParam mRecordParam;
    private String filename = "";
    private String tags = "";
    private String classId = "";
    private boolean mRecord = false;
    private EditText filenameEditText, tagEditText, classEditText;
    private CheckBox trancodeCheckBox, screenshotCheckBox, watermarkCheckBox, audioCheckBox;

    private void initRecordDialog() {
//        recordDialog = new Dialog(this, R.style.dialog);
//        recordDialog.setContentView(R.layout.record_param);
//        mRecordParam = TIMAvManager.getInstance().new RecordParam();
//
//        filenameEditText = (EditText) recordDialog.findViewById(R.id.record_filename);
//        tagEditText = (EditText) recordDialog.findViewById(R.id.record_tag);
//        classEditText = (EditText) recordDialog.findViewById(R.id.record_class);
//        trancodeCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_tran_code);
//        screenshotCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_screen_shot);
//        watermarkCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_water_mark);
//        audioCheckBox = (CheckBox) recordDialog.findViewById(R.id.record_audio);
//
//        if (filename.length() > 0) {
//            filenameEditText.setText(filename);
//        }
//        filenameEditText.setText("" + CurLiveInfo.getRoomNum());
//
//        if (tags.length() > 0) {
//            tagEditText.setText(tags);
//        }
//
//        if (classId.length() > 0) {
//            classEditText.setText(classId);
//        }
//        Button recordOk = (Button) recordDialog.findViewById(R.id.btn_record_ok);
//        recordOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SxbLog.d(TAG, LogConstants.ACTION_HOST_CREATE_ROOM + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "start record"
//                        + LogConstants.DIV + "room id " + MySelfInfo.getInstance().getMyRoomNum());
//                filename = filenameEditText.getText().toString();
//                mRecordParam.setFilename(filename);
//                tags = tagEditText.getText().toString();
//                classId = classEditText.getText().toString();
//                Log.d(TAG, "onClick classId " + classId);
//                if (classId.equals("")) {
//                    Toast.makeText(getApplicationContext(), "classID can not be empty", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                mRecordParam.setClassId(Integer.parseInt(classId));
//                mRecordParam.setTransCode(trancodeCheckBox.isChecked());
//                mRecordParam.setSreenShot(screenshotCheckBox.isChecked());
//                mRecordParam.setWaterMark(watermarkCheckBox.isChecked());
//
//                if (audioCheckBox.isChecked()) {
//                    mRecordParam.setRecordType(TIMAvManager.RecordType.AUDIO);
//                } else {
//                    mRecordParam.setRecordType(TIMAvManager.RecordType.VIDEO);
//                }
//                mLiveHelper.startRecord(mRecordParam);
//                recordDialog.dismiss();
//            }
//        });
//        Button recordCancel = (Button) recordDialog.findViewById(R.id.btn_record_cancel);
//        recordCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recordDialog.dismiss();
//            }
//        });
//        Window dialogWindow = recordDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setAttributes(lp);
//        recordDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 停止推流成功
     */
    @Override
    public void stopStreamSucc() {
        isPushed = false;
//        pushBtn.setText(R.string.live_btn_push);
    }

    @Override
    public void startRecordCallback(boolean isSucc) {
        mRecord = true;
//        recordBtn.setText(R.string.live_btn_stop_record);
    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {
        if (isSucc == true) {
            mRecord = false;
//            recordBtn.setText(R.string.live_btn_record);
        }
    }


    private VideoOrientationEventListener mOrientationEventListener;

    void registerOrientationListener() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new VideoOrientationEventListener(super.getApplicationContext(), SensorManager.SENSOR_DELAY_UI);
        }
    }

    void startOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }
    }

    void stopOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
    }


    class VideoOrientationEventListener extends OrientationEventListener {
        boolean mbIsTablet = true;

        public VideoOrientationEventListener(Context context, int rate) {
            super(context, rate);
            mbIsTablet = PhoneStatusTools.isTablet(context);
        }

        int mLastOrientation = -25;

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {      // 平放
                mLastOrientation = orientation;
                return;
            }

            if (mLastOrientation < 0) {
                mLastOrientation = 0;
            }

            if (((orientation - mLastOrientation) < 20)
                    && ((orientation - mLastOrientation) > -20)) {
                return;
            }

            //只检测是否有四个角度的改变
            if (2 == CurLiveInfo.getEquipment()) {
                if (QavsdkControl.getInstance() != null) {
                    if (orientation > 350 || orientation < 10) { //0度
                        QavsdkControl.getInstance().setRotation(0);
                    } else if (orientation > 80 && orientation < 100) { //90度
                        QavsdkControl.getInstance().setRotation(90);
                    } else if (orientation > 170 && orientation < 190) { //180度
                        QavsdkControl.getInstance().setRotation(180);
                    } else if (orientation > 260 && orientation < 280) { //270度
                        QavsdkControl.getInstance().setRotation(270);
                    }
                    return;
                }
                return;
            } else {
                if (QavsdkControl.getInstance() != null) {
                    if (orientation > 350 || orientation < 10) { //0度
                        QavsdkControl.getInstance().setRotation(0);
                    } else if (orientation > 80 && orientation < 100) { //90度
                        QavsdkControl.getInstance().setRotation(90);
                    } else if (orientation > 170 && orientation < 190) { //180度
                        QavsdkControl.getInstance().setRotation(180);
                    } else if (orientation > 260 && orientation < 280) { //270度
                        QavsdkControl.getInstance().setRotation(270);
                    }
                    return;
                }
            }
        }
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Constants.HOST != MySelfInfo.getInstance().getIdStatus()) {
                if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.RECORD_AUDIO);
                if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.WAKE_LOCK);
                if ((checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.WRITE_SETTINGS);
                if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
                if (permissionsList.size() != 0) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_PHONE_PERMISSIONS);
                }
            } else {
                if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.CAMERA);
                if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.RECORD_AUDIO);
                if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.WAKE_LOCK);
                if ((checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.WRITE_SETTINGS);
                if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
                if (permissionsList.size() != 0) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_PHONE_PERMISSIONS);
                }
            }
        }

    }

    // 清除老房间数据
    private void clearOldData() {
        mArrayListChatEntity.clear();
        mBoolNeedRefresh = true;
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
        QavsdkControl.getInstance().clearVideoData();
    }

    @Override
    public void onSlideUp() {
        if (!bReadyToChange && MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
            SxbLog.v(TAG, "ILVB-DBG|onSlideUp->enter");
            bSlideUp = true;
            bReadyToChange = true;
            if (bInAvRoom) {
                quiteLiveByPurpose();
            } else {
                clearOldData();
                mLiveListViewHelper.getPageData();
            }
        }
    }

    @Override
    public void onSlideDown() {
        if (!bReadyToChange && MySelfInfo.getInstance().getIdStatus() != Constants.HOST) {
            SxbLog.v(TAG, "ILVB-DBG|onSlideDown->enter");
            bSlideUp = false;
            bReadyToChange = true;
            if (bInAvRoom) {
                quiteLiveByPurpose();
            } else {
                clearOldData();
                mLiveListViewHelper.getPageData();
            }
        }
    }

    @Override
    public void showFirstPage(ArrayList<LiveInfoJson> livelist) {
        int index = 0, oldPos = 0;
        for (; index < livelist.size(); index++) {
            if (livelist.get(index).getAvRoomId() == CurLiveInfo.getRoomNum()) {
                oldPos = index;
                index++;
                break;
            }
        }
        if (bSlideUp) {
            index -= 2;
        }
        LiveInfoJson info = livelist.get((index + livelist.size()) % livelist.size());
        SxbLog.v(TAG, "ILVB-DBG|showFirstPage->index:" + index + "/" + oldPos + "|room:" + info.getHost().getUid() + "/" + CurLiveInfo.getHostID());

        if (null != info) {
            MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
            MySelfInfo.getInstance().setJoinRoomWay(false);
            CurLiveInfo.setHostID(info.getHost().getUid());
            CurLiveInfo.setHostName(info.getHost().getUsername());
            CurLiveInfo.setHostAvator(info.getHost().getAvatar());
            CurLiveInfo.setRoomNum(info.getAvRoomId());
            CurLiveInfo.setMembers(info.getWatchCount() + 1); // 添加自己
            CurLiveInfo.setAdmires(info.getAdmireCount());
            CurLiveInfo.setAddress(info.getLbs().getAddress());

            backGroundId = CurLiveInfo.getHostID();

            showHeadIcon(mHeadIcon, CurLiveInfo.getHostAvator());
            if (!TextUtils.isEmpty(CurLiveInfo.getHostName())) {
                mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostName(), 10));
            } else {
                mHostNameTv.setText(UIUtils.getLimitString(CurLiveInfo.getHostID(), 10));
            }
            mMemberListButton.setText(String.format("%d", CurLiveInfo.getMembers()));
            tvAdmires.setText("" + CurLiveInfo.getAdmires());

            //进入房间流程
            mEnterRoomHelper.startEnterRoom();
        }
    }


    @Override
    public void joinLiveSuc(String s) {
        JSONObject response = null;
        try {
            response = new JSONObject(s);
            CurLiveInfo.setJoinTime(response.getLong("orderId"));
            total = response.getInt("total");
            mMemberListButton.setText(String.format("%d", total));
            loadMemberData();
            CurLiveInfo.setMembers(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeLiveSuc(String s) {
        try {
            JSONObject response = new JSONObject(s);
            int userNum = response.getInt("userNum");
            Intent in = new Intent(LiveActivity.this, EndLiveActivity.class);
            in.putExtra("userNum", userNum);
            startActivity(in);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void memberExitSuc(String s) {
        JSONObject response = null;
        try {
            response = new JSONObject(s);
            int userNum = response.getInt("userNum");
            Intent in = new Intent(LiveActivity.this, EndLiveActivity.class);
            in.putExtra("userNum", userNum);
            startActivity(in);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMemberSuc(String s) {
        try {
            JSONArray result = new JSONArray(s);
            for (int i = 0; i < result.length(); i++) {
                JSONObject js = (JSONObject) result.get(i);
                if (js.getInt("user_type") == 9) {
//                            hostMember = new MemberInfo(js.getString("id"),
//                                    js.getString("nick_name"),
//                                    js.getString("head_image_url"),
//                                    js.getLong("enterTime"));
//                            mApplication.setHostInfo(hostMember);
//                            BitmapUtils bu = new BitmapUtils(AvActivity.this);
//                            bu.display(hostHead, hostMember.getHeadImagePath());
//                            hostHead.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    closeEdit();
//                                    new LiveUserInfoDialog(AvActivity.this, hostMember.getHeadImagePath(), hostMember.getUserName()) {
//                                        @Override
//                                        public void left() {
//                                            this.dismiss();
//                                        }
//                                    }.show();
//                                }
//                            });

                } else {
                    MemberInfo memberInfo = new MemberInfo();
                    memberInfo.setAvatar(js.getString("head_image_url"));
                    memberInfo.setUserName(js.getString("nick_name"));
                    memberInfo.setUserId(js.getString("id"));
                    memberInfo.setEnterTime(js.getLong("enterTime"));
                    mMemberList.add(0, memberInfo);
//                            mCommentUserList.add(0, memberInfo);
                }

            }
//                    CurLiveInfo.setMembers(mMemberList.size());
            membersAdapter = new MembersAdapter(LiveActivity.this, mMemberList);
            memberList.setAdapter(membersAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMsgSuc(String s) {
        mButtonSendMsg.setEnabled(true);
        ChatEntity entity = new ChatEntity();
        entity.setContext(AppManager.getUserInfo(LiveActivity.this).getNickName() + "&" + editCommit.getText().toString());
        entity.setSenderName(AppManager.getUserInfo(LiveActivity.this).getNickName());
        entity.setSendId(AppManager.getUserId(BaseApplication.getContext()));

        Log.d(TAG, "showTextMessage  isSelf " + true);
        mArrayListChatEntity.add(entity);
        refreshChatAdapter();
        editCommit.setText("");
        closeEdit();
    }

    @Override
    public void getPDFSuc(String s) {
        RxBus.get().post(RxConstant.LIVE_PDF_SUC, s);
    }
}
