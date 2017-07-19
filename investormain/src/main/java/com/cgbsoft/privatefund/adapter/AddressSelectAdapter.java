package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * @author chenlong
 *         地址选择dialog
 */
public class AddressSelectAdapter extends BaseAdapter {
    private Context context;
    private List<String> addresses;

    public AddressSelectAdapter(Context context, List<String> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    public void setData(List<String> list) {
        this.addresses = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (addresses == null) {
            return 0;
        }
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String addressChoose = addresses.get(position);
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_select_layout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }
        viewHolder.name.setText(addressChoose);
        return convertView;
    }

    class ViewHolder {
        private TextView name;
    }
}
