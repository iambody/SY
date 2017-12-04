package app.product.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

import java.util.List;

import app.product.com.model.FilterItem;
import app.product.com.mvp.contract.ProductContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-18:55
 */
public class ProductDetailPresenter extends BasePresenterImpl<ProductContract.view>implements ProductContract.Presenter {
    public ProductDetailPresenter(@NonNull Context context, @NonNull ProductContract.view view) {
        super(context, view);
    }

    @Override
    public void getProductData(LoadingDialog loadingDialo,int offset, String series, String orderBy, List<FilterItem> datas) {

    }

    @Override
    public void getProductDataOffset(LoadingDialog loadingDialog, int offset, String series, String orderBy, List<FilterItem> datas) {

    }

    @Override
    public void getProductFilterData() {

    }
}
