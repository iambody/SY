package com.cgbsoft.privatefund.public_fund;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * Created by wangpeng on 18-1-29.
 *
 * 买公募基金的Presenter
 */

public class BuyPublicFundPresenter extends BasePresenterImpl {
    private BuyPublicFundModle buyPublicFundModle = new BuyPublicFundModle();

    public BuyPublicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }


    /**
     * 支付
     * @param pwd
     */
    public void pay(String pwd,PayOfBuyPublicBean payOfBuyPublicBean){

    }

    /**
     *  解析从H5传来的数据数据
     * @param string
     * @return
     */
    public PayOfBuyPublicBean parseDataFormH5(String string){
         buyPublicFundModle.setDataFormH5(string);
         return buyPublicFundModle.getDataFormH5();
    }





    /**
     * 获取客户的银行列表
     */
    public void getBandListData(){

    }


    public  interface AsynCallBack {
        void bankList(BankListOfPublicListBean bankListOfPublicListBean);
    }


}
