package com.cgbsoft.privatefund.public_fund.model;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public interface TransactionPwdListener {
    /**
     * 请求数据
     */
    public void getTransactionPwdSuccess(String str);

    /**
     * 请求失败
     */

    public void getTransactionPwdError(String error);
}
