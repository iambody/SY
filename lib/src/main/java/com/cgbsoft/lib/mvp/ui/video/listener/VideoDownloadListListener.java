package com.cgbsoft.lib.mvp.ui.video.listener;

import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/12 18:15
 *  Email:zhangxyfs@126.com
 *  
 */
public interface VideoDownloadListListener extends OnBaseListener{

    void onItemClick(int position, ImageView iv_avd_cover, ImageView iv_avd_pause, TextView tv_avd_pause);

    void onCheck(int position, boolean isCheck);
}
