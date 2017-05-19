package app.privatefund.com.im.adapter;

import android.content.Context;

import app.privatefund.com.im.Contants;
import app.privatefund.com.im.R;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;

/**
 * @author chenlong
 */

public class RcConversationListAdapter extends ConversationListAdapter {

    private Context context;

    public RcConversationListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        customServerTop();
        super.notifyDataSetChanged();
    }

    private void customServerTop() {
        UIConversation tempServer = null;
        UIConversation topGroupConversation = null;
        int index = 0;
        int topGroupConversationIndex = 0;
        for (int i = 0; i < getCount(); i++) {
            UIConversation itemConversation = getItem(i);
            if (context.getString(R.string.simuyun_server).equals(itemConversation.getUIConversationTitle())) {
                tempServer = itemConversation;
                index = i;
            }
        }
        if (tempServer != null) {
            this.remove(index);
            this.add(tempServer, 0);
        }

        for (int i = 0; i < getCount(); i++) {
            UIConversation itemConversation = getItem(i);
            if (Contants.topConversationGroupId.equals(itemConversation.getConversationTargetId())) {
                topGroupConversationIndex = i;
                topGroupConversation = itemConversation;
                break;
            }
        }

        if (topGroupConversation != null) {
            this.remove(topGroupConversationIndex);
            this.add(topGroupConversation, 1);
        }
    }
}
