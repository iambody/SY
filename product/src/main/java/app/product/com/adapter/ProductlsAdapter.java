package app.product.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class ProductlsAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    public final int HOTPRODUCT = 1;//热门的标签
    public final int NORMALPRODUCT = 2;//正常的标签
    public final int OVERPRODUCT = 3;//已结束的标签
    // 服务器返回的时间格式，需要转换为毫秒值，与当前时间相减得到时间差，显示到list里
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private LayoutInflater layoutInflater;
    private Context acontext;
    private List<ProductlsBean> beanList;
    private OnRecyclerItemClickListener mOnItemClickListener = null;
    public ProductlsAdapter(Context acontext, List<ProductlsBean> beanList) {
        this.acontext = acontext;
        this.beanList = beanList;
        this.layoutInflater = LayoutInflater.from(acontext);
    }

    public void freshAp(List<ProductlsBean> beanList) {
        this.beanList = beanList;
        this.notifyDataSetChanged();

    }
    public void AddfreshAp(List<ProductlsBean> beanList) {
        int count=beanList.size();
        this.beanList.addAll(beanList);
        this.notifyItemRangeChanged(count-1,beanList.size());
    }
    public List<ProductlsBean>getBeanList(){
        return beanList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case HOTPRODUCT://热门产品
                View hotView=layoutInflater.inflate(R.layout.product_item_productls_hot, null);
                viewHolder = new HotProductHolder(hotView);
                hotView.setOnClickListener(this);
                break;
            case NORMALPRODUCT://正常标签
                View normalView=layoutInflater.inflate(R.layout.product_item_productls, null);
                viewHolder = new NormalProductHolder(normalView);
                normalView.setOnClickListener(this);
                break;
            case OVERPRODUCT://已清算
                View overView=layoutInflater.inflate(R.layout.product_item_productls_over, null);
                viewHolder = new OverProductHolder(overView);
                overView.setOnClickListener(this);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductlsBean productlsBean = beanList.get(position);

        switch (getItemViewType(position)) {
            case HOTPRODUCT://热门产品

                HotProductHolder hotProductHolder = (HotProductHolder) holder;
                hotProductHolder.itemView.setTag(position);
                Imageload.display(acontext, productlsBean.marketingImageUrl, hotProductHolder.productItemProductlsHotBg);
                String pro_name = productlsBean.productName;
                if (pro_name.length() > 16) {
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotTitle1, pro_name.substring(0, 16));
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotTitle2, pro_name.substring(16));
                    hotProductHolder.productItemProductlsHotTitle2.setVisibility(View.VISIBLE);
                } else {
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotTitle1, pro_name);
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotTitle2, "");
                    hotProductHolder.productItemProductlsHotTitle2.setVisibility(View.GONE);
                }
                BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotQixian, productlsBean.term);
                BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotCenter, productlsBean.hotName);
                try {
                    if ((productlsBean.state.equals("50") || productlsBean.state.equals("80")) && productlsBean.raiseEndTime != null) {
                        java.util.Date end_time = dateFormat.parse(productlsBean.raiseEndTime);
                        long l = end_time.getTime() - System.currentTimeMillis();
                        String dateString = null;
                        int day = (int) (l / 1000 / 60 / 60 / 24);
                        int hour = (int) (l / 1000 / 60 / 60);
                        int min = (int) (l / 1000 / 60);

                        if (hour >= 72) {
                            dateString = day + "天";
                        } else if (hour > 0 && hour < 72) {
                            dateString = hour + "小时";
                        } else {
                            if (min == 0) {
                                dateString = 1 + "分钟";
                            } else {
                                dateString = min + "分钟";
                            }
                        }
                        if (l <= 0) {
                            BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotJiezhi, "已截止");
                        } else {
                            BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotJiezhi, "截止打款" + dateString);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String type_code1 = productlsBean.productType;
                if (type_code1.equals("1")) {

                    hotProductHolder.productItemProductlsHotJiezhi.setText(productlsBean.expectedYield + "%");
                    String raised_amt = String.valueOf(productlsBean.remainingAmount);
                    if (TextUtils.isEmpty(raised_amt)) {
                        raised_amt = "0";
                    }
                    raised_amt = BStrUtils.getYi(raised_amt);
                    if (raised_amt.contains("亿")) {
                        raised_amt = raised_amt.replace("亿", "");

                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, "亿");
                    } else {
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, raised_amt);
                    }
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotLeijijingzhititle, "业绩基准");

                } else if (type_code1.equals("2")) {
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotJiezhi, productlsBean.cumulativeNet);
                    String raised_amt = String.valueOf(productlsBean.remainingAmount);
                    if (TextUtils.isEmpty(raised_amt)) {
                        raised_amt = "0";
                    }
                    raised_amt = BStrUtils.getYi(raised_amt);
                    if (raised_amt.contains("亿")) {
                        raised_amt = raised_amt.replace("亿", "");
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, "亿");
                    } else {
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, "万");
                    }
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotLeijijingzhititle, "累计净值");
                } else if (type_code1.equals("3")) {
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotJingzhi, productlsBean.expectedYield + "%+浮动");
                    String raised_amt = String.valueOf(productlsBean.remainingAmount);
                    if (TextUtils.isEmpty(raised_amt)) {
                        raised_amt = "0";
                    }
                    raised_amt = BStrUtils.getYi(raised_amt);
                    if (raised_amt.contains("亿")) {
                        raised_amt = raised_amt.replace("亿", "");


                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, "亿");
                    } else {

                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotMujicount, raised_amt);
                        BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotWan, "万");
                    }
                    BStrUtils.SetTxt(hotProductHolder.productItemProductlsHotLeijijingzhititle, "业绩基准");
                }
                hotProductHolder.productItemProductlsHotJiezhi.setBackgroundResource(AppManager.isInvestor(acontext) ? R.drawable.jiezhi_c_bg : R.drawable.ic_jiezhi);
                BStrUtils.switchColorToBandC(acontext, hotProductHolder.productItemProductlsHotMujicount);
                BStrUtils.switchColorToBandC(acontext, hotProductHolder.productItemProductlsHotCenter);
                break;
            case NORMALPRODUCT://正常标签
                NormalProductHolder normalProductHolder = (NormalProductHolder) holder;
                normalProductHolder.itemView.setTag(position);
                BStrUtils.SetTxt(normalProductHolder.productItemProductlsTitle, productlsBean.productName);

                normalProductHolder.productItemProductlsLogobackground.setImageResource(R.drawable.logobackgroundzanting);


                BStrUtils.SetTxt(normalProductHolder.productItemProductlsQixian, productlsBean.term);

                BStrUtils.SetTxt1(normalProductHolder.productItemProductlsInvisteArea, productlsBean.investmentArea);
                BStrUtils.SetTxt1(normalProductHolder.productItemProductlsInvisteBiaodi, productlsBean.label);

                String type_code = productlsBean.productType;

                if ("1".equals(type_code)) {
                    String remainingAmount = String.valueOf(productlsBean.remainingAmount); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = BStrUtils.getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "亿");
                    } else {
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "万");

                    }


                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijingzhi, productlsBean.expectedYield + "%");
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijinzhiText, "业绩基准");
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduText, "剩余额度");


                } else if ("2".equals(type_code)) {
                    String remainingAmount = String.valueOf(productlsBean.remainingAmount); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = BStrUtils.getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "亿");
                    } else {

                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "万");
                    }

                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijingzhi, productlsBean.cumulativeNet);

                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijinzhiText, "累计净值");
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduText, "剩余额度");


                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsInvisteArea, productlsBean.investmentArea);
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsInvisteBiaodi, productlsBean.label);


                } else if ("3".equals(type_code)) {
                    String remainingAmount = String.valueOf(productlsBean.remainingAmount); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = BStrUtils.getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");

                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "亿");
                    } else {

                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduCount, remainingAmount);
                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsWan, "万");
                    }

                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijingzhi, productlsBean.expectedYield + "%+浮动");
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsLeijijinzhiText, "业绩基准");
                    BStrUtils.SetTxt1(normalProductHolder.productItemProductlsShengyueduText, "剩余额度");

                }

                BStrUtils.switchColorToBandC(acontext, normalProductHolder.productItemProductlsShengyueduCount);

                // 倒计时，将时间显示到listitem上
                // 如果状态10并且有时间，则显示时间text。。。如果状态60，显示暂停text
                try {
                    if (productlsBean.state.equals("60")) {
                        normalProductHolder.productItemProductlsJiezhidate.setText("");
//                        h1.shengyuedu_count.setTextColor(0xff666666);
                        normalProductHolder.productItemProductlsShengyueduCount.setTextColor(0xff222222);
                        normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(R.drawable.zanting);
                    } else if (productlsBean.state.equals("42")) {

                        BStrUtils.SetTxt1(normalProductHolder.productItemProductlsJiezhidate, "");
//                        h1.shengyuedu_count.setTextColor(0xffea1202);
                        normalProductHolder.productItemProductlsShengyueduCount.setTextColor(0xff222222);
                        normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(R.drawable.daifaxing);
                    } else if (productlsBean.raiseEndTime != null) {
                        normalProductHolder.productItemProductlsShengyueduCount.setTextColor(0xffea1202);
//                        h1.shengyuedu_count.setTextColor(0xff222222);
                        java.util.Date end_time = dateFormat.parse(productlsBean.raiseEndTime);
                        long l = end_time.getTime() - System.currentTimeMillis();
                        String dateString = null;
                        int day = (int) (l / 1000 / 60 / 60 / 24);
                        int hour = (int) (l / 1000 / 60 / 60);
                        int min = (int) (l / 1000 / 60);

                        if (hour >= 72) {
                            dateString = day + "天";
                            normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(R.drawable.ic_lanse);
                            normalProductHolder.productItemProductlsJiezhidate.setTextColor(0xffffffff);
                        } else if (hour > 0 && hour < 72) {
                            dateString = hour + "小时";
                            normalProductHolder.productItemProductlsJiezhidate.setTextColor(0xffffffff);
                            normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(AppManager.isInvestor(acontext) ? R.drawable.c_jiaobiao : R.drawable.hongse);
                        } else {
                            if (min == 0) {
                                dateString = 1 + "分钟";
                            } else {
                                dateString = min + "分钟";

                            }
                            normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(AppManager.isInvestor(acontext) ? R.drawable.c_jiaobiao : R.drawable.hongse);
                            normalProductHolder.productItemProductlsJiezhidate.setTextColor(0xffffffff);
                        }
                        if (l <= 0) {
                            normalProductHolder.productItemProductlsJiezhibg.setBackgroundResource(R.drawable.zanting);
                            normalProductHolder.productItemProductlsJiezhidate.setText("");
                        } else {
                            normalProductHolder.productItemProductlsJiezhidate.setText(dateString);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case OVERPRODUCT://已清算
                OverProductHolder overProductHolder = (OverProductHolder) holder;
                overProductHolder.itemView.setTag(position);

                BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverTitle, productlsBean.productName);
                BStrUtils.switchColorToBandC(acontext, overProductHolder.productItemProductlsOverShengyueduCount);

//                h2.touziqidian.setText(p2.getBuyStart() + "万");
                if (productlsBean.state.equals("50")) {
//                    h2.shengyuedu.setTextColor(0xff333333);//hong
                    overProductHolder.productItemProductlsOverShengyueduCount.setTextColor(0xff222222);//hong
                    overProductHolder.productItemProductlsOverQingsuanIcon.setBackgroundResource(R.drawable.mujijieshulogo);


                } else if (productlsBean.state.equals("70")) {
//                    h2.shengyuedu.setTextColor(0xffd73a2e);

                    overProductHolder.productItemProductlsOverShengyueduCount.setTextColor(0xff222222);//hong
                    overProductHolder.productItemProductlsOverQingsuanIcon.setBackgroundResource(R.drawable.logobackgroundyiqingsuan);
                }

                BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverQixian, productlsBean.term);
                String remainingAmount = String.valueOf(productlsBean.remainingAmount); //剩余额度
                if (TextUtils.isEmpty(remainingAmount)) {
                    remainingAmount = "0";
                }
                remainingAmount = BStrUtils.getYi(remainingAmount);  //剩余额度
                if (remainingAmount.contains("亿")) {
                    remainingAmount = remainingAmount.replace("亿", "");

                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverShengyueduCount, remainingAmount);
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverWan, "亿");
                } else {
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverShengyueduCount, remainingAmount);
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverWan, "万");

                }


                BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverInvisteArea, productlsBean.investmentArea);
                BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverInvisteBiaodi, productlsBean.label);

                String type_code2 = productlsBean.productType;
                if (type_code2.equals("1")) {

                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijingzhi, productlsBean.expectedYield + "%");
                    String buyStart = String.valueOf(productlsBean.buyStart);
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijinzhiText, "业绩基准");
                } else if (type_code2.equals("2")) {

                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijingzhi, productlsBean.cumulativeNet + "");
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijinzhiText, "累计净值");
                } else if (type_code2.equals("3")) {
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijingzhi, productlsBean.expectedYield + "%+浮动");
                    BStrUtils.SetTxt1(overProductHolder.productItemProductlsOverLeijijinzhiText, "业绩基准");
                }
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (null == beanList) return 0;
        return beanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (null == beanList) return HOTPRODUCT;

        if (beanList.get(position).isHotProduct.equals("1")) return HOTPRODUCT;

        if (!beanList.get(position).state.equals("70")) return NORMALPRODUCT;

        return OVERPRODUCT;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
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

    public   interface OnRecyclerItemClickListener {
        void onItemClick(View view , int position);
    }
    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
