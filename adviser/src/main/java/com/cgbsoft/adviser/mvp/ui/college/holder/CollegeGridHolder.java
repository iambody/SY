package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * Created by xiaoyu.zhang on 2016/12/1 14:16
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeGridHolder extends BaseHolder {
    @BindView(R2.id.ll_icb)
    public LinearLayout ll_icb;

    @BindView(R2.id.iv_icb_bg)
    public ImageView iv_icb_bg;

    @BindView(R2.id.tv_icb_title)
    public TextView tv_icb_title;

    @BindView(R2.id.tv_icb_content)
    public TextView tv_icb_content;

    public CollegeGridHolder(View itemView, int max, CollegeListener listener) {
        super(itemView);
        int width = Utils.getScreenWidth(context) / 2 - Utils.convertDipOrPx(context, 16);
        int height = width * 829 / 1479;

        ViewGroup.LayoutParams lp = iv_icb_bg.getLayoutParams();
        lp.width = width;
        lp.height = height;
        iv_icb_bg.setLayoutParams(lp);

        if (getAdapterPosition() % 2 == 0) {
            ll_icb.setPadding(max, max / 2, max / 2, max / 2);
        } else {
            ll_icb.setPadding(max / 2, max / 2, max, max / 2);
        }

        ll_icb.setOnClickListener(v -> listener.onGridItemClick(getAdapterPosition()));
    }
}
