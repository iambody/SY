package app.privatefund.com.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.adapter.SearchMessageListAdapter;
import app.privatefund.com.im.bean.ChatHistoryMessage;
import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;


/**
 * @author chenlong
 *
 * 消息搜索
 */
public class MessageSearchActivity extends Activity {

    private static final String NOTICE_SEARCH = "notice_search";
    private ListView listView;
    private SearchMessageListAdapter listAdapter;
    private ClearEditText textEdit;
    protected ImageButton imageButton_kong;
    private List<ChatHistoryMessage> allDataList = new ArrayList<>();
    private boolean isNoticeSearch;
    private RelativeLayout noticeSearchLinear;
    private String keyName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivity_message_search);
        isNoticeSearch = getIntent().getBooleanExtra(NOTICE_SEARCH, false);
        initView();
        initListView();
        textEdit.setTextChangedListener(new ClearEditText.TextChangedListener() {
            @Override
            public void onTextChanged(String value) {
                if (TextUtils.isEmpty(value)) {
                    listAdapter.setData(null);
                    return;
                }
                keyName = value;
                searchMessage(value);
            }
        });
        textEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(v.getText().toString())) {
                        listAdapter.setData(null);
                        return false;
                    }
                    keyName = v.getText().toString();
                    searchMessage(v.getText().toString());
                }
                return false;
            }
        });
        findViewById(R.id.search_cancel).setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.message_search_out_bottom);
        });
        initData();
    }

    private void initView() {
//        imageButton_kong = (ImageButton) findViewById(R.id.image_button_kong);
        listView = (ListView) findViewById(R.id.message_list);
        textEdit = (ClearEditText) findViewById(R.id.search_title_ed);
        noticeSearchLinear = (RelativeLayout) findViewById(R.id.notice_search);
        noticeSearchLinear.setVisibility(isNoticeSearch ? View.GONE : View.VISIBLE);
        ((ImageView)findViewById(R.id.notice_image)).setImageResource(R.drawable.notice_search);
    }

    private void initListView() {
        listAdapter = new SearchMessageListAdapter(this);
        listView.setAdapter(listAdapter);
        TextView textView = (TextView)noticeSearchLinear.findViewById(R.id.name);
        ViewUtils.setTextColor(textView, getString(R.string.search_notice_name), Color.parseColor("#f47900"));
        noticeSearchLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MessageSearchActivity.this, SearchResultListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SEARCH_TYPE_PARAMS", "4");
                bundle.putBoolean("notice_search", true);
                bundle.putString("notice_search_title", textEdit.getText().toString());
                NavigationUtils.startActivityByRouter(MessageSearchActivity.this, RouteConfig.SEARCH_RESULT_ACTIVITY, bundle);
//                MessageSearchActivity.this.startActivity(intent);
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ChatHistoryMessage message = (ChatHistoryMessage) parent.getAdapter().getItem(position);
            if (message.isPolymerize()) {
                Intent intent = new Intent(MessageSearchActivity.this, MessageSearchListActivity.class);
                intent.putExtra(MessageSearchListActivity.SEARCH_MESSAGE_RESULT, message);
                intent.putExtra(MessageSearchListActivity.SEARCH_MESSAGE_KEY_NAME, keyName);
                startActivity(intent);
            } else {
                Conversation.ConversationType type = message.getType();
//                    String userId = Utils.transferUserId(message.getTagerId(), true);
                RongIM.getInstance().startConversation(MessageSearchActivity.this, type, message.getTagerId(), message.getStrName());
            }
        });
    }

    public void initData() {
        List<Conversation> conversationList = RongIMClient.getInstance().getConversationList();
        if (!CollectionUtils.isEmpty(conversationList)) {
            for (Conversation conversation : conversationList) {
                if (getString(R.string.search_notice_name).equals(conversation.getTargetId()) || RongCouldUtil.getInstance().hideConversation(conversation.getSenderUserId())) {
                    continue;
                }

                List<Message> listmessage = RongIMClient.getInstance().getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), -1, Integer.MAX_VALUE);
                if (!CollectionUtils.isEmpty(listmessage)) {
                    for (Message message : listmessage) {
                        if (message.getContent() instanceof TextMessage) {
                            allDataList.add(ChatHistoryMessage.formatMessageToChatHistoryMessage(message, this));
                        }
                    }
                }
            }
        }
    }

    private void searchMessage(String text) {
//        List<ChatHistoryMessage> dataList = RongYunDatabase.getInstance(this).getMessageListByContent(text);
        if (!CollectionUtils.isEmpty(allDataList)) {
            List<ChatHistoryMessage> resultData = getMatchData(text);
           // header.setVisibility(CollectionUtils.isEmpty(resultData) ? View.GONE : View.VISIBLE);
            updateViewStatus(CollectionUtils.isEmpty(resultData));
            listAdapter.setKeyName(text);
            listAdapter.setData(resultData);
        }
    }

    private void updateViewStatus(boolean isEmpty) {
//        imageButton_kong.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        listView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private List<ChatHistoryMessage> getMatchData(String text) {
        List<ChatHistoryMessage> matchInfo = new ArrayList<>();
        for (ChatHistoryMessage chatHistoryMessage : allDataList) {
            if ((chatHistoryMessage.getContent().contains(text) ||
                    chatHistoryMessage.getStrName().contains(text))) {
                if (!matchInfo.contains(chatHistoryMessage)) {
                    matchInfo.add(chatHistoryMessage);
                    chatHistoryMessage.setCount(1);
                    chatHistoryMessage.setPolymerize(false);
                    chatHistoryMessage.setShowContent("");
                } else {
                    int indexOf = matchInfo.indexOf(chatHistoryMessage);
                    ChatHistoryMessage tempChatHistoryMessage = matchInfo.get(indexOf);
                    tempChatHistoryMessage.setCount(tempChatHistoryMessage.getCount() + 1);
                    tempChatHistoryMessage.setShowContent(String.format(getString(R.string.message_relative), String.valueOf(tempChatHistoryMessage.getCount())));
                    tempChatHistoryMessage.setPolymerize(true);
                }
            }
        }
        return matchInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (CollectionUtils.isEmpty(allDataList)) {
            for (ChatHistoryMessage message : allDataList) {
                message = null;
            }
            allDataList.clear();
            allDataList = null;
        }
    }
}
