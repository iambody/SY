package com.cgbsoft.lib.utils.tools.rong.data;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/25 15:34
 *  Email:zhangxyfs@126.com
 *  
 */
public interface RongServiceListener extends OnBaseListener {

    void sureListener(int position);
    void canceListener(int position);
}
