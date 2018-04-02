package com.cgbsoft.privatefund.public_fund;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-29.
 * <p>
 * 买公募基金的Presenter
 */

public class BuyPublicFundPresenter extends BasePublicFundPresenter {
    private BuyPublicFundModle buyPublicFundModle = new BuyPublicFundModle();

    public BuyPublicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }


    /**
     *  请求支付网点字典
     *
     */
    public void requestDictionary(BasePublicFundPresenter.PreSenterCallBack callBack) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trantype", "520020");
        hashMap.put("dictitem", "110080");
        addSubscription(ApiClient.directRequestJzServer(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                parseResultFormServer(s, callBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                if (error instanceof ApiException && callBack != null) {
                    callBack.field(((ApiException) error).getCode(), error.getMessage());
                }
                error.printStackTrace();
            }
        }));
    }

    /**
     * 解析从H5传来的数据数据
     *
     * @param string
     * @return
     */
    public PayOfBuyPublicBean parseDataFormH5(String string) {
        buyPublicFundModle.setDataFormH5(string);
        return buyPublicFundModle.getDataFormH5();
    }


    /**
     * 获取申购需要数据
     */
    public void getData(String fundCode, BasePublicFundPresenter.PreSenterCallBack callBack) {
        super.handlerPublicFundResult(ApiClient.getBuyInfo(fundCode),callBack);
   /*     ApiClient.getBuyInfo(fundCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                parseResultFormServer(s, callBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                if (error instanceof ApiException && callBack != null) {
                    callBack.field(((ApiException) error).getCode(), error.getMessage());
                }
                error.printStackTrace();
            }
        });*/
    }


    public void getNewBindedBankCord(String custno, String bankCordNum, BasePublicFundPresenter.PreSenterCallBack callBack) {
        HashMap hashMap = new HashMap<>();

        hashMap.put("trantype", "520102");
        hashMap.put("custno", custno); // 客户号
        hashMap.put("isall", ""); // TODo 传2还是传空
        hashMap.put("depositacct", bankCordNum); // 客户号

        ApiClient.postNewBankCordInfo(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                parseResultFormServer(s, callBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                if (error instanceof ApiException && callBack != null) {
                    callBack.field(((ApiException) error).getCode(), error.getMessage());
                }
                error.printStackTrace();

            }

        });
        // TODo
        super.handlerPublicFundResult(ApiClient.getOrderAndPay(hashMap),callBack);

}

    public void sure(BuyPublicFundActivity.Bean bean, BuyPublicFundActivity.BankCardInfo bankCardInfo, String money, String paswd, PreSenterCallBack preSenterCallBack) {
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
                highriskwarnflag:'1' 买最高风险等级产品
*/

        /**
         *
         String parseJS eval
         {
         "taNo":"21",
         "transactionAccountId":"Z017A00000267",
         "applicationAmt":"109",
         "tPasswd":"123456",
         "fundCode":"210012",
         "branchCode":"370"
         }
         */
     //   parms.put("fundName", bean.getFundName());
        // parms.put("fundType", bean.getFundtype());
        // parms.put("shareType", bean.getSharetype());


        //  parms.put("certificateType", AppManager.getPublicFundInf(getContext()).getCertificateType());
        //   parms.put("certificateNo", AppManager.getPublicFundInf(getContext()).getCertificateNo());
        //  parms.put("depositacctName", AppManager.getPublicFundInf(getContext()).getDepositAcctName());
        //  parms.put("custNo", bankCardInfo.getCustno());

        //   parms.put("buyFlag", bean.getBuyflag());


        //   parms.put("depositAcct", bankCardInfo.getDepositacct());
        //   parms.put("mobileTelNo", AppManager.getPublicFundInf(getContext()).getMobileNo());
        //   parms.put("channelId", bankCardInfo.getChannelId());
        //   parms.put("moneyAccount",bankCardInfo.getMoneyaccount());
        //   parms.put("payCenterId", bankCardInfo.getPaycenterid());
        //   parms.put("riskwarnflag", "1");
        //   parms.put("highriskwarnflag", "1");
        //  parms.put("callbackUrl", "");
        //  parms.put("businessCode", BStrUtils.isEmpty(bean.getBusinesscode()) ? "22" : bean.getBusinesscode());

        //{"taNo":"21","transactionAccountId":"Z017A00000267","applicationAmt":"109","tPasswd":"123456","fundCode":"210012","branchCode":"370"}

        Map<String, String> parms = new HashMap<>();
        parms.put("taNo", bean.getTaNo());
        parms.put("fundCode", bean.getFundCode());
        parms.put("transactionAccountId",bankCardInfo.getTransactionAccountId());

        parms.put("applicationAmt", money); // 认申购金额
        parms.put("branchCode",bankCardInfo.getBranchCode());
        parms.put("tPasswd", paswd); // 交易密码

        super.handlerPublicFundResult(ApiClient.getOrderAndPay(parms),preSenterCallBack);
        //super.getFundDataFormJZ(parms, preSenterCallBack);
    }

}
