package app.mall.com.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cgbsoft.lib.widget.recycler.BaseAdapter;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.holder.MallAddressListHolder;
import app.mall.com.mvp.listener.MallAddressListeber;
import qcloud.mall.R;

/**
 * desc  商城地址列表
 * Created by yangzonghui on 2017/5/10 21:11
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MallListAdapter extends BaseAdapter<MallAddressBean, MallAddressListeber, RecyclerView.ViewHolder> {

    public MallListAdapter(MallAddressListeber listener) {
        super(listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MallAddressBean.LIST)
            return new MallAddressListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_address, parent, false), listener);
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MallAddressBean model = list.get(position);

        if (holder instanceof MallAddressListHolder) {
            MallAddressListHolder vhh = (MallAddressListHolder) holder;
            vhh.mall_item_address.setText(model.getAddress());
            vhh.mall_item_name.setText(model.getShopping_name());
            vhh.mall_item_phone.setText(model.getPhone());
            if (model.getDefault_flag() == 1) {
                vhh.mall_item_linear.setBackgroundColor(0xffd0d0d0);
            } else {
                vhh.mall_item_linear.setBackgroundColor(0xffffffff);
            }
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
