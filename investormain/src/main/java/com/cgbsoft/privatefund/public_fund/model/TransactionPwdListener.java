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


    /**
     * 运营信息
     */
    public void getOperationSuccess(String str);

    /**
     * 运营信息失败
     */
    public void getOperationError(String error);
}
