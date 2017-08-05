package app.privatefund.com.vido.mvp.ui.video.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvp.ui.video.holder.VideoDownloadListHolder;
import app.privatefund.com.vido.mvp.ui.video.listener.VideoDownloadListListener;
import app.privatefund.com.vido.mvp.ui.video.model.VideoDownloadListModel;

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
//                vdlh.ll_avd.setVisibility(View.VISIBLE);
                vdlh.cb_avd.setVisibility(View.VISIBLE);
            } else {
//                vdlh.ll_avd.setVisibility(View.GONE);
                vdlh.cb_avd.setVisibility(View.GONE);
            }

            vdlh.pb_avd.setMax(model.max);
            vdlh.pb_avd.setProgress(model.progress);
            vdlh.pb_avd.setVisibility(View.VISIBLE);
            vdlh.ll_avd_pause.setVisibility(View.VISIBLE);

            if (model.status == VideoStatus.DOWNLOADING) {
                vdlh.iv_avd_pause.setImageResource(R.drawable.ic_cache_d);
                vdlh.tv_avd_pause.setText(R.string.caching_str);
                vdlh.tv_avd_speed.setVisibility(View.VISIBLE);
            } else if (model.status == VideoStatus.WAIT) {//等待中
                vdlh.iv_avd_pause.setImageResource(R.drawable.i_cache_wait);
                vdlh.tv_avd_pause.setText("等待中");
                vdlh.tv_avd_speed.setVisibility(View.VISIBLE);
                BStrUtils.SetTxt(vdlh.tv_avd_progress, "等待中");
            } else if (model.status == VideoStatus.NONE) {//未下载
                vdlh.iv_avd_pause.setImageResource(R.drawable.ic_video_download_pause);
                vdlh.tv_avd_pause.setText(R.string.paused_str);
                vdlh.tv_avd_speed.setVisibility(View.VISIBLE);
                BStrUtils.SetTxt(vdlh.tv_avd_progress, "已暂停");
            } else if (model.status == VideoStatus.FINISH) {
                vdlh.ll_avd_pause.setVisibility(View.GONE);
                vdlh.pb_avd.setVisibility(View.GONE);
                vdlh.tv_avd_speed.setVisibility(View.GONE);
                String done = "";
                BStrUtils.SetTxt(vdlh.tv_avd_progress, "已下载");
//                if (!BStrUtils.isEmpty(model.progressStr) && model.progressStr.contains("/")) {
//                    int postion = model.progressStr.indexOf("/");
//                    done = model.progressStr.substring(0, postion);
//                    if ("0.0M".equals(done) || "0M".equals(done)) {
//                        BStrUtils.SetTxt(vdlh.tv_avd_progress, done);
//                    } else {
//                        BStrUtils.SetTxt(vdlh.tv_avd_progress, done);
//                    }
//                }

            }

            vdlh.cb_avd.setChecked(model.isCheck);
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
