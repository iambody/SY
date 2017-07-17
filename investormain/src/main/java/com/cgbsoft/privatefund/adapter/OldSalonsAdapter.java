package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.OldSalonsEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * Created by sunfe on 2017/7/13 0013.
 */

public class OldSalonsAdapter extends RecyclerView.Adapter<OldSalonsAdapter.OldSalonsViewHodler> {

    private final Context context;
    private final List<OldSalonsEntity.SalonItemBean> datas;
    private OnItemClickListener listener;

    public OldSalonsAdapter(Context context, List<OldSalonsEntity.SalonItemBean> data) {
        this.context=context;
        this.datas=data;
    }
    @Override
    public OldSalonsViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OldSalonsViewHodler(LayoutInflater.from(context).inflate(R.layout.oldsalon_item,parent,false));
    }

    @Override
    public void onBindViewHolder(OldSalonsAdapter.OldSalonsViewHodler holder, int position) {
            OldSalonsEntity.SalonItemBean salonItemBean = datas.get(position);
        Imageload.display(context,salonItemBean.getImageUrl(),holder.salonBg);
        holder.salonAddress.setText(salonItemBean.getCity());
        holder.salonDate.setText(salonItemBean.getStartTime());
        holder.salonTitle.setText(salonItemBean.getTitle());
        holder.salonDepict.setText("主讲嘉宾：".concat(salonItemBean.getSpeaker()));
        if (null != listener) {
            holder.layoutAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    listener.onItemClick(layoutPosition,salonItemBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OldSalonsViewHodler extends RecyclerView.ViewHolder{

        ImageView salonBg;
        TextView salonAddress;
        TextView salonDate;
        TextView salonTime;
        TextView salonTitle;
        TextView salonDepict;
        FrameLayout layoutAll;

        public OldSalonsViewHodler(View itemView) {
            super(itemView);
            layoutAll = (FrameLayout) itemView.findViewById(R.id.al_oldsalon_all);
            salonBg = (ImageView) itemView.findViewById(R.id.iv_oldsalon_bg);
            salonAddress = (TextView) itemView.findViewById(R.id.tv_oldsalon_address);
            salonDate = (TextView) itemView.findViewById(R.id.tv_oldsalon_date);
            salonTime = (TextView) itemView.findViewById(R.id.tv_oldsalon_time);
            salonTitle = (TextView) itemView.findViewById(R.id.tv_oldsalon_title);
            salonDepict = (TextView) itemView.findViewById(R.id.tv_oldsalon_depict);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position, OldSalonsEntity.SalonItemBean bean);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
