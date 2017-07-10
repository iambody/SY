package app.mall.com.mvp.holder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.widget.recycler.BaseHolder;

import app.mall.com.mvp.listener.MallAddressListeber;
import butterknife.BindView;
import qcloud.mall.R2;

/**
 * desc  商城holder
 * Created by yangzonghui on 2017/5/10 21:23
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MallAddressListHolder extends BaseHolder {

    @BindView(R2.id.mall_address_name)
    public TextView mall_item_name;

    @BindView(R2.id.mall_address_phone)
    public TextView mall_item_phone;

    @BindView(R2.id.mall_address_address)
    public TextView mall_item_address;

    @BindView(R2.id.mall_item_linear)
    public LinearLayout mall_item_linear;

    @BindView(R2.id.item_linear)
    public LinearLayout item_linear;

    @BindView(R2.id.mall_address_normal)
    public ImageView mall_address_normal;

    @BindView(R2.id.mall_edit_address)
    public ImageView mall_edit_address;

    public MallAddressListHolder(View itemView, final MallAddressListeber listeber) {
        super(itemView);
        item_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeber.onItemClick(getAdapterPosition(), mall_item_linear);
            }
        });
        mall_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeber.onItemEditClick(getAdapterPosition(), mall_item_linear);
            }
        });
    }
}
