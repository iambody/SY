package com.cgbsoft.adviser.mvp.ui.college.listener;

import android.widget.ImageView;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/7 15:05
 *  Email:zhangxyfs@126.com
 *  
 */
public interface VideoListListener extends OnBaseListener {
    void onVideoListItemClick(int position, ImageView iv_ivl_img);
}
