package com.cgbsoft.lib.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.privatefund.bean.commui.HorizontalBean;

import java.util.List;

//import android.widget.BaseAdapter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/4-10:03
 */
public class HorizontalScrollViewAdapter {

    private List<HorizontalBean> horizontalBeen;
    private Context apContext;
    private LayoutInflater layoutInflater;
    private int resourceId;

    public HorizontalScrollViewAdapter(List<HorizontalBean> horizontalBeen, int resourceID, Context apContext) {
        this.horizontalBeen = horizontalBeen;
        this.apContext = apContext;
        this.resourceId = resourceID;
        this.layoutInflater = LayoutInflater.from(apContext);
    }

    public int getCount() {
//        return 2;
        return null == horizontalBeen ? 0 : horizontalBeen.size();
    }

    public Object getItem(int position) {
        return horizontalBeen.get(position);
    }

    //    @Override
    public long getItemId(int position) {
        return position;
    }

    //    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        item_horizontal_img

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(resourceId, null);
            viewHolder.item_horizontal_img = (ImageView) convertView.findViewById(R.id.item_horizontal_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HorizontalBean horizontalBean = horizontalBeen.get(position);
        Imageload.display(apContext, horizontalBean.getUrl(), viewHolder.item_horizontal_img);
        return convertView;
    }

    class ViewHolder {
        ImageView item_horizontal_img;
    }
}
