package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * Created by xiaoyu.zhang on 2016/12/1 14:16
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeGridHolder extends BaseHolder {
    @BindView(R2.id.ll_icb)
    public LinearLayout ll_icb;

    @BindView(R2.id.iv_icb_bg)
    public ImageView iv_icb_bg;

    @BindView(R2.id.tv_icb_title)
    public TextView tv_icb_title;

    @BindView(R2.id.tv_icb_content)
    public TextView tv_icb_content;

    public CollegeGridHolder(View itemView, int max) {
        super(itemView);

        if (getAdapterPosition() % 2 == 0) {
            ll_icb.setPadding(max, max / 2, max / 2, max / 2);
        } else {
            ll_icb.setPadding(max / 2, max / 2, max, max / 2);
        }
    }
}
