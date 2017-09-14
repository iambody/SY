package app.privatefund.investor.health.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.model.HealthListModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenlong
 */
public class HealthSummaryAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<HealthListModel> listModelListdata = new ArrayList<>();
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<HealthListModel> listModelListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener<HealthListModel> listener) {
        this.listModelListItemClickListener = listener;
    }

    public HealthSummaryAdapter(Context apContext, List<HealthListModel> listModelListData) {
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
        this.listModelListdata = listModelListData;
    }

    public void refrushData(List<HealthListModel> listModelListdata, boolean refrush) {
        if (refrush) {
            this.listModelListdata = listModelListdata;
        } else {
            this.listModelListdata.addAll(listModelListdata);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_health_list, null);
        LsViewHolder lsViewHolder = new LsViewHolder(view);
        view.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        HealthListModel healthListModel = listModelListdata.get(position);
        Imageload.display(ApContext, healthListModel.getImageUrl(), 0, 0, 1, lsViewHolder.imageV, R.drawable.bg_default, R.drawable.bg_default);
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

        @BindView(R2.id.image_view)
        public ImageView imageV;

        LsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
