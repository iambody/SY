package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * Created by fei on 2017/8/10.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardListViewHolder> {

    private List<CardListEntity.CardBean> datas;
    private  Context context;
    private CardListItemClick listener;

    public CardListAdapter(List<CardListEntity.CardBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public CardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardListViewHolder(LayoutInflater.from(context).inflate(R.layout.card_collect_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(CardListViewHolder holder, int position) {
        CardListEntity.CardBean cardBean = datas.get(position);
        holder.titleTv.setText(cardBean.getName());
        String credentialsState = cardBean.getStateName();
        if (!TextUtils.isEmpty(credentialsState) && credentialsState.equals("已通过")) {
            holder.titleValueTv.setText(cardBean.getNumber());
        } else if (!TextUtils.isEmpty(credentialsState)) {
            holder.titleValueTv.setText(credentialsState);
        }
        if (null != listener) {
            holder.all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(position,cardBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class CardListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView titleValueTv;
        RelativeLayout all;

        public CardListViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.tv_card_item_title);
            titleValueTv = (TextView) itemView.findViewById(R.id.tv_card_item_value);
            all = (RelativeLayout) itemView.findViewById(R.id.rl_card_list_all);
        }
    }
    public void setItemClickListener(CardListItemClick listener){
        this.listener=listener;
    }
    public interface CardListItemClick{
        void itemClick(int position, CardListEntity.CardBean cardBean);
    }
}
