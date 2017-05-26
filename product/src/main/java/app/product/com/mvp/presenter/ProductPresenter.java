package app.product.com.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.investorm.CacheInvestor;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.product.com.R;
import app.product.com.model.FilterItem;
import app.product.com.model.Series;
import app.product.com.mvp.contract.ProductContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/5-21:22
 */
public class ProductPresenter extends BasePresenterImpl<ProductContract.view> implements ProductContract.Presenter {


    public ProductPresenter(@NonNull Context context, @NonNull ProductContract.view view) {
        super(context, view);
    }

    /**
     * @param offset         第几页
     * @param productType    产品类型 字符串数组
     * @param riskLevel      风险等级 字符串数组
     * @param income         预期收益 字符串数组
     * @param investmentArea 投资领域 字符串数组
     * @param orderBy        排序 字符串数组
     * @param series         系列 字符串数组
     */
    @Override
    public void getProductData(int offset, String series, String orderBy, List<FilterItem> datas) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("offset", "" + offset * Constant.LOAD_PRODUCT_lIMIT);
            jsonObject.put("limit", Constant.LOAD_PRODUCT_lIMIT + "");
            jsonObject.put("userId", AppManager.getUserId(getContext().getApplicationContext()));

            jsonObject.put("series", BStrUtils.StrToJsonArray(series));
            jsonObject.put("orderBy", BStrUtils.StrToJsonArray(orderBy));
            //单选的
            if (null != datas) {
                insetJsonByLsFilter(jsonObject, datas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("param", jsonObject.toString());
        addSubscription(ApiClient.getProductlsDate(map1).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (!BStrUtils.isEmpty(s)) {
                    getView().getDataSucc(ProductContract.LOAD_PRODUCT_LISTDATA, s);
                } else {
                    getView().getDataFail(ProductContract.LOAD_PRODUCT_LISTDATA, getContext().getString(R.string.resultempty));
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getDataFail(ProductContract.LOAD_PRODUCT_LISTDATA, error.toString());
            }
        }));
    }

    //如果筛选条件有的话需要添加条件处理
    private void insetJsonByLsFilter(JSONObject jsonObject, List<FilterItem> datas) {
        for (int i = 0; i < datas.size(); i++) {
            try {
                jsonObject.put(datas.get(i).getKey(), "text".equals(datas.get(i).getType()) ? filterEditTojsonarray(datas.get(i)) : filterItemTojsonarray(datas.get(i).getItems()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 单选多选是需要判断标识的
     *
     * @param data
     * @return
     */
    private JSONArray filterItemTojsonarray(List<Series> data) {
        List<String> lsdatas = new ArrayList<>();
        for (Series h : data) {
            if (h.isChecked()) lsdatas.add(h.getKey());
        }
        return BStrUtils.LsToJsonArray(lsdatas);
    }

    /**
     * 输入模式时候需要直接接入最大最小值的
     *
     * @param seriesData
     * @return
     */
    private JSONArray filterEditTojsonarray(FilterItem seriesData) {
        List<String> lsdatass = new ArrayList<>();
        lsdatass.add(seriesData.getMinNumber());
        lsdatass.add(seriesData.getMaxNumber());
        return BStrUtils.LsToJsonArray(lsdatass);
    }

    @Override
    public void getProductFilterData() {
        addSubscription(ApiClient.getProductFiltrtDate().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String result) {
                getView().getDataSucc(ProductContract.LOAD_FILTER, result);
                if (!BStrUtils.isEmpty(result)) {
                    CacheInvestor.saveProductFilterCache(getContext(), result);
                } else {
                    getView().getDataFail(ProductContract.LOAD_FILTER, getContext().getString(R.string.resultempty));
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getDataFail(ProductContract.LOAD_FILTER, error.toString());
            }
        }));
    }


}
