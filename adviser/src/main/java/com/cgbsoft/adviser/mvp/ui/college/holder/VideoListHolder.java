package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.ui.college.listener.VideoListListener;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * Created by xiaoyu.zhang on 2016/12/7 15:05
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoListHolder extends BaseHolder {
    @BindView(R2.id.ll_ivl_item)
    public LinearLayout ll_ivl_item;

    @BindView(R2.id.iv_ivl_img)
    public ImageView iv_ivl_img;

    @BindView(R2.id.tv_ivl_title)
    public TextView tv_ivl_title;

    @BindView(R2.id.tv_ivl_heart)
    public TextView tv_ivl_heart;

    @BindView(R2.id.tv_ivl_content)
    public TextView tv_ivl_content;

    @BindView(R2.id.tv_ivl_time)
    public TextView tv_ivl_time;

    public VideoListHolder(View itemView, VideoListListener listener) {
        super(itemView);
        ll_ivl_item.setOnClickListener(v -> listener.onVideoListItemClick(getAdapterPosition(), iv_ivl_img));
    }
}
