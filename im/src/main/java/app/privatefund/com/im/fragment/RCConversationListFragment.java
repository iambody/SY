//package app.privatefund.com.im.fragment;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Parcel;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cgbsoft.lib.base.webview.CwebNetConfig;
//import com.cgbsoft.lib.base.webview.WebViewConstant;
//import com.cgbsoft.lib.utils.cache.SPreference;
//import com.cgbsoft.lib.utils.constant.Constant;
//import com.cgbsoft.lib.utils.net.NetConfig;
//import com.cgbsoft.lib.utils.tools.CollectionUtils;
//import com.cgbsoft.lib.utils.tools.NavigationUtils;
//import com.cgbsoft.lib.utils.tools.ThreadUtils;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//import app.privatefund.com.im.MessageListActivity;
//import app.privatefund.com.im.R;
//import app.privatefund.com.im.adapter.RcConversationListAdapter;
//import io.rong.common.RLog;
//import io.rong.imkit.RongContext;
//import io.rong.imkit.RongIM;
//import io.rong.imkit.fragment.UriFragment;
//import io.rong.imkit.model.Event;
//import io.rong.imkit.model.GroupUserInfo;
//import io.rong.imkit.model.UIConversation;
//import io.rong.imkit.widget.ArraysDialogFragment;
//import io.rong.imkit.widget.adapter.ConversationListAdapter;
//import io.rong.imlib.MessageTag;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//import io.rong.imlib.model.Discussion;
//import io.rong.imlib.model.Group;
//import io.rong.imlib.model.Message;
//import io.rong.imlib.model.MessageContent;
//import io.rong.imlib.model.PublicServiceProfile;
//import io.rong.imlib.model.UserInfo;
//import io.rong.message.ReadReceiptMessage;
//import io.rong.message.VoiceMessage;
//
///**
// * Created by lee on 2016/9/16.
// */
//public class RCConversationListFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ConversationListAdapter.OnPortraitItemClick {
//    private static String TAG = "ConvListFrag";
//    public static final String noticeId = "公告";
//    private RcConversationListAdapter mAdapter;
//    private ListView mList;
//    private TextView mNotificationBar;
//    private boolean isShowWithoutConnected = false;
//    private ArrayList<Conversation.ConversationType> mSupportConversationList = new ArrayList();
//    private ArrayList<Message> mMessageCache = new ArrayList();
//    private boolean isInit = false;
//    private boolean isNoticeList;
//    private boolean isInitNotice;
//    private List<UIConversation> cacheConversation = new ArrayList<>();
//    private RongIMClient.ResultCallback<List<Conversation>> mCallback = new RongIMClient.ResultCallback() {
//        @Override
//        public void onSuccess(Object o) {
//            List<Conversation> conversations = (List<Conversation>) o;
//            RLog.d("RCConversationListFragment", "Fragment onSuccess callback : list = " + (conversations != null ? Integer.valueOf(conversations.size()) : "null"));
//            if (RCConversationListFragment.this.mAdapter != null && RCConversationListFragment.this.mAdapter.getCount() != 0) {
//                RCConversationListFragment.this.mAdapter.clear();
//            }
//
//            if (conversations != null && conversations.size() != 0) {
//                RCConversationListFragment.this.makeUiConversationList(conversations);
//                if (RCConversationListFragment.this.mList != null && RCConversationListFragment.this.mList.getAdapter() != null) {
//                    RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                }
//
//            } else {
//                if (RCConversationListFragment.this.mAdapter != null) {
//                    RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                }
//            }
//        }
//
//        public void onError(RongIMClient.ErrorCode e) {
//            RLog.d("RCConversationListFragment", "initFragment onError callback, e=" + e);
//            if (e.equals(RongIMClient.ErrorCode.IPC_DISCONNECT)) {
//                RCConversationListFragment.this.isShowWithoutConnected = true;
//            }
//        }
//    };
//
//    public RCConversationListFragment() {
//    }
//
//    public static RCConversationListFragment getInstance() {
//        return new RCConversationListFragment();
//    }
//
//    public void onCreate(Bundle savedInstanceState) {
//        RLog.d("RCConversationListFragment", "onCreate");
//        super.onCreate(savedInstanceState);
//        this.mSupportConversationList.clear();
//        RongContext.getInstance().getEventBus().register(this);
//        if (getArguments() != null) {
//            isNoticeList = getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false);
//            Log.i("onCreate","------isNoticeList=" + isNoticeList);
//        }
//    }
//
//    public void onAttach(Activity activity) {
//        RLog.d("RCConversationListFragment", "onAttach");
//        super.onAttach(activity);
//    }
//
//    protected void initFragment(Uri uri) {
//        Conversation.ConversationType[] conversationType = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.DISCUSSION, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.CHATROOM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
//        RLog.d("RCConversationListFragment", "initFragment");
//        if (uri == null) {
//            RongIM.getInstance().getRongIMClient().getConversationList(this.mCallback);
//        } else {
//            Conversation.ConversationType[] arr$ = conversationType;
//            int len$ = conversationType.length;
//
//            for (int i$ = 0; i$ < len$; ++i$) {
//                Conversation.ConversationType type = arr$[i$];
//                if (uri.getQueryParameter(type.getName()) != null) {
//                    this.mSupportConversationList.add(type);
//                    if ("false".equals(uri.getQueryParameter(type.getName())) || type == Conversation.ConversationType.GROUP) {
//                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(false));
//                    } else if ("true".equals(uri.getQueryParameter(type.getName()))) {
//                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(true));
//                    }
//                }
//            }
//
//            if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//                if (this.mSupportConversationList.size() > 0) {
//                    RongIM.getInstance().getRongIMClient().getConversationList(this.mCallback, (Conversation.ConversationType[]) this.mSupportConversationList.toArray(new Conversation.ConversationType[this.mSupportConversationList.size()]));
//                } else {
//                    RongIM.getInstance().getRongIMClient().getConversationList(this.mCallback);
//                }
//
//            } else {
//                Log.d("ConversationListFr", "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
//                this.isShowWithoutConnected = true;
//            }
//        }
//
//        System.out.println("-------initFragment-----isNoticeList=" + isNoticeList);
//        if (!isNoticeList) {
//            showUserBelongGroupList();
//            // showNoticeItem();
//        }
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        RLog.d("RCConversationListFragment", "onCreateView");
//        View view = inflater.inflate(io.rong.imkit.R.layout.rc_fr_conversationlist, container, false);
//        this.mNotificationBar = (TextView) this.findViewById(view, io.rong.imkit.R.id.rc_status_bar);
//        this.mNotificationBar.setVisibility(View.GONE);
//        this.mList = (ListView) this.findViewById(view, io.rong.imkit.R.id.rc_list);
//        //TextView mEmptyView = (TextView) this.findViewById(view, 16908292);
//        TextView mEmptyView = (TextView) this.findViewById(view, io.rong.imkit.R.id.rc_empty_tv);
//        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
//        } else {
//            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_not_connected));
//        }
//
//        this.mList.setEmptyView(mEmptyView);
//        return view;
//    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        if (this.mAdapter == null) {
//            this.mAdapter = new RcConversationListAdapter(RongContext.getInstance());
//        }
//        this.mList.setAdapter(this.mAdapter);
//        this.mList.setOnItemClickListener(this);
//        this.mList.setOnItemLongClickListener(this);
//        this.mAdapter.setOnPortraitItemClick(this);
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    private void showNoticeItem() {
//        if (!hasIntimeInfo()) {
//            return;
//        }
//        addNoticeItem();
//    }
//
//    private void addNoticeItem() {
//        int originalIndex = this.mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
//        MessageContent mc = new MessageContent() {
//            @Override
//            public byte[] encode() {
//                return noticeId.getBytes();
//            }
//
//            @Override
//            public int describeContents() {
//                return 0;
//            }
//
//            @Override
//            public void writeToParcel(Parcel dest, int flags) {
//            }
//        };
//
//        UIConversation conversation = this.mAdapter.getItem(originalIndex < 0 ? 0 : originalIndex);
//        long showTime = 0;
//        if (conversation != null && conversation.getConversationTargetId().equals(noticeId)) {
//            mc = conversation.getMessageContent();
//            showTime = conversation.getUIConversationTime();
//        }
//         Message message = Message.obtain(noticeId, Conversation.ConversationType.PRIVATE, mc);
//
//        UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
//        uiConversation.setConversationGatherState(false);
//        uiConversation.setIconUrl(Uri.parse(NetConfig.getDefaultRemoteLogin));
//        if (!CollectionUtils.isEmpty(cacheConversation)) {
//            UIConversation conversation1 = cacheConversation.get(0);
//            showTime = conversation1.getUIConversationTime();
//            uiConversation.setUIConversationTime(showTime == 0 ? System.currentTimeMillis() : showTime);
//            uiConversation.setConversationContent(conversation1.getConversationContent());
//            int value = 0;
//            for (UIConversation conversation2 : cacheConversation) {
//                value += conversation2.getUnReadMessageCount();
//            }
//            System.out.println("--------showTime=" + showTime + "-------value=" + value+"--------conversation1.getConversationContent()=" + conversation1.getConversationContent());
//            if (value > 0) {
//                uiConversation.setUnReadMessageCount(value);
//            }
//        }
//        uiConversation.setTop(false);
//        //conversation.setIconUrl(Uri.parse(Domain.getDefaultRemoteLogin));
//        int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
//        if (originalIndex < 0) {
//            this.mAdapter.add(uiConversation, newPosition);
//        } else if (originalIndex != newPosition) {
//            this.mAdapter.remove(originalIndex);
//            this.mAdapter.add(uiConversation, newPosition);
//        }
//        this.mAdapter.notifyDataSetChanged();
//    }
//
//    // 显示彩云追月群列表
//    private void showUserBelongGroupList() {
////        String apiParam = ApiParams.requestParamGroupList(MApplication.getUserid());
//        if (getActivity() != null) {
////            new GroupChatGroupListTask(getActivity()).start(apiParam, new HttpResponseListener() {
////                @Override
////                public void onResponse(JSONObject response) {
////                    try {
////                        Log.i(RCConversationListFragment.this.getClass().getName(), "showUserBelongGroupList=" + response.toString());
////                        String vas = String.valueOf(response.get("result"));
////                        SPSave.getInstance(getContext()).putBoolean(Constants.HAS_USER_GROUP, !"null".equals(vas) && !TextUtils.isEmpty(vas) ? true : false);
////                        if (!"null".equals(vas) && !TextUtils.isEmpty(vas)) {
////                            JSONArray jsonArray = new JSONArray(vas);
////                            for (int i = 0; i < jsonArray.length(); i++) {
////                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
////                                String id = jsonObject.optString("id");
////                                String name = jsonObject.optString("name");
////                                // {"result":[{"headImgUrl":"[{\"name\":\"937fd0a6c1cdcbda63480f56075d2baf.jpg\",\"url\":\"https:\/\/upload.simuyun.com\/android\/8716016e-163f-4073-8923-31aa15084d4d.jpg\"}]","id":"337f294bfa6a4a73959b9c516e765074","name":"互联网群聊测试"}]}
////                                String headImageUrl = jsonObject.optString("headImgUrl");
////                                if (headImageUrl.contains("name")) {
////                                    JSONArray jsonArray1 = new JSONArray(headImageUrl);
////                                    headImageUrl = jsonArray1.getJSONObject(0).optString("url");
////                                }
////                                addGroupInfo(id, name);
////                            }
////                        }
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
////
////                @Override
////                public void onErrorResponse(String error, int statueCode) {
////                }
////            });
//
//            ThreadUtils.runOnMainThreadDelay(new Runnable() {
//                @Override
//                public void run() {
//                    showNoticeItem();
//                }
//            },1000);
//        }
//    }
//
//    private boolean hasIntimeInfo() {
//        List<Conversation> list = RongIMClient.getInstance().getConversationList();
//        System.out.println("-------converstainlist=" + (CollectionUtils.isEmpty(list) ? "null" : list.size()));
//        if (!CollectionUtils.isEmpty(list)) {
//            for (Conversation conver : list) {
//                if (RongCouldUtil.getInstance().customConversation(conver.getSenderUserId())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void addGroupInfo(String id, final String name) {
//        Log.i(this.getClass().getName(), "---EXECUTE addGroupInfo");
//        int originalIndex = this.mAdapter.findPosition(Conversation.ConversationType.GROUP, id);
//        MessageContent mc = new MessageContent() {
//            @Override
//            public byte[] encode() {
//                return name.getBytes();
//            }
//
//            @Override
//            public int describeContents() {
//                return 0;
//            }
//
//            @Override
//            public void writeToParcel(Parcel dest, int flags) {
//            }
//        };
//
//        UIConversation conversation = this.mAdapter.getItem(originalIndex < 0 ? 0 : originalIndex);
//        long showTime = 0;
//        boolean isTop = false;
//        if (conversation != null && conversation.getConversationTargetId().equals(id)) {
//            mc = conversation.getMessageContent();
//            showTime = conversation.getUIConversationTime();
//            isTop = conversation.isTop();
//        }
//        final Message message = Message.obtain(id, Conversation.ConversationType.GROUP, mc);
//        UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
//        uiConversation.setConversationGatherState(false);
//        uiConversation.setTop(isTop);
//        if (showTime != 0) {
//            uiConversation.setUIConversationTime(showTime);
//        }
//        int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
//        if (isNoticeList) {
//            return;
//        }
//        if (originalIndex < 0) {
//            this.mAdapter.add(uiConversation, newPosition);
//        } else if (originalIndex != newPosition) {
//            this.mAdapter.remove(originalIndex);
//            this.mAdapter.add(uiConversation, newPosition);
//        }
//        this.mAdapter.notifyDataSetChanged();
//    }
//
//    public void onResume() {
//        super.onResume();
//        if (RongIM.getInstance() != null) {
//            RLog.d("onResume", "current connect status is:" + RongIM.getInstance().getCurrentConnectionStatus());
//            //RongNotificationManager.getInstance().onRemoveNotification(); // CHENLONG DEL
//            RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();
//            Drawable drawable = this.getActivity().getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_network_available);
//            int width = (int) this.getActivity().getResources().getDimension(R.dimen.ui_17_dip);
//            drawable.setBounds(0, 0, width, width);
//            this.mNotificationBar.setCompoundDrawablePadding(16);
//            this.mNotificationBar.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
//            if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
//                // RongIM.getInstance().getRongIMClient().reconnect((RongIMClient.ConnectCallback) null); // CHENLONG
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick));
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
//                this.mNotificationBar.setVisibility(View.GONE);
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting));
//            }
//        } else {
//            Log.d("ConversationListFr", "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
//            this.isShowWithoutConnected = true;
//        }
//    }
//
//    public void onDestroy() {
//        RLog.d("RCConversationListFragment", "onDestroy");
//        RongContext.getInstance().getEventBus().unregister(this);
//        this.getHandler().removeCallbacksAndMessages((Object) null);
//        super.onDestroy();
//    }
//
//    public void onPause() {
//        RLog.d("RCConversationListFragment", "onPause");
//        super.onPause();
//    }
//
//    public boolean onBackPressed() {
//        return false;
//    }
//
//    public void setAdapter(RcConversationListAdapter adapter) {
//        if (this.mAdapter != null) {
//            this.mAdapter.clear();
//        }
//
//        this.mAdapter = adapter;
//        if (this.mList != null && this.getUri() != null) {
//            this.mList.setAdapter(adapter);
//            this.initFragment(this.getUri());
//        }
//    }
//
//    public ConversationListAdapter getAdapter() {
//        return this.mAdapter;
//    }
//
//    public void onEventMainThread(Event.ConnectEvent event) {
//        RLog.d("onEventMainThread", "Event.ConnectEvent: isListRetrieved = " + this.isShowWithoutConnected);
//        if (this.isShowWithoutConnected) {
//            if (this.mSupportConversationList.size() > 0) {
//                RongIM.getInstance().getRongIMClient().getConversationList(this.mCallback, (Conversation.ConversationType[]) this.mSupportConversationList.toArray(new Conversation.ConversationType[this.mSupportConversationList.size()]));
//            } else {
//                RongIM.getInstance().getRongIMClient().getConversationList(this.mCallback);
//            }
//
//            TextView mEmptyView = (TextView) this.mList.getEmptyView();
//            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
//            this.isShowWithoutConnected = false;
//        }
//    }
//
//    public void onEventMainThread(Event.ReadReceiptEvent event) {
//        if (this.mAdapter == null) {
//            Log.d(TAG, "the conversation list adapter is null.");
//        } else {
//            int originalIndex = this.mAdapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
//            boolean gatherState = RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue();
//            if (!gatherState && originalIndex >= 0) {
//                UIConversation conversation = (UIConversation) this.mAdapter.getItem(originalIndex);
//                ReadReceiptMessage content = (ReadReceiptMessage) event.getMessage().getContent();
//                if (content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
//                    conversation.setSentStatus(Message.SentStatus.READ);
//                    this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
//                }
//            }
//        }
//    }
//
//    public void onEventMainThread(final Event.OnReceiveMessageEvent event) {
////        if (event.getMessage().getContent() instanceof TextMessage) {
////            RongYunDatabase.getInstance(getActivity()).saveMessage(ChatHistoryMessage.formatMessageToChatHistoryMessage(event.getMessage(), getContext()));
////        }
//
//        if (Constant.msgNoKnowInformation.equals(event.getMessage().getSenderUserId())) {
//            RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
//            RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
//        }
//
//        Log.d(TAG, "Receive MessageEvent: id=" + event.getMessage().getTargetId() + ", type=" + event.getMessage().getConversationType());
//        if ((this.mSupportConversationList.size() == 0 || this.mSupportConversationList.contains(event.getMessage().getConversationType())) && (this.mSupportConversationList.size() != 0 || event.getMessage().getConversationType() != Conversation.ConversationType.CHATROOM && event.getMessage().getConversationType() != Conversation.ConversationType.CUSTOMER_SERVICE)) {
//            if (isNoticeList && RongCouldUtil.getInstance().hideConversation(event.getMessage().getSenderUserId())) {
//                return;
//            }
//
//            if (this.mAdapter == null) {
//                Log.d(TAG, "the conversation list adapter is null. Cache the received message firstly!!!");
//                this.mMessageCache.add(event.getMessage());
//            } else {
//                int originalIndex = this.mAdapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
//                UIConversation uiConversation = this.makeUiConversation(event.getMessage(), originalIndex);
//                int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
//
//                if (isFilterNoticeInfo(event.getMessage().getSenderUserId())) {
//                    refreshUnreadMessage(event.getMessage(), originalIndex);
//                    return;
//                }
//
//                if (originalIndex < 0) {
//                    this.mAdapter.add(uiConversation, newPosition);
//                } else if (originalIndex != newPosition) {
//                    this.mAdapter.remove(originalIndex);
//                    this.mAdapter.add(uiConversation, newPosition);
//                }
//
//                this.mAdapter.notifyDataSetChanged();
//                MessageTag msgTag = (MessageTag) event.getMessage().getContent().getClass().getAnnotation(MessageTag.class);
//                if (msgTag != null && (msgTag.flag() & 3) == 3) {
//                    this.refreshUnreadCount(event.getMessage().getConversationType(), event.getMessage().getTargetId());
//                }
//
//                if (RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue()) {
//                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//
//                        @Override
//                        public void onSuccess(Object o) {
//                            List<Conversation> conversations = (List<Conversation>) o;
//                            Iterator i$ = conversations.iterator();
//
//                            while (true) {
//                                if (i$.hasNext()) {
//                                    Conversation conv = (Conversation) i$.next();
//                                    if (conversations == null || conversations.size() == 0) {
//                                        return;
//                                    }
//
//                                    if (!conv.getConversationType().equals(event.getMessage().getConversationType()) || !conv.getTargetId().equals(event.getMessage().getTargetId())) {
//                                        continue;
//                                    }
//
//                                    int pos = RCConversationListFragment.this.mAdapter.findPosition(conv.getConversationType(), conv.getTargetId());
//                                    if (pos >= 0) {
//                                        ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(pos)).setDraft(conv.getDraft());
//                                        if (TextUtils.isEmpty(conv.getDraft())) {
//                                            ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(pos)).setSentStatus((Message.SentStatus) null);
//                                        } else {
//                                            ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(pos)).setSentStatus(conv.getSentStatus());
//                                        }
//
//                                        RCConversationListFragment.this.mAdapter.getView(pos, RCConversationListFragment.this.mList.getChildAt(pos - RCConversationListFragment.this.mList.getFirstVisiblePosition()), RCConversationListFragment.this.mList);
//                                    }
//                                }
//
//                                return;
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    }, new Conversation.ConversationType[]{event.getMessage().getConversationType()});
//                }
//
//            }
//        } else {
//            Log.e(TAG, "Not included in conversation list. Return directly!");
//        }
//    }
//
//    public void onEventMainThread(Message message) {
////        if (message.getContent() instanceof TextMessage) {
////            RongYunDatabase.getInstance(getActivity()).saveMessage(ChatHistoryMessage.formatMessageToChatHistoryMessage(message, getContext()));
////        }
//
//        if (Constant.msgNoKnowInformation.equals(message.getSenderUserId())) {
//            RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, "INTIME49999");
//            RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.PRIVATE, "INTIME49999");
//        }
//
//        RLog.d("onEventMainThread", "Receive Message: name=" + message.getObjectName() + ", type=" + message.getConversationType());
//        if (this.mSupportConversationList.size() != 0 && !this.mSupportConversationList.contains(message.getConversationType()) || this.mSupportConversationList.size() == 0 && (message.getConversationType() == Conversation.ConversationType.CHATROOM || message.getConversationType() == Conversation.ConversationType.CUSTOMER_SERVICE)) {
//            RLog.d("onEventBackgroundThread", "Not included in conversation list. Return directly!");
//        } else {
//            int originalIndex = this.mAdapter.findPosition(message.getConversationType(), message.getTargetId());
//            UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
//            int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
//
//            if (isFilterNoticeInfo(message.getSenderUserId())) {
//                refreshUnreadMessage(message, originalIndex);
//                return;
//            }
//
//            if (originalIndex >= 0) {
//                if (newPosition == originalIndex) {
//                    this.mAdapter.getView(newPosition, this.mList.getChildAt(newPosition - this.mList.getFirstVisiblePosition()), this.mList);
//                } else {
//                    this.mAdapter.remove(originalIndex);
//                    this.mAdapter.add(uiConversation, newPosition);
//                    this.mAdapter.notifyDataSetChanged();
//                }
//            } else {
//                this.mAdapter.add(uiConversation, newPosition);
//                this.mAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    private boolean isFilterNoticeInfo(String messageId) {
//        if (isNoticeList) {
//            return !RongCouldUtil.getInstance().customConversation(messageId);
//        } else {
//            return RongCouldUtil.getInstance().customConversation(messageId);
//        }
//    }
//
//    public void onEventMainThread(MessageContent content) {
//        RLog.d("onEventMainThread:", "MessageContent");
//        for (int index = 0; index < this.mAdapter.getCount(); ++index) {
//            UIConversation tempUIConversation = (UIConversation) this.mAdapter.getItem(index);
//            if (content != null && tempUIConversation.getMessageContent() != null && tempUIConversation.getMessageContent() == content) {
//                tempUIConversation.setMessageContent(content);
//                tempUIConversation.setConversationContent(tempUIConversation.buildConversationContent(tempUIConversation));
//                if (index >= this.mList.getFirstVisiblePosition()) {
//                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
//                }
//            } else {
//                RLog.e("onEventMainThread", "MessageContent is null");
//            }
//        }
//
//    }
//
//    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
//        RLog.d("ConnectionStatus", status.toString());
//        if (this.isResumed() && this.getResources().getBoolean(io.rong.imkit.R.bool.rc_is_show_warning_notification)) {
//            Drawable drawable = this.getActivity().getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_network_available);
//            int width = (int) this.getActivity().getResources().getDimension(R.dimen.ui_17_dip);
//            drawable.setBounds(0, 0, width, width);
//            this.mNotificationBar.setCompoundDrawablePadding(16);
//            this.mNotificationBar.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
//            if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
//                // RongIM.getInstance().getRongIMClient().reconnect((RongIMClient.ConnectCallback) null); //CHENLONG
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick));
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
//                this.mNotificationBar.setVisibility(View.GONE);
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable));
//            } else if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
//                this.mNotificationBar.setVisibility(View.VISIBLE);
//                this.mNotificationBar.setText(this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting));
//            }
//        }
//    }
//
//    public void onEventMainThread(Event.CreateDiscussionEvent createDiscussionEvent) {
//        RLog.d("onEventBackgroundThread:", "createDiscussionEvent");
//        UIConversation conversation = new UIConversation();
//        conversation.setConversationType(Conversation.ConversationType.DISCUSSION);
//        if (createDiscussionEvent.getDiscussionName() != null) {
//            conversation.setUIConversationTitle(createDiscussionEvent.getDiscussionName());
//        } else {
//            conversation.setUIConversationTitle("");
//        }
//
//        conversation.setConversationTargetId(createDiscussionEvent.getDiscussionId());
//        conversation.setUIConversationTime(System.currentTimeMillis());
//        boolean isGather = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.DISCUSSION.getName()).booleanValue();
//        conversation.setConversationGatherState(isGather);
//        if (isGather) {
//            String gatherPosition = RongContext.getInstance().getGatheredConversationTitle(conversation.getConversationType());
//            conversation.setUIConversationTitle(gatherPosition);
//        }
//
//        int gatherPosition1 = this.mAdapter.findGatherPosition(Conversation.ConversationType.DISCUSSION);
//        if (isNoticeList) {
//            return;
//        }
//        if (gatherPosition1 == -1) {
//            this.mAdapter.add(conversation, ConversationListUtils.findPositionForNewConversation(conversation, this.mAdapter));
//            this.mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public void onEventMainThread(Draft draft) {
//        Conversation.ConversationType curType = Conversation.ConversationType.setValue(draft.getType().intValue());
//        if (curType == null) {
//            throw new IllegalArgumentException("the type of the draft is unknown!");
//        } else {
//            RLog.i("onEventMainThread(draft)", curType.getName());
//            int position = this.mAdapter.findPosition(curType, draft.getId());
//            if (position >= 0) {
//                UIConversation conversation = (UIConversation) this.mAdapter.getItem(position);
//                if (conversation.getConversationTargetId().equals(draft.getId())) {
//                    conversation.setDraft(draft.getContent());
//                    if (!TextUtils.isEmpty(draft.getContent())) {
//                        conversation.setSentStatus((Message.SentStatus) null);
//                    }
//
//                    this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
//                }
//            }
//        }
//    }
//
//    public void onEventMainThread(Group groupInfo) {
//        int count = this.mAdapter.getCount();
//        RLog.d("onEventMainThread", "Group: name=" + groupInfo.getName() + ", id=" + groupInfo.getId());
//        if (groupInfo.getName() != null) {
//            for (int i = 0; i < count; ++i) {
//                UIConversation item = (UIConversation) this.mAdapter.getItem(i);
//                if (item != null && item.getConversationType().equals(Conversation.ConversationType.GROUP) && item.getConversationTargetId().equals(groupInfo.getId())) {
//                    boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
//                    if (gatherState) {
//                        SpannableStringBuilder builder = new SpannableStringBuilder();
//                        Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
//                        if (item.getMessageContent() instanceof VoiceMessage) {
//                            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
//                            if (isListened) {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
//                            } else {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
//                            }
//                        }
//
//                        builder.append(groupInfo.getName()).append(" : ").append(messageData);
//                        item.setConversationContent(builder);
//                        if (groupInfo.getPortraitUri() != null) {
//                            item.setIconUrl(groupInfo.getPortraitUri());
//                        }
//                    } else {
//                        item.setUIConversationTitle(groupInfo.getName());
//                        if (groupInfo.getPortraitUri() != null) {
//                            item.setIconUrl(groupInfo.getPortraitUri());
//                        }
//                    }
//
//                    this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//                } else {
//                    addGroupInfo(groupInfo.getId(), groupInfo.getName());
//                }
//            }
//        }
//    }
//
//    public void onEventMainThread(Discussion discussion) {
//        int count = this.mAdapter.getCount();
//        RLog.d("onEventMainThread", "Discussion: name=" + discussion.getName() + ", id=" + discussion.getId());
//
//        for (int i = 0; i < count; ++i) {
//            UIConversation item = (UIConversation) this.mAdapter.getItem(i);
//            if (item != null && item.getConversationType().equals(Conversation.ConversationType.DISCUSSION) && item.getConversationTargetId().equals(discussion.getId())) {
//                boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
//                if (gatherState) {
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
//                    if (messageData != null) {
//                        if (item.getMessageContent() instanceof VoiceMessage) {
//                            boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
//                            if (isListened) {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
//                            } else {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
//                            }
//                        }
//
//                        builder.append(discussion.getName()).append(" : ").append(messageData);
//                    } else {
//                        builder.append(discussion.getName());
//                    }
//
//                    item.setConversationContent(builder);
//                } else {
//                    item.setUIConversationTitle(discussion.getName());
//                }
//
//                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//            }
//        }
//    }
//
//    public void onEventMainThread(Event.GroupUserInfoEvent event) {
//        int count = this.mAdapter.getCount();
//        GroupUserInfo userInfo = event.getUserInfo();
//        Log.d("qinxiao", "GroupUserInfoEvent: " + userInfo.getUserId());
//        if (userInfo != null && userInfo.getNickname() != null) {
//            for (int i = 0; i < count; ++i) {
//                UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(i);
//                Conversation.ConversationType type = uiConversation.getConversationType();
//                boolean gatherState = RongContext.getInstance().getConversationGatherState(uiConversation.getConversationType().getName()).booleanValue();
//                boolean isShowName;
//                if (uiConversation.getMessageContent() == null) {
//                    isShowName = false;
//                } else {
//                    isShowName = RongContext.getInstance().getMessageProviderTag(uiConversation.getMessageContent().getClass()).showSummaryWithName();
//                }
//
//                if (!gatherState && isShowName && type.equals(Conversation.ConversationType.GROUP) && uiConversation.getConversationSenderId().equals(userInfo.getUserId())) {
//                    Spannable messageData = RongContext.getInstance().getMessageTemplate(uiConversation.getMessageContent().getClass()).getContentSummary(uiConversation.getMessageContent());
//                    SpannableStringBuilder builder = new SpannableStringBuilder();
//                    if (uiConversation.getMessageContent() instanceof VoiceMessage) {
//                        boolean isListened = RongIM.getInstance().getRongIMClient().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId()).getReceivedStatus().isListened();
//                        if (isListened) {
//                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
//                        } else {
//                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
//                        }
//                    }
//
//                    if (uiConversation.getConversationTargetId().equals(userInfo.getGroupId())) {
//                        uiConversation.addNickname(userInfo.getUserId());
//                        builder.append(userInfo.getNickname()).append(" : ").append(messageData);
//                        uiConversation.setConversationContent(builder);
//                    }
//
//                    this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//                }
//            }
//        }
//    }
//
//    public void onEventMainThread(UserInfo userInfo) {
//        RLog.d("onEventMainThread", "UserInfo: name=" + userInfo.getName());
//        int count = this.mAdapter.getCount();
//        if (userInfo.getName() != null) {
//            for (int i = 0; i < count; ++i) {
//                UIConversation temp = (UIConversation) this.mAdapter.getItem(i);
//                String type = temp.getConversationType().getName();
//                boolean gatherState = RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName()).booleanValue();
//                if (!temp.hasNickname(userInfo.getUserId())) {
//                    boolean isShowName = false;
//                    if (temp.getMessageContent() == null) {
//                        isShowName = false;
//                    } else if (temp.getMessageContent() != null && RongContext.getInstance().getMessageProviderTag(temp.getMessageContent().getClass()) != null) {
//                        isShowName = RongContext.getInstance().getMessageProviderTag(temp.getMessageContent().getClass()).showSummaryWithName();
//                    }
//
//                    Spannable messageData;
//                    SpannableStringBuilder builder;
//                    boolean isListened;
//                    if (!gatherState && isShowName && (type.equals("group") || type.equals("discussion")) && temp.getConversationSenderId().equals(userInfo.getUserId())) {
//                        messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
//                        builder = new SpannableStringBuilder();
//                        if (temp.getMessageContent() instanceof VoiceMessage) {
//                            isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
//                            if (isListened) {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
//                            } else {
//                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
//                            }
//                        }
//
//                        builder.append(userInfo.getName()).append(" : ").append(messageData);
//                        temp.setConversationContent(builder);
//                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//                    } else if (temp.getConversationTargetId().equals(userInfo.getUserId())) {
//                        if (gatherState || type != "private" && type != "system") {
//                            if (isShowName) {
//                                messageData = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
//                                builder = new SpannableStringBuilder();
//                                if (messageData != null) {
//                                    if (temp.getMessageContent() instanceof VoiceMessage) {
//                                        isListened = RongIM.getInstance().getRongIMClient().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
//                                        if (isListened) {
//                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
//                                        } else {
//                                            messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
//                                        }
//                                    }
//
//                                    builder.append(userInfo.getName()).append(" : ").append(messageData);
//                                } else {
//                                    builder.append(userInfo.getName());
//                                }
//
//                                temp.setConversationContent(builder);
//                                temp.setIconUrl(userInfo.getPortraitUri());
//                            }
//                        } else {
//                            temp.setUIConversationTitle(userInfo.getName());
//                            temp.setIconUrl(userInfo.getPortraitUri());
//                        }
//
//                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//                    }
//                }
//            }
//        }
//    }
//
//    public void onEventMainThread(Event.MessageRecallEvent event) {
//        int count = this.mAdapter.getCount();
//
//        for (int i = 0; i < count; ++i) {
//            if (event.getMessageId() == ((UIConversation) this.mAdapter.getItem(i)).getLatestMessageId()) {
//                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
//                if (gatherState) {
//                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback() {
//                        public void onSuccess(Object object) {
//                            List<Conversation> conversationList = (List<Conversation>) object;
//                            if (conversationList != null && conversationList.size() != 0) {
//                                UIConversation uiConversation = RCConversationListFragment.this.makeUIConversationFromList(conversationList);
//                                int oldPos = RCConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
//                                if (oldPos >= 0) {
//                                    RCConversationListFragment.this.mAdapter.remove(oldPos);
//                                }
//
//                                int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, RCConversationListFragment.this.mAdapter);
//                                RCConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
//                                RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    }, new Conversation.ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});
//                } else {
//                    RongIM.getInstance().getConversation(((UIConversation) this.mAdapter.getItem(i)).getConversationType(), ((UIConversation) this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback() {
//                        public void onSuccess(Object object) {
//                            Conversation conversation = (Conversation) object;
//                            if (conversation == null) {
//                                RLog.d(RCConversationListFragment.this.getClass().getName(), "onEventMainThread getConversation : onSuccess, conversation = null");
//                            } else {
//                                UIConversation temp = UIConversation.obtain(conversation, false);
//                                int pos = RCConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
//                                if (pos >= 0) {
//                                    RCConversationListFragment.this.mAdapter.remove(pos);
//                                }
//
//                                int newPosition = ConversationListUtils.findPositionForNewConversation(temp, RCConversationListFragment.this.mAdapter);
//                                RCConversationListFragment.this.mAdapter.add(temp, newPosition);
//                                RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    });
//                }
//                break;
//            }
//        }
//    }
//
//    public void onEventMainThread(PublicServiceProfile accountInfo) {
//        int count = this.mAdapter.getCount();
//        boolean gatherState = RongContext.getInstance().getConversationGatherState(accountInfo.getConversationType().getName()).booleanValue();
//
//        for (int i = 0; i < count; ++i) {
//            if (((UIConversation) this.mAdapter.getItem(i)).getConversationType().equals(accountInfo.getConversationType()) && ((UIConversation) this.mAdapter.getItem(i)).getConversationTargetId().equals(accountInfo.getTargetId()) && !gatherState) {
//                ((UIConversation) this.mAdapter.getItem(i)).setUIConversationTitle(accountInfo.getName());
//                ((UIConversation) this.mAdapter.getItem(i)).setIconUrl(accountInfo.getPortraitUri());
//                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
//                break;
//            }
//        }
//    }
//
//    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
//        if (event != null && !event.isFollow()) {
//            int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
//            if (isNoticeList) {
//                return;
//            }
//            if (originalIndex >= 0) {
//                this.mAdapter.remove(originalIndex);
//                this.mAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    public void onEventMainThread(final Event.ConversationUnreadEvent unreadEvent) {
//        int targetIndex = this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
//        RLog.d("onEventMainThread", "ConversationUnreadEvent: name=");
//        if (targetIndex >= 0) {
//            UIConversation temp = (UIConversation) this.mAdapter.getItem(targetIndex);
//            boolean gatherState = temp.getConversationGatherState();
//            if (gatherState) {
//                RongIM.getInstance().getRongIMClient().getUnreadCount(new RongIMClient.ResultCallback() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Integer count = (Integer) o;
//                        if (unreadEvent.getTargetId().equals(noticeId)) {
////                            EventBus.getDefault().post(new UnReadInfomation(count));
//                            return;
//                        }
//                        int pos = RCConversationListFragment.this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
//                        if (pos >= 0) {
//                            ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(pos)).setUnReadMessageCount(count.intValue());
//                            RCConversationListFragment.this.mAdapter.getView(pos, RCConversationListFragment.this.mList.getChildAt(pos - RCConversationListFragment.this.mList.getFirstVisiblePosition()), RCConversationListFragment.this.mList);
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode e) {
//                        System.err.print("Throw exception when get unread message count from ipc remote side!");
//                    }
//                }, new Conversation.ConversationType[]{unreadEvent.getType()});
//            } else {
//                if (unreadEvent.getTargetId().equals(noticeId)) {
////                    EventBus.getDefault().post(new UnReadInfomation(0));
//                    return;
//                }
//                temp.setUnReadMessageCount(0);
//                RLog.d("onEventMainThread", "ConversationUnreadEvent: set unRead count to be 0");
//                this.mAdapter.getView(targetIndex, this.mList.getChildAt(targetIndex - this.mList.getFirstVisiblePosition()), this.mList);
//            }
//        }
//    }
//
////    public void onEventMainThread(UnReadInfomation unReadInfomation) {
////        Log.i("onEventMainThread","-----unReadInfomation=" + unReadInfomation.getReadCount());
////        int headCount = unReadInfomation.getReadCount();
////        int originalIndex = this.mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
////        UIConversation conversation = this.mAdapter.getItem(originalIndex < 0 ? 0 : originalIndex);
////        conversation.setUnReadMessageCount(headCount);
////        int newPosition = ConversationListUtils.findPositionForNewConversation(conversation, this.mAdapter);
////        if (originalIndex < 0) {
////            this.mAdapter.add(conversation, newPosition);
////        } else {
////            this.mAdapter.remove(originalIndex);
////            this.mAdapter.add(conversation, newPosition);
////        }
////        mAdapter.notifyDataSetChanged();
////    }
//
//    private void refreshUnreadMessage(Message message, int position) {
//        if (!isNoticeList && RongCouldUtil.getInstance().customConversation(message.getSenderUserId())) {
//            System.out.println("-------conversation-----unReadMessage");
//            UIConversation conversation = null;
//            int originalIndex = this.mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
//            if (originalIndex < 0) {
//                addNoticeItem();
//                originalIndex = mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
//                conversation = this.mAdapter.getItem(originalIndex);
//            } else {
//                conversation = this.mAdapter.getItem(originalIndex);
//                if (conversation == null) {
//                    MessageContent mc = new MessageContent() {
//                        @Override
//                        public byte[] encode() {
//                            return "公告".getBytes();
//                        }
//
//                        @Override
//                        public int describeContents() {
//                            return 0;
//                        }
//
//                        @Override
//                        public void writeToParcel(Parcel dest, int flags) {
//                        }
//                    };
//                    Message targetMessage = Message.obtain(noticeId, Conversation.ConversationType.PRIVATE, mc);
//                    conversation = this.makeUiConversation(targetMessage, position);
//                }
//            }
//
//            if (conversation != null) {
//                conversation.setUnReadMessageCount(conversation.getUnReadMessageCount() + 1);
//                int newPosition = ConversationListUtils.findPositionForNewConversation(conversation, this.mAdapter);
//
//                long showTime = conversation.getSentStatus() == Message.SentStatus.SENT ? message.getSentTime() : message.getReceivedTime();
//                if (showTime != 0) {
//                    conversation.setUIConversationTime(showTime);
//                }
//
//                int unread = 0;
//                for (UIConversation uiConversation : cacheConversation) {
//                    unread += uiConversation.getUnReadMessageCount();
//                }
//                if (unread > 0) {
//                    conversation.setUnReadMessageCount(unread);
//                }
//
//                try {
//                    Field field = message.getContent().getClass().getDeclaredField("content");/////这个对应的是属性
//                    field.setAccessible(true);
//                    Object value = field.get(message.getContent());
//                    if (value != null && value instanceof String) {
//                        String content = String.valueOf(value);
//                        System.out.println("------content=" + content);
//                        conversation.setConversationContent(Spannable.Factory.getInstance().newSpannable(content));
//                    }
//                } catch (Exception e) {
//                }
//
//                if (originalIndex < 0) {
//                    this.mAdapter.add(conversation, newPosition);
//                } else {
//                    this.mAdapter.remove(originalIndex);
//                    this.mAdapter.add(conversation, newPosition);
//                }
//
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    public void onEventMainThread(final Event.ConversationTopEvent setTopEvent) throws IllegalAccessException {
//        int originalIndex = this.mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
//        if (originalIndex >= 0) {
//            UIConversation temp = (UIConversation) this.mAdapter.getItem(originalIndex);
//            boolean originalValue = temp.isTop();
//            if (originalValue != setTopEvent.isTop()) {
//                if (temp.getConversationGatherState()) {
//                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            List<Conversation> conversations = (List<Conversation>) o;
//                            if (conversations != null && conversations.size() != 0) {
//                                UIConversation newConversation = RCConversationListFragment.this.makeUIConversationFromList(conversations);
//                                int pos = RCConversationListFragment.this.mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
//                                if (pos >= 0) {
//                                    RCConversationListFragment.this.mAdapter.remove(pos);
//                                }
//
//                                int newIndex = ConversationListUtils.findPositionForNewConversation(newConversation, RCConversationListFragment.this.mAdapter);
//                                RCConversationListFragment.this.mAdapter.add(newConversation, newIndex);
//
//                                RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    }, new Conversation.ConversationType[]{temp.getConversationType()});
//                } else {
//                    int newIndex;
//                    if (originalValue) {
//                        temp.setTop(false);
//                        newIndex = ConversationListUtils.findPositionForCancleTop(originalIndex, this.mAdapter);
//                    } else {
//                        temp.setTop(true);
//                        newIndex = ConversationListUtils.findPositionForSetTop(temp, this.mAdapter);
//                    }
//
//                    if (originalIndex == newIndex) {
//                        this.mAdapter.getView(newIndex, this.mList.getChildAt(newIndex - this.mList.getFirstVisiblePosition()), this.mList);
//                    } else {
//                        this.mAdapter.remove(originalIndex);
//                        this.mAdapter.add(temp, newIndex);
//                        this.mAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        } else {
//            throw new IllegalAccessException("the item has already been deleted!");
//        }
//    }
//
//    public void onEventMainThread(final Event.ConversationRemoveEvent removeEvent) {
//        int removedIndex = this.mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
//        boolean gatherState = RongContext.getInstance().getConversationGatherState(removeEvent.getType().getName()).booleanValue();
//        if (!gatherState) {
//            if (removedIndex >= 0) {
//                this.mAdapter.remove(removedIndex);
//                this.mAdapter.notifyDataSetChanged();
//            }
//        } else if (removedIndex >= 0) {
//            RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//                @Override
//                public void onSuccess(Object o) {
//                    List<Conversation> conversationList = (List<Conversation>) o;
//                    int oldPos = RCConversationListFragment.this.mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
//                    if (conversationList != null && conversationList.size() != 0) {
//                        UIConversation newConversation = RCConversationListFragment.this.makeUIConversationFromList(conversationList);
//                        if (oldPos >= 0) {
//                            RCConversationListFragment.this.mAdapter.remove(oldPos);
//                        }
//
//                        int newIndex = ConversationListUtils.findPositionForNewConversation(newConversation, RCConversationListFragment.this.mAdapter);
//                        RCConversationListFragment.this.mAdapter.add(newConversation, newIndex);
//                        RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                    } else {
//                        if (oldPos >= 0) {
//                            RCConversationListFragment.this.mAdapter.remove(oldPos);
//                        }
//                        RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                public void onError(RongIMClient.ErrorCode e) {
//                }
//            }, new Conversation.ConversationType[]{removeEvent.getType()});
//        }
//
//    }
//
//    public void onEventMainThread(Event.MessageDeleteEvent event) {
//        int count = this.mAdapter.getCount();
//
//        for (int i = 0; i < count; ++i) {
//            if (event.getMessageIds().contains(Integer.valueOf(((UIConversation) this.mAdapter.getItem(i)).getLatestMessageId()))) {
//                boolean gatherState = ((UIConversation) this.mAdapter.getItem(i)).getConversationGatherState();
//                if (gatherState) {
//                    RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//                        @Override
//                        public void onSuccess(Object o) {
//                            List<Conversation> conversationList = (List<Conversation>) o;
//                            if (conversationList != null && conversationList.size() != 0) {
//                                UIConversation uiConversation = RCConversationListFragment.this.makeUIConversationFromList(conversationList);
//                                int oldPos = RCConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
//                                if (oldPos >= 0) {
//                                    RCConversationListFragment.this.mAdapter.remove(oldPos);
//                                }
//
//                                int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, RCConversationListFragment.this.mAdapter);
//                                RCConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
//                                RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    }, new Conversation.ConversationType[]{((UIConversation) this.mAdapter.getItem(i)).getConversationType()});
//                } else {
//                    RongIM.getInstance().getRongIMClient().getConversation(((UIConversation) this.mAdapter.getItem(i)).getConversationType(), ((UIConversation) this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback() {
//
//                        @Override
//                        public void onSuccess(Object o) {
//                            Conversation conversation = (Conversation) o;
//                            if (conversation == null) {
//                                RLog.d("onEventMainThread", "getConversation : onSuccess, conversation = null");
//                            } else {
//                                UIConversation temp = UIConversation.obtain(conversation, false);
//                                int pos = RCConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
//                                if (pos >= 0) {
//                                    RCConversationListFragment.this.mAdapter.remove(pos);
//                                }
//
//                                int newPosition = ConversationListUtils.findPositionForNewConversation(temp, RCConversationListFragment.this.mAdapter);
//                                RCConversationListFragment.this.mAdapter.add(temp, newPosition);
//                                RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    });
//                }
//                break;
//            }
//        }
//
//    }
//
//    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
//        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
//        if (originalIndex >= 0) {
//            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
//        }
//    }
//
//    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
//        int originalIndex = this.mAdapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());
//        if (originalIndex >= 0) {
//            boolean gatherState = RongContext.getInstance().getConversationGatherState(clearMessagesEvent.getType().getName()).booleanValue();
//            if (gatherState) {
//                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//
//                    @Override
//                    public void onSuccess(Object o) {
//                        List<Conversation> conversationList = (List<Conversation>) o;
//                        if (conversationList != null && conversationList.size() != 0) {
//                            UIConversation uiConversation = RCConversationListFragment.this.makeUIConversationFromList(conversationList);
//                            int pos = RCConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
//                            if (pos >= 0) {
//                                RCConversationListFragment.this.mAdapter.remove(pos);
//                            }
//
//                            int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, RCConversationListFragment.this.mAdapter);
//                            RCConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
//                            RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode e) {
//                    }
//                }, new Conversation.ConversationType[]{Conversation.ConversationType.GROUP});
//            } else {
//                RongIMClient.getInstance().getConversation(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId(), new RongIMClient.ResultCallback() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Conversation conversation = (Conversation) o;
//                        if (conversation != null) {
//                            UIConversation uiConversation = UIConversation.obtain(conversation, false);
//                            int pos = RCConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
//                            if (pos >= 0) {
//                                RCConversationListFragment.this.mAdapter.remove(pos);
//                            }
//
//                            int newPos = ConversationListUtils.findPositionForNewConversation(uiConversation, RCConversationListFragment.this.mAdapter);
//                            RCConversationListFragment.this.mAdapter.add(uiConversation, newPos);
//                            RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode e) {
//                    }
//                });
//            }
//        }
//
//    }
//
//    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
//        int index = this.mAdapter.findPosition(sendErrorEvent.getMessage().getConversationType(), sendErrorEvent.getMessage().getTargetId());
//        if (index >= 0) {
//            UIConversation temp = (UIConversation) this.mAdapter.getItem(index);
//            temp.setUIConversationTime(sendErrorEvent.getMessage().getSentTime());
//            temp.setMessageContent(sendErrorEvent.getMessage().getContent());
//            temp.setConversationContent(temp.buildConversationContent(temp));
//            temp.setSentStatus(Message.SentStatus.FAILED);
//            this.mAdapter.remove(index);
//            int newPosition = ConversationListUtils.findPositionForNewConversation(temp, this.mAdapter);
//            this.mAdapter.add(temp, newPosition);
//            this.mAdapter.notifyDataSetChanged();
//        }
//
//    }
//
//    public void onEventMainThread(Event.QuitDiscussionEvent event) {
//        int index = this.mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());
//        if (index >= 0) {
//            this.mAdapter.remove(index);
//            this.mAdapter.notifyDataSetChanged();
//        }
//
//    }
//
//    public void onEventMainThread(Event.QuitGroupEvent event) {
//        final int index = this.mAdapter.findPosition(Conversation.ConversationType.GROUP, event.getGroupId());
//        boolean gatherState = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.GROUP.getName()).booleanValue();
//        if (index >= 0 && gatherState) {
//            RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//                public void onSuccess(Object o) {
//                    List<Conversation> conversationList = (List<Conversation>) o;
//                    if (conversationList != null && conversationList.size() != 0) {
//                        UIConversation uiConversation = RCConversationListFragment.this.makeUIConversationFromList(conversationList);
//                        int pos = RCConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
//                        if (pos >= 0) {
//                            RCConversationListFragment.this.mAdapter.remove(pos);
//                        }
//
//                        int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, RCConversationListFragment.this.mAdapter);
//                        RCConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
//
//                        RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                    } else {
//                        if (index >= 0) {
//                            RCConversationListFragment.this.mAdapter.remove(index);
//                        }
//
//
//                        RCConversationListFragment.this.mAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                public void onError(RongIMClient.ErrorCode e) {
//                }
//            }, new Conversation.ConversationType[]{Conversation.ConversationType.GROUP});
//        } else if (index >= 0) {
//            this.mAdapter.remove(index);
//            this.mAdapter.notifyDataSetChanged();
//        }
//    }
//
////    public void onEventMainThread(Event.MessageListenedEvent event) { // CHENLONG
////        int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
////        if (originalIndex >= 0) {
////            UIConversation temp = (UIConversation) this.mAdapter.getItem(originalIndex);
////            if (temp.getLatestMessageId() == event.getLatestMessageId()) {
////                temp.setConversationContent(temp.buildConversationContent(temp));
////            }
////            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
////        }
////    }
//
//    public void onPortraitItemClick(View v, UIConversation data) {
//        Conversation.ConversationType type = data.getConversationType();
//        if (RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
//            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
//        } else {
//            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
//                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(this.getActivity(), type, data.getConversationTargetId());
//                if (isDefault) {
//                    return;
//                }
//            }
//
//            data.setUnReadMessageCount(0);
//            RongIM.getInstance().startConversation(this.getActivity(), type, data.getConversationTargetId(), data.getUIConversationTitle());
//        }
//
//    }
//
//    public boolean onPortraitItemLongClick(View v, UIConversation data) {
//        Conversation.ConversationType type = data.getConversationType();
//        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
//            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(this.getActivity(), type, data.getConversationTargetId());
//            if (isDealt) {
//                return true;
//            }
//        }
//
//        if (!RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
//            this.buildMultiDialog(data);
//            return true;
//        } else {
//            this.buildSingleDialog(data);
//            return true;
//        }
//    }
//
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        UIConversation uiconversation = (UIConversation) parent.getAdapter().getItem(position);
//        Conversation.ConversationType type = uiconversation.getConversationType();
//        if (!uiconversation.getConversationTargetId().equals(noticeId)) {
//            uiconversation.setUnReadMessageCount(0);
//            getAdapter().notifyDataSetChanged();
//        }
//
//        String targetId = uiconversation.getConversationTargetId();
//        SPreference.putBoolean(getContext(), "isTop", uiconversation.isTop());
//        RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, targetId);
//        String[] param = null;
//        HashMap<String, String> params = null;
//        switch (targetId) {
//            case "dd0cc61140504258ab474b8f0a38bb56":
//                break;
////            case "INTIME40001":
//            case "INTIME40002":
//                break;
//            case "INTIME40003":
//                break;
//            case "INTIME40004":
//                break;
//            case "INTIME40005":
//                break;
//            case "INTIME40006":
//                break;
////            case "INTIME40007":
////                param = new String[]{"客服", MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName()};
////                params = DataStatisticsUtils.getParams("1007", "10053", param);
////                DataStatisticsUtils.push(getActivity(), params);
////                break;
//            default:
//                break;
//        }
//
//        if (targetId.contains("INTIME") && !targetId.contains("INTIME40006")) {
//            if (!uiconversation.getConversationTargetId().equals(noticeId)) {
//                uiconversation.setUnReadMessageCount(0);
//                getAdapter().notifyDataSetChanged();
//            }
////            if (targetId.equals("INTIME40001")) { //  直CHENLONG播动态消息直接到二级列表页面
////                Intent intent = new Intent(getActivity(), AVLiveListActivity.class);
////                startActivity(intent);
////            } else {
//            HashMap hashMap = new HashMap();
//            hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.msgDetal + targetId);
//            hashMap.put(WebViewConstant.push_message_title, uiconversation.getUIConversationTitle());
//            hashMap.put(WebViewConstant.PAGE_SHARE_WITH_EMAIL, true);
//            hashMap.put(WebViewConstant.RIGHT_SAVE, false);
//            hashMap.put(WebViewConstant.PAGE_INIT, false);
//            NavigationUtils.startActivityByRouter(getContext(), "lib_basewebviewactivity", hashMap);
//            return;
//        }
//
//        if (RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue() && type != Conversation.ConversationType.GROUP) {
//            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
//        } else {
//            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
//                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiconversation);
//                if (isDefault) {
//                    return;
//                }
//            }
//
//            if (!uiconversation.getConversationTargetId().equals(noticeId)) {
//                uiconversation.setUnReadMessageCount(0);
//                getAdapter().notifyDataSetChanged();
//            }
//            RongIM.getInstance().startConversation(this.getActivity(), type, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());
//        }
//    }
//
//    private String formatTitleName(UIConversation conversation) {
//        if (RongCouldUtil.getInstance().customConversation(conversation.getConversationSenderId())) {
//            return getContext().getResources().getString(Constant.hashMap.get(conversation.getConversationSenderId()));
//        }
//        return conversation.getUIConversationTitle();
//    }
//
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
//        String type = uiConversation.getConversationType().getName();
//        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
//            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
//            if (isDealt) {
//                return true;
//            }
//        }
//
//        if (!RongContext.getInstance().getConversationGatherState(type).booleanValue()) {
//            this.buildMultiDialog(uiConversation);
//            return true;
//        } else {
//            this.buildSingleDialog(uiConversation);
//            return true;
//        }
//    }
//
//    private void buildMultiDialog(final UIConversation uiConversation) {
//        String[] items = new String[2];
//        if (uiConversation.isTop()) {
//            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_cancel_top);
//        } else {
//            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top);
//        }
//
//        items[1] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove);
//        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
//            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    RongIM.getInstance().getRongIMClient().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback() {
//
//                        @Override
//                        public void onSuccess(Object o) {
//                            Boolean aBoolean = (Boolean) o;
//                            if (uiConversation.isTop()) {
//                                Toast.makeText(RongContext.getInstance(), RCConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(RongContext.getInstance(), RCConversationListFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//
//                        public void onError(RongIMClient.ErrorCode e) {
//                        }
//                    });
//                } else if (which == 1) {
//                    RongIM.getInstance().getRongIMClient().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
//                }
//            }
//        }).show(this.getFragmentManager());
//    }
//
//    private void buildSingleDialog(final UIConversation uiConversation) {
//        String[] items = new String[]{RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove)};
//        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
//            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
//                RongIM.getInstance().getRongIMClient().getConversationList(new RongIMClient.ResultCallback() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        List<Conversation> conversations = (List<Conversation>) o;
//                        if (conversations != null && conversations.size() != 0) {
//                            Iterator i$ = conversations.iterator();
//
//                            while (i$.hasNext()) {
//                                Conversation conversation = (Conversation) i$.next();
//                                RongIM.getInstance().getRongIMClient().removeConversation(conversation.getConversationType(), conversation.getTargetId());
//                            }
//
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                    }
//                }, new Conversation.ConversationType[]{uiConversation.getConversationType()});
//            }
//        }).show(this.getFragmentManager());
//    }
//
//    private void makeUiConversationList(List<Conversation> conversationList) {
//        UIConversation uiCon;
//        cacheConversation.clear();
//        for (Iterator i$ = conversationList.iterator(); i$.hasNext(); this.refreshUnreadCount(uiCon.getConversationType(), uiCon.getConversationTargetId())) {
//            Conversation conversation = (Conversation) i$.next();
//            Conversation.ConversationType conversationType = conversation.getConversationType();
//            boolean gatherState = RongContext.getInstance().getConversationGatherState(conversationType.getName()).booleanValue();
//            int originalIndex = this.mAdapter.findPosition(conversationType, conversation.getTargetId());
//            uiCon = UIConversation.obtain(conversation, gatherState);
//            if (isFilterNoticeInfo(conversation.getSenderUserId())) {
//                cacheConversation.add(uiCon);
//                continue;
//            }
//            if (!RongCouldUtil.getInstance().hideConversation(conversation.getSenderUserId())) {
//                if (originalIndex < 0) {
//                    this.mAdapter.add(uiCon);
//                }
//            } else {
//                continue;
//            }
//        }
//    }
//
//    private UIConversation makeUiConversation(Message message, int pos) {
//        UIConversation uiConversation;
//        if (pos >= 0) {
//            uiConversation = (UIConversation) this.mAdapter.getItem(pos);
//            if (uiConversation != null) {
//                uiConversation.setMessageContent(message.getContent());
//                if (message.getMessageDirection() == Message.MessageDirection.SEND) {
//                    uiConversation.setUIConversationTime(message.getSentTime());
//                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//                        uiConversation.setConversationSenderId(RongIM.getInstance().getRongIMClient().getCurrentUserId());
//                    }
//                } else {
//                    uiConversation.setUIConversationTime(message.getSentTime());
//                    uiConversation.setConversationSenderId(message.getSenderUserId());
//                }
//
//                uiConversation.setConversationTargetId(message.getTargetId());
//                uiConversation.setConversationContent(uiConversation.buildConversationContent(uiConversation));
//                uiConversation.setSentStatus(message.getSentStatus());
//                uiConversation.setLatestMessageId(message.getMessageId());
//            }
//        } else {
//            uiConversation = UIConversation.obtain(message, RongContext.getInstance().getConversationGatherState(message.getConversationType().getName()).booleanValue());
//        }
//        return uiConversation;
//    }
//
//    private UIConversation makeUIConversationFromList(List<Conversation> conversations) {
//        int unreadCount = 0;
//        boolean topFlag = false;
//        Conversation newest = (Conversation) conversations.get(0);
//
//        Conversation conversation;
//        for (Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
//            conversation = (Conversation) uiConversation.next();
//            if (newest.isTop()) {
//                if (conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
//                    newest = conversation;
//                }
//            } else if (conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
//                newest = conversation;
//            }
//
//            if (conversation.isTop()) {
//                topFlag = true;
//            }
//        }
//
//        UIConversation uiConversation1 = UIConversation.obtain(newest, RongContext.getInstance().getConversationGatherState(newest.getConversationType().getName()).booleanValue());
//        uiConversation1.setUnReadMessageCount(unreadCount);
//        uiConversation1.setTop(topFlag);
//        return uiConversation1;
//    }
//
//    private void refreshUnreadCount(final Conversation.ConversationType type, final String targetId) {
//        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//            if (RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
//                RongIM.getInstance().getRongIMClient().getUnreadCount(new RongIMClient.ResultCallback() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        Integer count = (Integer) o;
//                        int curPos = RCConversationListFragment.this.mAdapter.findPosition(type, targetId);
//                        if (curPos >= 0) {
//                            ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(curPos)).setUnReadMessageCount(count.intValue());
//                            RCConversationListFragment.this.mAdapter.getView(curPos, RCConversationListFragment.this.mList.getChildAt(curPos - RCConversationListFragment.this.mList.getFirstVisiblePosition()), RCConversationListFragment.this.mList);
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode e) {
//                        System.err.print("Throw exception when get unread message count from ipc remote side!");
//                    }
//                }, new Conversation.ConversationType[]{type});
//            } else {
//                RongIM.getInstance().getRongIMClient().getUnreadCount(type, targetId, new RongIMClient.ResultCallback() {
//
//                    @Override
//                    public void onSuccess(Object o) {
//                        Integer integer = (Integer) o;
//                        int curPos = RCConversationListFragment.this.mAdapter.findPosition(type, targetId);
//                        if (curPos >= 0) {
//                            ((UIConversation) RCConversationListFragment.this.mAdapter.getItem(curPos)).setUnReadMessageCount(integer.intValue());
//                            RCConversationListFragment.this.mAdapter.getView(curPos, RCConversationListFragment.this.mList.getChildAt(curPos - RCConversationListFragment.this.mList.getFirstVisiblePosition()), RCConversationListFragment.this.mList);
//                        }
//                    }
//
//                    public void onError(RongIMClient.ErrorCode e) {
//                    }
//                });
//            }
//        }
//    }
//}
