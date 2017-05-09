package app.product.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.model.ProductlsBean;
import app.product.com.widget.TopRoundImage;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc  产品列表的adapter
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-10:55
 */
public class ProductlsAdapter extends RecyclerView.Adapter {
    public final int HOTPRODUCT = 1;//热门的标签
    public final int NORMALPRODUCT = 2;//正常的标签
    public final int OVERPRODUCT = 3;//已结束的标签

    private LayoutInflater layoutInflater;
    private Context acontext;
    private List<ProductlsBean> beanList;

    public ProductlsAdapter(Context acontext, List<ProductlsBean> beanList) {
        this.acontext = acontext;
        this.beanList = beanList;
        this.layoutInflater = LayoutInflater.from(acontext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case HOTPRODUCT://热门产品
                viewHolder = new HotProductHolder(layoutInflater.inflate(R.layout.product_item_productls_hot, null));

                break;
            case NORMALPRODUCT://正常标签
                viewHolder = new NormalProductHolder(layoutInflater.inflate(R.layout.product_item_productls, null));
                break;
            case OVERPRODUCT://已清算
                viewHolder = new OverProductHolder(layoutInflater.inflate(R.layout.product_item_productls_over, null));
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HOTPRODUCT://热门产品

                break;
            case NORMALPRODUCT://正常标签

                break;
            case OVERPRODUCT://已清算

                break;

        }
    }

    @Override
    public int getItemCount() {
        if(null==beanList)return 10;
        return beanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (null == beanList) return HOTPRODUCT;

        if (beanList.get(position).getIsHotProduct().equals("1")) return HOTPRODUCT;
        if (beanList.get(position).getIsHotProduct().equals("0")) return NORMALPRODUCT;
//        if () return OVERPRODUCT;
        return super.getItemViewType(position);
    }

    //热门的产品Holder
    static class HotProductHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_item_productls_hot_bg)
        TopRoundImage productItemProductlsHotBg;
        @BindView(R2.id.product_item_productls_hot_title1)
        TextView productItemProductlsHotTitle1;
        @BindView(R2.id.product_item_productls_hot_title2)
        TextView productItemProductlsHotTitle2;
        @BindView(R2.id.product_item_productls_hot_jiezhi)
        TextView productItemProductlsHotJiezhi;
        @BindView(R2.id.product_item_productls_hot_center)
        TextView productItemProductlsHotCenter;
        @BindView(R2.id.product_item_productls_hot_leijijingzhititle)
        TextView productItemProductlsHotLeijijingzhititle;
        @BindView(R2.id.product_item_productls_hot_jingzhi)
        TextView productItemProductlsHotJingzhi;
        @BindView(R2.id.product_item_productls_hot_qixian)
        TextView productItemProductlsHotQixian;
        @BindView(R2.id.product_item_productls_hot_mujicount)
        TextView productItemProductlsHotMujicount;
        @BindView(R2.id.product_item_productls_hot_wan)
        TextView productItemProductlsHotWan;

        public HotProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //普通的产品Holder
    static class NormalProductHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_item_productls_logobackground)
        ImageView productItemProductlsLogobackground;
        @BindView(R2.id.product_item_productls_title)
        TextView productItemProductlsTitle;
        @BindView(R2.id.product_item_productls_title_layout)
        RelativeLayout productItemProductlsTitleLayout;
        @BindView(R2.id.product_item_productls_inviste_biaodi)
        TextView productItemProductlsInvisteBiaodi;
        @BindView(R2.id.product_item_productls_inviste_area)
        TextView productItemProductlsInvisteArea;
        @BindView(R2.id.product_item_productls_inveister)
        LinearLayout productItemProductlsInveister;
        @BindView(R2.id.product_item_productls_leijijinzhi_text)
        TextView productItemProductlsLeijijinzhiText;
        @BindView(R2.id.product_item_productls_leijijingzhi)
        TextView productItemProductlsLeijijingzhi;
        @BindView(R2.id.product_item_productls_chanpinqixian)
        TextView productItemProductlsChanpinqixian;
        @BindView(R2.id.product_item_productls_qixian)
        TextView productItemProductlsQixian;
        @BindView(R2.id.product_item_productls_shengyuedu_text)
        TextView productItemProductlsShengyueduText;
        @BindView(R2.id.product_item_productls_shengyuedu_count)
        TextView productItemProductlsShengyueduCount;
        @BindView(R2.id.product_item_productls_wan)
        TextView productItemProductlsWan;
        @BindView(R2.id.product_item_productls_show_value)
        LinearLayout productItemProductlsShowValue;
        @BindView(R2.id.product_item_productls_description)
        TextView productItemProductlsDescription;
        @BindView(R2.id.product_item_productls_jiezhibg)
        ImageView productItemProductlsJiezhibg;
        @BindView(R2.id.product_item_productls_jiezhidate)
        TextView productItemProductlsJiezhidate;
        @BindView(R2.id.product_item_productls_qingsuan_icon)
        ImageView productItemProductlsQingsuanIcon;

        public NormalProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //已经结算的产品

    //普通的产品Holder
    static class OverProductHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.product_item_productls_over_title)
        TextView productItemProductlsOverTitle;
        @BindView(R2.id.product_item_productls_over_title_layout)
        RelativeLayout productItemProductlsOverTitleLayout;
        @BindView(R2.id.product_item_productls_over_inviste_biaodi)
        TextView productItemProductlsOverInvisteBiaodi;
        @BindView(R2.id.product_item_productls_over_inviste_area)
        TextView productItemProductlsOverInvisteArea;
        @BindView(R2.id.product_item_productls_over_inveister)
        LinearLayout productItemProductlsOverInveister;
        @BindView(R2.id.product_item_productls_over_leijijinzhi_text)
        TextView productItemProductlsOverLeijijinzhiText;
        @BindView(R2.id.product_item_productls_over_leijijingzhi)
        TextView productItemProductlsOverLeijijingzhi;
        @BindView(R2.id.product_item_productls_over_chanpinqixian)
        TextView productItemProductlsOverChanpinqixian;
        @BindView(R2.id.product_item_productls_over_qixian)
        TextView productItemProductlsOverQixian;
        @BindView(R2.id.product_item_productls_over_shengyuedu_text)
        TextView productItemProductlsOverShengyueduText;
        @BindView(R2.id.product_item_productls_over_shengyuedu_count)
        TextView productItemProductlsOverShengyueduCount;
        @BindView(R2.id.product_item_productls_over_wan)
        TextView productItemProductlsOverWan;
        @BindView(R2.id.product_item_productls_over_qingsuan_icon)
        ImageView productItemProductlsOverQingsuanIcon;

        public OverProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
