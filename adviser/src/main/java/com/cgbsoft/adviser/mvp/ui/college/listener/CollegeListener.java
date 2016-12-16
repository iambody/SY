package com.cgbsoft.adviser.mvp.ui.college.listener;

import android.widget.ImageView;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/1 10:58
 *  Email:zhangxyfs@126.com
 *  
 */
public interface CollegeListener extends OnBaseListener {

    void onHeadBtnClick(int which);

    void onTitleClick();

    void onFirstVideoClick(ImageView iv_ich_bg);

    void onGridItemClick(int position, ImageView iv_icb_bg);
}
