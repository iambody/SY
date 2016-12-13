package com.cgbsoft.lib.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.mvp.ui.holder.VideoDownloadListHolder;
import com.cgbsoft.lib.mvp.ui.listener.VideoDownloadListListener;
import com.cgbsoft.lib.mvp.ui.model.VideoDownloadListModel;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

/**
 * Created by xiaoyu.zhang on 2016/12/13 16:59
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListAdapter extends BaseAdapter<VideoDownloadListModel, VideoDownloadListListener, RecyclerView.ViewHolder> {
    private boolean openCheck;

    public VideoDownloadListAdapter(VideoDownloadListListener listener) {
        super(listener);
    }

    public void changeCheck() {
        openCheck = !openCheck;
        notifyDataSetChanged();
    }

    public boolean getCheckStatus() {
        return openCheck;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VideoDownloadListModel.LIST)
            return new VideoDownloadListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_download, null), listener);
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoDownloadListModel model = list.get(position);

        if (holder instanceof VideoDownloadListHolder) {
            VideoDownloadListHolder vdlh = (VideoDownloadListHolder) holder;
            Imageload.display(vdlh.context, model.videoCoverUrl, 0, 0, 8, vdlh.iv_avd_cover, R.drawable.bg_default, R.drawable.bg_default);
            vdlh.tv_avd_title.setText(model.videoTitle);
            vdlh.tv_avd_progress.setText(model.progressStr);
            vdlh.tv_avd_speed.setText(model.speedStr);

            if (openCheck) {
                vdlh.cb_avd.setVisibility(View.VISIBLE);
            } else {
                vdlh.cb_avd.setVisibility(View.GONE);
            }

            vdlh.cb_avd.setChecked(model.isCheck);
        }
    }
}
