package app.privatefund.investor.health.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
        BStrUtils.SetTxt1(lsViewHolder.projectSubTitle, healthListModel.getSubtitle());
        BStrUtils.SetTxt1(lsViewHolder.effectPosition, healthListModel.getEffectPosition());
        BStrUtils.SetTxt1(lsViewHolder.fitSymptomView, healthListModel.getFitSymptom());
        BStrUtils.SetTxt1(lsViewHolder.fitCrowdView, healthListModel.getFitCrowd());
        Imageload.display(ApContext, healthListModel.getHeadImage(), 0, 0, 1, lsViewHolder.customImage, R.drawable.custom_default_head, null);
        BStrUtils.SetTxt1(lsViewHolder.customFromPlatformView, healthListModel.getUserNickName());
        BStrUtils.SetTxt1(lsViewHolder.customCommentView, TextUtils.isEmpty(healthListModel.getJudgment()) ? "当前暂无数据" : healthListModel.getJudgment());
        lsViewHolder.effectPositionLayout.setVisibility(TextUtils.isEmpty(healthListModel.getEffectPosition()) ? View.GONE : View.VISIBLE);
        lsViewHolder.fitSymptomLayout.setVisibility(TextUtils.isEmpty(healthListModel.getFitSymptom()) ? View.GONE : View.VISIBLE);
        lsViewHolder.fitCrowdLayout.setVisibility(TextUtils.isEmpty(healthListModel.getFitCrowd()) ? View.GONE : View.VISIBLE);
        lsViewHolder.customCommentll.setOnClickListener(v -> {
            HashMap<String ,Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.RIGHT_SHARE, true);
            hashMap.put(WebViewConstant.push_message_title, healthListModel.getTitle());
            hashMap.put(WebViewConstant.push_message_url, Utils.appendWebViewUrl(healthListModel.getUrl()).concat("?healthId=").concat(healthListModel.getId()).concat("&healthImg=")
                    .concat(healthListModel.getImageUrl()).concat("&healthTitle=").concat(healthListModel.getTitle()).concat("&goCustomFeedBack=1"));
            NavigationUtils.startActivityByRouter(ApContext, RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
        });
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

        @BindView(R2.id.custom_comment_ll)
        public LinearLayout customCommentll;

        @BindView(R2.id.custom_image_id)
        public RoundImageView customImage;

        @BindView(R2.id.customFromPlatform)
        public TextView customFromPlatformView;

        @BindView(R2.id.customComment)
        public TextView customCommentView;

        @BindView(R2.id.effectPosition_ll)
        public LinearLayout effectPositionLayout;

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
