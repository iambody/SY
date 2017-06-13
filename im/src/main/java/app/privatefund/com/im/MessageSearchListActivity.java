package app.privatefund.com.im;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.adapter.SearchMessageListAdapter;
import app.privatefund.com.im.bean.ChatHistoryMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * @author chenlong
 *         <p>
 *         消息搜索列表
 */
public class MessageSearchListActivity extends Activity {

    public static final String SEARCH_MESSAGE_RESULT = "search_message_result";
    public static final String SEARCH_MESSAGE_KEY_NAME = "search_message_keyname";
    private ListView listView;
    private SearchMessageListAdapter listAdapter;
    private List<ChatHistoryMessage> allDataList = new ArrayList<>();
    private ChatHistoryMessage chatHistoryMessage;
    private String keyName;
    private ImageView backView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivity_message_searchlist);
        initListView();
        initData();
        backView = (ImageView) findViewById(R.id.title_left);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.message_search_out_bottom);
            }
        });
        if (chatHistoryMessage != null) {
            ((TextView) findViewById(R.id.title_mid)).setText(chatHistoryMessage.getStrName());
        }
    }

    private void initData() {
        chatHistoryMessage = getIntent().getParcelableExtra(SEARCH_MESSAGE_RESULT);
        keyName = getIntent().getStringExtra(SEARCH_MESSAGE_KEY_NAME);
        List<Message> listMessage = RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.setValue(chatHistoryMessage.getTypeIndex()), chatHistoryMessage.getTagerId(), -1, Integer.MAX_VALUE);
        if (!CollectionUtils.isEmpty(listMessage)) {
            for (Message message : listMessage) {
                if (message.getContent() instanceof TextMessage) {
                    String content = message.getContent() != null ? ((TextMessage) message.getContent()).getContent() : "";
                    String name = RongContext.getInstance().getConversationTemplate(message.getConversationType().getName()).getTitle(message.getTargetId());
                    if (content.contains(keyName) || (!TextUtils.isEmpty(name) && name.contains(keyName))) {
                        allDataList.add(ChatHistoryMessage.formatMessageToChatHistoryMessage(message, this));
                    }
                }
            }
            listAdapter.setKeyName(keyName);
            listAdapter.setData(allDataList);
        }
    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.message_list);
        listAdapter = new SearchMessageListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatHistoryMessage message = (ChatHistoryMessage) parent.getAdapter().getItem(position);
                Conversation.ConversationType type = message.getType();
//                String userId = Utils.transferUserId(message.getTagerId(), true);
                RongIM.getInstance().startConversation(MessageSearchListActivity.this, type, message.getTagerId(), message.getStrName());
            }
        });
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
