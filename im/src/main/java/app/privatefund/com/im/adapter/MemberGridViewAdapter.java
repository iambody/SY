package app.privatefund.com.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;

import java.util.ArrayList;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.GroupMember;

/**
 * @author chenlong
 */
public class MemberGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GroupMember.GroupMemberPerson> list;


    public MemberGridViewAdapter(Context context, ArrayList<GroupMember.GroupMemberPerson> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.grid_member_item, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.headImg = (ImageView) view.findViewById(R.id.member_img);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        }
        String url = list.get(i).getImageUrl();
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = NetConfig.UPLOAD_FILE.concat(url);
            }
            Imageload.display(context, url, viewHolder.headImg);
        }
        viewHolder.name.setText(list.get(i).getUserName());
        return view;
    }

    class ViewHolder {
        private ImageView headImg;
        private TextView name;
    }
}
