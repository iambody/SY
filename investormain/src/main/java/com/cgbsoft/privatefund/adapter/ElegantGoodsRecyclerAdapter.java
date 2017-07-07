package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.mvp.ui.MallEditAddressActivity;

/**
 * 尚品顶部分类适配器
 * Created by sunfei on 2017/6/30 0030.
 */

public class ElegantGoodsRecyclerAdapter extends RecyclerView.Adapter<ElegantGoodsRecyclerAdapter.RecyclerViewHolder> {
    private List<ElegantGoodsEntity.ElegantGoodsCategoryBean> data;
    private Context context;
    private CategoryItemClickListener listener;
    private int oldCheckPos=-1;

    public ElegantGoodsRecyclerAdapter(Context context, List<ElegantGoodsEntity.ElegantGoodsCategoryBean> data) {
        this.data = data;
        this.context = context;
    }
    public void setDatas(List<ElegantGoodsEntity.ElegantGoodsCategoryBean> datas){
        if (null == data) {
            data = new ArrayList<>();
        }
        data.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.elegant_goods_recycler_item,
                parent, false));
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ElegantGoodsRecyclerAdapter.RecyclerViewHolder holder, int position) {
        ElegantGoodsEntity.ElegantGoodsCategoryBean elegantGoodsCategoryBean = data.get(position);
        holder.name.setText(elegantGoodsCategoryBean.getTitle());
        if (elegantGoodsCategoryBean.getIsCheck() == 1) {
            this.oldCheckPos=position;
            holder.name.setBackground(context.getResources().getDrawable(R.drawable.elegant_category_selected));
        } else {
            holder.name.setBackground(context.getResources().getDrawable(R.drawable.elegant_category_normal));
        }
        if (null != listener) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    if (pos == oldCheckPos) {
                        return;
                    }
                    holder.name.setBackground(context.getResources().getDrawable(R.drawable.elegant_category_selected));
                    elegantGoodsCategoryBean.setIsCheck(1);
                    data.get(oldCheckPos).setIsCheck(0);
                    notifyDataSetChanged();
                    listener.onCategoryItemClick(holder.name,oldCheckPos,pos,elegantGoodsCategoryBean);
                    oldCheckPos=pos;
                }
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
            name = (TextView) itemView.findViewById(R.id.iv_recycler_goods_item);
        }
    }
    public interface CategoryItemClickListener{
        void onCategoryItemClick(View view,int oldPosition,int position,ElegantGoodsEntity.ElegantGoodsCategoryBean posBean);
    }
    public void setCategoryItemClickListener(CategoryItemClickListener listener){
        this.listener=listener;
    }
}
