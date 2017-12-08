package app.privatefund.investor.health.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingHealthDataStatistics;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.model.HealthProjectListEntity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenlong
 */
public class HealthSummaryAdapter extends RecyclerView.Adapter implements OnClickListener {

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
        lsViewHolder.projectImage.setOnClickListener(this);
//        lsViewHolder.projectTitle.setOnClickListener(this);
//        lsViewHolder.projectSubTitle.setOnClickListener(this);
        lsViewHolder.customTextLayout.setOnClickListener(this);
        lsViewHolder.customCommenMore.setOnClickListener(this);
        lsViewHolder.customCommentView.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        lsViewHolder.projectImage.setTag(R.id.bind_summary_image, position);
//        lsViewHolder.projectTitle.setTag(R.id.bind_summary_title, position);
//        lsViewHolder.projectSubTitle.setTag(R.id.bind_summary_subtitle, position);
        lsViewHolder.customTextLayout.setTag(R.id.bind_summary_title, position);
        lsViewHolder.customCommenMore.setTag(R.id.bind_summary_more, position);
        lsViewHolder.customCommentView.setTag(R.id.bind_summary_evluate, position);
        HealthProjectListEntity.HealthProjectItemEntity healthListModel = listModelListdata.get(position);
        Imageload.display(ApContext, healthListModel.getImageUrl(), 0, 0, 1, lsViewHolder.projectImage, null, null);
        BStrUtils.SetTxt1(lsViewHolder.projectTitle, healthListModel.getTitle());
        BStrUtils.SetTxt1(lsViewHolder.projectSubTitle, healthListModel.getSubtitle());
        BStrUtils.SetTxt1(lsViewHolder.effectPosition, healthListModel.getEffectPosition());
        BStrUtils.SetTxt1(lsViewHolder.fitSymptomView, healthListModel.getFitSymptom());
        BStrUtils.SetTxt1(lsViewHolder.fitCrowdView, healthListModel.getFitCrowd());
        if (TextUtils.isEmpty(healthListModel.getHeadImage())) {
            lsViewHolder.customImage.setImageResource(R.drawable.custom_default_head);
        } else {
            Imageload.display(ApContext, healthListModel.getHeadImage(), 0, 0, 1, lsViewHolder.customImage, R.drawable.custom_default_head, null);
        }
        BStrUtils.SetTxt1(lsViewHolder.customFromPlatformView, healthListModel.getUserNickName());
        BStrUtils.SetTxt1(lsViewHolder.customCommentView, TextUtils.isEmpty(healthListModel.getJudgment()) ? "当前暂无数据" : healthListModel.getJudgment());
        lsViewHolder.effectPositionLayout.setVisibility(TextUtils.isEmpty(healthListModel.getEffectPosition()) ? View.GONE : View.VISIBLE);
        lsViewHolder.fitSymptomLayout.setVisibility(TextUtils.isEmpty(healthListModel.getFitSymptom()) ? View.GONE : View.VISIBLE);
        lsViewHolder.fitCrowdLayout.setVisibility(TextUtils.isEmpty(healthListModel.getFitCrowd()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return null == listModelListdata ? 0 : listModelListdata.size();
    }

    @Override
    public void onClick(View v) {
        int index;
        HealthProjectListEntity.HealthProjectItemEntity healthProjectItemEntity;
        if (v.getId() == R.id.item_project_image_id) {
            index = (int)v.getTag(R.id.bind_summary_image);
            healthProjectItemEntity = listModelListdata.get(index);
            TrackingHealthDataStatistics.projectListItemImage(ApContext, healthProjectItemEntity.getTitle());
        } else if (v.getId() == R.id.custom_item_text_ll) {
            index = (int)v.getTag(R.id.bind_summary_title);
            healthProjectItemEntity = listModelListdata.get(index);
            TrackingHealthDataStatistics.projectListItemText(ApContext, healthProjectItemEntity.getTitle());
//        } else if (v.getId() == R.id.health_subTitle_id) {
//            index = (int)v.getTag(R.id.bind_summary_subtitle);
//            healthProjectItemEntity = listModelListdata.get(index);
//            TrackingHealthDataStatistics.projectListItemText(ApContext, healthProjectItemEntity.getSubtitle());
        } else if (v.getId() == R.id.customComment_more) {
            index = (int)v.getTag(R.id.bind_summary_more);
            healthProjectItemEntity = listModelListdata.get(index);
            HashMap<String ,Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.RIGHT_SHARE, true);
            hashMap.put(WebViewConstant.push_message_title, healthProjectItemEntity.getTitle());
            hashMap.put(WebViewConstant.push_message_url, Utils.appendWebViewUrl(healthProjectItemEntity.getUrl()).concat("?healthId=").concat(healthProjectItemEntity.getId()).concat("&healthImg=")
                    .concat(healthProjectItemEntity.getImageUrl()).concat("&healthTitle=").concat(healthProjectItemEntity.getTitle()).concat("&goCustomFeedBack=1"));
            NavigationUtils.startActivityByRouter(ApContext, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
            TrackingHealthDataStatistics.projectListItemEvaluateMore(ApContext, healthProjectItemEntity.getTitle());
            return;
        } else if (v.getId() == R.id.customComment) {
            index = (int)v.getTag(R.id.bind_summary_evluate);
            healthProjectItemEntity = listModelListdata.get(index);
            TrackingHealthDataStatistics.projectListItemEvaluate(ApContext, healthProjectItemEntity.getTitle());
        } else {
            index = (int)v.getTag();
            healthProjectItemEntity = listModelListdata.get(index);
        }
        listModelListItemClickListener.onItemClick(index, healthProjectItemEntity);
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

        @BindView(R2.id.custom_comment_ll)
        public LinearLayout customCommentll;

        @BindView(R2.id.customComment_more)
        public LinearLayout customCommenMore;

        @BindView(R2.id.custom_image_id)
        public RoundImageView customImage;

        @BindView(R2.id.customFromPlatform)
        public TextView customFromPlatformView;

        @BindView(R2.id.customComment)
        public TextView customCommentView;

        @BindView(R2.id.effectPosition_ll)
        public LinearLayout effectPositionLayout;

        @BindView(R2.id.custom_item_text_ll)
        public LinearLayout customTextLayout;

        @BindView(R2.id.fitSymptom_ll)
        public LinearLayout fitSymptomLayout;

        @BindView(R2.id.fitCrowd_ll)
        public LinearLayout fitCrowdLayout;

        LsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
