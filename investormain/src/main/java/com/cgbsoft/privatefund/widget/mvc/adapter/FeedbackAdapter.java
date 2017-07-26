package com.cgbsoft.privatefund.widget.mvc.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cgbsoft.lib.listener.listener.FeedbackListener;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.widget.mvc.holder.FeedbackHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/11/14 14:28
 * Email:zhangxyfs@126.com
 */
public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackHolder> {
    private FeedbackListener feedbackListener;
    private List<String> dataList;

    public FeedbackAdapter(FeedbackListener listener,List<String> datas) {
        feedbackListener = listener;
        dataList=datas;
//        dataList = new ArrayList<>();
//        dataList.add("+");
    }

    public void addPic(String picPath) {
        if (dataList.size() > 0) {
            dataList.remove(dataList.size() - 1);
            dataList.add(picPath);
            dataList.add("+");
            notifyDataSetChanged();
        }
    }

    public void removeOne(int picPos) {
        if (picPos > -1 && dataList.size() > 0) {
            dataList.remove(picPos);
            notifyItemRemoved(picPos);
        }
    }

    public List<String> getList() {
        return dataList;
    }

    @Override
    public FeedbackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedbackHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feedback,parent,false));
    }

    @Override
    public void onBindViewHolder(FeedbackHolder holder, final int position) {
        final String value = dataList.get(position);
        if (TextUtils.equals("+", value)) {
//            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(holder.context).load(R.drawable.ic_asset_prove_upload).into(holder.imageView);
        } else
//            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(holder.context).load(new File(value)).into(holder.imageView);

        holder.imageView.setOnClickListener(view -> {
            if (feedbackListener != null) {
                feedbackListener.picClickListener(position, value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
