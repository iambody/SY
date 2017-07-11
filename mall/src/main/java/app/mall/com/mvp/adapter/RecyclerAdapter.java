package app.mall.com.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import java.util.ArrayList;
import java.util.List;

import qcloud.mall.R;


/**
 * Created by feigecal on 2017/6/29 0029.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private final Context context;
    private List<ElegantLivingEntity.ElegantLivingBean> data;
    private OnItemClickListener listener;

    public RecyclerAdapter(Context context,ArrayList<ElegantLivingEntity.ElegantLivingBean> data) {
        this.data=data;
        this.context=context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.elegant_living_recycler_item,parent,false));
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        ElegantLivingEntity.ElegantLivingBean elegantLivingBean = data.get(position);
        Imageload.display(context,elegantLivingBean.getBanner(),holder.iv);
        if (null != listener) {
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    listener.onItemClick(holder.iv,pos,elegantLivingBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_recycler_item);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position,ElegantLivingEntity.ElegantLivingBean elegantLivingBean);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
