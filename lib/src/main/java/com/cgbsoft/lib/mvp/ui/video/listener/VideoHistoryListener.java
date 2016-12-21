package com.cgbsoft.lib.mvp.ui.video.listener;

import android.widget.ImageView;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/12 18:15
 *  Email:zhangxyfs@126.com
 *  
 */
public interface VideoHistoryListener extends OnBaseListener{

    void onItemClick(int position, ImageView iv_avh_cover);

    void onCheck(int position, boolean isCheck);
}
