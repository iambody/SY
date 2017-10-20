package app.privatefund.investor.health.adapter;

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
import com.cgbsoft.lib.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.model.HealthProjectListEntity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenlong
 */
public class HealthSummaryAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<HealthProjectListEntity.HealthProjectItemEntity> listModelListdata = new ArrayList<>();
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<HealthProjectListEntity.HealthProjectItemEntity> listModelListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener<HealthProjectListEntity.HealthProjectItemEntity> listener) {
        this.listModelListItemClickListener = listener;
    }

    public HealthSummaryAdapter(Context apContext, List<HealthProjectListEntity.HealthProjectItemEntity> listModelListData) {
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
        this.listModelListdata = listModelListData;
    }

    public void refrushData(List<HealthProjectListEntity.HealthProjectItemEntity> listModelListdata, boolean refrush) {
        if (refrush) {
            this.listModelListdata = listModelListdata;
        } else {
            this.listModelListdata.addAll(listModelListdata);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_health_project, parent, false);
        LsViewHolder lsViewHolder = new LsViewHolder(view);
        view.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        HealthProjectListEntity.HealthProjectItemEntity healthListModel = listModelListdata.get(position);
        Imageload.display(ApContext, healthListModel.getImageUrl(), 0, 0, 1, lsViewHolder.projectImage, null, null);
        BStrUtils.SetTxt1(lsViewHolder.projectTitle, healthListModel.getTitle());
        BStrUtils.SetTxt1(lsViewHolder.projectSubTitle, healthListModel.getSutTitle());
        BStrUtils.SetTxt1(lsViewHolder.effectPosition, healthListModel.getEffectPosition());
        BStrUtils.SetTxt1(lsViewHolder.fitSymptomView, healthListModel.getFitSymptom());
        BStrUtils.SetTxt1(lsViewHolder.fitCrowdView, healthListModel.getFitCrowd());
        Imageload.display(ApContext, healthListModel.getUserThumbnail(), 0, 0, 1, lsViewHolder.customImage, null, null);
        BStrUtils.SetTxt1(lsViewHolder.customFromPlatformView, healthListModel.getUserFrom());
        BStrUtils.SetTxt1(lsViewHolder.customCommentView, healthListModel.getComment());
    }

    @Override
    public int getItemCount() {
        return null == listModelListdata ? 0 : listModelListdata.size();
    }

    @Override
    public void onClick(View v) {
        listModelListItemClickListener.onItemClick((int) v.getTag(), listModelListdata.get((int) v.getTag()));
    }

    static class LsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.item_project_image_id)
        public ImageView projectImage;

        @BindView(R2.id.health_title_id)
        public TextView projectTitle;

        @BindView(R2.id.health_subTitle_id)
        public TextView projectSubTitle;

        @BindView(R2.id.effectPosition)
        public TextView effectPosition;

        @BindView(R2.id.fitSymptom)
        public TextView fitSymptomView;

        @BindView(R2.id.fitCrowd)
        public TextView fitCrowdView;

        @BindView(R2.id.custom_image_id)
        public RoundImageView customImage;

        @BindView(R2.id.customFromPlatform)
        public TextView customFromPlatformView;

        @BindView(R2.id.customComment)
        public TextView customCommentView;

        LsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
