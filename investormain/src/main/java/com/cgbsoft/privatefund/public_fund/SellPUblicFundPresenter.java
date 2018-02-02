package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.Map;

/**
 * Created by wangpeng on 18-2-2.
 */

class SellPUblicFundPresenter extends BasePublicFundPresenter {
    public SellPUblicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }

    public void sureSell(Map parms,PreSenterCallBack preSenterCallBack){
        super.getFundDataFormJZ(parms,preSenterCallBack);
    }


}
