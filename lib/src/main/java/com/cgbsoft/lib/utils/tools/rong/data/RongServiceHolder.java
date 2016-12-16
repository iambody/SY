package com.cgbsoft.lib.utils.tools.rong.data;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.ButterKnife;

/**
 * Created by xiaoyu.zhang on 2016/11/25 15:34
 * Email:zhangxyfs@126.com
 *  
 */
public class RongServiceHolder extends BaseHolder {
    public TextView tv_vcd_title;
    public TextView tv_vcd_message;
    public Button btn_vcd_sure;
    public Button iv_vcd_cancel;

    public RongServiceHolder(View itemView, RongServiceListener listener) {
        super(itemView);
        tv_vcd_title = ButterKnife.findById(itemView, R.id.tv_vcd_title);
        tv_vcd_message = ButterKnife.findById(itemView, R.id.tv_vcd_message);
        btn_vcd_sure = ButterKnife.findById(itemView, R.id.btn_vcd_sure);
        iv_vcd_cancel = ButterKnife.findById(itemView, R.id.iv_vcd_cancel);

        btn_vcd_sure.setText("点击查看");
        iv_vcd_cancel.setVisibility(View.GONE);

        btn_vcd_sure.setOnClickListener(v -> listener.sureListener(getAdapterPosition()));
    }
}
