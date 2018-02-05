package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/14-14:29
 */
public class OperationAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int resourceId;
    private List<HomeEntity.Operate> resourceData = new ArrayList<>();
    private int numColumns;

    public OperationAdapter(Context acontext, int resourceId) {
        this.context = acontext;
        this.resourceId = resourceId;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<HomeEntity.Operate> resource, int numcolumns) {
        this.resourceData = resource;
        this.numColumns = numcolumns;
        OperationAdapter.this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return resourceData.size();
    }

    @Override
    public Object getItem(int position) {
        return resourceData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OperationItem operationItem = null;
        if (null == convertView) {
            operationItem = new OperationItem();
            convertView = layoutInflater.inflate(resourceId, null);
            operationItem.logoImageView = ViewHolders.get(convertView, R.id.item_operation_iv);
            operationItem.operationdes = ViewHolders.get(convertView, R.id.item_operation_des);
            convertView.setTag(operationItem);
        } else {
            operationItem = (OperationItem) convertView.getTag();
        }
        BStrUtils.SetTxt(operationItem.operationdes, resourceData.get(position).title);
        ivOption(operationItem.logoImageView, numColumns);
        Imageload.display(context, resourceData.get(position).imageUrl, operationItem.logoImageView);
        return convertView;
    }

    class OperationItem {
        ImageView logoImageView;
        TextView operationdes;
    }

    private void ivOption(ImageView view, int Columns) {
        int width = Utils.getScreenWidth(context);
        int itemWidth = ((width - DimensionPixelUtil.dip2px(context, (Columns + 1) * 10)) / Columns) * 2 / 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemWidth);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(params);
    }
}
