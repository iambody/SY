package app.mall.com.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.ArrayList;

import app.mall.com.model.MallAddressBean;

/**
 * desc  商城地址编辑
 * Created by yangzonghui on 2017/5/10 12:45
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface MallContract {

    interface Presenter extends BasePresenter {
        //编辑商城收货地址
        void saveMallAddress(MallAddressBean model);

        //新增商城收货地址
        void addMallAddress(MallAddressBean model);

        //删除收货地址
        void deleteMallAddress(String id);

        //获取收货地址列表
        void getMallAddressList();

        //设置默认地址
        void setDefaultAddress(String id);
    }


    interface View extends BaseView {

        void saveAddressSucc(MallAddressBean model);

        void saveAddressErr(String s);

        void getMallAddressLitSuc(ArrayList<MallAddressBean> list);

        void deleteSuc(String id);

        void setDefaultSuc(String id);

        void addAddressSuc(MallAddressBean mallAddressBean);
    }

}
