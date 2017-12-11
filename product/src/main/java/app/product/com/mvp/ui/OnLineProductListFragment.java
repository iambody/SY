package app.product.com.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.investorm.CacheInvestor;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.TrackingDataUtils;
import com.cgbsoft.lib.utils.ui.RecycleViewDivider;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.chenenyu.router.Router;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.adapter.ProductlsAdapter;
import app.product.com.model.EventFiltBean;
import app.product.com.model.FilterItem;
import app.product.com.model.ProductFilterBean;
import app.product.com.model.Series;
import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.presenter.ProductPresenter;
import app.product.com.utils.BUtils;
import app.product.com.utils.ProductNavigationUtils;
import app.product.com.widget.FilterPop;
import app.product.com.widget.MsgSentDialog;
import app.product.com.widget.OrderbyPop;
import app.product.com.widget.ProductSeriesLayout;
import app.product.com.widget.SimpleItemDecoration;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-10:15
 */
public class OnLineProductListFragment extends BaseFragment<ProductPresenter> implements ProductContract.view, OnLoadMoreListener, OnRefreshListener {

    public static final String FROM_SEND_PRODUCT = "from_rong_send_product";

    @BindView(R2.id.swipe_target)
    RecyclerView fragmentProductrecyclerView;
    @BindView(R2.id.product_productfragment_paixu)
    TextView productProductfragmentPaixu;
    @BindView(R2.id.product_productfragment_shaixuan)
    TextView productProductfragmentShaixuan;
    @BindView(R2.id.product_productfragment_productserieslayout)
    ProductSeriesLayout productProductfragmentProductserieslayout;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R2.id.product_productfragment_empty_iv)
    ImageView productProductfragmentEmptyIv;
    @BindView(R2.id.product_product_wenjuan)
    TextView productProductWenjuan;
    @BindView(R2.id.product_product_filter_lay)
    LinearLayout productProductFilterLay;
    @BindView(R2.id.filter_line)
    View filter_line;

    @BindView(R2.id.swipe_load_more_footer)
    CustomRefreshFootView swipe_load_more_footer;

    @BindView(R2.id.series_layout)
    LinearLayout seriesLayout;

    @BindView(R2.id.load_error)
    TextView load_error;

    @BindView(R2.id.reload)
    TextView reload;

//    @BindView(R2.id.blurring_view)
//    BlurringView blurringView;

    //加载数据的dialog
    private LoadingDialog loadingDialog;
    //标识 是否第一次初始化加载数据
    private boolean isInitData = true;
    //风险测评
    private View product_product_riskevalust;

    //排序的事件
    private Observable<Series> seriesObservable;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //筛选的确定事件
    private Observable<EventFiltBean> filterObservable;
    private Observable<Boolean> refreshObserable;
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
    private String CurrentSeries = "0";
    //记录当前的排序的数据
    private String CurrentOderBy;
    //默认第0页
    private int CurrentOffset = 0;
    //记录多个筛选的数据
    private List<FilterItem> CurrentFilter = null;

    private boolean fromShare;

    private boolean onLineProduct = true;

    private boolean isFirstShow;

    //获取列表需要的数据************

    private List<ProductlsBean> productlsBeen = new ArrayList<>();
    private TextView product_product_wenjuan;

    private Observable<Boolean> riskStateObservable;


    @Override
    protected int layoutID() {
        return R.layout.fragment_product_product;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        isFirstShow = true;
        initConfig();
        initRiskEvaluat();
        initCache();
        initData();
        riskStateObservable = RxBus.get().register(RxConstant.RefreshRiskState, Boolean.class);
        riskStateObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean b) {
                if (productlsBeen != null && productlsBeen.size() > 0) {
                    initRiskEvaluat();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (productlsBeen != null && productlsBeen.size() > 0) {
            if (isVisibleToUser) {
                initRiskEvaluat();
            }
        }
    }

    /**
     * 产品的缓存处理  进来勿忘状态OR请求数据前需要展示的
     */
    private void initCache() {
/////筛选条件
        if (!BStrUtils.isEmpty(CacheInvestor.getProductFilterCache(baseActivity.getApplicationContext()))) {//筛选条件有缓存
            productFilterBean = new Gson().fromJson(CacheInvestor.getProductFilterCache(baseActivity.getApplicationContext()).trim(), ProductFilterBean.class);
            initFilterDate(productFilterBean.getSeries().getItems());
            CurrentFilter = productFilterBean.getFilter();
        }
        //第一页缓存
        if (!BStrUtils.isEmpty(CacheInvestor.getProductls(baseActivity.getApplicationContext()))) {
            productlsBeen = new Gson().fromJson(CacheInvestor.getProductls(baseActivity.getApplicationContext()), new TypeToken<List<ProductlsBean>>() {
            }.getType());
            fragmentProductrecyclerView.setBackground(getResources().getDrawable(R.drawable.shape_null));

            productProductfragmentEmptyIv.setVisibility(View.GONE);
            swipeToLoadLayout.setVisibility(View.VISIBLE);

            if (0 == productlsBeen.size()) {
                productProductfragmentEmptyIv.setVisibility(View.VISIBLE);
                swipeToLoadLayout.setVisibility(View.GONE);
            }
            productlsAdapter.freshAp(productlsBeen);
        }
    }

    /**
     */
    private void initRiskEvaluat() {
        isFirstShow = false;
        if (AppManager.isVisitor(getActivity())) {
            product_product_wenjuan.setText(getResources().getString(R.string.login_see_product));
        } else {
            product_product_wenjuan.setText(getResources().getString(R.string.edit_risk_question));
        }
        product_product_riskevalust.setVisibility(TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType()) ? View.VISIBLE : View.GONE);
        productlsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_SIHANG_CP);
        //是否需要风险评测d 弹出框
        product_product_riskevalust.setVisibility(TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType()) ? View.VISIBLE : View.GONE);
        productlsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_SIHANG_CP);
    }

    /**
     * 初始化是否IM分享过来的view
     */
    private void initShareProductView() {
        fromShare = getArguments() != null && getArguments().getBoolean(FROM_SEND_PRODUCT, false);
        productProductFilterLay.setVisibility(fromShare ? View.GONE : View.VISIBLE);
        seriesLayout.setVisibility(fromShare ? View.GONE : View.VISIBLE);
    }

    /**
     * 打开分享产品对话框
     */
    private void openShareProductDialog(int position) {
        final int finalPosition = position;
        String series = productlsBeen.get(finalPosition).series;
        new MsgSentDialog(getActivity(), productlsBeen.get(finalPosition).productName, AppManager.getChatName(getContext()), Series.formatSeries(series), productlsBeen.get(finalPosition).productId) {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right(String extra) {
                this.dismiss();
                RxBus.get().post(RxConstant.SHARE_PRODUCT_SEND, productlsBeen.get(finalPosition));
                getActivity().finish();
            }

        }.show();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        initShareProductView();
        loadingDialog = LoadingDialog.getLoadingDialog(baseActivity, getString(R.string.getproductloading), false, false);
        product_product_riskevalust = mFragmentView.findViewById(R.id.product_product_riskevalust);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        product_product_wenjuan = (TextView) product_product_riskevalust.findViewById(R.id.product_product_wenjuan);
        swipeToLoadLayout.setOnRefreshListener(this);
        productProductfragmentProductserieslayout.setInit(true);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentProductrecyclerView.setLayoutManager(linearLayoutManager);
        productlsAdapter = new ProductlsAdapter(baseActivity, null);
        fragmentProductrecyclerView.addItemDecoration(new SimpleItemDecoration(baseActivity, R.color.transparent, R.dimen.ui_10_dip));
        fragmentProductrecyclerView.setAdapter(productlsAdapter);
        fragmentProductrecyclerView.addItemDecoration(new RecycleViewDivider(
                getContext(),
                LinearLayoutManager.VERTICAL,
                DimensionPixelUtil.dip2px(getContext(), 5),
                getResources().getColor(R.color.gray_font)
        ));
        productlsAdapter.setOnItemClickListener(new ProductlsAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (productlsAdapter.getItemViewType(position) == productlsAdapter.CHECKOLD) {
                    productlsAdapter.destoryFootView();
                    reSetConditionAction();
                    TrackingDataUtils.save(baseActivity,"1010011081","");
                    onLineProduct = false;
                } else {
                    if (fromShare) {
                        openShareProductDialog(position);
                    } else {
                        ProductlsBean productlsBean = productlsAdapter.getBeanList().get(position);
                        ProductNavigationUtils.startProductDetailActivity(baseActivity, productlsBean.schemeId, productlsBean.productName, 100);
                        DataStatistApiParam.onStatisToCProductItemClick(productlsBean.productId, productlsBean.shortName, "1".equals(productlsBean.isHotProduct));
                    }
                    TrackingDataUtils.save(baseActivity, "1010010101", productlsBeen.get(position).productName +"|"+ productlsBeen.get(position).schemeId);
                }

            }
        });
        fragmentProductrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollThreshold;
            boolean isScorlling;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
//                    case RecyclerView.SCROLL_STATE_IDLE:
                        isScorlling = true;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isScorlling = false;
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (isSignificantDelta && isScorlling && !fromShare) {
                    if (dy > 30) {//上滑动
                        if (productProductFilterLay.getVisibility() == View.VISIBLE)
                            productProductFilterLay.setVisibility(View.GONE);
                    }
                    if (dy < -5) {//下互动
                        if (productProductFilterLay.getVisibility() == View.GONE)
                            productProductFilterLay.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        CurrentSeries = "0";//默认系列是全部  0代表全部
        CurrentOderBy = "";//默认排序是没有的  这样做是传递空的话 后台会返回默认的排序方式
        if (!BStrUtils.isEmpty(CacheInvestor.getProductFilterCache(baseActivity))) {//判断是否有筛选条件的缓存
            productFilterBean = new Gson().fromJson(CacheInvestor.getProductFilterCache(baseActivity), ProductFilterBean.class);
            //如果哦有缓存 就初始化筛选条件
            initFilterDate(productFilterBean.getSeries().getItems());
        }
//        blurringView.setBlurredView(swipeToLoadLayout);
//        blurringView.invalidate();
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
                CurrentOffset = 0;
                isLoadmore = false;
//                onLineProduct = true;
                productProductfragmentPaixu.setTextColor(getResources().getColor(R.color.app_golden));
                reSetConditionAction();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        refreshObserable = RxBus.get().register(ProductPresenter.REFRESH_PRODUCT, Boolean.class);

        refreshObserable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                initConfig();
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
                CurrentOffset = 0;
                isLoadmore = false;
//                onLineProduct = true;

                productProductfragmentShaixuan.setTextColor(getResources().getColor(isHaveFilter(CurrentFilter) ? R.color.app_golden : R.color.black));
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

        if (null != refreshObserable)
            RxBus.get().unregister(ProductPresenter.REFRESH_PRODUCT, refreshObserable);

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

    /**
     * 无网络重新加载
     */
    @OnClick(R2.id.reload)
    public void reload() {
        CurrentOffset = 0;
        reSetConditionAction();
//        onLineProduct = true;
    }


    @OnClick(R2.id.product_productfragment_paixu)
    public void onProductProductfragmentPaixuClicked() {
        if (TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {
            return;
        }
        if (null != orderbyPop && orderbyPop.isShowing()) {
            orderbyPop.dismiss();
            return;

        }
//        if (!BStrUtils.isEmpty(CurrentOderBy))
        orderbyPop = new OrderbyPop(baseActivity, productFilterBean.getOrderBy().getItems(), CurrentOderBy);
        if (Build.VERSION.SDK_INT < 24) {
            orderbyPop.showAsDropDown(productProductfragmentPaixu);
        } else {

            int[] location = new int[2];
            productProductfragmentPaixu.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            orderbyPop.showAtLocation(productProductfragmentPaixu, Gravity.NO_GRAVITY, 0, y + productProductfragmentPaixu.getHeight());
        }
        TrackingDataUtils.save(baseActivity, "1010010051", "");

    }

    @OnClick(R2.id.product_productfragment_shaixuan)
    public void onProductProductfragmentShaixuanClicked() {
        if (TextUtils.isEmpty(AppManager.getUserInfo(baseActivity).getToC().getCustomerType())) {
            return;
        }
        if (null == filterPop)
            filterPop = new FilterPop(baseActivity, CurrentFilter);
        if (null == CurrentFilter) {
            PromptManager.ShowCustomToast(baseActivity, getResources().getString(R.string.nofiltedate));
            return;
        }

        if (Build.VERSION.SDK_INT < 24) {
            filterPop.showAsDropDown(productProductfragmentPaixu, 0, 20, CurrentFilter);
        } else {

            int[] location = new int[2];
            productProductfragmentPaixu.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            filterPop.showAtLocation(productProductfragmentPaixu, Gravity.NO_GRAVITY, 0, y + productProductfragmentPaixu.getHeight() + 2);
        }
        TrackingDataUtils.save(baseActivity, "1010010061", "");
    }


    @Override
    public void getDataSucc(int Type, String str) {
        clodLsAnim(swipeToLoadLayout);
        load_error.setVisibility(View.GONE);
        reload.setVisibility(View.GONE);
        switch (Type) {
            case ProductContract.LOAD_FILTER://获取到的筛选条件
                productFilterBean = new Gson().fromJson(str.trim(), ProductFilterBean.class);
                initFilterDate(productFilterBean.getSeries().getItems());
                CurrentFilter = productFilterBean.getFilter();
                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://获取到列表数据
//                PromptManager.ShowCustomToast(getContext(), "请求列表成功" + str);
                //开始解析数据
                productProductfragmentEmptyIv.setVisibility(View.GONE);
                productlsBeen = new Gson().fromJson(str, new TypeToken<List<ProductlsBean>>() {
                }.getType());
                boolean disOnLine = false;
                for (int i = 0; i < productlsBeen.size(); i++) {
                    try {
                        long l = dateFormat.parse(productlsBeen.get(i).raiseEndTime).getTime() - System.currentTimeMillis();
                        if (l < 0 && onLineProduct) {
                            productlsBeen.remove(i);
                            --i;
                            disOnLine = true;
                        } else if (!productlsBeen.get(i).state.equals("50") && onLineProduct) {
                            productlsBeen.remove(i);
                            --i;
                            disOnLine = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (disOnLine) {
//                    onLineProduct = false;
                    ProductlsBean productlsBean = new ProductlsBean();
                    productlsBean.productName = String.format("%d", productlsAdapter.CHECKOLD);
                    productlsBeen.add(productlsBean);
//                    swipe_load_more_footer.setVisibility(View.GONE);
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
//                    swipe_load_more_footer.setVisibility(View.VISIBLE);
                }
                fragmentProductrecyclerView.setBackground(getResources().getDrawable(R.drawable.shape_null));

                productProductfragmentEmptyIv.setVisibility(View.GONE);
                swipeToLoadLayout.setVisibility(View.VISIBLE);
                if (isLoadmore) {
                    productlsAdapter.AddfreshAp(productlsBeen);
                    if (null == productlsBeen || 0 == productlsBeen.size()) {
                        PromptManager.ShowCustomToast(baseActivity, getResources().getString(R.string.no_more_product));
                    }
                } else {
                    if (0 == productlsBeen.size()) {
                        productProductfragmentEmptyIv.setVisibility(View.VISIBLE);
                        swipeToLoadLayout.setVisibility(View.GONE);
                        if (productProductFilterLay.getVisibility() == View.GONE)
                            productProductFilterLay.setVisibility(View.VISIBLE);
                    }
                    productlsAdapter.freshAp(productlsBeen);

                }

                break;
        }
        isLoadmore = false;
    }

    @Override
    public void getDataFail(int Type, String str) {
        clodLsAnim(swipeToLoadLayout);
//        PromptManager.ShowCustomToast(getContext(), str);
        switch (Type) {
            case ProductContract.LOAD_FILTER://请求筛选条件失败
                load_error.setVisibility(View.GONE);
                reload.setVisibility(View.GONE);
                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://请求产品列表成功
                if (!isLoadmore) {
                    productProductfragmentEmptyIv.setVisibility(View.VISIBLE);
                    swipeToLoadLayout.setVisibility(View.GONE);
                    load_error.setVisibility(View.GONE);
                    reload.setVisibility(View.GONE);
                }
                break;
            case ProductContract.NET_ERROR: //网络加载失败
                productProductfragmentEmptyIv.setVisibility(View.VISIBLE);
                swipeToLoadLayout.setVisibility(View.GONE);
                productProductfragmentEmptyIv.setImageResource(R.drawable.net_err_tip);
                load_error.setText(baseActivity.getResources().getString(R.string.err_net_str));
                load_error.setVisibility(View.VISIBLE);
                reload.setVisibility(View.VISIBLE);


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
                    Series series = (Series) textView.getTag();
                    CurrentSeries = ((Series) textView.getTag()).getKey();
                    isInitData = true;
//                    reSetConditionAction();
//                    onLineProduct = true;
                    resetSeries();
                    //埋点
                    if (null != series)
                        DataStatistApiParam.onStatisToCProductTabTag(series.getName());
                }
                TrackingDataUtils.save(baseActivity, "1010010071", textView.getText().toString());
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

    private void resetSeries() {
        getPresenter().getProductDataOffset(isInitData ? loadingDialog : null, 0, CurrentSeries, CurrentOderBy, CurrentFilter);
        isInitData = false;
    }

    public void setParamFiLter(String currentSeries) {
        getPresenter().getProductDataOffset(loadingDialog, 0, currentSeries, null, null);
    }

    /**
     * 点击系列 点击只能排序 点击筛选 的确定
     * 开始重新请求数据
     * 第一次初始化数据才加载dialog 非第一次不需要加载dialog
     */
    private void reSetConditionAction() {
//        CurrentOffset = productlsAdapter.getItemCount();
        getPresenter().getProductDataOffset(isInitData ? loadingDialog : null, CurrentOffset, CurrentSeries, CurrentOderBy, CurrentFilter);
        isInitData = false;
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
        if (productlsAdapter.getFootCout() == 0) {
            CurrentOffset = productlsAdapter.getItemCount();
            isLoadmore = true;
            reSetConditionAction();
        }
        TrackingDataUtils.save(baseActivity, "1010010103", "");
    }

    @Override
    public void onRefresh() {
        CurrentOffset = 0;
        reSetConditionAction();
//        onLineProduct = true;
        TrackingDataUtils.save(baseActivity, "1010010102", "");
    }

    /**
     * 重新整理数据
     */
    public void resetAllData() {
        CurrentOffset = 0;
        CurrentSeries = "0";//默认系列是全部  0代表全部
        CurrentOderBy = "";//默认排序是没有的  这样做是传递空的话 后台会返回默认的排序方式
        isLoadmore = false;
//        onLineProduct = true;

        for (FilterItem h : CurrentFilter) {
            for (int i = 0; i < h.getItems().size(); i++) {
                h.getItems().get(i).setChecked(false);
            }
        }
        BStrUtils.SetTxt(productProductfragmentPaixu, getResources().getString(R.string.zhinengpaixu));
        productProductfragmentPaixu.setTextColor(getResources().getColor(R.color.black));
        productProductfragmentShaixuan.setTextColor(getResources().getColor(R.color.black));
        orderbyPop = null;
        filterPop = null;
        if (null != productFilterBean) {
            productProductfragmentProductserieslayout.setInit(true);
            initFilterDate(productFilterBean.getSeries().getItems());
        }
        reSetConditionAction();
    }

    //问卷调查
    @OnClick(R2.id.product_product_wenjuan)
    public void onViewClicked() {
        if (AppManager.isVisitor(getActivity())) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("insidegotologin", true);
            NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_LOGIN, map);
            DataStatistApiParam.productLogin();
        } else {
            Router.build(RouteConfig.GOTO_APP_RISKEVALUATIONACTIVITY).go(baseActivity);
        }
    }

    /**
     * 判断是否存在筛选条件
     */
    public boolean isHaveFilter(List<FilterItem> data) {
        boolean ishaveFilter = false;
        for (FilterItem h : CurrentFilter) {
            for (int i = 0; i < h.getItems().size(); i++) {
                if (h.getType().equals("text") && !BStrUtils.isEmpty(h.getMinNumber()) && !BStrUtils.isEmpty(h.getMaxNumber()))
                    ishaveFilter = true;
                else if (h.getItems().get(i).isChecked()) ishaveFilter = true;
                else ;
            }
        }
        return ishaveFilter;
    }
}
