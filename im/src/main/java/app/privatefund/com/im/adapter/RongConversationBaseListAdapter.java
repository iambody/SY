package app.privatefund.com.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.privatefund.com.im.R;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.adapter.BaseAdapter;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 */
public class RongConversationBaseListAdapter extends BaseAdapter<UIConversation> {
    private static final String TAG = "ConversationListAdapter";
    LayoutInflater mInflater;
    Context mContext;
    private io.rong.imkit.widget.adapter.ConversationListAdapter.OnPortraitItemClick mOnPortraitItemClick;

    public long getItemId(int position) {
        UIConversation conversation = (UIConversation) this.getItem(position);
        return conversation == null ? 0L : (long) conversation.hashCode();
    }

    public RongConversationBaseListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public int findGatheredItem(Conversation.ConversationType type) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            UIConversation uiConversation = (UIConversation) this.getItem(index);
            if (uiConversation.getConversationType().equals(type)) {
                position = index;
                break;
            }
        }
        return position;
    }

    public int findPosition(Conversation.ConversationType type, String targetId) {
        int index = this.getCount();
        int position = -1;

        while (index-- > 0) {
            if (((UIConversation) this.getItem(index)).getConversationType().equals(type) && ((UIConversation) this.getItem(index)).getConversationTargetId().equals(targetId)) {
                position = index;
                break;
            }
        }
        return position;
    }

    protected View newView(Context context, int position, ViewGroup group) {
        View result = null;
//            if (position == 0) {
        result = this.mInflater.inflate(R.layout.rc_item_conversation_new, (ViewGroup) null);
//            } else {
//                result = this.mInflater.inflate(R.layout.rc_item_conversation, (ViewGroup)null);
//            }
        RongConversationBaseListAdapter.ViewHolder holder = new ViewHolder();
        holder.layout = this.findViewById(result, R.id.rc_item_conversation);
        holder.leftImageLayout = this.findViewById(result, R.id.rc_item1);
        holder.rightImageLayout = this.findViewById(result, R.id.rc_item2);
        holder.leftImageView = (AsyncImageView) this.findViewById(result, R.id.rc_left);
        holder.rightImageView = (AsyncImageView) this.findViewById(result, R.id.rc_right);
        holder.contentView = (ProviderContainerView) this.findViewById(result, R.id.rc_content);
        holder.unReadMsgCount = (TextView) this.findViewById(result, R.id.rc_unread_message);
        holder.unReadMsgCountRight = (TextView) this.findViewById(result, R.id.rc_unread_message_right);
        holder.unReadMsgCountIcon = (ImageView) this.findViewById(result, R.id.rc_unread_message_icon);
        holder.unReadMsgCountRightIcon = (ImageView) this.findViewById(result, R.id.rc_unread_message_icon_right);
        holder.dividerItem = (LinearLayout) this.findViewById(result, R.id.rc_item_height_divider);
        result.setTag(holder);
        return result;
    }

    protected void bindView(View v, int position, final UIConversation data) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (data != null) {
            IContainerItemProvider.ConversationProvider provider = RongContext.getInstance().getConversationTemplate(data.getConversationType().getName());
            if (provider == null) {
                RLog.e("ConversationListAdapter", "provider is null");
            } else {
                boolean isKehu = "dd0cc61140504258ab474b8f0a38bb56".equals(data.getConversationSenderId()) || "dd0cc61140504258ab474b8f0a38bb56".equals(data.getConversationTargetId());
                holder.dividerItem.setVisibility(isKehu ? View.VISIBLE : View.GONE);
                View view = holder.contentView.inflate(provider);
                provider.bindView(view, position, data);
//                if (data.isTop()) {
//                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.rc_item_top_list_selector));
//                } else {
//                    holder.layout.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.rc_item_list_selector));
//                }
                ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(data.getConversationType().getName());
                boolean defaultId = false;
                int defaultId1;
                if (tag.portraitPosition() == 1) {
                    holder.leftImageLayout.setVisibility(0);
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                    } else {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
                    }

                    holder.leftImageLayout.setOnClickListener(v1 -> {
                        if (mOnPortraitItemClick != null) {
                            mOnPortraitItemClick.onPortraitItemClick(v1, data);
                        }

                    });
                    holder.leftImageLayout.setOnLongClickListener(v12 -> {
                        if (mOnPortraitItemClick != null) {
                            mOnPortraitItemClick.onPortraitItemLongClick(v12, data);
                        }

                        return true;
                    });
                    if (data.getConversationGatherState()) {
                        holder.leftImageView.setAvatar((String) null, defaultId1);
                    } else if (data.getIconUrl() != null) {
                        holder.leftImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                    } else {
                        holder.leftImageView.setAvatar((String) null, defaultId1);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountIcon.setVisibility(0);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCount.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCount.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCount.setVisibility(0);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(8);
                            holder.unReadMsgCountIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_list_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(8);
                        holder.unReadMsgCount.setVisibility(8);
                    }

                    holder.rightImageLayout.setVisibility(8);
                } else if (tag.portraitPosition() == 2) {
                    holder.rightImageLayout.setVisibility(0);
                    holder.rightImageLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (mOnPortraitItemClick != null) {
                                mOnPortraitItemClick.onPortraitItemClick(v, data);
                            }

                        }
                    });
                    holder.rightImageLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            if (mOnPortraitItemClick != null) {
                                mOnPortraitItemClick.onPortraitItemLongClick(v, data);
                            }

                            return true;
                        }
                    });
                    if (data.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_group_portrait;
                    } else if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_discussion_portrait;
                    } else {
                        defaultId1 = io.rong.imkit.R.drawable.rc_default_portrait;
                    }

                    if (data.getConversationGatherState()) {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    } else if (data.getIconUrl() != null) {
                        holder.rightImageView.setAvatar(data.getIconUrl().toString(), defaultId1);
                    } else {
                        holder.rightImageView.setAvatar((String) null, defaultId1);
                    }

                    if (data.getUnReadMessageCount() > 0) {
                        holder.unReadMsgCountRightIcon.setVisibility(0);
                        if (data.getUnReadType().equals(UIConversation.UnreadRemindType.REMIND_WITH_COUNTING)) {
                            holder.unReadMsgCount.setVisibility(0);
                            if (data.getUnReadMessageCount() > 99) {
                                holder.unReadMsgCountRight.setText(this.mContext.getResources().getString(io.rong.imkit.R.string.rc_message_unread_count));
                            } else {
                                holder.unReadMsgCountRight.setText(Integer.toString(data.getUnReadMessageCount()));
                            }

                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_count_bg);
                        } else {
                            holder.unReadMsgCount.setVisibility(8);
                            holder.unReadMsgCountRightIcon.setImageResource(io.rong.imkit.R.drawable.rc_unread_remind_without_count);
                        }
                    } else {
                        holder.unReadMsgCountIcon.setVisibility(8);
                        holder.unReadMsgCount.setVisibility(8);
                    }

                    holder.leftImageLayout.setVisibility(8);
                } else {
                    if (tag.portraitPosition() != 3) {
                        throw new IllegalArgumentException("the portrait position is wrong!");
                    }

                    holder.rightImageLayout.setVisibility(8);
                    holder.leftImageLayout.setVisibility(8);
                }

            }
        }
    }

    public void setOnPortraitItemClick(io.rong.imkit.widget.adapter.ConversationListAdapter.OnPortraitItemClick onPortraitItemClick) {
        this.mOnPortraitItemClick = onPortraitItemClick;
    }


    public interface OnPortraitItemClick {
        void onPortraitItemClick(View var1, UIConversation var2);

        boolean onPortraitItemLongClick(View var1, UIConversation var2);
    }

    class ViewHolder {
        View layout;
        View leftImageLayout;
        View rightImageLayout;
        AsyncImageView leftImageView;
        TextView unReadMsgCount;
        ImageView unReadMsgCountIcon;
        AsyncImageView rightImageView;
        TextView unReadMsgCountRight;
        ImageView unReadMsgCountRightIcon;
        ProviderContainerView contentView;
        LinearLayout dividerItem;

        ViewHolder() {
        }
    }
}
