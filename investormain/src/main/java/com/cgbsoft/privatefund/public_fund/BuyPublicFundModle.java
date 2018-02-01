package com.cgbsoft.privatefund.public_fund;


import com.google.gson.Gson;

import java.util.List;

/**
 * Created by wangpeng on 18-1-29.
 */

public class BuyPublicFundModle {
    private String dataFormH5;
    private PayOfBuyPublicBean payOfBuyPublicBean;
    private List<BankListOfPublicListBean> bankListOfPublicListBeans;

    public void setDataFormH5(String data){
        this.dataFormH5 = data;
    }

    public PayOfBuyPublicBean getDataFormH5(){
        if(payOfBuyPublicBean != null) return payOfBuyPublicBean;
        return null;
    }

    /**
     * 获取银行列表的请求参数
     * @return
     */
    public String getBandListRequestParms(){
        ReqeustListParmBean reqeustListParmBean = new ReqeustListParmBean();
        reqeustListParmBean.setCustno(this.payOfBuyPublicBean.getCustno());
        return new Gson().toJson(reqeustListParmBean);
    }

    /**
     *  保存客户的银行卡列表信息
     * @param data 网络请求回来的银行卡数据
     */
    public void  saveBandListOfPublicFundBean(String data){

    }

    /**
     *  获取一个银行卡信息
     * @param index 银行卡列表中的位置
     * @return
     */
    public BankListOfPublicListBean getBankInfoOfPublicFund(int index){
        if(bankListOfPublicListBeans != null) bankListOfPublicListBeans.get(index);
        return null;
    }

}
