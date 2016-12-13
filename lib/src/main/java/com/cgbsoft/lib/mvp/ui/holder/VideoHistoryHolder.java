package com.cgbsoft.lib.mvp.ui.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.mvp.ui.listener.VideoHistoryListener;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * Created by xiaoyu.zhang on 2016/12/12 18:14
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoHistoryHolder extends BaseHolder {
    @BindView(R2.id.avh_head_line)
    View avh_head_line;

    @BindView(R2.id.fl_avh_check)
    public FrameLayout fl_avh_check;

    @BindView(R2.id.ll_avh)
    public LinearLayout ll_avh;

    @BindView(R2.id.cb_avh)
    public CheckBox cb_avh;

    @BindView(R2.id.iv_avh_cover)
    public ImageView iv_avh_cover;

    @BindView(R2.id.tv_avh_title)
    public TextView tv_avh_title;

    @BindView(R2.id.tv_avh_time)
    public TextView tv_avh_time;

    public VideoHistoryHolder(View itemView, VideoHistoryListener listener) {
        super(itemView);
        int width = Utils.convertDipOrPx(context, 150);
        int height = width * 9 / 16;

        ViewGroup.LayoutParams lp = iv_avh_cover.getLayoutParams();
        lp.width = width;
        lp.height = height;
        iv_avh_cover.setLayoutParams(lp);

        if (getAdapterPosition() == 0) {
            avh_head_line.setVisibility(View.VISIBLE);
        }

        iv_avh_cover.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), iv_avh_cover));
        cb_avh.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onCheck(getAdapterPosition(), isChecked));
        fl_avh_check.setOnClickListener(v -> {
            boolean isCheck = !cb_avh.isChecked();
            cb_avh.setChecked(isCheck);
            listener.onCheck(getAdapterPosition(), isCheck);
        });
    }
}
