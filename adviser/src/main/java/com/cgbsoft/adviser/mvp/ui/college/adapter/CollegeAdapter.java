package com.cgbsoft.adviser.mvp.ui.college.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.adviser.R;
import com.cgbsoft.adviser.mvp.ui.college.holder.CollegeGridHolder;
import com.cgbsoft.adviser.mvp.ui.college.holder.CollegeHeadHolder;
import com.cgbsoft.adviser.mvp.ui.college.holder.CollegeTitleHolder;
import com.cgbsoft.adviser.mvp.ui.college.listener.CollegeListener;
import com.cgbsoft.adviser.mvp.ui.college.model.CollegeModel;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

/**
 * Created by xiaoyu.zhang on 2016/12/1 10:58
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeAdapter extends BaseAdapter<CollegeModel, CollegeListener, RecyclerView.ViewHolder> {

    public CollegeAdapter(CollegeListener listener) {
        super(listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int max = Utils.convertDipOrPx(parent.getContext(), 8);
        switch (viewType) {
            case CollegeModel.HEAD:
                return new CollegeHeadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_head, null), listener);
            case CollegeModel.COMM_HEAD:
                return new CollegeTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_title, null), listener);
            case CollegeModel.COMM:
                return new CollegeGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_bottom, null), max, listener);
            case CollegeModel.OHTER_HEAD:
                return new CollegeTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_title, null), listener);
            case CollegeModel.OTHER:
                return new CollegeGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_bottom, null), max, listener);
            default:
                return onCreateErrorViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollegeModel model = list.get(position);

        switch (model.type) {
            case CollegeModel.HEAD:
                CollegeHeadHolder chh = (CollegeHeadHolder) holder;
                Imageload.display(chh.context, model.headBgUrl, 0, 0, 0, chh.iv_ich_bg, null, null);
                chh.tv_ich_content.setText(model.headBgContent);
                break;
            case CollegeModel.COMM_HEAD:
                CollegeTitleHolder ch = (CollegeTitleHolder) holder;
                ch.tv_ict_title.setText("推荐视频");
                ch.iv_ict_arrow.setVisibility(View.GONE);
                break;
            case CollegeModel.COMM:
                CollegeGridHolder cgh = (CollegeGridHolder) holder;
                if (!model.isVisable) {
                    cgh.ll_icb.setVisibility(View.GONE);
                } else {
                    Imageload.display(cgh.context, model.bottomVideoImgUrl, 0, 0, 8, cgh.iv_icb_bg, null, null);
                    cgh.tv_icb_title.setText(model.bottomVideoTitle);
                    cgh.tv_icb_content.setText(model.bottomVideoTitle);
                }
                break;
            case CollegeModel.OHTER_HEAD:
                CollegeTitleHolder oh = (CollegeTitleHolder) holder;
                oh.tv_ict_title.setText("其他视频");
                oh.iv_ict_arrow.setVisibility(View.VISIBLE);
                break;
            case CollegeModel.OTHER:
                CollegeGridHolder co = (CollegeGridHolder) holder;
                Imageload.display(co.context, model.bottomVideoImgUrl, 0, 0, 8, co.iv_icb_bg, null, null);
                co.tv_icb_title.setText(model.bottomVideoTitle);
                co.tv_icb_content.setText(model.bottomVideoTitle);
                break;
            default:
                bindErrorHolder(model, holder);
                break;
        }
    }
}
