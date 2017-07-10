package app.mall.com.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.ElegantGoodsBeanInterface;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.string.MySpannableString;

import java.util.ArrayList;
import java.util.List;

import qcloud.mall.R;


/**
 * Created by sunfei on 2017/7/1 0001.
 */

public class ElegantGoodsMultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private List<ElegantGoodsBeanInterface> datas;
    private OnGoodsClickListener goodsClickListener;

    public ElegantGoodsMultAdapter(Context context,List<ElegantGoodsBeanInterface> datas) {
        this.datas = datas;
        this.context=context;
    }

    /**
     * 适配器更新数据
     * @param data 数据
     * @param isRef 是否是下拉刷新的数据 true 是   false 不是
     */
    public void addDatas(List<ElegantGoodsBeanInterface> data,boolean isRef){
        if (null == datas) {
            datas = new ArrayList<>();
        }
        if (isRef) {
            datas.clear();
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {//标题
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elegant_goods_title_item, parent, false);
            return new TitleHolder(view);
        } else if (viewType == 1) {//热门清单
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elegant_goods_product_item, parent, false);
            return new HotItemHolder(view);
        } else {//全部清单
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elegant_goods_product_item, parent, false);
            return new NormalItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ElegantGoodsBeanInterface elegantGoodsBeanInterface = datas.get(position);
        if (holder instanceof TitleHolder) {
            ElegantGoodsEntity.ElegantGoodsTitleBean bean= (ElegantGoodsEntity.ElegantGoodsTitleBean) elegantGoodsBeanInterface;
            ((TitleHolder)holder).titleTv.setText(bean.getName());
        } else if (holder instanceof HotItemHolder) {
            HotItemHolder hotHolder = (HotItemHolder) holder;
            ElegantGoodsEntity.HotListItemBean bean = (ElegantGoodsEntity.HotListItemBean) elegantGoodsBeanInterface;
            String imgUrl = bean.getImageUrl();
            Imageload.display(context,imgUrl,hotHolder.imgIv);
//            hotHolder.productNameTv.setText(bean.getGoodsName());
            String goodsName = bean.getGoodsName();
            if (!TextUtils.isEmpty(goodsName) && goodsName.contains("【") && goodsName.contains("】")) {
                MySpannableString.setNewStringStyle1(hotHolder.productNameTv, goodsName, goodsName.indexOf("【"), goodsName.indexOf("】")+1, 1,context.getResources().getColor(R.color.app_golden));
            } else {
                hotHolder.productNameTv.setText(goodsName);
            }
            hotHolder.ydNumTv.setText(String.valueOf(bean.getYdQuantity()));
            if (goodsClickListener != null) {
                hotHolder.layoutAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goodsClickListener.onClick(bean,true);
                    }
                });
            }
        } else {
            NormalItemHolder normalHolder = (NormalItemHolder) holder;
            ElegantGoodsEntity.AllNewsItemBean bean = (ElegantGoodsEntity.AllNewsItemBean) elegantGoodsBeanInterface;
            String imgUrl = bean.getImageUrl();
            Imageload.display(context,imgUrl,normalHolder.imgIv);
            String goodsName = bean.getGoodsName();
            if (!TextUtils.isEmpty(goodsName) && goodsName.contains("【") && goodsName.contains("】")) {
                MySpannableString.setNewStringStyle1(normalHolder.productNameTv, goodsName, goodsName.indexOf("【"), goodsName.indexOf("】")+1, 1, context.getResources().getColor(R.color.app_golden));
            } else {
                normalHolder.productNameTv.setText(goodsName);
            }
            normalHolder.ydNumTv.setText(String.valueOf(bean.getYdQuantity()));
            if (goodsClickListener != null) {
                normalHolder.layoutAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goodsClickListener.onClick(bean,false);
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
        ElegantGoodsBeanInterface elegantGoodsBeanInterface = datas.get(position);
        return elegantGoodsBeanInterface.getCustomItemType();
    }

    public void clean() {
        datas.clear();
        notifyDataSetChanged();
    }

    class TitleHolder extends RecyclerView.ViewHolder{

        private final TextView titleTv;

        public TitleHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_title);
        }
    }
    class HotItemHolder extends RecyclerView.ViewHolder{

        private final ImageView imgIv;
        private final TextView productNameTv;
        private final TextView ydNumTv;
        private final TextView ydStrTv;
        private final LinearLayout layoutAll;

        public HotItemHolder(View itemView) {
            super(itemView);
            layoutAll = (LinearLayout) itemView.findViewById(R.id.ll_elegant_goods_item_all);
            imgIv = (ImageView) itemView.findViewById(R.id.iv_recycler_goods_item);
            productNameTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_product_name);
            ydNumTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_yd_num);
            ydStrTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_yd_str);
        }
    }
    class NormalItemHolder extends RecyclerView.ViewHolder{

        private final ImageView imgIv;
        private final TextView productNameTv;
        private final TextView ydNumTv;
        private final TextView ydStrTv;
        private final LinearLayout layoutAll;
        public NormalItemHolder(View itemView) {
            super(itemView);
            layoutAll = (LinearLayout) itemView.findViewById(R.id.ll_elegant_goods_item_all);
            imgIv = (ImageView) itemView.findViewById(R.id.iv_recycler_goods_item);
            productNameTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_product_name);
            ydNumTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_yd_num);
            ydStrTv = (TextView) itemView.findViewById(R.id.tv_elegant_goods_yd_str);
        }
    }
    public interface OnGoodsClickListener{
        void onClick(ElegantGoodsBeanInterface bean,boolean isHot);
    }
    public void setOnGoodsClickListener(OnGoodsClickListener listener){
        this.goodsClickListener=listener;
    }
}
