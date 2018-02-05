package com.cgbsoft.privatefund.public_fund;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-29.
 *
 * 买公募基金的Presenter
 */

public class BuyPublicFundPresenter extends BasePublicFundPresenter {
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
     * 获取申购需要数据
     *
     */
    public void getData(String fundCode, BasePublicFundPresenter.PreSenterCallBack callBack){
        ApiClient.getPublicFundConfig(fundCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                 parseResultFormServer(s,callBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                if(error instanceof ApiException && callBack!=null){
                    callBack.field(((ApiException) error).getCode(),error.getMessage());
                }
                error.printStackTrace();
            }
        });
    }


    public  interface AsynCallBack {
        void bankList(BankListOfPublicListBean bankListOfPublicListBean);
    }


    public void sure(BuyPublicFundActivity.Bean bean,String money,String paswd,PreSenterCallBack preSenterCallBack){
         /* trantype: 'orderAndPay',
                custno: '155', //客户号
                fundcode: 'DC0011', //基金代码（H5调取app指令的时候会传入）
                sharetype: 'B', //份额类别 'A'前收费 'B' 后收费（H5调取app指令的时候会传入）
                tano: 'DC', //TA代码（H5调取app指令的时候会传入）
                certificatetype: '0', //证件类型（H5调取app指令的时候会传入）
                certificateno: '120101198303093538', //身份证号（H5调取app指令的时候会传入）
                depositacctname: '何美福', //客户姓名（H5调取app指令的时候会传入）
            @    applicationamt: "10000000", //认/申购金额
                businesscode: "22", //业务类型，认购或申购（H5调取app指令的时候会传入）
                buyflag: "1", / 制购买标志，1为强制购买（H5调取app指令的时候会传入）
      @  callbackurl: "", //银行回调地址，留空即可
                depositacct: "6226090102934672", //客户银行卡号（H5调取app指令的时候会传入）
                fundname: "江恒领先成长2", //基金名称（H5调取app指令的时候会传入）
                fundtype: "k", //基金类型，0-股票型基金 1-债券型基金 2-货币型基 金 3-混合型基金 4-专户基金 5-指数型基金 6-QDII 基金（H5调取app指令的时候会传入）
                mobiletelno: "13701355892", //客户手机号（H5调取app指令的时候会传入）
        @        tpasswd: "123456", //交易密码，这需要客户自己输入
                transactionaccountid: '234234324',//交易账户id（从银行卡列表信息中获取）
                channelid: '支付网点id', //支付网点id（从银行卡列表信息中获取）
                branchcode: '份额托管网点编号', //份额托管网点编号（从银行卡列表信息中获取）
                moneyaccount: '资金账户', //资金账户（从银行卡列表信息中获取）
                paycenterid: '支付网点所属中心', //支付网点所属中心（从银行卡列表信息中获取）
                riskwarnflag: '1' //已经阅读风险提示标志，传死1即可
*/
        Map<String,Object> parms = new HashMap<>();
        parms.put("trantype","orderAndPay");
        parms.put("fundcode",bean.getFundCode());
        parms.put("fundname",bean.getFundName());
        parms.put("fundtype",bean.getFundtype());
        parms.put("sharetype",bean.getSharetype());
        parms.put("tano",bean.getTano());


        parms.put("certificatetype", AppManager.getPublicFundInf(getContext()).getCertificatetype());
        parms.put("certificateno", AppManager.getPublicFundInf(getContext()).getCertificateno());
        parms.put("depositacctname", AppManager.getPublicFundInf(getContext()).getDepositacctname());
        parms.put("custno", bean.getBankCardInfo().getCustno());

        parms.put("buyflag",bean.getBuyflag());


        parms.put("depositacct", bean.getBankCardInfo().getDepositacct());
        parms.put("mobiletelno",AppManager.getPublicFundInf(getContext()).getMobileno());
        parms.put("transactionaccountid",bean.getBankCardInfo().getTransactionaccountid());
        parms.put("channelid",bean.getBankCardInfo().getChannelid());
        parms.put("moneyaccount",bean.getBankCardInfo().getMoneyaccount());
        parms.put("paycenterid",bean.getBankCardInfo().getPaycenterid());
        parms.put("branchcode",bean.getBankCardInfo().getBranchcode());
        parms.put("riskwarnflag","1");
        parms.put("callbackurl","");
        parms.put("businesscode",""); // 认申购


        parms.put("applicationamt",money); // 认申购金额
        parms.put("tpasswd",paswd); // 交易密码

        super.getFundDataFormJZ(parms,preSenterCallBack);
    }

}
