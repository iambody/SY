package app.privatefund.com.vido.mvp.ui.video.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.privatefund.com.vido.R;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

import app.privatefund.com.vido.mvp.ui.video.holder.VideoHistoryHolder;
import app.privatefund.com.vido.mvp.ui.video.listener.VideoHistoryListener;
import app.privatefund.com.vido.mvp.ui.video.model.VideoHistoryModel;

/**
 * Created by xiaoyu.zhang on 2016/12/12 18:14
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoHistoryAdapter extends BaseAdapter<VideoHistoryModel, VideoHistoryListener, RecyclerView.ViewHolder> {
    private boolean openCheck;

    public VideoHistoryAdapter(VideoHistoryListener listener) {
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
        if (viewType == VideoHistoryModel.LIST)
            return new VideoHistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_history, null), listener);
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoHistoryModel model = list.get(position);

        if (holder instanceof VideoHistoryHolder) {
            VideoHistoryHolder vhh = (VideoHistoryHolder) holder;
            Imageload.display(vhh.context, model.videoCoverUrl, 0, 0, 8, vhh.iv_avh_cover, R.drawable.bg_default, R.drawable.bg_default);
            vhh.tv_avh_title.setText(model.videoTitle);
            vhh.tv_avh_time.setText(model.time);

            if (openCheck) {
                vhh.cb_avh.setVisibility(View.VISIBLE);
            } else {
                vhh.cb_avh.setVisibility(View.GONE);
            }

            vhh.cb_avh.setChecked(model.isCheck);
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
