package app.product.com.mvc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.recycler.BaseHolder;
import com.cn.hugo.android.scanner.common.BitmapUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.product.com.R;
import app.product.com.model.SearchResultBean;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.utils.CollectionUtils;
import app.product.com.utils.ViewUtil;

/**
 * @author chenlong
 */
public class SearchAdatper extends RecyclerView.Adapter {
    private Context context;
    private List<SearchResultBean.ResultBean> dataList;
//    private BitmapUtil bitmapUtil;
    private String keyName;
    private List<String> returnKeys = new ArrayList<>();
    private String currentType;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private HashMap<SearchResultBean.ResultType, List<SearchResultBean.ResultBean>> hashMap = new HashMap<>();

    public SearchAdatper(Context context, String currentType) {
        this.context = context;
        this.currentType = currentType;
//        bitmapUtil = new BitmapUtil(context);
    }

    public void setReturnKeys(List<String> returnKeys) {
        this.returnKeys = returnKeys;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public SearchResultBean.ResultType getResultTypeByType(int viewType) {
        SearchResultBean.ResultType[] vas = SearchResultBean.ResultType.values();
        return vas[viewType];
    }

    @Override
    public int getItemViewType(int position) {
        SearchResultBean.ResultBean searchBean = dataList.get(position);
        return searchBean.getIsPart().ordinal();
    }

    public boolean isHeadView(int position) {
        if (CollectionUtils.isEmpty(dataList)) {
            SearchResultBean.ResultBean resultBean = dataList.get(position);
            if (resultBean.getIsPart() == SearchResultBean.ResultType.CUSTOM_HEADER ||
                    resultBean.getIsPart() == SearchResultBean.ResultType.XUN_HEADER ||
                    resultBean.getIsPart() == SearchResultBean.ResultType.INFO_HEADER ||
                    resultBean.getIsPart() == SearchResultBean.ResultType.PRODUCT_HEADER ||
                    resultBean.getIsPart() == SearchResultBean.ResultType.VIDEO_HEADER ||
                    resultBean.getIsPart() == SearchResultBean.ResultType.ORDER_HEADER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (CollectionUtils.isEmpty(dataList)) {
            return 0;
        }
        return dataList.size();
    }

    public void setData(List<SearchResultBean> list) {
        List<SearchResultBean.ResultBean> returnList = new ArrayList<>();
        for (SearchResultBean resultBean : list) {
            List<SearchResultBean.ResultBean> valuesList = resultBean.getResults();
            if (!CollectionUtils.isEmpty(valuesList)) {
                processData(returnList, valuesList, resultBean, list.get(list.size() - 1).getType());
            }
        }
        dataList = returnList;
        notifyDataSetChanged();
    }

    public void addDataList(List<SearchResultBean.ResultBean> list) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(list)) {
            dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addDataFooter(SearchResultBean.ResultBean footerResult) {
        if (dataList != null) {
            SearchResultBean.ResultBean resultBean = findDataFooter();
            if (resultBean != null) {
                dataList.remove(resultBean);
            }
            dataList.add(footerResult);
            notifyDataSetChanged();
        }
    }

    public void removeDataFooter() {
        if (dataList != null) {
            SearchResultBean.ResultBean resultBean = findDataFooter();
            if (resultBean != null) {
                dataList.remove(resultBean);
                notifyDataSetChanged();
            }
        }
    }

    private SearchResultBean.ResultBean findDataFooter() {
        for (SearchResultBean.ResultBean resultBean : dataList) {
            if (resultBean.getIsPart() == SearchResultBean.ResultType.NO_MORE_DATA) {
                return resultBean;
            }
        }
        return null;
    }

    public void clearData() {
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.clear();
        }
    }

    private void processData(List<SearchResultBean.ResultBean> returnList, List<SearchResultBean.ResultBean> targetList, SearchResultBean searchResultBean, String lastType) {
        switch (searchResultBean.getType()) {
            case SearchBaseActivity.PRODUCT:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.PRODUCT_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.PRODUCT_ITEM);
                if ((TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6) ||
                        (!TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 2)) {
                    returnList.addAll(targetList.subList(0, TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) ? 6 : 3));
                    hashMap.put(SearchResultBean.ResultType.PRODUCT_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.PRODUCT_MORE));
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
            case SearchBaseActivity.INFOMATION:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.INFO_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.INFO_ITEM);
                if ((TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6) ||
                        (!TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 2)) {
                    returnList.addAll(targetList.subList(0, TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) ? 6 : 3));
                    hashMap.put(SearchResultBean.ResultType.INFO_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.INFO_MORE));
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
            case SearchBaseActivity.ZIXUN:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.XUN_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.XUN_ITEM);
                if (!AppManager.isInvestor(context) && (TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6)) {
                    returnList.addAll(targetList.subList(0, 6));
                    hashMap.put(SearchResultBean.ResultType.XUN_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.XUN_MORE));
                    break;
                } else if ((AppManager.isInvestor(context) || (!AppManager.isInvestor(context) && !TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())))) && targetList.size() > 2) {
                    returnList.addAll(targetList.subList(0, 3));
                    hashMap.put(SearchResultBean.ResultType.XUN_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.XUN_MORE));
                    break;
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
            case SearchBaseActivity.VIDEO:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.VIDEO_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.VIDEO_ITEM);
                if (AppManager.isInvestor(context) && TextUtils.equals(currentType, SearchBaseActivity.ZIXUN) && targetList.size() > 6 ||
                        (!AppManager.isInvestor(context) && TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6)) {
                    returnList.addAll(targetList.subList(0, 6));
                    hashMap.put(SearchResultBean.ResultType.VIDEO_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.VIDEO_MORE));
                    break;
                } else if (!AppManager.isInvestor(context) && !TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 2 ||
                        AppManager.isInvestor(context) && !TextUtils.equals(currentType, SearchBaseActivity.ZIXUN) && targetList.size() > 2) {
                    returnList.addAll(targetList.subList(0, 3));
                    hashMap.put(SearchResultBean.ResultType.VIDEO_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.VIDEO_MORE));
                    break;
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
            case SearchBaseActivity.CUSTOM:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.CUSTOM_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.CUSTOM_ITEM);
                if ((TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6) ||
                        (!TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 2)) {
                    returnList.addAll(targetList.subList(0, TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) ? 6 : 3));
                    hashMap.put(SearchResultBean.ResultType.CUSTOM_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.CUSTOM_MORE));
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
            case SearchBaseActivity.ORDER:
                returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.ORDER_HEADER));
                insertTypeData(targetList, SearchResultBean.ResultType.ORDER_ITEM);
                if ((TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 6) ||
                        (!TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) && targetList.size() > 2)) {
                    returnList.addAll(targetList.subList(0, TextUtils.equals(currentType, String.valueOf(searchResultBean.getType())) ? 6 : 3));
                    hashMap.put(SearchResultBean.ResultType.ORDER_MORE, targetList);
                    returnList.add(new SearchResultBean.ResultBean(SearchResultBean.ResultType.ORDER_MORE));
                } else {
                    returnList.addAll(targetList);
                    returnList.get(returnList.size() - 1).setLaster(!TextUtils.equals(searchResultBean.getType(), lastType));
                }
                break;
        }
    }

    public void insertTypeData(List<SearchResultBean.ResultBean> targetList, SearchResultBean.ResultType resultType) {
        if (!CollectionUtils.isEmpty(targetList)) {
            for (SearchResultBean.ResultBean bean : targetList) {
                bean.setIsPart(resultType);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        SearchResultBean.ResultType vatype = getResultTypeByType(viewType);
        RecyclerView.ViewHolder viewHolder = null;
        switch (vatype) {
            case INFO_HEADER:
            case PRODUCT_HEADER:
            case XUN_HEADER:
            case VIDEO_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            case INFO_MORE:
            case PRODUCT_MORE:
            case XUN_MORE:
            case VIDEO_MORE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_footer, parent, false);
                viewHolder = new FooterViewHolder(view);
                break;
            case INFO_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_info, parent, false);
                viewHolder = new InfoViewHolder(view);
                break;
            case PRODUCT_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_product, parent, false);
                viewHolder = new ProductViewHolder(view);
                break;
            case XUN_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_zixun, parent, false);
                viewHolder = new ZixunViewHolder(view);
                break;
            case VIDEO_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_video, parent, false);
                viewHolder = new VideoViewHolder(view);
                break;
            case NO_MORE_DATA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_holder_no_more, parent, false);
                viewHolder = new NoDataViewHolder(view);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchResultBean.ResultBean bean = dataList.get(position);
        if (holder != null) {
            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
                viewHolder.bindData(bean);
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.bindData(bean);
            } else if (holder instanceof InfoViewHolder) {
                InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
                infoViewHolder.bindData(bean);
            } else if (holder instanceof ProductViewHolder) {
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                productViewHolder.bindData(bean);
            } else if (holder instanceof ZixunViewHolder) {
                ZixunViewHolder zixunViewHolder = (ZixunViewHolder) holder;
                zixunViewHolder.bindData(bean);
            } else if (holder instanceof VideoViewHolder) {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.bindData(bean);
            }
        }
    }

    public class BaseViewHolder extends BaseHolder {
        private View rootView;
        protected TextView headView;
        protected TextView descrptionView;
        public boolean displayFinished;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getTag() != null) {
//                        SearchResultBean.ResultBean resultBean = (SearchResultBean.ResultBean) v.getTag();
//                        switch (resultBean.getIsPart()) {
//                            case PRODUCT_ITEM:
//                                NavigationUtils.startProductActivity(context, resultBean.getTargetId());
//                                Utils.hotLookWrite(context, SearchBaseActivity.PRODUCT, resultBean);
//                                break;
//                            case XUN_ITEM:
//                                NavigationUtils.startZiXunActivity(context, resultBean);
//                                Utils.hotLookWrite(context, SearchBaseActivity.ZIXUN, resultBean);
//                                break;
//                            case INFO_ITEM:
//                                NavigationUtils.startMessageActivity(context, resultBean, keyName);
//                                Utils.hotLookWrite(context, SearchBaseActivity.INFOMATION, resultBean);
//                                break;
//                            case VIDEO_ITEM:
//                                ToolsUtils.toPlayVideoActivity(context, resultBean.getTargetId());
//                                Utils.hotLookWrite(context, SearchBaseActivity.VIDEO, resultBean);
//                                break;
//                        }
//                    }
//                }
//            });
        }

        protected void bindData(SearchResultBean.ResultBean resultBean) {
            headView.setText(resultBean.getTitle());
            displayFinished = resultBean.isLaster();
            headView.setTag(resultBean);
            rootView.setTag(resultBean);
        }
    }

    private class HeaderViewHolder extends BaseViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            headView = (TextView) itemView.findViewById(R.id.title_head);
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
            int res;
            if (resultBean.getIsPart() == SearchResultBean.ResultType.INFO_HEADER) {
                res = R.string.search_info;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.PRODUCT_HEADER) {
                res = R.string.search_product;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.XUN_HEADER) {
                res = R.string.search_zixun;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.VIDEO_HEADER) {
                res = R.string.search_video;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.CUSTOM_HEADER) {
                res = R.string.search_custom;
            } else {
                res = R.string.search_video;
            }
            headView.setText(String.format(context.getString(R.string.relative_title), context.getString(res)));
        }
    }

    public class FooterViewHolder extends BaseViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
            headView = (TextView) itemView.findViewById(R.id.item_more);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        PromptManager.ShowCustomToast(context,"在searchAdapter 里面的 FooterViewHolder 找我填补");
//                        SearchResultBean.ResultBean resultBean = (SearchResultBean.ResultBean) v.getTag();
//                        List<SearchResultBean.ResultBean> list = hashMap.get(resultBean.getIsPart());
//                        Intent intent = new Intent(context, SearchResultListActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(SearchResultListActivity.LIST_PARAM, (Serializable) list);
//                        bundle.putStringArrayList(SearchResultListActivity.KEY_NAME_PARAM, (ArrayList<String>) returnKeys);
//                        bundle.putString(SearchBaseActivity.KEY_NAME_PARAM, keyName);
//                        bundle.putString(SearchBaseActivity.TYPE_PARAM, currentType);
//                        bundle.putString(SearchBaseActivity.SUB_TYPE_PARAM, getSubType(resultBean));
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
            int res;
            if (resultBean.getIsPart() == SearchResultBean.ResultType.INFO_MORE) {
                res = R.string.search_info;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.PRODUCT_MORE) {
                res = R.string.search_product;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.XUN_MORE) {
                res = R.string.search_zixun;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.VIDEO_MORE) {
                res = R.string.search_video;
            } else if (resultBean.getIsPart() == SearchResultBean.ResultType.CUSTOM_MORE) {
                res = R.string.search_custom;
            } else {
                res = R.string.search_video;
            }
            headView.setText(String.format(context.getString(R.string.look_more), context.getString(res)));
        }
    }

    private String getSubType(SearchResultBean.ResultBean resultBean) {
        String result = SearchBaseActivity.INFOMATION;
        switch (resultBean.getIsPart()) {
            case INFO_MORE:
                result = SearchBaseActivity.INFOMATION;
                break;
            case PRODUCT_MORE:
                result = SearchBaseActivity.PRODUCT;
                break;
            case XUN_MORE:
                result = SearchBaseActivity.ZIXUN;
                break;
            case VIDEO_MORE:
                result = SearchBaseActivity.VIDEO;
                break;
        }
        return result;
    }

    private class InfoViewHolder extends BaseViewHolder {

        private TextView contextView;
        protected boolean displayFinished;

        public InfoViewHolder(View itemView) {
            super(itemView);
            headView = (TextView) itemView.findViewById(R.id.name);
            contextView = (TextView) itemView.findViewById(R.id.content);
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
            contextView.setText(resultBean.getSummary());
            ViewUtil.setTextColor(context, headView, returnKeys);
            ViewUtil.setTextColor(context, contextView, returnKeys);
        }
    }

    private class ProductViewHolder extends BaseViewHolder {
        private ImageView logobackground, jiezhibg, qingsuanImage;
        private TextView leijijingzhi, leijijinzhi_text, shengyuedu_text, wan,
                shengyuedu_count, jiezhi, jiezhidate, qixian, touziArea, touziLable;

        private ProductViewHolder(View itemView) {
            super(itemView);
            descrptionView = (TextView) itemView.findViewById(R.id.description);
            headView = (TextView) itemView.findViewById(R.id.title);
            jiezhibg = (ImageView) itemView.findViewById(R.id.jiezhibg);
            qingsuanImage = (ImageView) itemView.findViewById(R.id.qingsuan_icon);
            jiezhidate = (TextView) itemView.findViewById(R.id.jiezhidate);
            logobackground = (ImageView) itemView.findViewById(R.id.logobackground);
            leijijingzhi = (TextView) itemView.findViewById(R.id.leijijingzhi);
            shengyuedu_count = (TextView) itemView.findViewById(R.id.shengyuedu_count);
            leijijinzhi_text = (TextView) itemView.findViewById(R.id.leijijinzhi_text);
            shengyuedu_text = (TextView) itemView.findViewById(R.id.shengyuedu_text);
            jiezhi = (TextView) itemView.findViewById(R.id.jiezhi);
            wan = (TextView) itemView.findViewById(R.id.wan);
            qixian = (TextView) itemView.findViewById(R.id.qixian);
            touziLable = (TextView) itemView.findViewById(R.id.inviste_biaodi);
            touziArea = (TextView) itemView.findViewById(R.id.inviste_area);
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
            if (!"70".equals(resultBean.getState())) {
                headView.setText(resultBean.getTitle());
                if ("20".equals(resultBean.getState())) {
                    logobackground.setImageResource(R.drawable.logobackgroundzanting);
                } else {
                    logobackground.setImageResource(R.drawable.logobackgroundzanting);
                }
                qixian.setText(resultBean.getTerm());
                touziArea.setText(resultBean.getInvestmentArea());
                touziLable.setText(resultBean.getLabel());
                String type_code = resultBean.getProductType();
                if ("1".equals(type_code)) {
                    String remainingAmount = String.valueOf(resultBean.getRemainingAmount()); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("亿");
                    } else {
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("万");
                    }

                    leijijingzhi.setText(resultBean.getExpectedYield() + "%");  //累计净值
                    leijijinzhi_text.setText("业绩基准");
                    shengyuedu_text.setText("剩余额度");
                } else if ("2".equals(type_code)) {
                    String remainingAmount = String.valueOf(resultBean.getRemainingAmount()); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("亿");
                    } else {
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("万");
                    }

                    leijijingzhi.setText(resultBean.getCumulativeNet() + "");  //累计净值
                    leijijinzhi_text.setText("累计净值");
                    shengyuedu_text.setText("剩余额度");
                    touziArea.setText(resultBean.getInvestmentArea());
                    touziLable.setText(resultBean.getLabel());
                } else if ("3".equals(type_code)) {
                    String remainingAmount = String.valueOf(resultBean.getRemainingAmount()); //剩余额度
                    if (TextUtils.isEmpty(remainingAmount)) {
                        remainingAmount = "0";
                    }
                    remainingAmount = getYi(remainingAmount);  //剩余额度
                    if (remainingAmount.contains("亿")) {
                        remainingAmount = remainingAmount.replace("亿", "");
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("亿");
                    } else {
                        shengyuedu_count.setText(remainingAmount);
                        wan.setText("万");
                    }

                    leijijingzhi.setText(resultBean.getExpectedYield() + "%+浮动");  //累计净值
                    leijijinzhi_text.setText("业绩基准");
                    shengyuedu_text.setText("剩余额度");
                }
                ViewUtils.switchColorToBandC(context, shengyuedu_count);
                try {
                    if ("60".equals(resultBean.getState())) {
                        jiezhidate.setText("");
                        shengyuedu_count.setTextColor(0xff222222);
                        jiezhibg.setBackgroundResource(R.drawable.zanting);
                    } else if ("42".equals(resultBean.getState())) {
                        jiezhidate.setText("");
                        shengyuedu_count.setTextColor(0xff222222);
                        jiezhibg.setBackgroundResource(R.drawable.daifaxing);
                    } else if (resultBean.getRaiseEndTime() != null) {
                        shengyuedu_count.setTextColor(0xffea1202);
                        java.util.Date end_time = dateFormat.parse(resultBean.getRaiseEndTime());
                        long l = end_time.getTime() - System.currentTimeMillis();
                        String dateString = null;
                        int day = (int) (l / 1000 / 60 / 60 / 24);
                        int hour = (int) (l / 1000 / 60 / 60);
                        int min = (int) (l / 1000 / 60);
                        if (hour >= 72) {
                            dateString = day + "天";
                            jiezhibg.setBackgroundResource(R.drawable.lanse);
                            jiezhidate.setTextColor(0xffffffff);
                        } else if (hour > 0 && hour < 72) {
                            dateString = hour + "小时";
                            jiezhidate.setTextColor(0xffffffff);
                            jiezhibg.setBackgroundResource(AppManager.isInvestor(context) ? R.drawable.c_jiaobiao : R.drawable.hongse);
                        } else {
                            if (min == 0) {
                                dateString = 1 + "分钟";
                            } else {
                                dateString = min + "分钟";
                            }
                            jiezhibg.setBackgroundResource(AppManager.isInvestor(context) ? R.drawable.c_jiaobiao : R.drawable.hongse);
                            jiezhidate.setTextColor(0xffffffff);
                        }
                        if (l <= 0) {
                            jiezhibg.setBackgroundResource(R.drawable.zanting);
                            jiezhidate.setText("");
                        } else {
                            jiezhidate.setText(dateString);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                qingsuanImage.setVisibility(View.GONE);
                jiezhibg.setVisibility(View.VISIBLE);
                jiezhidate.setVisibility(View.VISIBLE);

                boolean titleSuc = ViewUtil.setTextColor(context, headView, returnKeys);
                boolean lableSuc = ViewUtil.setTextColor(context, touziLable, returnKeys);
                boolean areaSuc = ViewUtil.setTextColor(context, touziArea, returnKeys);
                if (!TextUtils.isEmpty(resultBean.getDes()) && descrptionView != null && !titleSuc && !lableSuc && !areaSuc) {
                    descrptionView.setText(resultBean.getDes());
                    descrptionView.setVisibility(View.VISIBLE);
                    ViewUtil.setTextColor(context, descrptionView, returnKeys);
                } else {
                    descrptionView.setVisibility(View.GONE);
                }
            } else {
                qingsuanImage.setVisibility(View.VISIBLE);
                jiezhibg.setVisibility(View.GONE);
                jiezhidate.setVisibility(View.GONE);
                headView.setText(resultBean.getTitle());
                ViewUtils.switchColorToBandC(context, shengyuedu_count);
                if ("50".equals(resultBean.getState())) {
                    shengyuedu_count.setTextColor(0xff222222);
                    qingsuanImage.setBackgroundResource(R.drawable.mujijieshulogo);
                } else if ("70".equals(resultBean.getState())) {
                    shengyuedu_count.setTextColor(0xff222222);//hong
                    qingsuanImage.setBackgroundResource(R.drawable.logobackgroundyiqingsuan);
                }
                qixian.setText(resultBean.getTerm());

                String remainingAmount = String.valueOf(resultBean.getRemainingAmount()); //剩余额度
                if (TextUtils.isEmpty(remainingAmount)) {
                    remainingAmount = "0";
                }
                remainingAmount = getYi(remainingAmount);  //剩余额度
                if (remainingAmount.contains("亿")) {
                    remainingAmount = remainingAmount.replace("亿", "");
                    shengyuedu_count.setText(remainingAmount);
                    wan.setText("亿");
                } else {
                    shengyuedu_count.setText(remainingAmount);
                    wan.setText("万");
                }

                touziArea.setText(resultBean.getInvestmentArea());
                touziLable.setText(resultBean.getLabel());

                String type_code2 = resultBean.getProductType();
                if (type_code2.equals("1")) {
                    leijijingzhi.setText(resultBean.getExpectedYield() + "%");
                    leijijinzhi_text.setText("业绩基准");
                } else if (type_code2.equals("2")) {
                    leijijingzhi.setText(resultBean.getCumulativeNet() + "");   //累计净值
                    leijijinzhi_text.setText("累计净值");
                } else if (type_code2.equals("3")) {
                    leijijingzhi.setText(resultBean.getExpectedYield() + "%+浮动");  //累计净值
                    leijijinzhi_text.setText("业绩基准");
                }
            }
        }
    }

    private String getYi(String num) {
        try {
            Float f = Float.parseFloat(num);
            if (f >= 10000.00f) {
                int vas = f.intValue();
                if (vas % 10000 == 0) {
                    return vas / 10000 + "亿";
                }
                f = f / 10000;
                String d = String.format("%.2f", f);
                return d + "亿";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    private class ZixunViewHolder extends BaseViewHolder {
        private TextView timeView;

        public ZixunViewHolder(View itemView) {
            super(itemView);
            headView = (TextView) itemView.findViewById(R.id.name);
            timeView = (TextView) itemView.findViewById(R.id.time);
            descrptionView = (TextView) itemView.findViewById(R.id.description);
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
            timeView.setText(resultBean.getTime());
            boolean titleSuc = ViewUtil.setTextColor(context, headView, returnKeys);
            ViewUtil.setTextColor(context, timeView, returnKeys);
            if (!TextUtils.isEmpty(resultBean.getDes()) && descrptionView != null && !titleSuc) {
                descrptionView.setText(resultBean.getDes());
                descrptionView.setVisibility(View.VISIBLE);
                ViewUtil.setTextColor(context, descrptionView, returnKeys);
            } else {
                descrptionView.setVisibility(View.GONE);
            }
        }
    }

    private class VideoViewHolder extends BaseViewHolder {

        private ImageView videoImage;
        private TextView timeView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            headView = (TextView) itemView.findViewById(R.id.name);
            timeView = (TextView) itemView.findViewById(R.id.time);
            descrptionView = (TextView) itemView.findViewById(R.id.description);
        }

        @Override
        protected void bindData(SearchResultBean.ResultBean resultBean) {
            super.bindData(resultBean);
//            bitmapUtils.display(videoImage, resultBean.getUrl(), new BitmapLoadCallBack<ImageView>() {
//                @Override
//                public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//                    if (bitmap != null) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                }
//
//                @Override
//                public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
//                    imageView.setImageResource(R.drawable.logoshare);
//                }
//            });
            Imageload.display( context,resultBean.getUrl(),videoImage);
            timeView.setText(resultBean.getTime());
            boolean titleSuc = ViewUtil.setTextColor(context, headView, returnKeys);
            ViewUtil.setTextColor(context, timeView, returnKeys);
            if (!TextUtils.isEmpty(resultBean.getDes()) && descrptionView != null && !titleSuc) {
                descrptionView.setText(resultBean.getDes());
                descrptionView.setVisibility(View.VISIBLE);
                ViewUtil.setTextColor(context, descrptionView, returnKeys);
            } else {
                descrptionView.setVisibility(View.GONE);
            }
        }
    }

    public class NoDataViewHolder extends BaseHolder {

        private TextView textView;

        public NoDataViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
