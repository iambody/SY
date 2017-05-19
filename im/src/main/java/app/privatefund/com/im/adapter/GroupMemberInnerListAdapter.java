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
import com.cgbsoft.lib.utils.tools.DataUtil;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.GroupMember;

/**
 * @author chenlong 16/11/14.
 */
public class GroupMemberInnerListAdapter extends BaseAdapter {

    private List<GroupMember.GroupMemberPerson> list = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public GroupMemberInnerListAdapter(Context context, List<GroupMember.GroupMemberPerson> list, LayoutInflater layoutInflater) {
        this.context = context;
        this.list = list;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupMember.GroupMemberPerson groupMemberPerson = list.get(position);
        ViewHolderContent viewHolderContent;
        if (convertView == null) {
            viewHolderContent = new ViewHolderContent();
            convertView = layoutInflater.inflate(R.layout.list_item_inner_group_member, null);
            viewHolderContent.imageView = (ImageView) convertView.findViewById(R.id.user_image);
            viewHolderContent.nameView = (TextView) convertView.findViewById(R.id.user_name);
            viewHolderContent.timeTextView = (TextView) convertView.findViewById(R.id.time_text);
            viewHolderContent.timeValueView = (TextView) convertView.findViewById(R.id.time_value);
            convertView.setTag(viewHolderContent);
        } else {
            viewHolderContent = (ViewHolderContent) convertView.getTag();
        }
        String url = groupMemberPerson.getImageUrl();
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = NetConfig.UPLOAD_FILE.concat(url);
            }

            Imageload.display(context, url, viewHolderContent.imageView);
        }
        viewHolderContent.nameView.setText(groupMemberPerson.getUserName());
        if (TextUtils.isEmpty(groupMemberPerson.getLastLoginTime())) {
            viewHolderContent.timeValueView.setVisibility(View.GONE);
            viewHolderContent.timeTextView.setText("无登录记录");
        } else {
            viewHolderContent.timeTextView.setText("最近登录");
            viewHolderContent.timeValueView.setVisibility(View.VISIBLE);
            viewHolderContent.timeValueView.setText(DataUtil.formatCalendarToTime(groupMemberPerson.getLastLoginTime()));
        }
        return convertView;
    }

    public class ViewHolderContent {
        public ImageView imageView;
        public TextView nameView;
        public TextView timeTextView;
        public TextView timeValueView;
    }
}
