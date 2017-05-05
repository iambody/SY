package app.product.com.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import app.product.com.mvp.ui.adapter.ProductAdapter;


/**
 * desc  ${DESC}
 * author wangyongkui
 * wangyongkui@simuyun.com
 * 日期 2017/5/5-21:11
 */
public class ProductContract {

   public interface Presenter extends BasePresenter {

        void getProductData(ProductAdapter adapter, boolean isRef);

    }

    public  interface view extends BaseView {
        void getProductDataSucc(boolean isRef);

        void getProductDataFail(boolean isRef);
    }
}
