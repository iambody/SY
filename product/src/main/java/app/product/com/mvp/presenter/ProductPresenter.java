package app.product.com.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.VideoLikeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.utils.cache.investorm.CacheInvestor;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.product.com.R;
import app.product.com.model.FilterItem;
import app.product.com.model.ProductFilterBean;
import app.product.com.model.Series;
import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.ui.adapter.ProductAdapter;
import app.product.com.widget.ProductSeriesLayout;

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
            jsonObject.put("offset", "" + offset * Contant.LOAD_PRODUCT_lIMIT);
            jsonObject.put("limit", Contant.LOAD_PRODUCT_lIMIT + "");
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
