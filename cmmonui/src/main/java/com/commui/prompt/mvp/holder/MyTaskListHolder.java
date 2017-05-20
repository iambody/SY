package com.commui.prompt.mvp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.widget.recycler.BaseHolder;
import com.commui.prompt.mvp.listener.MyTaskListener;

import app.privatefund.com.cmmonui.R2;
import butterknife.BindView;

/**
 * desc  taskHolder
 * Created by yangzonghui on 2017/5/11 12:08
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MyTaskListHolder extends BaseHolder {

    @BindView(R2.id.view_idt)
    public View view_idt;
    @BindView(R2.id.tv_idt_name)
    public TextView tv_idt_name;
    @BindView(R2.id.tv_idt_content)
    public TextView tv_idt_content;
    @BindView(R2.id.iv_idt_btn)
    public ImageView iv_idt_btn;
    @BindView(R2.id.rl_idt_root)
    public RelativeLayout rl_idt_root;

    public MyTaskListHolder(View itemView, final MyTaskListener listener) {
        super(itemView);

        rl_idt_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(getAdapterPosition());
            }
        });
    }

}
