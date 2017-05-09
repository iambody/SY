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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void getProductData(int offset, String productType, String riskLevel, String income, String investmentArea, String orderBy, String series) {
        Map<String, String> map = new HashMap<>();
//        map.put("offset", "" + offset * Contant.LOAD_PRODUCT_lIMIT);
//        map.put("limit", Contant.LOAD_PRODUCT_lIMIT + "");
//         map.put("category", AppManager.isInvestor(getContext()) ? "c" : "b");
//        map.put("series", series);
//
//        map.put("userId", series);

        addSubscription(ApiClient.getProductlsDate(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                LogUtils.Log("s", "ssss");
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", "ssss");

            }
        }));
    }


    @Override
    public void getProductFilterData() {
//        if (!BStrUtils.isEmpty(CacheInvestor.getProductFilterCache(getContext())))

        addSubscription(ApiClient.getProductFiltrtDate().subscribe(new RxSubscriber<String>() {

            @Override
            protected void onEvent(String result) {
                getView().getProductDataSucc(ProductContract.LOAD_FILTER, result);
                if (!BStrUtils.isEmpty(result)) {
                    CacheInvestor.saveProductFilterCache(getContext(), result);
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getProductDataFail(ProductContract.LOAD_FILTER, error.toString());
            }
        }));
    }


}
