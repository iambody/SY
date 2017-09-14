package app.mall.com.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.ElegantGoodsBeanInterface;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.DividerGridItemDecoration;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecorationHorizontal;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.mall.com.mvp.adapter.ElegantGoodsMultAdapter;
import app.mall.com.mvp.adapter.ElegantGoodsRecyclerAdapter;
import app.mall.com.mvp.contract.ElegantGoodsContract;
import app.mall.com.mvp.presenter.ElegantGoodsPresenterImpl;
import butterknife.BindView;
import qcloud.mall.R;
import qcloud.mall.R2;


/**
 * 尚品页面
 * Created by sunfei on 2017/6/29 0029.
 */

public class ElegantGoodsFragment extends BaseFragment<ElegantGoodsPresenterImpl> implements ElegantGoodsContract.ElegantGoodsView,OnLoadMoreListener, OnRefreshListener {
    @BindView(R2.id.elegant_goods_rv)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R2.id.swipe_target)
    RecyclerView recyclerViewPros;
    @BindView(R2.id.ll_category_all)
    LinearLayout categoryAll;
    private LinearLayoutManager linearLayoutManager;
    private List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categoryDatas=new ArrayList<>();
    private List<ElegantGoodsBeanInterface> prosDatas=new ArrayList<>();
    private LoadingDialog mLoadingDialog;
//    private int offset=0;
//    private int offsetMore=0;
    private final String CATEGORY_ALL = "300200";
    private String category = CATEGORY_ALL;//记录被点击的分类标记，默认是全部分类
    private String categoryStr = "全部";//记录被点击的分类标记，默认是全部分类
    private ElegantGoodsRecyclerAdapter categoryAdapter;
    private ElegantGoodsMultAdapter proAdapter;
    private boolean isOver;//是否已经加载全部数据
//    private boolean isCategorySelect;//是否是选择分类后去请求的数据

    @Override
    protected int layoutID() {
        return R.layout.elegantgoods_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_SHENGHUO_SP);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_SHENGHUO_SP);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        DataStatistApiParam.intoElegantGoods();
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleItemDecorationHorizontal(baseActivity,android.R.color.transparent,R.dimen.ui_10_dip));
        recyclerView.setHasFixedSize(true);
        categoryAdapter = new ElegantGoodsRecyclerAdapter(baseActivity, categoryDatas);
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setCategoryItemClickListener(new ElegantGoodsRecyclerAdapter.CategoryItemClickListener() {
            @Override
            public void onCategoryItemClick(View view,int oldPosition, int position, ElegantGoodsEntity.ElegantGoodsCategoryBean posBean) {
                LogUtils.Log("aaa","click item");
                loadCategory(posBean);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setLoadMoreEnabled(false);
        DividerGridItemDecoration dividerGridItemDecoration = new DividerGridItemDecoration(baseActivity, R.drawable.elegant_divider);
        recyclerViewPros.addItemDecoration(dividerGridItemDecoration);
        recyclerViewPros.setHasFixedSize(true);
        proAdapter = new ElegantGoodsMultAdapter(baseActivity, prosDatas);
        recyclerViewPros.setAdapter(proAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseActivity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = proAdapter.getItemViewType(position);
                if (itemViewType == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerViewPros.setLayoutManager(gridLayoutManager );
        proAdapter.setOnGoodsClickListener(new ElegantGoodsMultAdapter.OnGoodsClickListener() {
            @Override
            public void onClick(ElegantGoodsBeanInterface bean, boolean isHot) {
                String id;
                String name;
                String hotOrNew = "";
                if (isHot) {//热门清单产品
                    ElegantGoodsEntity.HotListItemBean clickBean = (ElegantGoodsEntity.HotListItemBean) bean;
                    name=clickBean.getGoodsName();
                    id=clickBean.getId();
                    hotOrNew = "热门";
                } else {//全部产品
                    ElegantGoodsEntity.AllNewsItemBean clickBean = (ElegantGoodsEntity.AllNewsItemBean) bean;
                    id=clickBean.getId();
                    name=clickBean.getGoodsName();
                    hotOrNew = "最新";
                }
                    DataStatistApiParam.clickProduct(name,categoryStr,hotOrNew);
//                Toast.makeText(baseActivity.getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                HashMap hashMap = new HashMap();
                hashMap.put(WebViewConstant.RIGHT_SHARE, true);
                hashMap.put(WebViewConstant.push_message_title, getResources().getString(R.string.product_detail));
                hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.elegantGoodsDetail+id);
                NavigationUtils.startActivityByRouter(getContext(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
            }
        });
        recyclerViewPros.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollThreshold;
            boolean isScorlling;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.Log("aaa","newState==="+newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
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
                if (isSignificantDelta && isScorlling ) {
                    if (dy > 30) {//上滑动
                        if (categoryAll.getVisibility() == View.VISIBLE)
                            categoryAll.setVisibility(View.GONE);
                    }
                    if (dy < -5) {//下互动
                        if (categoryAll.getVisibility() == View.GONE)
                            categoryAll.setVisibility(View.VISIBLE);
                    }
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    if (categoryAll.getVisibility() == View.GONE)
                        categoryAll.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 点击分类时，去请求相应分类的数据
     * @param posBean
     */
    private void loadCategory(ElegantGoodsEntity.ElegantGoodsCategoryBean posBean) {
        mRefreshLayout.setLoadMoreEnabled(false);
        category=posBean.getCode();
        categoryStr=posBean.getTitle();
        DataStatistApiParam.clickCategory(categoryStr);
        isOver=false;
//        LogUtils.Log("aaa","click item is==="+category);
        prosDatas.clear();
        proAdapter.notifyDataSetChanged();
        if (category.equals(CATEGORY_ALL)) {
            getPresenter().getElegantGoodsFirst(prosDatas.size());
        } else {
            getPresenter().getElegantGoodsMore(prosDatas.size(),category);
        }
    }

    /**
     * 懒加载，在fragment显示的时候才去请求数据，只会加载一次，若想每次切换都加载数据，则设置{@linksetIsLoad(false)}方法
     */
    @Override
    protected void loadData() {
        getPresenter().getElegantGoodsFirst(prosDatas.size());
//        setIsLoad(false);
    }

    @Override
    protected ElegantGoodsPresenterImpl createPresenter() {
        return new ElegantGoodsPresenterImpl(baseActivity,this);
    }

    @Override
    public void showLoadDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void updateUi(List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categorys,List<ElegantGoodsBeanInterface> result) {
        clodLsAnim(mRefreshLayout);
        mRefreshLayout.setLoadMoreEnabled(true);
        categoryAdapter.setDatas(categorys);
        prosDatas.clear();
        prosDatas.addAll(result);
        if (prosDatas.size()==0) {
            mRefreshLayout.setLoadMoreEnabled(false);
        }
        proAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateUiMore(List<ElegantGoodsBeanInterface> allRows) {
        mRefreshLayout.setLoadMoreEnabled(true);
        clodLsAnim(mRefreshLayout);
            if (allRows.size() == 0||allRows.size() < Constant.LOAD_ELEGANT_GOODS_MORE_lIMIT) {
                isOver=true;
            }
        prosDatas.addAll(allRows);
        if (prosDatas.size()==0) {
            mRefreshLayout.setLoadMoreEnabled(false);
        }
        proAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateFirstError(Throwable error) {
        showToast(R.string.load_fail);
        clodLsAnim(mRefreshLayout);
    }

    @Override
    public void updateMoreError(Throwable error) {
//        LogUtils.Log("aaa","click item is not 300200---updateMoreError");
        showToast(R.string.load_fail);
        clodLsAnim(mRefreshLayout);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        recyclerView.setEnabled(false);
        mRefreshLayout.setLoadMoreEnabled(false);
        isOver=false;
        prosDatas.clear();
        if (category.equals(CATEGORY_ALL)) {
            getPresenter().getElegantGoodsFirst(prosDatas.size());
        } else {
            getPresenter().getElegantGoodsMore(prosDatas.size(),category);
        }
        recyclerView.setEnabled(true);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        if (isOver) {
            Toast.makeText(baseActivity.getApplicationContext(),"已经加载全部",Toast.LENGTH_SHORT).show();
            clodLsAnim(mRefreshLayout);
            return;
        }
//        offsetMore+=LOAD_ELEGANT_GOODS_MORE_lIMIT;
        getPresenter().getElegantGoodsMore(prosDatas.size(),category);
    }

    @Override
    protected void viewBeShow() {
        super.viewBeShow();
        DataStatistApiParam.intoElegantGoods();
    }
}
