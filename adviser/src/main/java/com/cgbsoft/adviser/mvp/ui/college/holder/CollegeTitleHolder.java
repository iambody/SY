package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
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

    public CollegeTitleHolder(View itemView) {
        super(itemView);
    }
}
