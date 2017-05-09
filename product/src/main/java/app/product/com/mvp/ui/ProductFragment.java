package app.product.com.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.cache.investorm.CacheInvestor;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cn.hugo.android.scanner.Intents;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.adapter.ProductlsAdapter;
import app.product.com.model.ProductFilterBean;
import app.product.com.model.Series;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.presenter.ProductPresenter;
import app.product.com.utils.BUtils;
import app.product.com.widget.ProductSeriesLayout;
import app.product.com.widget.SimpleItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-10:15
 */
public class ProductFragment extends BaseFragment<ProductPresenter> implements ProductContract.view {

    @BindView(R2.id.fragment_productrecyclerView)
    RecyclerView fragmentProductrecyclerView;
    @BindView(R2.id.product_productfragment_sousou)
    TextView productProductfragmentSousou;
    @BindView(R2.id.product_productfragment_paixu)
    TextView productProductfragmentPaixu;
    @BindView(R2.id.product_productfragment_shaixuan)
    TextView productProductfragmentShaixuan;
    @BindView(R2.id.product_productfragment_productserieslayout)
    ProductSeriesLayout productProductfragmentProductserieslayout;


private LinearLayoutManager linearLayoutManager;
    private ProductlsAdapter productlsAdapter;
    //获取的筛选条件的bean
    private ProductFilterBean productFilterBean;
    //筛选是否展开
    private boolean isExtend;
    //记录当前的系列的数据
    private Series CurrentSeries;

    //获取列表需要的数据************
    //默认第0页
    private int CurrentOffset=0;



    @Override
    protected int layoutID() {
        return R.layout.fragment_product_product;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initConfig();
        initData();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        productProductfragmentProductserieslayout.setInit(true);
        linearLayoutManager=new LinearLayoutManager(baseActivity);
        fragmentProductrecyclerView.setLayoutManager(linearLayoutManager);
        productlsAdapter=new ProductlsAdapter(baseActivity,null);
        fragmentProductrecyclerView.addItemDecoration(new SimpleItemDecoration(baseActivity));
        fragmentProductrecyclerView.setAdapter(productlsAdapter);


        CurrentSeries = new Series("全部", "0");
        if (!BStrUtils.isEmpty(CacheInvestor.getProductFilterCache(baseActivity))) {//判断是否有筛选条件爱今年的缓存
            productFilterBean = new Gson().fromJson(CacheInvestor.getProductFilterCache(baseActivity), ProductFilterBean.class);
            //如果哦有缓存 就初始化筛选条件
            initFilterDate(productFilterBean.getSeries().getItems());
        }
    }

    /**
     * 初始化所有网络数据
     */
    private void initData() {
        //请求筛选条件的数据
        getPresenter().getProductFilterData();
        //请求列表的数据

        JSONArray jsonArray=new JSONArray();
        jsonArray.put("5");
        getPresenter().getProductData(CurrentOffset,"","","","","",jsonArray.toString());
    }

    @Override
    protected ProductPresenter createPresenter() {
        return new ProductPresenter(getContext(), this);
    }


    @OnClick(R2.id.product_productfragment_sousou)
    public void onProductProductfragmentSousouClicked() {
        UiSkipUtils.toNextActivity(baseActivity, SearchBaseActivity.class);
    }

    @OnClick(R2.id.product_productfragment_paixu)
    public void onProductProductfragmentPaixuClicked() {
    }

    @OnClick(R2.id.product_productfragment_shaixuan)
    public void onProductProductfragmentShaixuanClicked() {
    }


    @Override
    public void getProductDataSucc(int Type, String str) {

        switch (Type) {
            case ProductContract.LOAD_FILTER://获取到的筛选条件
                productFilterBean = new Gson().fromJson(str.trim(), ProductFilterBean.class);

                initFilterDate(productFilterBean.getSeries().getItems());

                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://获取到列表数据
                break;
        }
    }

    @Override
    public void getProductDataFail(int Type, String str) {

    }

    /**
     * 开始初始化筛选条件
     */
    public void initFilterDate(final List<Series> filterDate) {
        final List dataList = BUtils.arrayListClone(filterDate);

        productProductfragmentProductserieslayout.setOnClickTextCallBack(new ProductSeriesLayout.OnClickTextCallBack() {
            @Override
            public void onTextClick(TextView textView) {

                productProductfragmentProductserieslayout.setInit(getString(R.string.product_series_all).equals(textView.getText().toString()));
                if (TextUtils.equals(textView.getText().toString(), "更多") || TextUtils.equals(textView.getText().toString(), "收起")) {
                    dataList.clear();
                    if (!isExtend) {
                        isExtend = true;
                        initFilterDate(filterDate);
                    } else {
                        isExtend = false;
                        initFilterDate(filterDate);
                    }
                } else {
                    Series series = (Series) textView.getTag();
                    CurrentSeries=(Series) textView.getTag();
//                    if (fragment != null && series != null) {
//                        try {
//                            JSONArray jsonArray = new JSONArray();
//                            filterParam.put("series", jsonArray.put(series.getKey()));
//                            fragment.setParams(filterParam);
//                            fragment.requestPage();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        ProductDataStatistApiParam.onClickProductSeriesLable(series.getName());
                }
            }
        });
        if (dataList.size() > 10) {
            if (!isExtend) {
                List<Series> data = dataList.subList(0, 9);
                data.add(9, new Series("更多", "more"));
                productProductfragmentProductserieslayout.setLables(data, false);
            } else {
                List<Series> data = dataList;
                data.add(data.size(), new Series("收起", "more"));
                productProductfragmentProductserieslayout.setLables(data, true);
            }
        } else {
            productProductfragmentProductserieslayout.setLables(dataList, false);
        }
    }

}
