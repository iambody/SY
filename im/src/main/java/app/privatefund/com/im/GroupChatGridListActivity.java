package app.privatefund.com.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.GroupMemberNewEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.ToggleButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.adapter.MemberGridViewAdapter;
import app.privatefund.com.im.bean.GroupMember;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 *
 * 群组成员列表缩略图
 */
public class GroupChatGridListActivity extends BaseActivity {

//    @BindView(R2.id.toolbar)
//    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    @BindView(R2.id.member_grid)
    protected GridView memberList;

    @BindView(R2.id.more_member)
    protected TextView moreMember;

    @BindView(R2.id.toggle_no_notification)
    protected ToggleButton setNotifyToggle;

    @BindView(R2.id.toggle_set_top)
    protected ToggleButton setChatTopTog;

    @BindView(R2.id.set_top_layout)
    protected LinearLayout setTopLayout;

    private MemberGridViewAdapter groupMemberListAdapter;
    private String groupId;
    private ArrayList<GroupMember.GroupMemberPerson> datas;

    @Override
    protected int layoutID() {
        return R.layout.activity_group_chat_grid_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        groupId = getIntent().getStringExtra(Contants.CHAT_GROUP_ID);
        titleMid.setText(getIntent().getStringExtra(Contants.CHAT_GROUP_NAME));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
        boolean isTop = SPreference.getBoolean(this, "isTop");
        if (isTop) {
            setChatTopTog.setToggleOn();
        } else {
            setChatTopTog.setToggleOff();
        }
        questGroupMemberList();
        memberList.setOnItemClickListener((adapterView, view, i, l) -> RongIM.getInstance().startConversation(GroupChatGridListActivity.this, Conversation.ConversationType.PRIVATE, datas.get(i).getUserId(), datas.get(i).getUserName()));
        RongIM.getInstance().getConversationNotificationStatus (Conversation.ConversationType.GROUP, getIntent().getStringExtra(Contants.CHAT_GROUP_ID),
            new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                    if (conversationNotificationStatus.equals(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB)) {
                        setNotifyToggle.setToggleOn();
                    } else {
                        setNotifyToggle.setToggleOff();
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            }
        );

        if (getIntent().getStringExtra(Contants.CHAT_GROUP_ID).equals(Contants.topConversationGroupId)) {
            setTopLayout.setVisibility(View.INVISIBLE);
        }

        setChatTopTog.setOnToggleChanged(on -> {
            if (on) {
                RongIM.getInstance().setConversationToTop(
                    Conversation.ConversationType.GROUP,
                    groupId,
                    true,
                    new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            Toast.makeText(GroupChatGridListActivity.this, "设置置顶成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(GroupChatGridListActivity.this, "设置置顶失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            } else {
                RongIM.getInstance().setConversationToTop(
                    Conversation.ConversationType.GROUP,
                    groupId,
                    false,
                    new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            Toast.makeText(GroupChatGridListActivity.this, "关闭置顶成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(GroupChatGridListActivity.this, "关闭置顶失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
        });
        setNotifyToggle.setOnToggleChanged(on -> {
            if (on) {
                RongIM.getInstance().setConversationNotificationStatus(
                    Conversation.ConversationType.GROUP,
                    getIntent().getStringExtra(Contants.CHAT_GROUP_ID),
                    Conversation.ConversationNotificationStatus.DO_NOT_DISTURB,
                    new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                        @Override
                        public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                            Toast.makeText(GroupChatGridListActivity.this, "设置免打扰成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(GroupChatGridListActivity.this, "设置免打扰失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            } else {
                RongIM.getInstance().setConversationNotificationStatus(
                    Conversation.ConversationType.GROUP,
                    getIntent().getStringExtra(Contants.CHAT_GROUP_ID),
                    Conversation.ConversationNotificationStatus.NOTIFY,
                    new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                        @Override
                        public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                            Toast.makeText(GroupChatGridListActivity.this, "关闭免打扰成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Toast.makeText(GroupChatGridListActivity.this, "关闭免打扰失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void questGroupMemberList() {
        ApiClient.getTestGetGroupMemberNew(groupId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    Log.i("groupnumberlist", "----" + s);
                    if (!TextUtils.isEmpty(s)) {
                        try {
                            JSONObject response = new JSONObject(s);
                            String string = response.get("result").toString();
                            if (!TextUtils.isEmpty(string)) {
                                Gson g = new Gson();
                                datas = g.fromJson(string, new TypeToken<List<GroupMember.GroupMemberPerson>>() {
                                }.getType());
                                if (datas.size() >= 15) {
                                    moreMember.setVisibility(View.VISIBLE);
                                }
                                groupMemberListAdapter = new MemberGridViewAdapter(GroupChatGridListActivity.this, datas);
                                memberList.setAdapter(groupMemberListAdapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                String err = error.getMessage();
                Log.e("err", err);
            }
        });
    }


    @OnClick(R2.id.more_member)
    void moreMemberClick() {
        Intent intent = new Intent(GroupChatGridListActivity.this, GroupChatMemberListActivity.class);
        intent.putExtra(GroupChatMemberListActivity.GROUP_ID, groupId);
        startActivity(intent);
    }

}
