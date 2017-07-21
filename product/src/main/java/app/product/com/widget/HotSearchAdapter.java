package app.product.com.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.model.SearchResultBean;
import app.product.com.mvc.ui.SearchBaseActivity;

/**
 *  热搜列表
 *
 * @author chenlong
 */
public class HotSearchAdapter<T> extends BaseAdapter {

    private Context mContext = null;//上下文
    private LayoutInflater mInflater = null;
    private List<SearchResultBean.ResultBean> mData = new ArrayList<>();
    private String currentType;

    public HotSearchAdapter(Context context, String currentType) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.currentType = currentType;
    }

    public void setData(List<SearchResultBean.ResultBean> list) {
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

    @Override
    public int getItemViewType(int position) {
        if (isMutilyView()) {
            return position == 0 ? 0 : 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public int getViewTypeCount() {
        if (isMutilyView()) {
            return 2;
        }
        return super.getViewTypeCount();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        SearchResultBean.ResultBean hotSearch = mData.get(position);
        ViewHolder viewHolderTitle;
        if (convertView == null ) {
            viewHolderTitle = new ViewHolder();
            if (isMutilyView() && getItemViewType(position) == 0) {
                convertView = this.mInflater.inflate(R.layout.list_item_hot_search_image , viewGroup, false);
                viewHolderTitle.imageView = (ImageView) convertView.findViewById(R.id.person_image_id);
                viewHolderTitle.flagView = (ImageView) convertView.findViewById(R.id.first_item_image);
                viewHolderTitle.imageViewPlay = (ImageView) convertView.findViewById(R.id.play_image_flag);
                viewHolderTitle.nameView = (TextView) convertView.findViewById(R.id.content);
                viewHolderTitle.timeView = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(viewHolderTitle);
            } else {
                convertView = this.mInflater.inflate(R.layout.list_item_hot_search_text , viewGroup, false);
                viewHolderTitle.number = (TextView) convertView.findViewById(R.id.number);
                viewHolderTitle.nameView = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolderTitle);
            }
        } else {
            viewHolderTitle = (ViewHolder) convertView.getTag();
        }

        if (isMutilyView() && position == 0) {
//            bitmapUtils.display(viewHolderTitle.imageView, hotSearch.getImgUrl(), new BitmapLoadCallBack<ImageView>() {
//                @Override
//                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//                    if (bitmap != null) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//
//                @Override
//                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
//
//                }
//            });
            Imageload.display( mContext,hotSearch.getImgUrl(),viewHolderTitle.imageView);
            viewHolderTitle.nameView.setText(hotSearch.getInfoName());
            viewHolderTitle.flagView.setImageResource(R.drawable.one);
            viewHolderTitle.timeView.setText(hotSearch.getInfoCreateTime());
            viewHolderTitle.imageViewPlay.setVisibility(SearchBaseActivity.VIDEO.equals(hotSearch.getInfoType()) ? View.VISIBLE : View.GONE);
        } else {
            if (position < 3) {
                viewHolderTitle.number.setTextColor(ContextCompat.getColor(mContext, R.color.app_golden));
            }
            viewHolderTitle.number.setText(String.valueOf(position + 1));
            viewHolderTitle.nameView.setText(hotSearch.getInfoName());
        }
        return convertView;
    }

    private boolean isMutilyView() {
        return TextUtils.equals(currentType, SearchBaseActivity.ZIXUN) ||
                TextUtils.equals(currentType, SearchBaseActivity.VIDEO);
    }

    public static class ViewHolder {
        public ImageView imageView;
        public ImageView flagView;
        public ImageView imageViewPlay;
        public TextView nameView;
        public TextView timeView;
        public TextView number;
    }
}