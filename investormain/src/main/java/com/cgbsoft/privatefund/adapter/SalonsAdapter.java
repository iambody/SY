package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunfe on 2017/7/13 0013.
 */

public class SalonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<SalonsEntity.SalonItemBean> datas;
    private OnItemClickListener listener;

    public SalonsAdapter(Context context, List<SalonsEntity.SalonItemBean> data) {
        this.context=context;
        this.datas=data;
    }
//    public void setDatas(List<SalonsEntity.SalonItemBean> data){
//        if (null == data || data.size() == 0) {
//            return;
//        }
//        if (null == datas) {
//            datas = new ArrayList<>();
//        }
//        datas.clear();
//        datas.addAll(data);
//        notifyDataSetChanged();
//    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new SalonsButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.salon_item_button,parent,false));
        } else {
            return new SalonsViewHodler(LayoutInflater.from(context).inflate(R.layout.salon_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SalonsEntity.SalonItemBean salonItemBean = datas.get(position);
        if (holder instanceof SalonsViewHodler) {
            SalonsViewHodler salonHolder= (SalonsViewHodler) holder;
            Imageload.display(context,salonItemBean.getImageUrl(),salonHolder.salonBg);
            salonHolder.salonAddress.setText(salonItemBean.getCity());
            salonHolder.salonDate.setText(salonItemBean.getStartTime());
            salonHolder.salonTitle.setText(salonItemBean.getTitle());
            salonHolder.salonDepict.setText("主讲嘉宾：".concat(salonItemBean.getSpeaker()));
            if (null != listener) {
                salonHolder.layoutAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = salonHolder.getLayoutPosition();
                        listener.onItemClick(layoutPosition,salonItemBean);
                    }
                });
            }
        } else {
            SalonsButtonViewHolder buttonHolder = (SalonsButtonViewHolder) holder;
            Imageload.display(context,R.drawable.salons_button_bg,buttonHolder.salonBg);
            buttonHolder.salonTitle.setText(context.getResources().getString(R.string.oldsalons_activities));
            if (null != listener) {
                buttonHolder.layoutAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = buttonHolder.getLayoutPosition();
                        listener.onItemClick(layoutPosition,salonItemBean);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        SalonsEntity.SalonItemBean salonItemBean = datas.get(position);
        String isButton = salonItemBean.getIsButton();
        if (TextUtils.isEmpty(isButton)) {
            return 0;
        }
        int i = Integer.parseInt(isButton);
        return i;
    }

    class SalonsViewHodler extends RecyclerView.ViewHolder{

        ImageView salonBg;
        TextView salonAddress;
        TextView salonDate;
        TextView salonTime;
        TextView salonTitle;
        TextView salonDepict;
        RelativeLayout salonLiveFlag;
        FrameLayout layoutAll;

        public SalonsViewHodler(View itemView) {
            super(itemView);
            layoutAll = (FrameLayout) itemView.findViewById(R.id.al_salon_all);
            salonBg = (ImageView) itemView.findViewById(R.id.iv_salon_bg);
            salonAddress = (TextView) itemView.findViewById(R.id.tv_salon_address);
            salonDate = (TextView) itemView.findViewById(R.id.tv_salon_date);
            salonTime = (TextView) itemView.findViewById(R.id.tv_salon_time);
            salonTitle = (TextView) itemView.findViewById(R.id.tv_salon_title);
            salonDepict = (TextView) itemView.findViewById(R.id.tv_salon_depict);
            salonLiveFlag = (RelativeLayout) itemView.findViewById(R.id.rl_salon_live_flag);
        }
    }
    class SalonsButtonViewHolder extends RecyclerView.ViewHolder{

        ImageView salonBg;
        TextView salonTitle;
        FrameLayout layoutAll;
        public SalonsButtonViewHolder(View itemView) {
            super(itemView);
            layoutAll = (FrameLayout) itemView.findViewById(R.id.al_salon_all);
            salonBg = (ImageView) itemView.findViewById(R.id.iv_salon_bg);
            salonTitle = (TextView) itemView.findViewById(R.id.tv_salon_title);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position, SalonsEntity.SalonItemBean bean);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
