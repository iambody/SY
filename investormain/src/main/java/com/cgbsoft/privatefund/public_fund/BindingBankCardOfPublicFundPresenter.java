package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.tools.BStrUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-30.
 */

public class BindingBankCardOfPublicFundPresenter extends BasePublicFundPresenter {

    public BindingBankCardOfPublicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }


    /**
     * 530335 获取支持的银行列表
     * <p>
     * {
     * trantype: '530335',
     * custno: '155',  //客户号（H5调取app指令的时候会传入）
     * planflag: ''  //留空即可
     * }
     */
    public void getBinidedBankList(String custno,PreSenterCallBack<String> preSenterCallBack) {
        super.handlerPublicFundResult(ApiClient.getUseableBankList(custno),preSenterCallBack);
     /*   Map<String, Object> parms = new HashMap<>();
        parms.put("trantype", "530335");
        parms.put("custno", custno); // TODO //证件类型（H5调取app指令的时候会传入） BStrUtils.nullToEmpty(bindingBankCardBean.getCustno())
        parms.put("planflag", "");

        super.getFundDataFormJZ(parms, preSenterCallBack);*/
    }

    /**
     * 通知服务器发送验证码手机验证码
     * <p>
     * {
     * trantype: 'bgMsgSend',
     * certificatetype: '0', //证件类型（H5调取app指令的时候会传入）
     * certificateno: '120101198303093538', //身份证号（H5调取app指令的时候会传入）
     * depositacctname: '何美福', //投资人姓名（H5调取app指令的时候会传入）
     * depositacct: '银行卡号', //银行卡号（客户填写）
     * mobiletelno: '13700000000' //银行卡绑定的手机号（客户填写）
     * }
     */
    public void getVerificationCodeFormServer(BindingBankCardBean bindingBankCardBean, String phone, String bankCode, BasePublicFundPresenter.PreSenterCallBack<String> preSenterCallBack) {
        Map<String, String> parms = new HashMap<>();
       /* parms.put("certificateType", bindingBankCardBean.getCertificatetype());//证件类型（H5调取app指令的时候会传入）
        parms.put("certificateNo", bindingBankCardBean.getCertificateno());*/
        parms.put("channelId", bindingBankCardBean.getChannelid());
       // parms.put("depositAcctName", bindingBankCardBean.getDepositacctname());
        parms.put("depositAcct", bankCode);
        parms.put("mobileTelNo", phone);
        super.handlerPublicFundResult(ApiClient.getBindCardCaptcha(parms),preSenterCallBack);
        //super.getFundDataFormJZ(parms, preSenterCallBack);
    }


    /**
     * 确定绑定
     *
     * @param
     * @param callBack
     */
    public void sureBind(BindingBankCardBean bindingBankCardBean, String bankName, String bankCode, String phoneCode, String verificationCode, PreSenterCallBack<String> callBack) {
        Map parms = new HashMap();
        parms.put("custNo", BStrUtils.nullToEmpty(bindingBankCardBean.getCustno()));
        parms.put("mobileNo", BStrUtils.nullToEmpty(phoneCode));
        parms.put("verificationCode", BStrUtils.nullToEmpty(verificationCode));
        parms.put("authenticateFlag", "");
      //  parms.put("bankname", BStrUtils.nullToEmpty(bankName));
        parms.put("channelId", BStrUtils.nullToEmpty(bindingBankCardBean.getChannelid()));
      //  parms.put("channelname", BStrUtils.nullToEmpty(bankName));
        parms.put("depositAcct", BStrUtils.nullToEmpty(bankCode));
        parms.put("depositAcctName", BStrUtils.nullToEmpty(bindingBankCardBean.getDepositacctname()));
        parms.put("depositName", BStrUtils.nullToEmpty(bindingBankCardBean.getDepositname()));
        parms.put("depositCity", ""); // 所在城市
        parms.put("depositProv", ""); // 所以省份
        parms.put("operOrg", BStrUtils.isEmpty(bindingBankCardBean.getOperorg()) ? "9999" : bindingBankCardBean.getOperorg());  //交易操作网点，写死9999就可以
        parms.put("tPasswd", BStrUtils.isEmpty(bindingBankCardBean.getTpasswd()) ? "" : bindingBankCardBean.getTpasswd());
        parms.put("certificateType", "0");

        parms.put("bankName", BStrUtils.nullToEmpty(bindingBankCardBean.getBankname()));
        parms.put("channelName", BStrUtils.nullToEmpty(bindingBankCardBean.getChannelname()));
        parms.put("paraType", BStrUtils.nullToEmpty(bindingBankCardBean.getParatype()));

        //获取身份证号
        String certificateno = bindingBankCardBean.getCertificateno();

        if (BStrUtils.isEmpty(certificateno)) {
            certificateno= AppManager.getPublicFundInf(getContext()).getCertificateNo();
        }

        parms.put("certificateNo", BStrUtils.isEmpty(certificateno)?"":certificateno);

        handlerPublicFundResult(ApiClient.bindCard(parms),callBack);

       // super.getFundDataFormJZ(parms, callBack);
    }


    public void getBranchBankInfo(String keyWords,String channelid,PreSenterCallBack preSenterCallBack){
        Map parms = new HashMap();
        parms.put("channelId", channelid);
        parms.put("paraCity", keyWords);
        super.handlerPublicFundResult(ApiClient.getBankBranch(parms),preSenterCallBack);
    }
}
