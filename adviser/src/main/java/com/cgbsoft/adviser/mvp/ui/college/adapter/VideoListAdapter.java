package com.cgbsoft.adviser.mvp.ui.college.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cgbsoft.adviser.R;
import com.cgbsoft.adviser.mvp.ui.college.holder.VideoListHolder;
import com.cgbsoft.adviser.mvp.ui.college.listener.VideoListListener;
import com.cgbsoft.adviser.mvp.ui.college.model.VideoListModel;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

/**
 * Created by xiaoyu.zhang on 2016/12/7 15:04
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoListAdapter extends BaseAdapter<VideoListModel, VideoListListener, RecyclerView.ViewHolder> {

    public VideoListAdapter(VideoListListener listener) {
        super(listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VideoListModel.BOTTOM) {
            return new VideoListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, null), listener);
        }
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoListModel model = list.get(position);
        if (model.type == VideoListModel.BOTTOM) {
            VideoListHolder vlh = (VideoListHolder) holder;
            Imageload.display(vlh.context, model.leftImgUrl, 0, 0, 8, vlh.iv_ivl_img, R.drawable.bg_default, R.drawable.bg_default);
            vlh.tv_ivl_title.setText(model.title);
            vlh.tv_ivl_heart.setText(String.valueOf(model.heartNum));
            vlh.tv_ivl_content.setText(model.content);
            vlh.tv_ivl_time.setText(model.timeStr);
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
