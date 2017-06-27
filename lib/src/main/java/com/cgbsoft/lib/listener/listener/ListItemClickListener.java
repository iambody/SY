package com.cgbsoft.lib.listener.listener;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * desc 所有的item点击的回调时间
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/27-12:04
 */
public interface ListItemClickListener<T> extends OnBaseListener {
    public void onItemClick(int position, T t);
}
