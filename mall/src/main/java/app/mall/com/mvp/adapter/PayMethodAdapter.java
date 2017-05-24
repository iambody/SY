package app.mall.com.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.mall.com.model.PayMethod;
import qcloud.mall.R;

/**
 * desc
 * Created by yangzonghui on 2017/5/21 17:35
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayMethodAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PayMethod> payMethods;
    private int checked;

    public PayMethodAdapter(Context context, ArrayList<PayMethod> payMethods, int checked) {
        this.context = context;
        this.payMethods = payMethods;
        this.checked = checked;
    }

    @Override
    public int getCount() {
        if (payMethods != null)
            return payMethods.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return payMethods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.pay_method_item, parent, false);
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.check = (ImageView) view.findViewById(R.id.paymethod_check);
            viewHolder.payIcon = (ImageView) view.findViewById(R.id.image_pay);
            viewHolder.payName = (TextView) view.findViewById(R.id.pay_name);
            viewHolder.limit = (TextView) view.findViewById(R.id.pay_limit);
            view.setTag(viewHolder);
        }
        viewHolder.payName.setText(payMethods.get(position).getName());
        PayMethod payMethod = payMethods.get(position);
        viewHolder.payIcon.setImageResource(403 == payMethod.getTypeCode() ? R.drawable.pay_weixin_icon : 401 == payMethod.getTypeCode() ? R.drawable.pay_zhifubao_icon : R.drawable.pay_yinlian_icon);
        viewHolder.limit.setText(String.format("(最高限额 : ¥%d)", payMethod.getMaxLimit()));

        if (checked == payMethod.getTypeCode()) {
            viewHolder.check.setImageResource(R.drawable.paymethod_select_pre);
        } else {
            viewHolder.check.setImageResource(R.drawable.paymethod_select_nor);
        }

        return view;
    }

    public void check(int typeCode) {
        this.checked = typeCode;
        notifyDataSetChanged();
    }

    public int getCheck() {
        return checked;
    }

    private class ViewHolder {
        private TextView limit;
        private ImageView payIcon;
        private TextView payName;
        private ImageView check;

    }
}
