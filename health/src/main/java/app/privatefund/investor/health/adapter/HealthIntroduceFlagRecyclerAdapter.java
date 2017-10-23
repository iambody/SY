package app.privatefund.investor.health.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.mvp.model.HealthIntroduceNavigationEntity;

/**
 * @author chenlong
 * 介绍下面标签的适配器
 */
public class HealthIntroduceFlagRecyclerAdapter extends RecyclerView.Adapter<HealthIntroduceFlagRecyclerAdapter.RecyclerViewHolder> {

    private List<HealthIntroduceNavigationEntity> data = new ArrayList<>();
    private Context context;
    private CategoryItemClickListener listener;
    private int oldCheckPos=-1;

    public HealthIntroduceFlagRecyclerAdapter(Context context) {
        this.context = context;
    }
    public void setDatas(List<HealthIntroduceNavigationEntity> datas){
        if (null == data) {
            data = new ArrayList<>();
        }
        data.clear();
        data.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.health_introduce_flag_recycler_item,
                parent, false));
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(HealthIntroduceFlagRecyclerAdapter.RecyclerViewHolder holder, int position) {
        HealthIntroduceNavigationEntity healthIntroduceCategoryBean = data.get(position);
        holder.name.setText(healthIntroduceCategoryBean.getTitle());
        if (healthIntroduceCategoryBean.getIsCheck() == 1) {
            this.oldCheckPos=position;
            holder.name.setBackground(ContextCompat.getDrawable(context, R.drawable.health_introduce_category_selected));
        } else {
            holder.name.setBackground(ContextCompat.getDrawable(context, R.drawable.health_introduce_category_normal));
        }
        if (null != listener) {
            holder.name.setOnClickListener(v -> {
                int pos = holder.getLayoutPosition();
                if (pos == oldCheckPos) {
                    return;
                }
                holder.name.setBackground(context.getResources().getDrawable(R.drawable.health_introduce_category_selected));
                healthIntroduceCategoryBean.setIsCheck(1);
                data.get(oldCheckPos).setIsCheck(0);
                notifyDataSetChanged();
                listener.onCategoryItemClick(holder.name, oldCheckPos, pos, healthIntroduceCategoryBean);
                oldCheckPos=pos;
            });
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.iv_recycler_health_item);
        }
    }

    public interface CategoryItemClickListener{
        void onCategoryItemClick(View view, int oldPosition, int position, HealthIntroduceNavigationEntity posBean);
    }

    public void setCategoryItemClickListener(CategoryItemClickListener listener){
        this.listener=listener;
    }
}
