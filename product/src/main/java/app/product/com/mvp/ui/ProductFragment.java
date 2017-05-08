package app.product.com.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.presenter.ProductPresenter;
import app.product.com.widget.ProductSeriesLayout;
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



    @Override
    protected int layoutID() {
        return R.layout.fragment_product_product;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        initView();
        getPresenter().getProductFilterData(productProductfragmentProductserieslayout);
    }

    /**
     * 初始化view
     */
    private void initView() {
        productProductfragmentProductserieslayout.setInit(true);
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
                PromptManager.ShowCustomToast(baseActivity, "页面中得到回调" + str);
                break;
            case ProductContract.LOAD_PRODUCT_LISTDATA://获取到列表数据
                break;
        }
    }

    @Override
    public void getProductDataFail(int Type, String str) {

    }



}
