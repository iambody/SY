package app.privatefund.com.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.GroupMember;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 群成员列表
 *
 * @author chenlong
 */
public class GroupChatMemberListAdapter extends BaseAdapter {

    private Context mContext = null;//上下文
    private LayoutInflater mInflater = null;
    private List<GroupMember> mData = new ArrayList<>();

    public GroupChatMemberListAdapter(Context context, List<GroupMember> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void add(List<GroupMember> list) {
        mData.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return this.mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        final GroupMember groupMember = mData.get(position);
        final ViewHolderTitle viewHolderTitle;
        if (convertView == null) {
            //没有缓存过
            viewHolderTitle = new ViewHolderTitle();
            convertView = this.mInflater.inflate(R.layout.acitivity_list_item_group_chat, null, false);
            viewHolderTitle.nameLinearLayout = (LinearLayout) convertView.findViewById(R.id.org_parent);
            viewHolderTitle.orgTextView = (TextView) convertView.findViewById(R.id.org_name);
            viewHolderTitle.flagTextView = (TextView) convertView.findViewById(R.id.org_show_flag);
            viewHolderTitle.innerListView = (ListView) convertView.findViewById(R.id.list_inner);
            convertView.setTag(viewHolderTitle);
        } else {
            viewHolderTitle = (ViewHolderTitle) convertView.getTag();
        }
        viewHolderTitle.flagTextView.setText(viewHolderTitle.innerListView.getVisibility() == View.VISIBLE ? R.string.org_member_hide : R.string.org_member_show);
        viewHolderTitle.orgTextView.setText(groupMember.getOrgName());
        GroupMemberInnerListAdapter adapter = new GroupMemberInnerListAdapter(mContext, groupMember.getMembers(), mInflater);
        viewHolderTitle.innerListView.setAdapter(adapter);
        viewHolderTitle.nameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolderTitle.innerListView.getVisibility() == View.VISIBLE) {
                    viewHolderTitle.innerListView.setVisibility(View.GONE);
                    viewHolderTitle.flagTextView.setText(R.string.org_member_show);
                } else if (viewHolderTitle.innerListView.getVisibility() == View.GONE) {
                    viewHolderTitle.innerListView.setVisibility(View.VISIBLE);
                    viewHolderTitle.flagTextView.setText(R.string.org_member_hide);
                }
            }
        });
        viewHolderTitle.innerListView.setOnItemClickListener((parent, view, sunPosition, id) -> {
            List<GroupMember.GroupMemberPerson> groupMemberPersonList = mData.get(position).getMembers();
            if (!CollectionUtils.isEmpty(groupMemberPersonList)) {
                GroupMember.GroupMemberPerson person = groupMemberPersonList.get(sunPosition);
                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE, person.getUserId(), person.getUserName());
            }
        });
        setListViewHeightBasedOnChildren(viewHolderTitle.innerListView);
        return convertView;
    }

    /**
     * 此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
     * 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public class ViewHolderTitle {
        public LinearLayout nameLinearLayout;
        public TextView orgTextView;
        public TextView flagTextView;
        public ListView innerListView;
    }
}