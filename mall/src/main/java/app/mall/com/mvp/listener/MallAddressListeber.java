package app.mall.com.mvp.listener;

import android.widget.LinearLayout;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * desc  商城地址列表监听
 * Created by yangzonghui on 2017/5/10 21:15
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface MallAddressListeber extends OnBaseListener{

    void onItemClick(int position, LinearLayout linear);

    void onItemLongClick(int position,LinearLayout linear);
}
