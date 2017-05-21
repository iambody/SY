package app.privatefund.com.im.listener;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.privatefund.bean.share.NewsBean;


import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.NewsMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by lee on 2016/11/17.
 */
@ProviderTag(messageContent = NewsMessage.class)
public class NewMessageItemProvider extends IContainerItemProvider.MessageProvider<NewsMessage> {

    class ViewHolder {
        TextView message;
        ImageView imageView;
        LinearLayout linearLayout;
    }

    private Context context;
    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversition_new_item, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.text1);
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.linearLayout = (LinearLayout) view.findViewById(R.id.background);
        this.context = context;
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, NewsMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.linearLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.message.setText(content.getTitle());

    }

    @Override
    public Spannable getContentSummary(NewsMessage data) {
        return new SpannableString("[链接]" + data.getTitle());
    }

    @Override
    public void onItemClick(View view, int i, NewsMessage newsMessage, UIMessage uiMessage) {
        NewsBean newsBean = new NewsBean();
        newsBean.setSummary(newsMessage.getSummary());
        newsBean.setCategory(newsMessage.getCategory());
        newsBean.setTitle(newsMessage.getTitle());
        newsBean.setLikes(Integer.parseInt(newsMessage.getLikes()));
        newsBean.setInfoId(newsMessage.getInfoId());
//        newsBean.setDate(newsMessage.getDate());
//        newsBean.setUrl(newsMessage.getUrl());
        newsBean.setIsLike(newsMessage.getIsLike());
        if (!AppManager.isInvestor(BaseApplication.getContext())) {
           /* Intent intent = new Intent(context, FoundNewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(FoundNewsDetailActivity.NEW_PARAM_NAME, newsBean);
            intent.putExtras(bundle);
            context.startActivity(intent);*/
        }else {
//            HashMap<String,String> hashMap = new HashMap<>();
//            hashMap.put("1", "行业资讯");
//            hashMap.put("2", "云观察");
//            hashMap.put("3", "云观点");
//            hashMap.put("4", "早知道");
//            Intent intent = new Intent(context, PushMsgActivity.class);
//            intent.putExtra(Contant.push_message_url, Domain.baseWebsite+"/apptie/new_detail_toc.html?id="+newsBean.getInfoId()+"&category="+newsBean.getCategory());
//            intent.putExtra(Contant.push_message_title, hashMap.get(newsBean.getCategory()));
//            intent.putExtra(Contant.RIGHT_SAVE, false);
//            intent.putExtra(Contant.RIGHT_SHARE, true);
//            intent.putExtra(Contant.PAGE_INIT, false);
//            ((Activity)context).startActivityForResult(intent, 300);
        }
    }

    @Override
    public void onItemLongClick(View view, int i, NewsMessage newsMessage, UIMessage uiMessage) {

    }
}