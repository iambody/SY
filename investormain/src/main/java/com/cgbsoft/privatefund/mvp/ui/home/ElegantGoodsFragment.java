package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.ElegantGoodsBeanInterface;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.DividerGridItemDecoration;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecorationHorizontal;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.ElegantGoodsMultAdapter;
import com.cgbsoft.privatefund.adapter.ElegantGoodsRecyclerAdapter;
import com.cgbsoft.privatefund.mvp.contract.home.ElegantGoodsContract;
import com.cgbsoft.privatefund.mvp.presenter.home.ElegantGoodsPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.mvp.ui.MallAddressListActivity;
import butterknife.BindView;

/**
 * 尚品页面
 * Created by sunfei on 2017/6/29 0029.
 */

public class ElegantGoodsFragment extends BaseFragment<ElegantGoodsPresenterImpl> implements ElegantGoodsContract.ElegantGoodsView,OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.elegant_goods_rv)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerViewPros;
    private LinearLayoutManager linearLayoutManager;
    private List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categoryDatas=new ArrayList<>();
    private List<ElegantGoodsBeanInterface> prosDatas=new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    private int offset=0;
    private int offsetMore=0;
    private final String CATEGORY_ALL = "300200";
    private String category = CATEGORY_ALL;//记录被点击的分类标记，默认是全部分类
    private ElegantGoodsRecyclerAdapter categoryAdapter;
    private ElegantGoodsMultAdapter proAdapter;
    private boolean isOver;
    private boolean isCategorySelect;//是否是选择分类后去请求的数据

    @Override
    protected int layoutID() {
        return R.layout.elegantgoods_fragment;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
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
                Intent intent = new Intent(getActivity(), MallAddressListActivity.class);
                startActivity(intent);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);
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
                if (isHot) {//热门清单产品
                    ElegantGoodsEntity.HotListItemBean clickBean = (ElegantGoodsEntity.HotListItemBean) bean;
                    id=clickBean.getId();
                } else {//全部产品
                    ElegantGoodsEntity.AllNewsItemBean clickBean = (ElegantGoodsEntity.AllNewsItemBean) bean;
                    id=clickBean.getId();
                }
                Toast.makeText(baseActivity.getApplicationContext(),id,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 点击分类时，去请求相应分类的数据
     * @param posBean
     */
    private void loadCategory(ElegantGoodsEntity.ElegantGoodsCategoryBean posBean) {
        offsetMore=0;
        category=posBean.getCode();
        isOver=false;
//        LogUtils.Log("aaa","click item is==="+category);
        proAdapter.clean();
        if (category.equals(CATEGORY_ALL)) {
            getPresenter().getElegantGoodsFirst(offset);
        } else {
            isCategorySelect=true;
            getPresenter().getElegantGoodsMore(offsetMore,category);
        }
    }

    /**
     * 懒加载，在fragment显示的时候才去请求数据，只会加载一次，若想每次切换都加载数据，则设置{@linksetIsLoad(false)}方法
     */
    @Override
    protected void loadData() {
        getPresenter().getElegantGoodsFirst(offset);
//        setIsLoad(false);
    }

    @Override
    protected ElegantGoodsPresenterImpl createPresenter() {
        return new ElegantGoodsPresenterImpl(baseActivity,this);
    }

    @Override
    public void showLoadDialog() {
//        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
//        mLoadingDialog.dismiss();
    }

    @Override
    public void updateUi(List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categorys,List<ElegantGoodsBeanInterface> result) {
        clodLsAnim(mRefreshLayout);
        categoryAdapter.setDatas(categorys);
        proAdapter.addDatas(result,true);
    }

    @Override
    public void updateUiMore(List<ElegantGoodsBeanInterface> allRows) {
        clodLsAnim(mRefreshLayout);
        if (null == allRows||allRows.size() == 0) {
            offsetMore-=LOAD_ELEGANT_GOODS_MORE_lIMIT;
            offsetMore=offsetMore<=0?0:offsetMore;
            if (allRows.size() == 0) {
                isOver=true;
            }
            return;
        }
        if (allRows.size() < Constant.LOAD_ELEGANT_GOODS_MORE_lIMIT) {
            isOver=true;
        }
        boolean isRef;
        if (isCategorySelect||offsetMore == 0) {
            proAdapter.addDatas(allRows, true);
            isCategorySelect = false;
            isRef=true;
        } else {
            isRef=false;
            proAdapter.addDatas(allRows, false);
        }
    }

    @Override
    public void updateFirstError(Throwable error) {
        clodLsAnim(mRefreshLayout);
    }

    @Override
    public void updateMoreError(Throwable error) {
//        LogUtils.Log("aaa","click item is not 300200---updateMoreError");
        clodLsAnim(mRefreshLayout);
        if (isCategorySelect) {
            isCategorySelect=false;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        isOver=false;
        if (category.equals(CATEGORY_ALL)) {
            getPresenter().getElegantGoodsFirst(offset);
        } else {
            offsetMore=0;
            getPresenter().getElegantGoodsMore(offsetMore,category);
        }
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
        offsetMore+=LOAD_ELEGANT_GOODS_MORE_lIMIT;
        getPresenter().getElegantGoodsMore(offsetMore,category);
    }
}
