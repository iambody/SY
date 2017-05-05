package app.product.com.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.product.com.mvp.contract.ProductContract;
import app.product.com.mvp.ui.adapter.ProductAdapter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/5-21:22
 */
public class ProductPresenter extends BasePresenterImpl<ProductContract.view> implements ProductContract.Presenter {


    public ProductPresenter(@NonNull Context context, @NonNull ProductContract.view view) {
        super(context, view);
    }

    @Override
    public void getProductData(ProductAdapter adapter, boolean isRef) {

    }
}
