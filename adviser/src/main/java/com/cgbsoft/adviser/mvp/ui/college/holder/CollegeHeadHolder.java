package com.cgbsoft.adviser.mvp.ui.college.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.adviser.R2;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.recycler.BaseHolder;

import butterknife.BindView;

/**
 * Created by xiaoyu.zhang on 2016/12/1 10:57
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeHeadHolder extends BaseHolder {

    public static final int PRODUCT = 1;
    public static final int FOREFRONT = 2;
    public static final int MANAGER = 3;
    public static final int KNOWLEDGE = 4;

    @BindView(R2.id.iv_ich_bg)
    public ImageView iv_ich_bg;

    @BindView(R2.id.tv_ich_content)
    public TextView tv_ich_content;

    @BindView(R2.id.tv_ich_product_train)
    public TextView tv_ich_product_train;

    @BindView(R2.id.tv_ich_forefront)
    public TextView tv_ich_forefront;

    @BindView(R2.id.tv_ich_manager_train)
    public TextView tv_ich_manager_train;

    @BindView(R2.id.tv_ich_wealth_knowledge)
    public TextView tv_ich_wealth_knowledge;

    public CollegeHeadHolder(View itemView, CollegeListener listener) {
        super(itemView);

        ViewGroup.LayoutParams lp = iv_ich_bg.getLayoutParams();
        lp.width = Utils.getScreenWidth(context);
        lp.height = lp.width * 9 / 16;
        iv_ich_bg.setLayoutParams(lp);

        iv_ich_bg.setOnClickListener(v -> listener.onFirstVideoClick(iv_ich_bg));
        tv_ich_product_train.setOnClickListener(v -> listener.onHeadBtnClick(PRODUCT));
        tv_ich_forefront.setOnClickListener(v -> listener.onHeadBtnClick(FOREFRONT));
        tv_ich_manager_train.setOnClickListener(v -> listener.onHeadBtnClick(MANAGER));
        tv_ich_wealth_knowledge.setOnClickListener(v -> listener.onHeadBtnClick(KNOWLEDGE));
    }
}
