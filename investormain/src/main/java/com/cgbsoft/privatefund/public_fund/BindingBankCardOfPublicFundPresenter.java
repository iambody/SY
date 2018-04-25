package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.bean.publicfund.BankBranchInf;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void getBinidedBankList(String custno, PreSenterCallBack<String> preSenterCallBack) {
        super.handlerPublicFundResult(ApiClient.getUseableBankList(custno), preSenterCallBack);
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
    public void getVerificationCodeFormServer(String channelid, String phone, String bankCode, BasePublicFundPresenter.PreSenterCallBack<String> preSenterCallBack) {
        Map<String, String> parms = new HashMap<>();
       /* parms.put("certificateType", bindingBankCardBean.getCertificatetype());//证件类型（H5调取app指令的时候会传入）
        parms.put("certificateNo", bindingBankCardBean.getCertificateno());*/
        parms.put("channelId", channelid);
        // parms.put("depositAcctName", bindingBankCardBean.getDepositacctname());
        parms.put("depositAcct", bankCode);
        parms.put("mobileTelNo", phone);
        super.handlerPublicFundResult(ApiClient.getBindCardCaptcha(parms), preSenterCallBack);
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
        //  parms.put("custNo", BStrUtils.nullToEmpty(bindingBankCardBean.getCustNo()));
        parms.put("mobileNo", BStrUtils.nullToEmpty(phoneCode));
        parms.put("verificationCode", BStrUtils.nullToEmpty(verificationCode));
        //   parms.put("authenticateFlag", "");
        //  parms.put("bankname", BStrUtils.nullToEmpty(bankName));
        parms.put("channelId", BStrUtils.nullToEmpty(bindingBankCardBean.getChannelid()));
        //  parms.put("channelname", BStrUtils.nullToEmpty(bankName));
        parms.put("depositAcct", BStrUtils.nullToEmpty(bankCode));
        // parms.put("depositAcctName", BStrUtils.nullToEmpty(bindingBankCardBean.getDepositAcctName()));
        //   parms.put("depositName", BStrUtils.nullToEmpty(bindingBankCardBean.getDepositName()));
        parms.put("depositCity", ""); // 所在城市
        parms.put("depositProv", ""); // 所以省份
        //   parms.put("operOrg", BStrUtils.isEmpty(bindingBankCardBean.getOperOrg()) ? "9999" : bindingBankCardBean.getOperOrg());  //交易操作网点，写死9999就可以
        //  parms.put("tPasswd", BStrUtils.isEmpty(bindingBankCardBean.gettPasswd()) ? "" : bindingBankCardBean.gettPasswd());
        //  parms.put("certificateType", "0");

        parms.put("bankName", BStrUtils.nullToEmpty(bindingBankCardBean.getBankname()));
        parms.put("channelName", BStrUtils.nullToEmpty(bindingBankCardBean.getChannelname()));
        parms.put("paraType", BStrUtils.nullToEmpty(bindingBankCardBean.getParatype()));

     /*   //获取身份证号
        String certificateno = bindingBankCardBean.getCertificateNo();

        if (BStrUtils.isEmpty(certificateno)) {
            certificateno= AppManager.getPublicFundInf(getContext()).getCertificateNo();
        }

        parms.put("certificateNo", BStrUtils.isEmpty(certificateno)?"":certificateno);
*/
        handlerPublicFundResult(ApiClient.bindCard(parms), callBack);

        // super.getFundDataFormJZ(parms, callBack);
    }


    public void getBranchBankInfo(String keyWords, String channelid, PreSenterCallBack preSenterCallBack) {
        Map parms = new HashMap();
        parms.put("channelId", channelid);
        parms.put("paraCity", keyWords);
        super.handlerPublicFundResult(ApiClient.getBankBranch(parms), preSenterCallBack);
    }

    /**
     * 获取运营位
     */
    public void getBindCardOperationinf(PreSenterCallBack<String> preSenterCallBack) {
        addSubscription(ApiClient.getBindOrSetPwdOperationInf().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (null != preSenterCallBack) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        preSenterCallBack.even(obj.getString("result"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.printStackTrace();
                String errorCode = UNEXPECTED;
                if (error instanceof ApiException && "500".equals(((ApiException) error).getCode())) {
                    MToast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    errorCode = ((ApiException) error).getCode();
                }
                if (preSenterCallBack != null) {
                    preSenterCallBack.field(errorCode, error.getMessage());
                }
            }
        }));
    }


    /**
     * 根据银行卡号获取银行信息
     */
    public void getBanckInfByNumber(String bankNumber,PreSenterCallBack<BankBranchInf> preSenterCallBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put("depositAcct", bankNumber);
        getCompositeSubscription().add(ApiClient.getBanckinfByNumber(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (null != preSenterCallBack) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        String result=obj.getString("result");
                        preSenterCallBack.even(new Gson().fromJson(result,BankBranchInf.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.printStackTrace();
                String errorCode = UNEXPECTED;
                if (error instanceof ApiException && "500".equals(((ApiException) error).getCode())) {
                    MToast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    errorCode = ((ApiException) error).getCode();
                }
                if (preSenterCallBack != null) {
                    preSenterCallBack.field(errorCode, error.getMessage());
                }
            }
        }));

//        getBanckinfByNumber
    }
}
