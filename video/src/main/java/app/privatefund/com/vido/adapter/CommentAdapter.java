package app.privatefund.com.vido.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.VideoInfoEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import app.privatefund.com.vido.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/23-19:04
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<VideoInfoEntity.CommentBean> comments;

    public CommentAdapter(Context context, ArrayList<VideoInfoEntity.CommentBean> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        if (comments != null) {
            return comments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.video_comment_item, parent, false);
        } else {
            view = convertView;
        }

        ViewHolder holder =(ViewHolder) view.getTag();
        if (holder == null){
            holder = new ViewHolder();
            holder.time = (TextView) view.findViewById(R.id.item_time);
            holder.name = (TextView) view.findViewById(R.id.item_name);
            holder.comment = (TextView) view.findViewById(R.id.item_comment);
            holder.avatar = (ImageView) view.findViewById(R.id.item_avatar);
            view.setTag(holder);
        }

        holder.comment.setText(comments.get(position).commentContent);
        holder.name.setText(comments.get(position).sendName);
        holder.time.setText(comments.get(position).commentTime);
        BitmapUtils bu = new BitmapUtils(context);
        bu.display(holder.avatar,comments.get(position).sendAvatar);

        return view;
    }

    class ViewHolder{
        private ImageView avatar;
        private TextView name;
        private TextView comment;
        private TextView time;
    }

}
