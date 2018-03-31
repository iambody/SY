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
 * Created by wangpeng on 18-2-2.
 */

class SellPUblicFundPresenter extends BasePublicFundPresenter {
    public SellPUblicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }

    public void sureSell(String fundcode, String largeredemptionflag, String transactionaccountid, String branchcode,
                         String tano, String fastredeemflag, String money, String payPassword,PreSenterCallBack preSenterCallBack) {
      /* {
            trantype: '520004',
                    applicationvol:'100000',//申请份额(必填 String)[用户手动填写]
                branchcode:'8866',//份额托管网点编号(必填 String)[H5调取app指令的时候会传入]
                custno:'152',//客户号(必填 String)[H5调取app指令的时候会传入]
                fundcode:'DC0010',//基金代码(必填 String)[H5调取app指令的时候会传入]
                largeredemptionflag:'1',//巨额赎回标志0-放弃超额部分 1-继续赎回[110051](必填 String)[固定传1]
                tano:'DC',//TA 代码 (必填 String)[H5调取app指令的时候会传入]
                transactionaccountid:'0108A00000229',//交易账号(必填 String)[H5调取app指令的时候会传入]
                taserialno:'',//TA 确认流水号(必须有，无值时用''占位)
                transactorcertno:'',//监护人证件号码，未成证年人交易必填(必须有,无值时用''占位)
                transactorcerttype:'',//监护人证件类型，未成年人交易必填[110047](必须有,无值时用''占位)
                transactorname:'',//监护人姓名，未成年人交易必填(必须有,无值时用''占位)

        }*/

        Map<String,String> parms = new HashMap<>();
        parms.put("custNo", AppManager.getPublicFundInf(getContext()).getCustNo());
        parms.put("fundCode",fundcode);
        parms.put("largeRedemptionFlag",largeredemptionflag);
        parms.put("transactionAccountId",transactionaccountid);
        parms.put("branchCode",branchcode);
        parms.put("taNo",tano);
        if(!BStrUtils.isEmpty(fastredeemflag)){
            parms.put("fastRedeemFlag",fastredeemflag);
        }
        parms.put("taserialNo","");
        parms.put("transactorCertNo","");
        parms.put("transactorCertType","");
        parms.put("transactorName","");

        parms.put("applicationVol",money); // 申请份额
        // TODO校验
        parms.put("isChktPasswd","1"); // 是否校验密码，这里固定写1
        parms.put("tPasswd",payPassword); // 交易密码


        super.handlerPublicFundResult(ApiClient.redeem(parms),preSenterCallBack);
      //  super.getFundDataFormJZ(parms,preSenterCallBack);
    }
}
