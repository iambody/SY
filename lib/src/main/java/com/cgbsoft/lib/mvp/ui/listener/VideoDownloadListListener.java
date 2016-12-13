package com.cgbsoft.lib.mvp.ui.listener;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/12/12 18:15
 *  Email:zhangxyfs@126.com
 *  
 */
public interface VideoDownloadListListener extends OnBaseListener{

    void onItemClick(int position);

    void onCheck(int position, boolean isCheck);
}
