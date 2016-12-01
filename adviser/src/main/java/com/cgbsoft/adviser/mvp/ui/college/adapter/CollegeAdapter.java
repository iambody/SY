package com.cgbsoft.adviser.mvp.ui.college.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
                return new CollegeTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_title, null));
            case CollegeModel.COMM:
                return new CollegeGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_bottom, null), max);
            case CollegeModel.OHTER_HEAD:
                return new CollegeTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_title, null));
            case CollegeModel.OTHER:
                return new CollegeGridHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college_bottom, null), max);
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
                Imageload.display(chh.context, model.headBgUrl, 4, chh.iv_ich_bg, null, null);
                chh.tv_ich_content.setText(model.headBgContent);
                break;
            case CollegeModel.COMM_HEAD:
                CollegeTitleHolder ch = (CollegeTitleHolder) holder;
                ch.tv_ict_title.setText("推荐视频");
                break;
            case CollegeModel.COMM:

                break;
            case CollegeModel.OHTER_HEAD:
                CollegeTitleHolder oh = (CollegeTitleHolder) holder;
                oh.tv_ict_title.setText("其他视频");
                break;
            case CollegeModel.OTHER:

                break;
            default:
                bindErrorHolder(model, holder);
                break;
        }
    }
}
