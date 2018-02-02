package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.view.BaseView;

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

     {
     trantype: '530335',
     custno: '155',  //客户号（H5调取app指令的时候会传入）
     planflag: ''  //留空即可
     }
     */
    public void getBinidedBankList(PreSenterCallBack<String> preSenterCallBack){
        Map<String,Object> parms = new HashMap<>();
        parms.put("trantype","530335");
        parms.put("custno","175"); // TODO //证件类型（H5调取app指令的时候会传入）
        parms.put("planflag","");

        super.getFundDataFormJZ(parms,preSenterCallBack);
    }

    /**
     *  通知服务器发送验证码手机验证码
     *
     *  {
     trantype: 'bgMsgSend',
     certificatetype: '0', //证件类型（H5调取app指令的时候会传入）
     certificateno: '120101198303093538', //身份证号（H5调取app指令的时候会传入）
     depositacctname: '何美福', //投资人姓名（H5调取app指令的时候会传入）
     depositacct: '银行卡号', //银行卡号（客户填写）
     mobiletelno: '13700000000' //银行卡绑定的手机号（客户填写）
     }

     *
     */
    public void getVerificationCodeFormServer(String certificatetype,String depositacctname, String phone ,String depositacct,String mobiletelno, BasePublicFundPresenter.PreSenterCallBack<String> preSenterCallBack){
        Map<String,Object> parms = new HashMap<>();
        parms.put("trantype","bgMsgSend");
        parms.put("certificatetype","0");//证件类型（H5调取app指令的时候会传入）
        parms.put("depositacctname","何美福");
        parms.put("depositacct","银行卡号");
        parms.put("mobiletelno","手机号");
        super.getFundDataFormJZ(parms,preSenterCallBack);
    }

}
