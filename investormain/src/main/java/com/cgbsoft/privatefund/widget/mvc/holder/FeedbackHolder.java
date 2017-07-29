package com.cgbsoft.privatefund.widget.mvc.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;

/**
 * TODO
 * TODO Created by xiaoyu.zhang on 2016/11/14 14:28
 * TODO Email:zhangxyfs@126.com
 *  
 */
public class FeedbackHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public Context context;

    private int size;

    public FeedbackHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        context = itemView.getContext();

//        size = (Utils.getScreenWidth(context.getApplicationContext()) - Utils.convertDipOrPx(context.getApplicationContext(), 44)) / 4 - 12;
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//        lp.width = size;
//        lp.height = size;
//        lp.setMargins(4, 4, 4, 4);
//        imageView.setLayoutParams(lp);
    }
}
