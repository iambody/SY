package app.privatefund.com.vido.mvc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import java.util.ArrayList;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.bean.Comment;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-19:29
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
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

        holder.comment.setText(comments.get(position).getCommentContent());
        holder.name.setText(comments.get(position).getSendName());
        holder.time.setText(comments.get(position).getCommentTime());
//        BitmapUtils bu = new BitmapUtils(context);
//        bu.display(holder.avatar,comments.get(position).getSendAvatar());
        Imageload.display(context,comments.get(position).getSendAvatar(),holder.avatar);
        return view;
    }

    class ViewHolder{
        private ImageView avatar;
        private TextView name;
        private TextView comment;
        private TextView time;
    }
}
