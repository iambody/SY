package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/1 14:04
 *  Email:zhangxyfs@126.com
 *  
 */
public class CollegeTitleHolder extends BaseHolder {
    @BindView(R2.id.tv_ict_title)
    public TextView tv_ict_title;

    @BindView(R2.id.iv_ict_arrow)
    public ImageView iv_ict_arrow;

    @BindView(R2.id.rl_ict)
    public RelativeLayout rl_ict;

    public CollegeTitleHolder(View itemView, CollegeListener listener) {
        super(itemView);
        rl_ict.setOnClickListener(v -> {
            if(iv_ict_arrow.getVisibility() == View.VISIBLE){
                listener.onTitleClick();
            }
        });
    }
}
