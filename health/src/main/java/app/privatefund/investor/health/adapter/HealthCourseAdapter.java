package app.privatefund.investor.health.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.TrackingHealthDataStatistics;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.model.HealthCourseEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author chenlong
 */
public class HealthCourseAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<HealthCourseEntity.HealthCourseListModel> listModelListdata = new ArrayList<>();
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<HealthCourseEntity.HealthCourseListModel> listModelListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener<HealthCourseEntity.HealthCourseListModel> listener) {
        this.listModelListItemClickListener = listener;
    }

    public HealthCourseAdapter(Context apContext, List<HealthCourseEntity.HealthCourseListModel> listModelListData) {
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
        this.listModelListdata = listModelListData;
    }

    public void refrushData(List<HealthCourseEntity.HealthCourseListModel> listModelListdata, boolean refrush) {
        if (refrush) {
            this.listModelListdata = listModelListdata;
        } else {
            this.listModelListdata.addAll(listModelListdata);
        }
        this.notifyDataSetChanged();
    }

    public void notifyDataReadCount(HealthCourseEntity.HealthCourseListModel healthListModel) {
        if (!CollectionUtils.isEmpty(listModelListdata)) {
            for (HealthCourseEntity.HealthCourseListModel listModel : listModelListdata) {
                if (TextUtils.equals(listModel.getId(), healthListModel.getId())) {
                    listModel.setReadCount(String.valueOf(Integer.parseInt(listModel.getReadCount()) + 1));
                }
            }
            this.notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_fragment_healthcourse, null);
        LsViewHolder lsViewHolder = new LsViewHolder(view);
        view.setOnClickListener(this);
        lsViewHolder.Item_fragment_videoschool_image_bg.setOnClickListener(this);
        lsViewHolder.Item_fragment_videoschool_title.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        lsViewHolder.Item_fragment_videoschool_image_bg.setTag(R.id.bind_course_image, position);
        lsViewHolder.Item_fragment_videoschool_title.setTag(R.id.bind_course_title, position);
        HealthCourseEntity.HealthCourseListModel healthCourseListModel = listModelListdata.get(position);
        Imageload.display(ApContext, healthCourseListModel.getThumbnailUrl(), lsViewHolder.Item_fragment_videoschool_image_bg);
        BStrUtils.SetTxt(lsViewHolder.Item_fragment_videoschool_readnum, healthCourseListModel.getReadCount().concat("阅读"));
        BStrUtils.SetTxt(lsViewHolder.Item_fragment_videoschool_title, healthCourseListModel.getTitle());
        BStrUtils.SetTxt(lsViewHolder.Item_fragment_videoschool_time, healthCourseListModel.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return null == listModelListdata ? 0 : listModelListdata.size();
    }

    @Override
    public void onClick(View v) {
        HealthCourseEntity.HealthCourseListModel itemModel;
        int position;
        if (v.getId() == R.id.item_fragment_videoschool_image_bg) {
            position = (int)v.getTag(R.id.bind_course_image);
            itemModel = listModelListdata.get(position);
            TrackingHealthDataStatistics.clickHealthCoureseItemImage(ApContext, itemModel.getTitle());
        } else if (v.getId() == R.id.item_fragment_videoschool_title) {
            position = (int)v.getTag(R.id.bind_course_title);
            itemModel = listModelListdata.get(position);
            TrackingHealthDataStatistics.clickHealthCoureseItemTitle(ApContext, itemModel.getTitle());
        } else {
            position = (int)v.getTag();
            itemModel = listModelListdata.get(position);
        }
        listModelListItemClickListener.onItemClick(position, itemModel);
    }

    static class LsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.item_fragment_videoschool_image_bg)
        ImageView Item_fragment_videoschool_image_bg;
        @BindView(R2.id.item_fragment_videoschool_title)
        TextView Item_fragment_videoschool_title;
        @BindView(R2.id.item_fragment_videoschool_time)
        TextView Item_fragment_videoschool_time;
        @BindView(R2.id.item_fragment_videoschool_readnum)
        TextView Item_fragment_videoschool_readnum;

        LsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
