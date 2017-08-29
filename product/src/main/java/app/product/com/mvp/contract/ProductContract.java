package app.product.com.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

import java.util.List;

import app.product.com.model.FilterItem;
import app.product.com.mvp.ui.adapter.ProductAdapter;
import app.product.com.widget.ProductSeriesLayout;


/**
 * desc  ${DESC}
 * author wangyongkui
 * wangyongkui@simuyun.com
 * 日期 2017/5/5-21:11
 */
public class ProductContract {
    //回调得到筛选条件的标识
    public final static int LOAD_FILTER = 1;
    //回调得到产品列表的标识
    public final static int LOAD_PRODUCT_LISTDATA = 2;
    //网络错误
    public final static int NET_ERROR = 3;

    public interface Presenter extends BasePresenter {
        /**
         * @param offset         第几页
         * @param productType    产品类型 字符串数组
         * @param riskLevel      风险等级 字符串数组
         * @param income         预期收益 字符串数组
         * @param investmentArea 投资领域 字符串数组
         * @param orderBy        排序 字符串数组
         * @param series         系列 字符串数组
         */
        void getProductData(LoadingDialog loadingDialog,int offset, String series, String  orderBy, List<FilterItem> datas);


        void getProductFilterData(   );

    }

    public interface view extends BaseView {
        void getDataSucc(int Type, String str);

        void getDataFail(int Type, String str);
    }
}
