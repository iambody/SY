package com.cgbsoft.lib.mvp.ui.video.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.mvp.ui.video.holder.VideoDownloadListHolder;
import com.cgbsoft.lib.mvp.ui.video.listener.VideoDownloadListListener;
import com.cgbsoft.lib.mvp.ui.video.model.VideoDownloadListModel;
import com.cgbsoft.lib.utils.constant.VideoStatus;
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
            vdlh.tv_avd_id.setText(model.videoId);

            if (openCheck) {
                vdlh.cb_avd.setVisibility(View.VISIBLE);
            } else {
                vdlh.cb_avd.setVisibility(View.GONE);
            }

            vdlh.pb_avd.setMax(model.max);
            vdlh.pb_avd.setProgress(model.progress);
            vdlh.pb_avd.setVisibility(View.VISIBLE);
            vdlh.ll_avd_pause.setVisibility(View.VISIBLE);

            if (model.status == VideoStatus.DOWNLOADING || model.status == VideoStatus.WAIT) {//正在下载
                vdlh.iv_avd_pause.setImageResource(R.drawable.ic_video_download_start);
                vdlh.tv_avd_pause.setText(R.string.caching_str);
            } else if (model.status == VideoStatus.NONE) {//未下载
                vdlh.iv_avd_pause.setImageResource(R.drawable.ic_video_download_pause);
                vdlh.tv_avd_pause.setText(R.string.paused_str);
            } else if (model.status == VideoStatus.FINISH) {
                vdlh.ll_avd_pause.setVisibility(View.GONE);
                vdlh.pb_avd.setVisibility(View.GONE);
            }

            vdlh.cb_avd.setChecked(model.isCheck);
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
