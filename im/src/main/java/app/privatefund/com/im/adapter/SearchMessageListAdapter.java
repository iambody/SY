package app.privatefund.com.im.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.DataUtil;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.privatefund.com.im.R;
import app.privatefund.com.im.bean.ChatHistoryMessage;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 *
 * @author chenlong
 */
public class SearchMessageListAdapter extends BaseAdapter {

    private Context mContext = null;//上下文
    private LayoutInflater mInflater = null;
    private List<ChatHistoryMessage> mData = new ArrayList<>();
    private String keyName;

    public SearchMessageListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<ChatHistoryMessage> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return this.mData.size();
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ChatHistoryMessage message = mData.get(position);
        Conversation.ConversationType conversationType = message.getType();
        ViewHolder viewHolderTitle;
        if (convertView == null) {
            //没有缓存过
            viewHolderTitle = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.list_item_message_search, viewGroup, false);
            viewHolderTitle.imageView = (ImageView) convertView.findViewById(R.id.person_image_id);
            viewHolderTitle.nameView = (TextView) convertView.findViewById(R.id.name);
            viewHolderTitle.flagView = (TextView) convertView.findViewById(R.id.flag);
            viewHolderTitle.contentView = (TextView) convertView.findViewById(R.id.content);
            viewHolderTitle.timeView = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolderTitle);
        } else {
            viewHolderTitle = (ViewHolder) convertView.getTag();
        }
        viewHolderTitle.flagView.setVisibility(View.GONE);

        switch (conversationType) {
            case  PRIVATE:
                message.setStrName(message.getStrName());
                viewHolderTitle.nameView.setText(message.getStrName());
                viewHolderTitle.contentView.setText(message.isPolymerize() ? message.getShowContent() : message.getContent());
                if (!TextUtils.isEmpty(keyName)) {
                    ViewUtils.setTextColor(mContext, viewHolderTitle.nameView, Arrays.asList(new String[]{keyName}));
                    ViewUtils.setTextColor(mContext, viewHolderTitle.contentView, Arrays.asList(new String[]{keyName}));
                }
                    if (!TextUtils.isEmpty(message.getTime()) && !"null".equals(message.getTime())) {
                        viewHolderTitle.timeView.setText(DataUtil.formatRongyunData(message.getTime()));
                    }
                    if (message.getImageUrl() != null) {
                        new BitmapUtils(mContext).display(viewHolderTitle.imageView, message.getImageUrl().toString(), new BitmapLoadCallBack<ImageView>() {
                            @Override
                            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                                //imageView.setImageResource(R.drawable.touxiang_default);
                                imageView.setImageResource(R.drawable.logoshare);
                            }
                        });
                    } else {
                        viewHolderTitle.imageView.setImageResource(R.drawable.logoshare);
                    }
                break;
            case GROUP:
                GroupUserInfo groupUserInfo = RongUserInfoManager.getInstance().getGroupUserInfo(message.getTagerId(), message.getSenderId());
                Group group = RongUserInfoManager.getInstance().getGroupInfo(message.getTagerId());
                message.setStrName(group.getName());
                if (groupUserInfo != null && group != null) {
                    viewHolderTitle.nameView.setText(group.getName());
                    viewHolderTitle.contentView.setText(message.isPolymerize() ? message.getShowContent() : message.getContent());
                    if (!TextUtils.isEmpty(keyName)) {
                        ViewUtils.setTextColor(mContext, viewHolderTitle.nameView, Arrays.asList(new String[]{keyName}));
                        ViewUtils.setTextColor(mContext, viewHolderTitle.contentView, Arrays.asList(new String[]{keyName}));
                    }
                    if (!TextUtils.isEmpty(message.getTime()) && !"null".equals(message.getTime())) {
                        viewHolderTitle.timeView.setText(DataUtil.formatRongyunData(message.getTime()));
                    }
                    if (group.getPortraitUri() != null) {
                        new BitmapUtils(mContext).display(viewHolderTitle.imageView, group.getPortraitUri().toString(), new BitmapLoadCallBack<ImageView>() {
                            @Override
                            public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                                if (bitmap != null) {
                                    imageView.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                                imageView.setImageResource(R.drawable.logoshare);
                            }
                        });
                    } else {
                        viewHolderTitle.imageView.setImageResource(R.drawable.logoshare);
                    }
                }
            break;
        }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public TextView flagView;
        public TextView contentView;
        public TextView timeView;
    }
}