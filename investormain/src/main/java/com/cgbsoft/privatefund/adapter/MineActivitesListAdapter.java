package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.DiscoveryListModel;
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
            listModelListdata.clear();
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
        View view = layoutInflater.inflate(R.layout.item_fragment_videoschool, null);
        LsViewHolder lsViewHolder = new LsViewHolder(view);
        view.setOnClickListener(this);
        return lsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LsViewHolder lsViewHolder = (LsViewHolder) holder;
        lsViewHolder.itemView.setTag(position);
        MineActivitesModel.ActivitesItem activitesItem = listModelListdata.get(position);
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
        @BindView(R.id.item_fragment_videoschool_image_bg)
        ImageView Item_fragment_videoschool_image_bg;
        @BindView(R.id.item_fragment_videoschool_title)
        TextView Item_fragment_videoschool_title;
        @BindView(R.id.item_fragment_videoschool_time)
        TextView Item_fragment_videoschool_time;
        @BindView(R.id.item_fragment_videoschool_readnum)
        TextView Item_fragment_videoschool_readnum;

        LsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
