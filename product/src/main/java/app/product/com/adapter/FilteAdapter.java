package app.product.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.taglayout.OnInitSelectedPosition;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.model.Series;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/11-10:32
 */
public class FilteAdapter extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<Series> mDataList;

    public FilteAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<Series>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.product_filter_tag_item, null);

        TextView textView = (TextView) view.findViewById(R.id.product_dilte_pop_tv_tag);
        Series t = mDataList.get(position);

        BStrUtils.SetTxt(textView,t.getName());

        return view;
    }

    public void onlyAddAll(List<Series> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<Series> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (mDataList.get(position).isChecked()) {
            return true;
        }
        return false;
    }

}
