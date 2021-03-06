package app.privatefund.com.vido.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.Dates;
import com.cgbsoft.privatefund.bean.video.VideoAllModel;

import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-15:24
 */
public class VideoListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<VideoAllModel.VideoListModel> listModelList;
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<VideoAllModel.VideoListModel> listModelListItemClickListener;


    /**
     * 设置回调
     *
     * @param
     * @param
     */
    public void setOnItemClickListener(ListItemClickListener<VideoAllModel.VideoListModel> listener) {
        this.listModelListItemClickListener = listener;
    }

    public VideoListAdapter(List<VideoAllModel.VideoListModel> listModelList, Context apContext) {
        this.listModelList = listModelList;
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
    }

    public void freshAp(List<VideoAllModel.VideoListModel> listModelListdata) {
        this.listModelList = listModelListdata;
        this.notifyDataSetChanged();
    }
    public void addFreshAp(List<VideoAllModel.VideoListModel> listModelListdata) {
        this.listModelList.addAll(listModelListdata);
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = layoutInflater.inflate(R.layout.item_fragment_videoschool, null);
        View view = layoutInflater.inflate(R.layout.item_fragment_videoschool_new, null);
        LsViewHolder lsViewHolder = new LsViewHolder(view);
        view.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        VideoAllModel.VideoListModel videoListModel = listModelList.get(position);
        Imageload.display(ApContext, videoListModel.coverImageUrl, lsViewHolder.item_fragment_videoschool_new_image);
        BStrUtils.SetTxt(lsViewHolder.item_fragment_videoschool_new_looknumber, String.format("%s观看", videoListModel.playCount));
        BStrUtils.SetTxt(lsViewHolder.item_fragment_videoschool_new_title, videoListModel.videoName);
        BStrUtils.SetTxt(lsViewHolder.item_fragment_videoschool_new_time, Dates.videoChange(videoListModel.createTime));
    }

    @Override
    public int getItemCount() {
        return null == listModelList ? 0 : listModelList.size();
    }

    @Override
    public void onClick(View v) {
        listModelListItemClickListener.onItemClick((int) v.getTag(), listModelList.get((int) v.getTag()));
    }

//    static class LsViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R2.id.item_fragment_videoschool_image_bg)
//        ImageView Item_fragment_videoschool_image_bg;
//        @BindView(R2.id.item_fragment_videoschool_title)
//        TextView Item_fragment_videoschool_title;
//        @BindView(R2.id.item_fragment_videoschool_time)
//        TextView Item_fragment_videoschool_time;
//        @BindView(R2.id.item_fragment_videoschool_readnum)
//        TextView Item_fragment_videoschool_readnum;
//
//        LsViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
static class LsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R2.id.item_fragment_videoschool_new_image)
    ImageView item_fragment_videoschool_new_image;
    @BindView(R2.id.item_fragment_videoschool_new_title)
    TextView item_fragment_videoschool_new_title;
    @BindView(R2.id.item_fragment_videoschool_new_time)
    TextView item_fragment_videoschool_new_time;
    @BindView(R2.id.item_fragment_videoschool_new_looknumber)
    TextView item_fragment_videoschool_new_looknumber;
    @BindView(R2.id.item_fragment_videoschool_new_plaly)
    ImageView item_fragment_videoschool_new_plaly;
    LsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}


}
