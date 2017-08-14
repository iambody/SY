package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * Created by fei on 2017/8/11.
 */

public class IndentityAdapter extends RecyclerView.Adapter<IndentityAdapter.IndentityViewHolder> {

    private Context context;
    private List<IndentityEntity.IndentityItem> datas;
    private int currentPosition = -1;
    private OnMyItemClickListener listener;

    public IndentityAdapter(Context context, List<IndentityEntity.IndentityItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public IndentityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndentityViewHolder(LayoutInflater.from(context).inflate(com.cgbsoft.lib.R.layout.select_indentity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(IndentityViewHolder holder, int position) {
        IndentityEntity.IndentityItem indentityItem = datas.get(position);
        holder.titleTv.setText(indentityItem.getTitle());
        if (currentPosition == position ) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        if (null != listener) {
            holder.all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    notifyDataSetChanged();
                    listener.click(position, currentPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class IndentityViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        CheckBox checkBox;
        RelativeLayout all;

        public IndentityViewHolder(View itemView) {
            super(itemView);
            all = (RelativeLayout) itemView.findViewById(R.id.rl_indentity_all);
            titleTv = (TextView) itemView.findViewById(R.id.tv_indentity_item_title);
            checkBox = (CheckBox) itemView.findViewById(R.id.iv_indentity_item_right_arrow_gender);
        }
    }

    public void setDefaultCheckPosition() {
        currentPosition = -1;
    }

    public void setCheckPosition(int pos) {
        currentPosition = pos;
    }

    public void setOnItemClickListener(OnMyItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnMyItemClickListener {
        void click(int position, int currentPosLeft);
    }
}
