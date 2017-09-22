package app.privatefund.com.im.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.cgbsoft.lib.AppManager;

import app.privatefund.com.im.Contants;
import app.privatefund.com.im.R;
import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.imkit.model.UIConversation;

/**
 * @author chenlong
 */
public class RongConversationListAdapter extends RongConversationBaseListAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public RongConversationListAdapter(Context context) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        RongCouldUtil.customServerTop(context, RongConversationListAdapter.this);
        super.notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void customServerTop() {
        UIConversation tempServer = null;
        UIConversation bindAdviser = null;
        String bindAdviserId = TextUtils.isEmpty(AppManager.getUserInfo(context).getToC().getBandingAdviserId()) ? "" : AppManager.getUserInfo(context).getToC().getBandingAdviserId();
        int index = 0;
        int bindAdvierIndex = 0;

        for (int i = 0; i < getCount(); i++) {
            UIConversation itemConversation = getItem(i);
            if (bindAdviserId.equals(itemConversation.getConversationTargetId())) {
                bindAdvierIndex = i;
                bindAdviser = itemConversation;
                break;
            }
        }

        if (bindAdviser != null) {
            this.remove(bindAdvierIndex);
            this.add(bindAdviser, 0);
        }

        for (int i = 0; i < getCount(); i++) {
            UIConversation itemConversation = getItem(i);
            if (context.getString(R.string.simuyun_server).equals(itemConversation.getUIConversationTitle())) {
                tempServer = itemConversation;
                index = i;

            }
        }
        if (tempServer != null) {
            this.remove(index);
            this.add(tempServer, bindAdviser != null ? 1 : 0);
        }
    }

}
