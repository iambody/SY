package qcloud.liveold.mvp.adapters;


import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import qcloud.liveold.R;
import qcloud.liveold.mvp.model.ChatEntity;
import qcloud.liveold.mvp.model.MemberInfo;


public class ChatMsgListAdapter extends BaseAdapter {

    private static String TAG = ChatMsgListAdapter.class.getSimpleName();
    private static final int ITEMCOUNT = 9;
    private List<ChatEntity> listMessage = null;
    private LayoutInflater inflater;
    private LinearLayout layout;
    public static final int TYPE_TEXT_SEND = 0;
    public static final int TYPE_TEXT_RECV = 1;
    private Context context;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private ViewHolder holder;
    private MemberInfo host;
    private BaseApplication mApplication;

    public ChatMsgListAdapter(Context context, List<ChatEntity> objects) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listMessage = objects;
        host = new MemberInfo();
    }


    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatEntity entity = listMessage.get(position);

        Log.d(TAG, "ChatMsgListAdapter senderName " + entity.getSenderName());


        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(R.layout.chat_item_left, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.tv_chatcontent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mApplication = (BaseApplication) context.getApplicationContext();
        String sendId = entity.getSendId();
        if (sendId == null) {
            sendId = "asd";
        }
        String liveHostId = SPreference.getString(context,"liveHostId");
        if (entity.getContext().startsWith("<font")){
            CharSequence charSequence = Html.fromHtml(entity.getContext());
            holder.text.setText(charSequence);
        } else if (sendId.equals(liveHostId)) {
            //是主播
            String msg = entity.getContext();
            String content = msg.substring(msg.indexOf("&") + 1);
            String userName = msg.substring(0, msg.indexOf("&"));
            String html = "<font color='#ffde00'>" + "平台公告" + ":" + "</font>" + "<font color='#ffde00'>" + content + "</font>";
            CharSequence charSequence = Html.fromHtml(html);
            holder.text.setText(charSequence);
        } else {
            String msg = entity.getContext();
            String content = msg.substring(msg.indexOf("&") + 1);
            String userName = msg.substring(0, msg.indexOf("&"));
//            String userName =  entity.getSenderName();
            if (TextUtils.isEmpty(userName)) {
                userName = "私享云用户";
            }
            Pattern p = Pattern.compile("^((13[0-9])|(14[5-7])|(15[0-9])|(17[0-8])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(userName);
            boolean b = m.matches();
            if (b) {
                userName = "私享云用户";
            }
            String html = "<font color='#5ba8f3'>" + userName + ":" + "</font>" + "<font color='#ffffff'>" + content + "</font>";
            CharSequence charSequence = Html.fromHtml(html);
            holder.text.setText(charSequence);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView text;
    }

    public void refreshHost(MemberInfo memberInfo) {
        host = memberInfo;
    }
}
