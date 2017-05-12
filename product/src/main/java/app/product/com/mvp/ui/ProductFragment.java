package app.product.com.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.cache.investorm.CacheInvestor;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.adapter.ProductlsAdapter;
import app.product.com.model.EventFiltBean;
import app.product.com.model.FilterItem;
import app.product.com.model.ProductFilterBean;
import app.product.com.model.ProductlsBean;
import app.product.com.model.Series;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.presenter.ProductPresenter;
import app.product.com.utils.BUtils;
import app.product.com.widget.FilterPop;
import app.product.com.widget.OrderbyPop;
import app.product.com.widget.ProductSeriesLayout;
import app.product.com.widget.SimpleItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-10:15
 */
public class ProductFragment extends BaseFragment<ProductPresenter> implements ProductContract.view, OnLoadMoreListener, OnRefreshListener {

    @BindView(R2.id.swipe_target)
    RecyclerView fragmentProductrecyclerView;
    @BindView(R2.id.product_productfragment_sousou)
    TextView productProductfragmentSousou;
    @BindView(R2.id.product_productfragment_paixu)
    TextView productProductfragmentPaixu;
    @BindView(R2.id.product_productfragment_shaixuan)
    TextView productProductfragmentShaixuan;
    @BindView(R2.id.product_productfragment_productserieslayout)
    ProductSeriesLayout productProductfragmentProductserieslayout;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;


    //排序的事件
    private Observable<Series> seriesObservable;
    //筛选的确定事件
    private Observable<EventFiltBean> filterObservable;
    private LinearLayoutManager linearLayoutManager;
    private ProductlsAdapter productlsAdapter;
    //智能排序的视图Pop
    private OrderbyPop orderbyPop;
    //筛选的视图Pop
    private FilterPop filterPop;
    //获取的筛选条件的bean
    private ProductFilterBean productFilterBean;
    //筛选是否展开
    private boolean isExtend;
    private boolean isLoadmore;
    //所有记录选择状态的数据**********************
    // 记录当前的系列的数据
    private String CurrentSeries;
    //记录当前的排序的数据
    private String CurrentOderBy;
    //默认第0页
    private int CurrentOffset = 0;
    //记录多个筛选的数据
    private List<FilterItem> CurrentFilter = null;


    //获取列表需要的数据************

    private List<ProductlsBean> productlsBeen = new ArrayList<>();

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
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        productProductfragmentProductserieslayout.setInit(true);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentProductrecyclerView.setLayoutManager(linearLayoutManager);
        productlsAdapter = new ProductlsAdapter(baseActivity, null);
        fragmentProductrecyclerView.addItemDecoration(new SimpleItemDecoration(baseActivity, R.color.transparent, R.dimen.ui_10_dip));
        fragmentProductrecyclerView.setAdapter(productlsAdapter);
        productlsAdapter.setOnItemClickListener(new ProductlsAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PromptManager.ShowCustomToast(baseActivity,"点击位置"+position);
            }
        });

        CurrentSeries = "0";//默认系列是全部  0代表全部
        CurrentOderBy = "";//默认排序是没有的  这样做是传递空的话 后台会返回默认的排序方式
        if (!BStrUtils.isEmpty(CacheInvestor.getProductFilterCache(baseActivity))) {//判断是否有筛选条件的缓存
            productFilterBean = new Gson().fromJson(CacheInvestor.getProductFilterCache(baseActivity), ProductFilterBean.class);
            //如果哦有缓存 就初始化筛选条件
            initFilterDate(productFilterBean.getSeries().getItems());
        }
        initEvent();

    }

    //注册事件
    private void initEvent() {
        // 智能排序
        seriesObservable = RxBus.get().register(ProductPresenter.PRODUCT_ORDERBY_TO_FRAGMENT, Series.class);
        seriesObservable.subscribe(new RxSubscriber<Series>() {
            @Override
            protected void onEvent(Series series) {
                BStrUtils.SetTxt(productProductfragmentPaixu, series.getName());
                CurrentOderBy = series.getKey();
                reSetConditionAction();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        // 筛选点击
        filterObservable = RxBus.get().register(ProductPresenter.PRODUCT_FILTER_TO_FRAGMENT, EventFiltBean.class);

        filterObservable.subscribe(new RxSubscriber<EventFiltBean>() {
            @Override
            protected void onEvent(EventFiltBean filterItems) {
                CurrentFilter = filterItems.getFilterItemList();

                reSetConditionAction();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销
        if (null != seriesObservable)
            RxBus.get().unregister(ProductPresenter.PRODUCT_ORDERBY_TO_FRAGMENT, seriesObservable);

        if (null != filterObservable)
            RxBus.get().unregister(ProductPresenter.PRODUCT_FILTER_TO_FRAGMENT, filterObservable);
    }

    /**
     * 初始化所有网络数据
     */
    private void initData() {
        //请求筛选条件的数据
        getPresenter().getProductFilterData();
        //请求列表的数据
        reSetConditionAction();
    }

    @Override
    protected ProductPresenter createPresenter() {
        return new ProductPresenter(getContext(), this);
    }


    @OnClick(R2.id.product_productfragment_sousou)
    public void onProductProductfragmentSousouClicked() {
//        UiSkipUtils.toNextActivity(baseActivity, SearchBaseActivity.class);
        Intent i = new Intent(baseActivity, SearchBaseActivity.class);
        i.putExtra(SearchBaseActivity.TYPE_PARAM, SearchBaseActivity.PRODUCT);
        baseActivity.startActivity(i);

    }

    @OnClick(R2.id.product_productfragment_paixu)
    public void onProductProductfragmentPaixuClicked() {
        if (null != orderbyPop && orderbyPop.isShowing()) {
            orderbyPop.dismiss();


            return;

        }

        orderbyPop = new OrderbyPop(baseActivity, productFilterBean.getOrderBy().getItems());
        orderbyPop.showAsDropDown(productProductfragmentPaixu, 0, 20);

    }

    @OnClick(R2.id.product_productfragment_shaixuan)
    public void onProductProductfragmentShaixuanClicked() {
        if (null == filterPop)
            filterPop = new FilterPop(baseActivity, CurrentFilter);
        if (null == CurrentFilter) {
            PromptManager.ShowCustomToast(baseActivity, getResources().getString(R.string.nofiltedate));
            return;
        }

        filterPop.showAsDropDown(productProductfragmentPaixu, 0, 20, CurrentFilter);

    }


    @Override
    public void getDataSucc(int Type, String str) {
        clodLsAnim(swipeToLoadLayout);
        switch (Type) {
            case ProductContract.LOAD_FILTER://获取到的筛选条件
                productFilterBean = new Gson().fromJson(str.trim(), ProductFilterBean.class);
                initFilterDate(productFilterBean.getSeries().getItems());
                CurrentFilter = productFilterBean.getFilter();

                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://获取到列表数据
//                PromptManager.ShowCustomToast(getContext(), "请求列表成功" + str);
                //开始解析数据
                productlsBeen = new Gson().fromJson(str, new TypeToken<ArrayList<ProductlsBean>>() {
                }.getType());
                if (isLoadmore) productlsAdapter.AddfreshAp(productlsBeen);
                else
                    productlsAdapter.freshAp(productlsBeen);

                break;
        }
        isLoadmore = false;
    }

    @Override
    public void getDataFail(int Type, String str) {
        clodLsAnim(swipeToLoadLayout);
        PromptManager.ShowCustomToast(getContext(), str);
        switch (Type) {
            case ProductContract.LOAD_FILTER://请求筛选条件失败

                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://请求产品列表成功
                break;
        }
        isLoadmore = false;
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
//                    Series series = (Series) textView.getTag();
                    CurrentSeries = ((Series) textView.getTag()).getKey();
                    reSetConditionAction();
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

    /**
     * 点击系列 点击只能排序 点击筛选 的确定
     * 开始重新请求数据
     */
    private void reSetConditionAction() {
        getPresenter().getProductData(CurrentOffset, CurrentSeries, CurrentOderBy, CurrentFilter);
    }

    /**
     * 返回键
     */
    public void backClick() {
        if (null != orderbyPop && orderbyPop.isShowing())
            orderbyPop.dismiss();

        if (null != filterPop && filterPop.isShowing())
            filterPop.dismiss();

    }

    /**
     * 判断是否有显示的dialog
     */
    public boolean isShow() {
        return (null != orderbyPop && orderbyPop.isShowing()) || (null != filterPop && filterPop.isShowing());
    }

    @Override
    public void onLoadMore() {
        CurrentOffset = CurrentOffset + 1;
        isLoadmore = true;
        reSetConditionAction();
    }

    @Override
    public void onRefresh() {
        CurrentOffset = 0;
        reSetConditionAction();
    }


}
