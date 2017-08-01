package app.privatefund.com.im.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import app.privatefund.com.im.MessageListActivity;
import app.privatefund.com.im.R;
import app.privatefund.com.im.adapter.RongConversationListAdapter;
import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.R.bool;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.id;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.model.Event.AudioListenedEvent;
import io.rong.imkit.model.Event.ClearConversationEvent;
import io.rong.imkit.model.Event.ConnectEvent;
import io.rong.imkit.model.Event.ConversationNotificationEvent;
import io.rong.imkit.model.Event.ConversationRemoveEvent;
import io.rong.imkit.model.Event.ConversationTopEvent;
import io.rong.imkit.model.Event.ConversationUnreadEvent;
import io.rong.imkit.model.Event.CreateDiscussionEvent;
import io.rong.imkit.model.Event.DraftEvent;
import io.rong.imkit.model.Event.MessageDeleteEvent;
import io.rong.imkit.model.Event.MessageLeftEvent;
import io.rong.imkit.model.Event.MessageRecallEvent;
import io.rong.imkit.model.Event.MessagesClearEvent;
import io.rong.imkit.model.Event.OnMessageSendErrorEvent;
import io.rong.imkit.model.Event.OnReceiveMessageEvent;
import io.rong.imkit.model.Event.PublicServiceFollowableEvent;
import io.rong.imkit.model.Event.QuitDiscussionEvent;
import io.rong.imkit.model.Event.QuitGroupEvent;
import io.rong.imkit.model.Event.ReadReceiptEvent;
import io.rong.imkit.model.Event.RemoteMessageRecallEvent;
import io.rong.imkit.model.Event.SyncReadStatusEvent;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.widget.adapter.ConversationListAdapter.OnPortraitItemClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ReadReceiptMessage;
import io.rong.push.RongPushClient;

/**
 * @author chenlong
 *
 * 消息会话列表
 */
public class RongConversationListFragment extends UriFragment implements OnItemClickListener, OnItemLongClickListener, OnPortraitItemClick {
    private static final String noticeId = "公告";
    private String TAG = "RongConversationListFragment";
    private List<RongConversationListFragment.ConversationConfig> mConversationsConfig;
    private RongConversationListFragment mThis;
    private RongConversationListAdapter mAdapter;
    private ListView mList;
    private LinearLayout mNotificationBar;
    private ImageView mNotificationBarImage;
    private TextView mNotificationBarText;
    private boolean isShowWithoutConnected = false;
    private boolean isNoticeList;
    private List<UIConversation> cacheConversationList = new ArrayList<>();

    public RongConversationListFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mThis = this;
        this.TAG = this.getClass().getSimpleName();
        this.mConversationsConfig = new ArrayList();
        if (getArguments() != null) {
            isNoticeList = getArguments().getBoolean(MessageListActivity.IS_NOTICE_MESSAGE_LIST, false);
        }
        EventBus.getDefault().register(this);
        InternalModuleManager.getInstance().onLoaded();
    }

    protected void initFragment(Uri uri) {
        RLog.d(this.TAG, "initFragment " + uri);
        ConversationType[] defConversationType = new ConversationType[]{ConversationType.PRIVATE, ConversationType.GROUP, ConversationType.DISCUSSION, ConversationType.SYSTEM, ConversationType.CUSTOMER_SERVICE, ConversationType.CHATROOM, ConversationType.PUBLIC_SERVICE, ConversationType.APP_PUBLIC_SERVICE};
        ConversationType[] type = defConversationType;
        int arr$ = defConversationType.length;
        int len$;
        for(len$ = 0; len$ < arr$; ++len$) {
            ConversationType i$ = type[len$];
            if(uri.getQueryParameter(i$.getName()) != null) {
                RongConversationListFragment.ConversationConfig conversationType = new RongConversationListFragment.ConversationConfig();
                conversationType.conversationType = i$;
                conversationType.isGathered = uri.getQueryParameter(i$.getName()).equals("true");
                this.mConversationsConfig.add(conversationType);
            }
        }

        if(this.mConversationsConfig.size() == 0) {
            String var9 = uri.getQueryParameter("type");
            ConversationType[] var10 = defConversationType;
            len$ = defConversationType.length;

            for(int var11 = 0; var11 < len$; ++var11) {
                ConversationType var12 = var10[var11];
                if(var12.getName().equals(var9)) {
                    RongConversationListFragment.ConversationConfig config = new RongConversationListFragment.ConversationConfig();
                    config.conversationType = var12;
                    config.isGathered = false;
                    this.mConversationsConfig.add(config);
                    break;
                }
            }
        }

        this.mAdapter.clear();

        if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.DISCONNECTED)) {
            RLog.d(this.TAG, "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
            this.isShowWithoutConnected = true;
        } else {
            this.getConversationList(this.getConfigConversationTypes());
        }
    }

    /**
     * {"result":[{"headImgUrl":"[{\"name\":\"937fd0a6c1cdcbda63480f56075d2baf.jpg\",\"url\":\"https:\/\/upload.simuyun.com\/android\/8716016e-163f-4073-8923-31aa15084d4d.jpg\"}]",
     * "id":"337f294bfa6a4a73959b9c516e765074","name":"互联网群聊测试"}]}
     * 显示彩云追月群列表
     */
//    private void showUserBelongGroupList() {
//        if (getActivity() != null) {
//            ApiClient.getTestGetGroupList(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
//                @Override
//                protected void onEvent(String result) {
//                    RLog.i("ConversationListFragment", "----GroupList=" + result);
//                    try {
//                        JSONObject reslet = new JSONObject(result);
//                        JSONArray jsonArray = reslet.getJSONArray("result");
//                        if (jsonArray != null) {
//                            AppInfStore.saveHasUserGroup(getContext(), jsonArray.length() > 0 ? true : false);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                                String id = jsonObject.getString("id");
//                                String name = jsonObject.getString("name");
//                                String headImageUrl = jsonObject.getString("headImgUrl");
//                                if (headImageUrl.contains("name")) {
//                                    JSONArray jsonArray1 = new JSONArray(headImageUrl);
//                                    headImageUrl = jsonArray1.getJSONObject(0).optString("url");
//                                }
//                                addGroupInfo(id, name);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                protected void onRxError(Throwable error) {}
//            });
//            ThreadUtils.runOnMainThreadDelay(() -> {
//                addNoticeItem();
//            },1000);
//        }
//    }

//    private void addGroupInfo(String id, final String name) {
//        if (isNoticeList) {
//            return;
//        }
//        Conversation conversation = Conversation.obtain(Conversation.ConversationType.GROUP, id, name);
//        int position;
//        if(RongConversationListFragment.this.getGatherState(ConversationType.GROUP)) {
//            position = RongConversationListFragment.this.mAdapter.findGatheredItem(ConversationType.GROUP);
//        } else {
//            position = RongConversationListFragment.this.mAdapter.findPosition(ConversationType.GROUP, id);
//        }
//        conversation.setConversationTitle(name);
//        UIConversation uiConversation;
//        if(position < 0) {
//            conversation.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
//            uiConversation = UIConversation.obtain(conversation, RongConversationListFragment.this.getGatherState(ConversationType.GROUP));
//            int index = RongConversationListFragment.this.getPosition(uiConversation);
//            RongConversationListFragment.this.mAdapter.add(uiConversation, index);
//            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
//        } else {
//            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
//            uiConversation.updateConversation(conversation, RongConversationListFragment.this.getGatherState(ConversationType.GROUP));
//            RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
//        }
//    }

    private void addNoticeItem() {
        Conversation conversation = Conversation.obtain(ConversationType.PRIVATE, noticeId, "");
        int position;
        if(RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE)) {
            position = RongConversationListFragment.this.mAdapter.findGatheredItem(ConversationType.PRIVATE);
        } else {
            position = RongConversationListFragment.this.mAdapter.findPosition(ConversationType.PRIVATE, noticeId);
        }
        conversation.setConversationTitle("");
        conversation.setPortraitUrl(NetConfig.noticeRemoteLogin);
        UIConversation uiConversation;

        if(position < 0) {
            conversation.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
            uiConversation = UIConversation.obtain(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));

            if (!CollectionUtils.isEmpty(cacheConversationList)) {
                UIConversation conversation1 = cacheConversationList.get(0);
                long showTime = conversation1.getUIConversationTime();
                uiConversation.setUIConversationTime(showTime == 0 ? System.currentTimeMillis() : showTime);
                uiConversation.setConversationContent(conversation1.getConversationContent());
                int value = 0;
                for (UIConversation conversation2 : cacheConversationList) {
                    value += conversation2.getUnReadMessageCount();
                }
                System.out.println("--------showTime=" + showTime + "-------value=" + value+"--------conversation1.getConversationContent()=" + conversation1.getConversationContent());
                if (value > 0) {
                    uiConversation.setUnReadMessageCount(value);
                }
            }

//            if (!CollectionUtils.isEmpty(cacheConversationList)) {
//                UIConversation cacheConversation = cacheConversationList.get(0);
//                long showTime = cacheConversation.getUIConversationTime();
//                uiConversation.setUIConversationTime(showTime == 0 ? System.currentTimeMillis() : showTime);
//                uiConversation.setConversationContent(cacheConversation.getConversationContent());
//                int value = 0;
//                for (UIConversation conversation2 : cacheConversationList) {
//                    value += conversation2.getUnReadMessageCount();
//                }
//                if (value > 0) {
//                    uiConversation.setUnReadMessageCount(value);
//                }
//            }
            int index = RongConversationListFragment.this.getPosition(uiConversation);
            RongConversationListFragment.this.mAdapter.add(uiConversation, index);
            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
        } else {
            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
            uiConversation.updateConversation(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));
            RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
        }
    }

    public void addPlamtformServer() {
        Conversation conversation = Conversation.obtain(ConversationType.PRIVATE, Constant.msgCustomerService, "");
        int position;
        if(RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE)) {
            position = RongConversationListFragment.this.mAdapter.findGatheredItem(ConversationType.PRIVATE);
        } else {
            position = RongConversationListFragment.this.mAdapter.findPosition(ConversationType.PRIVATE, Constant.msgCustomerService);
        }
        conversation.setConversationTitle("客服");
        conversation.setTop(true);
        conversation.setPortraitUrl(NetConfig.defaultRemoteLogin);
        UIConversation uiConversation;

        if(position < 0) {
            conversation.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
            uiConversation = UIConversation.obtain(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));
            int index = RongConversationListFragment.this.getPosition(uiConversation);
            RongConversationListFragment.this.mAdapter.add(uiConversation, index);
            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
        } else {
            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
            uiConversation.updateConversation(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));
            RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
        }
    }

    public void addSystemServer() {
        Conversation conversation = Conversation.obtain(ConversationType.PRIVATE, Constant.msgSystemStatus, "");
        int position;
        if(RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE)) {
            position = RongConversationListFragment.this.mAdapter.findGatheredItem(ConversationType.PRIVATE);
        } else {
            position = RongConversationListFragment.this.mAdapter.findPosition(ConversationType.PRIVATE, Constant.msgSystemStatus);
        }
        conversation.setConversationTitle("系统通知");
        conversation.setPortraitUrl(NetConfig.systemRemoteLogin);
        UIConversation uiConversation;

        if(position < 0) {
            conversation.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
            uiConversation = UIConversation.obtain(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));
            int index = RongConversationListFragment.this.getPosition(uiConversation);
            RongConversationListFragment.this.mAdapter.add(uiConversation, index);
            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
        } else {
            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
            uiConversation.updateConversation(conversation, RongConversationListFragment.this.getGatherState(ConversationType.PRIVATE));
            RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.rc_fr_conversationlist, container, false);
        this.mNotificationBar = (LinearLayout)this.findViewById(view, id.rc_status_bar);
        this.mNotificationBar.setVisibility(8);
        this.mNotificationBarImage = (ImageView)this.findViewById(view, id.rc_status_bar_image);
        this.mNotificationBarText = (TextView)this.findViewById(view, id.rc_status_bar_text);
        View emptyView = this.findViewById(view, id.rc_conversation_list_empty_layout);
        TextView emptyText = (TextView)this.findViewById(view, id.rc_empty_tv);
        emptyText.setText(this.getActivity().getResources().getString(string.rc_conversation_list_empty_prompt));
        this.mList = (ListView)this.findViewById(view, id.rc_list);
        this.mList.setEmptyView(emptyView);
        this.mList.setOnItemClickListener(this);
        this.mList.setOnItemLongClickListener(this);
        if(this.mAdapter == null) {
            this.mAdapter = this.onResolveAdapter(this.getActivity());
        }

        this.mAdapter.setOnPortraitItemClick(this);
        this.mList.setAdapter(this.mAdapter);
        return view;
    }

    public void onResume() {
        super.onResume();
        RLog.d(this.TAG, "onResume " + RongIM.getInstance().getCurrentConnectionStatus());
        RongPushClient.clearAllPushNotifications(this.getActivity());
        this.setNotificationBarVisibility(RongIM.getInstance().getCurrentConnectionStatus());
    }

    private void getConversationList(ConversationType[] conversationTypes) {
        cacheConversationList.clear();
        this.getConversationList(conversationTypes, new IHistoryDataResultCallback<List<Conversation>>() {
            public void onResult(List<Conversation> data) {
                if (!CollectionUtils.isEmpty(data)) {
                    for (Conversation conversation : data) {
                        if (isFilterNoticeInfo(conversation.getSenderUserId())) {
                            cacheConversationList.add(UIConversation.obtain(conversation, false));
                        }
                    }
                }

                if(data != null && data.size() > 0) {
                    RongCouldUtil.customServerTop(getContext(), mAdapter);
                    RongConversationListFragment.this.makeUiConversationList(data);
                    RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                } else {
                    RLog.w(RongConversationListFragment.this.TAG, "getConversationList return null " + RongIMClient.getInstance().getCurrentConnectionStatus());
                    RongConversationListFragment.this.isShowWithoutConnected = true;
                }

                if (!isNoticeList) {
                    addNoticeItem();
                    addPlamtformServer();
                    addSystemServer();
                }

            }

            public void onError() {}
        });
    }



    public void getConversationList(ConversationType[] conversationTypes, final IHistoryDataResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
            public void onSuccess(List<Conversation> conversations) {
                if(callback != null) {
                    callback.onResult(conversations);
                }
            }

            public void onError(ErrorCode e) {
                if(callback != null) {
                    callback.onError();
                }
            }
        }, conversationTypes);
    }

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
//        Message message = Message.obtain(noticeId, Conversation.ConversationType.PRIVATE, mc);
//
//        UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
//        uiConversation.setConversationGatherState(false);
//        uiConversation.setIconUrl(Uri.parse(Domain.getDefaultRemoteLogin));
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


    public void focusUnreadItem() {
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int visibleCount = last - first + 1;
        int count = this.mList.getCount();
        if(visibleCount < count) {
            int index;
            if(last < count - 1) {
                index = first + 1;
            } else {
                index = 0;
            }

            if(!this.selectNextUnReadItem(index, count)) {
                this.selectNextUnReadItem(0, count);
            }
        }

    }

    private boolean selectNextUnReadItem(int startIndex, int totalCount) {
        int index = -1;

        for(int i = startIndex; i < totalCount; ++i) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
            if(uiConversation.getUnReadMessageCount() > 0) {
                index = i;
                break;
            }
        }

        if(index >= 0 && index < totalCount) {
            this.mList.setSelection(index);
            return true;
        } else {
            return false;
        }
    }

    private void setNotificationBarVisibility(ConnectionStatus status) {
        if(!this.getResources().getBoolean(bool.rc_is_show_warning_notification)) {
            RLog.e(this.TAG, "rc_is_show_warning_notification is disabled.");
        } else {
            String content = null;
            if(status.equals(ConnectionStatus.NETWORK_UNAVAILABLE)) {
                content = this.getResources().getString(string.rc_notice_network_unavailable);
            } else if(status.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                content = this.getResources().getString(string.rc_notice_tick);
            } else if(status.equals(ConnectionStatus.CONNECTED)) {
                this.mNotificationBar.setVisibility(8);
            } else if(status.equals(ConnectionStatus.DISCONNECTED)) {
                content = this.getResources().getString(string.rc_notice_disconnect);
            } else if(status.equals(ConnectionStatus.CONNECTING)) {
                content = this.getResources().getString(string.rc_notice_connecting);
            }

            if(content != null) {
                if(this.mNotificationBar.getVisibility() == 8) {
                    String finalContent = content;
                    this.getHandler().postDelayed(new Runnable() {
                        public void run() {
                            if(!RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                                RongConversationListFragment.this.mNotificationBar.setVisibility(0);
                                RongConversationListFragment.this.mNotificationBarText.setText(finalContent);
                                if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                                    RongConversationListFragment.this.mNotificationBarImage.setImageResource(drawable.rc_notification_connecting_animated);
                                } else {
                                    RongConversationListFragment.this.mNotificationBarImage.setImageResource(drawable.rc_notification_network_available);
                                }
                            }

                        }
                    }, 4000L);
                } else {
                    this.mNotificationBarText.setText(content);
                    if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                        this.mNotificationBarImage.setImageResource(drawable.rc_notification_connecting_animated);
                    } else {
                        this.mNotificationBarImage.setImageResource(drawable.rc_notification_network_available);
                    }
                }
            }
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public void setAdapter(RongConversationListAdapter adapter) {
        this.mAdapter = adapter;
        if(this.mList != null) {
            this.mList.setAdapter(adapter);
        }
    }

    public RongConversationListAdapter onResolveAdapter(Context context) {
        this.mAdapter = new RongConversationListAdapter(context);
        return this.mAdapter;
    }

    public void onEventMainThread(SyncReadStatusEvent event) {
        System.out.println("-----------rongconversation------SyncReadStatusEvent");
        ConversationType conversationType = event.getConversationType();
        String targetId = event.getTargetId();
        RLog.d(this.TAG, "SyncReadStatusEvent " + conversationType + " " + targetId);
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        int position;
        if(this.getGatherState(conversationType)) {
            position = this.mAdapter.findGatheredItem(conversationType);
        } else {
            position = this.mAdapter.findPosition(conversationType, targetId);
        }

        if(position >= 0) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if(position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(ReadReceiptEvent event) {
        System.out.println("-----------rongconversation------ReadReceiptEvent");
        ConversationType conversationType = event.getMessage().getConversationType();
        String targetId = event.getMessage().getTargetId();
        int originalIndex = this.mAdapter.findPosition(conversationType, targetId);
        boolean gatherState = this.getGatherState(conversationType);
        if(!gatherState && originalIndex >= 0) {
            UIConversation conversation = (UIConversation)this.mAdapter.getItem(originalIndex);
            ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
            if(content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                conversation.setSentStatus(SentStatus.READ);
                this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }
    }

    public void onEventMainThread(AudioListenedEvent event) {
        System.out.println("-----------rongconversation------AudioListenedEvent");
        Message message = event.getMessage();
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        RLog.d(this.TAG, "Message: " + message.getObjectName() + " " + conversationType + " " + message.getSentStatus());
        if(this.isConfigured(conversationType)) {
            boolean gathered = this.getGatherState(conversationType);
            int position = gathered?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
            if(position >= 0) {
                UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
                if(message.getMessageId() == uiConversation.getLatestMessageId()) {
                    uiConversation.updateConversation(message, gathered);
                    this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }
    }

    private boolean isFilterNoticeInfo(String messageId) {
        if (isNoticeList) {
            return !RongCouldUtil.getInstance().customConversation(messageId);
        } else {
            return RongCouldUtil.getInstance().customConversation(messageId);
        }
    }

    private void refreshUnreadMessage(Message message, int position) {
        if (!isNoticeList && RongCouldUtil.getInstance().customConversation(message.getSenderUserId())) {
            System.out.println("-------conversation-----unReadMessage");
            UIConversation conversation = null;
            int originalIndex = this.mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
            if (originalIndex < 0) {
                addNoticeItem();
                originalIndex = mAdapter.findPosition(Conversation.ConversationType.PRIVATE, noticeId);
                conversation = this.mAdapter.getItem(originalIndex);
            } else {
                conversation = this.mAdapter.getItem(originalIndex);
                if (conversation == null) {
                    MessageContent mc = new MessageContent() {
                        @Override
                        public byte[] encode() {
                            return "公告".getBytes();
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                        }
                    };
                    Message targetMessage = Message.obtain(noticeId, Conversation.ConversationType.PRIVATE, mc);
                    conversation = UIConversation.obtain(targetMessage, getGatherState(Conversation.ConversationType.PRIVATE));
                }
            }

            if (conversation != null) {
                conversation.setUnReadMessageCount(conversation.getUnReadMessageCount() + 1);
                int newPosition = this.mAdapter.findPosition(conversation);

                long showTime = conversation.getSentStatus() == Message.SentStatus.SENT ? message.getSentTime() : message.getReceivedTime();
                if (showTime != 0) {
                    conversation.setUIConversationTime(showTime);
                }

                int unread = 0;
                for (UIConversation uiConversation : cacheConversationList) {
                    unread += uiConversation.getUnReadMessageCount();
                }
                if (unread > 0) {
                    conversation.setUnReadMessageCount(unread);
                }

                try {
                    Field field = message.getContent().getClass().getDeclaredField("content");/////这个对应的是属性
                    field.setAccessible(true);
                    Object value = field.get(message.getContent());
                    if (value != null && value instanceof String) {
                        String content = String.valueOf(value);
                        System.out.println("------content=" + content);
                        conversation.setConversationContent(Spannable.Factory.getInstance().newSpannable(content));
                    }
                } catch (Exception e) {
                }

                if (originalIndex < 0) {
                    this.mAdapter.add(conversation, newPosition);
                } else {
                    this.mAdapter.remove(originalIndex);
                    this.mAdapter.add(conversation, newPosition);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public boolean shouldUpdateConversation(Message message, int left) {
        return true;
    }

    public void onEventMainThread(OnReceiveMessageEvent event) {
        System.out.println("-----------rongconversation------OnReceiveMessageEvent");
        Message message = event.getMessage();
        String targetId = message.getTargetId();
        ConversationType conversationType = message.getConversationType();
        int first = this.mList.getFirstVisiblePosition();
        int last = this.mList.getLastVisiblePosition();
        if(this.isConfigured(message.getConversationType()) && this.shouldUpdateConversation(event.getMessage(), event.getLeft())) {
            if(message.getMessageId() > 0) {
                boolean gathered = this.getGatherState(conversationType);
                int position;
                if(gathered) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                }

                if (isNoticeList && RongCouldUtil.getInstance().hideConversation(event.getMessage().getSenderUserId())) {
                    return;
                }

                if (isFilterNoticeInfo(event.getMessage().getSenderUserId())) { // chenlong后加的刷新未读消息
                    refreshUnreadMessage(event.getMessage(), position);
                    return;
                }

                UIConversation uiConversation;
                int index;
                if(position < 0) {
                    uiConversation = UIConversation.obtain(message, gathered);
                    index = this.getPosition(uiConversation);
                    this.mAdapter.add(uiConversation, index);
                    this.mAdapter.notifyDataSetChanged();
                } else {
                    uiConversation = (UIConversation)this.mAdapter.getItem(position);
                    if(event.getMessage().getSentTime() > uiConversation.getUIConversationTime()) {
                        uiConversation.updateConversation(message, gathered);
                        this.mAdapter.remove(position);
                        index = this.getPosition(uiConversation);
                        if(index == position) {
                            this.mAdapter.add(uiConversation, index);
                            if(index >= first && index <= last) {
                                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                            }
                        } else {
                            this.mAdapter.add(uiConversation, index);
                            if(index >= first && index <= last) {
                                this.mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        RLog.i(this.TAG, "ignore update message " + event.getMessage().getObjectName());
                    }
                }

                RLog.i(this.TAG, "conversation unread count : " + uiConversation.getUnReadMessageCount() + " " + conversationType + " " + targetId);
            }

            if(event.getLeft() == 0) {
                this.syncUnreadCount();
            }

            RLog.d(this.TAG, "OnReceiveMessageEvent: " + message.getObjectName() + " " + event.getLeft() + " " + conversationType + " " + targetId);
        }

    }

    public void onEventMainThread(MessageLeftEvent event) {
        System.out.println("-----------rongconversation------MessageLeftEvent");
        if(event.left == 0) {
            this.syncUnreadCount();
        }

    }

    private void syncUnreadCount() {
        if(this.mAdapter.getCount() > 0) {
            final int first = this.mList.getFirstVisiblePosition();
            final int last = this.mList.getLastVisiblePosition();

            for(int i = 0; i < this.mAdapter.getCount(); ++i) {
                final UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                ConversationType conversationType = uiConversation.getConversationType();
                String targetId = uiConversation.getConversationTargetId();
                final int position;
                if(this.getGatherState(conversationType)) {
                    position = this.mAdapter.findGatheredItem(conversationType);
                    RongIMClient.getInstance().getUnreadCount(new ResultCallback<Integer>() {
                        public void onSuccess(Integer integer) {
                            uiConversation.setUnReadMessageCount(integer.intValue());
                            if(position >= first && position <= last) {
                                RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    }, new ConversationType[]{conversationType});
                } else {
                    position = this.mAdapter.findPosition(conversationType, targetId);
                    RongIMClient.getInstance().getUnreadCount(conversationType, targetId, new ResultCallback<Integer>() {
                        public void onSuccess(Integer integer) {
                            uiConversation.setUnReadMessageCount(integer.intValue());
                            if(position >= first && position <= last) {
                                RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    });
                }
            }
        }
    }

    public void onEventMainThread(MessageRecallEvent event) {
        System.out.println("-----------rongconversation------MessageRecallEvent");
        RLog.d(this.TAG, "MessageRecallEvent");
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
            if(event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = ((UIConversation)this.mAdapter.getItem(i)).getConversationGatherState();
                final String targetId = ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() > 0) {
                                UIConversation uiConversation = RongConversationListFragment.this.makeUIConversation(conversationList);
                                int oldPos = RongConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), targetId);
                                if(oldPos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = RongConversationListFragment.this.getPosition(uiConversation);
                                RongConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    }, new ConversationType[]{uiConversation.getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation != null) {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = RongConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = RongConversationListFragment.this.getPosition(temp);
                                RongConversationListFragment.this.mAdapter.add(temp, newPosition);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }
    }

    public void onEventMainThread(RemoteMessageRecallEvent event) {
        System.out.println("-----------rongconversation------RemoteMessageRecallEvent");
        RLog.d(this.TAG, "RemoteMessageRecallEvent");
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
            if(event.getMessageId() == uiConversation.getLatestMessageId()) {
                boolean gatherState = uiConversation.getConversationGatherState();
                final String targetId = ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() > 0) {
                                UIConversation uiConversation = RongConversationListFragment.this.makeUIConversation(conversationList);
                                int oldPos = RongConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), targetId);
                                if(oldPos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = RongConversationListFragment.this.getPosition(uiConversation);
                                RongConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(ErrorCode e) {
                        }
                    }, new ConversationType[]{((UIConversation)this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), new ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation != null) {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = RongConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = RongConversationListFragment.this.getPosition(temp);
                                RongConversationListFragment.this.mAdapter.add(temp, newPosition);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }
                        public void onError(ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }
    }

    public void onEventMainThread(Message message) {
        System.out.println("-----------rongconversation------Message");
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        RLog.d(this.TAG, "Message: " + message.getObjectName() + " " + message.getMessageId() + " " + conversationType + " " + message.getSentStatus());
        boolean gathered = this.getGatherState(conversationType);
        if(this.isConfigured(conversationType) && message.getMessageId() > 0) {
            int position = gathered?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
            UIConversation uiConversation;
            int index;

            if (isFilterNoticeInfo(message.getSenderUserId())) {
                refreshUnreadMessage(message, position);
                return;
            }

            if(position < 0) {
                uiConversation = UIConversation.obtain(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                this.mAdapter.notifyDataSetChanged();
            } else {
                uiConversation = (UIConversation)this.mAdapter.getItem(position);
                this.mAdapter.remove(position);
                uiConversation.updateConversation(message, gathered);
                index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if(position == index) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onEventMainThread(ConnectionStatus status) {
        System.out.println("-----------rongconversation------ConnectionStatus");
        RLog.d(this.TAG, "ConnectionStatus, " + status.toString());
        this.setNotificationBarVisibility(status);
    }

    public void onEventMainThread(ConnectEvent event) {
        System.out.println("-----------rongconversation------ConnectEvent");
        if(this.isShowWithoutConnected) {
            this.getConversationList(this.getConfigConversationTypes());
            this.isShowWithoutConnected = false;
        }
    }

    public void onEventMainThread(final CreateDiscussionEvent createDiscussionEvent) {
        System.out.println("-----------rongconversation------CreateDiscussionEvent");
        RLog.d(this.TAG, "createDiscussionEvent");
        final String targetId = createDiscussionEvent.getDiscussionId();
        if(this.isConfigured(ConversationType.DISCUSSION)) {
            RongIMClient.getInstance().getConversation(ConversationType.DISCUSSION, targetId, new ResultCallback<Conversation>() {
                public void onSuccess(Conversation conversation) {
                    if(conversation != null) {
                        int position;
                        if(RongConversationListFragment.this.getGatherState(ConversationType.DISCUSSION)) {
                            position = RongConversationListFragment.this.mAdapter.findGatheredItem(ConversationType.DISCUSSION);
                        } else {
                            position = RongConversationListFragment.this.mAdapter.findPosition(ConversationType.DISCUSSION, targetId);
                        }

                        if (isNoticeList) {
                            return;
                        }

                        conversation.setConversationTitle(createDiscussionEvent.getDiscussionName());
                        UIConversation uiConversation;
                        if(position < 0) {
                            uiConversation = UIConversation.obtain(conversation, RongConversationListFragment.this.getGatherState(ConversationType.DISCUSSION));
                            int index = RongConversationListFragment.this.getPosition(uiConversation);
                            RongConversationListFragment.this.mAdapter.add(uiConversation, index);
                            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                        } else {
                            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
                            uiConversation.updateConversation(conversation, RongConversationListFragment.this.getGatherState(ConversationType.DISCUSSION));
                            RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
                        }
                    }

                }

                public void onError(ErrorCode e) {
                }
            });
        }

    }

    public void onEventMainThread(final DraftEvent draft) {
        System.out.println("-----------rongconversation------DraftEvent");
        ConversationType conversationType = draft.getConversationType();
        String targetId = draft.getTargetId();
        RLog.i(this.TAG, "Draft : " + conversationType);
        if(this.isConfigured(conversationType)) {
            final boolean gathered = this.getGatherState(conversationType);
            final int position = gathered?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
            RongIMClient.getInstance().getConversation(conversationType, targetId, new ResultCallback<Conversation>() {
                public void onSuccess(Conversation conversation) {
                    if(conversation != null) {
                        UIConversation uiConversation;
                        if(position < 0) {
                            if(!TextUtils.isEmpty(draft.getContent())) {
                                uiConversation = UIConversation.obtain(conversation, gathered);
                                int index = RongConversationListFragment.this.getPosition(uiConversation);
                                RongConversationListFragment.this.mAdapter.add(uiConversation, index);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            uiConversation = (UIConversation)RongConversationListFragment.this.mAdapter.getItem(position);
                            if(TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && TextUtils.isEmpty(uiConversation.getDraft()) || !TextUtils.isEmpty(draft.getContent()) && !TextUtils.isEmpty(uiConversation.getDraft()) && !draft.getContent().equals(uiConversation.getDraft())) {
                                uiConversation.updateConversation(conversation, gathered);
                                RongConversationListFragment.this.mAdapter.getView(position, RongConversationListFragment.this.mList.getChildAt(position - RongConversationListFragment.this.mList.getFirstVisiblePosition()), RongConversationListFragment.this.mList);
                            }
                        }
                    }

                }

                public void onError(ErrorCode e) {
                }
            });
        }

    }

    public void onEventMainThread(Group groupInfo) {
        System.out.println("-----------rongconversation------Group");
        RLog.d(this.TAG, "Group: " + groupInfo.getName() + " " + groupInfo.getId());
        int count = this.mAdapter.getCount();
        if(groupInfo.getName() != null) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for(int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                uiConversation.updateConversation(groupInfo);
                if(i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }
        }
    }

    public void onEventMainThread(Discussion discussion) {
        System.out.println("-----------rongconversation------Discussion");
        RLog.d(this.TAG, "Discussion: " + discussion.getName() + " " + discussion.getId());
        if(this.isConfigured(ConversationType.DISCUSSION)) {
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();
            int position;
            if(this.getGatherState(ConversationType.DISCUSSION)) {
                position = this.mAdapter.findGatheredItem(ConversationType.DISCUSSION);
            } else {
                position = this.mAdapter.findPosition(ConversationType.DISCUSSION, discussion.getId());
            }

            if(position >= 0) {
                for(int i = 0; i == position; ++i) {
                    UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                    uiConversation.updateConversation(discussion);
                    if(i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }
        }
    }

    public void onEventMainThread(GroupUserInfo groupUserInfo) {
        System.out.println("-----------rongconversation------GroupUserInfo");
        RLog.d(this.TAG, "GroupUserInfo " + groupUserInfo.getGroupId() + " " + groupUserInfo.getUserId() + " " + groupUserInfo.getNickname());
        if(groupUserInfo.getNickname() != null && groupUserInfo.getGroupId() != null) {
            int count = this.mAdapter.getCount();
            int last = this.mList.getLastVisiblePosition();
            int first = this.mList.getFirstVisiblePosition();

            for(int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                if(!this.getGatherState(ConversationType.GROUP) && uiConversation.getConversationTargetId().equals(groupUserInfo.getGroupId()) && uiConversation.getConversationSenderId().equals(groupUserInfo.getUserId())) {
                    uiConversation.updateConversation(groupUserInfo);
                    if(i >= first && i <= last) {
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }
        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        System.out.println("-----------rongconversation------UserInfo");
        RLog.i(this.TAG, "UserInfo " + userInfo.getUserId() + " " + userInfo.getName());
        int count = this.mAdapter.getCount();
        int last = this.mList.getLastVisiblePosition();
        int first = this.mList.getFirstVisiblePosition();

        for(int i = 0; i < count && userInfo.getName() != null; ++i) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
            if(uiConversation.hasNickname(userInfo.getUserId())) {
                RLog.i(this.TAG, "has nick name");
            } else {
                uiConversation.updateConversation(userInfo);
                if(i >= first && i <= last) {
                    this.mAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }
        }
    }

    public void onEventMainThread(PublicServiceProfile profile) {
        System.out.println("-----------rongconversation------PublicServiceProfile");
        RLog.d(this.TAG, "PublicServiceProfile");
        int count = this.mAdapter.getCount();
        boolean gatherState = this.getGatherState(profile.getConversationType());

        for(int i = 0; i < count; ++i) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
            if(uiConversation.getConversationType().equals(profile.getConversationType()) && uiConversation.getConversationTargetId().equals(profile.getTargetId()) && !gatherState) {
                uiConversation.setUIConversationTitle(profile.getName());
                uiConversation.setIconUrl(profile.getPortraitUri());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(PublicServiceFollowableEvent event) {
        System.out.println("-----------rongconversation------PublicServiceFollowableEvent");
        RLog.d(this.TAG, "PublicServiceFollowableEvent");
        if(!event.isFollow()) {
            if (isNoticeList) {
                return;
            }
            int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
            if(originalIndex >= 0) {
                this.mAdapter.remove(originalIndex);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onEventMainThread(ConversationUnreadEvent unreadEvent) {
        System.out.println("-----------rongconversation------ConversationUnreadEvent");
        RLog.d(this.TAG, "ConversationUnreadEvent");
        ConversationType conversationType = unreadEvent.getType();
        String targetId = unreadEvent.getTargetId();
        int position = this.getGatherState(conversationType)?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
        if(position >= 0) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
            uiConversation.clearUnRead(conversationType, targetId);
            if(position >= first && position <= last) {
                this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }
    }

    public void onEventMainThread(ConversationTopEvent setTopEvent) {
        System.out.println("-----------rongconversation------ConversationTopEvent");
        RLog.d(this.TAG, "ConversationTopEvent");
        ConversationType conversationType = setTopEvent.getConversationType();
        String targetId = setTopEvent.getTargetId();
        int position = this.mAdapter.findPosition(conversationType, targetId);
        if(position >= 0 && !this.getGatherState(conversationType)) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
            if(uiConversation.isTop() != setTopEvent.isTop()) {
                uiConversation.setTop(!uiConversation.isTop());
                this.mAdapter.remove(position);
                int index = this.getPosition(uiConversation);
                this.mAdapter.add(uiConversation, index);
                if(index == position) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onEventMainThread(ConversationRemoveEvent removeEvent) {
        System.out.println("-----------rongconversation------ConversationRemoveEvent");
        RLog.d(this.TAG, "ConversationRemoveEvent");
        ConversationType conversationType = removeEvent.getType();
        this.removeConversation(conversationType, removeEvent.getTargetId());
    }

    public void onEventMainThread(ClearConversationEvent clearConversationEvent) {
        System.out.println("-----------rongconversation------ClearConversationEvent");
        RLog.d(this.TAG, "ClearConversationEvent");
        List typeList = clearConversationEvent.getTypes();

        for(int i = this.mAdapter.getCount() - 1; i >= 0; --i) {
            if(typeList.indexOf(((UIConversation)this.mAdapter.getItem(i)).getConversationType()) >= 0) {
                this.mAdapter.remove(i);
            }
        }

        this.mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(MessageDeleteEvent event) {
        System.out.println("-----------rongconversation------MessageDeleteEvent");
        RLog.d(this.TAG, "MessageDeleteEvent");
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(event.getMessageIds().contains(Integer.valueOf(((UIConversation)this.mAdapter.getItem(i)).getLatestMessageId()))) {
                boolean gatherState = ((UIConversation)this.mAdapter.getItem(i)).getConversationGatherState();
                final String targetId = ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() != 0) {
                                UIConversation uiConversation = RongConversationListFragment.this.makeUIConversation(conversationList);
                                int oldPos = RongConversationListFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), targetId);
                                if(oldPos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = RongConversationListFragment.this.getPosition(uiConversation);
                                RongConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(ErrorCode e) {
                        }
                    }, new ConversationType[]{((UIConversation)this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(((UIConversation)this.mAdapter.getItem(i)).getConversationType(), ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId(), new ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation == null) {
                                RLog.d(RongConversationListFragment.this.TAG, "onEventMainThread getConversation : onSuccess, conversation = null");
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = RongConversationListFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    RongConversationListFragment.this.mAdapter.remove(pos);
                                }

                                int newIndex = RongConversationListFragment.this.getPosition(temp);
                                RongConversationListFragment.this.mAdapter.add(temp, newIndex);
                                RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(ConversationNotificationEvent notificationEvent) {
        System.out.println("-----------rongconversation------ConversationNotificationEvent");
        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if(originalIndex >= 0) {
            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(MessagesClearEvent clearMessagesEvent) {
        System.out.println("-----------rongconversation------MessagesClearEvent");
        RLog.d(this.TAG, "MessagesClearEvent");
        ConversationType conversationType = clearMessagesEvent.getType();
        String targetId = clearMessagesEvent.getTargetId();
        int position = this.getGatherState(conversationType)?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
        if(position >= 0) {
            UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
            uiConversation.clearLastMessage();
            this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(OnMessageSendErrorEvent sendErrorEvent) {
        System.out.println("-----------rongconversation------OnMessageSendErrorEvent");
        Message message = sendErrorEvent.getMessage();
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if(this.isConfigured(conversationType)) {
            int first = this.mList.getFirstVisiblePosition();
            int last = this.mList.getLastVisiblePosition();
            boolean gathered = this.getGatherState(conversationType);
            int index = gathered?this.mAdapter.findGatheredItem(conversationType):this.mAdapter.findPosition(conversationType, targetId);
            if(index >= 0) {
                UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(index);
                message.setSentStatus(SentStatus.FAILED);
                uiConversation.updateConversation(message, gathered);
                if(index >= first && index <= last) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }
        }

    }

    public void onEventMainThread(QuitDiscussionEvent event) {
        System.out.println("-----------rongconversation------QuitDiscussionEvent");
        RLog.d(this.TAG, "QuitDiscussionEvent");
        this.removeConversation(ConversationType.DISCUSSION, event.getDiscussionId());
    }

    public void onEventMainThread(QuitGroupEvent event) {
        System.out.println("-----------rongconversation------QuitGroupEvent");
        RLog.d(this.TAG, "QuitGroupEvent");
        this.removeConversation(ConversationType.GROUP, event.getGroupId());
    }

    private void removeConversation(final ConversationType conversationType, String targetId) {
        boolean gathered = this.getGatherState(conversationType);
        int index;
        if(gathered) {
            index = this.mAdapter.findGatheredItem(conversationType);
            if(index >= 0) {
                RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversationList) {
                        int oldPos = RongConversationListFragment.this.mAdapter.findGatheredItem(conversationType);
                        if(oldPos >= 0) {
                            RongConversationListFragment.this.mAdapter.remove(oldPos);
                            if(conversationList != null && conversationList.size() > 0) {
                                UIConversation uiConversation = RongConversationListFragment.this.makeUIConversation(conversationList);
                                int newIndex = RongConversationListFragment.this.getPosition(uiConversation);
                                RongConversationListFragment.this.mAdapter.add(uiConversation, newIndex);
                            }

                            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
                        }

                    }

                    public void onError(ErrorCode e) {
                    }
                }, new ConversationType[]{conversationType});
            }
        } else {
            index = this.mAdapter.findPosition(conversationType, targetId);
            if(index >= 0) {
                this.mAdapter.remove(index);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onPortraitItemClick(View v, UIConversation data) {
        ConversationType type = data.getConversationType();
        if(this.getGatherState(type)) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(this.getActivity(), type, data.getConversationTargetId());
                if(isDefault) {
                    return;
                }
            }

            data.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), type, data.getConversationTargetId(), data.getUIConversationTitle());
        }

    }

    public boolean onPortraitItemLongClick(View v, UIConversation data) {
        ConversationType type = data.getConversationType();
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(this.getActivity(), type, data.getConversationTargetId());
            if(isDealt) {
                return true;
            }
        }

        if(!this.getGatherState(type)) {
            this.buildMultiDialog(data);
            return true;
        } else {
            this.buildSingleDialog(data);
            return true;
        }
    }

//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
//        ConversationType conversationType = uiConversation.getConversationType();
//        if(this.getGatherState(conversationType)) {
//            RongIM.getInstance().startSubConversationList(this.getActivity(), conversationType);
//        } else {
//            if(RongContext.getInstance().getConversationListBehaviorListener() != null && RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiConversation)) {
//                return;
//            }
//
//            uiConversation.setUnReadMessageCount(0);
//            RongIM.getInstance().startConversation(this.getActivity(), conversationType, uiConversation.getConversationTargetId(), uiConversation.getUIConversationTitle());
//        }
//    }
//
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
//        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
//            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
//            if(isDealt) {
//                return true;
//            }
//        }
//
//        if(!this.getGatherState(uiConversation.getConversationType())) {
//            this.buildMultiDialog(uiConversation);
//            return true;
//        } else {
//            this.buildSingleDialog(uiConversation);
//            return true;
//        }
//    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiconversation = (UIConversation) parent.getAdapter().getItem(position);
        Conversation.ConversationType type = uiconversation.getConversationType();
        if (!uiconversation.getConversationTargetId().equals(noticeId)) {
            uiconversation.setUnReadMessageCount(0);
            this.mAdapter.notifyDataSetChanged();
        }

        String targetId = uiconversation.getConversationTargetId();
        SPreference.putBoolean(getContext(), "isTop", uiconversation.isTop());
        RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, targetId);
        String[] param = null;
        HashMap<String, String> params = null;
        switch (targetId) {
            case "dd0cc61140504258ab474b8f0a38bb56":
                break;
//            case "INTIME40001":
            case "INTIME40002":
                break;
            case "INTIME40003":
                break;
            case "INTIME40004":
                break;
            case "INTIME40005":
                break;
            case "INTIME40006":
                break;
//            case "INTIME40007":
//                param = new String[]{"客服", MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName()};
//                params = DataStatisticsUtils.getParams("1007", "10053", param);
//                DataStatisticsUtils.push(getActivity(), params);
//                break;
            default:
                break;
        }

        if (targetId.contains("INTIME") && !targetId.contains("INTIME40006")) {
            if (!uiconversation.getConversationTargetId().equals(noticeId)) {
                uiconversation.setUnReadMessageCount(0);
                this.mAdapter.notifyDataSetChanged();
            }
//            if (targetId.equals("INTIME40001")) { //  直CHENLONG播动态消息直接到二级列表页面
//                Intent intent = new Intent(getActivity(), AVLiveListActivity.class);
//                startActivity(intent);
//            } else {
            HashMap hashMap = new HashMap();
            hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.msgDetal + targetId);
            hashMap.put(WebViewConstant.push_message_title, uiconversation.getUIConversationTitle());
            hashMap.put(WebViewConstant.PAGE_SHARE_WITH_EMAIL, true);
            hashMap.put(WebViewConstant.RIGHT_SAVE, false);
            hashMap.put(WebViewConstant.PAGE_INIT, false);
            NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
            return;
        }

        if (this.getGatherState(type) && type != Conversation.ConversationType.GROUP) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiconversation);
                if (isDefault) {
                    return;
                }
            }
            if (!uiconversation.getConversationTargetId().equals(noticeId)) {
                uiconversation.setUnReadMessageCount(0);
                this.mAdapter.notifyDataSetChanged();
            }
            RongIM.getInstance().startConversation(this.getActivity(), type, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());
        }
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation) this.mAdapter.getItem(position);
        String type = uiConversation.getConversationType().getName();
        if (RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
            if (isDealt) {
                return true;
            }
        }

        if(!this.getGatherState(uiConversation.getConversationType())) {
            this.buildMultiDialog(uiConversation);
            return true;
        } else {
            this.buildSingleDialog(uiConversation);
            return true;
        }
    }

    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if(uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_set_top);
        }

        items[1] = RongContext.getInstance().getString(string.rc_conversation_list_dialog_remove);
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(which -> {
            if(which == 0) {
                RongIM.getInstance().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new ResultCallback<Boolean>() {
                    public void onSuccess(Boolean aBoolean) {
                        if(uiConversation.isTop()) {
                            Toast.makeText(RongContext.getInstance(), RongConversationListFragment.this.getString(string.rc_conversation_list_popup_cancel_top), 0).show();
                        } else {
                            Toast.makeText(RongContext.getInstance(), RongConversationListFragment.this.getString(string.rc_conversation_list_dialog_set_top), 0).show();
                        }

                    }

                    public void onError(ErrorCode e) {
                    }
                });
            } else if(which == 1) {
                RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), (ResultCallback)null);
            }

        }).show();
    }

    private void buildSingleDialog(final UIConversation uiConversation) {
        String[] items = new String[]{RongContext.getInstance().getString(string.rc_conversation_list_dialog_remove)};
        OptionsPopupDialog.newInstance(this.getActivity(), items).setOptionsPopupDialogListener(which -> {
            RongIM.getInstance().getConversationList(new ResultCallback<List<Conversation>>() {
                public void onSuccess(List<Conversation> conversations) {
                    if(conversations != null && conversations.size() > 0) {
                        Iterator i$ = conversations.iterator();

                        while(i$.hasNext()) {
                            Conversation conversation = (Conversation)i$.next();
                            RongIMClient.getInstance().removeConversation(conversation.getConversationType(), conversation.getTargetId(), (ResultCallback)null);
                        }
                    }

                }

                public void onError(ErrorCode errorCode) {
                }
            }, new ConversationType[]{uiConversation.getConversationType()});
            int position = RongConversationListFragment.this.mAdapter.findGatheredItem(uiConversation.getConversationType());
            RongConversationListFragment.this.mAdapter.remove(position);
            RongConversationListFragment.this.mAdapter.notifyDataSetChanged();
        }).show();
    }

    private void makeUiConversationList(List<Conversation> conversationList) {
        Iterator i$ = conversationList.iterator();

        while(i$.hasNext()) {
            Conversation conversation = (Conversation)i$.next();
            ConversationType conversationType = conversation.getConversationType();
            String targetId = conversation.getTargetId();
            boolean gatherState = this.getGatherState(conversationType);
            UIConversation uiConversation;
            int originalIndex;
            if (isFilterNoticeInfo(conversation.getSenderUserId())) {
                continue;
            }
            if(gatherState) {
                originalIndex = this.mAdapter.findGatheredItem(conversationType);
                if(originalIndex >= 0) {
                    uiConversation = (UIConversation)this.mAdapter.getItem(originalIndex);
                    uiConversation.updateConversation(conversation, true);
                } else {
                    uiConversation = UIConversation.obtain(conversation, true);
                    this.mAdapter.add(uiConversation);
                }
            } else {
                originalIndex = this.mAdapter.findPosition(conversationType, targetId);
                if(originalIndex < 0) {
                    uiConversation = UIConversation.obtain(conversation, false);
                    this.mAdapter.add(uiConversation);
                } else {
                    uiConversation = (UIConversation)this.mAdapter.getItem(originalIndex);
                    uiConversation.setUnReadMessageCount(conversation.getUnreadMessageCount());
                }
            }
        }
    }

    private UIConversation makeUIConversation(List<Conversation> conversations) {
        int unreadCount = 0;
        boolean topFlag = false;
        boolean isMentioned = false;
        Conversation newest = (Conversation)conversations.get(0);

        Conversation conversation;
        for(Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
            conversation = (Conversation)uiConversation.next();
            if(newest.isTop()) {
                if(conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
                    newest = conversation;
                }
            } else if(conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
                newest = conversation;
            }

            if(conversation.isTop()) {
                topFlag = true;
            }

            if(conversation.getMentionedCount() > 0) {
                isMentioned = true;
            }
        }

        UIConversation uiConversation1 = UIConversation.obtain(newest, this.getGatherState(newest.getConversationType()));
        uiConversation1.setUnReadMessageCount(unreadCount);
        uiConversation1.setTop(false);
        uiConversation1.setMentionedFlag(isMentioned);
        return uiConversation1;
    }

    private int getPosition(UIConversation uiConversation) {
        int count = this.mAdapter.getCount();
        int position = 0;

        for(int i = 0; i < count; ++i) {
            if(uiConversation.isTop()) {
                if(!((UIConversation)this.mAdapter.getItem(i)).isTop() || ((UIConversation)this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            } else {
                if(!((UIConversation)this.mAdapter.getItem(i)).isTop() && ((UIConversation)this.mAdapter.getItem(i)).getUIConversationTime() <= uiConversation.getUIConversationTime()) {
                    break;
                }

                ++position;
            }
        }

        return position;
    }

    private boolean isConfigured(ConversationType conversationType) {
        for(int i = 0; i < this.mConversationsConfig.size(); ++i) {
            if(conversationType.equals(((RongConversationListFragment.ConversationConfig)this.mConversationsConfig.get(i)).conversationType)) {
                return true;
            }
        }

        return false;
    }

    public boolean getGatherState(ConversationType conversationType) {
        Iterator i$ = this.mConversationsConfig.iterator();

        RongConversationListFragment.ConversationConfig config;
        do {
            if(!i$.hasNext()) {
                return false;
            }

            config = (RongConversationListFragment.ConversationConfig)i$.next();
        } while(!config.conversationType.equals(conversationType));

        return config.isGathered;
    }

    private ConversationType[] getConfigConversationTypes() {
        ConversationType[] conversationTypes = new ConversationType[this.mConversationsConfig.size()];

        for(int i = 0; i < this.mConversationsConfig.size(); ++i) {
            conversationTypes[i] = ((RongConversationListFragment.ConversationConfig)this.mConversationsConfig.get(i)).conversationType;
        }

        return conversationTypes;
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this.mThis);
        super.onDestroyView();
    }

    private class ConversationConfig {
        ConversationType conversationType;
        boolean isGathered;

        private ConversationConfig() {
        }
    }
}
