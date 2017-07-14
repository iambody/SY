package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.MineActivitesModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenlong
 */
public class MineActivitesListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<MineActivitesModel.ActivitesItem> listModelListdata = new ArrayList<>();
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<MineActivitesModel.ActivitesItem> listModelListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener<MineActivitesModel.ActivitesItem> listener) {
        this.listModelListItemClickListener = listener;
    }

    public MineActivitesListAdapter(Context apContext) {
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
    }

    public void refrushData(List<MineActivitesModel.ActivitesItem> listModelListdata, boolean refrush) {
        if (listModelListdata.size() == 0) {
            return;
        }
        if (refrush) {
            this.listModelListdata.clear();
            this.listModelListdata = listModelListdata;
        } else {
            this.listModelListdata.addAll(listModelListdata);
        }
        this.notifyDataSetChanged();
    }

//    public void deleteAllData() {
//        if (listModelListdata.size() > 0) {
//            listModelListdata.clear();
//        }
//    }
//
//    public void refAllData(List dataList) {
//        if (dataList.size() == 0) {
//            return;
//        }
//        listModelListdata.addAll(dataList);
//        notifyDataSetChanged();
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_mine_activites, null);
        MyActivitesHolder myActivitesHolder= new MyActivitesHolder(view);
        view.setOnClickListener(this);
        return myActivitesHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyActivitesHolder lsViewHolder = (MyActivitesHolder) holder;
        lsViewHolder.itemView.setTag(position);
        MineActivitesModel.ActivitesItem activitesItem = listModelListdata.get(position);
        lsViewHolder.textViewCity.setText(activitesItem.getCity());
        lsViewHolder.textViewTitle.setText(activitesItem.getTitle());
        lsViewHolder.textViewTime.setText(activitesItem.getStartTime());
        Imageload.display(ApContext, activitesItem.getImageUrl(), lsViewHolder.imageViewLogo);
        lsViewHolder.textViewSpeaker.setText(activitesItem.getSpeaker());
        lsViewHolder.linearLayoutPrompt.setVisibility(TextUtils.isEmpty(activitesItem.getPlaybackVideoUrl()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return null == listModelListdata ? 0 : listModelListdata.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (Integer) v.getTag();
        listModelListItemClickListener.onItemClick(postion, listModelListdata.get(postion));
    }

    static class MyActivitesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mine_activity_prompt)
        LinearLayout linearLayoutPrompt;
        @BindView(R.id.mine_activites_city)
        TextView textViewCity;
        @BindView(R.id.mine_activites_title)
        TextView textViewTitle;
        @BindView(R.id.mine_activity_time)
        TextView textViewTime;
        @BindView(R.id.mine_activity_speaker)
        TextView textViewSpeaker;
        @BindView(R.id.mine_activity_image)
        ImageView imageViewLogo;
        @BindView(R.id.mine_activites_status_desc)
        TextView textViewStatusDesc;
        @BindView(R.id.mine_play_look)
        TextView textViewPlayLook;

        MyActivitesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
