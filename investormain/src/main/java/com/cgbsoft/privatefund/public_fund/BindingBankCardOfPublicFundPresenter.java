package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import rx.functions.Action2;

/**
 * Created by wangpeng on 18-1-30.
 */

public class BindingBankCardOfPublicFundPresenter extends BasePresenterImpl {

    public BindingBankCardOfPublicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
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
    public void getVerificationCodeFormServer(String phone , Action2 action2){
      //  NetWork.post()
    }

}
