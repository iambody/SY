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
import com.cgbsoft.privatefund.model.DiscoverModel;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenlong
 */
public class DiscoveryListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<DiscoverModel.DiscoveryListModel> listModelListdata = new ArrayList<>();
    private Context ApContext;
    private LayoutInflater layoutInflater;

    private ListItemClickListener<DiscoverModel.DiscoveryListModel> listModelListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener<DiscoverModel.DiscoveryListModel> listener) {
        this.listModelListItemClickListener = listener;
    }

    public DiscoveryListAdapter(Context apContext, List<DiscoverModel.DiscoveryListModel> listModelListData) {
        this.ApContext = apContext;
        this.layoutInflater = LayoutInflater.from(apContext);
        this.listModelListdata = listModelListData;
    }

    public void refrushData(List<DiscoverModel.DiscoveryListModel> listModelListdata, boolean refrush) {
        if (refrush) {
            this.listModelListdata = listModelListdata;
        } else {
            this.listModelListdata.addAll(listModelListdata);
        }
        this.notifyDataSetChanged();
    }

    public void setData(List<DiscoverModel.DiscoveryListModel> listModelListdata) {
        this.listModelListdata = listModelListdata;
    }

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
        DiscoverModel.DiscoveryListModel discoveryListModel = listModelListdata.get(position);
        Imageload.display(ApContext, discoveryListModel.getImage(), lsViewHolder.Item_fragment_videoschool_image_bg);
        BStrUtils.SetTxt(lsViewHolder.Item_fragment_videoschool_readnum, String.format("%d阅读", discoveryListModel.getViews()));
        BStrUtils.SetTxt(lsViewHolder.Item_fragment_videoschool_title, discoveryListModel.getTitle());
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
